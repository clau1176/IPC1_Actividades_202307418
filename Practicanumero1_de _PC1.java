import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PracticaIPC1{
    // Clase Personaje
    static class Personaje{
        private static int contadorId = 1;
        private int id;
        private String nombre;
        private String arma;
        private ArrayList<String> habilidades;
        private int nivelPoder;
        public Personaje(String nombre, String arma, ArrayList<String> habilidades, int nivelPoder){
            
            this.id = contadorId++;
            this.nombre = nombre;
            this.arma = arma;
            this.habilidades = new ArrayList<>(habilidades);
            this.nivelPoder = nivelPoder;
        }

    }
}


