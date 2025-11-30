package gui.util;

import java.awt.Color;

import javax.swing.JButton;

import main.Main;

// Normalización de JButton

public class MiButton extends JButton {

	private static final long serialVersionUID = 1L;

	public MiButton() {
		
		setFocusable(false);
		setBackground(Color.WHITE);
		setFont(Main.FUENTE);
		
	}
	
	public MiButton(String texto) {
		
		setFocusable(false);
		setBackground(Color.WHITE);
		setFont(Main.FUENTE);
		setText(texto);
		
	}
	
}
