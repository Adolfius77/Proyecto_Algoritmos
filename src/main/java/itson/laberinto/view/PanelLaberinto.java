/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.laberinto.view;

import itson.laberinto.model.Arista;
import itson.laberinto.model.Laberinto;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
public class PanelLaberinto extends JPanel {

    public static final Color COLOR_PARED = new Color(0x00, 0x00, 0x00);
    public static final Color COLOR_LIBRE = new Color(0xFF, 0xFF, 0xFF);
    public static final Color COLOR_CUADRICULA = new Color(0x88, 0x88, 0x88);
    public static final Color COLOR_ENTRADA = new Color(0x1A, 0x8A, 0x2E);
    public static final Color COLOR_SALIDA = new Color(0xD6, 0x28, 0x28);
    public static final Color COLOR_CAMINO = new Color(0x0D, 0x47, 0xA1);
    public static final Color COLOR_CAMINO_FONDO = new Color(0x29, 0x79, 0xFF);
    private static final int MARGEN = 20;

    private Laberinto laberinto;
    private List<Integer> camino;

    public PanelLaberinto() {
        this.camino = new ArrayList<>();
        setBackground(Color.WHITE);
    }

    public void mostrar(Laberinto laberinto, List<Integer> camino) {
        this.laberinto = laberinto;
        this.camino = camino;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (laberinto == null) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int filas = laberinto.getFilas();
        int columnas = laberinto.getColumnas();
        int tam = Math.min((getWidth() - 2 * MARGEN) / columnas, (getHeight() - 2 * MARGEN) / filas);
        int x0 = MARGEN;
        int y0 = MARGEN;

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                int x = x0 + c * tam;
                int y = y0 + f * tam;
                int nodo = laberinto.getNodo(f, c);
                if (!laberinto.esLibre(f, c)) {
                    g2.setColor(COLOR_PARED);
                } else if (camino.contains(nodo)) {
                    g2.setColor(COLOR_CAMINO_FONDO);
                } else {
                    g2.setColor(COLOR_LIBRE);
                }
                g2.fillRect(x, y, tam, tam);
                g2.setColor(COLOR_CUADRICULA);
                g2.drawRect(x, y, tam, tam);
            }
        }

        g2.setStroke(new BasicStroke(3));
        for (int entrada : laberinto.getEntradas()) {
            g2.setColor(COLOR_ENTRADA);
            g2.drawRect(x0 + laberinto.getColumna(entrada) * tam + 3,
                    y0 + laberinto.getFila(entrada) * tam + 3, tam - 6, tam - 6);
        }
        for (int salida : laberinto.getSalidas()) {
            g2.setColor(COLOR_SALIDA);
            g2.drawRect(x0 + laberinto.getColumna(salida) * tam + 3,
                    y0 + laberinto.getFila(salida) * tam + 3, tam - 6, tam - 6);
        }

        g2.setColor(COLOR_CAMINO);
        g2.setStroke(new BasicStroke(4));
        for (int i = 0; i < camino.size() - 1; i++) {
            int a = camino.get(i);
            int b = camino.get(i + 1);
            g2.drawLine(x0 + laberinto.getColumna(a) * tam + tam / 2, y0 + laberinto.getFila(a) * tam + tam / 2,
                    x0 + laberinto.getColumna(b) * tam + tam / 2, y0 + laberinto.getFila(b) * tam + tam / 2);
        }
        g2.setStroke(new BasicStroke(1));

        if (tam >= 30) {
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            FontMetrics fm = g2.getFontMetrics();
            for (int nodo = 0; nodo < laberinto.getGrafo().getNumeroNodos(); nodo++) {
                if (!laberinto.esLibre(nodo)) {
                    continue;
                }
                int x = x0 + laberinto.getColumna(nodo) * tam;
                int y = y0 + laberinto.getFila(nodo) * tam;
                g2.setColor(Color.DARK_GRAY);
                g2.drawString(laberinto.nombre(nodo), x + 8, y + 20);

                for (Arista arista : laberinto.getGrafo().getAristas(nodo)) {
                    int destino = arista.getDestino();
                    String peso = String.valueOf(arista.getPeso());
                    int px = x + tam / 2;
                    int py = y + tam / 2;
                    if (laberinto.getFila(destino) == laberinto.getFila(nodo)) {
                        px = x + tam;
                    } else {
                        py = y + tam;
                    }
                    int ancho = fm.stringWidth(peso) + 8;
                    int alto = fm.getHeight();
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(px - ancho / 2, py - alto / 2, ancho, alto, 6, 6);
                    g2.setColor(COLOR_CUADRICULA);
                    g2.drawRoundRect(px - ancho / 2, py - alto / 2, ancho, alto, 6, 6);
                    if (esAristaDelCamino(nodo, destino)) {
                        g2.setColor(COLOR_CAMINO);
                    } else {
                        g2.setColor(Color.DARK_GRAY);
                    }
                    g2.drawString(peso, px - fm.stringWidth(peso) / 2, py + fm.getAscent() / 2 - 1);
                }
            }
        }
    }

    private boolean esAristaDelCamino(int origen, int destino) {
        for (int i = 0; i < camino.size() - 1; i++) {
            if (camino.get(i) == origen && camino.get(i + 1) == destino) {
                return true;
            }
        }
        return false;
    }
}