package main;

import java.awt.*;
import java.awt.event.*;

import javax.swing.border.*;

import gui.main.VentanaPrincipal;

// Clase con el método main y algunos genéricos útiles que utilizaremos en todo el proyecto para garantizar que el proyecto tenga un formato fácil de modificar

public class Main {
	
	public static final String NOMBRE_APP = "DeustoTrips";
	public static final Font FUENTE = new Font("Comic Sans MS", Font.PLAIN, 18);
	public static final LineBorder DEFAULT_LINE_BORDER = new LineBorder(new Color(0x7A8A99));
	public static final KeyAdapter ANTI_CARACTERES_RAROS = new KeyAdapter() {

		@Override
		public void keyTyped(KeyEvent e) {
			
			char c = e.getKeyChar();
			
			if (!Character.isDigit(c) && !Character.isAlphabetic(c) && !"@.".contains(Character.toString(c))) {			// Solo permite números, letras, y los caracteres '.' y '@'
				
				e.consume();
				
			}
				
		}
	
	};
	
	public static void main(String[] args) {
		
		new VentanaPrincipal();
		
	}

}
