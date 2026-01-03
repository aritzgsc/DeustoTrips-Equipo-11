package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.border.LineBorder;

import db.GestorDB;
import domain.Aeropuerto;
import domain.Compania;
import domain.Destino;
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
			
			if (!Character.isDigit(c) && !Character.isAlphabetic(c) && !"@. ".contains(Character.toString(c))) {			// Solo permite números, letras, y los caracteres '.' y '@'
				
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
