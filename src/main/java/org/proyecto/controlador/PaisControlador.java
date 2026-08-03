package org.proyecto.controlador;

import org.proyecto.dao.PaisDAO;
import org.proyecto.modelo.Pais;

import java.sql.SQLException;
import java.util.List;

/**
 * Orquesta las operaciones sobre Pais, aplicando validaciones antes de
 * delegar la persistencia al PaisDAO. Separa la lógica de negocio de la vista.
 */
public class PaisControlador {

    private final PaisDAO dao = new PaisDAO();

    public void guardar(Pais pais) throws ValidacionException, SQLException {
        validar(pais);
        if (pais.getId() == 0) {
            dao.insertar(pais);
        } else {
            dao.actualizar(pais);
        }
    }

    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }

    public List<Pais> listar() throws SQLException {
        return dao.listar();
    }

    private void validar(Pais pais) throws ValidacionException, SQLException {
        if (pais.getNombre() == null || pais.getNombre().trim().isEmpty()) {
            throw new ValidacionException("El nombre del país es obligatorio.");
        }
        if (pais.getNombre().trim().length() < 3) {
            throw new ValidacionException("El nombre del país debe tener al menos 3 caracteres.");
        }
        if (dao.existeNombre(pais.getNombre().trim(), pais.getId())) {
            throw new ValidacionException("Ya existe un país registrado con ese nombre.");
        }
    }
}
