/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.laberinto.algoritmos;

import itson.laberinto.model.Arista;
import itson.laberinto.model.Laberinto;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
public class OrdenTopologico {

    private static final int BLANCO = 0;
    private static final int GRIS = 1;
    private static final int NEGRO = 2;

    private Laberinto laberinto;
    private int[] color;
    private LinkedList<Integer> enVisita;
    private LinkedList<Integer> pila;
    private List<String> pasos;
    private int tiempo;
    private boolean hayCiclo;

    public OrdenTopologico(Laberinto laberinto) {
        this.laberinto = laberinto;
        int n = laberinto.getGrafo().getNumeroNodos();
        this.color = new int[n];
        this.enVisita = new LinkedList<>();
        this.pila = new LinkedList<>();
        this.pasos = new ArrayList<>();
        this.tiempo = 0;
        this.hayCiclo = false;
    }

    public List<Integer> ordenar() {
        for (int u = 0; u < laberinto.getGrafo().getNumeroNodos(); u++) {
            if (laberinto.esLibre(u) && color[u] == BLANCO) {
                visitar(u);
            }
        }
        return pila;
    }

    private void visitar(int u) {
        tiempo++;
        color[u] = GRIS;
        enVisita.add(u);
        pasos.add("t=" + tiempo + ": descubre " + laberinto.nombre(u)
                + " | Visitando: " + texto(enVisita) + " | Pila: " + texto(pila));

        for (Arista arista : laberinto.getGrafo().getAristas(u)) {
            int v = arista.getDestino();
            if (color[v] == BLANCO) {
                visitar(v);
            } else if (color[v] == GRIS) {
                hayCiclo = true;
            }
        }

        tiempo++;
        color[u] = NEGRO;
        enVisita.removeLast();
        pila.addFirst(u);
        pasos.add("t=" + tiempo + ": termina " + laberinto.nombre(u) + " y entra a la pila"
                + " | Visitando: " + texto(enVisita) + " | Pila: " + texto(pila));
    }

    private String texto(List<Integer> nodos) {
        String resultado = "";
        for (int nodo : nodos) {
            resultado += laberinto.nombre(nodo) + " ";
        }
        if (resultado.isEmpty()) {
            return "vacía";
        }
        return resultado.trim();
    }

    public List<String> getPasos() {
        return pasos;
    }

    public boolean hayCiclo() {
        return hayCiclo;
    }
}