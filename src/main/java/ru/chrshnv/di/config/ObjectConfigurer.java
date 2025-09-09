package ru.chrshnv.di.config;

public interface ObjectConfigurer {
	<T> T configure(T o);
}
