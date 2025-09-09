package ru.chrshnv.example.service;

import ru.chrshnv.di.annotation.Injectable;

@Injectable
public class SomeTestInjectableClass {
	public void testMethod() {
		System.out.println("test!");
	}
}
