package ru.chrshnv.di.config.impl;

import ru.chrshnv.di.ApplicationContext;
import ru.chrshnv.di.annotation.Inject;
import ru.chrshnv.di.config.ObjectConfigurer;

import java.lang.reflect.Field;

public class InjectAnnotationObjectConfigurer implements ObjectConfigurer {
	private final ApplicationContext context;

	public InjectAnnotationObjectConfigurer(ApplicationContext context) {
		this.context = context;
	}

	@Override
	public <T> T configure(T o) {
		Field[] fields = o.getClass().getDeclaredFields();

		for (Field field : fields) {
			if (field.getAnnotation(Inject.class) == null)
				continue;

			Class<?> type = field.getType();
			if (!context.getScannedClasses().contains(type))
				throw new RuntimeException(type.getName() + " not in context");

			field.setAccessible(true);
			try {
				field.set(o, context.registerInstance(type));
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		return o;
	}
}
