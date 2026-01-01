package domain;

import java.awt.image.BufferedImage;
import java.util.Objects;

import domain.Viaje.TipoViaje;

//Compañía de viajes con un nombre y un logo

public class Compania {

	
	private int id;
	private String nombre;
	private double factorPrecio;
	private BufferedImage logo;
	private TipoViaje tipoViaje;
	private Pais paisOrigen;
			
	public Compania(int id, String nombre, double factorPrecio, BufferedImage logo, TipoViaje tipoViaje, Pais paisOrigen) {
				
		this.id = id;
		this.nombre = nombre;
		this.factorPrecio = factorPrecio;
		this.logo = logo;
		this.tipoViaje = tipoViaje;
		this.paisOrigen = paisOrigen;
				
	}

	public int getId() {
		return id;
	}
			
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getFactorPrecio() {
		return factorPrecio;
	}

	public void setFactorPrecio(double factorPrecio) {
		this.factorPrecio = factorPrecio;
	}

	public BufferedImage getLogo() {
		return logo;
	}

	public void setLogo(BufferedImage logo) {
		this.logo = logo;
	}

	public TipoViaje getTipoViaje() {
		return tipoViaje;
	}

	public void setTipoViaje(TipoViaje tipoViaje) {
		this.tipoViaje = tipoViaje;
	}

	public Pais getPaisOrigen() {
		return paisOrigen;
	}

	public void setPaisOrigen(Pais paisOrigen) {
		this.paisOrigen = paisOrigen;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Compania other = (Compania) obj;
		return id == other.id;
	}
			
}
