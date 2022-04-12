package com.test.mytest;

import com.test.bean.ConfigBean;
import com.test.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigBean.class) // 加载spring容器配置文件
public class JunitTest {
	
	private final static Logger logger = LoggerFactory.getLogger(JunitTest.class);
	
	@Autowired
	private UserService userService;
	
	@Test
	public void testAop() {
		logger.info(userService.addUser("owen", "7788") + "");
	}
	
}
