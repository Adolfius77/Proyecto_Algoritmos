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
import java.util.Queue;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
public class BFS {

    private Laberinto laberinto;
    private boolean[] visitado;
    private List<String> pasos;
    private int salidaEncontrada;

    public BFS(Laberinto laberinto) {
        this.laberinto = laberinto;
        this.visitado = new boolean[laberinto.getGrafo().getNumeroNodos()];
        this.pasos = new ArrayList<>();
        this.salidaEncontrada = -1;
    }

    public boolean existeCamino() {
        Queue<Integer> cola = new LinkedList<>();
        for (int entrada : laberinto.getEntradas()) {
            visitado[entrada] = true;
            cola.add(entrada);
        }
        pasos.add("Paso 0: se encolan las entradas | Cola: " + texto(cola));

        int paso = 1;
        while (!cola.isEmpty()) {
            int u = cola.poll();
            if (laberinto.esSalida(u)) {
                salidaEncontrada = u;
                pasos.add("Paso " + paso + ": sale " + laberinto.nombre(u)
                        + ", es una salida y termina el recorrido | Cola: " + texto(cola));
                return true;
            }

            List<Integer> nuevos = new ArrayList<>();
            for (Arista arista : laberinto.getGrafo().getAristas(u)) {
                int v = arista.getDestino();
                if (!visitado[v]) {
                    visitado[v] = true;
                    cola.add(v);
                    nuevos.add(v);
                }
            }

            String entran = "ninguno";
            if (!nuevos.isEmpty()) {
                entran = texto(nuevos);
            }
            pasos.add("Paso " + paso + ": sale " + laberinto.nombre(u) + ", entran " + entran
                    + " | Cola: " + texto(cola));
            paso++;
        }
        pasos.add("La cola se vació sin llegar a una salida");
        return false;
    }

    private String texto(Iterable<Integer> nodos) {
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

    public int getSalidaEncontrada() {
        return salidaEncontrada;
    }
}
