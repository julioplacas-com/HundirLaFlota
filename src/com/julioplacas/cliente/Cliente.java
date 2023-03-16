package com.julioplacas.cliente;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.julioplacas.modelo.Barco;
import com.julioplacas.modelo.Direccion;
import com.julioplacas.modelo.Estado;
import com.julioplacas.utilidad.Utilidad;

public final class Cliente extends JFrame implements Runnable, ActionListener {

  public static void main(final String[] args) throws IOException {
    final Socket socket = ConexionConServidor.conectarConServer();
    if (socket == null) {
      System.err.println("No se pudo conectar con el servidor");
      return;
    }

    final ObjectOutputStream fSalida = new ObjectOutputStream(socket.getOutputStream());
    final ObjectInputStream fEntrada = new ObjectInputStream(socket.getInputStream());

    final Barco[] barcos = Generador.generarBarcos(new int[] { 2, 2, 3, 3, 4 });
    fSalida.writeObject(barcos);

    final Cliente cliente = new Cliente(socket, fSalida, fEntrada, barcos);
  }

  private final JButton[][] mis_barcos;
  private final JButton[][] sus_barcos;

  private final Socket socket;
  private final ObjectOutputStream fSalida;
  private final ObjectInputStream fEntrada;
  private final Barco[] barcos;

  private final int turno;
  private Estado estado;

  private int mandarx = -1;
  private int mandary = -1;

  private final Thread hilo;

  public Cliente(
    final Socket socket,
    final ObjectOutputStream fSalida,
    final ObjectInputStream fEntrada,
    final Barco[] barcos
  ) throws IOException {
    super("Battleship");

    this.socket = socket;
    this.fSalida = fSalida;
    this.fEntrada = fEntrada;
    this.barcos = barcos;

    this.turno = fEntrada.readInt();
    System.out.println("Turno: " + this.turno);
    this.estado = Estado.values()[fEntrada.readInt()];
    System.out.println("Estado: " + this.estado);

    this.setExtendedState(MAXIMIZED_BOTH);
    this.setResizable(false);

    // Dividir la pantalla en dos columnas
    this.setLayout(new GridLayout(1, 2));
    final Container pane = this.getContentPane();

    // Pintar la columna izquierda con mis barcos
    this.mis_barcos = this.initMisBarcos();
    this.pintarMisBarcos();

    // Pintar la columna derecha con los barcos enemigos
    this.sus_barcos = this.initBarcosEnemigos();

    this.setVisible(true);

    this.hilo = new Thread(this);
    this.hilo.start();
  }

  private JButton[][] initMisBarcos() {
    final JButton[][] buttons = new JButton[Utilidad.MAX_SIZE][Utilidad.MAX_SIZE];
    final JPanel panel = this.crearRejilla();

    this.crearEtiquetasSuperiores(panel);

    for (int i = 0; i < Utilidad.MAX_SIZE; i++) {
      // Letra de la columna
      panel.add(new JLabel(String.valueOf((char) ('A' + i)), SwingConstants.CENTER));
      // Botones
      for (int j = 0; j < Utilidad.MAX_SIZE; j++) {
        final JButton button = new JButton();
        button.setBackground(Color.BLUE);
        button.setEnabled(false);
        panel.add(button);
        buttons[i][j] = button;
      }
    }

    return buttons;
  }

  private JButton[][] initBarcosEnemigos() {
    final JButton[][] buttons = new JButton[Utilidad.MAX_SIZE][Utilidad.MAX_SIZE];
    final JPanel panel = this.crearRejilla();

    this.crearEtiquetasSuperiores(panel);

    for (int i = 0; i < Utilidad.MAX_SIZE; i++) {
      // Letra de la columna
      panel.add(new JLabel(String.valueOf((char) ('A' + i)), SwingConstants.CENTER));
      // Botones
      for (int j = 0; j < Utilidad.MAX_SIZE; j++) {
        final JButton button = new JButton();
        button.addActionListener(this);
        // El tag del boton es: "x y"
        button.setActionCommand(j + " " + i);
        panel.add(button);
        buttons[i][j] = button;
      }
    }

    return buttons;
  }

  private JPanel crearRejilla() {
    final JPanel panel = new JPanel();
    this.getContentPane().add(panel);
    panel.setLayout(new GridLayout(Utilidad.MAX_SIZE + 1, Utilidad.MAX_SIZE + 1));
    return panel;
  }

  private void crearEtiquetasSuperiores(final JPanel panel) {
    panel.add(new JLabel());
    for (int i = 0; i < Utilidad.MAX_SIZE; i++) {
      panel.add(new JLabel(String.valueOf(i + 1), SwingConstants.CENTER));
    }
  }

  private void pintarMisBarcos() {
    for (final Barco barco : this.barcos)
      for (int i = 0; i < barco.longitud; i++)
        if (barco.direccion == Direccion.HORIZONTAL) {
          this.mis_barcos[barco.posicion.y][barco.posicion.x + i].setBackground(Color.GREEN);
          this.mis_barcos[barco.posicion.y][barco.posicion.x + i].setText("B");
        } else {
          this.mis_barcos[barco.posicion.y + i][barco.posicion.x].setBackground(Color.GREEN);
          this.mis_barcos[barco.posicion.y + i][barco.posicion.x].setText("B");
        }
  }

  private boolean esMiTurno() {
    return (this.turno == 1 && this.estado == Estado.TURNO_JUGADOR_1)
      || (this.turno == 2 && this.estado == Estado.TURNO_JUGADOR_2);
  }

  private boolean heGanado() {
    return (this.turno == 1 && this.estado == Estado.VICTORIA_JUGADOR_1)
      || (this.turno == 2 && this.estado == Estado.VICTORIA_JUGADOR_2);
  }

  private boolean hePerdido() {
    return (this.turno == 1 && this.estado == Estado.VICTORIA_JUGADOR_2)
      || (this.turno == 2 && this.estado == Estado.VICTORIA_JUGADOR_1);
  }

  private void cerrarConexion() {
    try {
      this.fEntrada.close();
      this.fSalida.close();
      this.socket.close();
    } catch (final IOException e) {
    }
  }

  @Override
  public void run() {
    boolean bandera = true;
    while (bandera) {
      if (this.esMiTurno()) {
        synchronized (this.hilo) {
          try {
            this.hilo.wait();
          } catch (final InterruptedException e) {
            e.printStackTrace();
          }
          try {
            this.fSalida.writeInt(this.mandarx);
            this.fSalida.writeInt(this.mandary);
            this.fSalida.flush();
          } catch (final IOException e) {
            e.printStackTrace();
          }
          try {
            final boolean tocoBarco = this.fEntrada.readBoolean();
            if (tocoBarco) {
              this.sus_barcos[this.mandary][this.mandarx].setBackground(Color.YELLOW);
              this.sus_barcos[this.mandary][this.mandarx].setText("*");
            } else {
              this.sus_barcos[this.mandary][this.mandarx].setBackground(Color.GRAY);
              this.sus_barcos[this.mandary][this.mandarx].setText("A");
            }
          } catch (final IOException e) {
            e.printStackTrace();
          }
          this.mandarx = -1;
          this.mandary = -1;
        }
      } else if (this.heGanado()) {
        bandera = false;
        this.cerrarConexion();
        System.out.println("Has ganao");
      } else if (this.hePerdido()) {
        bandera = false;
        this.cerrarConexion();
        System.out.println("Pringao");
      } else {
        try {
          final int x = this.fEntrada.readInt();
          final int y = this.fEntrada.readInt();
          System.out.println("Enemigo pincho: " + x + "," + y);
          final boolean tocoBarco = this.fEntrada.readBoolean();
          if (tocoBarco) {
            this.mis_barcos[y][x].setBackground(Color.YELLOW);
            this.mis_barcos[y][x].setText("*");
          } else {
            this.mis_barcos[y][x].setBackground(Color.GRAY);
            this.mis_barcos[y][x].setText("A");
          }
        } catch (final IOException e) {
          e.printStackTrace();
        }
      }
      try {
        this.estado = Estado.values()[this.fEntrada.readInt()];
        System.out.println("Estado recibido: " + this.estado);
      } catch (final IOException e) {
      }
    }
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    if (!this.esMiTurno())
      return;
    final JButton button = (JButton) e.getSource();
    final int x = Utilidad.charToInt(button.getActionCommand().charAt(0));
    final int y = Utilidad.charToInt(button.getActionCommand().charAt(2));

    this.sus_barcos[y][x].setEnabled(false);
    this.sus_barcos[y][x].setBackground(Color.GRAY);
    this.sus_barcos[y][x].setText("A");
    this.mandarx = x;
    this.mandary = y;
    synchronized (this.hilo) {
      this.hilo.notify();
    }
  }
}
