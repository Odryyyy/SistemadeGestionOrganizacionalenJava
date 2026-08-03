package org.proyecto.controlador;

import org.proyecto.dao.EmpleadoDAO;
import org.proyecto.modelo.Empleado;

import java.sql.SQLException;
import java.util.List;

public class EmpleadoControlador {

    private final EmpleadoDAO dao = new EmpleadoDAO();

    public void guardar(Empleado e) throws ValidacionException, SQLException {
        validar(e);
        if (e.getId() == 0) {
            dao.insertar(e);
        } else {
            dao.actualizar(e);
        }
    }

    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }

    public List<Empleado> listar() throws SQLException {
        return dao.listar();
    }

    private void validar(Empleado e) throws ValidacionException {
        if (e.getNombre() == null || e.getNombre().trim().isEmpty()) {
            throw new ValidacionException("El nombre del empleado es obligatorio.");
        }
        if (e.getApellido() == null || e.getApellido().trim().isEmpty()) {
            throw new ValidacionException("El apellido del empleado es obligatorio.");
        }
        if (e.getSalario() <= 0) {
            throw new ValidacionException("El salario debe ser mayor que cero.");
        }
        if (e.getPais() == null) {
            throw new ValidacionException("Debe seleccionar un país.");
        }
        if (e.getDepartamento() == null) {
            throw new ValidacionException("Debe seleccionar un departamento.");
        }
        if (e.getCargo() == null) {
            throw new ValidacionException("Debe seleccionar un cargo.");
        }
    }
}
