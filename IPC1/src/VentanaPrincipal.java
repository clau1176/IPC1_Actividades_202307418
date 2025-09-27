import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ArenaUSACIntermedio {
    private Personaje[] personajes = new Personaje[100];
    private Batalla[] batallas = new Batalla[100];
    private int cantidadPersonajes = 0;
    private int cantidadBatallas = 0;
    
    public static void main(String[] args) {
        new ArenaUSACIntermedio().iniciar();
    }
    
    public void iniciar() {
        // Crear ventana principal
        JFrame ventana = new JFrame("ArenaUSAC - Sistema de Batallas");
        ventana.setSize(800, 600);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);
        
        // Crear pestañas
        JTabbedPane pestañas = new JTabbedPane();
        
        // Agregar paneles
        pestañas.addTab("Inicio", crearPanelInicio());
        pestañas.addTab("Personajes", crearPanelPersonajes());
        pestañas.addTab("Batallas", crearPanelBatallas());
        pestañas.addTab("Historial", crearPanelHistorial());
        pestañas.addTab("Estudiante", crearPanelEstudiante());
        
        ventana.add(pestañas);
        ventana.setVisible(true);
        
        // Cargar datos de ejemplo
        cargarDatosEjemplo();
    }
    
    // CLASE PERSONAJE (interna)
    class Personaje {
        private static int nextId = 1;
        private int id;
        private String nombre;
        private String arma;
        private int vida;
        private int ataque;
        private int defensa;
        
        public Personaje(String nombre, String arma, int vida, int ataque, int defensa) {
            this.id = nextId++;
            this.nombre = nombre;
            this.arma = arma;
            this.vida = vida;
            this.ataque = ataque;
            this.defensa = defensa;
        }
        
        // Getters
        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public String getArma() { return arma; }
        public int getVida() { return vida; }
        public int getAtaque() { return ataque; }
        public int getDefensa() { return defensa; }
        
        public boolean estaVivo() { return vida > 0; }
        
        public void recibirDaño(int daño) {
            vida -= daño;
            if (vida < 0) vida = 0;
        }
        
        @Override
        public String toString() { return nombre; }
    }
    
    // CLASE BATALLA (interna)
    class Batalla {
        private static int nextId = 1;
        private int id;
        private Personaje jugador1;
        private Personaje jugador2;
        private Personaje ganador;
        private String historial;
        
        public Batalla(Personaje p1, Personaje p2) {
            this.id = nextId++;
            this.jugador1 = p1;
            this.jugador2 = p2;
            this.historial = "Batalla entre " + p1.getNombre() + " y " + p2.getNombre() + "\n";
        }
        
        public void agregarEvento(String evento) {
            historial += evento + "\n";
        }
        
        public void setGanador(Personaje ganador) {
            this.ganador = ganador;
            historial += "\n¡GANADOR: " + ganador.getNombre() + "!";
        }
        
        // Getters
        public int getId() { return id; }
        public Personaje getJugador1() { return jugador1; }
        public Personaje getJugador2() { return jugador2; }
        public Personaje getGanador() { return ganador; }
        public String getHistorial() { return historial; }
    }
    
    private void cargarDatosEjemplo() {
        // Agregar algunos personajes de ejemplo
        agregarPersonajeEjemplo("Guerrero", "Espada", 100, 25, 15);
        agregarPersonajeEjemplo("Mago", "Varita", 80, 30, 10);
        agregarPersonajeEjemplo("Arquero", "Arco", 90, 20, 12);
    }
    
    private void agregarPersonajeEjemplo(String nombre, String arma, int vida, int ataque, int defensa) {
        if (cantidadPersonajes < personajes.length) {
            personajes[cantidadPersonajes] = new Personaje(nombre, arma, vida, ataque, defensa);
            cantidadPersonajes++;
        }
    }
    
    // PANEL DE INICIO
    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel titulo = new JLabel("ARENA USAC", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setForeground(Color.BLUE);
        
        JLabel subtitulo = new JLabel("Sistema de Simulación de Batallas", JLabel.CENTER);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        
        panel.add(titulo, BorderLayout.CENTER);
        panel.add(subtitulo, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // PANEL DE PERSONAJES
    private JPanel crearPanelPersonajes() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel de botones
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar Personaje");
        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        
        // Tabla de personajes
        String[] columnas = {"ID", "Nombre", "Arma", "Vida", "Ataque", "Defensa"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modelo);
        actualizarTablaPersonajes(modelo);
        
        JScrollPane scroll = new JScrollPane(tabla);
        
        // Agregar componentes al panel
        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        
        // Eventos de botones
        btnAgregar.addActionListener(e -> agregarPersonaje(modelo));
        btnEliminar.addActionListener(e -> eliminarPersonaje(tabla, modelo));
        
        return panel;
    }
    
    // PANEL DE BATALLAS
    private JPanel crearPanelBatallas() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel de selección
        JPanel panelSeleccion = new JPanel(new GridLayout(2, 2));
        
        JComboBox<Personaje> combo1 = new JComboBox<>();
        JComboBox<Personaje> combo2 = new JComboBox<>();
        actualizarCombos(combo1, combo2);
        
        panelSeleccion.add(new JLabel("Jugador 1:"));
        panelSeleccion.add(combo1);
        panelSeleccion.add(new JLabel("Jugador 2:"));
        panelSeleccion.add(combo2);
        
        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnBatalla = new JButton("Iniciar Batalla");
        
        // Área de texto para la batalla
        JTextArea areaBatalla = new JTextArea(15, 50);
        areaBatalla.setEditable(false);
        
        panelBotones.add(btnBatalla);
        
        // Agregar componentes
        panel.add(panelSeleccion, BorderLayout.NORTH);
        panel.add(panelBotones, BorderLayout.CENTER);
        panel.add(new JScrollPane(areaBatalla), BorderLayout.SOUTH);
        
        // Evento de batalla
        btnBatalla.addActionListener(e -> iniciarBatalla(
            (Personaje)combo1.getSelectedItem(), 
            (Personaje)combo2.getSelectedItem(), 
            areaBatalla
        ));
        
        return panel;
    }
    
    // PANEL DE HISTORIAL
    private JPanel crearPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JTextArea areaHistorial = new JTextArea(15, 50);
        areaHistorial.setEditable(false);
        
        JButton btnActualizar = new JButton("Actualizar Historial");
        btnActualizar.addActionListener(e -> actualizarHistorial(areaHistorial));
        
        panel.add(btnActualizar, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaHistorial), BorderLayout.CENTER);
        
        return panel;
    }
    
    // PANEL DE ESTUDIANTE
    private JPanel crearPanelEstudiante() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JTextArea areaInfo = new JTextArea();
        areaInfo.setEditable(false);
        areaInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        areaInfo.setText(
            "DATOS DEL ESTUDIANTE\n\n" +
            "Nombre: [COMPLETAR CON TU NOMBRE]\n" +
            "Carnet: [COMPLETAR CON TU CARNET]\n" +
            "Curso: Introducción a la Programación\n" +
            "Sección: [COMPLETAR SECCIÓN]\n" +
            "Práctica: 2 - ArenaUSAC\n" +
            "Fecha: Septiembre 2025"
        );
        
        panel.add(new JScrollPane(areaInfo), BorderLayout.CENTER);
        return panel;
    }
    
    // MÉTODOS AUXILIARES
    
    private void actualizarTablaPersonajes(DefaultTableModel modelo) {
        modelo.setRowCount(0); // Limpiar tabla
        for (int i = 0; i < cantidadPersonajes; i++) {
            Personaje p = personajes[i];
            modelo.addRow(new Object[]{
                p.getId(), p.getNombre(), p.getArma(), 
                p.getVida(), p.getAtaque(), p.getDefensa()
            });
        }
    }
    
    private void actualizarCombos(JComboBox<Personaje> combo1, JComboBox<Personaje> combo2) {
        combo1.removeAllItems();
        combo2.removeAllItems();
        for (int i = 0; i < cantidadPersonajes; i++) {
            Personaje p = personajes[i];
            if (p.estaVivo()) {
                combo1.addItem(p);
                combo2.addItem(p);
            }
        }
    }
    
    private void agregarPersonaje(DefaultTableModel modelo) {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        
        JTextField txtNombre = new JTextField();
        JTextField txtArma = new JTextField();
        JTextField txtVida = new JTextField("100");
        JTextField txtAtaque = new JTextField("20");
        JTextField txtDefensa = new JTextField("10");
        
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Arma:"));
        panel.add(txtArma);
        panel.add(new JLabel("Vida:"));
        panel.add(txtVida);
        panel.add(new JLabel("Ataque:"));
        panel.add(txtAtaque);
        panel.add(new JLabel("Defensa:"));
        panel.add(txtDefensa);
        
        int resultado = JOptionPane.showConfirmDialog(null, panel, 
            "Agregar Personaje", JOptionPane.OK_CANCEL_OPTION);
        
        if (resultado == JOptionPane.OK_OPTION) {
            try {
                String nombre = txtNombre.getText().trim();
                String arma = txtArma.getText().trim();
                int vida = Integer.parseInt(txtVida.getText());
                int ataque = Integer.parseInt(txtAtaque.getText());
                int defensa = Integer.parseInt(txtDefensa.getText());
                
                if (!nombre.isEmpty() && !arma.isEmpty()) {
                    if (cantidadPersonajes < personajes.length) {
                        personajes[cantidadPersonajes] = new Personaje(nombre, arma, vida, ataque, defensa);
                        cantidadPersonajes++;
                        actualizarTablaPersonajes(modelo);
                        JOptionPane.showMessageDialog(null, "Personaje agregado exitosamente!");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pueden agregar más personajes");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error: Ingrese valores numéricos válidos");
            }
        }
    }
    
    private void eliminarPersonaje(JTable tabla, DefaultTableModel modelo) {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            int id = (int) modelo.getValueAt(fila, 0);
            
            // Buscar y eliminar el personaje
            for (int i = 0; i < cantidadPersonajes; i++) {
                if (personajes[i].getId() == id) {
                    // Mover todos los elementos una posición hacia atrás
                    for (int j = i; j < cantidadPersonajes - 1; j++) {
                        personajes[j] = personajes[j + 1];
                    }
                    personajes[cantidadPersonajes - 1] = null;
                    cantidadPersonajes--;
                    break;
                }
            }
            
            actualizarTablaPersonajes(modelo);
            JOptionPane.showMessageDialog(null, "Personaje eliminado");
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un personaje primero");
        }
    }
    
    private void iniciarBatalla(Personaje p1, Personaje p2, JTextArea area) {
        if (p1 == null || p2 == null || p1 == p2) {
            JOptionPane.showMessageDialog(null, "Seleccione dos personajes diferentes");
            return;
        }
        
        if (!p1.estaVivo() || !p2.estaVivo()) {
            JOptionPane.showMessageDialog(null, "Ambos personajes deben estar vivos");
            return;
        }
        
        // Crear nueva batalla
        Batalla batalla = new Batalla(p1, p2);
        area.setText(batalla.getHistorial());
        
        // Simular batalla por turnos
        new Thread(() -> {
            Personaje atacante = p1;
            Personaje defensor = p2;
            
            while (p1.estaVivo() && p2.estaVivo()) {
                try {
                    Thread.sleep(1500); // Pausa entre turnos
                    
                    int daño = atacante.getAtaque() - defensor.getDefensa();
                    if (daño < 5) daño = 5; // Daño mínimo
                    
                    defensor.recibirDaño(daño);
                    
                    String evento = atacante.getNombre() + " ataca a " + defensor.getNombre() + 
                                   " - " + daño + " de daño. Vida restante: " + defensor.getVida();
                    
                    batalla.agregarEvento(evento);
                    
                    // Actualizar interfaz
                    SwingUtilities.invokeLater(() -> {
                        area.setText(batalla.getHistorial());
                    });
                    
                    // Cambiar turno
                    Personaje temp = atacante;
                    atacante = defensor;
                    defensor = temp;
                    
                } catch (InterruptedException ex) {
                    break;
                }
            }
            
            // Determinar ganador
            Personaje ganador = p1.estaVivo() ? p1 : p2;
            batalla.setGanador(ganador);
            
            // Agregar batalla al historial
            if (cantidadBatallas < batallas.length) {
                batallas[cantidadBatallas] = batalla;
                cantidadBatallas++;
            }
            
            SwingUtilities.invokeLater(() -> {
                area.setText(batalla.getHistorial());
                JOptionPane.showMessageDialog(null, "¡Batalla terminada! Ganador: " + ganador.getNombre());
            });
            
        }).start();
    }
    
    private void actualizarHistorial(JTextArea area) {
        if (cantidadBatallas == 0) {
            area.setText("No hay batallas registradas aún.");
            return;
        }
        
        StringBuilder texto = new StringBuilder("HISTORIAL DE BATALLAS\n\n");
        for (int i = 0; i < cantidadBatallas; i++) {
            Batalla b = batallas[i];
            texto.append("Batalla #").append(b.getId()).append("\n");
            texto.append(b.getJugador1().getNombre()).append(" vs ").append(b.getJugador2().getNombre()).append("\n");
            texto.append("Ganador: ").append(b.getGanador() != null ? b.getGanador().getNombre() : "En progreso").append("\n");
            texto.append("--------------------\n");
        }
        
        area.setText(texto.toString());
    }
}
