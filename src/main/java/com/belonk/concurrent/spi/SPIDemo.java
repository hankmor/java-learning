package com.belonk.concurrent.spi;

import java.util.ServiceLoader;

/**
 * SPI: service provider interface，是一种IOC思想，定义好接口，并将将类的实现放到配置文件中，通过动态加载的方式，而不是直接创建类实例，以达到
 * 解耦和可扩展的目的。
 * <p>
 * Created by sun on 2020/7/28.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class SPIDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		// 使用ServiceLoader加载实现类，这些类配置在classpath下的META-INF/services目录下的[接口全限定名]文件中，这里文件名为
		// com.belonk.concurrent.spi.Reader，内容为实现类全限定名称：
		// com.belonk.concurrent.spi.MyFileReader
		// com.belonk.concurrent.spi.MyInputStreamReader
		ServiceLoader<Reader> load = ServiceLoader.load(Reader.class);
		for (Reader reader : load) {
			System.out.println(reader.read());
		}
	}
}