package ru.chrshnv.example.service;

import ru.chrshnv.di.annotation.Injectable;

@Injectable
public class SomeTestConstructorInjection {
	private final SomeInterfaceInjectable injectable;

	public SomeTestConstructorInjection(SomeInterfaceInjectable injectable) {
		this.injectable = injectable;
	}

	public void test() {
		System.out.println("constructor-injection test");
		injectable.testMethod();
	}
}
