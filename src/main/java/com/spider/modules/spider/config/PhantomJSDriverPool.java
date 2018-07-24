package com.spider.modules.spider.config;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-19
 */
public class PhantomJSDriverPool extends GenericObjectPool<PhantomJSDriver> {

	public PhantomJSDriverPool(PooledObjectFactory<PhantomJSDriver> factory) {
		super(factory);
	}

	public PhantomJSDriverPool(PooledObjectFactory<PhantomJSDriver> factory, GenericObjectPoolConfig<PhantomJSDriver> config) {
		super(factory, config);
	}

	public PhantomJSDriverPool(PooledObjectFactory<PhantomJSDriver> factory, GenericObjectPoolConfig<PhantomJSDriver> config,
	                           AbandonedConfig abandonedConfig) {
		super(factory, config, abandonedConfig);
	}

	public PhantomJSDriver borrowPhantomJSDriver() throws Exception {
		long start = System.currentTimeMillis();
		PhantomJSDriver phantomJSDriver = this.borrowObject();
		System.out.println("获取PhantomJSDriver实际耗时：" + (System.currentTimeMillis() - start) + "毫秒");

		return phantomJSDriver;
	}
}
