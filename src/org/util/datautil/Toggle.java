package org.util.datautil;

public final class Toggle {

	private boolean value;

	public Toggle() {}

	public Toggle(final boolean value) {
		this.value = value;
	}

	public final boolean toggle() {
		return value ^= true;
	}

	public final boolean get() {
		return value;
	}

	public final void set(final boolean value) {
		this.value = value;
	}
}
