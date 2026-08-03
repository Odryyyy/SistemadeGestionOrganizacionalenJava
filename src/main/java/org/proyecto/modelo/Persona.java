package org.proyecto.modelo;

/**
 * Clase abstracta que representa a una persona en general.
 * Aplica el principio de Herencia: Empleado hereda de Persona.
 */
public abstract class Persona {

    private int id;
    private String nombre;
    private String apellido;

    public Persona() {
    }

    public Persona(int id, String nombre, String apellido) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Método abstracto que cada subclase debe implementar,
     * demostrando el principio de Polimorfismo.
     */
    public abstract String getDescripcion();

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
}
