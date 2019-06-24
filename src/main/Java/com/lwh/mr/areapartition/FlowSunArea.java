package com.lwh.mr.areapartition;

import com.lwh.mr.flowsort.SortMR;
import com.lwh.mr.flowsum.FlowBean;
import com.lwh.mr.flowsum.FlowSumReducer;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 对流量原始日志进行流量统计，将不同省份的用户统计结果输出到不同文本
 * 需要自定义改造两个机制：
 * 1、改造分区的逻辑，自定义一个partitioner
 * 2、自定义reducer task的并发任务数（对应文件输出的个数）
 * @author lwh
 * @date 2019/6/14 21:13
 */
public class FlowSunArea {

    private static class FlowSumAreaMapper extends Mapper<LongWritable, Text,Text, FlowBean>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line=value.toString();
            String[] fields= StringUtils.split(line,"\t");
            String phoneNB=fields[0];
            long u_flow=Long.parseLong(fields[1]);
            long d_flow=Long.parseLong(fields[2]);
            context.write(new Text(phoneNB),new FlowBean(phoneNB,u_flow,d_flow));
        }
    }

    private static class FlowSumAreaReducer extends Reducer<Text,FlowBean,Text,FlowBean>{
        @Override
        protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
            long u_flow_counter=0;
            long d_flow_counter=0;
            for (FlowBean flowBean : values) {
                u_flow_counter+=flowBean.getUp_flow();
                d_flow_counter+=flowBean.getD_flow();
            }
            context.write(key,new FlowBean(key.toString(),u_flow_counter,d_flow_counter));
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf=new Configuration();
        Job job=Job.getInstance(conf);

        job.setJarByClass(SortMR.class);

        job.setMapperClass(FlowSumAreaMapper.class);
        job.setReducerClass(FlowSumReducer.class);

        //设置自定义的分组定义
        job.setPartitionerClass(AreaPartitioner.class);

        //自定义reducer task的并发任务数（对应文件输出的个数）根据分组来设定 默认reducer只有1个
        job.setNumReduceTasks(5);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        job.waitForCompletion(true);
    }
}
