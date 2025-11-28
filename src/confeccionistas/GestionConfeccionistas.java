package confeccionistas;

import java.util.*;

public class GestionConfeccionistas {

    public static class Empleado {
        private String nombre;
        private int id;

        public Empleado(String n, int id) {
            this.nombre = n;
            this.id = id;
        }

        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public void setNombre(String n) { this.nombre = n; }

        @Override
        public String toString() { return "(ID: " + id + ") | " + nombre; }
    }

    private final List<Empleado> empleados = new ArrayList<>();
    private int contadorId = 1;
    private final Scanner scanner;

    public GestionConfeccionistas(Scanner scanner) {
        this.scanner = scanner;
    }

    public Empleado registrarEmpleado() {
        System.out.print("Nombre del confeccionista: ");
        String n = scanner.nextLine().trim();
        Empleado e = new Empleado(n, contadorId++);
        empleados.add(e);
        System.out.println("Registrado (ID: " + e.getId() + ")");
        return e;
    }

    public void eliminarEmpleadoInteractive() {
        verEmpleados();
        System.out.print("ID a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            empleados.removeIf(e -> e.getId() == id);
            System.out.println("Eliminado.");
        } catch (Exception e) { System.out.println("ID inválido."); }
    }

    public void editarEmpleado() {
        verEmpleados();
        System.out.print("ID a editar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            for (Empleado e : empleados) {
                if (e.getId() == id) {
                    System.out.print("Nuevo nombre: ");
                    e.setNombre(scanner.nextLine().trim());
                    System.out.println("Actualizado.");
                    return;
                }
            }
        } catch (Exception e) { System.out.println("ID inválido."); }
    }

    public void verEmpleados() {
        System.out.println("\n--- Confeccionistas ---");
        if (empleados.isEmpty()) { System.out.println("No hay."); return; }
        empleados.forEach(e -> System.out.println(e));
    }

    public Empleado buscarEmpleadoPorId(int id) {
        return empleados.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }

    public List<Empleado> getEmpleados() { return empleados; }
}
