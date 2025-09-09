package ru.chrshnv.di.config.impl;

import ru.chrshnv.di.ApplicationContext;
import ru.chrshnv.di.annotation.Injectable;
import ru.chrshnv.di.config.ObjectPreConfigurer;
import ru.chrshnv.di.dto.PreConfigurerResult;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ConstructorInjectionObjectPreConfigurer implements ObjectPreConfigurer {
	private ApplicationContext context;

	@Override
	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	@Override
	public PreConfigurerResult configure(Object[] args, Class<?>[] argsTypes, Class<?> clazz) {
		Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		if (constructors.length == 0)
			return new PreConfigurerResult(args, argsTypes);

		if (constructors.length > 1)
			throw new RuntimeException("Should be one constructor");

		Constructor<?> constructor = constructors[0];
		constructor.setAccessible(true);

		Class<?>[] parameterTypes = constructor.getParameterTypes();

		for (Class<?> parameterType : parameterTypes) {
			if (!parameterType.isAnnotationPresent(Injectable.class))
				throw new RuntimeException(parameterType.getName() + " not in context");

			args = Arrays.copyOf(args, args.length + 1);
			argsTypes = Arrays.copyOf(argsTypes, argsTypes.length + 1);

			args[args.length - 1] = context.registerInstance(parameterType);
			argsTypes[argsTypes.length - 1] = getInstanceType(args[args.length - 1]);
		}

		return new PreConfigurerResult(args, argsTypes);
	}

	private Class<?> getInstanceType(Object o) {
		Class<?> clazz = o.getClass();

		Class<?>[] ifaces = clazz.getInterfaces();
		if (ifaces.length == 0)
			return clazz;

		if (ifaces.length > 1)
			throw new RuntimeException("Should implement only one interface");

		return ifaces[0];
	}
}
