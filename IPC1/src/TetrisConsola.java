import java.io.*;
import java.util.*;

public class TetrisConsola {
    
    // CONSTANTES DEL JUEGO
    static final int FILAS = 20;
    static final int COLUMNAS = 10;
    static final int CARNE = 202307418; 
    
    // MATRICES PARA LAS PIEZAS 
    static final int PIEZA_I[][] = {
        {0, 0, 0, 0},
        {1, 1, 1, 1},
        {0, 0, 0, 0},
        {0, 0, 0, 0}
    };
    
    static final int PIEZA_O[][] = {
        {0, 0, 0, 0},
        {0, 1, 1, 0},
        {0, 1, 1, 0},
        {0, 0, 0, 0}
    };
    
    static final int PIEZA_T[][] = {
        {0, 0, 0, 0},
        {0, 1, 0, 0},
        {1, 1, 1, 0},
        {0, 0, 0, 0}
    };
    
    static final int PIEZA_S[][] = {
        {0, 0, 0, 0},
        {0, 1, 1, 0},
        {1, 1, 0, 0},
        {0, 0, 0, 0}
    };
    
    static final int PIEZA_Z[][] = {
        {0, 0, 0, 0},
        {1, 1, 0, 0},
        {0, 1, 1, 0},
        {0, 0, 0, 0}
    };
    
    static final int PIEZA_J[][] = {
        {0, 0, 0, 0},
        {1, 0, 0, 0},
        {1, 1, 1, 0},
        {0, 0, 0, 0}
    };
    
    static final int PIEZA_L[][] = {
        {0, 0, 0, 0},
        {0, 0, 1, 0},
        {1, 1, 1, 0},
        {0, 0, 0, 0}
    };
    
    // ARREGLO DE PIEZAS
    static final int PIEZAS[][][] = {
        PIEZA_I, PIEZA_O, PIEZA_T, PIEZA_S, PIEZA_Z, PIEZA_J, PIEZA_L
    };
    
    // NOMBRES DE LAS PIEZAS 
    static final String NOMBRES_PIEZAS[] = {"I", "O", "T", "S", "Z", "J", "L"};
    
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
    static int tiempoSegundos = 0;
    static int juegoActivo = 0;
    static int pausado = 0;
    
    // NOMBRES DE ARCHIVOS
    static String ARCHIVO_PUNTAJES = "mejores_puntajes.txt";
    static String ARCHIVO_ESTADISTICAS = "estadisticas.txt";
    
    //  EL TIEMPO
    static long tiempoInicio = 0;
    
    public static void main(String[] args) {
        inicializarArchivos();
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
        System.out.println("TETRIS - CARNÉ: " + CARNE);
        System.out.println();
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
        inicializarJuego();
        
        juegoActivo = 1;
        pausado = 0;
        tiempoInicio = System.currentTimeMillis();
        
        // Hilo para actualizar el tiempo
        Thread hiloTiempo = new Thread(new Runnable() {
            public void run() {
                while (juegoActivo == 1) {
                    if (pausado == 0) {
                        tiempoSegundos = (int)((System.currentTimeMillis() - tiempoInicio) / 1000);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        
        hiloTiempo.start();
        
        // Bucle principal del juego
        while (juegoActivo == 1) {
            mostrarInterfazJuego();
            
            System.out.print("Comando (A/D/S/M/Q/P/ESC): ");
            
            String entrada = scanner.nextLine();
            
            if (entrada.length() == 0) {
                continue;
            }
            
            entrada = entrada.trim().toUpperCase();
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
                
                if (verificarColision(piezaX, piezaY, piezaActual) == 1) {
                    finDelJuego(scanner);
                    break;
                }
            }
        }
        
        hiloTiempo.interrupt();
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
                    puntuacion += 1;
                } else {
                    colocarPieza();
                    verificarLineasCompletas();
                    generarNuevaPieza();
                }
                break;
            case 'W':
            case 'M':
                rotarPieza();
                break;
            case 'Q':
                caidaInstantanea();
                break;
        }
    }
    
    static void inicializarJuego() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                tablero[i][j] = 0;
            }
        }
        
        puntuacion = 0;
        nivel = 1;
        lineasEliminadas = 0;
        tiempoSegundos = 0;
        
        tipoSiguientePieza = (int)(Math.random() * 7);
        generarNuevaPieza();
    }
    
    static void generarNuevaPieza() {
        tipoPiezaActual = tipoSiguientePieza;
        copiarMatriz(PIEZAS[tipoPiezaActual], piezaActual, 4, 4);
        
        tipoSiguientePieza = (int)(Math.random() * 7);
        copiarMatriz(PIEZAS[tipoSiguientePieza], siguientePieza, 4, 4);
        
        piezaX = COLUMNAS / 2 - 2;
        piezaY = 0;
        
        if (verificarColision(piezaX, piezaY, piezaActual) == 1) {
            juegoActivo = 0;
        }
    }
    
    static int moverPiezaIzquierda() {
        if (verificarColision(piezaX - 1, piezaY, piezaActual) == 0) {
            piezaX--;
            return 1;
        }
        return 0;
    }
    
    static int moverPiezaDerecha() {
        if (verificarColision(piezaX + 1, piezaY, piezaActual) == 0) {
            piezaX++;
            return 1;
        }
        return 0;
    }
    
    static int moverPiezaAbajo() {
        if (verificarColision(piezaX, piezaY + 1, piezaActual) == 0) {
            piezaY++;
            return 1;
        }
        return 0;
    }
    
    static void rotarPieza() {
        int piezaRotada[][] = new int[4][4];
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                piezaRotada[i][j] = piezaActual[3 - j][i];
            }
        }
        
        if (verificarColision(piezaX, piezaY, piezaRotada) == 0) {
            copiarMatriz(piezaRotada, piezaActual, 4, 4);
        }
    }
    
    static void caidaInstantanea() {
        int celdasDescendidas = 0;
        
        while (moverPiezaAbajo() == 1) {
            celdasDescendidas++;
        }
        
        puntuacion += celdasDescendidas * 2;
        
        colocarPieza();
        verificarLineasCompletas();
        generarNuevaPieza();
    }
    
    static int verificarColision(int x, int y, int pieza[][]) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (pieza[i][j] != 0) {
                    int tableroX = x + j;
                    int tableroY = y + i;
                    
                    if (tableroX < 0 || tableroX >= COLUMNAS || tableroY >= FILAS) {
                        return 1;
                    }
                    
                    if (tableroY >= 0 && tablero[tableroY][tableroX] != 0) {
                        return 1;
                    }
                }
            }
        }
        return 0;
    }
    
    static void colocarPieza() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (piezaActual[i][j] != 0) {
                    int tableroX = piezaX + j;
                    int tableroY = piezaY + i;
                    
                    if (tableroY >= 0 && tableroY < FILAS && tableroX >= 0 && tableroX < COLUMNAS) {
                        tablero[tableroY][tableroX] = tipoPiezaActual + 1;
                    }
                }
            }
        }
    }
    
    static void verificarLineasCompletas() {
        int lineasCompletadas = 0;
        
        for (int fila = FILAS - 1; fila >= 0; fila--) {
            int lineaCompleta = 1;
            
            for (int col = 0; col < COLUMNAS; col++) {
                if (tablero[fila][col] == 0) {
                    lineaCompleta = 0;
                    break;
                }
            }
            
            if (lineaCompleta == 1) {
                for (int f = fila; f > 0; f--) {
                    for (int c = 0; c < COLUMNAS; c++) {
                        tablero[f][c] = tablero[f - 1][c];
                    }
                }
                
                for (int c = 0; c < COLUMNAS; c++) {
                    tablero[0][c] = 0;
                }
                
                lineasCompletadas++;
                lineasEliminadas++;
                fila++;
            }
        }
        
        if (lineasCompletadas > 0) {
            int puntosBase = 0;
            
            if (lineasCompletadas == 1) puntosBase = 100;
            else if (lineasCompletadas == 2) puntosBase = 300;
            else if (lineasCompletadas == 3) puntosBase = 500;
            else if (lineasCompletadas == 4) puntosBase = 800;
            
            puntuacion += puntosBase * nivel;
            
            int nuevoNivel = (lineasEliminadas / 10) + 1;
            if (nuevoNivel > nivel) {
                nivel = nuevoNivel;
            }
        }
    }
    
    static void finDelJuego(Scanner scanner) {
        juegoActivo = 0;
        limpiarConsola();
        mostrarCarne();
        
        System.out.println("=== GAME OVER ===");
        System.out.println("Puntuación final: " + puntuacion);
        System.out.println("Nivel alcanzado: " + nivel);
        System.out.println("Líneas eliminadas: " + lineasEliminadas);
        System.out.println("Tiempo jugado: " + formatTiempo(tiempoSegundos));
        
        System.out.print("\nIngrese su nombre (máx. 15 caracteres): ");
        String nombre = scanner.nextLine().trim();
        
        if (nombre.length() > 15) {
            nombre = nombre.substring(0, 15);
        }
        
        if (nombre.length() > 0) {
            guardarPuntaje(nombre, puntuacion, nivel, lineasEliminadas);
            actualizarEstadisticas();
        }
        
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
    
    // ==================== MÉTODOS DE VISUALIZACIÓN ====================
    
    static void mostrarInterfazJuego() {
        limpiarConsola();
        mostrarCarne();
        
        // Cabeceras alineadas
        System.out.println("SIGUIENTE      PUNTUACIÓN");
        System.out.println();
        
        // Mostrar la siguiente pieza correctamente
        for (int i = 0; i < 4; i++) {
            // Mostrar la siguiente pieza
            System.out.print("  ");
            for (int j = 0; j < 4; j++) {
                if (siguientePieza[i][j] == 0) {
                    System.out.print("  ");
                } else {
                    System.out.print("██");
                }
            }
            
            // Mostrar estadísticas al lado derecho
            if (i == 0) {
                System.out.print("      " + String.format("%4d", puntuacion) + " pts");
            } else if (i == 1) {
                System.out.print("      NIVEL: " + nivel);
            } else if (i == 2) {
                System.out.print("      LÍNEAS: " + lineasEliminadas);
            } else if (i == 3) {
                System.out.print("      TIEMPO: " + formatTiempo(tiempoSegundos));
            }
            System.out.println();
        }
        
        // Línea separadora
        System.out.println();
        for (int i = 0; i < 50; i++) {
            System.out.print("─");
        }
        System.out.println();
        
        // Mostrar tablero
        int tableroTemporal[][] = new int[FILAS][COLUMNAS];
        
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                tableroTemporal[i][j] = tablero[i][j];
            }
        }
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (piezaActual[i][j] != 0) {
                    int x = piezaX + j;
                    int y = piezaY + i;
                    
                    if (y >= 0 && y < FILAS && x >= 0 && x < COLUMNAS) {
                        tableroTemporal[y][x] = tipoPiezaActual + 1;
                    }
                }
            }
        }
        
        // Dibujar tablero con bordes
        System.out.print("┌");
        for (int i = 0; i < COLUMNAS * 2; i++) {
            System.out.print("─");
        }
        System.out.println("┐");
        
        for (int i = 0; i < FILAS; i++) {
            System.out.print("│");
            for (int j = 0; j < COLUMNAS; j++) {
                if (tableroTemporal[i][j] == 0) {
                    System.out.print("  ");
                } else if (tablero[i][j] == 0) {
                    System.out.print("▓▓"); // Pieza actual
                } else {
                    System.out.print("██"); // Pieza colocada
                }
            }
            System.out.println("│");
        }
        
        System.out.print("└");
        for (int i = 0; i < COLUMNAS * 2; i++) {
            System.out.print("─");
        }
        System.out.println("┘");
        
        // Controles
        System.out.println("\nCONTROLES: A/D (Izq/der) | S (abajo) | M (rotar)");
        System.out.println("Q (caída rápida) | P (pausa) | ESC (salir)");
        System.out.println();
        
        if (pausado == 1) {
            System.out.println("JUEGO PAUSADO - Presione P para reanudar");
        }
    }
    
    static String formatTiempo(int segundos) {
        int minutos = segundos / 60;
        int segs = segundos % 60;
        return String.format("%d:%02d", minutos, segs);
    }
    
    // ==================== MÉTODOS DE ARCHIVOS ====================
    
    static void inicializarArchivos() {
        try {
            File archivoPuntajes = new File(ARCHIVO_PUNTAJES);
            if (archivoPuntajes.exists() == false) {
                archivoPuntajes.createNewFile();
            }
            
            File archivoEstadisticas = new File(ARCHIVO_ESTADISTICAS);
            if (archivoEstadisticas.exists() == false) {
                FileWriter writer = new FileWriter(archivoEstadisticas);
                writer.write("0\n0\n0\n1\n");
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("Error al inicializar archivos");
        }
    }
    
    static void guardarPuntaje(String nombre, int puntuacion, int nivel, int lineas) {
        try {
            String puntajes[] = new String[100];
            int contador = 0;
            
            FileReader fileReader = new FileReader(ARCHIVO_PUNTAJES);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            String linea;
            while ((linea = bufferedReader.readLine()) != null && contador < 100) {
                puntajes[contador] = linea;
                contador++;
            }
            
            bufferedReader.close();
            fileReader.close();
            
            puntajes[contador] = nombre + "," + puntuacion + "," + nivel + "," + lineas;
            contador++;
            
            // Ordenar por puntuación
            for (int i = 0; i < contador - 1; i++) {
                for (int j = 0; j < contador - i - 1; j++) {
                    int scoreA = Integer.parseInt(puntajes[j].split(",")[1]);
                    int scoreB = Integer.parseInt(puntajes[j + 1].split(",")[1]);
                    
                    if (scoreA < scoreB) {
                        String temp = puntajes[j];
                        puntajes[j] = puntajes[j + 1];
                        puntajes[j + 1] = temp;
                    }
                }
            }
            
            if (contador > 10) contador = 10;
            
            FileWriter writer = new FileWriter(ARCHIVO_PUNTAJES);
            for (int i = 0; i < contador; i++) {
                writer.write(puntajes[i] + "\n");
            }
            writer.close();
            
        } catch (IOException e) {
            System.out.println("Error al guardar el puntaje");
        }
    }
    
    static void actualizarEstadisticas() {
        try {
            FileReader fileReader = new FileReader(ARCHIVO_ESTADISTICAS);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            int partidas = Integer.parseInt(bufferedReader.readLine());
            int maxPuntaje = Integer.parseInt(bufferedReader.readLine());
            int totalLineas = Integer.parseInt(bufferedReader.readLine());
            int maxNivel = Integer.parseInt(bufferedReader.readLine());
            
            bufferedReader.close();
            fileReader.close();
            
            partidas++;
            if (puntuacion > maxPuntaje) maxPuntaje = puntuacion;
            totalLineas += lineasEliminadas;
            if (nivel > maxNivel) maxNivel = nivel;
            
            FileWriter writer = new FileWriter(ARCHIVO_ESTADISTICAS);
            writer.write(partidas + "\n");
            writer.write(maxPuntaje + "\n");
            writer.write(totalLineas + "\n");
            writer.write(maxNivel + "\n");
            writer.close();
            
        } catch (IOException e) {
            System.out.println("Error al actualizar estadísticas");
        }
    }
    
    // ==================== MÉTODOS UTILITARIOS ====================
    
    static void copiarMatriz(int origen[][], int destino[][], int filas, int columnas) {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                destino[i][j] = origen[i][j];
            }
        }
    }
    
    static int leerEntero(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int valor = scanner.nextInt();
                scanner.nextLine();
                
                if (valor >= min && valor <= max) {
                    return valor;
                } else {
                    System.out.print("Por favor, ingrese un número entre " + min + " y " + max + ": ");
                }
            } catch (Exception e) {
                System.out.print("Entrada inválida. Por favor ingrese un número: ");
                scanner.nextLine();
            }
        }
    }
    
    static void limpiarConsola() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}

