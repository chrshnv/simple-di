package ru.chrshnv.di.config;

import ru.chrshnv.di.ApplicationContext;
import ru.chrshnv.di.dto.PreConfigurerResult;

public interface ObjectPreConfigurer {
	PreConfigurerResult configure(Object[] args, Class<?>[] argsTypes, Class<?> clazz);

	void setContext(ApplicationContext context);
}
