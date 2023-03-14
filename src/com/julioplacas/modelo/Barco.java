package com.julioplacas.modelo;

import java.io.Serializable;

public final class Barco implements Serializable {
	private static final long serialVersionUID = 1L;

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

	@Override
	public String toString() {
		return "Posicion: " + posicion + ", Direccion: " + direccion + ", Longitud: " + longitud;
	}
	
}
