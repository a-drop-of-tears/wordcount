package com.lwh.mr.flowsum;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * FlowBean 是我们自定义的一种数据类型，要在hadoop的各个节点之间传输，应该遵循hadoop的序列化机制
 * 就必须实现Writable接口
 * @author lwh
 * @date 2019/6/14 16:50
 */
public class FlowSumMapper extends Mapper<LongWritable, Text,Text,FlowBean> {

    /**
     * 拿到日志中的一行数据，切分各个字段，抽取出我们需要的字典：手机号，上行流量，下行流量，然后封装成kv发送出去
     * @param key
     * @param value
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line=value.toString();
        String[] values=StringUtils.split(line,"\t");

        String phoneNB=values[1];
        long u_flow=Long.parseLong(values[7]);
        long d_flow=Long.parseLong(values[8]);

        context.write(new Text(phoneNB),new FlowBean(phoneNB,u_flow,d_flow));
    }
}
