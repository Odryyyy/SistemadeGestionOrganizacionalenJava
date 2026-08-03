package org.proyecto.dao;

import org.proyecto.conexion.ConexionSQLite;
import org.proyecto.modelo.Cargo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CargoDAO {

    private Connection getConexion() {
        return ConexionSQLite.getInstancia().getConexion();
    }

    public void insertar(Cargo c) throws SQLException {
        String sql = "INSERT INTO cargo (nombre, salarioBase) VALUES (?, ?)";
        try (PreparedStatement ps = getConexion().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNombre());
            ps.setDouble(2, c.getSalarioBase());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setId(rs.getInt(1));
                }
            }
        }
    }

    public void actualizar(Cargo c) throws SQLException {
        String sql = "UPDATE cargo SET nombre = ?, salarioBase = ? WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setDouble(2, c.getSalarioBase());
            ps.setInt(3, c.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM cargo WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Cargo> listar() throws SQLException {
        List<Cargo> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, salarioBase FROM cargo ORDER BY nombre";
        try (PreparedStatement ps = getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Cargo(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("salarioBase")));
            }
        }
        return lista;
    }

    public boolean existeNombre(String nombre, int idExcluir) throws SQLException {
        String sql = "SELECT COUNT(*) FROM cargo WHERE LOWER(nombre) = LOWER(?) AND id <> ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
