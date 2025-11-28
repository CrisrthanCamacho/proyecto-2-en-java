package pedidos;

import usuarios.Usuario;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Pedido {
    private static int contador = 101;

    private int idPedido;
    private Usuario cliente;
    private Map<String,Integer> tallas;
    private String tela;
    private String color;
    private String imagen;
    private int cantidadTotal;
    private String detalles;
    private String estado;
    private Double precioUnitario;
    private double montoPagar;
    private LocalDateTime fecha;
    private String confeccionista;
    private List<Anticipo> anticipos = new ArrayList<>();

    public Pedido(Usuario cliente, Map<String,Integer> tallas, String tela, String imagen, 
                  String color, int cantidad, String detalles) {

        this.idPedido = contador++;
        this.cliente = cliente;

        this.tallas = new LinkedHashMap<>();
        this.tallas.put("S", tallas.get("S"));
        this.tallas.put("M", tallas.get("M"));
        this.tallas.put("L", tallas.get("L"));
        this.tallas.put("XL", tallas.get("XL"));

        this.tela = tela;
        this.color = color;
        this.imagen = imagen;
        this.cantidadTotal = cantidad;
        this.detalles = detalles;
        this.estado = "pendiente";
        this.precioUnitario = null;
        this.montoPagar = 0;
        this.fecha = LocalDateTime.now();
        this.confeccionista = null;
    }

    public int getIdPedido() { return idPedido; }
    public Usuario getCliente() { return cliente; }
    public Map<String,Integer> getTallas() { return tallas; }
    public String getTela() { return tela; }
    public String getColor() { return color; }
    public String getImagen() { return imagen; }
    public String getEstado() { return estado; }
    public int getCantidadTotal() { return cantidadTotal; }
    public double getMontoPagar() { return montoPagar; }
    public Double getPrecioUnitario() { return precioUnitario; }
    public List<Anticipo> getAnticipos() { return anticipos; }
    public String getConfeccionista() { return confeccionista; }
    public LocalDateTime getFecha() { return fecha; }

    public void setTela(String v) { this.tela = v; }
    public void setColor(String v) { this.color = v; }
    public void setDetalles(String v) { this.detalles = v; }
    public void setEstado(String v) { this.estado = v; }
    public void setPrecioUnitario(double v) { this.precioUnitario = v; this.montoPagar = cantidadTotal * v; }
    public void setConfeccionista(String v) { this.confeccionista = v; }

    public void agregarAnticipo(Anticipo a) { anticipos.add(a); }

    public double totalAnticipos() {
        return anticipos.stream().mapToDouble(Anticipo::getMonto).sum();
    }

    public double restantePorPagar() {
        return montoPagar - totalAnticipos();
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "\nID Pedido: " + idPedido +
               "\nFecha: " + fecha.format(fmt) +
               "\nCliente: " + cliente.getNombre() + " (ID: " + cliente.getId() + ")" +
               "\nTallas: S=" + tallas.get("S") + " M=" + tallas.get("M") +
               " L=" + tallas.get("L") + " XL=" + tallas.get("XL") +
               "\nTela: " + tela +
               "\nColor: " + color +
               "\nImagen: " + imagen +
               "\nCantidad total: " + cantidadTotal +
               "\nEstado: " + estado +
               "\nPrecio unitario: " + precioUnitario +
               "\nMonto total: " + montoPagar +
               "\nAnticipos: " + totalAnticipos() +
               "\nConfeccionista: " + (confeccionista == null ? "No asignado" : confeccionista) +
               "\n";
    }
}
