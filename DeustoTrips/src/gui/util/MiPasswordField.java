package gui.util;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import gui.main.VentanaPrincipal;
import main.Main;

// Clase para normalizar el tipo de JPasswordField utilizado, añade la funcionalidad de poder visualizar lo que tienes escrito mientras estés clicando en el botón

public class MiPasswordField extends JSplitPane {

	static final long serialVersionUID = 1L;

	private JPasswordField contrasenaPF = new JPasswordField();			// Creamos esto fuera del constructor para poder acceder a el mediante funciones
	
	public MiPasswordField() {
		
		// Configuración del JSplitPane (lo usamos para tener más control sobre el tamaño del botón aunque se podría usar un JPanel normal con BorderLayout)
		
		setDividerLocation(430);
		setDividerSize(0);
		setBorder(null);
		
		// FIN Configuración del JSplitPane
		////
		// Creamos el panel de la izquierda (con JPasswordField)
		
		JPanel panelIzquierda = new JPanel(new BorderLayout());
		
		contrasenaPF.setBorder(Main.DEFAULT_LINE_BORDER);
		contrasenaPF.setFont(Main.FUENTE);
		contrasenaPF.addKeyListener(Main.ANTI_CARACTERES_RAROS);
		contrasenaPF.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				SwingUtilities.invokeLater(() -> VentanaPrincipal.getVentanaPrincipal().requestFocusInWindow());
			}
		
		});
		
		panelIzquierda.add(contrasenaPF);
		
		// FIN Creamos el panel de la izquierda
		// Creamos el panel de la derecha (con JButton)
		
		JPanel panelDerecha = new JPanel(new BorderLayout());
		
		ImageIcon ojoCerrado = new ImageIcon(new ImageIcon("resources/images/ojo-cerrado.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		ImageIcon ojoAbierto = new ImageIcon(new ImageIcon("resources/images/ojo.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		
		JButton mostrar = new JButton(ojoCerrado);
		mostrar.setFocusable(false);
		mostrar.setBackground(new Color(0xEEEEEE));
		mostrar.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				contrasenaPF.setEchoChar('•');
				mostrar.setIcon(ojoCerrado);
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				contrasenaPF.setEchoChar((char) 0);
				mostrar.setIcon(ojoAbierto);
			}
			
		});
		
		panelDerecha.add(mostrar);
		
		// FIN Creamos el panel de la derecha
		// Añadimos los paneles a sus respectivos sitios
		
		setLeftComponent(panelIzquierda);
		setRightComponent(panelDerecha);
		
	}
	
	// Método para conseguir la contraseña en formato String (más fácil que char[])
	
	public String getPassword() {
		String contrasena = "";
		for (char letra : contrasenaPF.getPassword()) {
			contrasena += letra;
		}
		return contrasena;
	}
	
	// Método para ver si la contraseña es válida 
	// TODO Se le pueden meter más condiciones (al menos una mayúscula, al menos un número, al menos un ".", etc...) - de momento así para pruebas 
	
	public boolean isContrasenaValida() {
		return this.getPassword().length() >= 8;
	}
	
}
