package domain;

import javax.swing.ImageIcon;

// Clase madre del resto de tipos de destinos

public abstract class Destino implements Comparable<Destino> {

	private int id;
	private String nombre;
	private double latitud;
	private double longitud;
	private ImageIcon bandera;
	private boolean defaultAns;
	
	private String nombreBusqueda;		// Para mayor eficiencia
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public ImageIcon getBandera() {
		return bandera;
	}

	public void setBandera(ImageIcon bandera) {
		this.bandera = bandera;
	}

	public boolean isDefaultAns() {
		return defaultAns;
	}

	public void setDefaultAns(boolean defaultAns) {
		this.defaultAns = defaultAns;
	}
	
	public String getNombreBusqueda() {
		return nombreBusqueda;
	}

	public void setNombreBusqueda(String nombreBusqueda) {
		this.nombreBusqueda = nombreBusqueda;
	}

	public abstract Pais getPais();
	
	public abstract String getNombrePais();
	
	// Comprobado con datos de prueba en MiSelectorDestinos
	
	@Override
	public int compareTo(Destino other) {											// Objetivo: DefaultAns, País, Ciudades del país, Aeropuertos del país (todo en orden alfabético - países entre ellos también)
		
		if (other == null) return 1;

	    // Prioridad del default
		
	    if (this.isDefaultAns() && !other.isDefaultAns()) return -1;
	    if (!this.isDefaultAns() && other.isDefaultAns()) return 1;
	    
	    // Prioridad por tipo
	    
	    int thisTipo = this instanceof Pais ? 0 : this instanceof Ciudad ? 1 : 2;
	    int otherTipo = other instanceof Pais ? 0 : other instanceof Ciudad ? 1 : 2;

	    // Comparar país seguro
	    
	    String thisPais = this.getNombrePais() != null ? this.getNombrePais() : "";
	    String otherPais = other.getNombrePais() != null ? other.getNombrePais() : "";
	    int paisCompare = thisPais.compareTo(otherPais);
	    
	    if (paisCompare != 0) return paisCompare;

	    // Comparar tipo
	    
	    if (thisTipo != otherTipo) return thisTipo - otherTipo;

	    // Comparar nombre seguro
	    
	    String thisNombre = this.getNombre() != null ? this.getNombre() : "";
	    String otherNombre = other.getNombre() != null ? other.getNombre() : "";
	    
	    return thisNombre.compareTo(otherNombre);
		
	}
	
}
