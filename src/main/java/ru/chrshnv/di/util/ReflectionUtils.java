package ru.chrshnv.di.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {
	private final Class<?> mainClass;
	private final ClassLoader classLoader;

	private final List<Class<?>> loadedClasses = new ArrayList<>();

	public ReflectionUtils(Class<?> mainClass) {
		this.mainClass = mainClass;
		this.classLoader = mainClass.getClassLoader();

		this.init();
	}

	public List<Class<?>> getAnnotatedWith(Class<? extends Annotation> annotation) {
		return loadedClasses.stream().filter(it -> it.getAnnotation(annotation) != null).toList();
	}

	public <T> List<Class<T>> getImplements(Class<T> clazz) {
		return loadedClasses
			.stream()
			.filter(clazz::isAssignableFrom)
			.filter(it -> !it.isInterface())
			.map(it -> (Class<T>) it.asSubclass(clazz))
			.toList();
	}

	private void init() {
		loadedClasses.addAll(getLoadedClasses(mainClass.getPackageName()));
	}

	private List<Class<?>> getLoadedClasses(String packageName) {
		List<Class<?>> result = new ArrayList<>();

		String packagePath = packageName.replaceAll("[.]", "/");

		try (InputStream stream = classLoader.getResourceAsStream(packagePath)) {
			if (stream == null)
				return result;

			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

			reader
				.lines()
				.forEach(it -> {
					if (it.endsWith(".class")) {
						String str = it.replace(".class", "");

						try {
							Class<?> clazz = Class.forName(String.format("%s.%s", packageName, str));
							result.add(clazz);
						} catch (ClassNotFoundException e) {
							throw new RuntimeException(e);
						}
					} else {
						List<Class<?>> classes = getLoadedClasses(String.format("%s.%s", packageName, it));
						result.addAll(classes);
					}
				});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return result;
	}
}
