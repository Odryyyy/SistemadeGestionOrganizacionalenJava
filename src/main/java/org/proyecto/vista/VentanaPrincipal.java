package org.proyecto.vista;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Ventana principal de la aplicación. Implementa una interfaz MDI
 * (Multiple Document Interface) mediante JDesktopPane, donde cada módulo
 * (Países, Departamentos, Cargos, Empleados, Proyectos, Asignaciones)
 * se abre como una JInternalFrame independiente.
 */
public class VentanaPrincipal extends JFrame {

    private final JDesktopPane escritorio;
    private final Map<String, JInternalFrame> ventanasAbiertas = new HashMap<>();

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión Organizacional  —  POO + SQLite + Swing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Configurado en pantalla completa para que coincida perfectamente con el Login
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null);
        setIconImage(crearIconoAplicacion());

        escritorio = new JDesktopPane();
        escritorio.setBackground(EstiloUI.COLOR_FONDO);
        escritorio.setDesktopManager(new DefaultDesktopManager());

        setJMenuBar(crearMenu());
        add(crearBarraSuperior(), BorderLayout.NORTH);
        add(new JScrollPane(escritorio), BorderLayout.CENTER);
        add(crearBarraEstado(), BorderLayout.SOUTH);

        // Cargar el Dashboard imponente automáticamente pasando la referencia 'this'
        abrirDashboardInicial();
    }

    private void abrirDashboardInicial() {
        DashboardInterna dashboard = new DashboardInterna(this);
        ventanasAbiertas.put("dashboard", dashboard);
        escritorio.add(dashboard);
        dashboard.setVisible(true);
        try {
            dashboard.setMaximum(true);
        } catch (Exception ignored) {
        }
    }

    private Image crearIconoAplicacion() {
        BufferedImageIcono icono = new BufferedImageIcono(32, 32, EstiloUI.COLOR_PRIMARIO);
        return icono.getImage();
    }

    private JPanel crearBarraSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(EstiloUI.COLOR_PRIMARIO);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel titulo = new JLabel("Sistema de Gestión Organizacional");
        titulo.setFont(EstiloUI.FUENTE_TITULO);
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Java SE  •  Swing  •  SQLite  •  JDBC");
        subtitulo.setFont(EstiloUI.FUENTE_LABEL);
        subtitulo.setForeground(new Color(210, 220, 235));

        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);

        panel.add(textos, BorderLayout.WEST);
        return panel;
    }

    private JPanel crearBarraEstado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(233, 236, 239));
        panel.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        JLabel estado = new JLabel("Listo. Panel de control activo.");
        estado.setFont(EstiloUI.FUENTE_LABEL);
        panel.add(estado, BorderLayout.WEST);
        return panel;
    }

    private JMenuBar crearMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuModulos = new JMenu("Módulos");
        menuModulos.add(crearItemMenu("Dashboard / Inicio", () -> mostrarVentana("dashboard", new DashboardInterna(this))));
        menuModulos.addSeparator();
        menuModulos.add(crearItemMenu("Países", () -> mostrarVentana("pais", new PaisInterna())));
        menuModulos.add(crearItemMenu("Departamentos", () -> mostrarVentana("departamento", new DepartamentoInterna())));
        menuModulos.add(crearItemMenu("Cargos", () -> mostrarVentana("cargo", new CargoInterna())));
        menuModulos.add(crearItemMenu("Empleados", () -> mostrarVentana("empleado", new EmpleadoInterna())));
        menuModulos.add(crearItemMenu("Proyectos", () -> mostrarVentana("proyecto", new ProyectoInterna())));
        menuModulos.add(crearItemMenu("Asignaciones", () -> mostrarVentana("asignacion", new AsignacionInterna())));
        menuModulos.addSeparator();
        JMenuItem salir = new JMenuItem("Salir");
        salir.addActionListener(e -> System.exit(0));
        menuModulos.add(salir);

        JMenu menuVentana = new JMenu("Ventana");
        JMenuItem organizar = new JMenuItem("Organizar en cascada");
        organizar.addActionListener(e -> organizarCascada());
        menuVentana.add(organizar);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem acercaDe = new JMenuItem("Acerca de");
        acercaDe.addActionListener(e -> EstiloUI.mostrarInfo(this,
                "Sistema de Gestión Organizacional\nProyecto Final - POO\nJava SE + Swing + SQLite + JDBC"));
        menuAyuda.add(acercaDe);

        menuBar.add(menuModulos);
        menuBar.add(menuVentana);
        menuBar.add(menuAyuda);
        return menuBar;
    }

    private JMenuItem crearItemMenu(String texto, Runnable accion) {
        JMenuItem item = new JMenuItem(texto);
        item.addActionListener(e -> accion.run());
        return item;
    }

    public void mostrarVentana(String clave, JInternalFrame ventana) {
        JInternalFrame existente = ventanasAbiertas.get(clave);
        if (existente != null && !existente.isClosed()) {
            try {
                existente.setIcon(false);
                if (existente.isMaximizable()) {
                    existente.setMaximum(true);
                }
                existente.moveToFront();
                existente.setSelected(true);
            } catch (Exception ignored) {
            }
            return;
        }
        ventanasAbiertas.put(clave, ventana);
        escritorio.add(ventana);
        ventana.setLocation(20 + (ventanasAbiertas.size() * 15) % 200, 20 + (ventanasAbiertas.size() * 15) % 150);
        ventana.setVisible(true);
        try {
            ventana.setSelected(true);
        } catch (Exception ignored) {
        }
    }

    private void organizarCascada() {
        int offset = 0;
        for (JInternalFrame f : escritorio.getAllFrames()) {
            try {
                f.setMaximum(false);
            } catch (Exception ignored) {
            }
            f.setLocation(offset, offset);
            offset += 28;
        }
    }

    /** Pequeño ícono generado por código, sin depender de recursos externos. */
    private static class BufferedImageIcono {
        private final java.awt.image.BufferedImage imagen;

        BufferedImageIcono(int w, int h, Color color) {
            imagen = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = imagen.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(color);
            g.fillRoundRect(0, 0, w, h, 8, 8);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g.drawString("S", w / 2 - 5, h / 2 + 6);
            g.dispose();
        }

        Image getImage() {
            return imagen;
        }
    }
}