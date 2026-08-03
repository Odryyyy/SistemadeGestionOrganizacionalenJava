package org.proyecto.vista;

import javax.swing.JTable;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ExportadorCSV {

    public static void exportarTabla(JTable tabla, String nombreSugerido) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como CSV");
        fileChooser.setSelectedFile(new File(nombreSugerido + ".csv"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File archivoToSave = fileChooser.getSelectedFile();
            
            String ruta = archivoToSave.getAbsolutePath();
            if (!ruta.toLowerCase().endsWith(".csv")) {
                archivoToSave = new File(ruta + ".csv");
            }

            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivoToSave), StandardCharsets.UTF_8))) {
                // Escribir BOM de UTF-8 para que Excel reconozca las tildes automáticamente sin corromper caracteres
                bw.write("\uFEFF");
                
                javax.swing.table.TableModel modelo = tabla.getModel();
                
                // Escribir cabeceras separadas por punto y coma (;)
                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    bw.write(modelo.getColumnName(i) + (i == modelo.getColumnCount() - 1 ? "" : ";"));
                }
                bw.newLine();

                // Escribir filas de datos
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    for (int j = 0; j < modelo.getColumnCount(); j++) {
                        Object valor = tabla.getValueAt(i, j);
                        String texto = (valor != null ? valor.toString() : "");
                        texto = texto.replace("\n", " ").replace(";", " ");
                        bw.write(texto + (j == modelo.getColumnCount() - 1 ? "" : ";"));
                    }
                    bw.newLine();
                }
                
                JOptionPane.showMessageDialog(null, "¡Datos exportados con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error al exportar el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}