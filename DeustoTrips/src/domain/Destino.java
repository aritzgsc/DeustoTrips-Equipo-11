package domain;

import java.util.Objects;

// Clase madre del resto de tipos de destinos

public abstract class Destino implements Comparable<Destino> {

	private String nombre;
	private boolean defaultAns;
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isDefaultAns() {
		return defaultAns;
	}

	public void setDefaultAns(boolean defaultAns) {
		this.defaultAns = defaultAns;
	}
	
	public abstract String getNombrePais();
	
	@Override
	public int hashCode() {
		return Objects.hash(nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Destino other = (Destino) obj;
		return Objects.equals(nombre, other.nombre);
	}
	
	// Comprobado con datos de prueba en MiSelectorDestinos
	
	@Override
	public int compareTo(Destino other) {											// Objetivo: País, Ciudades del país, Aeropuertos del país (todo en orden alfabético - países entre ellos también)
		
		int thisTipo = this instanceof Pais? 0 : this instanceof Ciudad? 1 : 2;		// Asignamos la "prioridad" que queremos darle a cada tipo de Destino
		int otherTipo = other instanceof Pais? 0 : other instanceof Ciudad? 1 : 2;	// Asignamos la "prioridad" que queremos darle a cada tipo de Destino
		
		if (other.isDefaultAns()) {
			
			return 1;																// El default va primero
			
		} else if (!this.getNombrePais().equals(other.getNombrePais())) {
			
			return this.getNombrePais().compareTo(other.getNombrePais());			// Si son de países distintos ordenamos por orden alfabético de país
			
		} else if (thisTipo != otherTipo) {
			
			return thisTipo - otherTipo;											// Si son del mismo país pero de diferente tipo, ordenamos por tipo
			
		} else {
			
			return this.getNombre().compareTo(other.getNombre());					// Si son del mismo país y tipo ordenamos alfabéticamente
			
		}
		
	}
	
}
