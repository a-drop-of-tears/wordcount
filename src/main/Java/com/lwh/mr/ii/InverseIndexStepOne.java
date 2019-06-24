package com.lwh.mr.ii;

import com.lwh.mr.flowsum.FlowBean;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 倒排序索引步骤一 job
 * @author lwh
 * @date 2019/6/15 12:44
 */
public class InverseIndexStepOne {

    private static class StepOneMapper extends Mapper<LongWritable, Text,Text, LongWritable>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line=value.toString();
            String[] values= StringUtils.split(line,"\t");
            FileSplit inputSplit=(FileSplit) context.getInputSplit();
            String fileName=inputSplit.getPath().getName();
            for (String field : values) {
                context.write(new Text(field+"-->"+fileName),new LongWritable(1));
            }
        }
    }

    private static class StepOneReduce extends Reducer<Text,LongWritable,Text,LongWritable>{
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            long count=0;
            for (LongWritable value : values) {
                count+=value.get();
            }
            context.write(key,new LongWritable(count));
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf=new Configuration();
        Job job=Job.getInstance(conf);

        job.setJarByClass(InverseIndexStepOne.class);

        job.setMapperClass(StepOneMapper.class);
        job.setReducerClass(StepOneReduce.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        FileInputFormat.setInputPaths(job,new Path(args[0]));
        Path output=new Path(args[1]);
        FileSystem fs=FileSystem.get(conf);
        if(fs.exists(output)){
            fs.delete(output,true);
        }
        FileOutputFormat.setOutputPath(job,output);
        job.waitForCompletion(true);
    }
}
