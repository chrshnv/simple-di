package ru.chrshnv.example;

import ru.chrshnv.di.ApplicationContext;
import ru.chrshnv.example.service.SomeClassWithAnnotationInjection;
import ru.chrshnv.example.service.SomeInterfaceInjectable;
import ru.chrshnv.example.service.SomeTestConstructorInjection;

public class Main {
	public static void main(String[] args) {
		ApplicationContext context = new ApplicationContext(Main.class);

		SomeClassWithAnnotationInjection instance = context.createInstance(SomeClassWithAnnotationInjection.class);

		instance.test3();

		SomeInterfaceInjectable ifaceImpl = context.createInstance(SomeInterfaceInjectable.class);
		ifaceImpl.testMethod();

		SomeTestConstructorInjection constructorInjectionInstance = context.createInstance(SomeTestConstructorInjection.class);
		constructorInjectionInstance.test();
	}
}
