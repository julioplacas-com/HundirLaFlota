package com.julioplacas.servidor;

import java.io.EOFException;
import java.io.IOException;

import com.julioplacas.modelo.Estado;
import com.julioplacas.utilidad.Utilidad;

public final class Sala extends Thread {
  public final int nSala;
  public final Jugador j1;
  public final Jugador j2;
  public Estado estado;

  public Sala(
    final int nSala,
    final Jugador j1,
    final Jugador j2
  ) {
    this.nSala = nSala;
    this.j1 = j1;
    this.j2 = j2;
    this.estado = Estado.TURNO_JUGADOR_1;
    this.start();
  }

  @Override
  public void run() {
    try {
      this.mandarTurnos();
    } catch (final IOException e) {
    }
    boolean bandera = true;
    while (bandera) {
      switch (this.estado) {
      case TURNO_JUGADOR_1:
        try {
          // coger la posicion pulsada por j1
          final int x = this.j1.fEntrada.readInt();
          final int y = this.j1.fEntrada.readInt();
          // saber si ha tocado barco de j2
          final boolean tocado = Utilidad.hayBarcoEnPosicion(x, y, this.j2.barcos);
          // los dos saben si ha tocado
          this.j1.fSalida.writeBoolean(tocado);
          this.j1.fSalida.flush();

          this.j2.fSalida.writeInt(x);
          this.j2.fSalida.writeInt(y);
          this.j2.fSalida.writeBoolean(tocado);
          this.j2.fSalida.flush();

          if (tocado) {
            this.j2.barcosPorTocar--;
          }

          if (this.j2.barcosPorTocar == 0) {
            this.estado = Estado.VICTORIA_JUGADOR_1;
          } else {
            this.estado = Estado.TURNO_JUGADOR_2;
          }

          this.j1.fSalida.writeInt(this.estado.ordinal());
          this.j1.fSalida.flush();
          this.j2.fSalida.writeInt(this.estado.ordinal());
          this.j2.fSalida.flush();
        } catch (final EOFException e) {
          bandera = false;
          this.estado = Estado.ERROR;
        } catch (final Exception e) {
        }

        break;

      case TURNO_JUGADOR_2:
        try {
          // coger la posicion pulsada por j1
          final int x = this.j2.fEntrada.readInt();
          final int y = this.j2.fEntrada.readInt();
          // saber si ha tocado barco de j2
          final boolean tocado = Utilidad.hayBarcoEnPosicion(x, y, this.j1.barcos);
          // los dos saben si ha tocado
          this.j2.fSalida.writeBoolean(tocado);
          this.j2.fSalida.flush();

          this.j1.fSalida.writeInt(x);
          this.j1.fSalida.writeInt(y);
          this.j1.fSalida.writeBoolean(tocado);
          this.j1.fSalida.flush();

          if (tocado) {
            this.j1.barcosPorTocar--;
          }

          if (this.j1.barcosPorTocar == 0) {
            this.estado = Estado.VICTORIA_JUGADOR_2;
          } else {
            this.estado = Estado.TURNO_JUGADOR_1;
          }

          this.j2.fSalida.writeInt(this.estado.ordinal());
          this.j2.fSalida.flush();
          this.j1.fSalida.writeInt(this.estado.ordinal());
          this.j1.fSalida.flush();
        } catch (final EOFException e) {
          bandera = false;
          this.estado = Estado.ERROR;
        } catch (final Exception e) {
        }

        break;

      default:
        break;
      }
    }
  }

  private void mandarTurnos() throws IOException {
    this.j1.fSalida.writeInt(1);
    this.j1.fSalida.writeInt(this.estado.ordinal());
    this.j1.fSalida.flush();
    this.j2.fSalida.writeInt(2);
    this.j2.fSalida.writeInt(this.estado.ordinal());
    this.j2.fSalida.flush();
    System.out.println("Turnos mandados");
  }
}
