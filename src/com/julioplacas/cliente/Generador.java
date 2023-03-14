package com.julioplacas.cliente;

import java.util.Random;

import com.julioplacas.modelo.Barco;
import com.julioplacas.modelo.Direccion;
import com.julioplacas.modelo.Posicion;
import com.julioplacas.utilidad.Utilidad;

public final class Generador {
  public static Barco[] generarBarcos(
    final int[] longitudes
  ) {
    final Barco[] barcos = new Barco[longitudes.length];
    final boolean[][] posiciones = new boolean[Utilidad.MAX_SIZE][Utilidad.MAX_SIZE];
    final Random random = new Random();
    for (int i = 0; i < longitudes.length; i++) {
      boolean valido = false;
      while (!valido) {
        final int x = random.nextInt(Utilidad.MAX_SIZE);
        final int y = random.nextInt(Utilidad.MAX_SIZE);
        final Direccion direccion = random.nextBoolean() ? Direccion.HORIZONTAL
          : Direccion.VERTICAL;
        valido = validarPosicion(x, y, direccion, longitudes[i], posiciones);
        if (valido) {
          final Barco barco = new Barco(new Posicion(x, y), direccion, longitudes[i]);
          rellenarPosicionesBarco(barco, posiciones);
          barcos[i] = barco;
        }
      }
    }
    return barcos;
  }

  private static void rellenarPosicionesBarco(
    final Barco barco,
    final boolean[][] posiciones
  ) {
    for (int j = 0; j < barco.longitud; j++) {
      final int pos_x = barco.direccion == Direccion.HORIZONTAL
        ? barco.posicion.x + j
        : barco.posicion.x;
      final int pos_y = barco.direccion == Direccion.VERTICAL
        ? barco.posicion.y + j
        : barco.posicion.y;
      posiciones[pos_x][pos_y] = true;
    }
  }

  private static boolean validarPosicion(
    final int x,
    final int y,
    final Direccion direccion,
    final int longitud,
    final boolean[][] board
  ) {
    for (int i = 0; i < longitud; i++) {
      final int pos_x = direccion == Direccion.HORIZONTAL ? x + i : x;
      final int pos_y = direccion == Direccion.VERTICAL ? y + i : y;

      // Verifica que la posición esté dentro del tablero
      if (pos_x < 0 || pos_x >= Utilidad.MAX_SIZE || pos_y < 0
        || pos_y >= Utilidad.MAX_SIZE)
        return false;

      // Verifica que la posición no esté ocupada por otro barco
      if (board[pos_x][pos_y])
        return false;
    }
    return true;
  }
}
