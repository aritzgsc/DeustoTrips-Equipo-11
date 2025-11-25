package domain;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

public abstract class Alojamiento {

	private String nombre;
	private String direccion;
	private Ciudad ciudad;
	private String descripcion;
	private List<Resena> resenas;
	private List<BufferedImage> imagenes;
	
	public Alojamiento(String nombre, String direccion, Ciudad ciudad, String descripcion, List<Resena> resenas , List<BufferedImage> imagenes) {
		
		this.nombre = nombre;
		this.direccion = direccion;
		this.ciudad = ciudad;
		this.descripcion = descripcion;
		this.resenas = resenas;
		this.imagenes = imagenes;
		
	}


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public List<BufferedImage> getImagenes() {
		return imagenes;
	}

	public void setImagenes(List<BufferedImage> imagenes) {
		this.imagenes = imagenes;
	}

	public List<Resena> getResenas() {
		return resenas;
	}

	

	@Override
	public int hashCode() {
		return Objects.hash(ciudad, descripcion, direccion, nombre, resenas);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alojamiento other = (Alojamiento) obj;
		return Objects.equals(ciudad, other.ciudad) && Objects.equals(descripcion, other.descripcion)
				&& Objects.equals(direccion, other.direccion) && Objects.equals(nombre, other.nombre)
				&& Objects.equals(resenas, other.resenas);
	}


	public double calcularNotaMedia() {
		
		double suma = 0;
		
		if (resenas.size() == 0) {
			
			return 0;
			
		}
		
		for (Resena resena : resenas) {
			
			suma += resena.getEstrellas();
			
		}
		
		return suma / resenas.size();
		
	}
	
	public abstract double calcularPrecio(int nPersonas, int nNoches);
	
	public abstract int getId();

}
