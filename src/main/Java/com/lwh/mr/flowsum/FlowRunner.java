package com.lwh.mr.flowsum;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import javax.xml.soap.Text;

/**
 * @author lwh
 * @date 2019/6/14 16:51
 */
public class FlowRunner extends Configured implements Tool {

    @Override
    public int run(String[] strings) throws Exception {

        Configuration conf=new Configuration();
        Job job=Job.getInstance(conf);

        job.setJarByClass(FlowRunner.class);

        job.setMapperClass(FlowSumMapper.class);
        job.setReducerClass(FlowSumReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        FileInputFormat.setInputPaths(job,new Path(strings[0]));
        FileOutputFormat.setOutputPath(job,new Path(strings[1]));

        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
       int res = ToolRunner.run(new Configuration(),new FlowRunner(),args);
       System.exit(res);
    }
}
