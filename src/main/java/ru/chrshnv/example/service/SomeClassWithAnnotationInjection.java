package ru.chrshnv.example.service;

import ru.chrshnv.di.annotation.Inject;

public class SomeClassWithAnnotationInjection {
	@Inject
	private SomeTestInjectableClass test;

	public void test3() {
		System.out.printf("from injected -");
		test.testMethod();
		System.out.println();
	}
}
