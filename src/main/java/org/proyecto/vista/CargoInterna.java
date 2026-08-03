package org.proyecto.vista;

import org.proyecto.controlador.CargoControlador;
import org.proyecto.controlador.ValidacionException;
import org.proyecto.modelo.Cargo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.NumberFormat;

public class CargoInterna extends JInternalFrame {

    private final CargoControlador controlador = new CargoControlador();

    private JTextField txtNombre;
    private JTextField txtSalarioBase;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private int idSeleccionado = 0;

    public CargoInterna() {
        super("Gestión de Cargos", true, true, true, true);
        setSize(720, 520);
        setLayout(new BorderLayout());

        add(EstiloUI.crearEncabezado("Cargos"), BorderLayout.NORTH);
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
        panel.add(EstiloUI.crearLabel("Salario base:"), gbc);
        txtSalarioBase = EstiloUI.crearCampoTexto();
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(txtSalarioBase, gbc);

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
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Salario Base"}, 0) {
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
            for (Cargo c : controlador.listar()) {
                modeloTabla.addRow(new Object[]{c.getId(), c.getNombre(), moneda.format(c.getSalarioBase())});
            }
        } catch (SQLException ex) {
            EstiloUI.mostrarError(this, "Error al cargar los cargos: " + ex.getMessage());
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
        txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
        try {
            Cargo c = controlador.listar().stream()
                    .filter(x -> x.getId() == idSeleccionado).findFirst().orElse(null);
            txtSalarioBase.setText(c != null ? String.valueOf(c.getSalarioBase()) : "");
        } catch (SQLException ignored) {
        }
    }

    private void guardar() {
        try {
            double salario = parsearDouble(txtSalarioBase.getText(), "Salario base");
            Cargo c = new Cargo(idSeleccionado, txtNombre.getText().trim(), salario);
            controlador.guardar(c);
            EstiloUI.mostrarInfo(this, "Cargo guardado correctamente.");
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
            EstiloUI.mostrarError(this, "Seleccione un cargo de la tabla para eliminar.");
            return;
        }
        if (!EstiloUI.confirmar(this, "¿Desea eliminar el cargo seleccionado?")) {
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
        txtSalarioBase.setText("");
        tabla.clearSelection();
    }
}
