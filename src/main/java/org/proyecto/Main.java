package org.proyecto;

import org.proyecto.conexion.ConexionSQLite;
import org.proyecto.vista.EstiloUI;
import org.proyecto.vista.FrmLogin; // <--- Importamos el login

import javax.swing.*;

/**
 * Punto de entrada de la aplicación "Sistema de Gestión Organizacional".
 * Configura el Look and Feel, valida la conexión a la base de datos
 * y lanza la ventana de inicio de sesión.
 */
public class Main {

    public static void main(String[] args) {
        EstiloUI.aplicarLookAndFeel();

        SwingUtilities.invokeLater(() -> {
            try {
                // Fuerza la inicialización de la conexión y del esquema de la BD al arrancar.
                ConexionSQLite.getInstancia();

                // Cambiamos VentanaPrincipal por FrmLogin para que sea lo primero en mostrarse
                FrmLogin login = new FrmLogin();
                login.setVisible(true);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "No fue posible iniciar la aplicación.\nDetalle: " + ex.getMessage(),
                        "Error crítico", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> ConexionSQLite.getInstancia().cerrar()));
    }
}