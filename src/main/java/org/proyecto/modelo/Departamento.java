package org.proyecto.modelo;

/**
 * Representa un departamento de la empresa. Un departamento tiene muchos empleados (1:N).
 */
public class Departamento {

    private int id;
    private String nombre;
    private double presupuesto;

    public Departamento() {
    }

    public Departamento(String nombre, double presupuesto) {
        this.nombre = nombre;
        this.presupuesto = presupuesto;
    }

    public Departamento(int id, String nombre, double presupuesto) {
        this.id = id;
        this.nombre = nombre;
        this.presupuesto = presupuesto;
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

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
