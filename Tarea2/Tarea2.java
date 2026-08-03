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
 
        // TODO: en los siguientes commits se agregará la lógica completa
        // (métodos de validación, arreglo 1D y matriz 2D).
 
        // --- Sección: arreglo unidimensional ---
        tamanioArreglo = leerEnteroValidado("¿Cuántos números desea ingresar en el arreglo? (1-20): ", 1, 20);
        int[] arreglo = new int[tamanioArreglo];
        llenarArreglo(arreglo);
        procesarArreglo(arreglo);
 
        // --- Sección: arreglo bidimensional (matriz) ---
        filas = leerEnteroValidado("\n¿Cuántas filas tendrá la matriz? (1-10): ", 1, 10);
        columnas = leerEnteroValidado("¿Cuántas columnas tendrá la matriz? (1-10): ", 1, 10);
        int[][] matriz = new int[filas][columnas];
        llenarMatriz(matriz);
        procesarMatriz(matriz);
 
        /*
         * Sección final: se calcula el promedio general del arreglo
         * unidimensional y, a partir de él, se asigna una "calificación"
         * en letra (char) como ejemplo adicional de uso de tipos primitivos.
         * La variable booleana programaValido se usa para confirmar que
         * el programa terminó su ejecución sin inconvenientes.
         */
        promedio = calcularPromedio(arreglo);
        calificacion = obtenerCalificacion(promedio);
 
        System.out.println("\n=== RESUMEN FINAL ===");
        System.out.printf("Promedio general del arreglo: %.2f%n", promedio);
        System.out.println("Calificación asignada: " + calificacion);
        System.out.println("Ejecución finalizada correctamente: " + programaValido);
 
        sc.close();
    }
 
    /**
     * Calcula el promedio de los valores de un arreglo unidimensional.
     *
     * @param arreglo arreglo de enteros ya lleno
     * @return promedio como valor double
     */
    static double calcularPromedio(int[] arreglo) {
        int suma = 0;
        for (int i = 0; i < arreglo.length; i++) {
            suma += arreglo[i];
        }
        return (double) suma / arreglo.length;
    }
 
    /**
     * Asigna una calificación en letra según el promedio recibido.
     * Ejemplo simple de uso del tipo char junto con condicionales.
     *
     * @param promedioValor promedio calculado previamente
     * @return 'A' si promedio >= 90, 'B' si >= 75, 'C' si >= 60, 'D' en otro caso
     */
    static char obtenerCalificacion(double promedioValor) {
        char letra;
        if (promedioValor >= 90) {
            letra = 'A';
        } else if (promedioValor >= 75) {
            letra = 'B';
        } else if (promedioValor >= 60) {
            letra = 'C';
        } else {
            letra = 'D';
        }
        return letra;
    }
 
    /**
     * Llena una matriz (arreglo bidimensional) solicitando al usuario
     * un valor por cada celda, validado en el rango 1-100.
     *
     * @param matriz matriz previamente creada (vacía) que se llenará
     */
    static void llenarMatriz(int[][] matriz) {
        System.out.println("\n-- Llenado de la matriz --");
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                matriz[i][j] = leerEnteroValidado(
                        "Ingrese el valor para la posición [fila " + (i + 1) + ", columna " + (j + 1) + "] (1-100): ",
                        1, 100);
            }
        }
    }
 
    /**
     * Recorre la matriz por filas y columnas, mostrando su contenido
     * de forma ordenada, la suma total de todos los elementos y la
     * suma correspondiente a cada fila.
     *
     * @param matriz matriz ya llena con datos válidos
     */
    static void procesarMatriz(int[][] matriz) {
        System.out.println("\n-- Contenido de la matriz --");
        int sumaTotal = 0;
 
        for (int i = 0; i < matriz.length; i++) {
            int sumaFila = 0;
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.printf("%5d", matriz[i][j]); // impresión alineada por columnas
                sumaFila += matriz[i][j];
            }
            sumaTotal += sumaFila;
            System.out.println("   -> Suma fila " + (i + 1) + ": " + sumaFila);
        }
 
        System.out.println("Suma total de todos los elementos de la matriz: " + sumaTotal);
    }
 
    /**
     * Llena un arreglo de una dimensión pidiendo al usuario un valor
     * por cada posición. Cada valor se valida en el rango 1-100
     * mediante leerEnteroValidado.
     *
     * @param arreglo arreglo previamente creado (vacío) que se llenará
     */
    static void llenarArreglo(int[] arreglo) {
        System.out.println("\n-- Llenado del arreglo unidimensional --");
        for (int i = 0; i < arreglo.length; i++) {
            arreglo[i] = leerEnteroValidado("Ingrese el número #" + (i + 1) + " (1-100): ", 1, 100);
        }
    }
 
    /**
     * Recorre el arreglo unidimensional y muestra el valor máximo,
     * el valor mínimo y el promedio de sus elementos.
     *
     * @param arreglo arreglo ya lleno con datos válidos
     */
    static void procesarArreglo(int[] arreglo) {
        int maximo = arreglo[0];
        int minimo = arreglo[0];
        int suma = 0;
 
        for (int i = 0; i < arreglo.length; i++) {
            suma += arreglo[i];
            if (arreglo[i] > maximo) {
                maximo = arreglo[i];
            }
            if (arreglo[i] < minimo) {
                minimo = arreglo[i];
            }
        }
 
        double promedioArreglo = (double) suma / arreglo.length; // conversión explícita a double
 
        System.out.println("\n-- Resultados del arreglo unidimensional --");
        System.out.println("Máximo:   " + maximo);
        System.out.println("Mínimo:   " + minimo);
        System.out.printf("Promedio: %.2f%n", promedioArreglo);
    }
 
    /**
     * Solicita al usuario un número entero por consola y lo valida.
     * Se vuelve a pedir el dato mientras:
     *  - el usuario no ingrese un número entero (se captura el error de tipo), o
     *  - el número esté fuera del rango [min, max].
     *
     * @param mensaje texto que se muestra al usuario al solicitar el dato
     * @param min     valor mínimo permitido (inclusive)
     * @param max     valor máximo permitido (inclusive)
     * @return un entero válido dentro del rango solicitado
     */
    static int leerEnteroValidado(String mensaje, int min, int max) {
        boolean esValido = false; // bandera de control del ciclo de validación
        int valor = 0;
 
        while (!esValido) {
            System.out.print(mensaje);
            if (sc.hasNextInt()) {
                valor = sc.nextInt();
                if (valor >= min && valor <= max) {
                    esValido = true; // el dato cumple tipo y rango
                } else {
                    System.out.println("Error: el valor debe estar entre " + min + " y " + max + ". Intente de nuevo.");
                }
            } else {
                // El usuario ingresó algo que no es un entero (por ejemplo, texto)
                System.out.println("Error: debe ingresar un número entero. Intente de nuevo.");
                sc.next(); // se descarta el token inválido para no dejar el Scanner "atascado"
            }
        }
        return valor;
    }
}
 
