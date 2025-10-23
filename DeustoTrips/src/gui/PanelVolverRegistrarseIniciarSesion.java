package gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import main.Main;

// Panel de arriba de la ventana principal, contendrá los botones para volver a la pestaña inicial, iniciar sesión, registrarse ; estos dos ultimos no deberán mostrarse en caso de ya estar con la sesión iniciada

public class PanelVolverRegistrarseIniciarSesion extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public PanelVolverRegistrarseIniciarSesion() {
		
		// Configuración del panel
		
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 20, 5, 20));
		
		// FIN Configuración del panel
		////
		// Creación de un panel para los botones de iniciar sesión y registrarse
		
		JPanel panelDerecha = new JPanel();
		panelDerecha.setLayout(new GridLayout(1, 2, 5, 0));
		
		// FIN Creación de un panel para los botones de iniciar sesión y registrarse
		////
		// Creación de los botones de registrarse e iniciar sesión
		
		JButton registrarse = new JButton("Registrarse");
		registrarse.setFocusable(false);
		registrarse.setBackground(Color.WHITE);
		registrarse.setFont(Main.fuente);
		registrarse.addActionListener(e -> new VentanaRegistrarse());			// Abrimos ventana emergente configurada en su propia clase
		
		JButton iniciarSesion = new JButton("Iniciar sesión");
		iniciarSesion.setFocusable(false);
		iniciarSesion.setBackground(Color.WHITE);
		iniciarSesion.setFont(Main.fuente);
		iniciarSesion.addActionListener(e -> new VentanaIniciarSesion());		// Abrimos ventana emergente configurada en su propia clase
		
		// FIN Creación de los botones de registrarse e iniciar sesión
		////
		// Creación del botón volver
		
		JButton volver = new JButton(Main.nombreApp);
		volver.setPreferredSize(new Dimension(350, 50));
		volver.setFocusable(false);
		volver.setBackground(Color.WHITE);
		volver.setFont(Main.fuente);
		volver.addActionListener(e -> PanelPestanasBusqueda.resetAll());		// Cuando hagamos click en volver se resetearán todas las selecciones y datos escritos
		
		// FIN Creación del botón volver
		////
		// Añadimos los botones a sus correspondientes sitios
		
		add(volver, BorderLayout.WEST);
		panelDerecha.add(registrarse);
		panelDerecha.add(iniciarSesion);
		add(panelDerecha, BorderLayout.EAST);
		
	}
	
	// TODO Actualizar este panel cuando se inicie sesión (función en db.Consulta)
	
}
