package com.elpais.context;

public final class World {
	private static final ThreadLocal<ScenarioContext> CTX = ThreadLocal.withInitial(ScenarioContext::new);

	private World() {}

	public static ScenarioContext get() {
		return CTX.get();
	}

	public static void clear() {
		CTX.remove();
	}
}


