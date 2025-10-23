package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import main.Main;

// Clase para normalizar el tipo de JTextField utilizado en todo el proyecto

public class MiTextField extends JTextField {

	private static final long serialVersionUID = 1L;
	
	public MiTextField() {
		
		// Configuración del JTextField
		
		setBorder(new LineBorder(new Color(0x7A8A99)));
		setFont(Main.fuente);
		addKeyListener(Main.antiCaracteresRaros);
		setMargin(new Insets(5, 10, 5, 10));
		
		// FIN Configuración del JTextField
		////
		// Añadimos esta linea para que cuando se deje de escribir en un JTextField no se pase automaticamente al siguiente, sino que el usuario tenga que clicar donde quiere
		
		addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				SwingUtilities.invokeLater(() -> VentanaPrincipal.getVentanaPrincipal().requestFocusInWindow());
			}
		
		});
		
	}
	
}
