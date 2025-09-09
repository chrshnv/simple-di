package ru.chrshnv.example.service.impl;

import ru.chrshnv.di.annotation.DeprecatedInjectable;
import ru.chrshnv.example.service.SomeInterfaceInjectable;

@DeprecatedInjectable
public class SomeInterfaceInjectableImpl implements SomeInterfaceInjectable {
	@Override
	public void testMethod() {
		System.out.println("test2");
	}
}
