package com.julioplacas.modelo;

import java.io.Serializable;

public final class Posicion implements Serializable {
  private static final long serialVersionUID = 1L;

  public final int x;
  public final int y;

  public Posicion(
    final int x,
    final int y
  ) {
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "x: " + this.x + ", y: " + this.y;
  }

}
