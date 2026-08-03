package org.proyecto.vista;

import org.proyecto.controlador.PaisControlador;
import org.proyecto.controlador.ValidacionException;
import org.proyecto.modelo.Pais;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class PaisInterna extends JInternalFrame {

    private final PaisControlador controlador = new PaisControlador();

    private JTextField txtNombre;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private int idSeleccionado = 0;

    public PaisInterna() {
        super("Gestión de Países", true, true, true, true);
        setSize(680, 500);
        setLayout(new BorderLayout());

        add(EstiloUI.crearEncabezado("Países"), BorderLayout.NORTH);
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
        panel.add(EstiloUI.crearLabel("Nombre del país:"), gbc);

        txtNombre = EstiloUI.crearCampoTexto();
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(txtNombre, gbc);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        botones.setOpaque(false);
        JButton btnAgregar = EstiloUI.crearBotonPrimario("Guardar");
        JButton btnEliminar = EstiloUI.crearBotonPeligro("Eliminar");
        JButton btnLimpiar = EstiloUI.crearBotonSecundario("Limpiar");
        botones.add(btnAgregar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weightx = 0;
        panel.add(botones, gbc);

        btnAgregar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        return panel;
    }

    private JScrollPane crearTabla() {
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre"}, 0) {
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
            for (Pais p : controlador.listar()) {
                modeloTabla.addRow(new Object[]{p.getId(), p.getNombre()});
            }
        } catch (SQLException ex) {
            EstiloUI.mostrarError(this, "Error al cargar los países: " + ex.getMessage());
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        idSeleccionado = (int) modeloTabla.getValueAt(fila, 0);
        txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
    }

    private void guardar() {
        try {
            Pais pais = new Pais(idSeleccionado, txtNombre.getText().trim());
            controlador.guardar(pais);
            EstiloUI.mostrarInfo(this, "País guardado correctamente.");
            limpiarFormulario();
            cargarTabla();
        } catch (ValidacionException ve) {
            EstiloUI.mostrarError(this, ve.getMessage());
        } catch (SQLException ex) {
            EstiloUI.mostrarError(this, "Error de base de datos: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (idSeleccionado == 0) {
            EstiloUI.mostrarError(this, "Seleccione un país de la tabla para eliminar.");
            return;
        }
        if (!EstiloUI.confirmar(this, "¿Desea eliminar el país seleccionado?")) {
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
        tabla.clearSelection();
    }
}
