package com.test.mytest;

import com.test.bean.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Ctrl + Alt + Shift + u 查看类图结构
 */
public class MyTest {

	private final static Logger logger = LoggerFactory.getLogger(MyTest.class);

	public static void main(String[] args) {
//		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigBean.class);
//		Person person = (Person) context.getBean("person");
//		logger.info("Person:" + person);
//
//		UserService userService = (UserService) context.getBean("userService");
//		logger.info(userService.addUser("aaa", "111") + "");

		ApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("application.xml");
		Person person = (Person) classPathXmlApplicationContext.getBean("person");
		System.out.println("--------"+person);
	}

}
