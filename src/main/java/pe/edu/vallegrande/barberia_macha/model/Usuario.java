package pe.edu.vallegrande.barberia_macha.model;

import jakarta.persistence.*;
@Table(name = "usuario", schema = "OMARRIVERAFELIX")
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    @Column(name = "TIPODEDOCUMENTO")
    //@Pattern(regexp = "dni|cne", message = "Tipo de documento inválido")
    private String tipoDeDocumento;

    @Column(name = "NUMERODEDOCUMENTO")
    //@Pattern(regexp = "^[0-9]{8,20}$", message = "Número de documento inválido")
    private String numeroDeDocumento;

    @Column(name = "NOMBRE")
    //@NotBlank
    private String nombre;

    @Column(name = "APELLIDO")
    //@NotBlank
    private String apellido;

    @Column(name = "CELULAR")
    //@Pattern(regexp = "^9[0-9]{8}$", message = "Celular inválido")
    private String celular;

    @Column(name = "EMAIL")
    //@Email
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ROL")
    //@Pattern(regexp = "cliente|admin|barbero", message = "Rol inválido")
    private String rol;

    @Column(name = "ACTIVO")
    private Integer activo;

    // Constructor sin argumentos
    public Usuario() {}

    // Getters y Setters
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getTipoDeDocumento() { return tipoDeDocumento; }
    public void setTipoDeDocumento(String tipoDeDocumento) { this.tipoDeDocumento = tipoDeDocumento; }

    public String getNumeroDeDocumento() { return numeroDeDocumento; }
    public void setNumeroDeDocumento(String numeroDeDocumento) { this.numeroDeDocumento = numeroDeDocumento; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Integer getActivo() { return activo; }
    public void setActivo(Integer activo) { this.activo = activo; }
}
