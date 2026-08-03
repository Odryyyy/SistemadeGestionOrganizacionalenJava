package org.proyecto.controlador;

/**
 * Excepción de negocio lanzada cuando los datos ingresados por el usuario
 * no cumplen las reglas de validación del sistema.
 */
public class ValidacionException extends Exception {

    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
