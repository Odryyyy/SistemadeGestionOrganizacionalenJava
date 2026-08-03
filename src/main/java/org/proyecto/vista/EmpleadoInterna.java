package org.proyecto.vista;

import org.proyecto.controlador.DepartamentoControlador;
import org.proyecto.controlador.EmpleadoControlador;
import org.proyecto.controlador.PaisControlador;
import org.proyecto.controlador.ValidacionException;
import org.proyecto.controlador.CargoControlador;
import org.proyecto.modelo.Cargo;
import org.proyecto.modelo.Departamento;
import org.proyecto.modelo.Empleado;
import org.proyecto.modelo.Pais;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EmpleadoInterna extends JInternalFrame {

    private final EmpleadoControlador empleadoControlador = new EmpleadoControlador();
    private final DepartamentoControlador departamentoControlador = new DepartamentoControlador();
    private final CargoControlador cargoControlador = new CargoControlador();
    private final PaisControlador paisControlador = new PaisControlador();

    private JTextField txtNombre, txtApellido, txtSalario, txtBuscar;
    private JComboBox<Pais> cmbPais;
    private JComboBox<Departamento> cmbDepartamento;
    private JComboBox<Cargo> cmbCargo;
    private JTable tablaEmpleados;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;

    private Integer empleadoIdSeleccionado = null;

    public EmpleadoInterna() {
        super("Gestión de Empleados", true, true, true, true);
        setSize(950, 650);
        setLayout(new BorderLayout());
        setBackground(EstiloUI.COLOR_FONDO);

        add(EstiloUI.crearEncabezado("Empleados"), BorderLayout.NORTH);

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(EstiloUI.COLOR_FONDO);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelContenido.add(crearPanelFormulario());
        panelContenido.add(Box.createRigidArea(new Dimension(0, 15)));
        panelContenido.add(crearPanelBusquedaYExportar());
        panelContenido.add(Box.createRigidArea(new Dimension(0, 10)));
        panelContenido.add(crearPanelTabla());

        add(new JScrollPane(panelContenido), BorderLayout.CENTER);

        cargarCombos();
        cargarTabla();
    }

    private JPanel crearPanelFormulario() {
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(EstiloUI.COLOR_BLANCO);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloUI.COLOR_BORDE),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panelForm.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = EstiloUI.crearCampoTexto();
        txtApellido = EstiloUI.crearCampoTexto();
        txtSalario = EstiloUI.crearCampoTexto();

        cmbPais = new JComboBox<>();
        EstiloUI.estilizarCampo(cmbPais);
        cmbDepartamento = new JComboBox<>();
        EstiloUI.estilizarCampo(cmbDepartamento);
        cmbCargo = new JComboBox<>();
        EstiloUI.estilizarCampo(cmbCargo);

        // Fila 0
        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(EstiloUI.crearLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; panelForm.add(txtNombre, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.0; panelForm.add(EstiloUI.crearLabel("País:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0; panelForm.add(cmbPais, gbc);

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0; panelForm.add(EstiloUI.crearLabel("Apellido:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; panelForm.add(txtApellido, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.0; panelForm.add(EstiloUI.crearLabel("Departamento:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0; panelForm.add(cmbDepartamento, gbc);

        // Fila 2
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0; panelForm.add(EstiloUI.crearLabel("Salario:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; panelForm.add(txtSalario, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0.0; panelForm.add(EstiloUI.crearLabel("Cargo:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 1.0; panelForm.add(cmbCargo, gbc);

        // Fila 3: Botones de Acción
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBotones.setOpaque(false);

        JButton btnGuardar = EstiloUI.crearBotonPrimario("Guardar");
        btnGuardar.addActionListener(e -> guardarEmpleado());

        JButton btnEliminar = EstiloUI.crearBotonPeligro("Eliminar");
        btnEliminar.addActionListener(e -> eliminarEmpleado());

        JButton btnLimpiar = EstiloUI.crearBotonSecundario("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        JButton btnRefrescar = EstiloUI.crearBotonSecundario("Refrescar listas");
        btnRefrescar.addActionListener(e -> cargarCombos());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnRefrescar);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        panelForm.add(panelBotones, gbc);

        return panelForm;
    }

    private JPanel crearPanelBusquedaYExportar() {
        JPanel panelBusqueda = new JPanel(new BorderLayout(15, 0));
        panelBusqueda.setOpaque(false);
        panelBusqueda.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JPanel panelIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelIzq.setOpaque(false);
        panelIzq.add(EstiloUI.crearLabel("Buscar en tabla:"));
        
        txtBuscar = EstiloUI.crearCampoTexto();
        txtBuscar.setPreferredSize(new Dimension(250, 30));
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarTabla(txtBuscar.getText());
            }
        });
        panelIzq.add(txtBuscar);

        JButton btnExportar = EstiloUI.crearBotonPrimario("📊 Exportar a Excel (CSV)");
        btnExportar.setBackground(EstiloUI.COLOR_ACCENTO); // Color verde referencia a Excel
        btnExportar.addActionListener(e -> exportarACSV());

        JButton btnExportarPDF = EstiloUI.crearBotonPrimario("🧾 Exportar a PDF");
        btnExportarPDF.setBackground(EstiloUI.COLOR_PELIGRO); // Color rojo, referencia a PDF
        btnExportarPDF.addActionListener(e -> exportarAPDF());

        JPanel panelBotonesExportar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotonesExportar.setOpaque(false);
        panelBotonesExportar.add(btnExportar);
        panelBotonesExportar.add(btnExportarPDF);

        panelBusqueda.add(panelIzq, BorderLayout.WEST);
        panelBusqueda.add(panelBotonesExportar, BorderLayout.EAST);

        return panelBusqueda;
    }

    private JPanel crearPanelTabla() {
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setOpaque(false);

        String[] columnas = {"ID", "Nombre", "Apellido", "Salario", "País", "Departamento", "Cargo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEmpleados = new JTable(modeloTabla);
        EstiloUI.estilizarTabla(tablaEmpleados);
        
        sorter = new TableRowSorter<>(modeloTabla);
        tablaEmpleados.setRowSorter(sorter);

        tablaEmpleados.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaEmpleados.getSelectedRow() != -1) {
                int filaSeleccionada = tablaEmpleados.getSelectedRow();
                int filaReal = tablaEmpleados.convertRowIndexToModel(filaSeleccionada);
                cargarDatosEnFormulario(filaReal);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaEmpleados);
        panelTabla.add(scrollPane, BorderLayout.CENTER);

        return panelTabla;
    }

    private void cargarCombos() {
        try {
            cmbPais.removeAllItems();
            List<Pais> paises = paisControlador.listar();
            for (Pais p : paises) {
                cmbPais.addItem(p);
            }

            cmbDepartamento.removeAllItems();
            List<Departamento> departamentos = departamentoControlador.listar();
            for (Departamento d : departamentos) {
                cmbDepartamento.addItem(d);
            }
            
            cmbCargo.removeAllItems();
            List<Cargo> cargos = cargoControlador.listar();
            for (Cargo c : cargos) {
                cmbCargo.addItem(c);
            }
        } catch (SQLException e) {
            EstiloUI.mostrarError(this, "Error al cargar listas desplegables: " + e.getMessage());
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            List<Empleado> empleados = empleadoControlador.listar();
            for (Empleado emp : empleados) {
                Object[] fila = {
                    emp.getId(),
                    emp.getNombre(),
                    emp.getApellido(),
                    String.format("L%,.2f", emp.getSalario()),
                    emp.getPais() != null ? emp.getPais().getNombre() : "N/A",
                    emp.getDepartamento() != null ? emp.getDepartamento().getNombre() : "N/A",
                    emp.getCargo() != null ? emp.getCargo().getNombre() : "N/A"
                };
                modeloTabla.addRow(fila);
            }
        } catch (SQLException e) {
            EstiloUI.mostrarError(this, "Error al cargar empleados: " + e.getMessage());
        }
    }

    private void filtrarTabla(String consulta) {
        if (consulta.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + consulta));
        }
    }

    private void cargarDatosEnFormulario(int fila) {
        empleadoIdSeleccionado = (Integer) modeloTabla.getValueAt(fila, 0);
        txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
        txtApellido.setText((String) modeloTabla.getValueAt(fila, 2));
        
        String salarioStr = ((String) modeloTabla.getValueAt(fila, 3))
                .replace("L", "").replace(",", "").trim();
        txtSalario.setText(salarioStr);
        
        String paisNombre = (String) modeloTabla.getValueAt(fila, 4);
        for (int i = 0; i < cmbPais.getItemCount(); i++) {
            if (cmbPais.getItemAt(i).getNombre().equals(paisNombre)) {
                cmbPais.setSelectedIndex(i);
                break;
            }
        }

        String depNombre = (String) modeloTabla.getValueAt(fila, 5);
        for (int i = 0; i < cmbDepartamento.getItemCount(); i++) {
            if (cmbDepartamento.getItemAt(i).getNombre().equals(depNombre)) {
                cmbDepartamento.setSelectedIndex(i);
                break;
            }
        }

        String cargoNombre = (String) modeloTabla.getValueAt(fila, 6);
        for (int i = 0; i < cmbCargo.getItemCount(); i++) {
            if (cmbCargo.getItemAt(i).getNombre().equals(cargoNombre)) {
                cmbCargo.setSelectedIndex(i);
                break;
            }
        }
    }

    private void guardarEmpleado() {
        try {
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String salarioStr = txtSalario.getText().trim();

            if (nombre.isEmpty() || apellido.isEmpty() || salarioStr.isEmpty()) {
                EstiloUI.mostrarError(this, "Por favor complete todos los campos obligatorios.");
                return;
            }

            double salario = Double.parseDouble(salarioStr);
            Pais pais = (Pais) cmbPais.getSelectedItem();
            Departamento departamento = (Departamento) cmbDepartamento.getSelectedItem();
            Cargo cargo = (Cargo) cmbCargo.getSelectedItem();

            Empleado emp = new Empleado();
            emp.setNombre(nombre);
            emp.setApellido(apellido);
            emp.setSalario(salario);
            emp.setPais(pais);
            emp.setDepartamento(departamento);
            emp.setCargo(cargo);

            if (empleadoIdSeleccionado != null) {
                emp.setId(empleadoIdSeleccionado);
            }

            empleadoControlador.guardar(emp);

            if (empleadoIdSeleccionado == null) {
                EstiloUI.mostrarInfo(this, "Empleado guardado exitosamente.");
            } else {
                EstiloUI.mostrarInfo(this, "Empleado actualizado exitosamente.");
            }

            limpiarFormulario();
            cargarTabla();

        } catch (NumberFormatException e) {
            EstiloUI.mostrarError(this, "El salario debe ser un número válido.");
        } catch (ValidacionException e) {
            EstiloUI.mostrarError(this, e.getMessage());
        } catch (SQLException e) {
            EstiloUI.mostrarError(this, "Error en la base de datos: " + e.getMessage());
        }
    }

    private void eliminarEmpleado() {
        if (empleadoIdSeleccionado == null) {
            EstiloUI.mostrarError(this, "Seleccione un empleado de la tabla para eliminar.");
            return;
        }

        if (EstiloUI.confirmar(this, "¿Está seguro de eliminar este empleado?")) {
            try {
                empleadoControlador.eliminar(empleadoIdSeleccionado);
                EstiloUI.mostrarInfo(this, "Empleado eliminado correctamente.");
                limpiarFormulario();
                cargarTabla();
            } catch (SQLException e) {
                EstiloUI.mostrarError(this, "Error al eliminar empleado: " + e.getMessage());
            }
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtSalario.setText("");
        if (cmbPais.getItemCount() > 0) cmbPais.setSelectedIndex(0);
        if (cmbDepartamento.getItemCount() > 0) cmbDepartamento.setSelectedIndex(0);
        if (cmbCargo.getItemCount() > 0) cmbCargo.setSelectedIndex(0);
        empleadoIdSeleccionado = null;
        tablaEmpleados.clearSelection();
    }

    private void exportarACSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como CSV");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".csv")) {
                fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".csv");
            }

            try (FileWriter writer = new FileWriter(fileToSave)) {
                for (int i = 0; i < modeloTabla.getColumnCount(); i++) {
                    writer.write(modeloTabla.getColumnName(i) + (i == modeloTabla.getColumnCount() - 1 ? "" : ","));
                }
                writer.write("\n");

                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    for (int j = 0; j < modeloTabla.getColumnCount(); j++) {
                        Object value = modeloTabla.getValueAt(i, j);
                        writer.write((value != null ? value.toString() : "") + (j == modeloTabla.getColumnCount() - 1 ? "" : ","));
                    }
                    writer.write("\n");
                }

                EstiloUI.mostrarInfo(this, "Reporte exportado exitosamente a Excel (CSV).");
            } catch (IOException ex) {
                EstiloUI.mostrarError(this, "Error al exportar archivo: " + ex.getMessage());
            }
        }
    }

    private void exportarAPDF() {
        ExportadorPDF.exportarTabla(tablaEmpleados, "Empleados", "Reporte de Empleados");
    }
}