package ru.chrshnv.di.config.impl;

import ru.chrshnv.di.annotation.DeprecatedInjectable;
import ru.chrshnv.di.config.ObjectConfigurer;
import ru.chrshnv.di.handler.DeprecatedInjectableInvocationHandler;

import java.lang.reflect.Proxy;

public class DeprecatedInjectableAnnotationObjectConfigurer implements ObjectConfigurer {
	@Override
	public <T> T configure(T o) {
		Class<?> clazz = o.getClass();

		if (!clazz.isAnnotationPresent(DeprecatedInjectable.class))
			return o;

		DeprecatedInjectableInvocationHandler invocationHandler = new DeprecatedInjectableInvocationHandler(o);

		return (T) Proxy.newProxyInstance(
			clazz.getClassLoader(),
			clazz.getInterfaces(),
			invocationHandler
		);
	}
}
