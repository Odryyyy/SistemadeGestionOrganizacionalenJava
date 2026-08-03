package org.proyecto.controlador;

import org.proyecto.dao.CargoDAO;
import org.proyecto.modelo.Cargo;

import java.sql.SQLException;
import java.util.List;

public class CargoControlador {

    private final CargoDAO dao = new CargoDAO();

    public void guardar(Cargo c) throws ValidacionException, SQLException {
        validar(c);
        if (c.getId() == 0) {
            dao.insertar(c);
        } else {
            dao.actualizar(c);
        }
    }

    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }

    public List<Cargo> listar() throws SQLException {
        return dao.listar();
    }

    private void validar(Cargo c) throws ValidacionException, SQLException {
        if (c.getNombre() == null || c.getNombre().trim().isEmpty()) {
            throw new ValidacionException("El nombre del cargo es obligatorio.");
        }
        if (c.getSalarioBase() <= 0) {
            throw new ValidacionException("El salario base debe ser mayor que cero.");
        }
        if (dao.existeNombre(c.getNombre().trim(), c.getId())) {
            throw new ValidacionException("Ya existe un cargo registrado con ese nombre.");
        }
    }
}
