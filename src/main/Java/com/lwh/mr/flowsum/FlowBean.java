package com.lwh.mr.flowsum;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author lwh
 * @date 2019/6/14 16:50
 */
public class FlowBean  implements WritableComparable<FlowBean> {

    private  String phoneNB;
    private  Long up_flow;
    private  Long d_flow;
    private  Long s_flow;

    /**反序列化的反射需要*/
    public FlowBean() {
    }

    /**
     * 为了对象数据的初始化方便，加入一个带参的构造函数
     */
    public FlowBean(String phoneNB, Long up_flow, Long d_flow) {
        this.phoneNB = phoneNB;
        this.up_flow = up_flow;
        this.d_flow = d_flow;
        this.s_flow=this.up_flow+this.d_flow;
    }

    public String getPhoneNB() {
        return phoneNB;
    }

    public void setPhoneNB(String phoneNB) {
        this.phoneNB = phoneNB;
    }

    public Long getUp_flow() {
        return up_flow;
    }

    public void setUp_flow(Long up_flow) {
        this.up_flow = up_flow;
    }

    public Long getD_flow() {
        return d_flow;
    }

    public void setD_flow(Long d_flow) {
        this.d_flow = d_flow;
    }

    public Long getS_flow() {
        return s_flow;
    }

    public void setS_flow(Long s_flow) {
        this.s_flow = s_flow;
    }

    @Override
    public String toString() {
        return "FlowBean{" +
                "phoneNB='" + phoneNB + '\'' +
                ", up_flow=" + up_flow +
                ", d_flow=" + d_flow +
                ", s_flow=" + s_flow +
                '}';
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(phoneNB);
        dataOutput.writeLong(up_flow);
        dataOutput.writeLong(d_flow);
        dataOutput.writeLong(s_flow);
    }

    /**
     * 从数据流中反序列出对象数据
     * 从数据流中读出对象字段时2，必须跟序列化时的顺序保持一致
     * @param dataInput
     * @throws IOException
     */
    @Override
    public void readFields(DataInput dataInput) throws IOException {
        phoneNB=dataInput.readUTF();
        up_flow=dataInput.readLong();
        d_flow=dataInput.readLong();
        s_flow=dataInput.readLong();
    }

    @Override
    public int compareTo(FlowBean o) {
        return this.s_flow>o.getS_flow()?-1:1;
    }
}
