package ru.chrshnv.di.dto;

public record PreConfigurerResult(
	Object[] args,
	Class<?>[] argsTypes
) {
}
