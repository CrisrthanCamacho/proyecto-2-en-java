package usuarios;

public class Usuario {
    private String nombre;
    private String correo;
    private String contrasena;
    private int id;

    public Usuario(String nombre, String correo, String contrasena, int id) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.id = id;
    }

    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getContrasena() { return contrasena; }
    public int getId() { return id; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public void editarPerfil(String n, String c, String p) {
        if (n != null && !n.isEmpty()) this.nombre = n;
        if (c != null && !c.isEmpty()) this.correo = c;
        if (p != null && !p.isEmpty()) this.contrasena = p;
    }

    @Override
    public String toString() {
        return "(ID: " + id + ") | Nombre: " + nombre + " | Correo: " + correo;
    }
}
