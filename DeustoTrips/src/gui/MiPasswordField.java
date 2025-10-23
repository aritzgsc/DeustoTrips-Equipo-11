package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

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
		
		contrasenaPF.setBorder(new LineBorder(new Color(0x7A8A99)));
		contrasenaPF.setFont(Main.fuente);
		contrasenaPF.addKeyListener(Main.antiCaracteresRaros);
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
		
		// TODO Cambiar los caracteres de mostrar por imagenes (en /resources/images hay que cambiarles el tamaño (mejor cambiarselo a mano ya que el tamaño del botón va a ser siempre el mismo, ya que las ventanas emergentes que lo usan no son resizable))
		
		JButton mostrar = new JButton("○");
		mostrar.setFocusable(false);
		mostrar.setBackground(Color.WHITE);
		mostrar.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				contrasenaPF.setEchoChar('•');
				mostrar.setText("○");
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				contrasenaPF.setEchoChar((char) 0);
				mostrar.setText("◉");
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
