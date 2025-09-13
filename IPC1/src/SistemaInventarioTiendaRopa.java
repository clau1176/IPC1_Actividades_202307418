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
    
    // Función para cargar datos desde archivos
    private static void cargarDatos() {
        // Cargar inventario
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/inventario.txt"));
            String linea;
            while((linea = reader.readLine()) != null && contadorProductos < inventario.length) {
                String[] datos = linea.split(",");
                if(datos.length == 5) {
                    String codigo = datos[0];
                    String nombre = datos[1];
                    String categoria = datos[2];
                    double precio = Double.parseDouble(datos[3]);
                    int cantidad = Integer.parseInt(datos[4]);
                    
                    inventario[contadorProductos] = new Producto(codigo, nombre, categoria, precio, cantidad);
                    contadorProductos++;
                }
            }
            reader.close();
        } catch(IOException e) {
            System.out.println("No se pudo cargar el inventario. Se iniciará vacío.");
        }
        
        // Cargar ventas
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/ventas.txt"));
            String linea;
            while((linea = reader.readLine()) != null && contadorVentas < ventas.length) {
                String[] datos = linea.split(",");
                if(datos.length == 4) {
                    String codigo = datos[0];
                    int cantidad = Integer.parseInt(datos[1]);
                    double total = Double.parseDouble(datos[2]);
                    Date fecha = new Date(Long.parseLong(datos[3]));
                    
                    ventas[contadorVentas] = new Venta(codigo, cantidad, total, fecha);
                    contadorVentas++;
                }
            }
            reader.close();
        } catch(IOException e) {
            System.out.println("No se pudo cargar el historial de ventas. Se iniciará vacío.");
        }
        
        // Cargar bitácora
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/bitacora.txt"));
            String linea;
            while((linea = reader.readLine()) != null && contadorBitacora < bitacora.length) {
                String[] datos = linea.split(",");
                if(datos.length == 3) {
                    String accion = datos[0];
                    String detalle = datos[1];
                    Date fecha = new Date(Long.parseLong(datos[2]));
                    
                    bitacora[contadorBitacora] = new Bitacora(accion, detalle, fecha);
                    contadorBitacora++;
                }
            }
            reader.close();
        } catch(IOException e) {
            System.out.println("No se pudo cargar la bitácora. Se iniciará vacía.");
        }
    }
    
    // Función para guardar datos en archivos
    private static void guardarDatos() {
        // Guardar inventario
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/inventario.txt"));
            for(int i = 0; i < contadorProductos; i++) {
                Producto p = inventario[i];
                writer.write(p.getCodigo() + "," + p.getNombre() + "," + p.getCategoria() + "," + 
                            p.getPrecio() + "," + p.getCantidad() + "\n");
            }
            writer.close();
        } catch(IOException e) {
            System.out.println("Error al guardar el inventario: " + e.getMessage());
        }
        
        // Guardar ventas
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/ventas.txt"));
            for(int i = 0; i < contadorVentas; i++) {
                Venta v = ventas[i];
                writer.write(v.getCodigoProducto() + "," + v.getCantidad() + "," + 
                            v.getTotal() + "," + v.getFecha().getTime() + "\n");
            }
            writer.close();
        } catch(IOException e) {
            System.out.println("Error al guardar las ventas: " + e.getMessage());
        }
        
        // Guardar bitácora
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/bitacora.txt"));
            for(int i = 0; i < contadorBitacora; i++) {
                Bitacora b = bitacora[i];
                writer.write(b.getAccion() + "," + b.getDetalle() + "," + b.getFecha().getTime() + "\n");
            }
            writer.close();
        } catch(IOException e) {
            System.out.println("Error al guardar la bitácora: " + e.getMessage());
        }
    }
    
    // Función para generar reporte de stock
    private static void generarReporteStock() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        String nombreArchivo = "reportes/" + sdf.format(new Date()) + "_Stock.pdf";
        
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo));
            
            // Encabezado del reporte
            writer.println("=== REPORTE DE STOCK ===");
            writer.println("Fecha: " + new Date());
            writer.println("==============================================");
            writer.println("Código\tNombre\tCategoría\tPrecio\tStock");
            writer.println("==============================================");
            
            // Datos de productos
            for(int i = 0; i < contadorProductos; i++) {
                Producto p = inventario[i];
                writer.println(p.getCodigo() + "\t" + p.getNombre() + "\t" + 
                              p.getCategoria() + "\t$" + p.getPrecio() + "\t" + p.getCantidad());
            }
            
            writer.close();
            System.out.println("Reporte guardado como: " + nombreArchivo);
            
        } catch(IOException e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
        }
    }
    
    // Función para generar reporte de ventas
    private static void generarReporteVentas() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        String nombreArchivo = "reportes/" + sdf.format(new Date()) + "_Venta.pdf";
        
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo));
            
            // Encabezado del reporte
            writer.println("=== REPORTE DE VENTAS ===");
            writer.println("Fecha: " + new Date());
            writer.println("======================================================");
            writer.println("Código\tCantidad\tTotal\tFecha");
            writer.println("======================================================");
            
            // Datos de ventas
            double totalVentas = 0;
            SimpleDateFormat fechaFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            for(int i = 0; i < contadorVentas; i++) {
                Venta v = ventas[i];
                writer.println(v.getCodigoProducto() + "\t" + v.getCantidad() + 
                              "\t$" + v.getTotal() + "\t" + fechaFormat.format(v.getFecha()));
                totalVentas += v.getTotal();
            }
            
            writer.println("======================================================");
            writer.println("TOTAL DE VENTAS: $" + totalVentas);
            
            writer.close();
            System.out.println("Reporte guardado como: " + nombreArchivo);
            
        } catch(IOException e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
        }
    }
}

// Clase para representar un producto
class Producto {
    private String codigo;
    private String nombre;
    private String categoria;
    private double precio;
    private int cantidad;
    
    public Producto(String codigo, String nombre, String categoria, double precio, int cantidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.cantidad = cantidad;
    }
    
    // Getters y setters
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public double getPrecio() { return precio; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    @Override
    public String toString() {
        return "Código: " + codigo + ", Nombre: " + nombre + ", Categoría: " + categoria + 
               ", Precio: $" + precio + ", Stock: " + cantidad;
    }
}

// Clase para representar una venta
class Venta {
    private String codigoProducto;
    private int cantidad;
    private double total;
    private Date fecha;
    
    public Venta(String codigoProducto, int cantidad, double total, Date fecha) {
        this.codigoProducto = codigoProducto;
        this.cantidad = cantidad;
        this.total = total;
        this.fecha = fecha;
    }
    
    // Getters
    public String getCodigoProducto() { return codigoProducto; }
    public int getCantidad() { return cantidad; }
    public double getTotal() { return total; }
    public Date getFecha() { return fecha; }
}

// Clase para representar una entrada en la bitácora
class Bitacora {
    private String accion;
    private String detalle;
    private Date fecha;
    
    public Bitacora(String accion, String detalle) {
        this.accion = accion;
        this.detalle = detalle;
        this.fecha = new Date();
    }
    
    public Bitacora(String accion, String detalle, Date fecha) {
        this.accion = accion;
        this.detalle = detalle;
        this.fecha = fecha;
    }
    
    // Getters
    public String getAccion() { return accion; }
    public String getDetalle() { return detalle; }
    public Date getFecha() { return fecha; }
    
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(fecha) + " - " + accion + " - " + detalle;
    }
}
    


