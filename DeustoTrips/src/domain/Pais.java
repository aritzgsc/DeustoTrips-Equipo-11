package domain;

// País es un tipo de destino

public class Pais extends Destino {

	public Pais(String nombre) {
		
		setNombre(nombre);
		
	}
	
	public Pais(String nombre, boolean defaultAns) {
		setNombre(nombre);
		setDefaultAns(defaultAns);
	}
	
	@Override
	public String toString() {
		return getNombre();
	}

	@Override
	public String getNombrePais() {
		return getNombre();
	}

}
