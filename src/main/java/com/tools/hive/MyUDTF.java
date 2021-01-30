package com.tools.hive;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;

/**
 * Title:  MyUDTF.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-06 22:53
 **/

public class MyUDTF extends GenericUDTF {

	@Override
	public StructObjectInspector initialize(StructObjectInspector argOIs) throws UDFArgumentException {
		return super.initialize(argOIs);
	}

	@Override
	public void process(Object[] args) throws HiveException {

	}

	@Override
	public void close() throws HiveException {

	}
}
