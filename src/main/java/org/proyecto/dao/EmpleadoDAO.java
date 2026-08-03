package org.proyecto.dao;

import org.proyecto.conexion.ConexionSQLite;
import org.proyecto.modelo.Cargo;
import org.proyecto.modelo.Departamento;
import org.proyecto.modelo.Empleado;
import org.proyecto.modelo.Pais;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    private static final String SELECT_BASE =
            "SELECT e.id, e.nombre, e.apellido, e.salario, " +
            "       p.id AS paisId, p.nombre AS paisNombre, " +
            "       d.id AS depId, d.nombre AS depNombre, d.presupuesto AS depPresupuesto, " +
            "       c.id AS cargoId, c.nombre AS cargoNombre, c.salarioBase AS cargoSalarioBase " +
            "FROM empleado e " +
            "JOIN pais p ON e.idPais = p.id " +
            "JOIN departamento d ON e.idDepartamento = d.id " +
            "JOIN cargo c ON e.idCargo = c.id ";

    private Connection getConexion() {
        return ConexionSQLite.getInstancia().getConexion();
    }

    public void insertar(Empleado emp) throws SQLException {
        String sql = "INSERT INTO empleado (nombre, apellido, salario, idPais, idDepartamento, idCargo) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConexion().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, emp.getNombre());
            ps.setString(2, emp.getApellido());
            ps.setDouble(3, emp.getSalario());
            ps.setInt(4, emp.getPais().getId());
            ps.setInt(5, emp.getDepartamento().getId());
            ps.setInt(6, emp.getCargo().getId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    emp.setId(rs.getInt(1));
                }
            }
        }
    }

    public void actualizar(Empleado emp) throws SQLException {
        String sql = "UPDATE empleado SET nombre = ?, apellido = ?, salario = ?, " +
                     "idPais = ?, idDepartamento = ?, idCargo = ? WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, emp.getNombre());
            ps.setString(2, emp.getApellido());
            ps.setDouble(3, emp.getSalario());
            ps.setInt(4, emp.getPais().getId());
            ps.setInt(5, emp.getDepartamento().getId());
            ps.setInt(6, emp.getCargo().getId());
            ps.setInt(7, emp.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM empleado WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Empleado> listar() throws SQLException {
        List<Empleado> lista = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY e.apellido, e.nombre";
        try (PreparedStatement ps = getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearEmpleado(rs));
            }
        }
        return lista;
    }

    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        Pais pais = new Pais(rs.getInt("paisId"), rs.getString("paisNombre"));
        Departamento depto = new Departamento(rs.getInt("depId"), rs.getString("depNombre"), rs.getDouble("depPresupuesto"));
        Cargo cargo = new Cargo(rs.getInt("cargoId"), rs.getString("cargoNombre"), rs.getDouble("cargoSalarioBase"));
        return new Empleado(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido"),
                rs.getDouble("salario"), pais, depto, cargo);
    }
}
