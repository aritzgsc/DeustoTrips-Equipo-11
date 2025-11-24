package domain;

import main.util.Utilidades;

// Ciudad es un tipo de destino que está en un país

public class Ciudad extends Destino {

	private Pais pais;

	public Ciudad(int id, Pais pais, String nombre, double latitud, double longitud) {
		
		setId(id);
		setPais(pais);
		setNombre(nombre);
		setLatitud(latitud);
		setLongitud(longitud);
		setBandera(pais.getBandera());
		setDefaultAns(false);
		
		setNombreBusqueda(Utilidades.normalizar(toString()));
		
	}
	
	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	@Override
	public String toString() {
		return getNombre() + ", " + getPais();
	}

	@Override
	public String getNombrePais() {
		return getPais().getNombrePais();
	}
	
}
