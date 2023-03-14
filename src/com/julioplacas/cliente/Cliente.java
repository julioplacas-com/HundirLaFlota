package com.julioplacas.cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import com.julioplacas.modelo.Barco;
import com.julioplacas.modelo.Estado;
import com.julioplacas.utilidad.Utilidad;

import com.julioplacas.modelo.Direccion;
import com.julioplacas.modelo.Posicion;

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
		  boolean[][] board = new boolean[10][10];
	        Random random = new Random();
	        for (int i = 0; i < longitudes.length; i++) {
	            int longitud = longitudes[i];
	            boolean valido = false;
	            while (!valido) {
	                int x = random.nextInt(10);
	                int y = random.nextInt(10);
	                Direccion direccion = random.nextBoolean() ? Direccion.HORIZONTAL : Direccion.VERTICAL;
	                valido = validarPosicion(x, y, direccion, longitud, board);
	                if (valido) {
	                    Barco barco = new Barco(new Posicion(x, y), direccion, longitud);
	                    for (int j = 0; j < longitud; j++) {
	                        int pos_x = direccion == Direccion.HORIZONTAL ? x + j : x;
	                        int pos_y = direccion == Direccion.VERTICAL ? y + j : y;
	                        barco.posicionesTocadas[j] = false;
	                        board[pos_x][pos_y] = true;
	                    }
	                    barcos[i] = barco;
	                }
	            }
	        }
	        return barcos;
	      
	}
	 public static boolean validarPosicion(int x, int y, Direccion direccion, int longitud, boolean[][] board) {
	        for (int i = 0; i < longitud; i++) {
	            int pos_x = direccion == Direccion.HORIZONTAL ? x + i : x;
	            int pos_y = direccion == Direccion.VERTICAL ? y + i : y;

	            // Verifica que la posición esté dentro del tablero
	            if (pos_x < 0 || pos_x >= Utilidad.MAX_SIZE || pos_y < 0 || pos_y >= Utilidad.MAX_SIZE) {
	                return false;
	            }

	            // Verifica que la posición no esté ocupada por otro barco
	            if (board[pos_x][pos_y]) {
	                return false;
	            }
	        }
	        return true;
	    }
}
