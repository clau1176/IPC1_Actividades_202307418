import java.util.Scanner;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

//clase prinsipal 
public class SistemaInventarioTiendaRopa {
    // Estructuras de datos para almacenamiento
    private static Producto[] inventario = new Producto[100];
    private static int contadorProductos = 0;
    private static Venta[] ventas = new Venta[100];
    private static int contadorVentas = 0;
    private static Bitacora[] bitacora = new Bitacora[100];
    private static int contadorBitacora = 0;
    private static Scanner scanner = new Scanner(System.in);
     public static void main(String[] args) {
        // Crear directorios necesarios
        new File("data").mkdir();
        new File("reportes").mkdir();
        
        cargarDatos();
        mostrarMenuPrincipal();
    }
    
    // Menú principal del sistema
    private static void mostrarMenuPrincipal() {
        int opcion;
        do {
            System.out.println("\n=== SISTEMA DE GESTIÓN DE INVENTARIO ===");
            System.out.println("1. Agregar Producto");
            System.out.println("2. Buscar Producto");
            System.out.println("3. Eliminar Producto");
            System.out.println("4. Registrar Venta");
            System.out.println("5. Generar Reportes");
            System.out.println("6. Ver Datos del Estudiante");
            System.out.println("7. Bitácora");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcion = 0;
            }
            
            switch(opcion) {
                case 1:
                    agregarProducto();
                    break;
                case 2:
                    buscarProducto();
                    break;
                case 3:
                    eliminarProducto();
                    break;
                case 4:
                    registrarVenta();
                    break;
                case 5:
                    generarReportes();
                    break;
                case 6:
                    verDatosEstudiante();
                    break;
                case 7:
                    mostrarBitacora();
                    break;
                case 8:
                    guardarDatos();
                    System.out.println("¡Gracias por usar el sistema!");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while(opcion != 8);
    }
    

}

// Función para agregar un nuevo producto
    private static void agregarProducto() {
        System.out.println("\n--- AGREGAR PRODUCTO ---");
        
        System.out.print("Código único: ");
        String codigo = scanner.nextLine();
        
        // Validar que el código sea único
        for(int i = 0; i < contadorProductos; i++) {
            if(inventario[i].getCodigo().equals(codigo)) {
                System.out.println("Error: Ya existe un producto con este código.");
                registrarBitacora("Agregar Producto", "Error: Código duplicado - " + codigo);
                return;
            }
        }
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Categoría: ");
        String categoria = scanner.nextLine();
        
        double precio = 0;
        while (true) {
            try {
                System.out.print("Precio: ");
                precio = Double.parseDouble(scanner.nextLine());
                if (precio <= 0) {
                    System.out.println("Error: El precio debe ser positivo.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un valor numérico válido.");
            }
        }
        
        int cantidad = 0;
        while (true) {
            try {
                System.out.print("Cantidad en stock: ");
                cantidad = Integer.parseInt(scanner.nextLine());
                if (cantidad < 0) {
                    System.out.println("Error: La cantidad debe ser positiva.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un valor numérico válido.");
            }
        }
        
        // Crear y agregar producto
        inventario[contadorProductos] = new Producto(codigo, nombre, categoria, precio, cantidad);
        contadorProductos++;
        
        System.out.println("Producto agregado exitosamente.");
        registrarBitacora("Agregar Producto", "Éxito: Producto " + codigo + " agregado");
    }
