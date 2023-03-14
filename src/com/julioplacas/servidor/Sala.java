package com.julioplacas.servidor;

import java.io.IOException;

import com.julioplacas.modelo.Estado;

public final class Sala extends Thread {
	public final int nSala;
	public final Jugador j1;
	public final Jugador j2;
	private Estado estado;
	
	public Sala(final int nSala, final Jugador j1, final Jugador j2) {
		this.nSala = nSala;
		this.j1 = j1;
		this.j2 = j2;
		this.estado = Estado.TURNO_JUGADOR_1;
		this.start();
	}
	
	@Override
	public void run() {
		try { mandarTurnos(); } catch (IOException e) {}
		while (true) {
			// Recibir eventos del jugador que toque
			// Validar que sea legal y enviar el nuevo estado a ambos jugadores
			// Enviar al jugador contrario donde toco el actual
		}
	}
	
	private void mandarTurnos() throws IOException {
		this.j1.fSalida.writeInt(1);
		this.j1.fSalida.writeInt(estado.ordinal());
		this.j1.fSalida.flush();
		this.j2.fSalida.writeInt(2);
		this.j2.fSalida.writeInt(estado.ordinal());
		this.j2.fSalida.flush();
		System.out.println("Turnos mandados");
	}
}
