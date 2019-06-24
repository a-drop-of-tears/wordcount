package com.lwh.mr.areapartition;

import org.apache.hadoop.mapreduce.Partitioner;

import java.util.HashMap;

/**
 * @author lwh
 * @date 2019/6/14 21:21
 */
public class AreaPartitioner<KEY,VALUE> extends Partitioner<KEY,VALUE>{

    private static HashMap<String,Integer> areaMap=new HashMap<>();

    static {
        //从数据库查询手机归属地字典并放入hashMap中
        areaMap.put("135",0);
        areaMap.put("136",1);
        areaMap.put("137",2);
        areaMap.put("139",3);
    }

    @Override
    public int getPartition(KEY key, VALUE value, int i) {
        //从key中拿到手机号，查询手机归属地字典，不同省份返回不同的组号
        return areaMap.get(key.toString().substring(0,3))==null?5:areaMap.get(key.toString().substring(0,3));
    }
}
