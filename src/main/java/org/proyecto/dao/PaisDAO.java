package org.proyecto.dao;

import org.proyecto.conexion.ConexionSQLite;
import org.proyecto.modelo.Pais;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para la entidad Pais. Toda interacción con la base de datos
 * se realiza mediante PreparedStatement para prevenir inyección SQL.
 */
public class PaisDAO {

    private Connection getConexion() {
        return ConexionSQLite.getInstancia().getConexion();
    }

    public void insertar(Pais pais) throws SQLException {
        String sql = "INSERT INTO pais (nombre) VALUES (?)";
        try (PreparedStatement ps = getConexion().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, pais.getNombre());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    pais.setId(rs.getInt(1));
                }
            }
        }
    }

    public void actualizar(Pais pais) throws SQLException {
        String sql = "UPDATE pais SET nombre = ? WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, pais.getNombre());
            ps.setInt(2, pais.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM pais WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Pais> listar() throws SQLException {
        List<Pais> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM pais ORDER BY nombre";
        try (PreparedStatement ps = getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Pais(rs.getInt("id"), rs.getString("nombre")));
            }
        }
        return lista;
    }

    public boolean existeNombre(String nombre, int idExcluir) throws SQLException {
        String sql = "SELECT COUNT(*) FROM pais WHERE LOWER(nombre) = LOWER(?) AND id <> ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
