package domain;

// Ciudad es un tipo de destino que está en un país

public class Ciudad extends Destino {

	private Pais pais;

	public Ciudad(Pais pais, String nombre) {
		
		setPais(pais);
		setNombre(nombre);
		
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
