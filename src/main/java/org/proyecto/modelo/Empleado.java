package org.proyecto.modelo;

/**
 * Representa un empleado de la empresa.
 * Aplica Herencia (extiende Persona) y Asociación (referencias a Pais, Departamento y Cargo).
 */
public class Empleado extends Persona {

    private double salario;
    private Pais pais;
    private Departamento departamento;
    private Cargo cargo;

    public Empleado() {
        super();
    }

    public Empleado(String nombre, String apellido, double salario,
                     Pais pais, Departamento departamento, Cargo cargo) {
        super(0, nombre, apellido);
        this.salario = salario;
        this.pais = pais;
        this.departamento = departamento;
        this.cargo = cargo;
    }

    public Empleado(int id, String nombre, String apellido, double salario,
                     Pais pais, Departamento departamento, Cargo cargo) {
        super(id, nombre, apellido);
        this.salario = salario;
        this.pais = pais;
        this.departamento = departamento;
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    /**
     * Implementación del método abstracto de Persona (Polimorfismo).
     */
    @Override
    public String getDescripcion() {
        return getNombre() + " " + getApellido() + " - " + (cargo != null ? cargo.getNombre() : "Sin cargo");
    }

    @Override
    public String toString() {
        return getNombre() + " " + getApellido();
    }
}
