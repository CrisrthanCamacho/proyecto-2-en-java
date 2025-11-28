package pedidos;

import usuarios.Usuario;
import confeccionistas.GestionConfeccionistas;
import confeccionistas.GestionConfeccionistas.Empleado;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GestorPedidos {

    private final List<Pedido> pedidos = new ArrayList<>();
    private final EditorPedido editor = new EditorPedido();
    private final Scanner scanner;

    public GestorPedidos(Scanner scanner) {
        this.scanner = scanner;
    }

    // ============================================================
    //  REGISTRAR PEDIDO (CLIENTE)
    // ============================================================
    public void agregarPedido(Usuario cliente) {
        System.out.println("\n--- REGISTRAR PEDIDO ---");

        int cantidadTotal = pedirEntero("Cantidad total de prendas: ");

        Map<String, Integer> tallas;
        while (true) {
            tallas = new LinkedHashMap<>();
            System.out.println("\nIngrese cantidades por talla:");
            tallas.put("S", pedirEntero("Talla S: "));
            tallas.put("M", pedirEntero("Talla M: "));
            tallas.put("L", pedirEntero("Talla L: "));
            tallas.put("XL", pedirEntero("Talla XL: "));

            int suma = tallas.values().stream().mapToInt(Integer::intValue).sum();
            if (suma != cantidadTotal) {
                System.out.println("\n❌ La suma de tallas (" + suma + ") no coincide con la cantidad total (" + cantidadTotal + ").");
                System.out.println("Vuelve a ingresarlas.\n");
            } else break;
        }

        String tela = seleccionarTela();

        System.out.print("Imagen o referencia: ");
        String imagen = scanner.nextLine().trim();

        System.out.print("Color de la tela: ");
        String colorTela = scanner.nextLine().trim();

        System.out.print("Detalles extras: ");
        String detalles = scanner.nextLine().trim();

        Pedido nuevo = new Pedido(cliente, tallas, tela, imagen, colorTela, cantidadTotal, detalles);
        pedidos.add(nuevo);

        System.out.println("\n✔ Pedido registrado correctamente con ID: " + nuevo.getIdPedido());
    }

    private int pedirEntero(String msg) {
        while (true) {
            System.out.print(msg);
            String in = scanner.nextLine();
            try {
                int n = Integer.parseInt(in);
                if (n < 0) throw new Exception();
                return n;
            } catch (Exception e) {
                System.out.println("Valor inválido.");
            }
        }
    }

    private String seleccionarTela() {
        while (true) {
            System.out.println("\nSeleccione la tela:");
            System.out.println("1. Algodón");
            System.out.println("2. Poliéster");
            System.out.println("3. Entretela");
            System.out.println("4. Jersey");
            System.out.print("Opción: ");

            switch (scanner.nextLine().trim()) {
                case "1": return "Algodón";
                case "2": return "Poliéster";
                case "3": return "Entretela";
                case "4": return "Jersey";
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    // ============================================================
    //  LISTA DE PEDIDOS DEL CLIENTE
    // ============================================================
    public void mostrarPedidosCliente(Usuario cliente) {

        List<Pedido> lista = pedidosPorCliente(cliente.getId());

        if (lista.isEmpty()) {
            System.out.println("No tienes pedidos.");
            return;
        }

        System.out.println("\n--- TUS PEDIDOS ---");
        mostrarResumenPedidos(lista);

        while (true) {
            System.out.print("\nIngrese ID de pedido para ver detalle o 's' para salir: ");
            String op = scanner.nextLine().trim();

            if (op.equalsIgnoreCase("s")) return;

            try {
                int id = Integer.parseInt(op);
                Pedido p = lista.stream()
                        .filter(x -> x.getIdPedido() == id)
                        .findFirst()
                        .orElse(null);

                if (p == null) System.out.println("ID no válido.");
                else System.out.println(p);

            } catch (Exception e) {
                System.out.println("Entrada inválida.");
            }
        }
    }

    private List<Pedido> pedidosPorCliente(int idCliente) {
        List<Pedido> lista = new ArrayList<>();
        for (Pedido p : pedidos)
            if (p.getCliente().getId() == idCliente)
                lista.add(p);
        return lista;
    }

    private void mostrarResumenPedidos(List<Pedido> lista) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Pedido p : lista) {
            System.out.printf("ID: %d | Fecha: %s | Total: %.2f | Estado: %s%n",
                    p.getIdPedido(),
                    p.getFecha().format(fmt),
                    p.getMontoPagar(),
                    p.getEstado());
        }
    }

    // ============================================================
    //  PAGAR PEDIDO (CLIENTE)
    // ============================================================
    public void pagarPedidoCliente(Usuario cliente) {
        List<Pedido> disponibles = new ArrayList<>();

        for (Pedido p : pedidos) {
            boolean ok = p.getCliente().getId() == cliente.getId()
                    && p.getMontoPagar() > 0
                    && !p.getEstado().equalsIgnoreCase("cancelado");

            if (ok) disponibles.add(p);
        }

        if (disponibles.isEmpty()) {
            System.out.println("No hay pedidos disponibles para pagar.");
            return;
        }

        System.out.println("\n--- PAGAR PEDIDO ---");
        disponibles.forEach(p -> {
            System.out.printf("ID: %d | Total: %.2f | Restante: %.2f%n",
                    p.getIdPedido(), p.getMontoPagar(), p.restantePorPagar());
        });

        System.out.print("ID del pedido a pagar: ");
        int id = pedirEntero("");

        Pedido p = disponibles.stream()
                .filter(x -> x.getIdPedido() == id)
                .findFirst()
                .orElse(null);

        if (p == null) {
            System.out.println("Pedido no válido.");
            return;
        }

        System.out.println("Método de pago:");
        System.out.println("1. Nequi");
        System.out.println("2. Bancolombia");
        System.out.print("Opción: ");

        String metodo = switch (scanner.nextLine().trim()) {
            case "1" -> "Nequi";
            case "2" -> "Bancolombia";
            default -> null;
        };

        if (metodo == null) {
            System.out.println("Método inválido.");
            return;
        }

        System.out.print("Monto del anticipo: ");
        double monto;
        try { monto = Double.parseDouble(scanner.nextLine()); }
        catch (Exception e) { System.out.println("Monto inválido."); return; }

        p.agregarAnticipo(new Anticipo(monto, LocalDateTime.now(), metodo));
        System.out.println("Anticipo registrado.");

        if (p.totalAnticipos() >= p.getMontoPagar()) {
            p.setEstado("Pagado");
            System.out.println("Pedido totalmente pagado.");
        }
    }

    // ============================================================
    //  CANCELAR PEDIDO (CLIENTE)
    // ============================================================
    public void cancelarPedidoCliente(Usuario cliente) {
        List<Pedido> lista = pedidosPorCliente(cliente.getId());

        if (lista.isEmpty()) {
            System.out.println("No tienes pedidos.");
            return;
        }

        System.out.println("--- TUS PEDIDOS ---");
        lista.forEach(p -> System.out.printf("ID: %d | Total: %.2f | Estado: %s%n",
                p.getIdPedido(), p.getMontoPagar(), p.getEstado()));

        System.out.print("ID del pedido a cancelar: ");
        int id = pedirEntero("");

        Pedido p = lista.stream()
                .filter(x -> x.getIdPedido() == id)
                .findFirst()
                .orElse(null);

        if (p == null) {
            System.out.println("No existe ese pedido.");
            return;
        }

        p.setEstado("Cancelado");
        System.out.println("Pedido cancelado exitosamente.");
    }

    // ============================================================
    //  ANTICIPOS (ADMIN Y CLIENTE)
    // ============================================================
    public void mostrarAnticiposPedido() {
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos.");
            return;
        }

        System.out.println("\n--- PEDIDOS ---");
        for (Pedido p : pedidos) {
            System.out.printf("ID: %d | Total: %.2f%n", p.getIdPedido(), p.getMontoPagar());
        }

        System.out.print("ID del pedido: ");
        int id = pedirEntero("");

        Pedido p = buscarPedido(id);
        if (p == null) {
            System.out.println("Pedido no existe.");
            return;
        }

        if (p.getAnticipos().isEmpty()) {
            System.out.println("No hay anticipos.");
            return;
        }

        System.out.println("\n--- ANTICIPOS DEL PEDIDO " + id + " ---");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Anticipo a : p.getAnticipos()) {
            System.out.println("Fecha: " + a.getFecha().format(fmt) +
                               " | Monto: " + a.getMonto() +
                               " | Método: " + a.getMetodo());
        }

        System.out.println("Total pagado: " + p.totalAnticipos());
        System.out.println("Restante: " + p.restantePorPagar());
    }

    // ============================================================
    //  VER PEDIDOS (ADMIN)
    // ============================================================
    public void verPedidosPorOpciones(UsuariosManagerRef ref, GestionConfeccionistas gestion) {
        while (true) {
            System.out.println("\n--- VER PEDIDOS ---");
            System.out.println("1. Por Cliente");
            System.out.println("2. Por Confeccionista");
            System.out.println("3. Todos");
            System.out.println("4. Salir");
            System.out.print("Opción: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> verPedidosPorCliente(ref);
                case "2" -> verPedidosPorConfeccionista(gestion);
                case "3" -> pedidos.forEach(p -> System.out.println(p));
                case "4" -> { return; }
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private void verPedidosPorCliente(UsuariosManagerRef ref) {
        ref.mostrarClientes();
        System.out.print("ID del cliente: ");
        int id = pedirEntero("");

        pedidos.stream()
                .filter(p -> p.getCliente().getId() == id)
                .forEach(System.out::println);
    }

    private void verPedidosPorConfeccionista(GestionConfeccionistas gestion) {
        gestion.verEmpleados();

        System.out.print("ID del confeccionista: ");
        int id = pedirEntero("");

        Empleado e = gestion.buscarEmpleadoPorId(id);
        if (e == null) { System.out.println("No existe."); return; }

        pedidos.stream()
                .filter(p -> e.getNombre().equals(p.getConfeccionista()))
                .forEach(System.out::println);
    }

    // ============================================================
    //  ASIGNAR PEDIDO → ADMIN
    // ============================================================
    public void asignarPedido(GestionConfeccionistas gestion) {
        List<Pedido> disponibles = pedidos.stream()
                .filter(p -> p.getConfeccionista() == null)
                .toList();

        if (disponibles.isEmpty()) {
            System.out.println("No hay pedidos disponibles.");
            return;
        }

        System.out.println("\n--- PEDIDOS DISPONIBLES ---");
        disponibles.forEach(p ->
                System.out.printf("ID: %d | Estado: %s%n", p.getIdPedido(), p.getEstado())
        );

        System.out.print("ID del pedido: ");
        int idP = pedirEntero("");

        Pedido pedido = buscarPedido(idP);
        if (pedido == null || pedido.getConfeccionista() != null) {
            System.out.println("Pedido no válido.");
            return;
        }

        System.out.println("\n--- CONFECCIONISTAS DISPONIBLES ---");
        gestion.getEmpleados().forEach(System.out::println);

        System.out.print("ID del confeccionista: ");
        int idC = pedirEntero("");

        Empleado emp = gestion.buscarEmpleadoPorId(idC);
        if (emp == null) { System.out.println("No existe."); return; }

        pedido.setConfeccionista(emp.getNombre());
        System.out.println("Pedido asignado correctamente.");
    }

    // ============================================================
    //  ELIMINAR PEDIDO (ADMIN)
    // ============================================================
    public void eliminarPedidoInteractive() {
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos.");
            return;
        }

        System.out.println("\n--- PEDIDOS ---");
        pedidos.forEach(p ->
                System.out.printf("ID: %d | Estado: %s%n", p.getIdPedido(), p.getEstado())
        );

        System.out.print("ID a eliminar: ");
        int id = pedirEntero("");

        pedidos.removeIf(p -> p.getIdPedido() == id);
        System.out.println("Pedido eliminado.");
    }

    // ============================================================
    //  BUSCAR PEDIDO POR ID
    // ============================================================
    private Pedido buscarPedido(int id) {
        for (Pedido p : pedidos) if (p.getIdPedido() == id) return p;
        return null;
    }

    // ============================================================
    //  EDITAR PEDIDO (MenuSistema llamará este método)
    // ============================================================
    public EditorPedido getEditor() { return editor; }

    public List<Pedido> getPedidos() { return pedidos; }

    // ============================================================
    //  WRAPPER PARA ADMIN
    // ============================================================
    public static class UsuariosManagerRef {
        private final List<Usuario> clientes;

        public UsuariosManagerRef(List<Usuario> lista) { this.clientes = lista; }

        public void mostrarClientes() {
            System.out.println("\n--- CLIENTES ---");
            if (clientes.isEmpty()) {
                System.out.println("No hay clientes.");
                return;
            }
            clientes.forEach(c -> System.out.println("(ID: " + c.getId() + ") | " + c.getNombre()));
        }
    }
}
