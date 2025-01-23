import java.time.LocalDate;

public class HistorialMedico {
    private LocalDate fecha;
    private String diagnostico;
    private String tratamiento;

    public HistorialMedico(LocalDate fecha, String diagnostico, String tratamiento) {
        this.fecha = fecha;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    @Override
    public String toString() {
        return "Fecha: " + fecha + ", Diagnóstico: " + diagnostico + ", Tratamiento: " + tratamiento;
    }
}
