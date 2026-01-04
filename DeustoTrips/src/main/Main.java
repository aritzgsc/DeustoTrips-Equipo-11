package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.border.LineBorder;

import db.GestorDB;
import domain.Aeropuerto;
import domain.Compania;
import domain.Destino;
import domain.Viaje;
import gui.main.VentanaPrincipal;
import gui.main.busqueda.MiSelectorDestino;

// Clase con el método main y algunos genéricos útiles que utilizaremos en todo el proyecto para garantizar que el proyecto tenga un formato fácil de modificar

public class Main {
	
	public static final String NOMBRE_APP = "DeustoTrips";
	
	public static final Font FUENTE = new Font("Comic Sans MS", Font.PLAIN, 18);
	
	public static final LineBorder DEFAULT_LINE_BORDER = new LineBorder(new Color(0x7A8A99));
	
	public static final KeyAdapter ANTI_CARACTERES_RAROS = new KeyAdapter() {

		@Override
		public void keyTyped(KeyEvent e) {
			
			char c = e.getKeyChar();
			
			if (!Character.isDigit(c) && !Character.isAlphabetic(c) && !"_-@. ".contains(Character.toString(c))) {			// Solo permite números, letras, y los caracteres '.', '@', '-' y '_'
				
				e.consume();
				
			}
				
		}
	
	};
	
	// Al registrar un nuevo usuario se le asignará uno de estos colores para mostrar la foto de perfil
	// Lista generada por GEMINI
	
	public static final String[] COLORES_PERFIL = {
			
			"#FF6B6B", // Rojo Pastel Intenso (Muy popular)
	        "#4ECDC4", // Turquesa "Pearl Aqua"
	        "#45B7D1", // Azul Cielo Vibrante
	        "#96CEB4", // Verde Menta Apagado
	        "#FFEEAD", // Amarillo Crema (Ideal para texto negro)
	        
	        // --- TENDENCIA: NORDIC & EARTHY (Elegantes) ---
	        "#2C3E50", // Azul Medianoche (Muy elegante)
	        "#E056FD", // Violeta Neón Suave
	        "#686DE0", // Azul Blurple (Estilo Discord)
	        "#30336B", // Azul Profundo Nocturno
	        "#95AFC0", // Gris Azulado Claro
	        
	        // --- TENDENCIA: FRUITY & FRESH (Amigables) ---
	        "#F7B731", // Amarillo Mango
	        "#FA8231", // Naranja Mandarina
	        "#EB3B5A", // Rojo Sandía
	        "#20BF6B", // Verde Esmeralda Suave
	        "#0FB9B1", // Cian Retro
	        
	        // --- TENDENCIA: RETRO WAVE ---
	        "#A3CB38", // Verde Oliva Moderno
	        "#1289A7", // Azul Océano
	        "#D980FA", // Lavanda Chillón
	        "#B53471", // Rosa Berry Profundo
	        "#833471"  // Ciruela
		        
	};
	
	public static Comparator<List<Viaje>> comparadorViajesCompletos = new Comparator<List<Viaje>>() {

	    public static final double VALOR_HORA_VIAJE = 10; 

	    public static final double PENALIZACION_ESCALA_AVION = 30; 
	    public static final double PENALIZACION_ESCALA_BUS = 15;   
	    public static final double PENALIZACION_ESCALA_TREN = 10;  
	    
	    public static final double PENALIZACION_HORA_EXTREMA = 25;
	    
	    public static final int HORA_LIMITE_MADRUGON = 8;
	    public static final int HORA_LIMITE_NOCHE = 22;
		
	    private double calcularPuntuacion(List<Viaje> viajeCompleto) {
	        
	        // Puntuación base = Precio viaje 
	    	
	        double puntuacion = Viaje.calcularPrecioTotal(viajeCompleto, 1);
	        
	        // Penalización por duración
	        
	        double horasDuracion = Viaje.getDuracionTotalViaje(viajeCompleto) / 60.0;
	        puntuacion += (horasDuracion * VALOR_HORA_VIAJE);
	        
	        // Penalización por tipo de escala
	        
	        for (int i = 1; i < viajeCompleto.size(); i++) {
	            
	            Viaje siguienteTramo = viajeCompleto.get(i);
	            
	            switch (siguienteTramo.getTipoViaje()) {
	                case AVION:
	                    puntuacion += PENALIZACION_ESCALA_AVION;
	                    break;
	                case TREN:
	                    puntuacion += PENALIZACION_ESCALA_TREN;
	                    break;
	                case AUTOBUS:
	                	puntuacion += PENALIZACION_ESCALA_BUS;
	                    break;
	                default:
	                    puntuacion += 20;
	                    break;
	            }
	        }
	        
	        // Penalización por malos horarios
	        
	        Viaje primerViaje = viajeCompleto.getFirst();
	        Viaje ultimoViaje = viajeCompleto.getLast();
	        LocalTime horaSalida = primerViaje.getHora();
	        LocalTime horaLlegada = ultimoViaje.getHora().plusMinutes(ultimoViaje.getDuracion());

	        // Madrugón -> Penalización (Mayor madrugón -> Mayor penalización)
	        
	        if (horaSalida.getHour() < HORA_LIMITE_MADRUGON) {
	        	
	            int horasAntes = HORA_LIMITE_MADRUGON - horaSalida.getHour();
	            puntuacion += PENALIZACION_HORA_EXTREMA * (horasAntes / 4.0);
	            
	        }

	        // Llegar muy tarde -> Penalización (Más tarde -> Más penalización)
	        
	        double horaLlegadaDecimal = horaLlegada.getHour() + (horaLlegada.getMinute() / 60.0);
	        
	        // Ajuste para horas de madrugada (las 01:00 deben contar como las 25:00 para la mate)
	        if (horaLlegadaDecimal < 6.0) horaLlegadaDecimal += 24.0;

	        if (horaLlegadaDecimal > HORA_LIMITE_NOCHE) {
	        	
	            double horasDespues = horaLlegadaDecimal - HORA_LIMITE_NOCHE;
	            puntuacion += PENALIZACION_HORA_EXTREMA * (horasDespues / 4.0);
	            
	        }
	        
	        return puntuacion;
	    }
		
		@Override
		public int compare(List<Viaje> viaje1, List<Viaje> viaje2) {
			return Double.compare(calcularPuntuacion(viaje1), calcularPuntuacion(viaje2));
		}
		
	};
	
	public static Map<Integer, Destino> destinoPorIndice;
	
	public static Map<Integer, Compania> companiaPorIndice;
	
	public static Map<Integer, List<Aeropuerto>> aeropuertosPorIndiceCiudad;
	
	public static void main(String[] args) {
		
		new VentanaPrincipal();
		
		destinoPorIndice = new HashMap<Integer, Destino>();
		
		List<Destino> destinos = MiSelectorDestino.getTodosDestinos();
		
		for (Destino destino : destinos) {
			if (!destinoPorIndice.containsKey(destino.getId())) destinoPorIndice.put(destino.getId(), destino);
		}
		
		companiaPorIndice = new HashMap<Integer, Compania>();
		
		List<Compania> companias = GestorDB.getCompanias();
		
		for (Compania compania : companias) {
			if (!companiaPorIndice.containsKey(compania.getId())) companiaPorIndice.put(compania.getId(), compania);
		}
		
		aeropuertosPorIndiceCiudad = new HashMap<Integer, List<Aeropuerto>>();
		
		for (Destino destino : destinos) {
			if (destino instanceof Aeropuerto) {
				if (!aeropuertosPorIndiceCiudad.containsKey(((Aeropuerto) destino).getCiudad().getId())) aeropuertosPorIndiceCiudad.put(((Aeropuerto) destino).getCiudad().getId(), new ArrayList<Aeropuerto>());
				aeropuertosPorIndiceCiudad.get(((Aeropuerto) destino).getCiudad().getId()).add((Aeropuerto) destino);
			}
		}
		
	}

}
