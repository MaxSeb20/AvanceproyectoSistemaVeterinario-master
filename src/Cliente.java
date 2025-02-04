import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String nombre;
    private String telefono;
    private String direccion;
    private List<Mascota> mascotas;

    public Cliente(String nombre, String telefono, String direccion) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.mascotas = new ArrayList<>();
    }
    public void agregarHistorial(HistorialMedico registro) {
        historialMedico.add(registro);
    }

    public List<HistorialMedico> getHistorialMedico() {
        return historialMedico;
    }
    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public List<Mascota> getMascotas() {
        return mascotas;
    }

    public void agregarMascota(Mascota mascota) {
        mascotas.add(mascota);
    }

    private List<HistorialMedico> historialMedico = new ArrayList<>();




    @Override
    public String toString() {
        return nombre;
    }
}
