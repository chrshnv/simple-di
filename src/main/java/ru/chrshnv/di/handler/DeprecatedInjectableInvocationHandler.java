package ru.chrshnv.di.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DeprecatedInjectableInvocationHandler implements InvocationHandler {
	private final Object target;

	public DeprecatedInjectableInvocationHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("this injectable is deprecated!!");

		return method.invoke(target, args);
	}
}
