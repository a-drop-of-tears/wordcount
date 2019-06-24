package com.lwh.mr.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author lwh
 * @date 2019/6/13 21:06
 * 描述一个特定的一个作业，
 * 比如，该作业使用哪个类作为逻辑处理中的map，哪个作为reduce
 * 还可以指定该作业要处理的数据所在的路径
 * 还可以指定该作业输出的结果放在哪个路径
 * 等等
 */
public class WCRunner {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        //设置整个job所用的那些类在哪个jar下（或者在哪个位置上）
        //通过WCRunner的类加载器找到所在的位置
        job.setJarByClass(WCRunner.class);

        //本job使用的mapper和reduce的类
        job.setMapperClass(WCMapper.class);
        job.setReducerClass(WCReduce.class);

        //指定reduce的输出key-value类型（可同时对mapper和reduce进行设置）
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //指定mapper的输出key-value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        //指定要处理的输入数据的存放路径（需要存在）
        FileInputFormat.setInputPaths(job,new Path("/wc/input/"));
        //本地运行模式
        FileInputFormat.setInputPaths(job,new Path("c:/wc/input/"));
        //指定处理结果的输出数据存放路径（不需要存在）
        FileOutputFormat.setOutputPath(job,new Path("/wc/output/"));
        //本地运行模式
        FileOutputFormat.setOutputPath(job,new Path("c:/wc/output/"));


        //将job提交给集群运行 运行过程会提示出来
        job.waitForCompletion(true);
        //启动一个进程RunJar
    }

    /**
     * job的提交逻辑及yarn框架的技术机制
     *
     * yarn{resourceManage,nodeManage被ResourceManage管理}只做资源调度
     *  job.waitForCompletion(true);
     *  //启动一个进程RunJar
     * （1）RunJar进程会向resourceManage申请执行一个Job
     * （2）resourceManage会返回job相关资源提交路径staging-dir和为本job产生一个JobID
     * （3）RunJar会提交资源到HDFS的/tmp/xx/xx/yarn-staging/JobID/目录下
     *  (4)RunJar现货resourceManage回报提交的结果
     * （5）resourceManage将本次Job加入任务队列
     * （6）NodeManage会发送心跳包检测有没有新的任务，有的话会去领取该任务
     * （7）yarn会给NodeManage产生一个相当于虚拟机的容器（container），里面封装了本次job运行所需的资源如 cpu资源，磁盘访问以及所需的文件资源等等。
     *  (8)MapReduce框架里的MRAppMaster（动态产生的resourceManage随机找到一台NodeMange启动）管理MapReduce的逻辑，它由yarn框架的resourceManage启动
     *  (9)MRAppMaster向resourceManage注册并请求一些信息（刚为NodeManage分配的资源在哪里）
     *  （10）MRAppMaster会找到NodeManage并启动Map task 是一个进程（名字yarnChild（动态产生的））并监控它的状态 若执行缓慢或GG则会另找一台机运行，两台同时运行看谁先运行完
     *  (11)MRAppMaster启动Reduce task（名字yarnChild（动态产生的））并监控它的状态 若执行缓慢或GG则会另找一台机运行，两台同时运行看谁先运行完
     *  （12）Job执行完会告诉resourceManage并回收资源
     */


    /**
     * MR程序的几种提交运行模式
     */
}
