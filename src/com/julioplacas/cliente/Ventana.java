package com.julioplacas.cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.julioplacas.modelo.Barco;
import com.julioplacas.modelo.Direccion;
import com.julioplacas.modelo.Estado;

public class Ventana extends JFrame implements Runnable, ActionListener {
	private static final int BOARD_SIZE = 10;
	private JButton[][] mis_barcos;
	private JButton[][] sus_barcos;

	private final Socket socket;
	private final ObjectOutputStream fSalida;
	private final ObjectInputStream fEntrada;
	private final Barco[] barcos;

	private int turno;
	private Estado estado;

	private Thread hilo;

	public Ventana(Socket socket, ObjectOutputStream fSalida, ObjectInputStream fEntrada, Barco[] barcos)
			throws IOException {
		super("Battleship");

		this.socket = socket;
		this.fSalida = fSalida;
		this.fEntrada = fEntrada;
		this.barcos = barcos;

		hilo = new Thread(this);
		hilo.start();

		turno = fEntrada.readInt();
		System.out.println("Turno: " + turno);
		estado = Estado.values()[fEntrada.readInt()];
		System.out.println("Estado: " + estado);

		setExtendedState(MAXIMIZED_BOTH);
		setResizable(false);
		setLayout(new GridLayout(1, 2));
		Container pane = this.getContentPane();
		mis_barcos = initButtons(pane, BorderLayout.LINE_START, false);
		sus_barcos = initButtons(pane, BorderLayout.LINE_END, true);
		setVisible(true);
	}

	public void run() {
		while (true) {
			if (turno == 1 && estado == Estado.TURNO_JUGADOR_1) {
				try {
					hilo.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					estado = Estado.values()[fEntrada.readInt()];
					int x = fEntrada.readInt();
					int y = fEntrada.readInt();
					sus_barcos[x][y].setBackground(Color.RED);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// Crea los botones y los añade al JFrame
	private JButton[][] initButtons(Container pane, String pos, boolean accion) {
		JButton[][] buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
		JPanel frame = new JPanel();
		pane.add(frame, pos);

		frame.setLayout(new GridLayout(BOARD_SIZE + 1, BOARD_SIZE + 1));

		// Agregamos las etiquetas de la fila superior
		frame.add(new JLabel());
		for (int i = 0; i < BOARD_SIZE; i++) {
			frame.add(new JLabel(String.valueOf(i + 1), SwingConstants.CENTER));
		}

		// Creamos los botones y las etiquetas de la columna izquierda
		for (int i = 0; i < BOARD_SIZE; i++) {
			frame.add(new JLabel(String.valueOf((char) ('A' + i)), SwingConstants.CENTER));
			for (int j = 0; j < BOARD_SIZE; j++) {
				buttons[i][j] = new JButton();
				if (accion) {
					buttons[i][j].addActionListener(this);
				}

				frame.add(buttons[i][j]);
			}
		}
		return buttons;
	}

	// Lógica del juego cuando se hace clic en un botón
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		int x = -1, y = -1;
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (sus_barcos[i][j] == button) {
					x = i;
					y = j;
					break;
				}
			}
		}
		sus_barcos[x][y].setEnabled(false);
		if (hayBarco(x, y)) {
			sus_barcos[x][y].setBackground(Color.YELLOW);
		} else {
			sus_barcos[x][y].setBackground(Color.BLUE);
		}
	}

	public boolean hayBarco(int x, int y) {
		for (Barco barco : barcos) {
			if (barcoEnHorizontal(x, y, barco) || barcoEnVertical(x, y, barco))
				return true;
		}
		return false;
	}

	private boolean barcoEnHorizontal(int x, int y, Barco barco) {
		return
				barco.direccion == Direccion.HORIZONTAL
				&& barco.posicion.y == y
				&& barco.posicion.x >= x
				&& barco.posicion.x + barco.longitud <= barco.posicion.x;
	}

	private boolean barcoEnVertical(int x, int y, Barco barco) {
		return
				barco.direccion == Direccion.VERTICAL
				&& barco.posicion.x == x
				&& barco.posicion.y >= y
				&& barco.posicion.y + barco.longitud <= barco.posicion.y;
	}
}
