package org.proyecto.controlador;

import org.proyecto.dao.DepartamentoDAO;
import org.proyecto.modelo.Departamento;

import java.sql.SQLException;
import java.util.List;

public class DepartamentoControlador {

    private final DepartamentoDAO dao = new DepartamentoDAO();

    public void guardar(Departamento d) throws ValidacionException, SQLException {
        validar(d);
        if (d.getId() == 0) {
            dao.insertar(d);
        } else {
            dao.actualizar(d);
        }
    }

    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }

    public List<Departamento> listar() throws SQLException {
        return dao.listar();
    }

    private void validar(Departamento d) throws ValidacionException, SQLException {
        if (d.getNombre() == null || d.getNombre().trim().isEmpty()) {
            throw new ValidacionException("El nombre del departamento es obligatorio.");
        }
        if (d.getPresupuesto() < 0) {
            throw new ValidacionException("El presupuesto no puede ser negativo.");
        }
        if (dao.existeNombre(d.getNombre().trim(), d.getId())) {
            throw new ValidacionException("Ya existe un departamento registrado con ese nombre.");
        }
    }
}
