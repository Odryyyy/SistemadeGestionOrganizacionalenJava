package org.proyecto.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Clase utilitaria para centralizar los estilos visuales de la aplicación
 * con un tema Moderno / Dark Mode Suavizado (Tonos más claros y profesionales).
 */
public class EstiloUI {

    // --- PALETA DE COLORES MODO OSCURO SUAVIZADO (MÁS CLARO) ---
    public static final Color COLOR_FONDO = Color.decode("#334155");        // Fondo general (Gris azulado más claro y suave)
    public static final Color COLOR_BLANCO = Color.decode("#475569");        // Tarjetas y contenedores (Gris intermedio iluminado)
    public static final Color COLOR_PRIMARIO = Color.decode("#3B82F6");      // Azul brillante para botones principales
    public static final Color COLOR_PRIMARIO_HOVER = Color.decode("#60A5FA");// Azul hover más claro
    public static final Color COLOR_SECUNDARIO = Color.decode("#64748B");  // Gris secundario para botones/cabeceras
    public static final Color COLOR_BORDE = Color.decode("#94A3B8");          // Bordes más claros y visibles
    public static final Color COLOR_TEXTO = Color.decode("#FFFFFF");          // Texto blanco puro para máxima claridad
    public static final Color COLOR_TEXTO_SECUNDARIO = Color.decode("#F1F5F9"); // Texto secundario muy claro
    public static final Color COLOR_ACCENTO = Color.decode("#10B981");       // Verde esmeralda
    public static final Color COLOR_PELIGRO = Color.decode("#EF4444");       // Rojo peligro

    // --- TIPOGRAFÍAS ---
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FUENTE_LABEL = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FUENTE_GENERAL = new Font("Segoe UI", Font.PLAIN, 13);

    public static void aplicarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }

    public static JPanel crearEncabezado(String texto) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BLANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDE),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel lbl = new JLabel(texto);
        lbl.setFont(FUENTE_TITULO);
        lbl.setForeground(COLOR_TEXTO);
        panel.add(lbl, BorderLayout.WEST);

        return panel;
    }

    // --- Encabezado con el logotipo ubicado a la DERECHA y tamaño ampliado (85x85) ---
    public static JPanel crearEncabezadoConLogo(String texto, String rutaLogo) {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(COLOR_BLANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDE),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel lbl = new JLabel(texto);
        lbl.setFont(FUENTE_TITULO);
        lbl.setForeground(COLOR_TEXTO);
        panel.add(lbl, BorderLayout.CENTER);

        if (rutaLogo != null && !rutaLogo.isEmpty()) {
            // Logotipo a la derecha con tamaño de 85x85 píxeles para que destaque con claridad
            JLabel lblLogo = crearLogo(rutaLogo, 85, 85);
            panel.add(lblLogo, BorderLayout.EAST);
        }

        return panel;
    }

    // --- Método auxiliar para cargar y redimensionar la imagen del logo ---
    public static JLabel crearLogo(String rutaImagen, int ancho, int alto) {
        JLabel lblLogo = new JLabel();
        try {
            java.net.URL imageUrl = EstiloUI.class.getResource("/" + rutaImagen);
            if (imageUrl == null) {
                imageUrl = EstiloUI.class.getResource(rutaImagen);
            }
            
            ImageIcon iconoOriginal = new ImageIcon(imageUrl);
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(imagenEscalada));
        } catch (Exception e) {
            lblLogo.setText("[Logo]");
            lblLogo.setForeground(COLOR_TEXTO);
        }
        return lblLogo;
    }

    public static JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FUENTE_LABEL);
        // CORREGIDO: Se cambia COLOR_TEXTO_SECUNDARIO por COLOR_TEXTO para garantizar total legibilidad en fondos oscuros
        label.setForeground(COLOR_TEXTO);
        return label;
    }

    public static JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(FUENTE_GENERAL);
        campo.setBackground(COLOR_FONDO);    
        campo.setForeground(COLOR_TEXTO);    
        campo.setCaretColor(Color.WHITE);    
        campo.setOpaque(true);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return campo;
    }

    public static <T> void estilizarCampo(JComboBox<T> combo) {
        combo.setFont(FUENTE_GENERAL);
        combo.setBackground(COLOR_FONDO);
        combo.setForeground(COLOR_TEXTO);
        combo.setOpaque(true);
    }

    public static JButton crearBotonPrimario(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_LABEL);
        boton.setForeground(Color.WHITE);
        boton.setBackground(COLOR_PRIMARIO);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_PRIMARIO_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_PRIMARIO);
            }
        });

        return boton;
    }

    public static JButton crearBotonSecundario(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_LABEL);
        boton.setForeground(COLOR_TEXTO);
        boton.setBackground(COLOR_SECUNDARIO);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE),
                BorderFactory.createEmptyBorder(9, 15, 9, 15)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(Color.decode("#475569"));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_SECUNDARIO);
            }
        });

        return boton;
    }

    public static JButton crearBotonPeligro(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_LABEL);
        boton.setForeground(Color.WHITE);
        boton.setBackground(COLOR_PELIGRO);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(9, 15, 9, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    public static void estilizarTabla(JTable tabla) {
        tabla.setFont(FUENTE_GENERAL);
        tabla.setBackground(COLOR_BLANCO);
        tabla.setForeground(COLOR_TEXTO);
        tabla.setGridColor(COLOR_BORDE);
        tabla.setRowHeight(28);
        tabla.setSelectionBackground(COLOR_PRIMARIO);
        tabla.setSelectionForeground(Color.WHITE);
        
        tabla.getTableHeader().setBackground(COLOR_SECUNDARIO);
        tabla.getTableHeader().setForeground(COLOR_TEXTO);
        tabla.getTableHeader().setFont(FUENTE_LABEL);
        tabla.getTableHeader().setOpaque(true);

        DefaultTableCellRenderer renderizador = new DefaultTableCellRenderer();
        renderizador.setBackground(COLOR_BLANCO);
        renderizador.setForeground(COLOR_TEXTO);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(renderizador);
        }
    }

    public static void mostrarError(Component parent, String mensaje) {
        JOptionPane.showMessageDialog(parent, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void mostrarInfo(Component parent, String mensaje) {
        JOptionPane.showMessageDialog(parent, mensaje, "Información del Sistema", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirmar(Component parent, String mensaje) {
        int opcion = JOptionPane.showConfirmDialog(parent, mensaje, "Confirmar acción", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return opcion == JOptionPane.YES_OPTION;
    }
}