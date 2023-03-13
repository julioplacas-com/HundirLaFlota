package com.julioplacas.servidor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.julioplacas.modelo.Barco;

public final class Jugador {
	public final Barco[] barcos;
	public final Socket socket;
	public final DataOutputStream fSalida;
	public final DataInputStream fEntrada;
	
	public Jugador(final Socket socket) throws IOException {
		this.socket = socket;
		this.fSalida = new DataOutputStream(socket.getOutputStream());
		this.fEntrada = new DataInputStream(socket.getInputStream());
		
		// Implementar;
		this.barcos = null;
	}
}
