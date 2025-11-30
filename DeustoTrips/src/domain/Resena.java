package domain;

import java.time.LocalDate;

// Reseña que se deja en una reserva de alojamiento particular (1 reseña <-> 1 reserva)

public class Resena {

	private int id;
	private String nombreYApellido;
	private double estrellas;
	private String mensaje;
	private LocalDate fecha;
	
	public Resena(int id, String nombreYApellido, double estrellas, String mensaje, LocalDate fecha) {
		
		this.id = id;
		this.nombreYApellido = nombreYApellido;
		this.estrellas = estrellas;
		this.mensaje = mensaje;
		this.fecha = fecha;
		
	}
	
	public int getId() {
		return id;
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
