package ru.chrshnv.example;

import ru.chrshnv.di.ApplicationContext;
import ru.chrshnv.di.ApplicationRunner;
import ru.chrshnv.example.service.SomeTestInjectableClass;

public class Main {
	public static void main(String[] args) {
		ApplicationContext ctx = ApplicationRunner.run(Main.class, args);

		System.out.println("loaded classes:");
		ctx.getScannedClasses().forEach(it -> System.out.println(it.getName()));

		SomeTestInjectableClass instance = ctx.getInstance(SomeTestInjectableClass.class);
		instance.testMethod();
	}
}
