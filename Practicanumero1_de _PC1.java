import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PracticaIPC1{
    // Clase del Personaje
    static class Personaje{
        private static int contadorId = 1;
        private int id;
        private String nombre;
        private String arma;
        private ArrayList<String> habilidades;
        private int nivelpoder;
        public Personaje(String nombre, String arma, ArrayList<String> habilidades, int nivelPoder){
            
            this.id = contadorId++;
            this.nombre = nombre;
            this.arma = arma;
            this.habilidades = new ArrayList<>(habilidades);
            this.nivelPoder = nivelPoder;
        }
        public int getid() { return id; }
        public String getNombre() { return nombre; }
        public void setnombre(String nombre) { this.nombre = nombre; }
        public String getarma() { return arma; }
        public void setarma(String arma) { this.arma = arma; }
        public ArrayList<String> gethabilidades() { return new ArrayList<>(habilidades); }
        public void sethabilidades(ArrayList<String> habilidades) { this.habilidades = new ArrayList<>(habilidades); }
        public int getnivelpoder() { return nivelpoder; }
        public void setnivelpoder(int nivelpoder) { this.nivelpoder = nivelPoder; }

        public String toString() {
            return "ID: " + id + "\nNombre: " + nombre + "\nArma: " + arma + 
                   "\nHabilidades: " + String.join(", ", habilidades) + 
                   "\nNivel de poder: " + nivelPoder;
        }
        
        public String toShortString() {
            return "ID: " + id + " | Nombre: " + nombre + " | Nivel: " + nivelPoder;
        }
    
    }
}

     ///clase de pelea
     static class pelea{
        private Personaje personaje1;
        private Personaje personaje2;
        private local.date.time fechahora;

        public Pelea(Personaje personaje1, Personaje personaje2){
            this.personaje1 = personaje1;
            this.personaje2 = personaje2;
            this.fechahora = LocalDateTime.now();
        }

        public Personaje getPersonaje1() { return personaje1; }
        public Personaje getPersonaje2() { return personaje2; }
        public LocalDateTime getFechaHora() { return fechaHora; }

        public string toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return "Pelea entre " + personaje1.getNombre() + " (ID: " + personaje1.getId() + 
                   ") y " + personaje2.getNombre() + " (ID: " + personaje2.getId() + 
                   ") - Fecha: " + fechaHora.format(formatter);
        }
     }

     // Clase Sistema
    static class Sistema {
        private ArrayList<Personaje> personajes;
        private ArrayList<Pelea> peleas;
        private Scanner scanner;
        
        public Sistema() {
            personajes = new ArrayList<>();
            peleas = new ArrayList<>();
            scanner = new Scanner(System.in);
        }
        
        public void mostrarMenu() {
            while (true) {
                System.out.println("\n--- MENÚ PRINCIPAL ---");
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
                
                try {
                    int opcion = Integer.parseInt(scanner.nextLine());
                    
                    switch (opcion) {
                        case 1: agregarPersonaje(); break;
                        case 2: modificarPersonaje(); break;
                        case 3: eliminarPersonaje(); break;
                        case 4: verDatosPersonaje(); break;
                        case 5: verListadoPersonajes(); break;
                        case 6: realizarPelea(); break;
                        case 7: verHistorialPeleas(); break;
                        case 8: mostrarDatosEstudiante(); break;
                        case 9:
                            System.out.println("Saliendo del sistema...");
                            return;
                        default:
                            System.out.println("Opción no válida. Intente nuevamente.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Debe ingresar un número válido.");
                }
            }
        }
       

        private void agregarPersonaje() {
            System.out.println("\n--- AGREGAR PERSONAJE ---");
            
            String nombre;
            while (true) {
                System.out.print("Nombre del personaje: ");
                nombre = scanner.nextLine().trim();
                
                if (nombre.isEmpty()) {
                    System.out.println("Error: El nombre no puede estar vacío.");
                    continue;
                }
                
                if (buscarPersonajePorNombre(nombre) != null) {
                    System.out.println("Error: Ya existe un personaje con ese nombre.");
                } else {
                    break;
                }
            }
        }
