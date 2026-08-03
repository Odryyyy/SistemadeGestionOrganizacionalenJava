package org.proyecto.vista;

import org.proyecto.controlador.AsignacionControlador;
import org.proyecto.controlador.EmpleadoControlador;
import org.proyecto.controlador.ProyectoControlador;
import org.proyecto.controlador.ValidacionException;
import org.proyecto.modelo.Asignacion;
import org.proyecto.modelo.Empleado;
import org.proyecto.modelo.Proyecto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class AsignacionInterna extends JInternalFrame {

    private final AsignacionControlador controlador = new AsignacionControlador();
    private final EmpleadoControlador empleadoControlador = new EmpleadoControlador();
    private final ProyectoControlador proyectoControlador = new ProyectoControlador();

    private JComboBox<Empleado> comboEmpleado;
    private JComboBox<Proyecto> comboProyecto;
    private JTextField txtHoras;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private int idSeleccionado = 0;

    public AsignacionInterna() {
        super("Asignación de Empleados a Proyectos", true, true, true, true);
        setSize(850, 560);
        setLayout(new BorderLayout());

        add(EstiloUI.crearEncabezado("Asignaciones"), BorderLayout.NORTH);
        add(crearPanelContenido(), BorderLayout.CENTER);

        cargarCombos();
        cargarTabla();
    }

    private JPanel crearPanelContenido() {
        JPanel contenedor = new JPanel(new BorderLayout(10, 10));
        contenedor.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contenedor.setBackground(EstiloUI.COLOR_FONDO);
        contenedor.add(crearFormulario(), BorderLayout.NORTH);
        contenedor.add(crearTabla(), BorderLayout.CENTER);
        return contenedor;
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(EstiloUI.COLOR_BLANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        comboEmpleado = new JComboBox<>();
        comboProyecto = new JComboBox<>();
        txtHoras = EstiloUI.crearCampoTexto();
        EstiloUI.estilizarCampo(comboEmpleado);
        EstiloUI.estilizarCampo(comboProyecto);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(EstiloUI.crearLabel("Empleado:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(comboEmpleado, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(EstiloUI.crearLabel("Proyecto:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(comboProyecto, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(EstiloUI.crearLabel("Horas asignadas:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(txtHoras, gbc);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botones.setOpaque(false);
        JButton btnGuardar = EstiloUI.crearBotonPrimario("Guardar");
        JButton btnEliminar = EstiloUI.crearBotonPeligro("Eliminar");
        JButton btnLimpiar = EstiloUI.crearBotonSecundario("Limpiar");
        JButton btnRefrescar = EstiloUI.crearBotonSecundario("Refrescar listas");
        botones.add(btnGuardar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);
        botones.add(btnRefrescar);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(botones, gbc);

        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnRefrescar.addActionListener(e -> cargarCombos());

        return panel;
    }

    private JScrollPane crearTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Empleado", "Proyecto", "Horas"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        EstiloUI.estilizarTabla(tabla);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                cargarSeleccion();
            }
        });
        return new JScrollPane(tabla);
    }

    private void cargarCombos() {
        try {
            comboEmpleado.removeAllItems();
            for (Empleado emp : empleadoControlador.listar()) {
                comboEmpleado.addItem(emp);
            }
            comboProyecto.removeAllItems();
            for (Proyecto p : proyectoControlador.listar()) {
                comboProyecto.addItem(p);
            }
        } catch (SQLException ex) {
            EstiloUI.mostrarError(this, "Error al cargar las listas: " + ex.getMessage());
        }
    }

    private void cargarTabla() {
        try {
            modeloTabla.setRowCount(0);
            for (Asignacion a : controlador.listar()) {
                modeloTabla.addRow(new Object[]{
                        a.getId(),
                        a.getEmpleado().getNombre() + " " + a.getEmpleado().getApellido(),
                        a.getProyecto().getNombre(),
                        a.getHorasAsignadas()
                });
            }
        } catch (SQLException ex) {
            EstiloUI.mostrarError(this, "Error al cargar las asignaciones: " + ex.getMessage());
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
        try {
            Asignacion a = controlador.listar().stream()
                    .filter(x -> x.getId() == idSeleccionado).findFirst().orElse(null);
            if (a == null) {
                return;
            }
            seleccionarEmpleado(a.getEmpleado().getId());
            seleccionarProyecto(a.getProyecto().getId());
            txtHoras.setText(String.valueOf(a.getHorasAsignadas()));
        } catch (SQLException ex) {
            EstiloUI.mostrarError(this, "Error al cargar el registro: " + ex.getMessage());
        }
    }

    private void seleccionarEmpleado(int id) {
        for (int i = 0; i < comboEmpleado.getItemCount(); i++) {
            if (comboEmpleado.getItemAt(i).getId() == id) {
                comboEmpleado.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarProyecto(int id) {
        for (int i = 0; i < comboProyecto.getItemCount(); i++) {
            if (comboProyecto.getItemAt(i).getId() == id) {
                comboProyecto.setSelectedIndex(i);
                return;
            }
        }
    }

    private void guardar() {
        try {
            if (comboEmpleado.getSelectedItem() == null || comboProyecto.getSelectedItem() == null) {
                throw new ValidacionException("Debe registrar al menos un Empleado y un Proyecto antes de asignar.");
            }
            double horas = parsearDouble(txtHoras.getText(), "Horas asignadas");
            Asignacion a = new Asignacion(
                    idSeleccionado,
                    (Empleado) comboEmpleado.getSelectedItem(),
                    (Proyecto) comboProyecto.getSelectedItem(),
                    horas
            );
            controlador.guardar(a);
            EstiloUI.mostrarInfo(this, "Asignación guardada correctamente.");
            limpiarFormulario();
            cargarTabla();
        } catch (ValidacionException ve) {
            EstiloUI.mostrarError(this, ve.getMessage());
        } catch (SQLException ex) {
            EstiloUI.mostrarError(this, "Error de base de datos: " + ex.getMessage());
        }
    }

    private double parsearDouble(String texto, String campo) throws ValidacionException {
        try {
            return Double.parseDouble(texto.trim().replace(",", "."));
        } catch (NumberFormatException ex) {
            throw new ValidacionException("El campo '" + campo + "' debe ser un número válido.");
        }
    }

    private void eliminar() {
        if (idSeleccionado == 0) {
            EstiloUI.mostrarError(this, "Seleccione una asignación de la tabla para eliminar.");
            return;
        }
        if (!EstiloUI.confirmar(this, "¿Desea eliminar la asignación seleccionada?")) {
            return;
        }
        try {
            controlador.eliminar(idSeleccionado);
            limpiarFormulario();
            cargarTabla();
        } catch (SQLException ex) {
            EstiloUI.mostrarError(this, "No se pudo eliminar la asignación: " + ex.getMessage());
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = 0;
        txtHoras.setText("");
        if (comboEmpleado.getItemCount() > 0) comboEmpleado.setSelectedIndex(0);
        if (comboProyecto.getItemCount() > 0) comboProyecto.setSelectedIndex(0);
        tabla.clearSelection();
    }
}
