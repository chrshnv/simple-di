package ru.chrshnv.di;

import ru.chrshnv.di.config.ObjectConfigurer;
import ru.chrshnv.di.config.impl.InjectAnnotationObjectConfigurer;
import ru.chrshnv.di.factory.ObjectFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {
	private final ObjectFactory factory;
	private final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();

	private final List<ObjectConfigurer> configurers = List.of(
		new InjectAnnotationObjectConfigurer(this)
	);

	public ApplicationContext(Class<?> mainClass) {
		this.factory = new ObjectFactory(mainClass);
	}

	public <T> T createInstance(Class<T> clazz) {
		//noinspection unchecked
		return (T) instances.computeIfAbsent(clazz, m -> {
			@SuppressWarnings("unchecked") T instance = (T) factory.createInstance(m);

			for (ObjectConfigurer configurer : configurers) {
				instance = configurer.configure(instance);
			}

			return instance;
		});
	}
}
