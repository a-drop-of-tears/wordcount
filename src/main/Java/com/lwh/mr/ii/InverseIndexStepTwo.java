package com.lwh.mr.ii;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 倒排序步骤二
 * @author lwh
 * @date 2019/6/15 13:25
 */
public class InverseIndexStepTwo {
    public static class StepTwoMapper extends Mapper<Text, LongWritable,Text,Text>{
        @Override
        protected void map(Text key, LongWritable value, Context context) throws IOException, InterruptedException {
            String line=value.toString();
            String[] fields= StringUtils.split(line,"\t");
            String[] words=StringUtils.split(fields[0],"-->");
            String word=words[0];
            String fileName=words[2];
            long count=Long.parseLong(fields[1]);

            context.write(new Text(word),new Text(fileName+"-->"+count));
        }
    }

    public static class StepTwoReducer extends Reducer<Text,Text,Text,Text>{
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            StringBuilder result=new StringBuilder();
            for (Text value : values) {
                result.append(value);
            }
            context.write(key,new Text(result.toString()));
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf=new Configuration();
        Job job=Job.getInstance(conf);

        job.setJarByClass(InverseIndexStepOne.class);

        job.setMapperClass(StepTwoMapper.class);
        job.setReducerClass(StepTwoReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

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
