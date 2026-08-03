package org.proyecto.dao;

import org.proyecto.conexion.ConexionSQLite;
import org.proyecto.modelo.Departamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartamentoDAO {

    private Connection getConexion() {
        return ConexionSQLite.getInstancia().getConexion();
    }

    public void insertar(Departamento d) throws SQLException {
        String sql = "INSERT INTO departamento (nombre, presupuesto) VALUES (?, ?)";
        try (PreparedStatement ps = getConexion().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getNombre());
            ps.setDouble(2, d.getPresupuesto());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    d.setId(rs.getInt(1));
                }
            }
        }
    }

    public void actualizar(Departamento d) throws SQLException {
        String sql = "UPDATE departamento SET nombre = ?, presupuesto = ? WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, d.getNombre());
            ps.setDouble(2, d.getPresupuesto());
            ps.setInt(3, d.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM departamento WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Departamento> listar() throws SQLException {
        List<Departamento> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, presupuesto FROM departamento ORDER BY nombre";
        try (PreparedStatement ps = getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Departamento(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("presupuesto")));
            }
        }
        return lista;
    }

    public boolean existeNombre(String nombre, int idExcluir) throws SQLException {
        String sql = "SELECT COUNT(*) FROM departamento WHERE LOWER(nombre) = LOWER(?) AND id <> ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
