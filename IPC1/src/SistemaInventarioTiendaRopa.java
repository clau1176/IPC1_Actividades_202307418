import java.util.Scanner;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

// Clase principal del sistema
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
    
    // Función para buscar productos
    private static void buscarProducto() {
        System.out.println("\n--- BUSCAR PRODUCTO ---");
        System.out.println("1. Por código");
        System.out.println("2. Por nombre");
        System.out.println("3. Por categoría");
        System.out.print("Seleccione criterio de búsqueda: ");
        
        int criterio;
        try {
            criterio = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opción no válida.");
            return;
        }
        
        String busqueda;
        boolean encontrado = false;
        
        switch(criterio) {
            case 1:
                System.out.print("Ingrese código: ");
                busqueda = scanner.nextLine();
                System.out.println("\nResultados de la búsqueda:");
                
                for(int i = 0; i < contadorProductos; i++) {
                    if(inventario[i].getCodigo().equals(busqueda)) {
                        System.out.println(inventario[i]);
                        encontrado = true;
                    }
                }
                break;
                
            case 2:
                System.out.print("Ingrese nombre: ");
                busqueda = scanner.nextLine();
                System.out.println("\nResultados de la búsqueda:");
                
                for(int i = 0; i < contadorProductos; i++) {
                    if(inventario[i].getNombre().toLowerCase().contains(busqueda.toLowerCase())) {
                        System.out.println(inventario[i]);
                        encontrado = true;
                    }
                }
                break;
                
            case 3:
                System.out.print("Ingrese categoría: ");
                busqueda = scanner.nextLine();
                System.out.println("\nResultados de la búsqueda:");
                
                for(int i = 0; i < contadorProductos; i++) {
                    if(inventario[i].getCategoria().toLowerCase().contains(busqueda.toLowerCase())) {
                        System.out.println(inventario[i]);
                        encontrado = true;
                    }
                }
                break;
                
            default:
                System.out.println("Opción no válida.");
                return;
        }
        
        if(!encontrado) {
            System.out.println("No se encontraron productos.");
        }
        
        registrarBitacora("Buscar Producto", "Búsqueda realizada");
    }
    
    // Función para eliminar productos
    private static void eliminarProducto() {
        System.out.println("\n--- ELIMINAR PRODUCTO ---");
        System.out.print("Ingrese código del producto a eliminar: ");
        String codigo = scanner.nextLine();
        
        int indice = -1;
        for(int i = 0; i < contadorProductos; i++) {
            if(inventario[i].getCodigo().equals(codigo)) {
                indice = i;
                break;
            }
        }
        
        if(indice == -1) {
            System.out.println("Producto no encontrado.");
            registrarBitacora("Eliminar Producto", "Error: Producto no encontrado - " + codigo);
            return;
        }
        
        System.out.println("Producto encontrado:");
        System.out.println(inventario[indice]);
        System.out.print("¿Está seguro de eliminar este producto? (s/n): ");
        String confirmacion = scanner.nextLine();
        
        if(confirmacion.equalsIgnoreCase("s")) {
            // Desplazar elementos para eliminar
            for(int i = indice; i < contadorProductos - 1; i++) {
                inventario[i] = inventario[i + 1];
            }
            contadorProductos--;
            inventario[contadorProductos] = null;
            
            System.out.println("Producto eliminado exitosamente.");
            registrarBitacora("Eliminar Producto", "Éxito: Producto " + codigo + " eliminado");
        } else {
            System.out.println("Eliminación cancelada.");
            registrarBitacora("Eliminar Producto", "Cancelada: Producto " + codigo);
        }
    }
    
    // Función para registrar ventas
    private static void registrarVenta() {
        System.out.println("\n--- REGISTRAR VENTA ---");
        System.out.print("Ingrese código del producto: ");
        String codigo = scanner.nextLine();
        
        // Buscar producto
        int indice = -1;
        for(int i = 0; i < contadorProductos; i++) {
            if(inventario[i].getCodigo().equals(codigo)) {
                indice = i;
                break;
            }
        }
        
        if(indice == -1) {
            System.out.println("Producto no encontrado.");
            registrarBitacora("Registrar Venta", "Error: Producto no encontrado - " + codigo);
            return;
        }
        
        int cantidad = 0;
        while (true) {
            try {
                System.out.print("Cantidad a vender: ");
                cantidad = Integer.parseInt(scanner.nextLine());
                if (cantidad <= 0) {
                    System.out.println("Error: La cantidad debe ser positiva.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un valor numérico válido.");
            }
        }
        
        // Validar stock suficiente
        if(cantidad > inventario[indice].getCantidad()) {
            System.out.println("Error: Stock insuficiente. Stock disponible: " + inventario[indice].getCantidad());
            registrarBitacora("Registrar Venta", "Error: Stock insuficiente para producto " + codigo);
            return;
        }
        
        // Calcular total
        double total = cantidad * inventario[indice].getPrecio();
        
        // Actualizar inventario
        inventario[indice].setCantidad(inventario[indice].getCantidad() - cantidad);
        
        // Registrar venta
        Date fecha = new Date();
        ventas[contadorVentas] = new Venta(codigo, cantidad, total, fecha);
        contadorVentas++;
        
        System.out.println("Venta registrada exitosamente. Total: $" + total);
        registrarBitacora("Registrar Venta", "Éxito: Venta de " + cantidad + " unidades de " + codigo);
    }
    
    // Función para generar reportes
    private static void generarReportes() {
        System.out.println("\n--- GENERAR REPORTES ---");
        System.out.println("1. Reporte de Stock");
        System.out.println("2. Reporte de Ventas");
        System.out.print("Seleccione tipo de reporte: ");
        
        int opcion;
        try {
            opcion = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opción no válida.");
            return;
        }
        
        switch(opcion) {
            case 1:
                generarReporteStock();
                System.out.println("Reporte de stock generado exitosamente.");
                registrarBitacora("Generar Reportes", "Reporte de Stock generado");
                break;
                
            case 2:
                generarReporteVentas();
                System.out.println("Reporte de ventas generado exitosamente.");
                registrarBitacora("Generar Reportes", "Reporte de Ventas generado");
                break;
                
            default:
                System.out.println("Opción no válida.");
        }
    }
    
    // Función para mostrar datos del estudiante
    private static void verDatosEstudiante() {
        System.out.println("\n--- DATOS DEL ESTUDIANTE ---");
        System.out.println("Nombre: [Tu Nombre Completo]");
        System.out.println("Carnet: [Tu Carnet]");
        System.out.println("Laboratorio: [Nombre del Laboratorio]");
        System.out.println("Sección: [Tu Sección]");
        System.out.println("GitHub: [Tu Usuario de GitHub]");
        registrarBitacora("Ver Datos Estudiante", "Datos mostrados");
    }
    
    // Función para mostrar la bitácora
    private static void mostrarBitacora() {
        System.out.println("\n--- BITÁCORA ---");
        if (contadorBitacora == 0) {
            System.out.println("No hay registros en la bitácora.");
            return;
        }
        
        for(int i = 0; i < contadorBitacora; i++) {
            System.out.println(bitacora[i]);
        }
        registrarBitacora("Ver Bitácora", "Bitácora mostrada");
    }
    
    // Función para registrar acciones en la bitácora
    private static void registrarBitacora(String accion, String detalle) {
        if(contadorBitacora < bitacora.length) {
            bitacora[contadorBitacora] = new Bitacora(accion, detalle);
            contadorBitacora++;
        }
    }
    
    

