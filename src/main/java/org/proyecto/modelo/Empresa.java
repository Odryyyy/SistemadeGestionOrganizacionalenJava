package org.proyecto.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad agregadora del sistema. Aplica el principio de Composición:
 * la Empresa contiene y administra las colecciones de Departamentos,
 * Empleados y Proyectos.
 */
public class Empresa {

    private String nombre;
    private List<Departamento> listaDepartamentos;
    private List<Empleado> listaEmpleados;
    private List<Proyecto> listaProyectos;

    public Empresa(String nombre) {
        this.nombre = nombre;
        this.listaDepartamentos = new ArrayList<>();
        this.listaEmpleados = new ArrayList<>();
        this.listaProyectos = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Departamento> getListaDepartamentos() {
        return listaDepartamentos;
    }

    public void agregarDepartamento(Departamento departamento) {
        this.listaDepartamentos.add(departamento);
    }

    public List<Empleado> getListaEmpleados() {
        return listaEmpleados;
    }

    public void agregarEmpleado(Empleado empleado) {
        this.listaEmpleados.add(empleado);
    }

    public List<Proyecto> getListaProyectos() {
        return listaProyectos;
    }

    public void agregarProyecto(Proyecto proyecto) {
        this.listaProyectos.add(proyecto);
    }
}
