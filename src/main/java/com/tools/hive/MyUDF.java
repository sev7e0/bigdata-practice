package com.tools.hive;


import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.util.HashMap;
import java.util.Objects;

public class MyUDF extends UDF {

    @Description(
            name="用户自定义函数",
            value="将json转化为自定义表结构",
            extended="select MyUDF(data,'movie') from json;"
    )
    public Text evaluate(final String input, String key) {
        if(Objects.isNull(input)){
            return null;
        }
        HashMap<String,String> map = JSONObject.parseObject(input, HashMap.class);
        return new Text(map.get(key));
    }

    public static void main(String[] args) {
        MyUDF myUDF = new MyUDF();
        Text movie = myUDF.evaluate("{'movie':'1193','rate':'5','timeStamp':'978300760','uid':'1'}", "movie");
        System.out.println(movie.toString());
    }

}
