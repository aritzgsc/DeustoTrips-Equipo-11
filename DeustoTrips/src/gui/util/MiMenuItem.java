package gui.util;

import java.awt.Dimension;

import javax.swing.JMenuItem;

import main.Main;

// Normalización de JMenuItem

public class MiMenuItem extends JMenuItem {

	private static final long serialVersionUID = 1L;

	public MiMenuItem(String text, int width, int height) {
		
		setText(text);
		setPreferredSize(new Dimension(width, height));
		setFont(Main.FUENTE.deriveFont(16.f));
		setFocusable(false);
		
	}
	
}
