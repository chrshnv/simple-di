package ru.chrshnv.di;

import ru.chrshnv.di.annotation.Injectable;
import ru.chrshnv.di.config.ObjectConfigurer;
import ru.chrshnv.di.config.impl.DeprecatedInjectableAnnotationObjectConfigurer;
import ru.chrshnv.di.config.impl.InjectAnnotationObjectConfigurer;
import ru.chrshnv.di.factory.ObjectFactory;
import ru.chrshnv.di.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {
	private final ObjectFactory factory;
	private final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();
	private final ReflectionUtils utils;
	private final List<Class<?>> scannedClasses = new ArrayList<>();

	private final List<ObjectConfigurer> configurers = List.of(
		new InjectAnnotationObjectConfigurer(this),
		new DeprecatedInjectableAnnotationObjectConfigurer()
	);

	public ApplicationContext(Class<?> mainClass) {
		this.utils = new ReflectionUtils(mainClass);
		this.factory = new ObjectFactory(this, this.utils);

		this.init();
	}

	public void init() {
		List<Class<?>> annotated = utils.getAnnotatedWith(Injectable.class);
		scannedClasses.addAll(annotated);

		annotated
			.forEach(this::registerInstance);
	}

	public <T> T registerInstance(Class<T> clazz) {
		// DON'T use computeIfAbsent cause Concurrent implementations should override this method and, on a best-effort basis, throw an IllegalStateException if it is detected that the mapping function modifies this map during computation and as a result computation would never complete.

		@SuppressWarnings("unchecked") T instance = (T) instances.get(clazz);

		if (instance != null)
			return instance;

		instance = factory.createInstance(clazz);

		for (ObjectConfigurer configurer : configurers) {
			instance = configurer.configure(instance);
		}

		instances.put(clazz, instance);

		return instance;
	}

	public <T> T getInstance(Class<T> clazz) {
		//noinspection unchecked
		return (T) instances.get(clazz);
	}

	public List<Class<?>> getScannedClasses() {
		return scannedClasses;
	}

	public <T> Class<? extends  T> getImplements(Class<T> clazz) {
		List<Class<T>> impls = utils.getImplements(clazz);

		if (impls.size() != 1)
			throw new RuntimeException("Should have only one implementation");

		return impls.getLast();
	}
}
