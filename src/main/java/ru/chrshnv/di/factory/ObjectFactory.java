package ru.chrshnv.di.factory;

import ru.chrshnv.di.ApplicationContext;
import ru.chrshnv.di.config.ObjectPreConfigurer;
import ru.chrshnv.di.config.impl.ConstructorInjectionObjectPreConfigurer;
import ru.chrshnv.di.dto.PreConfigurerResult;
import ru.chrshnv.di.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectFactory {
	private final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();
	private final Map<Class<?>, Class<?>> implementations = new HashMap<>();

	private final List<ObjectPreConfigurer> preConfigurers = List.of(
		new ConstructorInjectionObjectPreConfigurer()
	);

	private final ReflectionUtils reflectionUtils;
	private final ApplicationContext ctx;

	public ObjectFactory(ApplicationContext ctx, ReflectionUtils utils) {
		this.reflectionUtils = utils;
		this.ctx = ctx;
	}

	public <T> T createInstance(Class<T> clazz) {
		// DON'T use computeIfAbsent cause Concurrent implementations should override this method and, on a best-effort basis, throw an IllegalStateException if it is detected that the mapping function modifies this map during computation and as a result computation would never complete.

		@SuppressWarnings("unchecked") T instance = (T) instances.get(clazz);
		if (instance != null)
			return instance;

		Class<?> originalClass = clazz;

		if (clazz.isInterface())
			//noinspection unchecked
			clazz = (Class<T>) implementations.computeIfAbsent(clazz, this::searchImplementation);

		Object[] args = new Object[]{};
		Class<?>[] argsTypes = new Class<?>[]{};

		for (ObjectPreConfigurer preConfigurer : preConfigurers) {
			preConfigurer.setContext(ctx);

			PreConfigurerResult result = preConfigurer.configure(args, argsTypes, clazz);

			args = result.args();
			argsTypes = result.argsTypes();
		}

		try {
			instance = clazz.getDeclaredConstructor(argsTypes).newInstance(args);

			instances.put(clazz, instance);
			instances.put(originalClass, instance);

			return instance;
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException |
				 NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	private <T> Class<? extends T> searchImplementation(Class<T> clazz) {
		List<Class<T>> impls = reflectionUtils.getImplements(clazz);

		if (impls.size() != 1)
			throw new RuntimeException("should have one implementation");

		return impls.getLast();
	}
}
