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

public class ObjectFactory {
	private final Map<Class<?>, Object> instance = new HashMap<>();
	private final Map<Class<?>, Class<?>> implementations = new HashMap<>();

	private final List<ObjectPreConfigurer> preConfigurers = List.of(
		new ConstructorInjectionObjectPreConfigurer()
	);

	private final ReflectionUtils reflectionUtils;
	private final ApplicationContext ctx;

	public ObjectFactory(Class<?> mainClass, ApplicationContext ctx) {
		this.reflectionUtils = new ReflectionUtils(mainClass);
		this.ctx = ctx;
	}

	public <T> T createInstance(Class<T> clazz) {
		//noinspection unchecked
		return (T) instance.computeIfAbsent(clazz, m -> {
			if (m.isInterface())
				m = implementations.computeIfAbsent(clazz, this::searchImplementation);

			Object[] args = new Object[]{};
			Class<?>[] argsTypes = new Class<?>[]{};

			for (ObjectPreConfigurer preConfigurer : preConfigurers) {
				preConfigurer.setContext(ctx);

				PreConfigurerResult result = preConfigurer.configure(args, argsTypes, clazz);

				args = result.args();
				argsTypes = result.argsTypes();
			}

			try {
				return m.getDeclaredConstructor(argsTypes).newInstance(args);
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
