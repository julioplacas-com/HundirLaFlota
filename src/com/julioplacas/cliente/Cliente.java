package com.julioplacas.cliente;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.julioplacas.modelo.Barco;
import com.julioplacas.modelo.Direccion;
import com.julioplacas.utilidad.Utilidad;

public final class Cliente extends JFrame implements ActionListener {

  public static void main(final String[] args) throws IOException {

    final Barco[] barcos = Generador.generarBarcos(new int[] { 2, 2, 3, 3, 4 });

    final Cliente cliente = new Cliente(barcos);
  }

  private final JButton[][] mis_barcos;
  private final JButton[][] sus_barcos;

  private final Barco[] barcos;

  private final int mandarx = -1;
  private final int mandary = -1;

  public Cliente(
    final Barco[] barcos
  ) throws IOException {
    super("Battleship");

    this.barcos = barcos;

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

  public void mensajeGanador() {
    JOptionPane.showMessageDialog(null, "¡HAS GANADO!", "Fin de Partida", JOptionPane.INFORMATION_MESSAGE);
  }

  public void mensajePerdedor() {
    JOptionPane.showMessageDialog(null, "¡PERDEDOR!", "Fin de Partida", JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
  }
}
