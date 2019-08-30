package org.util.datautil;

public class Triad<L, C, R> {

	private L l;
	private C c;
	private R r;

	public Triad() {
		super();
	}

	public Triad(L l, C c, R r) {
		this.l = l;
		this.c = c;
		this.r = r;
	}

	public L getLeft() {
		return l;
	}

	public void setLeft(L l) {
		this.l = l;
	}

	public C getCenter() {
		return c;
	}

	public void setCenter(C c) {
		this.c = c;
	}

	public R getRight() {
		return r;
	}

	public void setRight(R r) {
		this.r = r;
	}

}
