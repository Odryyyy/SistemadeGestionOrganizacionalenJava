package org.proyecto.vista;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utilidad para exportar el contenido de un JTable a un archivo PDF,
 * usando la librería iText 5 (ya incluida como dependencia del proyecto).
 */
public class ExportadorPDF {

    // Evita instanciar la clase
    private ExportadorPDF() {
    }

    /**
     * Exporta el contenido visible de una tabla a un archivo PDF.
     *
     * @param tabla          la JTable a exportar
     * @param nombreSugerido nombre de archivo sugerido (sin extensión)
     * @param titulo         título que se mostrará en el encabezado del PDF
     */
    public static void exportarTabla(JTable tabla, String nombreSugerido, String titulo) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como PDF");
        fileChooser.setSelectedFile(new File(nombreSugerido + ".pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF (*.pdf)", "pdf"));

        int seleccion = fileChooser.showSaveDialog(null);
        if (seleccion != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File archivo = fileChooser.getSelectedFile();
        String ruta = archivo.getAbsolutePath();
        if (!ruta.toLowerCase().endsWith(".pdf")) {
            archivo = new File(ruta + ".pdf");
        }

        Document documento = new Document(PageSize.A4.rotate(), 30, 30, 50, 40);

        try (FileOutputStream salida = new FileOutputStream(archivo)) {
            PdfWriter.getInstance(documento, salida);
            documento.open();

            // --- Título ---
            Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Paragraph parrafoTitulo = new Paragraph(titulo, fuenteTitulo);
            parrafoTitulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(parrafoTitulo);

            // --- Fecha de generación ---
            Font fuenteFecha = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            String fechaTexto = "Generado el " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
            Paragraph parrafoFecha = new Paragraph(fechaTexto, fuenteFecha);
            parrafoFecha.setAlignment(Element.ALIGN_CENTER);
            parrafoFecha.setSpacingAfter(15);
            documento.add(parrafoFecha);

            // --- Tabla ---
            TableModel modelo = tabla.getModel();
            int columnas = modelo.getColumnCount();

            PdfPTable pdfTabla = new PdfPTable(columnas);
            pdfTabla.setWidthPercentage(100);
            pdfTabla.setHeaderRows(1);

            Font fuenteEncabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            for (int i = 0; i < columnas; i++) {
                PdfPCell celda = new PdfPCell(new Paragraph(modelo.getColumnName(i), fuenteEncabezado));
                celda.setBackgroundColor(new BaseColor(59, 130, 246)); // Azul, referencia a COLOR_PRIMARIO
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPadding(6);
                pdfTabla.addCell(celda);
            }

            Font fuenteCelda = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
            BaseColor colorFilaPar = new BaseColor(240, 240, 240);
            for (int fila = 0; fila < modelo.getRowCount(); fila++) {
                BaseColor colorFondo = (fila % 2 == 0) ? colorFilaPar : BaseColor.WHITE;
                for (int col = 0; col < columnas; col++) {
                    Object valor = tabla.getValueAt(fila, col);
                    String texto = (valor != null) ? valor.toString() : "";
                    PdfPCell celda = new PdfPCell(new Paragraph(texto, fuenteCelda));
                    celda.setBackgroundColor(colorFondo);
                    celda.setPadding(5);
                    pdfTabla.addCell(celda);
                }
            }

            documento.add(pdfTabla);
            documento.close();

            int opcion = JOptionPane.showConfirmDialog(null,
                    "PDF exportado con éxito. ¿Desea abrirlo ahora?",
                    "Éxito", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

            if (opcion == JOptionPane.YES_OPTION) {
                abrirArchivo(archivo);
            }
        } catch (IOException | DocumentException ex) {
            JOptionPane.showMessageDialog(null, "Error al exportar el PDF: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void abrirArchivo(File archivo) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(archivo);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo abrir el archivo automáticamente: " + ex.getMessage(),
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}
