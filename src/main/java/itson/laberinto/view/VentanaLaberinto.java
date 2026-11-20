/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.laberinto.view;

import itson.laberinto.controller.LaberintoControlador;
import itson.laberinto.model.Laberinto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
public class VentanaLaberinto extends JFrame {

    private static final int MINIMO = 2;
    private static final int MAXIMO = 40;

    private LaberintoControlador controlador;
    private PanelLaberinto panelLaberinto;
    private JTextField txtFilas;
    private JTextField txtColumnas;
    private JTextField txtSemilla;
    private JComboBox<String> cmbEntradas;
    private JButton btnGenerar;
    private JButton btnBuscar;
    private JTextArea areaResultado;

    public VentanaLaberinto(LaberintoControlador controlador) {
        this.controlador = controlador;
        setTitle("Proyecto U3 - Resolución de laberinto con grafos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 720);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        txtFilas = new JTextField("8", 4);
        txtColumnas = new JTextField("12", 4);
        txtSemilla = new JTextField(8);
        cmbEntradas = new JComboBox<>();
        cmbEntradas.setPreferredSize(new Dimension(90, cmbEntradas.getPreferredSize().height));

        btnGenerar = new JButton("Generar");
        btnGenerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generar();
            }
        });

        btnBuscar = new JButton("Buscar camino");
        btnBuscar.setEnabled(false);
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscar();
            }
        });

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        controles.add(new JLabel("Filas:"));
        controles.add(txtFilas);
        controles.add(new JLabel("Columnas:"));
        controles.add(txtColumnas);
        controles.add(new JLabel("Semilla:"));
        controles.add(txtSemilla);
        controles.add(btnGenerar);
        controles.add(new JLabel("   Entrada:"));
        controles.add(cmbEntradas);
        controles.add(btnBuscar);

        panelLaberinto = new PanelLaberinto();

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaResultado.setMargin(new Insets(6, 8, 6, 8));
        JScrollPane scroll = new JScrollPane(areaResultado);
        scroll.setPreferredSize(new Dimension(420, 0));
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados"));

        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 6));
        leyenda.add(crearMuestra("Pared", PanelLaberinto.COLOR_PARED, PanelLaberinto.COLOR_PARED));
        leyenda.add(crearMuestra("Celda libre", PanelLaberinto.COLOR_LIBRE, PanelLaberinto.COLOR_CUADRICULA));
        leyenda.add(crearMuestra("Entrada", PanelLaberinto.COLOR_LIBRE, PanelLaberinto.COLOR_ENTRADA));
        leyenda.add(crearMuestra("Salida", PanelLaberinto.COLOR_LIBRE, PanelLaberinto.COLOR_SALIDA));
        leyenda.add(crearMuestra("Camino más corto", PanelLaberinto.COLOR_CAMINO_FONDO, PanelLaberinto.COLOR_CAMINO));

        JPanel centro = new JPanel(new BorderLayout());
        centro.add(panelLaberinto, BorderLayout.CENTER);
        centro.add(leyenda, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(controles, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(scroll, BorderLayout.EAST);
    }

    private JPanel crearMuestra(String texto, Color relleno, Color borde) {
        JPanel muestra = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        JPanel cuadro = new JPanel();
        cuadro.setPreferredSize(new Dimension(16, 16));
        cuadro.setBackground(relleno);
        cuadro.setBorder(BorderFactory.createLineBorder(borde, 2));
        muestra.add(cuadro);
        muestra.add(new JLabel(texto));
        return muestra;
    }

    private void generar() {
        int filas;
        int columnas;
        long semilla;
        try {
            filas = Integer.parseInt(txtFilas.getText().trim());
            columnas = Integer.parseInt(txtColumnas.getText().trim());
            String textoSemilla = txtSemilla.getText().trim();
            if (textoSemilla.isEmpty()) {
                semilla = new Random().nextInt(1000000);
            } else {
                semilla = Long.parseLong(textoSemilla);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Las filas, columnas y semilla deben ser números enteros.",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (filas < MINIMO || filas > MAXIMO || columnas < MINIMO || columnas > MAXIMO) {
            JOptionPane.showMessageDialog(this, "Las filas y columnas deben estar entre "
                    + MINIMO + " y " + MAXIMO + ".", "Dato inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        controlador.generar(filas, columnas, semilla);
        actualizar();
    }

    private void buscar() {
        int indice = cmbEntradas.getSelectedIndex();
        if (indice < 0) {
            return;
        }
        int entrada = controlador.getLaberinto().getEntradas().get(indice);
        controlador.buscarCamino(entrada);
        panelLaberinto.mostrar(controlador.getLaberinto(), controlador.getCamino());
        areaResultado.setText(controlador.getResultado());
        areaResultado.setCaretPosition(0);
    }

    private void actualizar() {
        Laberinto laberinto = controlador.getLaberinto();
        txtFilas.setText(String.valueOf(laberinto.getFilas()));
        txtColumnas.setText(String.valueOf(laberinto.getColumnas()));
        cmbEntradas.removeAllItems();
        for (int entrada : laberinto.getEntradas()) {
            cmbEntradas.addItem(laberinto.nombre(entrada));
        }
        btnBuscar.setEnabled(!laberinto.getEntradas().isEmpty());
        panelLaberinto.mostrar(laberinto, controlador.getCamino());
        areaResultado.setText(controlador.getResultado());
        areaResultado.setCaretPosition(0);
    }
}