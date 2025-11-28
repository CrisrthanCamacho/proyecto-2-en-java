package pedidos;

public class EditorPedido {

    public void editarTela(Pedido p, String t) { p.setTela(t); }

    public void editarColor(Pedido p, String c) { p.setColor(c); }

    public void editarDetalles(Pedido p, String d) { p.setDetalles(d); }

    public void editarEstado(Pedido p, String e) { p.setEstado(e); }

    public void editarPrecio(Pedido p, double precio) { p.setPrecioUnitario(precio); }

}
