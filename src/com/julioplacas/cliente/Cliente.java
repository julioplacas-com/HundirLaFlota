package com.julioplacas.cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.julioplacas.modelo.Barco;
import com.julioplacas.modelo.Estado;
import com.julioplacas.utilidad.Utilidad;

public final class Cliente {
	
	public static void main(String[] args) throws IOException {
		final Socket socket = conectarConServer();
		if (socket == null) {
			System.err.println("No se pudo conectar con el servidor");
			return;
		}
		
		final DataOutputStream fSalida = new DataOutputStream(socket.getOutputStream());
		final DataInputStream fEntrada = new DataInputStream(socket.getInputStream());
		
		int turno = fEntrada.readInt();
		System.out.println("Turno: " + turno);
		int estado = fEntrada.readInt();
		System.out.println("Estado: " + Estado.values()[estado]);
		
		final Barco[] barcos = generarBarcos(new int[] { 2, 2, 3, 3, 4 });
		
		// Tema de la ventana
		// Mandar los barcos al servidor
		// Leer del servidor eventos
		// Pintar en ventana
		// Mandar al servidor eventos
	}
	
	private static Socket conectarConServer() {
		int intentos = 0;
		Socket socket = null;
		while (intentos != 3) {
			try { socket = new Socket(Utilidad.SERVER, Utilidad.PUERTO); break; }
			catch (IOException e) {
				intentos++;
				System.err.println("Reintantando conexion ...");
			}
		}
		return socket;
	}
	
	private static Barco[] generarBarcos(int[] longitudes) {
		Barco[] barcos = new Barco[longitudes.length];
		// implementar
		return barcos;
	}
}
