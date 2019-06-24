package com.lwh.mr.wordcount;


import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author lwh
 * @date 2019/6/13 20:31
 * 4个泛型中，前两个是指定mapper输入数据的类型，KEYIN是输入的key的类型，VALUEIN是输入的value的数据类型
 * map和reduce的数据输入输出都是以key-value的形式封装
 *
 * 默认情况下，（mapper的输入数据是框架读取文件后传入的）框架传递给我们的mapper的输入数据中，key是要处理的文本中一行的起始偏移量，这一行的内容作为value
 * 为什么不适用jdk提供的序列化（因为jdk提供的序列化会产生很多无用的数据，加大了网络传输的压力，降低了网络传输的速率）
 * LongWritable是实现了hadoop定义的序列化的long类型
 * Text是实现了hadoop定义的序列化的String类型
 */
public class WCMapper extends Mapper<LongWritable, Text,Text,LongWritable> {

    /**
     * mapreduce框架每读一行数据就调用一次改方法
     * @param key mapper的输入数据的key
     * @param value mapper的输入数据的value
     * @param context 上下文
     * @throws IOException io异常
     * @throws InterruptedException 中断异常
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //具体业务逻辑就写在这个方法体中，而且我们业务要处理的数据已经被框架传递进来在方法的参数中key-value
        //key是这一行数据的起始偏移量，value是一行的文本内容

        //将这一行的内容转换成String类型
        String line = value.toString();
        //对这一行的文本按特定分隔符切分
        String[] words = StringUtils.split(line, " ");
        for (String word : words) {
            //context会将mapper输出的数据传给reduce（我们不需要关心reduce在哪里）
            context.write(new Text(word),new LongWritable(1L));
        }
    }
}
