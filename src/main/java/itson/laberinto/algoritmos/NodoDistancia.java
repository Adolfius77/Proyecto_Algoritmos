/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.laberinto.algoritmos;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
public class NodoDistancia implements Comparable<NodoDistancia> {

    private int nodo;
    private int distancia;

    public NodoDistancia(int nodo, int distancia) {
        this.nodo = nodo;
        this.distancia = distancia;
    }

    public int getNodo() {
        return nodo;
    }

    public int getDistancia() {
        return distancia;
    }

    @Override
    public int compareTo(NodoDistancia otro) {
        if (distancia != otro.distancia) {
            return Integer.compare(distancia, otro.distancia);
        }
        return Integer.compare(nodo, otro.nodo);
    }
}
