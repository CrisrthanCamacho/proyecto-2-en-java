package usuarios;

import java.util.*;

public class UsuariosManager {
    private int contadorAdmin = 1;
    private int contadorCliente = 1;

    public final List<Usuario> administradores = new ArrayList<>();
    public final List<Usuario> clientes = new ArrayList<>();

    private final Scanner scanner;

    public UsuariosManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public Usuario registrarCliente() {
        System.out.println("\n--- Registrar Cliente ---");
        System.out.print("Nombre: ");
        String n = scanner.nextLine().trim();
        System.out.print("Correo: ");
        String c = scanner.nextLine().trim();
        System.out.print("Contraseña: ");
        String p = scanner.nextLine().trim();

        Usuario u = new Usuario(n, c, p, contadorCliente++);
        clientes.add(u);
        System.out.println("Cliente registrado (ID: " + u.getId() + ")");
        return u;
    }

    public Usuario registrarAdmin() {
        System.out.println("\n--- Registrar Administrador ---");
        System.out.print("Nombre: ");
        String n = scanner.nextLine().trim();
        System.out.print("Correo: ");
        String c = scanner.nextLine().trim();
        System.out.print("Contraseña: ");
        String p = scanner.nextLine().trim();

        Usuario u = new Usuario(n, c, p, contadorAdmin++);
        administradores.add(u);
        System.out.println("Administrador registrado (ID: " + u.getId() + ")");
        return u;
    }

    public Usuario iniciarSesion(List<Usuario> lista) {
        System.out.println("\n--- Iniciar Sesión ---");
        System.out.print("Nombre: ");
        String n = scanner.nextLine().trim();
        System.out.print("Contraseña: ");
        String p = scanner.nextLine().trim();

        for (Usuario u : lista) {
            if (u.getNombre().equals(n) && u.getContrasena().equals(p)) {
                System.out.println("\nBienvenido " + u.getNombre() + " (ID: " + u.getId() + ")");
                return u;
            }
        }
        System.out.println("Usuario o contraseña incorrectos.");
        return null;
    }

    public void verClientes() {
        System.out.println("\n--- Lista de Clientes ---");
        if (clientes.isEmpty()) { System.out.println("No hay clientes."); return; }
        clientes.forEach(c -> System.out.println(c));
    }

    public void verPerfil(Usuario u) {
        System.out.println("\n--- Mi Perfil ---");
        System.out.println("ID: (ID: " + u.getId() + ")");
        System.out.println("Nombre: " + u.getNombre());
        System.out.println("Correo: " + u.getCorreo());
    }

    public void editarPerfil(Usuario u) {
        System.out.println("\n--- Editar Perfil ---");
        System.out.print("Nuevo nombre (enter = no cambiar): ");
        String n = scanner.nextLine().trim();
        System.out.print("Nuevo correo (enter = no cambiar): ");
        String c = scanner.nextLine().trim();
        System.out.print("Nueva contraseña (enter = no cambiar): ");
        String p = scanner.nextLine().trim();

        u.editarPerfil(
            n.isEmpty() ? null : n,
            c.isEmpty() ? null : c,
            p.isEmpty() ? null : p
        );
        System.out.println("Perfil actualizado.");
    }
}
