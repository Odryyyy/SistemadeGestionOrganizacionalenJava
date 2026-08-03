package org.proyecto.vista;

import org.proyecto.dao.UsuarioDao;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FrmLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JLabel lblUsuario;
    private JLabel lblPassword;
    private Image logoImagen;

    public FrmLogin() {
        setTitle("Sistema de Gestión Organizacional - Acceso");
        // Pantalla completa moderna
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Cargar el logo
        cargarLogo();

        // Panel principal con fondo degradado oscuro (Negro y Gris moderno)
        JPanel panelFondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Fondo degradado de gris oscuro a negro absoluto
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(35, 35, 35), 0, h, new Color(10, 10, 10));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);

                // Dibujar el logo centrado (Grande y visible)
                if (logoImagen != null) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.45f)); 
                    int imgW = 700; 
                    int imgH = 700;
                    int x = (w - imgW) / 2;
                    int y = (h - imgH) / 2;
                    g2d.drawImage(logoImagen, x, y, imgW, imgH, this);
                }
            }
        };
        panelFondo.setLayout(new GridBagLayout()); 
        setContentPane(panelFondo);

        // Tarjeta / Contenedor central de Login
        JPanel panelLoginCard = new JPanel();
        panelLoginCard.setPreferredSize(new Dimension(420, 380));
        panelLoginCard.setBackground(new Color(25, 25, 25, 240)); 
        panelLoginCard.setLayout(null);
        panelLoginCard.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1, true));

        // Título del Login
        JLabel lblTitulo = new JLabel("INICIO DE SESIÓN", SwingConstants.CENTER);
        lblTitulo.setBounds(40, 30, 340, 30);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panelLoginCard.add(lblTitulo);

        // Subtítulo
        JLabel lblSub = new JLabel("Sistema de Gestión Organizacional", SwingConstants.CENTER);
        lblSub.setBounds(40, 60, 340, 20);
        lblSub.setForeground(new Color(190, 190, 190));
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelLoginCard.add(lblSub);

        // Campo Usuario
        lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(50, 110, 100, 25);
        lblUsuario.setForeground(new Color(240, 240, 240));
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panelLoginCard.add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(50, 135, 320, 35);
        txtUsuario.setBackground(new Color(40, 40, 40));
        txtUsuario.setForeground(Color.WHITE);
        txtUsuario.setCaretColor(Color.WHITE);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(110, 110, 110)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panelLoginCard.add(txtUsuario);

        // Campo Contraseña
        lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(50, 185, 100, 25);
        lblPassword.setForeground(new Color(240, 240, 240));
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panelLoginCard.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(50, 210, 320, 35);
        txtPassword.setBackground(new Color(40, 40, 40));
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setCaretColor(Color.WHITE);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(110, 110, 110)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panelLoginCard.add(txtPassword);

        // Botón Ingresar Moderno
        btnIngresar = new JButton("INGRESAR");
        btnIngresar.setBounds(50, 280, 320, 40);
        btnIngresar.setBackground(new Color(60, 130, 200)); 
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.setBorder(BorderFactory.createEmptyBorder());
        panelLoginCard.add(btnIngresar);

        // --- ACCIONES PARA DETECTAR LA TECLA ENTER ---
        ActionListener oyenteAccion = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarLogin();
            }
        };

        // Asignamos la acción al botón, al campo de usuario y al campo de contraseña
        btnIngresar.addActionListener(oyenteAccion);
        txtUsuario.addActionListener(oyenteAccion);
        txtPassword.addActionListener(oyenteAccion);
        // ---------------------------------------------

        // Agregar tarjeta al fondo centrado
        panelFondo.add(panelLoginCard);
    }

    private void cargarLogo() {
        try {
            java.net.URL imgUrl = getClass().getResource("/logo.png");
            if (imgUrl != null) {
                logoImagen = new ImageIcon(imgUrl).getImage();
            } else {
                File f = new File("src/main/resources/logo.png");
                if (f.exists()) {
                    logoImagen = new ImageIcon(f.getAbsolutePath()).getImage();
                }
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el logo de fondo: " + e.getMessage());
        }
    }

    private void validarLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, llene todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UsuarioDao dao = new UsuarioDao();
        boolean accesoPermitido = dao.validarAcceso(usuario, password);

        if (accesoPermitido) {
            JOptionPane.showMessageDialog(this, "¡Bienvenido, " + usuario + "!", "Acceso Concedido", JOptionPane.INFORMATION_MESSAGE);
            
            // Abre la ventana principal de tu sistema
            new VentanaPrincipal().setVisible(true);
            
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtUsuario.requestFocus();
        }
    }
}