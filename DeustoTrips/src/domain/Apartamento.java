package domain;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

// Apartamento es un tipo de Alojamiento

public class Apartamento extends Alojamiento {

	private int id;
	private double precioNP;
	private int capacidadMax;
	private String correoPropietario;
	
	public Apartamento(int id, String nombre, String direccion, Ciudad ciudad, String descripcion, List<Resena> resenas, List<BufferedImage> imagenes, double precioNP, int capacidadMax, String correoPropietario) {
		
		super(nombre, direccion, ciudad, descripcion, resenas, imagenes);
		this.id = id;
		this.precioNP = precioNP;
		this.capacidadMax = capacidadMax;
		this.correoPropietario = correoPropietario;
		
	}

	public double getPrecioNP() {
		return precioNP;
	}

	public void setPrecioNP(double precioNP) {
		this.precioNP = precioNP;
	}

	public int getCapacidadMax() {
		return capacidadMax;
	}

	public void setCapacidadMax(int capacidadMax) {
		this.capacidadMax = capacidadMax;
	}

	public String getCorreoPropietario() {
		return correoPropietario;
	}

	@Override
	public double calcularPrecio(int nPersonas, int nNoches) {
		return precioNP * nPersonas * nNoches;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
    public int hashCode() {
        // Solo nos importa el ID.
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
        
        Apartamento other = (Apartamento) obj;
        return id == other.id;
    }
	
}
