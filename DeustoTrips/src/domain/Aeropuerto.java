package domain;

import main.util.Utilidades;

// Aeropuerto es un tipo de destino que está en una ciudad (que a su vez está en un país)

public class Aeropuerto extends Destino {

	private Ciudad ciudad;

	public Aeropuerto(int id, Ciudad ciudad, String nombre, double latitud, double longitud) {
		
		setId(id);
		setCiudad(ciudad);
		setNombre(nombre);
		setLatitud(latitud);
		setLongitud(longitud);
		setBandera(ciudad.getPais().getBandera());
		setDefaultAns(false);
		
		setNombreBusqueda(Utilidades.normalizar(toString()));
		
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
	public Pais getPais() {
		return getCiudad().getPais();
	}
	
	@Override
	public String getNombrePais() {
		return getCiudad().getNombrePais();
	}
	
}
