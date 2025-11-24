package domain;

import javax.swing.ImageIcon;

import main.util.Utilidades;

// País es un tipo de destino

public class Pais extends Destino {
	
	public Pais(int id, String nombre, double latitud, double longitud, ImageIcon bandera) {		// Para cargar datos al programa
		
		setId(id);
		setNombre(nombre);
		setLatitud(latitud);
		setLongitud(longitud);
		setBandera(bandera);
		setDefaultAns(false);
		
		setNombreBusqueda(Utilidades.normalizar(toString()));
		
	}
	
	public Pais(String nombre, boolean defaultAns) {										// Para cargar la defaultAns
		
		setId(0);
		setNombre(nombre);
		setDefaultAns(defaultAns);
		setLatitud(0);
		setLongitud(0);
		setBandera(null);
		
		setNombreBusqueda(Utilidades.normalizar(toString()));
		
	}
	
	public Pais(int id, String nombre, double latitud, double longitud) {							// Para meter datos a la BD
		
		setId(id);
		setNombre(nombre);
		setLatitud(latitud);
		setLongitud(longitud);
		setBandera(null);
		setDefaultAns(false);
		
		setNombreBusqueda(Utilidades.normalizar(toString()));
		
	}
	
	@Override
	public String toString() {
		return getNombre();
	}

	@Override
	public Pais getPais() {
		return this;
	}
	
	@Override
	public String getNombrePais() {
		return getNombre();
	}

}
