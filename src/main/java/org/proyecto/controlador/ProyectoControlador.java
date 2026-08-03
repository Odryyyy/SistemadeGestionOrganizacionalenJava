package org.proyecto.controlador;

import org.proyecto.dao.ProyectoDAO;
import org.proyecto.modelo.Proyecto;

import java.sql.SQLException;
import java.util.List;

public class ProyectoControlador {

    private final ProyectoDAO dao = new ProyectoDAO();

    public void guardar(Proyecto p) throws ValidacionException, SQLException {
        validar(p);
        if (p.getId() == 0) {
            dao.insertar(p);
        } else {
            dao.actualizar(p);
        }
    }

    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }

    public List<Proyecto> listar() throws SQLException {
        return dao.listar();
    }

    private void validar(Proyecto p) throws ValidacionException, SQLException {
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            throw new ValidacionException("El nombre del proyecto es obligatorio.");
        }
        if (p.getPresupuesto() < 0) {
            throw new ValidacionException("El presupuesto no puede ser negativo.");
        }
        if (dao.existeNombre(p.getNombre().trim(), p.getId())) {
            throw new ValidacionException("Ya existe un proyecto registrado con ese nombre.");
        }
    }
}
