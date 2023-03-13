package com.julioplacas.modelo;

public final class Barco {
	public final Posicion posicion;
	public final Direccion direccion;
	public final int longitud;
	public final boolean[] posicionesTocadas;
	
	public Barco(final Posicion posicion, final Direccion direccion, final int longitud) {
		this.posicion = posicion;
		this.direccion = direccion;
		this.longitud = longitud;
		this.posicionesTocadas = new boolean[longitud];
	}
}
