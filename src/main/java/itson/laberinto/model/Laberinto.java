/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.laberinto.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
public class Laberinto {

    private static final double PROBABILIDAD_PARED = 0.3;

    private int filas;
    private int columnas;
    private long semilla;
    private boolean[][] libre;
    private int[][] pesoDerecha;
    private int[][] pesoAbajo;
    private List<Integer> entradas;
    private List<Integer> salidas;
    private Grafo grafo;

    private Laberinto(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.libre = new boolean[filas][columnas];
        this.pesoDerecha = new int[filas][columnas];
        this.pesoAbajo = new int[filas][columnas];
        this.entradas = new ArrayList<>();
        this.salidas = new ArrayList<>();
    }

    public static Laberinto generar(int filas, int columnas, long semilla) {
        Laberinto laberinto = new Laberinto(filas, columnas);
        laberinto.semilla = semilla;
        Random random = new Random(semilla);

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                laberinto.libre[f][c] = random.nextDouble() >= PROBABILIDAD_PARED;
            }
        }

        if (!laberinto.hayLibreEnColumna(0)) {
            laberinto.libre[random.nextInt(filas)][0] = true;
        }
        if (!laberinto.hayLibreEnColumna(columnas - 1)) {
            laberinto.libre[random.nextInt(filas)][columnas - 1] = true;
        }

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                if (c + 1 < columnas) {
                    laberinto.pesoDerecha[f][c] = random.nextInt(100) + 1;
                }
                if (f + 1 < filas) {
                    laberinto.pesoAbajo[f][c] = random.nextInt(100) + 1;
                }
            }
        }

        laberinto.construirGrafo();
        return laberinto;
    }

    private boolean hayLibreEnColumna(int columna) {
        for (int f = 0; f < filas; f++) {
            if (libre[f][columna]) {
                return true;
            }
        }
        return false;
    }

    private void construirGrafo() {
        grafo = new Grafo(filas * columnas);
        entradas.clear();
        salidas.clear();

        for (int f = 0; f < filas; f++) {
            if (libre[f][0]) {
                entradas.add(getNodo(f, 0));
            }
            if (libre[f][columnas - 1]) {
                salidas.add(getNodo(f, columnas - 1));
            }
        }

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                if (!libre[f][c]) {
                    continue;
                }
                if (c + 1 < columnas && libre[f][c + 1]) {
                    grafo.agregarArista(getNodo(f, c), getNodo(f, c + 1), pesoDerecha[f][c]);
                }
                if (f + 1 < filas && libre[f + 1][c]) {
                    grafo.agregarArista(getNodo(f, c), getNodo(f + 1, c), pesoAbajo[f][c]);
                }
            }
        }
    }

    public int getNodo(int fila, int columna) {
        return fila * columnas + columna;
    }

    public int getFila(int nodo) {
        return nodo / columnas;
    }

    public int getColumna(int nodo) {
        return nodo % columnas;
    }

    public String nombre(int nodo) {
        return "(" + (getFila(nodo) + 1) + "," + (getColumna(nodo) + 1) + ")";
    }

    public boolean esLibre(int fila, int columna) {
        return libre[fila][columna];
    }

    public boolean esLibre(int nodo) {
        return libre[getFila(nodo)][getColumna(nodo)];
    }

    public boolean esEntrada(int nodo) {
        return entradas.contains(nodo);
    }

    public boolean esSalida(int nodo) {
        return salidas.contains(nodo);
    }

    public int contarLibres() {
        int total = 0;
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                if (libre[f][c]) {
                    total++;
                }
            }
        }
        return total;
    }

    public long getSemilla() {
        return semilla;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public List<Integer> getEntradas() {
        return entradas;
    }

    public List<Integer> getSalidas() {
        return salidas;
    }

    public Grafo getGrafo() {
        return grafo;
    }
}