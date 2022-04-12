package com.test.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 日志切面类
 */
@Component // 注入依赖
@Aspect // 定义切面类
public class LogAnnotationAspect {
	
	private final static Logger logger = LoggerFactory.getLogger(LogAnnotationAspect.class);
	
	@SuppressWarnings("unused")
	/*
	 * 第一个*代表方法返回值 ..*表示扫描到方法级别 .*是任意方法(在这里) (..)方法的任意参数
	 */
	// 定义切入点,提供一个方法,这个方法的名字就是改切入点的id
	@Pointcut("execution(* com.test.service.*.*(..))")
	private void allMethod() {
	}
	
	// 1、前置通知： 在目标方法开始之前执行（就是要告诉该方法要在哪个类哪个方法前执行）
	// @Before("execution(public int com.test.service.*.*(int ,int))")
	@Before("allMethod()")
	public void beforeMethod(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getName();
		logger.info("[注解-前置通知]:" + className + "类的" + methodName + "方法开始了," +
				"方法的参数值为:" + JSON.toJSONString(joinPoint.getArgs()));
	}
	
	// 2、后置通知：在目标方法执行后（无论是否发生异常）,执行的通知
	// 注意,在后置通知中还不能访问目标执行的结果!!!,执行结果需要到返回通知里访问
	// @After("execution(* com.test.service.*.*(..))")
	@After("allMethod()")
	public void afterMethod(JoinPoint joinPoint) {
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		logger.info("[注解-后置通知]:" + className + "类的" + methodName + "不管是否正常执行,一定会返回的");
	}
	
	// 无论连接点是正常返回还是抛出异常, 后置通知都会执行. 如果只想在连接点返回的时候记录日志, 应使用返回通知代替后置通知.
	
	// 3、返回通知:在方法正常结束后执行的代码,返回通知是可以访问到方法的返回值的！！！
	// @AfterReturning(pointcut = "execution(* com.test.service.*.*(..))", returning = "result")
	@AfterReturning(value = "allMethod()", returning = "result")
	public void afterReturning(JoinPoint joinPoint, Object result) {
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		logger.info("[注解-返回通知]:" + className + "类的" + methodName + "方法正常结束了,返回值是" + result);
	}
	
	// 4、异常通知：在目标方法出现异常
	/* 时会执行的代码,可以访问到异常对象：且可以指定在出现特定异常时在执行通知!!,如果是修改为nullPointerException里,
	 * 只有空指针异常才会执行,此处将 except 的类型声明为Throwable,意味着对目标方法抛出的异常不加限制*/
	// @AfterThrowing(pointcut = "execution(* com.test.service.*.*(..))", throwing = "except")
	@AfterThrowing(value = "allMethod()", throwing = "except")
	public void afterThrowing(JoinPoint joinPoint, Exception except) {
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		logger.info("[注解-异常通知]:" + className + "类的" + methodName + "方法执行时遇见异常了" + except);
	}
	
	/**
	 * 5、环绕通知 需要携带 ProceedingJoinPoint 类型的参数. 环绕通知类似于动态代理的全过程:
	 * ProceedingJoinPoint 类型的参数可以决定是否执行目标方法. 且环绕通知必须有返回值, 返回值即为目标方法的返回值
	 */
	//@Around("execution(* com.test.service.*.*(..))")
	@Around("allMethod()")
	public Object aroundMethod(ProceedingJoinPoint pjd) {
		Object result = null;
		String className = pjd.getTarget().getClass().getName();
		String methodName = pjd.getSignature().getName();
		try {
			// 前置通知
			logger.info("[注解-前置通知]:" + className + "类的" + methodName + "方法开始了");
			// 执行目标方法
			result = pjd.proceed();
			// 返回通知
			logger.info("[注解-返回通知]: " + className + "类的" + methodName + "方法正常结束了,返回值是" + result);
		} catch (Throwable e) {
			// 异常通知
			logger.info("[注解-异常通知]: " + className + "类的" + methodName + "方法执行时遇见异常了" + e);
			throw new RuntimeException(e);
		}
		// 后置通知
		logger.info("[注解-后置通知]: " + className + "类的" + methodName + "不管是否正常执行,一定会返回的");
		return result;
	}
}