package org.proyecto.dao;

import org.proyecto.conexion.ConexionSQLite;
import org.proyecto.modelo.Asignacion;
import org.proyecto.modelo.Empleado;
import org.proyecto.modelo.Proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsignacionDAO {

    private Connection getConexion() {
        return ConexionSQLite.getInstancia().getConexion();
    }

    public void insertar(Asignacion a) throws SQLException {
        String sql = "INSERT INTO asignacion (idEmpleado, idProyecto, horasAsignadas) VALUES (?, ?, ?)";
        try (PreparedStatement ps = getConexion().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getEmpleado().getId());
            ps.setInt(2, a.getProyecto().getId());
            ps.setDouble(3, a.getHorasAsignadas());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    a.setId(rs.getInt(1));
                }
            }
        }
    }

    public void actualizar(Asignacion a) throws SQLException {
        String sql = "UPDATE asignacion SET idEmpleado = ?, idProyecto = ?, horasAsignadas = ? WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, a.getEmpleado().getId());
            ps.setInt(2, a.getProyecto().getId());
            ps.setDouble(3, a.getHorasAsignadas());
            ps.setInt(4, a.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM asignacion WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Verifica si un empleado ya está asignado a un proyecto específico,
     * para evitar duplicados (regla de negocio / validación).
     */
    public boolean existeAsignacion(int idEmpleado, int idProyecto, int idExcluir) throws SQLException {
        String sql = "SELECT COUNT(*) FROM asignacion WHERE idEmpleado = ? AND idProyecto = ? AND id <> ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, idEmpleado);
            ps.setInt(2, idProyecto);
            ps.setInt(3, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public List<Asignacion> listar() throws SQLException {
        // Se cargan primero los empleados y proyectos completos en mapas para reconstruir los objetos.
        Map<Integer, Empleado> empleados = new HashMap<>();
        for (Empleado e : new EmpleadoDAO().listar()) {
            empleados.put(e.getId(), e);
        }
        Map<Integer, Proyecto> proyectos = new HashMap<>();
        for (Proyecto p : new ProyectoDAO().listar()) {
            proyectos.put(p.getId(), p);
        }

        List<Asignacion> lista = new ArrayList<>();
        String sql = "SELECT id, idEmpleado, idProyecto, horasAsignadas FROM asignacion ORDER BY id";
        try (PreparedStatement ps = getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Empleado emp = empleados.get(rs.getInt("idEmpleado"));
                Proyecto proy = proyectos.get(rs.getInt("idProyecto"));
                if (emp != null && proy != null) {
                    lista.add(new Asignacion(rs.getInt("id"), emp, proy, rs.getDouble("horasAsignadas")));
                }
            }
        }
        return lista;
    }
}
