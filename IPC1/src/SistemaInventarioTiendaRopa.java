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
    
}