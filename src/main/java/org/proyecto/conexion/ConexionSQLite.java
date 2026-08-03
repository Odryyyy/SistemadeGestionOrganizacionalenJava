package org.proyecto.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase responsable de administrar la conexión JDBC hacia la base de datos SQLite.
 * Implementa el patrón Singleton para garantizar una única instancia de conexión
 * activa durante la ejecución de la aplicación.
 */
public class ConexionSQLite {

    private static final String NOMBRE_BD = "organizacion.db";
    private static final String URL = "jdbc:sqlite:" + NOMBRE_BD;

    private static ConexionSQLite instancia;
    private Connection conexion;

    private ConexionSQLite() {
        try {
            Class.forName("org.sqlite.JDBC");
            conexion = DriverManager.getConnection(URL);
            // Habilita el chequeo de llaves foráneas (SQLite lo trae desactivado por defecto)
            try (Statement st = conexion.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON;");
            }
            inicializarEsquema();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("No fue posible establecer conexión con la base de datos: "
                    + e.getMessage(), e);
        }
    }

    public static synchronized ConexionSQLite getInstancia() {
        if (instancia == null) {
            instancia = new ConexionSQLite();
        }
        return instancia;
    }

    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al validar la conexión: " + e.getMessage(), e);
        }
        return conexion;
    }

    /**
     * Crea las tablas del sistema si aún no existen, de modo que la aplicación
     * funcione con solo ejecutar el programa (no requiere pasos manuales).
     */
    private void inicializarEsquema() throws SQLException {
        String[] sentencias = {
            // Tabla de usuarios añadida para el inicio de sesión
            "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "usuario TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "rol TEXT NOT NULL)",

            "CREATE TABLE IF NOT EXISTS pais (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL UNIQUE)",

            "CREATE TABLE IF NOT EXISTS departamento (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL UNIQUE," +
                "presupuesto REAL NOT NULL DEFAULT 0)",

            "CREATE TABLE IF NOT EXISTS cargo (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL UNIQUE," +
                "salarioBase REAL NOT NULL DEFAULT 0)",

            "CREATE TABLE IF NOT EXISTS empleado (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "apellido TEXT NOT NULL," +
                "salario REAL NOT NULL DEFAULT 0," +
                "idPais INTEGER NOT NULL," +
                "idDepartamento INTEGER NOT NULL," +
                "idCargo INTEGER NOT NULL," +
                "FOREIGN KEY (idPais) REFERENCES pais(id) ON DELETE RESTRICT," +
                "FOREIGN KEY (idDepartamento) REFERENCES departamento(id) ON DELETE RESTRICT," +
                "FOREIGN KEY (idCargo) REFERENCES cargo(id) ON DELETE RESTRICT)",

            "CREATE TABLE IF NOT EXISTS proyecto (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL UNIQUE," +
                "presupuesto REAL NOT NULL DEFAULT 0)",

            "CREATE TABLE IF NOT EXISTS asignacion (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idEmpleado INTEGER NOT NULL," +
                "idProyecto INTEGER NOT NULL," +
                "horasAsignadas REAL NOT NULL DEFAULT 0," +
                "FOREIGN KEY (idEmpleado) REFERENCES empleado(id) ON DELETE CASCADE," +
                "FOREIGN KEY (idProyecto) REFERENCES proyecto(id) ON DELETE CASCADE)"
        };

        try (Statement st = conexion.createStatement()) {
            for (String sql : sentencias) {
                st.execute(sql);
            }
            // Inserta el usuario administrador por defecto si no existe
            st.execute("INSERT OR IGNORE INTO usuarios (usuario, password, rol) VALUES ('admin', '1234', 'Administrador');");
        }
    }

    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}