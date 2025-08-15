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
        

    }
}



