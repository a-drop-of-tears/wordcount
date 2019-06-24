package com.lwh.mr.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author lwh
 * @date 2019/6/15 18:49
 */
public class HdfsUtilHA {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        Configuration conf=new Configuration();
        FileSystem fs=FileSystem.get(new URI("hdfs://ns1/"),conf,"hadoop");
        fs.copyFromLocalFile(new Path("c://test.txt"),new Path("hdfs://ns1/"));
    }
}
