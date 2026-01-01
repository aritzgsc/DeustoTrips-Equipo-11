package domain;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import main.util.Utilidades;

// Viaje (Origen -> Destino) un día de la semana determinado y a una hora determinada, con un precio por persona, de un tipo y realizado por una compañía de viajes

public class Viaje {
	
	public enum DiaSemana {
		
		LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO;
		
		public DiaSemana plusDays(int nDias) {
			return DiaSemana.values()[(this.ordinal() + nDias) % 7];
		}
		
		public int getId() {
			return this.ordinal() + 1;
		}
		
		public static DiaSemana getDiaSemana(int id) {
			return DiaSemana.values()[id - 1];
		}
		
	}
	
	public enum TipoViaje {
		
		AVION, TREN, AUTOBUS;
		
		public int getId() {
			return this.ordinal() + 1;
		}
		
		public static TipoViaje getTipoViaje(int id) {
			return TipoViaje.values()[id - 1];
		}
		
	}
	
	private int id;
	private DiaSemana diaSemana;
	private LocalTime hora;
	private double precioPorP;
	private int nPlazas;
	private TipoViaje tipoViaje;
	private Compania compania;
	private Destino origen;
	private Destino destino;
	
	public Viaje(int id, DiaSemana diaSemana, LocalTime hora, double precioPorP, int nPlazas, TipoViaje tipoViaje, Compania compania, Destino origen, Destino destino) {
		super();
		this.id = id;
		this.diaSemana = diaSemana;
		this.hora = hora;
		this.precioPorP = precioPorP;
		this.nPlazas = nPlazas;
		this.tipoViaje = tipoViaje;
		this.compania = compania;
		this.origen = origen;
		this.destino = destino;
	}

	public int getId() {
		return id;
	}

	public DiaSemana getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	public double getPrecioPorP() {
		return precioPorP;
	}

	public void setPrecioPorP(double precioPorP) {
		this.precioPorP = precioPorP;
	}

	public double getPrecio(int nPersonas) {
		return precioPorP * nPersonas;
	}
	
	public int getNPlazas() {
		return nPlazas;
	}

	public void setNPlazas(int nPlazas) {
		this.nPlazas = nPlazas;
	}

	public TipoViaje getTipoViaje() {
		return tipoViaje;
	}

	public void setTipoViaje(TipoViaje tipoViaje) {
		this.tipoViaje = tipoViaje;
	}

	public Compania getCompania() {
		return compania;
	}

	public void setCompania(Compania compania) {
		this.compania = compania;
	}

	public Destino getOrigen() {
		return origen;
	}

	public void setOrigen(Destino origen) {
		this.origen = origen;
	}

	public Destino getDestino() {
		return destino;
	}

	public void setDestino(Destino destino) {
		this.destino = destino;
	}

	public int getDuracion() {
		return (int) ((((Utilidades.calcularDistancia(origen, destino) * Utilidades.getFactorDesviacion(tipoViaje)) / Utilidades.getVelocidadMedia(tipoViaje)) * 60));
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Viaje other = (Viaje) obj;
		return id == other.id;
	}
	
	public static double calcularPrecioTotal(List<Viaje> viajes, int nPersonas) {
		
		int total = 0;
		
		for (Viaje viaje : viajes) {
			total += viaje.getPrecioPorP() * nPersonas;
		}
		
		return total;
		
	}
	
	public static int getDuracionTotalViaje(List<Viaje> viajes) {
		
		int duracionTotal = 0;
		
		for (int i = 0; i < viajes.size() - 1; i++) {
				
			Viaje actual = viajes.get(i);
	        Viaje siguiente = viajes.get(i + 1);
				
	        int minutosDiferenciaHora = (siguiente.getHora().getHour() * 60 + siguiente.getHora().getMinute()) - (actual.getHora().getHour() * 60 + actual.getHora().getMinute());		// Dará negativo si la hora del siguiente viaje es menor que la del actual
	        
	        int diasDiferencia = (siguiente.getDiaSemana().getId() - actual.getDiaSemana().getId() + 7) % 7;
	        
	        // Ponemos lo siguiente en caso de que haya quedado negativo y el día de ambos sea el mismo (significaría que hay 1 semana de diferencia)
	        
	        if (diasDiferencia == 0 && minutosDiferenciaHora < 0) {
	            diasDiferencia = 7;
	        }
	        
	        duracionTotal += (diasDiferencia * 24 * 60) + minutosDiferenciaHora;
	        
		}
		
		duracionTotal += viajes.getLast().getDuracion();
		
		return duracionTotal;
		
	}
	
	public static List<Compania> getCompaniasViaje(List<Viaje> viajes) {
		
		List<Compania> companiasViaje = new ArrayList<Compania>();
		
		for (Viaje viaje : viajes) {
			
			if (!companiasViaje.contains(viaje.getCompania())) companiasViaje.add(viaje.getCompania());
			
		}
		
		return companiasViaje;
		
	}
	
}
