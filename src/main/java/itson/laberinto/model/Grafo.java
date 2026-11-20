/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.laberinto.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
public class Grafo {

    private int numeroNodos;
    private List<List<Arista>> adyacencia;

    public Grafo(int numeroNodos) {
        this.numeroNodos = numeroNodos;
        this.adyacencia = new ArrayList<>();
        for (int i = 0; i < numeroNodos; i++) {
            adyacencia.add(new ArrayList<Arista>());
        }
    }

    public void agregarArista(int origen, int destino, int peso) {
        adyacencia.get(origen).add(new Arista(destino, peso));
    }

    public List<Arista> getAristas(int nodo) {
        return adyacencia.get(nodo);
    }

    public int getNumeroNodos() {
        return numeroNodos;
    }

    public int contarAristas() {
        int total = 0;
        for (List<Arista> lista : adyacencia) {
            total += lista.size();
        }
        return total;
    }
}