package org.proyecto.dao;

import org.proyecto.conexion.ConexionSQLite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDao {

    public boolean validarAcceso(String usuario, String password) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND password = ?";
        
        // Usamos el Singleton de tu clase ConexionSQLite
        try (Connection con = ConexionSQLite.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, usuario);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); 
            }
        } catch (SQLException e) {
            System.out.println("Error al validar usuario: " + e.getMessage());
            return false;
        }
    }
}