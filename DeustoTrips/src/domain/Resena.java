package domain;

import java.time.LocalDate;

public class Resena {

	private String nombreYApellido;
	private double estrellas;
	private String mensaje;
	private LocalDate fecha;
	
	public Resena(String nombreYApellido, double estrellas, String mensaje, LocalDate fecha) {
		
		this.nombreYApellido = nombreYApellido;
		this.estrellas = estrellas;
		this.mensaje = mensaje;
		this.fecha = fecha;
		
	}
	
	public String getNombreYApellido() {
		return nombreYApellido;
	}

	public void setNombreYApellido(String nombreYApellido) {
		this.nombreYApellido = nombreYApellido;
	}
	
	public double getEstrellas() {
		return estrellas;
	}

	public void setEstrellas(double estrellas) {
		this.estrellas = estrellas;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	
}
