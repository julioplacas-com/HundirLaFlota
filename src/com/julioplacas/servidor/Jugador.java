package com.julioplacas.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.julioplacas.modelo.Barco;

public final class Jugador {
  public final Barco[] barcos;
  public final Socket socket;
  public final ObjectOutputStream fSalida;
  public final ObjectInputStream fEntrada;
  public final String nombre;
  public int barcosPorTocar;

  public Jugador(
    final Socket socket
  ) throws IOException, ClassNotFoundException {
    this.socket = socket;
    this.fSalida = new ObjectOutputStream(socket.getOutputStream());
    this.fEntrada = new ObjectInputStream(socket.getInputStream());
    this.barcos = (Barco[]) this.fEntrada.readObject();
    this.nombre = (String) this.fEntrada.readObject();
    this.barcosPorTocar = 0;
    for (final Barco barco : this.barcos)
      this.barcosPorTocar += barco.longitud;
  }
}
