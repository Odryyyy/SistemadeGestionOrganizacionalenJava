package org.proyecto.modelo;

/**
 * Representa la asignación de un empleado a un proyecto,
 * resolviendo la relación N:M entre Empleado y Proyecto.
 */
public class Asignacion {

    private int id;
    private Empleado empleado;
    private Proyecto proyecto;
    private double horasAsignadas;

    public Asignacion() {
    }

    public Asignacion(Empleado empleado, Proyecto proyecto, double horasAsignadas) {
        this.empleado = empleado;
        this.proyecto = proyecto;
        this.horasAsignadas = horasAsignadas;
    }

    public Asignacion(int id, Empleado empleado, Proyecto proyecto, double horasAsignadas) {
        this.id = id;
        this.empleado = empleado;
        this.proyecto = proyecto;
        this.horasAsignadas = horasAsignadas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public double getHorasAsignadas() {
        return horasAsignadas;
    }

    public void setHorasAsignadas(double horasAsignadas) {
        this.horasAsignadas = horasAsignadas;
    }
}
