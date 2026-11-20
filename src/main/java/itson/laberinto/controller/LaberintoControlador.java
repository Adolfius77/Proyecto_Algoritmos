/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.laberinto.controller;

import itson.laberinto.algoritmos.BFS;
import itson.laberinto.algoritmos.Dijkstra;
import itson.laberinto.algoritmos.OrdenTopologico;
import itson.laberinto.model.Laberinto;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
public class LaberintoControlador {

    private Laberinto laberinto;
    private List<Integer> camino;
    private String resultado;

    public LaberintoControlador() {
        this.camino = new ArrayList<>();
        this.resultado = "";
    }

    public void generar(int filas, int columnas, long semilla) {
        laberinto = Laberinto.generar(filas, columnas, semilla);
        limpiarResultado();
    }

    private void limpiarResultado() {
        camino = new ArrayList<>();
        resultado = "Laberinto de " + laberinto.getFilas() + " x " + laberinto.getColumnas()
                + "\nSemilla: " + laberinto.getSemilla()
                + "\nNodos: " + laberinto.contarLibres()
                + "\nAristas: " + laberinto.getGrafo().contarAristas()
                + "\nEntradas: " + laberinto.getEntradas().size()
                + "\nSalidas: " + laberinto.getSalidas().size();
    }

    public void buscarCamino(int entrada) {
        camino = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("Semilla: ").append(laberinto.getSemilla()).append("\n");

        BFS bfs = new BFS(laberinto);
        boolean hayCamino = bfs.existeCamino();
        if (hayCamino) {
            sb.append("BFS: sí existe camino, llega a la salida ")
                    .append(laberinto.nombre(bfs.getSalidaEncontrada())).append("\n");
        } else {
            sb.append("BFS: no existe camino entre ninguna entrada y ninguna salida\n");
        }

        OrdenTopologico topologico = new OrdenTopologico(laberinto);
        List<Integer> orden = topologico.ordenar();
        if (topologico.hayCiclo()) {
            sb.append("Orden topológico: el grafo tiene un ciclo, no es un DAG\n");
        } else {
            sb.append("Orden topológico: ").append(unir(orden)).append("\n");
        }

        Dijkstra dijkstra = null;
        if (hayCamino) {
            dijkstra = new Dijkstra(laberinto);
            camino = dijkstra.caminoMasCorto(entrada);
            if (camino.isEmpty()) {
                sb.append("Dijkstra: desde ").append(laberinto.nombre(entrada))
                        .append(" no se puede llegar a ninguna salida\n");
            } else {
                int salida = dijkstra.getSalidaMasCercana();
                sb.append("Dijkstra: la salida más cercana a ").append(laberinto.nombre(entrada))
                        .append(" es ").append(laberinto.nombre(salida))
                        .append(" con costo ").append(dijkstra.getDistancia(salida)).append("\n");
                sb.append("Camino: ").append(unir(camino)).append("\n");
            }
        }

        sb.append("\n--- Pasos BFS ---\n");
        agregarPasos(sb, bfs.getPasos());
        sb.append("\n--- Pasos orden topológico ---\n");
        agregarPasos(sb, topologico.getPasos());
        if (dijkstra != null) {
            sb.append("\n--- Pasos Dijkstra ---\n");
            agregarPasos(sb, dijkstra.getPasos());
        }
        resultado = sb.toString();
    }

    private void agregarPasos(StringBuilder sb, List<String> pasos) {
        for (String paso : pasos) {
            sb.append(paso).append("\n");
        }
    }

    private String unir(List<Integer> nodos) {
        String texto = "";
        for (int i = 0; i < nodos.size(); i++) {
            if (i > 0) {
                texto += " -> ";
            }
            texto += laberinto.nombre(nodos.get(i));
        }
        return texto;
    }

    public Laberinto getLaberinto() {
        return laberinto;
    }

    public List<Integer> getCamino() {
        return camino;
    }

    public String getResultado() {
        return resultado;
    }
}