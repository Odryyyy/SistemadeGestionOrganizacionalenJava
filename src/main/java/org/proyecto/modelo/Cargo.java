package org.proyecto.modelo;

/**
 * Representa un cargo dentro de la empresa. Un cargo puede estar asociado a varios empleados (1:N).
 */
public class Cargo {

    private int id;
    private String nombre;
    private double salarioBase;

    public Cargo() {
    }

    public Cargo(String nombre, double salarioBase) {
        this.nombre = nombre;
        this.salarioBase = salarioBase;
    }

    public Cargo(int id, String nombre, double salarioBase) {
        this.id = id;
        this.nombre = nombre;
        this.salarioBase = salarioBase;
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

    public double getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(double salarioBase) {
        this.salarioBase = salarioBase;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
