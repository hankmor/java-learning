package com.belonk.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Test;

/**
 * Created by sun on 2017/10/18.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class FastJsonTest {
	//~ Static fields/initializers =====================================================================================


	//~ Instance fields ================================================================================================


	//~ Constructors ===================================================================================================


	//~ Methods ========================================================================================================
	@Test
	public void testToStr() {
		Obj obj = new Obj();
		obj.setName("张三");
		// 默认对象的null属性省略不输出
		System.out.println(JSON.toJSONString(obj));
		// {"name":"张三"}
	}

	@Test
	public void testWithFeather() {
//        SerializerFeature	描述
//        WriteNullListAsEmpty	将Collection类型字段的字段空值输出为[]
//        WriteNullStringAsEmpty	将字符串类型字段的空值输出为空字符串 ""
//        WriteNullNumberAsZero	将数值类型字段的空值输出为0
//        WriteNullBooleanAsFalse	将Boolean类型字段的空值输出为false
		Obj obj = new Obj();
		obj.setName("张三");
		String json = JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
		System.out.println(json);
		// {"age":null,"dept":null,"name":"张三"}
		json = JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero);
		System.out.println(json);
		// {"age":0,"dept":null,"name":"张三"}
	}
}
