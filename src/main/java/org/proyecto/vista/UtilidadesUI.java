package org.proyecto.vista;

import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

public class UtilidadesUI {

    // Método para agregar filtro en tiempo real a cualquier JTable usando un JTextField
    public static void configurarBuscador(JTextField txtBuscar, JTable tabla) {
        TableRowSorter<javax.swing.table.TableModel> sorter = new TableRowSorter<>(tabla.getModel());
        tabla.setRowSorter(sorter);

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filtrar(); }

            private void filtrar() {
                String texto = txtBuscar.getText().trim();
                if (texto.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    // (?i) hace que la búsqueda no distinga entre mayúsculas y minúsculas
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
                }
            }
        });
    }
}