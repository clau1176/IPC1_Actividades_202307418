import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Practicanumero1_de_IPC1 {
    // Constantes para tamaños máximos
    private static final int MAX_PERSONAJES = 100;
    private static final int MAX_PELEAS = 200;
    private static final int MAX_HABILIDADES = 5;
    
    // Datos  estudiante
    private static final String NOMBRE_ESTUDIANTE = "Claudio Arrillaga";
    private static final String CARNET = "202307418";
    private static final String CURSO = "Introducción a la Programación y Computación 1";
    private static final String SECCION = "A";
    
    public static void main(String[] args) {
        //  almacenar datos
        Personaje[] personajes = new Personaje[MAX_PERSONAJES];
        Pelea[] peleas = new Pelea[MAX_PELEAS];
        int numPersonajes = 0;
        int numPeleas = 0;
        
        Scanner scanner = new Scanner(System.in);
        
        // Menú principal
        while (true) {
            System.out.println("\n=== SISTEMA DE GESTIÓN DE PERSONAJES Y PELEAS ===");
            System.out.println("1. Agregar personaje");
            System.out.println("2. Modificar personaje");
            System.out.println("3. Eliminar personaje");
            System.out.println("4. Ver datos de un personaje");
            System.out.println("5. Ver listado de personajes");
            System.out.println("6. Realizar pelea entre personajes");
            System.out.println("7. Ver historial de peleas");
            System.out.println("8. Ver datos del estudiante");
            System.out.println("9. Salir");
            System.out.print("Seleccione una opción: ");
            
            int opcion;
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
                continue;
            }
            
            switch (opcion) {
                case 1:
                    numPersonajes = agregarPersonaje(personajes, numPersonajes, scanner);
                    break;
                case 2:
                    modificarPersonaje(personajes, numPersonajes, scanner);
                    break;
                case 3:
                    numPersonajes = eliminarPersonaje(personajes, numPersonajes, scanner);
                    break;
                case 4:
                    verDatosPersonaje(personajes, numPersonajes, scanner);
                    break;
                case 5:
                    verListadoPersonajes(personajes, numPersonajes);
                    break;
                case 6:
                    numPeleas = realizarPelea(personajes, numPersonajes, peleas, numPeleas, scanner);
                    break;
                case 7:
                    verHistorialPeleas(peleas, numPeleas);
                    break;
                case 8:
                    verDatosEstudiante();
                    break;
                case 9:
                    System.out.println("Saliendo del sistema...");
                    return;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }
    
    // Clase Personaje interna
    static class Personaje {
        int id;
        String nombre;
        String arma;
        String[] habilidades;
        int nivelPoder;
        int numHabilidades;
        
        public Personaje(int id, String nombre, String arma, String[] habilidades, int numHabilidades, int nivelPoder) {
            this.id = id;
            this.nombre = nombre;
            this.arma = arma;
            this.habilidades = habilidades;
            this.numHabilidades = numHabilidades;
            this.nivelPoder = nivelPoder;
        }
    }
    
    // Clase Pelea 
    static class Pelea {
        int idPersonaje1;
        int idPersonaje2;
        String fechaHora;
        
        public Pelea(int idPersonaje1, int idPersonaje2, String fechaHora) {
            this.idPersonaje1 = idPersonaje1;
            this.idPersonaje2 = idPersonaje2;
            this.fechaHora = fechaHora;
        }
    }
    
    //  agregar personaje
    private static int agregarPersonaje(Personaje[] personajes, int numPersonajes, Scanner scanner) {
        if (numPersonajes >= MAX_PERSONAJES) {
            System.out.println("Error: No se pueden agregar más personajes. Límite alcanzado.");
            return numPersonajes;
        }
        
        System.out.println("\n=== AGREGAR PERSONAJE ===");
        
        // Validar nombre 
        String nombre;
        while (true) {
            System.out.print("Nombre del personaje: ");
            nombre = scanner.nextLine().trim();
            
            if (nombre.isEmpty()) {
                System.out.println("Error: El nombre no puede estar vacío.");
                continue;
            }
            
            if (existePersonajeConNombre(personajes, numPersonajes, nombre)) {
                System.out.println("Error: Ya existe un personaje con ese nombre.");
            } else {
                break;
            }
        }
        
        System.out.print("Arma del personaje: ");
        String arma = scanner.nextLine().trim();
        
        // Obtener habilidades
        String[] habilidades = new String[MAX_HABILIDADES];
        int numHabilidades = 0;
        System.out.println("Ingrese las habilidades del personaje (máximo 5, dejar vacío para terminar):");
        for (int i = 0; i < MAX_HABILIDADES; i++) {
            System.out.print("Habilidad " + (i+1) + ": ");
            String habilidad = scanner.nextLine().trim();
            if (habilidad.isEmpty()) {
                break;
            }
            habilidades[i] = habilidad;
            numHabilidades++;
        }
        
        //  nivel de poder (1-100)
        int nivelPoder;
        while (true) {
            System.out.print("Nivel de poder (1-100): ");
            try {
                nivelPoder = Integer.parseInt(scanner.nextLine());
                if (nivelPoder < 1 || nivelPoder > 100) {
                    System.out.println("Error: El nivel de poder debe estar entre 1 y 100.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
            }
        }
        
        // Crear y agregar el personaje
        int id = numPersonajes + 1;
        personajes[numPersonajes] = new Personaje(id, nombre, arma, habilidades, numHabilidades, nivelPoder);
        System.out.println("Personaje agregado exitosamente con ID: " + id);
        
        return numPersonajes + 1;
    }
    
    //  verificar si existe un personaje con cierto nombre
    private static boolean existePersonajeConNombre(Personaje[] personajes, int numPersonajes, String nombre) {
        for (int i = 0; i < numPersonajes; i++) {
            if (personajes[i].nombre.equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }
    
    //  modificar personaje
    private static void modificarPersonaje(Personaje[] personajes, int numPersonajes, Scanner scanner) {
        System.out.println("\n=== MODIFICAR PERSONAJE ===");
        
        if (numPersonajes == 0) {
            System.out.println("No hay personajes registrados para modificar.");
            return;
        }
        
        //  lista de personajes para referencia
        verListadoPersonajes(personajes, numPersonajes);
        
        //  ID del personaje a modificar
        int id;
        while (true) {
            System.out.print("Ingrese el ID del personaje a modificar: ");
            try {
                id = Integer.parseInt(scanner.nextLine());
                if (id < 1 || id > numPersonajes) {
                    System.out.println("Error: ID no válido. Intente nuevamente.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
            }
        }
        
        // Obtener el personaje
        Personaje personaje = personajes[id - 1];
        
        // Mostrar datos actuales
        System.out.println("\nDatos actuales del personaje:");
        System.out.println("1. Nombre: " + personaje.nombre);
        System.out.println("2. Arma: " + personaje.arma);
        System.out.print("3. Habilidades: ");
        for (int i = 0; i < personaje.numHabilidades; i++) {
            System.out.print(personaje.habilidades[i] + (i < personaje.numHabilidades - 1 ? ", " : ""));
        }
        System.out.println("\n4. Nivel de poder: " + personaje.nivelPoder);
        
        // Menú de modificación
        System.out.println("\n¿Qué dato desea modificar?");
        System.out.println("1. Arma");
        System.out.println("2. Habilidades");
        System.out.println("3. Nivel de poder");
        System.out.println("4. Cancelar");
        System.out.print("Seleccione una opción: ");
        
        int opcion;
        try {
            opcion = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Error: Opción no válida.");
            return;
        }
        
        switch (opcion) {
            case 1:
                System.out.print("Nueva arma: ");
                String nuevaArma = scanner.nextLine().trim();
                personaje.arma = nuevaArma;
                System.out.println("Arma actualizada exitosamente.");
                break;
            case 2:
                // Modificar habilidades
                String[] nuevasHabilidades = new String[MAX_HABILIDADES];
                int nuevasHabilidadesCount = 0;
                System.out.println("Ingrese las nuevas habilidades (máximo 5, dejar vacío para terminar):");
                for (int i = 0; i < MAX_HABILIDADES; i++) {
                    System.out.print("Habilidad " + (i+1) + ": ");
                    String habilidad = scanner.nextLine().trim();
                    if (habilidad.isEmpty()) {
                        break;
                    }
                    nuevasHabilidades[i] = habilidad;
                    nuevasHabilidadesCount++;
                }
                personaje.habilidades = nuevasHabilidades;
                personaje.numHabilidades = nuevasHabilidadesCount;
                System.out.println("Habilidades actualizadas exitosamente.");
                break;
            case 3:
                // Modificar nivel de poder
                int nuevoNivel;
                while (true) {
                    System.out.print("Nuevo nivel de poder (1-100): ");
                    try {
                        nuevoNivel = Integer.parseInt(scanner.nextLine());
                        if (nuevoNivel < 1 || nuevoNivel > 100) {
                            System.out.println("Error: El nivel de poder debe estar entre 1 y 100.");
                        } else {
                            personaje.nivelPoder = nuevoNivel;
                            System.out.println("Nivel de poder actualizado exitosamente.");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Por favor ingrese un número válido.");
                    }
                }
                break;
            case 4:
                System.out.println("Modificación cancelada.");
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }
    
    //  eliminar personaje
    private static int eliminarPersonaje(Personaje[] personajes, int numPersonajes, Scanner scanner) {
        System.out.println("\n=== ELIMINAR PERSONAJE ===");
        
        if (numPersonajes == 0) {
            System.out.println("No hay personajes registrados para eliminar.");
            return numPersonajes;
        }
        
        // Mostrar lista de personajes para referencia
        verListadoPersonajes(personajes, numPersonajes);
        
        // Obtener ID del personaje a eliminar
        int id;
        while (true) {
            System.out.print("Ingrese el ID del personaje a eliminar: ");
            try {
                id = Integer.parseInt(scanner.nextLine());
                if (id < 1 || id > numPersonajes) {
                    System.out.println("Error: ID no válido. Intente nuevamente.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
            }
        }
        
        // Confirmar eliminación
        System.out.print("¿Está seguro que desea eliminar al personaje " + personajes[id-1].nombre + "? (S/N): ");
        String confirmacion = scanner.nextLine().trim().toUpperCase();
        
        if (confirmacion.equals("S")) {
            // Desplazar los personajes restantes
            for (int i = id - 1; i < numPersonajes - 1; i++) {
                personajes[i] = personajes[i + 1];
                personajes[i].id = i + 1; // Actualizar ID
            }
            personajes[numPersonajes - 1] = null;
            System.out.println("Personaje eliminado exitosamente.");
            return numPersonajes - 1;
        } else {
            System.out.println("Eliminación cancelada.");
            return numPersonajes;
        }
    }
    
    //  ver datos de un personaje
    private static void verDatosPersonaje(Personaje[] personajes, int numPersonajes, Scanner scanner) {
        System.out.println("\n=== VER DATOS DE PERSONAJE ===");
        
        if (numPersonajes == 0) {
            System.out.println("No hay personajes registrados.");
            return;
        }
        
        // Mostrar lista de personajes para referencia
        verListadoPersonajes(personajes, numPersonajes);
        
        // Obtener ID del personaje a ver
        int id;
        while (true) {
            System.out.print("Ingrese el ID del personaje a ver: ");
            try {
                id = Integer.parseInt(scanner.nextLine());
                if (id < 1 || id > numPersonajes) {
                    System.out.println("Error: ID no válido. Intente nuevamente.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
            }
        }
        
        // Mostrar datos del personaje
        Personaje personaje = personajes[id - 1];
        System.out.println("\n=== DATOS DEL PERSONAJE ===");
        System.out.println("ID: " + personaje.id);
        System.out.println("Nombre: " + personaje.nombre);
        System.out.println("Arma: " + personaje.arma);
        System.out.println("Habilidades:");
        for (int i = 0; i < personaje.numHabilidades; i++) {
            System.out.println("- " + personaje.habilidades[i]);
        }
        System.out.println("Nivel de poder: " + personaje.nivelPoder);
    }
    
    //  ver listado de personajes
    private static void verListadoPersonajes(Personaje[] personajes, int numPersonajes) {
        System.out.println("\n=== LISTADO DE PERSONAJES ===");
        
        if (numPersonajes == 0) {
            System.out.println("No hay personajes registrados.");
            return;
        }
        
        System.out.println("ID\tNombre\t\tNivel de poder");
        System.out.println("--------------------------------");
        for (int i = 0; i < numPersonajes; i++) {
            System.out.printf("%d\t%-15s\t%d\n", personajes[i].id, personajes[i].nombre, personajes[i].nivelPoder);
        }
    }
    
    //  realizar pelea
    private static int realizarPelea(Personaje[] personajes, int numPersonajes, Pelea[] peleas, int numPeleas, Scanner scanner) {
        System.out.println("\n=== REALIZAR PELEA ===");
        
        if (numPersonajes < 2) {
            System.out.println("Error: Se necesitan al menos 2 personajes registrados para realizar una pelea.");
            return numPeleas;
        }
        
        if (numPeleas >= MAX_PELEAS) {
            System.out.println("Error: No se pueden registrar más peleas. Límite alcanzado.");
            return numPeleas;
        }
        
        // Mostrar lista de personajes para referencia
        verListadoPersonajes(personajes, numPersonajes);
        
        // Obtener ID del primer personaje
        int id1;
        while (true) {
            System.out.print("Ingrese el ID del primer personaje: ");
            try {
                id1 = Integer.parseInt(scanner.nextLine());
                if (id1 < 1 || id1 > numPersonajes) {
                    System.out.println("Error: ID no válido. Intente nuevamente.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
            }
        }
        
        // Obtener ID del segundo personaje
        int id2;
        while (true) {
            System.out.print("Ingrese el ID del segundo personaje: ");
            try {
                id2 = Integer.parseInt(scanner.nextLine());
                if (id2 < 1 || id2 > numPersonajes) {
                    System.out.println("Error: ID no válido. Intente nuevamente.");
                } else if (id2 == id1) {
                    System.out.println("Error: No puede pelear un personaje consigo mismo.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
            }
        }
        
        // Obtener los personajes
        Personaje p1 = personajes[id1 - 1];
        Personaje p2 = personajes[id2 - 1];
        
        // Registrar la pelea con fecha y hora actual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaHora = LocalDateTime.now().format(formatter);
        peleas[numPeleas] = new Pelea(p1.id, p2.id, fechaHora);
        
        System.out.println("\n=== RESULTADO DE LA PELEA ===");
        System.out.println("Personaje 1: " + p1.nombre + " (Nivel: " + p1.nivelPoder + ")");
        System.out.println("Personaje 2: " + p2.nombre + " (Nivel: " + p2.nivelPoder + ")");
        System.out.println("Fecha y hora: " + fechaHora);
        
        // Determinar el ganador basado en el nivel de poder
        if (p1.nivelPoder > p2.nivelPoder) {
            System.out.println("¡GANADOR: " + p1.nombre + "!");
        } else if (p2.nivelPoder > p1.nivelPoder) {
            System.out.println("¡GANADOR: " + p2.nombre + "!");
        } else {
            System.out.println("¡La pelea terminó en EMPATE!");
        }
        
        return numPeleas + 1;
    }
    
    //  ver historial de peleas
    private static void verHistorialPeleas(Pelea[] peleas, int numPeleas) {
        System.out.println("\n=== HISTORIAL DE PELEAS ===");
        
        if (numPeleas == 0) {
            System.out.println("No hay peleas registradas.");
            return;
        }
        
        System.out.println("Fecha y hora\t\t\tID Personaje 1\tvs\tID Personaje 2");
        System.out.println("------------------------------------------------------------");
        for (int i = 0; i < numPeleas; i++) {
            System.out.printf("%-20s\t%-15d\tvs\t%-15d\n", 
                peleas[i].fechaHora, 
                peleas[i].idPersonaje1, 
                peleas[i].idPersonaje2);
        }
    }
    
    //  ver datos del estudiante
    private static void verDatosEstudiante() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaActual = LocalDateTime.now().format(formatter);
        
        System.out.println("\n=== DATOS DEL ESTUDIANTE ===");
        System.out.println("=================================");
        System.out.println("Nombre: " + NOMBRE_ESTUDIANTE);
        System.out.println("Carnet: " + CARNET);
        System.out.println("Curso: " + CURSO);
        System.out.println("Sección: " + SECCION);
        System.out.println("Fecha: " + fechaActual);
        System.out.println("=================================");
    }
}
