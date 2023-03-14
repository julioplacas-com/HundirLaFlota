package com.julioplacas.modelo;

public final class Posicion {
	public final int x;
	public final int y;
	
	public Posicion(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "Posicion [x=" + x + ", y=" + y + "]";
	}
	
}
