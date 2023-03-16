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

    for (final boolean[] bs : posiciones) {
      for (final boolean bs2 : bs) {
        System.out.print(bs2 ? "*" : " ");
      }
      System.out.println();
    }

    return barcos;
  }

  private static void rellenarPosicionesBarco(
    final Barco barco,
    final boolean[][] posiciones
  ) {
    rellenarFlancos(barco, posiciones);
    for (int j = 0; j < barco.longitud; j++) {
      if (barco.direccion == Direccion.HORIZONTAL) {
        final int pos_x = barco.posicion.x + j;
        final int pos_y = barco.posicion.y;
        posiciones[pos_x][pos_y] = true;

        if (pos_y - 1 >= 0)
          posiciones[pos_x][pos_y - 1] = true;
        if (pos_y + 1 < Utilidad.MAX_SIZE)
          posiciones[pos_x][pos_y + 1] = true;
      } else {
        final int pos_x = barco.posicion.x;
        final int pos_y = barco.posicion.y + j;
        posiciones[pos_x][pos_y] = true;

        if (pos_x - 1 >= 0)
          posiciones[pos_x - 1][pos_y] = true;
        if (pos_x + 1 < Utilidad.MAX_SIZE)
          posiciones[pos_x + 1][pos_y] = true;
      }
    }
  }

  private static void rellenarFlancos(
    final Barco barco,
    final boolean[][] posiciones
  ) {
    if (barco.direccion == Direccion.HORIZONTAL) {
      final int pos_x_izq = barco.posicion.x - 1;
      if (pos_x_izq < 0)
        return;

      // Arriba
      if (barco.posicion.y - 1 >= 0)
        posiciones[pos_x_izq][barco.posicion.y - 1] = true;

      // En medio
      posiciones[pos_x_izq][barco.posicion.y] = true;

      // Abajo
      if (barco.posicion.y + 1 < Utilidad.MAX_SIZE)
        posiciones[pos_x_izq][barco.posicion.y + 1] = true;

      final int pos_x_der = barco.posicion.x + barco.longitud;
      if (pos_x_der >= Utilidad.MAX_SIZE)
        return;

      // Arriba
      if (barco.posicion.y - 1 >= 0)
        posiciones[pos_x_der][barco.posicion.y - 1] = true;

      // En medio
      posiciones[pos_x_der][barco.posicion.y] = true;

      // Abajo
      if (barco.posicion.y + 1 < Utilidad.MAX_SIZE)
        posiciones[pos_x_der][barco.posicion.y + 1] = true;
    } else {
      final int pos_y_arb = barco.posicion.y - 1;
      if (pos_y_arb < 0)
        return;

      // Izquierda
      if (barco.posicion.x - 1 >= 0)
        posiciones[barco.posicion.x - 1][pos_y_arb] = true;

      // En medio
      posiciones[barco.posicion.x][pos_y_arb] = true;

      // Derecha
      if (barco.posicion.x + 1 < Utilidad.MAX_SIZE)
        posiciones[barco.posicion.x + 1][pos_y_arb] = true;

      final int pos_y_abj = barco.posicion.y + barco.longitud;
      if (pos_y_abj >= Utilidad.MAX_SIZE)
        return;

      // Izquierda
      if (barco.posicion.x - 1 >= 0)
        posiciones[barco.posicion.x - 1][pos_y_abj] = true;

      // En medio
      posiciones[barco.posicion.x][pos_y_abj] = true;

      // Derecha
      if (barco.posicion.x + 1 < Utilidad.MAX_SIZE)
        posiciones[barco.posicion.x + 1][pos_y_abj] = true;
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
