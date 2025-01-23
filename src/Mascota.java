import java.util.ArrayList;
import java.util.List;

public class Mascota {
    private String nombre;
    private String especie;
    private List<HistorialMedico> historialMedico;

    public Mascota(String nombre, String especie) {
        this.nombre = nombre;
        this.especie = especie;
        this.historialMedico = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public List<HistorialMedico> getHistorialMedico() {
        return historialMedico;
    }

    public void agregarHistorial(HistorialMedico historial) {
        historialMedico.add(historial);
    }
    public String toString() {
        return nombre; // Aquí "nombre" es el atributo que contiene el nombre de la mascota
    }
}

