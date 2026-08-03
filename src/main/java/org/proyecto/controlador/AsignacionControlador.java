package org.proyecto.controlador;

import org.proyecto.dao.AsignacionDAO;
import org.proyecto.modelo.Asignacion;

import java.sql.SQLException;
import java.util.List;

public class AsignacionControlador {

    private final AsignacionDAO dao = new AsignacionDAO();

    public void guardar(Asignacion a) throws ValidacionException, SQLException {
        validar(a);
        if (a.getId() == 0) {
            dao.insertar(a);
        } else {
            dao.actualizar(a);
        }
    }

    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }

    public List<Asignacion> listar() throws SQLException {
        return dao.listar();
    }

    private void validar(Asignacion a) throws ValidacionException, SQLException {
        if (a.getEmpleado() == null) {
            throw new ValidacionException("Debe seleccionar un empleado.");
        }
        if (a.getProyecto() == null) {
            throw new ValidacionException("Debe seleccionar un proyecto.");
        }
        if (a.getHorasAsignadas() <= 0) {
            throw new ValidacionException("Las horas asignadas deben ser mayores que cero.");
        }
        if (a.getHorasAsignadas() > 200) {
            throw new ValidacionException("Las horas asignadas no pueden superar 200 por proyecto.");
        }
        if (dao.existeAsignacion(a.getEmpleado().getId(), a.getProyecto().getId(), a.getId())) {
            throw new ValidacionException("Este empleado ya está asignado a ese proyecto.");
        }
    }
}
