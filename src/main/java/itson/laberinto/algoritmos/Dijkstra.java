/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.laberinto.algoritmos;

import itson.laberinto.model.Arista;
import itson.laberinto.model.Laberinto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
public class Dijkstra {

    private static final int INFINITO = Integer.MAX_VALUE;

    private Laberinto laberinto;
    private int[] distancia;
    private int[] anterior;
    private boolean[] visitado;
    private List<String> pasos;
    private int salidaMasCercana;

    public Dijkstra(Laberinto laberinto) {
        this.laberinto = laberinto;
        int n = laberinto.getGrafo().getNumeroNodos();
        this.distancia = new int[n];
        this.anterior = new int[n];
        this.visitado = new boolean[n];
        this.pasos = new ArrayList<>();
        this.salidaMasCercana = -1;
    }

    public List<Integer> caminoMasCorto(int origen) {
        for (int i = 0; i < distancia.length; i++) {
            distancia[i] = INFINITO;
            anterior[i] = -1;
        }
        distancia[origen] = 0;

        PriorityQueue<NodoDistancia> cola = new PriorityQueue<>();
        cola.add(new NodoDistancia(origen, 0));
        pasos.add("Paso 0: d" + laberinto.nombre(origen) + " = 0, los demás en infinito | Cola: " + texto(cola));

        int paso = 1;
        while (!cola.isEmpty()) {
            NodoDistancia actual = cola.poll();
            int u = actual.getNodo();
            if (visitado[u]) {
                continue;
            }
            visitado[u] = true;

            if (laberinto.esSalida(u)) {
                salidaMasCercana = u;
                pasos.add("Paso " + paso + ": sale " + laberinto.nombre(u) + " d=" + distancia[u]
                        + ", es salida y termina | Cola: " + texto(cola));
                break;
            }

            String relajaciones = "";
            for (Arista arista : laberinto.getGrafo().getAristas(u)) {
                int v = arista.getDestino();
                if (visitado[v]) {
                    continue;
                }
                int nueva = distancia[u] + arista.getPeso();
                String antes = "infinito";
                if (distancia[v] != INFINITO) {
                    antes = String.valueOf(distancia[v]);
                }
                relajaciones += laberinto.nombre(v) + ": " + distancia[u] + "+" + arista.getPeso() + "=" + nueva;
                if (nueva < distancia[v]) {
                    distancia[v] = nueva;
                    anterior[v] = u;
                    cola.add(new NodoDistancia(v, nueva));
                    relajaciones += " < " + antes + ", se actualiza; ";
                } else {
                    relajaciones += " >= " + antes + ", no cambia; ";
                }
            }
            if (relajaciones.isEmpty()) {
                relajaciones = "sin vecinos pendientes";
            } else {
                relajaciones = relajaciones.substring(0, relajaciones.length() - 2);
            }
            pasos.add("Paso " + paso + ": sale " + laberinto.nombre(u) + " d=" + distancia[u]
                    + " | " + relajaciones.trim() + " | Cola: " + texto(cola));
            paso++;
        }

        List<Integer> camino = new ArrayList<>();
        if (salidaMasCercana == -1) {
            return camino;
        }
        int nodo = salidaMasCercana;
        while (nodo != -1) {
            camino.add(0, nodo);
            nodo = anterior[nodo];
        }
        return camino;
    }

    private String texto(PriorityQueue<NodoDistancia> cola) {
        List<NodoDistancia> ordenados = new ArrayList<>(cola);
        Collections.sort(ordenados);
        String resultado = "";
        for (NodoDistancia nd : ordenados) {
            if (!visitado[nd.getNodo()]) {
                resultado += laberinto.nombre(nd.getNodo()) + ":" + nd.getDistancia() + " ";
            }
        }
        if (resultado.isEmpty()) {
            return "vacía";
        }
        return resultado.trim();
    }

    public List<String> getPasos() {
        return pasos;
    }

    public int getDistancia(int nodo) {
        return distancia[nodo];
    }

    public int getSalidaMasCercana() {
        return salidaMasCercana;
    }
}