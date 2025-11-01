package gui.main;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import main.Main;

// Panel que contiene todas las pestañas de búsqueda (Alojamientos, Viajes y Viaje + Alojamiento)

public class PanelPestanasBusqueda extends JTabbedPane {

	private static final long serialVersionUID = 1L;

	private static PanelPestanasBusqueda panelPestanasBusqueda;			// Usados en PanelVolverRegistrarseIniciarSesion (para resetear todos los valores de los tabs y seleccionar el inicial (Alojamientos))
	
	private static PanelAlojamientos panelAlojamientos;
	private static PanelViajes panelViajes;
//	private static PanelViajeAlojamiento panelViajeAlojamiento;
	
	public PanelPestanasBusqueda() {
		
		panelPestanasBusqueda = this;									// Le damos el valor a la variable nada más crear la instancia 
		
		// Configuración del panel
		
		setBorder(new EmptyBorder(5, 20, 5, 20));
		setFocusable(false);
		// FIN Configuración del panel
		////
		// Panel Alojamientos

		// Creación del panel y lo añadimos como panel de una nueva ventana
		
		panelAlojamientos = new PanelAlojamientos();					// TODO Clase PanelAlojamientos
		
		addTab("", panelAlojamientos);
		
		// FIN Creación del panel y lo añadimos como panel de una nueva ventana
		////
		// Creamos el componente personalizado
		
		JLabel alojamientosTab = new JLabel("Alojamientos");
		alojamientosTab.setPreferredSize(new Dimension(128, 50));
		alojamientosTab.setFont(Main.FUENTE.deriveFont(15.f));
		alojamientosTab.setHorizontalAlignment(SwingUtilities.CENTER);
		setTabComponentAt(0, alojamientosTab);
		
		// FIN Creamos el compontente personalizado
		// FIN Panel Alojamientos
		////
		// Panel Viajes
		
		// Creación del panel y lo añadimos como panel de una nueva ventana
		
		panelViajes = new PanelViajes();								
//		
		addTab("Viajes", panelViajes);
//		
		// FIN Creación del panel y lo añadimos como panel de una nueva ventana
		////
		// Creamos el componente personalizado
//		
		JLabel viajesTab = new JLabel("Viajes");
		viajesTab.setPreferredSize(new Dimension(85, 50));
		viajesTab.setFont(Main.FUENTE.deriveFont(15.f));
		viajesTab.setHorizontalAlignment(SwingUtilities.CENTER);
		setTabComponentAt(1, viajesTab);
		
		// FIN Creamos el compontente personalizado
		// FIN Panel Viajes
		////
		// Panel Viaje + Alojamiento
		
		// Creación del panel y lo añadimos como panel de una nueva ventana
		
//		panelViajeAlojamiento = new PanelViajeAlojamiento();			// TODO Clase PanelViajeAlojamiento
//		
//		addTab("Viaje + Alojamiento", panelViajeAlojamiento);
//		
		// FIN Creación del panel y lo añadimos como panel de una nueva ventana
		////
		// Creamos el componente personalizado
//		
//		JLabel viajeAlojamientoTab = new JLabel("Viaje + Alojamiento");
//		viajeAlojamientoTab.setPreferredSize(new Dimension(180, 50));
//		viajeAlojamientoTab.setFont(VentanaPrincipal.fuente.deriveFont(15.f));
//		viajeAlojamientoTab.setHorizontalAlignment(SwingUtilities.CENTER);
//		setTabComponentAt(2, viajeAlojamientoTab);
		
		// FIN Creamos el compontente personalizado
		// FIN Panel Viaje + Alojamiento
		
		setSelectedIndex(0);		// Seleccionamos el primer elemento como "Inicio" (Alojamientos)
		
	}
	
	public static String setError() {
		Component componenteSeleccionado = panelPestanasBusqueda.getSelectedComponent();
		if (componenteSeleccionado instanceof PanelAlojamientos) {
			return ((PanelAlojamientos) componenteSeleccionado).setError();
		} else if (componenteSeleccionado instanceof PanelViajes) {
			return ((PanelViajes) componenteSeleccionado).setError();
//		} else if (componenteSeleccionado instanceof PanelViajeAlojamiento) {
//			return ((PanelViajeAlojamiento) componenteSeleccionado).setError();
		} else {
			return null;
		}
	}
	
	public static void resetAll() {
		
		panelPestanasBusqueda.setSelectedIndex(0);
		
		panelAlojamientos.resetAll();
//		panelViajes.resetAll();
//		panelViajeAlojamiento.resetAll();
		
	}
	
}
