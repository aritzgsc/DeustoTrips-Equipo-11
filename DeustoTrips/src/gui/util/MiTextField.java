package gui.util;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import gui.main.VentanaPrincipal;
import main.Main;

// Normalización de JTextField

public class MiTextField extends JTextField {

	private static final long serialVersionUID = 1L;
	
	public MiTextField() {
		
		// Configuración del JTextField
		
		setBorder(Main.DEFAULT_LINE_BORDER);
		setFont(Main.FUENTE);
		addKeyListener(Main.ANTI_CARACTERES_RAROS);
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
	
	public MiTextField(String placeHolder) {
		
		// Configuración del JTextField
		
		setBorder(Main.DEFAULT_LINE_BORDER);
		setFont(Main.FUENTE);
		addKeyListener(Main.ANTI_CARACTERES_RAROS);
		setMargin(new Insets(5, 10, 5, 10));
		setForeground(Color.GRAY);
		
		// FIN Configuración del JTextField
		////
		// Añadimos esta linea para que cuando se deje de escribir en un JTextField no se pase automaticamente al siguiente, sino que el usuario tenga que clicar donde quiere
		// También configuramos el placeHolder
		
		setText(placeHolder);
		
		addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				
				if (getText().equals(placeHolder)) {
					
					setText("");
					setForeground(Color.BLACK);
					
				}
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				
				if (getText().equals("")) {
					
					setText(placeHolder);
					setForeground(Color.GRAY);
					
				}
				
				SwingUtilities.invokeLater(() -> VentanaPrincipal.getVentanaPrincipal().requestFocusInWindow());
			}
		
		});
		
	}
	
}
