package domain;

// Aeropuerto es un tipo de destino que está en una ciudad (que a su vez está en un país)

public class Aeropuerto extends Destino {

	private Ciudad ciudad;

	public Aeropuerto(Ciudad ciudad, String nombre) {
		
		setCiudad(ciudad);
		setNombre(nombre);
		
	}
	
	public Ciudad getCiudad() {
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	@Override
	public String toString() {
		return getNombre() + ", " + getCiudad();
	}

	@Override
	public String getNombrePais() {
		return getCiudad().getNombrePais();
	}
	
}
