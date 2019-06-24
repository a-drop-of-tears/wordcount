package com.lwh.mr.hdfs;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author lwh
 * @date 2019/6/22 17:41
 */
public class HdfsUtils {

    private static FileSystem fs;

    static {
        //读取classpath下的xxx-site.xml 配置文件，并解析其内容，封装到conf对象中
        Configuration conf=new Configuration();
        //也可以在代码中对conf中的配置信息进行手动设置，会覆盖掉配置文件中的读取的值
        conf.set("fs.defaultFS","hdfs://weekend110:9000/");
        try {
            fs= FileSystem.get(new URI("hdfs://weekend110:9000/"),conf,"hadoop");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件，比较底层的写法
     * @throws IOException
     */
    public void upload() throws IOException {
        Configuration configuration=new Configuration();
        configuration.set("fs.defaultFS","hdfs://weekend110:9000/");
        FileSystem fs=FileSystem.get(configuration);
        Path dst=new Path("hdfs://weekend110:9000/aa/qingshu.txt");
        FSDataOutputStream os = fs.create(dst);
        FileInputStream is = new FileInputStream("c:/qingshu.txt");
        IOUtils.copy(is,os);
    }

    /**
     * 上传文件，封装好的写法
     * @throws IOException
     */
    public void upload2() throws IOException {
        fs.copyFromLocalFile(new Path("c:/qingshu.txt"),new Path("hdfs://weekend110:9000/aaa/bbb/ccc/qingshu2.txt"));
    }

    /**
     * 下载文件
     * @throws IOException
     */
    public void download() throws IOException {
        fs.copyToLocalFile(new Path("hdfs://weekend110:9000/aa/qingshu2.txt"), new Path("c:/qingshu2.txt"));
    }

    public void listFiles() throws IOException {
        FileStatus[] listStatus = fs.listStatus(new Path("/"));
        for (FileStatus status : listStatus) {
            String name = status.getPath().getName();
            System.out.println(name+(status.isDir()?"is dir":"is file"));
        }
    }

    /**
     * 创建文件夹
     * @throws IOException
     */
    public void mkdir() throws IOException {
        fs.mkdirs(new Path("/aaa/bbb/ccc"));
    }

    /**
     * 创建文件夹
     * @throws IOException
     */
    public void rm() throws IOException {
        fs.delete(new Path("/aaa"),true);
    }


    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://weekend110:9000/");

        FileSystem fs = FileSystem.get(conf);

        FSDataInputStream is = fs.open(new Path("/jdk-7u65-linux-i586.tar.gz"));

        FileOutputStream os = new FileOutputStream("c:/jdk7.tgz");

        IOUtils.copy(is, os);
    }

}
