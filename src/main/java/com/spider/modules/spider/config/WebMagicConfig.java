package com.spider.modules.spider.config;

import java.util.Objects;
import javax.annotation.PostConstruct;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-25
 */
@Configuration
public class WebMagicConfig {

	@Value("${webmagic.selenuim_config}")
	private String selenuimConfig;

	/**
	 * PostConstruct在构造函数之后执行
	 */
	@PostConstruct
	public void initSelenuimConfig() {
		System.setProperty("selenuim_config",
				Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath()
						+ this.selenuimConfig);
	}

	@Bean(name = "phantomJSDriverPool")
	@Autowired
	public PhantomJSDriverPool newPool(PhantomJSDriverFactory phantomJSDriverFactory) {
		GenericObjectPoolConfig<PhantomJSDriver> conf = new GenericObjectPoolConfig<>();
		//最大空闲和最大容量一致，防止自动销毁对象
		conf.setMaxTotal(8);
		conf.setMaxIdle(8);
		conf.setLifo(false);
		conf.setJmxEnabled(false);
		return new PhantomJSDriverPool(phantomJSDriverFactory, conf);
	}

}
