package com.tools.hive;


import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.io.Text;

import java.util.Objects;

public class MyUDF extends GenericUDF {

	@Description(
		name = "用户自定义函数",
		value = "将json转化为自定义表结构",
		extended = "select MyUDF(data,'movie') from json;"
	)
	public Text evaluate(final String input, String key) {
		if (Objects.isNull(input)) {
			return null;
		}
		JSONObject jsonObject = JSONObject.parseObject(input);
		return new Text(String.valueOf(jsonObject.get(key)));
	}

	public static void main(String[] args) {
		MyUDF myUDF = new MyUDF();
		Text movie = myUDF.evaluate("{'movie':'1193','rate':'5','timeStamp':'978300760','uid':'1'}", "movie");
		System.out.println(movie.toString());
	}

	@Override
	public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
		return null;
	}

	@Override
	public Object evaluate(DeferredObject[] arguments) throws HiveException {
		return null;
	}

	@Override
	public String getDisplayString(String[] children) {
		return null;
	}
}
