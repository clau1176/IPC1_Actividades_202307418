import java.util.Scanner;
 
/**
 * Tarea2 - IPC1
 * Estudiante: Claudio Arrillaga
 * Carné: 202307418
 *
 * Programa de consola que solicita, valida y procesa datos numéricos
 * ingresados por el usuario, tanto en un arreglo unidimensional como
 * en una matriz (arreglo bidimensional).
 */
public class Tarea2 {
 
    // Scanner único para toda la entrada de consola del programa
    static Scanner sc = new Scanner(System.in);
 
    public static void main(String[] args) {
 
        /*
         * Declaración de variables con distintos tipos primitivos.
         * - int      : tamaños de los arreglos y contadores
         * - double   : promedio de los datos
         * - char     : letra de calificación según el promedio
         * - boolean  : bandera para controlar el flujo del programa
         */
        int tamanioArreglo;
        int filas;
        int columnas;
        double promedio = 0.0;
        char calificacion = ' ';
        boolean programaValido = true;
 
        System.out.println("=== TAREA 2 - IPC1 ===");
        System.out.println("Estudiante: Claudio Arrillaga | Carné: 202307418");
 
        
        // --- Sección: arreglo unidimensional 
        tamanioArreglo = leerEnteroValidado("¿Cuántos números desea ingresar en el arreglo? (1-20): ", 1, 20);
        int[] arreglo = new int[tamanioArreglo];
        llenarArreglo(arreglo);
        procesarArreglo(arreglo);
 
        // --- Sección: arreglo bidimensional (matriz) 
        filas = leerEnteroValidado("\n¿Cuántas filas tendrá la matriz? (1-10): ", 1, 10);
        columnas = leerEnteroValidado("¿Cuántas columnas tendrá la matriz? (1-10): ", 1, 10);
        int[][] matriz = new int[filas][columnas];
        llenarMatriz(matriz);
        procesarMatriz(matriz);
 
        /*
         * Sección final: se calcula el promedio general del arreglo
         * unidimensional y, a partir de él, se asigna una "calificación"
         
    }
}
 
