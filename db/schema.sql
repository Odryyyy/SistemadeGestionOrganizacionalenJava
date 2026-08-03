-- =====================================================================
-- Script de creación de Base de Datos - Sistema de Gestión Organizacional
-- Motor: SQLite
-- =====================================================================

PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS pais (
    id      INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre  TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS departamento (
    id           INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre       TEXT NOT NULL UNIQUE,
    presupuesto  REAL NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS cargo (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre      TEXT NOT NULL UNIQUE,
    salarioBase REAL NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS empleado (
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre           TEXT NOT NULL,
    apellido         TEXT NOT NULL,
    salario          REAL NOT NULL DEFAULT 0,
    idPais           INTEGER NOT NULL,
    idDepartamento   INTEGER NOT NULL,
    idCargo          INTEGER NOT NULL,
    FOREIGN KEY (idPais) REFERENCES pais(id) ON DELETE RESTRICT,
    FOREIGN KEY (idDepartamento) REFERENCES departamento(id) ON DELETE RESTRICT,
    FOREIGN KEY (idCargo) REFERENCES cargo(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS proyecto (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre      TEXT NOT NULL UNIQUE,
    presupuesto REAL NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS asignacion (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    idEmpleado      INTEGER NOT NULL,
    idProyecto      INTEGER NOT NULL,
    horasAsignadas  REAL NOT NULL DEFAULT 0,
    FOREIGN KEY (idEmpleado) REFERENCES empleado(id) ON DELETE CASCADE,
    FOREIGN KEY (idProyecto) REFERENCES proyecto(id) ON DELETE CASCADE
);
