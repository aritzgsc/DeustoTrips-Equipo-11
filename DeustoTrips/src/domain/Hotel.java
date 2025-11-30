package domain;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

// Hotel es un tipo de Alojamiento

public class Hotel extends Alojamiento {

	private int id;
	private int numHabs;
	private int capMaxHab;
	private double precioNHab;
	
	public Hotel(int id, String nombre, String direccion, Ciudad ciudad, String descripcion, List<Resena> resenas, List<BufferedImage> imagenes, int numHabs, int capMaxHab, double precioNHab) {
		
		super(nombre, direccion, ciudad, descripcion, resenas, imagenes);
		this.id = id;
		this.numHabs = numHabs;
		this.capMaxHab = capMaxHab;
		this.precioNHab = precioNHab;
		
	}

	public int getNumHabs() {
		return numHabs;
	}

	public void setNumHabs(int numHabs) {
		this.numHabs = numHabs;
	}

	public int getCapMaxHab() {
		return capMaxHab;
	}

	public void setCapMaxHab(int capMaxHab) {
		this.capMaxHab = capMaxHab;
	}

	public double getPrecioNHab() {
		return precioNHab;
	}

	public void setPrecioNHab(double precioNHab) {
		this.precioNHab = precioNHab;
	}

	public int nHabitacionesOcupadas(int nPersonas) {
		return (int) Math.ceil((double) nPersonas / capMaxHab); 
	}
	
	@Override
	public double calcularPrecio(int nPersonas, int nNoches) {
		return precioNHab * nHabitacionesOcupadas(nPersonas) * nNoches;
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
        
        Hotel other = (Hotel) obj;
        return id == other.id;
    }
	
}
