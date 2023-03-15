package com.julioplacas.utilidad;

import com.julioplacas.modelo.Barco;
import com.julioplacas.modelo.Direccion;

public final class Utilidad {
  public static final int PUERTO = 3000;
  public static final String SERVER = "psp.julioplacas.com";
  public static final int MAX_SALAS = 5;
  public static final int MAX_SIZE = 10;

  public static boolean hayBarcoEnPosicion(
    final int x,
    final int y,
    final Barco[] barcos
  ) {
    for (final Barco barco : barcos)
      if (barcoEnHorizontal(x, y, barco) || barcoEnVertical(x, y, barco))
        return true;
    return false;
  }

  private static boolean barcoEnHorizontal(
    final int x,
    final int y,
    final Barco barco
  ) {
    return barco.direccion == Direccion.HORIZONTAL
      && barco.posicion.y == y
      && x >= barco.posicion.x
      && x < barco.posicion.x + barco.longitud;
  }

  private static boolean barcoEnVertical(
    final int x,
    final int y,
    final Barco barco
  ) {
    return barco.direccion == Direccion.VERTICAL
      && barco.posicion.x == x
      && y >= barco.posicion.y
      && y < barco.posicion.y + barco.longitud;
  }

  public static int charToInt(final char n) {
    return n - '0';
  }
}
