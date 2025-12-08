import java.io.*;
import java.util.*;

public class TetrisConsola {
    
    // CONSTANTES DEL JUEGO
    static final int FILAS = 20;
    static final int COLUMNAS = 10;
    static final int CARNE = 202307418; 
    // MATRICES PARA LAS PIEZAS (TETROMINOS)
    static final int PIEZA_I[][] = {
        {0, 1, 0, 0},
        {0, 1, 0, 0},
        {0, 1, 0, 0},
        {0, 1, 0, 0}
    };
    
    static final int PIEZA_O[][] = {
        {1, 1, 0, 0},
        {1, 1, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
    };
    
    static final int PIEZA_T[][] = {
        {0, 1, 0, 0},
        {1, 1, 1, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
    };
    
    static final int PIEZA_S[][] = {
        {0, 1, 1, 0},
        {1, 1, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
    };
    
    static final int PIEZA_Z[][] = {
        {1, 1, 0, 0},
        {0, 1, 1, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
    };
    
    static final int PIEZA_J[][] = {
        {1, 0, 0, 0},
        {1, 1, 1, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
    };
    
    static final int PIEZA_L[][] = {
        {0, 0, 1, 0},
        {1, 1, 1, 0},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
    };
    
    // ARREGLO DE PIEZAS
    static final int PIEZAS[][][] = {
        PIEZA_I, PIEZA_O, PIEZA_T, PIEZA_S, PIEZA_Z, PIEZA_J, PIEZA_L
    };
    
    // VARIABLES DEL JUEGO 
    static int tablero[][] = new int[FILAS][COLUMNAS];
    static int piezaActual[][] = new int[4][4];
    static int siguientePieza[][] = new int[4][4];
    static int piezaX;
    static int piezaY;
    static int tipoPiezaActual;
    static int tipoSiguientePieza;
    
    // ESTADÍSTICAS Y PUNTUACIÓN
    static int puntuacion = 0;
    static int nivel = 1;
    static int lineasEliminadas = 0;
    static int juegoActivo = 0; // 0=false, 1=true
    static int pausado = 0; // 0=false, 1=true
    
    // NOMBRES DE ARCHIVOS
    static String ARCHIVO_PUNTAJES = "mejores_puntajes.txt";
    static String ARCHIVO_ESTADISTICAS = "estadisticas.txt";
    
    public static void main(String[] args) {
        // Inicializar archivos si no existen
        inicializarArchivos();
        
        // Scanner principal
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            limpiarConsola();
            mostrarCarne();
            mostrarMenuPrincipal();
            
            int opcion = leerEntero(scanner, 1, 4);
            
            switch (opcion) {
                case 1:
                    iniciarNuevaPartida(scanner);
                    break;
                case 2:
                    mostrarMejoresPuntajes();
                    break;
                case 3:
                    mostrarEstadisticasGenerales();
                    break;
                case 4:
                    System.out.println("¡Gracias por jugar!");
                    scanner.close();
                    return;
            }
            
            System.out.println("\nPresione Enter para continuar...");
            scanner.nextLine();
        }
    }
    
    // ==================== MÉTODOS DEL MENÚ ====================
    
    static void mostrarCarne() {
        System.out.println("========================================");
        System.out.println("CARNÉ: " + CARNE);
        System.out.println("========================================\n");
    }
    
    static void mostrarMenuPrincipal() {
        System.out.println("=== TETRIS - MENÚ PRINCIPAL ===");
        System.out.println("1. Jugar Partida Nueva");
        System.out.println("2. Ver Mejores Puntajes");
        System.out.println("3. Ver Estadísticas Generales");
        System.out.println("4. Salir");
        System.out.print("\nSeleccione una opción: ");
    }
    
    static void mostrarMejoresPuntajes() {
        limpiarConsola();
        mostrarCarne();
        System.out.println("=== MEJORES PUNTAJES ===\n");
        
        try {
            // Abrir archivo manualmente
            FileReader fileReader = new FileReader(ARCHIVO_PUNTAJES);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            String linea;
            int posicion = 1;
            
            System.out.println("Pos. Nombre            Puntaje  Nivel  Líneas");
            System.out.println("----------------------------------------------");
            
            while ((linea = bufferedReader.readLine()) != null && posicion <= 10) {
                String partes[] = linea.split(",");
                if (partes.length >= 4) {
                    System.out.printf("%3d. %-15s %8d %6d %7d\n", 
                        posicion++, partes[0], 
                        Integer.parseInt(partes[1]),
                        Integer.parseInt(partes[2]),
                        Integer.parseInt(partes[3]));
                }
            }
            
            bufferedReader.close();
            fileReader.close();
            
        } catch (IOException e) {
            System.out.println("No hay puntajes registrados aún.");
        }
    }
    
    static void mostrarEstadisticasGenerales() {
        limpiarConsola();
        mostrarCarne();
        System.out.println("=== ESTADÍSTICAS GENERALES ===\n");
        
        try {
            FileReader fileReader = new FileReader(ARCHIVO_ESTADISTICAS);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            System.out.println("Total de partidas jugadas: " + bufferedReader.readLine());
            System.out.println("Puntaje máximo: " + bufferedReader.readLine());
            System.out.println("Líneas totales eliminadas: " + bufferedReader.readLine());
            System.out.println("Nivel máximo alcanzado: " + bufferedReader.readLine());
            
            bufferedReader.close();
            fileReader.close();
            
        } catch (IOException e) {
            System.out.println("No hay estadísticas disponibles.");
        }
    }
    
    // ==================== MÉTODOS DEL JUEGO ====================
    
    static void iniciarNuevaPartida(Scanner scanner) {
        // Inicializar juego
        inicializarJuego();
        
        // Variables para control de entrada
        juegoActivo = 1; // true
        pausado = 0; // false
        
        // Bucle principal del juego
        while (juegoActivo == 1) {
            mostrarInterfazJuego();
            
            System.out.print("\nComando (A=izq, D=der, S=bajar, W=rotar, Q=caída total, P=pausa, ESC=salir): ");
            
            String entrada = scanner.nextLine();
            
            if (entrada.length() == 0) {
                continue;
            }
            
            entrada = entrada.trim();
            entrada = entrada.toUpperCase();
            
            char comando = entrada.charAt(0);
            
            if (comando == 'P') {
                pausado = (pausado == 0) ? 1 : 0;
                if (pausado == 1) {
                    System.out.println("\nJUEGO PAUSADO - Presione P para reanudar");
                    System.out.println("Presione Enter para continuar...");
                    scanner.nextLine();
                }
                continue;
            }
            
            if (comando == 27 || entrada.equals("ESC")) {
                juegoActivo = 0;
                continue;
            }
            
            if (pausado == 0) {
                procesarComando(comando);
                
                // Verificar si el juego terminó
                if (verificarColision(piezaX, piezaY, piezaActual) == 1) {
                    finDelJuego(scanner);
                    break;
                }
            }
        }
    }
    
    static void procesarComando(char comando) {
        switch (comando) {
            case 'A':
                moverPiezaIzquierda();
                break;
            case 'D':
                moverPiezaDerecha();
                break;
            case 'S':
                if (moverPiezaAbajo() == 1) {
                    puntuacion += 1; // Soft drop points
                } else {
                    // Si no se puede bajar más, colocar la pieza
                    colocarPieza();
                    verificarLineasCompletas();
                    generarNuevaPieza();
                }
                break;
            case 'W':
                rotarPieza();
                break;
            case 'Q':
                caidaInstantanea();
                break;
        }
    }
    
    static void inicializarJuego() {
        // Limpiar tablero
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                tablero[i][j] = 0;
            }
        }
        
        // Inicializar variables
        puntuacion = 0;
        nivel = 1;
        lineasEliminadas = 0;
        
        // Generar primera pieza
        tipoSiguientePieza = (int)(Math.random() * 7); // 7 piezas
        generarNuevaPieza();
    }
    
    
