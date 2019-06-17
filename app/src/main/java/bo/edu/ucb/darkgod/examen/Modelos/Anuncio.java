package bo.edu.ucb.darkgod.examen.Modelos;

public class Anuncio {
    private int id;
    private String titulo;
    private String descripcion;
    private String fecha;
    private String hora;
    private String created_at;
    private String nombres;
    private String apellidos;
    private String correo;

    public Anuncio(int id, String titulo, String descripcion, String fecha, String created_at) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = "";
        this.created_at = created_at;
        this.nombres="";
        this.apellidos="";
        this.correo="";
    }

    public Anuncio(int id, String titulo, String descripcion, String fecha, String hora,
                   String created_at, String nombres, String apellidos, String correo) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.created_at = created_at;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
    }
    public Anuncio() {
        this.id = 0;
        this.titulo = "";
        this.descripcion = "";
        this.fecha = "";
        this.hora = "";
        this.created_at = "";
        this.nombres="";
        this.apellidos="";
        this.correo="";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
