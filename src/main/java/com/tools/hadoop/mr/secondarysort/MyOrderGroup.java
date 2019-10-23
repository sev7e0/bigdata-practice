package com.tools.hadoop.mr.secondarysort;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class MyOrderGroup extends WritableComparator {

    public MyOrderGroup(){
        // 标识当前的key为orderbean
        super(OrderBean.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        OrderBean aOrderBean = (OrderBean)a;
        OrderBean bOrderBean = (OrderBean)b;

        String aUserId = aOrderBean.getUserid();
        String bUserId = bOrderBean.getUserid();
        //userid、年、月相同的，作为一组
        int ret1 = aUserId.compareTo(bUserId);
        if(ret1 == 0) {//同一用户
            //年月也相同返回0，在同一组；
            return aOrderBean.getDatetime().compareTo(bOrderBean.getDatetime());
        } else {
            return ret1;
        }
    }
}
