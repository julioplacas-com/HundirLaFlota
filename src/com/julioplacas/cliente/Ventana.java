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
import com.julioplacas.modelo.Estado;

public class Ventana extends JFrame implements ActionListener {
	private static final int BOARD_SIZE = 10;
	private JButton[][] buttons;
	private int[][] board;
	private boolean activo = true;
	private int barc = 2;
	
	private final Socket socket;
	private final ObjectOutputStream fSalida;
	private final ObjectInputStream fEntrada;
	private final Barco[] barcos;
	
	private int turno;
	private Estado estado;

	public Ventana(
			Socket socket,
			ObjectOutputStream fSalida,
			ObjectInputStream fEntrada,
			Barco[] barcos
	) throws IOException {
		super("Battleship");
		
		this.socket = socket;
		this.fSalida = fSalida;
		this.fEntrada = fEntrada;
		this.barcos = barcos;

		turno = fEntrada.readInt();
		System.out.println("Turno: " + turno);
		estado = Estado.values()[fEntrada.readInt()];
		System.out.println("Estado: " + estado);
		
		setExtendedState(MAXIMIZED_BOTH);
		setResizable(false);
		buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
		board = new int[BOARD_SIZE][BOARD_SIZE];
		initBoard();
		setLayout(new GridLayout(1, 2));
		Container pane = this.getContentPane();
		initButtons(pane, BorderLayout.LINE_START, false);
		initButtons(pane, BorderLayout.LINE_END, true);
		setVisible(true);
	}

	// Inicializa la matriz board con casillas vacías
	private void initBoard() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				board[i][j] = 0; // casilla vacía
			}
		}
	}

	// Crea los botones y los añade al JFrame
	private void initButtons(Container pane, String pos, boolean accion) {
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
	}

	// Lógica del juego cuando se hace clic en un botón
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		int row = -1, col = -1;
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (buttons[i][j] == button) {
					row = i;
					col = j;
					break;
				}
			}
		}

		if (board[row][col] == 1) {
			button.setBackground(Color.RED); // barco hundido
			board[row][col] = 2; // marcar como atacada
		} else {
			button.setBackground(Color.GRAY); // casilla vacía
			board[row][col] = 2;
			activo = false;
			button.setEnabled(activo); // marcar como atacada
		}
		if (board[row][col] == 1) {
			JOptionPane.showMessageDialog(this, "Barco encontrado en la posición " + (char) ('A' + col) + (row + 1));
		}
	}

}
