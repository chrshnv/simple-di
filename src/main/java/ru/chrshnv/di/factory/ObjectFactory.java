package ru.chrshnv.di.factory;

import ru.chrshnv.di.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectFactory {
	private final Map<Class<?>, Object> instance = new HashMap<>();
	private final Map<Class<?>, Class<?>> implementations = new HashMap<>();

	private final ReflectionUtils reflectionUtils;

	public ObjectFactory(Class<?> mainClass) {
		this.reflectionUtils = new ReflectionUtils(mainClass);
	}

	public <T> T createInstance(Class<T> clazz) {
		//noinspection unchecked
		return (T) instance.computeIfAbsent(clazz, m -> {
			if (m.isInterface())
				m = implementations.computeIfAbsent(clazz, this::searchImplementation);

			try {
				return m.getDeclaredConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException |
					 NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		});
	}

	private <T> Class<? extends T> searchImplementation(Class<T> clazz) {
		List<Class<T>> impls = reflectionUtils.getImplements(clazz);

		if (impls.size() != 1)
			throw new RuntimeException("should have one implementation");

		return impls.getLast();
	}
}
