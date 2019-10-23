package com.tools.hadoop.mr.secondarysort;

import lombok.Data;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@Data
public class OrderBean implements WritableComparable<OrderBean> {

    //用户ID
    private String userid;
    //年月
    //year+month -> 201408
    private String datetime;
    //标题
    private String title;
    //单价
    private double unitPrice;
    //购买量
    private int purchaseNum;
    //商品ID
    private String produceId;

    public OrderBean() {
    }

    public OrderBean(String userid, String datetime, String title, double unitPrice, int purchaseNum, String produceId) {
        super();
        this.userid = userid;
        this.datetime = datetime;
        this.title = title;
        this.unitPrice = unitPrice;
        this.purchaseNum = purchaseNum;
        this.produceId = produceId;
    }

    //key的比较规则
    public int compareTo(OrderBean other) {
        //OrderBean作为MR中的key；如果对象中的userid相同，即ret1为0；就表示两个对象是同一个用户
        int isEquals = this.userid.compareTo(other.userid);

        if (isEquals == 0) {
            //如果userid相同，比较年月
            String thisYearMonth = this.getDatetime();
            String otherYearMonth = other.getDatetime();
            int isEqualsWithDate = thisYearMonth.compareTo(otherYearMonth);

            if(isEqualsWithDate == 0) {//若datetime相同
                //如果userid、年月都相同，比较单笔订单的总开销
                Double thisTotalPrice = this.getPurchaseNum()*this.getUnitPrice();
                Double oTotalPrice = other.getPurchaseNum()*other.getUnitPrice();
                //总花销降序排序；即总花销高的排在前边
                return -thisTotalPrice.compareTo(oTotalPrice);
            } else {
                //若datatime不同，按照datetime升序排序
                return isEqualsWithDate;
            }
        } else {
            //按照userid升序排序
            return isEquals;
        }
    }

    /**
     * 序列化
     * @param dataOutput
     * @throws IOException
     */
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(userid);
        dataOutput.writeUTF(datetime);
        dataOutput.writeUTF(title);
        dataOutput.writeDouble(unitPrice);
        dataOutput.writeInt(purchaseNum);
        dataOutput.writeUTF(produceId);
    }

    /**
     * 反序列化
     * @param dataInput
     * @throws IOException
     */
    public void readFields(DataInput dataInput) throws IOException {
        this.userid = dataInput.readUTF();
        this.datetime = dataInput.readUTF();
        this.title = dataInput.readUTF();
        this.unitPrice = dataInput.readDouble();
        this.purchaseNum = dataInput.readInt();
        this.produceId = dataInput.readUTF();
    }
}
