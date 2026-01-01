package main.util;

import java.text.Normalizer;

import domain.Destino;
import domain.Viaje.TipoViaje;

public class Utilidades {

	// Funciones útiles para cálculos con Viajes

    // Fórmula de Haversine para distancia entre coordenadas
    public static double calcularDistancia(Destino d1, Destino d2) {
    	
        double radioTierra = 6371;
        
        double dLat = Math.toRadians(d2.getLatitud() - d1.getLatitud());
        double dLon = Math.toRadians(d2.getLongitud() - d1.getLongitud());
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(d1.getLatitud())) * Math.cos(Math.toRadians(d2.getLatitud())) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return radioTierra * c;
        
    }
	
    public static double getVelocidadMedia(TipoViaje tipo) {
        
		switch (tipo) {
            case AVION: return 800.0;
            case TREN: return 160.0;
            case AUTOBUS: return 90.0;
            default: return 100.0;
        }
		
    }
    
    public static double getPrecioBasePorKm(TipoViaje tipo) {
        
    	switch (tipo) {
            case AVION: return 0.10;
            case TREN: return 0.08;
            case AUTOBUS: return 0.05;
            default: return 0.05;
        }
    	
    }
    
    public static double getFactorDesviacion(TipoViaje tipo) {
        switch (tipo) {
            case AVION: return 1.1;   // +10% (Maniobras despegue/aterrizaje, rutas aéreas)
            case TREN: return 1.25;    // +25% (Las vías dan vueltas, pero menos que carreteras)
            case AUTOBUS: return 1.4; // +40% (Carreteras, curvas, tráfico)
            default: return 1.2;
        }
    }
    
    public static int getTiempoRotacion(TipoViaje tipo) {
        // Devuelve minutos de espera necesarios antes de volver a salir
        switch (tipo) {
            case AVION: return 60;   // 1 hora para repostar, maletas, limpieza...
            case TREN: return 30;    // 30 min cambio de cabina y limpieza
            case AUTOBUS: return 20; // 20 min descanso conductor
            default: return 30;
        }
    }
    
	// FIN Funciones útiles para cálculos con Viajes
	////
	// Funciones para comparar strings (usadas para no descartar tantas ciudades que podrían ser descartadas con equals)
	// Función que normaliza un String (le quita carácteres raros, lo pasa a minusculas y le quita espacios al final)

	public static String normalizar(String input) {

		if (input == null) {
			return "";
		}
		
		String inputNorm = Normalizer.normalize(input, Normalizer.Form.NFD);

		return inputNorm.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase().trim();
		
	}

	// Función que devuelve un entero que indica la "distancia" entre dos strings 0 si son iguales, 1 si hay 1 error, 2 si hay 2...
	// Recomendada por GEMINI

	public static int distanciaLevenshtein(String a, String b) {

		a = normalizar(a);
		b = normalizar(b);

		int[] costs = new int[b.length() + 1];

		for (int j = 0; j < costs.length; j++)

			costs[j] = j;

		for (int i = 1; i <= a.length(); i++) {

			costs[0] = i;

			int nw = i - 1;

			for (int j = 1; j <= b.length(); j++) {

				int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;

			}

		}

		return costs[b.length()];
	}

	// FIN Funciones para comparar strings
	
}
