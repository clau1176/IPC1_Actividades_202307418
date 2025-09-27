package gui;

import models.Personaje;
import models.Batalla;
import managers.GestorPersonajes;
import managers.GestorBatallas;
import managers.FileManager;
import threads.HiloBatalla;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame {
    private GestorPersonajes gestorPersonajes;
    private GestorBatallas gestorBatallas;
    private JTextArea textAreaBitacora;
    private JComboBox<Personaje> comboPersonaje1, comboPersonaje2;
    private HiloBatalla hilo1, hilo2;
    private Batalla batallaActual;
    private StringBuilder bitacoraBatalla;
    
    public VentanaPrincipal() {
        gestorPersonajes = new GestorPersonajes();
        gestorBatallas = new GestorBatallas();
        inicializarComponentes();
        cargarDatosEjemplo();
    }
    
    private void cargarDatosEjemplo() {
        gestorPersonajes.agregarPersonaje(new Personaje("Guerrero", "Espada de Fuego", 300, 50, 5, 3, 20));
        gestorPersonajes.agregarPersonaje(new Personaje("Mago", "Báculo Arcano", 200, 80, 3, 2, 10));
        gestorPersonajes.agregarPersonaje(new Personaje("Arquero", "Arco Élfico", 250, 60, 7, 6, 15));
        actualizarCombos();
    }
    
    private void inicializarComponentes() {
        setTitle("ArenaUSAC - Sistema de Batallas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Inicio", crearPanelInicio());
        tabbedPane.addTab("Gestión de Personajes", crearPanelPersonajes());
        tabbedPane.addTab("Simulación de Batallas", crearPanelBatallas());
        tabbedPane.addTab("Historial de Batallas", crearPanelHistorial());
        tabbedPane.addTab("Búsqueda", crearPanelBusqueda());
        tabbedPane.addTab("Datos del Estudiante", crearPanelEstudiante());
        
        add(tabbedPane);
    }
    
    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("ARENA USAC", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setForeground(Color.RED);
        JLabel subtitulo = new JLabel("Sistema de Simulación de Batallas", JLabel.CENTER);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(titulo, BorderLayout.CENTER);
        panel.add(subtitulo, BorderLayout.SOUTH);
        return panel;
    }
    
    private JPanel crearPanelPersonajes() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar Personaje");
        JButton btnModificar = new JButton("Modificar Personaje");
        JButton btnEliminar = new JButton("Eliminar Personaje");
        JButton btnGuardar = new JButton("Guardar Estado");
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnGuardar);
        
        String[] columnas = {"ID", "Nombre", "Arma", "HP", "Ataque", "Defensa", "Agilidad", "Velocidad"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modeloTabla);
        actualizarTablaPersonajes(modeloTabla);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        btnAgregar.addActionListener(e -> mostrarDialogoAgregarPersonaje(modeloTabla));
        btnModificar.addActionListener(e -> mostrarDialogoModificarPersonaje(tabla, modeloTabla));
        btnEliminar.addActionListener(e -> eliminarPersonaje(tabla, modeloTabla));
        btnGuardar.addActionListener(e -> FileManager.guardarEstado(gestorPersonajes, gestorBatallas));
        
        return panel;
    }
    
    private JPanel crearPanelBatallas() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel panelSeleccion = new JPanel(new GridLayout(2, 2));
        panelSeleccion.add(new JLabel("Personaje 1:"));
        comboPersonaje1 = new JComboBox<>();
        panelSeleccion.add(comboPersonaje1);
        panelSeleccion.add(new JLabel("Personaje 2:"));
        comboPersonaje2 = new JComboBox<>();
        panelSeleccion.add(comboPersonaje2);
        
        JPanel panelBotones = new JPanel();
        JButton btnIniciar = new JButton("Iniciar Batalla");
        JButton btnDetener = new JButton("Detener Batalla");
        panelBotones.add(btnIniciar);
        panelBotones.add(btnDetener);
        
        textAreaBitacora = new JTextArea(20, 50);
        textAreaBitacora.setEditable(false);
        JScrollPane scrollBitacora = new JScrollPane(textAreaBitacora);
        
        panel.add(panelSeleccion, BorderLayout.NORTH);
        panel.add(panelBotones, BorderLayout.CENTER);
        panel.add(scrollBitacora, BorderLayout.SOUTH);
        
        actualizarCombos();
        
        btnIniciar.addActionListener(e -> iniciarBatalla());
        btnDetener.addActionListener(e -> detenerBatalla());
        
        return panel;
    }
    
    private JPanel crearPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnas = {"ID", "Fecha", "Personaje 1", "Personaje 2", "Ganador"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modelo);
        actualizarTablaHistorial(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.add(new JLabel("Buscar personaje:"));
        JTextField txtBusqueda = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(btnBuscar);
        
        JTextArea areaResultado = new JTextArea(10, 50);
        areaResultado.setEditable(false);
        JScrollPane scrollResultado = new JScrollPane(areaResultado);
        
        panel.add(panelBusqueda, BorderLayout.NORTH);
        panel.add(scrollResultado, BorderLayout.CENTER);
        
        btnBuscar.addActionListener(e -> {
            String nombre = txtBusqueda.getText().trim();
            if (!nombre.isEmpty()) {
                Personaje personaje = gestorPersonajes.buscarPorNombre(nombre);
                if (personaje != null) {
                    areaResultado.setText("Información del Personaje:\n");
                    areaResultado.append("ID: " + personaje.getId() + "\n");
                    areaResultado.append("Nombre: " + personaje.getNombre() + "\n");
                    areaResultado.append("Arma: " + personaje.getArma() + "\n");
                    areaResultado.append("HP: " + personaje.getPuntosVida() + "\n");
                    areaResultado.append("Ataque: " + personaje.getNivelAtaque() + "\n");
                    areaResultado.append("Defensa: " + personaje.getDefensa() + "\n");
                    areaResultado.append("Agilidad: " + personaje.getAgilidad() + "\n");
                    areaResultado.append("Velocidad: " + personaje.getVelocidad() + "\n");
                    areaResultado.append("Batallas ganadas: " + personaje.getBatallasGanadas() + "\n");
                    areaResultado.append("Batallas perdidas: " + personaje.getBatallasPerdidas() + "\n");
                } else {
                    areaResultado.setText("Personaje no encontrado.");
                }
            }
        });
        
        return panel;
    }
    
    private JPanel crearPanelEstudiante() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea areaInfo = new JTextArea();
        areaInfo.setEditable(false);
        areaInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        areaInfo.setText("DATOS DEL ESTUDIANTE\n\n" +
                        "Curso: Introducción a la Programación y Computación 1\n" +
                        "Práctica: 2 - ArenaUSAC\n" +
                        "Semestre: 2S2025\n" +
                        "Desarrollado por: [Tu nombre aquí]\n" +
                        "Carnet: [Tu carnet aquí]\n" +
                        "Sección: [Tu sección aquí]\n\n" +
                        "Fecha de entrega: 26/Septiembre/2025");
        panel.add(new JScrollPane(areaInfo), BorderLayout.CENTER);
        return panel;
    }
    
    private void actualizarTablaPersonajes(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        Personaje[] personajes = gestorPersonajes.getTodosPersonajes();
        for (Personaje p : personajes) {
            modelo.addRow(new Object[]{
                p.getId(), p.getNombre(), p.getArma(), p.getPuntosVida(),
                p.getNivelAtaque(), p.getDefensa(), p.getAgilidad(), p.getVelocidad()
            });
        }
    }
    
    private void actualizarTablaHistorial(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        Batalla[] batallas = gestorBatallas.getBatallas();
        for (Batalla b : batallas) {
            modelo.addRow(new Object[]{
                b.getId(), b.getFecha(), 
                b.getPersonaje1().getNombre(), 
                b.getPersonaje2().getNombre(),
                b.getGanador() != null ? b.getGanador().getNombre() : "En progreso"
            });
        }
    }
    
    private void actualizarCombos() {
        comboPersonaje1.removeAllItems();
        comboPersonaje2.removeAllItems();
        Personaje[] personajes = gestorPersonajes.getPersonajesVivos();
        for (Personaje p : personajes) {
            comboPersonaje1.addItem(p);
            comboPersonaje2.addItem(p);
        }
    }
    
    private void mostrarDialogoAgregarPersonaje(DefaultTableModel modeloTabla) {
        JDialog dialogo = new JDialog(this, "Agregar Personaje", true);
        dialogo.setLayout(new GridLayout(8, 2));
        
        JTextField txtNombre = new JTextField();
        JTextField txtArma = new JTextField();
        JTextField txtHP = new JTextField();
        JTextField txtAtaque = new JTextField();
        JTextField txtVelocidad = new JTextField();
        JTextField txtAgilidad = new JTextField();
        JTextField txtDefensa = new JTextField();
        
        dialogo.add(new JLabel("Nombre:"));
        dialogo.add(txtNombre);
        dialogo.add(new JLabel("Arma:"));
        dialogo.add(txtArma);
        dialogo.add(new JLabel("Puntos de Vida (100-500):"));
        dialogo.add(txtHP);
        dialogo.add(new JLabel("Nivel de Ataque (10-100):"));
        dialogo.add(txtAtaque);
        dialogo.add(new JLabel("Velocidad (1-10):"));
        dialogo.add(txtVelocidad);
        dialogo.add(new JLabel("Agilidad (1-10):"));
        dialogo.add(txtAgilidad);
        dialogo.add(new JLabel("Defensa (1-50):"));
        dialogo.add(txtDefensa);
        
        JButton btnAgregar = new JButton("Agregar");
        JButton btnCancelar = new JButton("Cancelar");
        dialogo.add(btnAgregar);
        dialogo.add(btnCancelar);
        
        btnAgregar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText().trim();
                String arma = txtArma.getText().trim();
                int hp = Integer.parseInt(txtHP.getText());
                int ataque = Integer.parseInt(txtAtaque.getText());
                int velocidad = Integer.parseInt(txtVelocidad.getText());
                int agilidad = Integer.parseInt(txtAgilidad.getText());
                int defensa = Integer.parseInt(txtDefensa.getText());
                
                if (nombre.isEmpty() || arma.isEmpty()) {
                    JOptionPane.showMessageDialog(dialogo, "Nombre y arma no pueden estar vacíos");
                    return;
                }
                
                if (hp < 100 || hp > 500 || ataque < 10 || ataque > 100 ||
                    velocidad < 1 || velocidad > 10 || agilidad < 1 || agilidad > 10 ||
                    defensa < 1 || defensa > 50) {
                    JOptionPane.showMessageDialog(dialogo, "Valores fuera de rango permitido");
                    return;
                }
                
                if (gestorPersonajes.existePersonaje(nombre)) {
                    JOptionPane.showMessageDialog(dialogo, "Ya existe un personaje con ese nombre");
                    return;
                }
                
                Personaje nuevo = new Personaje(nombre, arma, hp, ataque, velocidad, agilidad, defensa);
                if (gestorPersonajes.agregarPersonaje(nuevo)) {
                    JOptionPane.showMessageDialog(dialogo, "Personaje agregado exitosamente");
                    actualizarTablaPersonajes(modeloTabla);
                    actualizarCombos();
                    dialogo.dispose();
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "Ingrese valores numéricos válidos");
            }
        });
        
        btnCancelar.addActionListener(e -> dialogo.dispose());
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }
    
    private void mostrarDialogoModificarPersonaje(JTable tabla, DefaultTableModel modeloTabla) {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un personaje para modificar");
            return;
        }
        
        int id = (int) tabla.getValueAt(filaSeleccionada, 0);
        Personaje personaje = gestorPersonajes.buscarPorId(id);
        
        JDialog dialogo = new JDialog(this, "Modificar Personaje", true);
        dialogo.setLayout(new GridLayout(7, 2));
        
        JTextField txtArma = new JTextField(personaje.getArma());
        JTextField txtHP = new JTextField(String.valueOf(personaje.getPuntosVida()));
        JTextField txtAtaque = new JTextField(String.valueOf(personaje.getNivelAtaque()));
        JTextField txtVelocidad = new JTextField(String.valueOf(personaje.getVelocidad()));
        JTextField txtAgilidad = new JTextField(String.valueOf(personaje.getAgilidad()));
        JTextField txtDefensa = new JTextField(String.valueOf(personaje.getDefensa()));
        
        