package org.proyecto.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardInterna extends JInternalFrame {

    private final VentanaPrincipal ventanaPrincipal;
    private JLabel lblReloj;

    public DashboardInterna(VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        setTitle("Panel de Control (Dashboard)");
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        setSize(1000, 700);

        // Panel Principal con scroll y diseño limpio
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(EstiloUI.COLOR_FONDO);
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Cabecera con Título y Reloj en tiempo real
        panelPrincipal.add(crearPanelCabecera(), BorderLayout.NORTH);

        // 2. Centro: Contenedor con distribución fija para que no se distorsione
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setOpaque(false);

        // Fila 1: Tarjetas de Estadísticas (Kpis) con altura controlada
        panelCentro.add(crearPanelTarjetas());
        panelCentro.add(Box.createRigidArea(new Dimension(0, 15)));

        // Fila 2: Panel inferior que divide Accesos Rápidos y Gráfico Estadístico
        panelCentro.add(crearPanelInferiorDashboard());

        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
        
        // Iniciar el reloj digital
        iniciarReloj();
    }

    private JPanel crearPanelCabecera() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel lblTitulo = new JLabel("Bienvenido al Sistema de Gestión - Panel General");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        // CORREGIDO: Cambiado a blanco puro para que contraste perfectamente con el fondo oscuro
        lblTitulo.setForeground(Color.WHITE); 
        panel.add(lblTitulo, BorderLayout.WEST);

        // Etiqueta del reloj y fecha actual (Cambiado a un color azul claro/brillante de alta visibilidad)
        lblReloj = new JLabel();
        lblReloj.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblReloj.setForeground(new Color(144, 202, 249)); // Azul claro suave legible sobre fondo oscuro
        panel.add(lblReloj, BorderLayout.EAST);

        return panel;
    }

    private void iniciarReloj() {
        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat formato = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy — hh:mm:ss a");
            lblReloj.setText(formato.format(new Date()));
        });
        timer.start();
    }

    private JPanel crearPanelTarjetas() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        panel.setPreferredSize(new Dimension(0, 110));

        panel.add(crearTarjetaKpi("Empleados Registrados", "3", new Color(41, 128, 185)));
        panel.add(crearTarjetaKpi("Departamentos", "3", new Color(39, 174, 96)));
        panel.add(crearTarjetaKpi("Países", "3", new Color(211, 84, 0)));
        panel.add(crearTarjetaKpi("Salario Promedio", "L14,000.00", new Color(142, 68, 173)));

        return panel;
    }

    private JPanel crearTarjetaKpi(String titulo, String valor, Color colorBorde) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 6, 0, 0, colorBorde),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel lblTit = new JLabel(titulo.toUpperCase());
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTit.setForeground(new Color(120, 120, 120));

        JLabel lblVal = new JLabel(valor);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblVal.setForeground(new Color(50, 50, 50));

        tarjeta.add(lblTit, BorderLayout.NORTH);
        tarjeta.add(lblVal, BorderLayout.CENTER);

        return tarjeta;
    }

    private JPanel crearPanelInferiorDashboard() {
        JPanel panelInferior = new JPanel(new GridLayout(1, 2, 15, 0));
        panelInferior.setOpaque(false);
        panelInferior.setPreferredSize(new Dimension(0, 320));

        // 1. Panel de Accesos Rápidos (Izquierda)
        JPanel panelAccesos = new JPanel();
        panelAccesos.setLayout(null);
        panelAccesos.setBackground(Color.WHITE);
        panelAccesos.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblAcc = new JLabel("Accesos Rápidos");
        lblAcc.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblAcc.setForeground(new Color(50, 50, 50));
        lblAcc.setBounds(20, 20, 300, 25);
        panelAccesos.add(lblAcc);

        JButton btnEmpleados = new JButton("📁 Gestionar Empleados");
        btnEmpleados.setBounds(20, 70, 320, 42);
        btnEmpleados.setBackground(new Color(41, 128, 185));
        btnEmpleados.setForeground(Color.WHITE);
        btnEmpleados.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEmpleados.setFocusPainted(false);
        btnEmpleados.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEmpleados.addActionListener(e -> ventanaPrincipal.mostrarVentana("empleado", new EmpleadoInterna()));
        panelAccesos.add(btnEmpleados);

        JButton btnActualizar = new JButton("🔄 Países y Departamentos");
        btnActualizar.setBounds(20, 130, 320, 42);
        btnActualizar.setBackground(new Color(52, 73, 94));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnActualizar.setFocusPainted(false);
        btnActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnActualizar.addActionListener(e -> ventanaPrincipal.mostrarVentana("pais", new PaisInterna()));
        panelAccesos.add(btnActualizar);

        // 2. Gráfico Estadístico (Derecha)
        JPanel panelGraficoContenedor = new JPanel(new BorderLayout());
        panelGraficoContenedor.setBackground(Color.WHITE);
        panelGraficoContenedor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblSub = new JLabel("Distribución de Registros");
        lblSub.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSub.setForeground(new Color(50, 50, 50));
        panelGraficoContenedor.add(lblSub, BorderLayout.NORTH);

        JPanel panelGrafico = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                String[] categorias = {"Emp.", "Dep.", "País", "Carg", "Proy"};
                int[] valores = {3, 3, 3, 4, 2}; 
                Color[] colores = {
                    new Color(41, 128, 185), 
                    new Color(39, 174, 96), 
                    new Color(211, 84, 0), 
                    new Color(142, 68, 173),
                    new Color(230, 126, 34)
                };

                int maxValor = 5; 
                int numBarras = categorias.length;
                int anchoBarra = 35;
                int espacio = (w - (anchoBarra * numBarras)) / (numBarras + 1);

                for (int i = 0; i < numBarras; i++) {
                    int x = espacio + i * (anchoBarra + espacio);
                    int alturaBarra = (int) ((double) valores[i] / maxValor * (h - 75));
                    int y = h - alturaBarra - 35;

                    g2.setColor(colores[i]);
                    g2.fillRoundRect(x, y, anchoBarra, alturaBarra, 6, 6);

                    g2.setColor(new Color(80, 80, 80));
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                    g2.drawString(String.valueOf(valores[i]), x + (anchoBarra / 2) - 5, y - 6);

                    g2.drawString(categorias[i], x + 2, h - 15);
                }
            }
        };
        panelGrafico.setOpaque(false);
        panelGraficoContenedor.add(panelGrafico, BorderLayout.CENTER);

        panelInferior.add(panelAccesos);
        panelInferior.add(panelGraficoContenedor);

        return panelInferior;
    }
}