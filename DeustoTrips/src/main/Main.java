package main;

import java.awt.Font;
import java.awt.event.*;

import gui.VentanaPrincipal;

// Clase con el método main y algunos genéricos útiles que utilizaremos en todo el proyecto para garantizar que el proyecto tenga un formato fácil de modificar

public class Main {
	
	public static final String nombreApp = "Deusto Trips";
	public static final Font fuente = new Font("Comic Sans MS", Font.PLAIN, 18);
	
	public static void main(String[] args) {
		
		new VentanaPrincipal();
		
	}

}
