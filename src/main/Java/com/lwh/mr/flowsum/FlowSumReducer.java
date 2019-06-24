package com.lwh.mr.flowsum;

import org.apache.hadoop.mapreduce.Reducer;

import javax.xml.soap.Text;
import java.io.IOException;

/**
 * @author lwh
 * @date 2019/6/14 16:51
 */
public class FlowSumReducer extends Reducer<Text,FlowBean,Text,FlowBean> {

    /**框架传递一组数据<1387777,{FlowBean,FlowBeanFlowBean}>**/
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        long u_flow_counter=0;
        long d_flow_counter=0;
        for (FlowBean flowBean : values) {
            u_flow_counter+=flowBean.getUp_flow();
            d_flow_counter+=flowBean.getD_flow();
        }
        context.write(key,new FlowBean(key.toString(),u_flow_counter,d_flow_counter));
    }
}
