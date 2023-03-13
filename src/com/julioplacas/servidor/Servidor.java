package com.julioplacas.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.julioplacas.utilidad.Utilidad;

public final class Servidor {
	private static final Sala[] salas = new Sala[Utilidad.MAX_SALAS];
	
	public static void main(String[] args) throws IOException {
		System.out.println("Server encendido");
		final ServerSocket serverSocket = new ServerSocket(Utilidad.PUERTO);
		while (true) {
			System.out.println("Esperando j1 ...");
			final Socket j1 = esperarConexion(serverSocket);
			System.out.println("Esperando j2 ...");
			final Socket j2 = esperarConexion(serverSocket);
			System.out.println("Buscando sala ...");
			final int nSala = getSalaVacia();
			if (nSala != -1) {
				System.out.println("Pasando j1 y j2 a sala " + nSala);
				salas[nSala] = new Sala(nSala, new Jugador(j1), new Jugador(j2));
			} else {
				System.out.println("No hay salas disponibles");
				// Implementar un mensaje a mandar al cliente "no hay salas disponibles"
				// O incluso una lista de espera
			}
		}
	}
	
	// Retorna -1 si no hay salas vacias
	private static int getSalaVacia() {
		for (int i = 0; i < salas.length; i++) if (salas[i] == null) return i;
		return -1;
	}
	
	// Esperara bloqueando el hilo principal a que se conecte un cliente
	private static Socket esperarConexion(ServerSocket serverSocket) throws IOException {
		return serverSocket.accept();
	}
}
