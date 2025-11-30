package gui.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import main.Main;

// Nuestra ventana principal, aquí estarán todos los componentes de la GUI que iremos creando con el tiempo, trataremos de subdividir todo en clases para poder reutilizarlas y no tener clases tan grandes

public final class VentanaPrincipal extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private static VentanaPrincipal ventanaPrincipal;		// Sacamos la propia ventana como variable estática para centrar las ventanas emergentes a la ventana principal
	
	public VentanaPrincipal(){
		
		ventanaPrincipal = this;							// Le damos el valor a la variable nada más crear la instancia 
		
		// Configuración de la ventana
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(1440, 500));								// Para que si se quita el maximizado se quede con ese tamaño
		setLocationRelativeTo(null);
		try {
			setIconImage(ImageIO.read(new File("resources/images/logo.jpg")));
		} catch (IOException e) {
			System.err.println("Error al cargar el logo");
//			e.printStackTrace();
		}
		setExtendedState(MAXIMIZED_BOTH);
		setTitle(Main.NOMBRE_APP);
		
		// FIN Configuración de la ventana
		////
		// Panel arriba -- Imagen/Nombre app que nos devuelve a la pestaña principal + Botón Registrarse + Botón Iniciar sesión

		PanelVolverRegistrarseIniciarSesion panelArriba = new PanelVolverRegistrarseIniciarSesion();
		add(panelArriba, BorderLayout.NORTH);
		
		// FIN Panel arriba -- Imagen/Nombre app que nos devuelve a la pestaña principal + Botón Registrarse y Iniciar sesión
		////
		// Panel medio -- Pestañas de búsqueda (En ese JTabbedPanel irán todas las pestañas que crearemos en clases separadas)

		PanelPestanasBusqueda panelMedio = new PanelPestanasBusqueda();
		add(panelMedio/*, BorderLayout.CENTER*/);
		
		// FIN Panel medio -- Pestañas de búsqueda (En ese JTabbedPanel irán todas las pestañas que crearemos en clases separadas)
		////
		// Panel abajo -- Resultados de búsqueda
		
		PanelResultadosBusqueda panelAbajo = new PanelResultadosBusqueda();
		add(panelAbajo, BorderLayout.SOUTH);
		
		// FIN Panel abajo -- Resultados de búsqueda
		////
		// Hacemos la ventana visible
		
		setVisible(true);
		
		SwingUtilities.invokeLater(() -> requestFocusInWindow());			// Hacemos esto para que al abrir la aplicación el usuario no aparezca con nada "clicado"
		
	}
	
	// Hacemos un getter estático para obtener la instancia de la ventana principal
	
	public static VentanaPrincipal getVentanaPrincipal() {
		return ventanaPrincipal;
	}
	
}
