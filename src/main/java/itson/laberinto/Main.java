/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package itson.laberinto;

import itson.laberinto.controller.LaberintoControlador;
import itson.laberinto.view.VentanaLaberinto;
import javax.swing.SwingUtilities;

/**
 *
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LaberintoControlador controlador = new LaberintoControlador();
                VentanaLaberinto ventana = new VentanaLaberinto(controlador);
                ventana.setVisible(true);
            }
        });
    }
}
