import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// MODELO
class Usuario {
    private String nombre;
    public Usuario(String nombre) { this.nombre = nombre; }
    public String getNombre() { return nombre; }
}

class CalculadoraModelo {
    private Usuario usuario;
    public CalculadoraModelo(Usuario usuario) { this.usuario = usuario; }
    public double sumar(double a, double b) { return a + b; }
    public double restar(double a, double b) { return a - b; }
    public double multiplicar(double a, double b) { return a * b; }
    public double dividir(double a, double b) { 
        if (b == 0) throw new ArithmeticException("No dividir entre cero");
        return a / b; 
    }
    public Usuario getUsuario() { return usuario; }
}

// VISTA
class LoginVista extends JFrame {
    private JTextField txtNombre;
    private JButton btnIngresar;
    public LoginVista() {
        setTitle("Login Calculadora");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Ingrese su nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);
        
        btnIngresar = new JButton("Ingresar");
        panel.add(btnIngresar);
        
        add(panel);
    }
    public String getNombre() { return txtNombre.getText().trim(); }
    public void setActionListener(ActionListener listener) { btnIngresar.addActionListener(listener); }
    public void mostrarError(String msg) { JOptionPane.showMessageDialog(this, msg); }
}

class CalculadoraVista extends JFrame {
    private JTextField txtA, txtB;
    private JTextArea txtResultado;
    private JButton btnSumar, btnRestar, btnMultiplicar, btnDividir;
    private JLabel lblUsuario;
    
    public CalculadoraVista() {
        setTitle("Calculadora MVC");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel usuario
        JPanel panelUsuario = new JPanel(new BorderLayout());
        lblUsuario = new JLabel("Usuario: ");
        panelUsuario.add(lblUsuario, BorderLayout.WEST);
        panel.add(panelUsuario, BorderLayout.NORTH);
        
        // Panel números
        JPanel panelNumeros = new JPanel(new GridLayout(2, 2, 5, 5));
        panelNumeros.add(new JLabel("Número A:"));
        txtA = new JTextField();
        panelNumeros.add(txtA);
        panelNumeros.add(new JLabel("Número B:"));
        txtB = new JTextField();
        panelNumeros.add(txtB);
        panel.add(panelNumeros, BorderLayout.CENTER);
        
        // Panel operaciones
        JPanel panelOps = new JPanel(new GridLayout(1, 4, 5, 5));
        btnSumar = new JButton("+");
        btnRestar = new JButton("-");
        btnMultiplicar = new JButton("*");
        btnDividir = new JButton("/");
        panelOps.add(btnSumar);
        panelOps.add(btnRestar);
        panelOps.add(btnMultiplicar);
        panelOps.add(btnDividir);
        panel.add(panelOps, BorderLayout.NORTH);
        
        // Panel resultados
        txtResultado = new JTextArea(10, 30);
        txtResultado.setEditable(false);
        panel.add(new JScrollPane(txtResultado), BorderLayout.SOUTH);
        
        add(panel);
    }
    
    public String getNumeroA() { return txtA.getText(); }
    public String getNumeroB() { return txtB.getText(); }
    public void setUsuario(String nombre) { lblUsuario.setText("Usuario: " + nombre); }
    public void mostrarResultado(String resultado) { txtResultado.append(resultado + "\n"); }
    public void limpiarCampos() { txtA.setText(""); txtB.setText(""); }
    public void mostrarError(String msg) { JOptionPane.showMessageDialog(this, msg); }
    
    public void setSumarListener(ActionListener l) { btnSumar.addActionListener(l); }
    public void setRestarListener(ActionListener l) { btnRestar.addActionListener(l); }
    public void setMultiplicarListener(ActionListener l) { btnMultiplicar.addActionListener(l); }
    public void setDividirListener(ActionListener l) { btnDividir.addActionListener(l); }
}

// CONTROLADOR
class CalculadoraControlador {
    private LoginVista loginVista;
    private CalculadoraVista calcVista;
    private CalculadoraModelo modelo;
    
    public CalculadoraControlador(LoginVista login, CalculadoraVista calc) {
        this.loginVista = login;
        this.calcVista = calc;
        
        loginVista.setActionListener(e -> login());
        calcVista.setSumarListener(e -> operacion("sumar"));
        calcVista.setRestarListener(e -> operacion("restar"));
        calcVista.setMultiplicarListener(e -> operacion("multiplicar"));
        calcVista.setDividirListener(e -> operacion("dividir"));
    }
    
    private void login() {
        String nombre = loginVista.getNombre();
        if (nombre.isEmpty()) {
            loginVista.mostrarError("Ingrese un nombre");
            return;
        }
        
        Usuario usuario = new Usuario(nombre);
        modelo = new CalculadoraModelo(usuario);
        
        calcVista.setUsuario(usuario.getNombre());
        calcVista.mostrarResultado("Bienvenido: " + usuario.getNombre());
        
        loginVista.setVisible(false);
        calcVista.setVisible(true);
    }
    
    private void operacion(String tipo) {
        try {
            double a = Double.parseDouble(calcVista.getNumeroA());
            double b = Double.parseDouble(calcVista.getNumeroB());
            double resultado = 0;
            String simbolo = "";
            
            switch (tipo) {
                case "sumar":
                    resultado = modelo.sumar(a, b);
                    simbolo = "+";
                    break;
                case "restar":
                    resultado = modelo.restar(a, b);
                    simbolo = "-";
                    break;
                case "multiplicar":
                    resultado = modelo.multiplicar(a, b);
                    simbolo = "*";
                    break;
                case "dividir":
                    resultado = modelo.dividir(a, b);
                    simbolo = "/";
                    break;
            }
            
            calcVista.mostrarResultado(String.format("%.2f %s %.2f = %.2f", a, simbolo, b, resultado));
            calcVista.limpiarCampos();
            
        } catch (NumberFormatException e) {
            calcVista.mostrarError("Ingrese números válidos");
        } catch (ArithmeticException e) {
            calcVista.mostrarError(e.getMessage());
        }
    }
}

// MAIN
public class CalculadoraMVC {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginVista login = new LoginVista();
            CalculadoraVista calc = new CalculadoraVista();
            new CalculadoraControlador(login, calc);
            login.setVisible(true);
        });
    }
}