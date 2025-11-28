package pedidos;

import java.time.LocalDateTime;

public class Anticipo {
    private double monto;
    private LocalDateTime fecha;
    private String metodo;

    public Anticipo(double monto, LocalDateTime fecha, String metodo) {
        this.monto = monto;
        this.fecha = fecha;
        this.metodo = metodo;
    }

    public double getMonto() { return monto; }
    public LocalDateTime getFecha() { return fecha; }
    public String getMetodo() { return metodo; }
}
