package com.lwh.mr.wordcount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

import javax.xml.soap.Text;
import java.io.IOException;

/**
 * @author lwh
 * @date 2019/6/13 20:58
 */
public class WCReduce extends Reducer<Text, LongWritable,Text,LongWritable> {

    /**
     * 框架在map处理完成之后，将所有key-value缓存起来，进行分组。然后传递一个组<key,values{}>调用一次reduce方法
     * <hello,{1,1,1,1,1,1....}>
     */
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        long count=0;
        //遍历values进行累加
        for (LongWritable value : values) {
            count+=value.get();
        }
        //输出一个单词的统计结果
        context.write(key,new LongWritable(count));
    }
}
