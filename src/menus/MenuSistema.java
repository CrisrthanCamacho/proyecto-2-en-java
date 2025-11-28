package menus;

import usuarios.*;
import pedidos.*;
import confeccionistas.*;

import java.util.*;

public class MenuSistema {

    private final Scanner scanner = new Scanner(System.in);

    private final UsuariosManager usuariosManager = new UsuariosManager(scanner);
    private final GestionConfeccionistas gestionConfeccionistas = new GestionConfeccionistas(scanner);
    private final GestorPedidos gestorPedidos = new GestorPedidos(scanner);

    public void mainLoop() {
    while (true) {
        System.out.println("\n\n===== SISTEMA DE CONFECCIONES =====");
        System.out.println("1. Iniciar sesión");
        System.out.println("2. Registrarse");
        System.out.println("3. Salir");
        System.out.print("Opción: ");

        switch (scanner.nextLine().trim()) {
            case "1" -> menuInicioSesion();
            case "2" -> {
                System.out.println("\n--- Registrarse como ---");
                System.out.println("1. Cliente");
                System.out.println("2. Administrador");
                System.out.print("Opción: ");

                String op = scanner.nextLine().trim();

                if (op.equals("1")) usuariosManager.registrarCliente();
                else if (op.equals("2")) usuariosManager.registrarAdmin();
                else System.out.println("Opción inválida.");
            }
            case "3" -> { 
                System.out.println("Saliendo...");
                return; 
            }
            default -> System.out.println("Opción inválida.");
        }
    }
}

    // ============================================================
    // INICIO DE SESIÓN
    // ============================================================
    private void menuInicioSesion() {

        System.out.println("\n--- Iniciar sesión como ---");
        System.out.println("1. Cliente");
        System.out.println("2. Administrador");
        System.out.print("Opción: ");

        switch (scanner.nextLine().trim()) {
            case "1" -> {
                Usuario u = usuariosManager.iniciarSesion(usuariosManager.clientes);
                if (u != null) menuCliente(u);
            }
            case "2" -> {
                Usuario u = usuariosManager.iniciarSesion(usuariosManager.administradores);
                if (u != null) menuAdmin(u);
            }
            default -> System.out.println("Opción inválida.");
        }
    }

    // ============================================================
    // MENÚ CLIENTE
    // ============================================================
    private void menuCliente(Usuario cliente) {
        while (true) {
            System.out.println("\n===== MENÚ CLIENTE =====");
            System.out.println("1. Ver perfil");
            System.out.println("2. Editar perfil");
            System.out.println("3. Registrar pedido");
            System.out.println("4. Lista de pedidos");
            System.out.println("5. Pagar pedido");
            System.out.println("6. Ver anticipos");
            System.out.println("7. Cancelar pedido");
            System.out.println("8. Cerrar sesión");
            System.out.print("Opción: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> usuariosManager.verPerfil(cliente);
                case "2" -> usuariosManager.editarPerfil(cliente);
                case "3" -> gestorPedidos.agregarPedido(cliente);
                case "4" -> gestorPedidos.mostrarPedidosCliente(cliente);
                case "5" -> gestorPedidos.pagarPedidoCliente(cliente);
                case "6" -> gestorPedidos.mostrarAnticiposPedido();
                case "7" -> gestorPedidos.cancelarPedidoCliente(cliente);
                case "8" -> { return; }
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    // ============================================================
    // MENÚ ADMINISTRADOR
    // ============================================================
    private void menuAdmin(Usuario admin) {
        while (true) {

            System.out.println("\n===== MENÚ ADMINISTRADOR =====");
            System.out.println("1. Gestión de Confeccionistas");
            System.out.println("2. Gestión de Pedidos");
            System.out.println("3. Ver Clientes");
            System.out.println("4. Editar perfil");
            System.out.println("5. Cerrar sesión");
            System.out.print("Opción: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> menuGestionConfeccionistas();
                case "2" -> menuGestionPedidos();
                case "3" -> usuariosManager.verClientes();
                case "4" -> usuariosManager.editarPerfil(admin);
                case "5" -> { return; }
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    // ============================================================
    // MENÚ GESTIÓN DE CONFECCIONISTAS
    // ============================================================
    private void menuGestionConfeccionistas() {
        while (true) {
            System.out.println("\n===== GESTIÓN DE CONFECCIONISTAS =====");
            System.out.println("1. Registrar confeccionista");
            System.out.println("2. Eliminar confeccionista");
            System.out.println("3. Editar confeccionista");
            System.out.println("4. Ver confección por confeccionista");
            System.out.println("5. Ver lista");
            System.out.println("6. Volver");
            System.out.print("Opción: ");

            switch (scanner.nextLine().trim()) {

                case "1" -> gestionConfeccionistas.registrarEmpleado();

                case "2" -> gestionConfeccionistas.eliminarEmpleadoInteractive();

                case "3" -> gestionConfeccionistas.editarEmpleado();

                case "4" -> {
                    gestionConfeccionistas.verEmpleados();
                    System.out.print("ID del confeccionista: ");
                    int id = pedirInt();

                    var emp = gestionConfeccionistas.buscarEmpleadoPorId(id);
                    if (emp == null) {
                        System.out.println("No existe.");
                        break;
                    }

                    System.out.println("\n--- PEDIDOS ASIGNADOS A " + emp.getNombre() + " ---");
                    gestorPedidos.getPedidos().stream()
                            .filter(p -> emp.getNombre().equals(p.getConfeccionista()))
                            .forEach(System.out::println);
                }

                case "5" -> gestionConfeccionistas.verEmpleados();

                case "6" -> { return; }

                default -> System.out.println("Opción inválida.");
            }
        }
    }

    // ============================================================
    // MENÚ GESTIÓN DE PEDIDOS
    // ============================================================
    private void menuGestionPedidos() {

        GestorPedidos.UsuariosManagerRef ref =
                new GestorPedidos.UsuariosManagerRef(usuariosManager.clientes);

        while (true) {
            System.out.println("\n===== GESTIÓN DE PEDIDOS =====");
            System.out.println("1. Ver pedidos");
            System.out.println("2. Asignar pedido");
            System.out.println("3. Eliminar pedido");
            System.out.println("4. Editar pedido");
            System.out.println("5. Volver");
            System.out.print("Opción: ");

            switch (scanner.nextLine().trim()) {

                case "1" -> gestorPedidos.verPedidosPorOpciones(ref, gestionConfeccionistas);

                case "2" -> gestorPedidos.asignarPedido(gestionConfeccionistas);

                case "3" -> gestorPedidos.eliminarPedidoInteractive();

                case "4" -> menuEditarPedido();

                case "5" -> { return; }

                default -> System.out.println("Opción inválida.");
            }
        }
    }

    // ============================================================
    // SUB MENÚ EDITAR PEDIDO
    // ============================================================
    private void menuEditarPedido() {

        System.out.println("--- PEDIDOS ---");
        gestorPedidos.getPedidos().forEach(p ->
                System.out.printf("ID: %d | Estado: %s%n", p.getIdPedido(), p.getEstado())
        );

        System.out.print("ID del pedido: ");
        int id = pedirInt();

        Pedido p = gestorPedidos.getPedidos().stream()
                .filter(x -> x.getIdPedido() == id)
                .findFirst()
                .orElse(null);

        if (p == null) {
            System.out.println("ID inválido.");
            return;
        }

        while (true) {
            System.out.println("\n--- EDITAR PEDIDO (ID: " + p.getIdPedido() + ") ---");
            System.out.println("1. Editar tela");
            System.out.println("2. Editar color");
            System.out.println("3. Editar detalles");
            System.out.println("4. Editar precio unitario");
            System.out.println("5. Editar estado");
            System.out.println("6. Volver");
            System.out.print("Opción: ");

            switch (scanner.nextLine().trim()) {

                case "1" -> {
                    System.out.print("Nueva tela: ");
                    gestorPedidos.getEditor().editarTela(p, scanner.nextLine().trim());
                }

                case "2" -> {
                    System.out.print("Nuevo color: ");
                    gestorPedidos.getEditor().editarColor(p, scanner.nextLine().trim());
                }

                case "3" -> {
                    System.out.print("Nuevos detalles: ");
                    gestorPedidos.getEditor().editarDetalles(p, scanner.nextLine().trim());
                }

                case "4" -> {
                    System.out.print("Nuevo precio unitario: ");
                    try {
                        double precio = Double.parseDouble(scanner.nextLine());
                        gestorPedidos.getEditor().editarPrecio(p, precio);
                    } catch (Exception e) {
                        System.out.println("Número inválido.");
                    }
                }

                case "5" -> {
                    System.out.print("Nuevo estado: ");
                    gestorPedidos.getEditor().editarEstado(p, scanner.nextLine().trim());
                }

                case "6" -> { return; }

                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private int pedirInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.print("Valor inválido. Intente de nuevo: ");
            }
        }
    }
}
