/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.config;

import java.lang.reflect.Constructor;

import org.springframework.beans.BeansException;
import org.springframework.lang.Nullable;

/**
 * 扩展 InstantiationAwareBeanPostProcessor 接口，添加一个回调用于预测已处理 bean 的最终类型。
 * 注:该接口是一个特殊用途的接口，主要用于框架内部使用。
 * 通常，应用程序提供的后置处理器应该简单地实现普通的 BeanPostProcessor 接口，或者从
 * InstantiationAwareBeanPostProcessorAdapter 类派生。即使在早些版本中，也可以向该接口添加新方法。
 * @author Juergen Hoeller
 * @see InstantiationAwareBeanPostProcessorAdapter
 * @since 2.0.3
 */
public interface SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor {
	
	/**
	 * 预测Bean的类型，返回第一个预测成功的Class类型，如果不能预测返回null
	 * @param beanClass the raw class of the bean
	 * @param beanName  the name of the bean
	 * @return the type of the bean, or {@code null} if not predictable
	 * @throws org.springframework.beans.BeansException in case of errors
	 */
	@Nullable
	default Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
		return null;
	}
	
	/**
	 * 选择合适的构造器，比如目标对象有多个构造器，在这里可以进行一些定制化，选择合适的构造器
	 * beanClass参数表示目标实例的类型，beanName是目标实例在 Spring 容器中的 name
	 * 返回值是个构造器数组，如果返回 null，会执行下一个 PostProcessor的determineCandidateConstructors 方法;
	 * 否则选取该 PostProcessor 选择的构造器
	 * @param beanClass the raw class of the bean (never {@code null})
	 * @param beanName  the name of the bean
	 * @return the candidate constructors, or {@code null} if none specified
	 * @throws org.springframework.beans.BeansException in case of errors
	 */
	@Nullable
	default Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName)
			throws BeansException {
		
		return null;
	}
	
	/**
	 * 获得提前暴露的 bean 引用。主要用于解决循环引用的问题,只有单例对象才会调用此方法
	 * @param bean     the raw bean instance
	 * @param beanName the name of the bean
	 * @return the object to expose as bean reference
	 * (typically with the passed-in bean instance as default)
	 * @throws org.springframework.beans.BeansException in case of errors
	 */
	default Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
		return bean;
	}
	
}
