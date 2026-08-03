package org.proyecto.dao;

import org.proyecto.conexion.ConexionSQLite;
import org.proyecto.modelo.Proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProyectoDAO {

    private Connection getConexion() {
        return ConexionSQLite.getInstancia().getConexion();
    }

    public void insertar(Proyecto p) throws SQLException {
        String sql = "INSERT INTO proyecto (nombre, presupuesto) VALUES (?, ?)";
        try (PreparedStatement ps = getConexion().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPresupuesto());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getInt(1));
                }
            }
        }
    }

    public void actualizar(Proyecto p) throws SQLException {
        String sql = "UPDATE proyecto SET nombre = ?, presupuesto = ? WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPresupuesto());
            ps.setInt(3, p.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM proyecto WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Proyecto> listar() throws SQLException {
        List<Proyecto> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, presupuesto FROM proyecto ORDER BY nombre";
        try (PreparedStatement ps = getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Proyecto(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("presupuesto")));
            }
        }
        return lista;
    }

    public boolean existeNombre(String nombre, int idExcluir) throws SQLException {
        String sql = "SELECT COUNT(*) FROM proyecto WHERE LOWER(nombre) = LOWER(?) AND id <> ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
