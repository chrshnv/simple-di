package ru.chrshnv.example;

import ru.chrshnv.di.ApplicationContext;
import ru.chrshnv.example.service.SomeClassWithAnnotationInjection;

public class Main {
	public static void main(String[] args) {
		/*ObjectFactory factory = new ObjectFactory(Main.class);

		SomeTestInjectableClass instance = factory.createInstance(SomeTestInjectableClass.class);

		instance.testMethod();

		SomeInterfaceInjectable ifaceInstance = factory.createInstance(SomeInterfaceInjectable.class);

		ifaceInstance.testMethod();*/

		ApplicationContext context = new ApplicationContext(Main.class);

		SomeClassWithAnnotationInjection instance = context.createInstance(SomeClassWithAnnotationInjection.class);

		instance.test3();
	}
}
