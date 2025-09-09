package ru.chrshnv.di;

public class ApplicationRunner {
	private final ApplicationContext context;

	public ApplicationRunner(ApplicationContext context) {
		this.context = context;
	}

	public static ApplicationContext run(Class<?> mainClass, String[] args) {
		ApplicationRunner runner = new ApplicationRunner(new ApplicationContext(mainClass));

		return runner.context;
	}
}
