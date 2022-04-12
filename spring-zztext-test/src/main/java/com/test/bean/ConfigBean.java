package com.test.bean;

import com.test.aspect.LogAnnotationAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Configuration // 申明配置类
@EnableAspectJAutoProxy // 启用AspectJ注解自动配置,proxyTargetClass用于指定是否强制使用cglib代理
@ComponentScan("com.test") // 启用包扫描,不写参数表示全局扫描
//@Import(value = LogAnnotationAspect.class) // 包扫描的时候就会扫描到 LogAnnotationAspect.java 这里不需要再引入
public class ConfigBean {
	
	@Bean
	public Person person(){
		Person person = new Person();
		/*person.setName("a1");
		person.setSex("18");*/
		return person;
	}
}
