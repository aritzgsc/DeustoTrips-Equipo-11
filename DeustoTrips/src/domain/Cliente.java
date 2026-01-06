package domain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Objects;

// Usuario con los datos que se guardan en la BD

public class Cliente {

	private String correo;
	private String nombre;
	private String apellidos;
	private String contrasena;
	private Color color;
	private BufferedImage imagen;
	
	public Cliente(String correo, String nombre, String apellidos, String contrasena, Color color, BufferedImage imagen) {
		
		this.correo = correo;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.contrasena = contrasena;
		this.color = color;
		this.imagen = imagen;
		
	}
	
	public Cliente(String correo, String nombre, String apellidos, Color color, BufferedImage imagen) {
		
		this.correo = correo;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.contrasena = null;
		this.color = color;
		this.imagen = imagen;
		
	}
	
	public Cliente(String correo, String nombre, String apellidos, String contrasena, Color color) {
	
		this.correo = correo;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.contrasena = contrasena;
		this.color = color;
		this.imagen = null;
		
	}
	
	public Cliente(String correo, String nombre, String apellidos) {
		
		this.correo = correo;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.contrasena = null;
		this.color = null;
		this.imagen = null;
		
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public BufferedImage getImagen() {
		return imagen;
	}

	public void setImagen(BufferedImage imagen) {
		this.imagen = imagen;
	}

	@Override
	public int hashCode() {
		return Objects.hash(correo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		return Objects.equals(correo, other.correo);
	}
	
}
