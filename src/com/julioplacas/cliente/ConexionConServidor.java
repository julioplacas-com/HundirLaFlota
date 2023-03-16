package com.julioplacas.cliente;

import java.io.IOException;
import java.net.Socket;

import com.julioplacas.utilidad.Utilidad;

public class ConexionConServidor {
  public static Socket conectarConServer() {
    int intentos = 0;
    Socket socket = null;
    while (intentos != 3) {
      try {
        socket = new Socket(Utilidad.SERVER, Utilidad.PUERTO);
        break;
      } catch (final IOException e) {
        intentos++;
        System.err.println("Reintantando conexion ...");
      }
    }
    return socket;
  }
}
