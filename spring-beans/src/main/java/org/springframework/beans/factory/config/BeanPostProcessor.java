/*
 * Copyright 2002-2019 the original author or authors.
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

import org.springframework.beans.BeansException;
import org.springframework.lang.Nullable;

/**
 * 后置处理器接口,系统按默认的名称有7个分别为:
 * 1:ApplicationcontextAwareProcessor 直接实现了 BeanPostProcessor
 * 用来直接处理组件是否实现了 ApplicationcontextAware 接口的作用,往容器中注册回调 Applicationcontext 接口
 * 起作用的接口:postProcessBeforetnitializatiah()方法来进行回调 aware 接口的执行时间：
 * Bean进行了调用了沟造方法之后,初始化之前
 *
 * 2:CommonAnnotationBeanPostProcessor
 * extends: InitDestroyAnnotationBeanPostProcessor 用于处理 @Postcust @Predestory 注解的
 * implements: InstantiationAwareBeanPostProcessor 实例化执行
 * 用来处理JSR250规范注解的.......
 *
 * 3:AutowiredAnnotationBeanPostProcessor
 * extends: InstantiationAwareBeanPostProcessorAdapter
 * implement: MergedBeanDefinitionPostProcessor 用来解析 bean 之间的 @Autowired 注解
 *
 * 4: RequiredAnnotationBeanPostProcessor 处理 reqireud 屑性的
 * extends: InstantiationAwareBeanPostProcessorAdapter
 * implement: MergedBeanDefinitionPostProcessor 用来解析 bean 之间的 @Autowired 注解
 *
 *
 *
 *
 *
 *
 *
 * 博客 https://www.cnblogs.com/zzq-include/p/12228461.html
 * spring的9个地方调用了5次后置处理器的详细情况
 * 其中的createBean方法中就有bean的处理器。
 *
 * @BeanPostProcess 只是顶层处理器，相当于一个最基本的后置处理器它会贯穿所有 spring 的
 * bean 初始化时的阶段，会在 initializationBean 中调用。
 *
 * 实际上还有很多后置处理器的更多具体实现：
 *
 * 第一个方法：resolveBeforeInstantiation ，获取所有后置处理器，判断是否为InstantiationAwareBeanPostProcessor 实现类型
 * 调用的方法是：postProcessBeforeInstantiation 这个后置处理器很关键，InstantiationAwareBeanPostProcessor 有3个方法，
 * 第一个方法postProcessBeforeInstantiation如果你直接返回一个“自建对象”的话，那spring上下文直接就会把你的这个对象放入容器中，
 * 并执行 BeanPostProcessor 的 postProcessAfterInitialization 方法。
 * 如果第一个方法返回为null，则spring创建bean的流程会继续执行，会在 populateBean 方法中继续调用
 * postProcessAfterInstantiation 和 postProcessPropertyValues 来进行属性的装配。
 * 如果这里出现了aop的切面类，就会有 InstantiationAwareBeanPostProcessor 的子处理器进行类的过滤，出现 @AspectJ 的类
 * 标记为不需要代理的类，会被放入map中。
 *
 * 第二个方法：在createBeanInstance中的determineConstructorsFromBeanPostProcessors
 * 方法中，判断是否为SmartInstantiationAwareBeanPostProcessor类型的后置处理器
 * 调用的方法是：determineCandidateConstructors，这个方法用来推断构造函数，
 * 实际使用的实现 SmartInstantiationAwareBeanPostProcessor 接口的 AutowiredAnnotationBeanPostProcess 后置处理器去做的。
 *
 * 第三个方法：在 createBeanInstance 中的 applyMergedBeanDefinitionPostProcessors 方法中，判断为
 * MergedBeanDefinitionPostProcessor
 * 调用的方法是：postProcessMergedBeanDefinition，用来缓存注解信息。
 *
 * 第四个方法：在createBeanInstance中的getEarlyBeanReference方法中，判断是否为 SmartInstantiationAwareBeanPostProcessor
 * 调用的方法是：getEarlyBeanReference 这个方法是来解决循环依赖问题的。这里很重要，要详细的分析
 *
 * 第五个方法：在 populateBean 中的会调用 InstantiationAwareBeanPostProcessor 这个处理器，
 * 调用的方法是：postProcessAfterInstantiation
 *
 * 第六个方法：在 populateBean 中的又会调用 InstantiationAwareBeanPostProcessor 这个处理器，但是
 * 调用的方法是：postProcessPropertyValues
 *
 * 第七个方法：在 initializationBean 中调用的是 BeanPostProcess 的 postProcessBeforInitialization 方法
 *
 * 第八个方法：在 initializationBean 中调用的是 BeanPostProcess 的 postProcessAfterInitialization 方法
 *
 * 第九个方法：是销毁时的方法
 */
public interface BeanPostProcessor {
	
	/**
	 * 将{@code BeanPostprocessor}应用到给定的新bean实例
	 * <i>before /i>
	 * 任何bean初始化回调(如InitializingBean的{@code ofterPropertiesSet})或者自定义初始化方法)。
	 * bean中已经填充了属性值。返回的bean实例可能是原始
	 * <p>的包装器。默认实现将给定的(@code bean})作为原样返回。
	 * @param bean     the new bean instance
	 * @param beanName the name of the bean
	 * @return the bean instance to use, either the original or a wrapped one;
	 * if {@code null}, no subsequent BeanPostProcessors will be invoked
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet
	 */
	@Nullable
	default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
	
	/**
	 * 将{@code BeanPostProcessor}应用到给定的新bean实例<i>after</i>任何bean初始化回调之后(像InitializingBean的
	 * {@code afterPropertiesSet})或者自定义初始化方法)。
	 * bean中已经填充了属性值。返回的bean实例可能是原始bean的包装器。
	 * <p>对于FactoryBean，这个回调将同时被FactoryBead实例和由FactoryBean创建的对象(从Spring 2.0开始)调用。
	 * 后处理程序可以通过相应的{@code bean instanceof FactoryBean}检查来决定是应用于FactoryBean还是已创建的对象，
	 * 或者同时应用于两者。
	 * <p>这个回调函数也将在由
	 * {@link InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation} method,
	 * in contrast to all other {@code BeanPostProcessor} callbacks.
	 * <p>The default implementation returns the given {@code bean} as-is.
	 * @param bean     the new bean instance
	 * @param beanName the name of the bean
	 * @return the bean instance to use, either the original or a wrapped one;
	 * if {@code null}, no subsequent BeanPostProcessors will be invoked
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet
	 * @see org.springframework.beans.factory.FactoryBean
	 */
	@Nullable
	default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
	
}
