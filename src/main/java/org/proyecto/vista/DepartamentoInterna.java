package org.proyecto.vista;

import org.proyecto.controlador.DepartamentoControlador;
import org.proyecto.controlador.ValidacionException;
import org.proyecto.modelo.Departamento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.NumberFormat;

public class DepartamentoInterna extends JInternalFrame {

    private final DepartamentoControlador controlador = new DepartamentoControlador();

    private JTextField txtNombre;
    private JTextField txtPresupuesto;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private int idSeleccionado = 0;

    public DepartamentoInterna() {
        super("Gestión de Departamentos", true, true, true, true);
        setSize(720, 520);
        setLayout(new BorderLayout());

        add(EstiloUI.crearEncabezado("Departamentos"), BorderLayout.NORTH);
        add(crearPanelContenido(), BorderLayout.CENTER);

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

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(EstiloUI.crearLabel("Nombre:"), gbc);
        txtNombre = EstiloUI.crearCampoTexto();
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(EstiloUI.crearLabel("Presupuesto:"), gbc);
        txtPresupuesto = EstiloUI.crearCampoTexto();
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(txtPresupuesto, gbc);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botones.setOpaque(false);
        JButton btnGuardar = EstiloUI.crearBotonPrimario("Guardar");
        JButton btnEliminar = EstiloUI.crearBotonPeligro("Eliminar");
        JButton btnLimpiar = EstiloUI.crearBotonSecundario("Limpiar");
        botones.add(btnGuardar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(botones, gbc);

        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        return panel;
    }

    private JScrollPane crearTabla() {
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Presupuesto"}, 0) {
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

    private void cargarTabla() {
        try {
            modeloTabla.setRowCount(0);
            NumberFormat moneda = NumberFormat.getCurrencyInstance();
            for (Departamento d : controlador.listar()) {
                modeloTabla.addRow(new Object[]{d.getId(), d.getNombre(), moneda.format(d.getPresupuesto())});
            }
        } catch (SQLException ex) {
            EstiloUI.mostrarError(this, "Error al cargar los departamentos: " + ex.getMessage());
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
        txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
        try {
            Departamento d = controlador.listar().stream()
                    .filter(x -> x.getId() == idSeleccionado).findFirst().orElse(null);
            txtPresupuesto.setText(d != null ? String.valueOf(d.getPresupuesto()) : "");
        } catch (SQLException ignored) {
        }
    }

    private void guardar() {
        try {
            double presupuesto = parsearDouble(txtPresupuesto.getText(), "Presupuesto");
            Departamento d = new Departamento(idSeleccionado, txtNombre.getText().trim(), presupuesto);
            controlador.guardar(d);
            EstiloUI.mostrarInfo(this, "Departamento guardado correctamente.");
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
            EstiloUI.mostrarError(this, "Seleccione un departamento de la tabla para eliminar.");
            return;
        }
        if (!EstiloUI.confirmar(this, "¿Desea eliminar el departamento seleccionado?")) {
            return;
        }
        try {
            controlador.eliminar(idSeleccionado);
            limpiarFormulario();
            cargarTabla();
        } catch (SQLException ex) {
            EstiloUI.mostrarError(this, "No se pudo eliminar: probablemente tiene empleados asociados.");
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = 0;
        txtNombre.setText("");
        txtPresupuesto.setText("");
        tabla.clearSelection();
    }
}
