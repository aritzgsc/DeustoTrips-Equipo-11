package domain;

// País es un tipo de destino

public class Pais extends Destino {

	public Pais(String nombre) {
		
		setNombre(nombre);
		
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
