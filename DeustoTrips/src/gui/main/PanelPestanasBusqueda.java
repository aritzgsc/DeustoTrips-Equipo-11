package gui.main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import gui.main.filtros.FiltroPrecio;
import gui.util.uis.MiTabbedPaneUI;
import main.Main;

// Panel que contiene todas las pestañas de búsqueda (Alojamientos, Viajes y Viaje + Alojamiento) - Parte de la interfaz gráfica diseñada por GEMINI

public class PanelPestanasBusqueda extends JTabbedPane {

	private static final long serialVersionUID = 1L;
		
	// Referencias estáticas
	
	private static PanelPestanasBusqueda panelPestanasBusqueda;
	private static PanelAlojamientos panelAlojamientos;
	private static PanelViajes panelViajes;

	public PanelPestanasBusqueda() {
		
		panelPestanasBusqueda = this;
		
		// Aplicamos la interfaz gráfica creada por gemini para cambiar el estilo del panel

		setUI(new MiTabbedPaneUI());
		
		// Configuración del panel
		
		setBorder(new EmptyBorder(10, 10, 10, 10)); 
		setPreferredSize(new Dimension(1400, 480));
		setFocusable(false);
		setBackground(MiTabbedPaneUI.COLOR_FONDO_APP); 
		
		// FIN Configuración del panel
		////
		// Añadimos las pestañas
		
		// Pestaña de alojamientos
		
		panelAlojamientos = new PanelAlojamientos();
		addTab("Alojamientos", panelAlojamientos);
		setTabComponentAt(0, crearCabeceraTab("Alojamientos", 140));

		// Pestaña de viajes
		
		panelViajes = new PanelViajes();
		addTab("Viajes", panelViajes);
		setTabComponentAt(1, crearCabeceraTab("Viajes", 100));
		
		// Evento para detectar cambios de selección y actualizar la interfaz gráfica
		
		addChangeListener((e) -> {
			actualizarEstiloTextoTabs();
			FiltroPrecio.calcularPrecioMaximo();
		});
		
		setSelectedIndex(0);
		actualizarEstiloTextoTabs();
	}
	
	// Métodos de diseño
	
	private JLabel crearCabeceraTab(String titulo, int ancho) {
		
		JLabel label = new JLabel(titulo);
		label.setPreferredSize(new Dimension(ancho, 40));
		label.setFont(Main.FUENTE.deriveFont(15.f)); 
		label.setHorizontalAlignment(SwingUtilities.CENTER);
		label.setOpaque(false); 
		return label;
		
	}
	
	private void actualizarEstiloTextoTabs() {
		
		for (int i = 0; i < getTabCount(); i++) {
			
			Component c = getTabComponentAt(i);
			
			if (c instanceof JLabel) {
				
				JLabel label = (JLabel) c;
				
				if (i == getSelectedIndex()) {
					
					label.setForeground(MiTabbedPaneUI.COLOR_TEXTO_SELECCIONADO);
					label.setFont(Main.FUENTE.deriveFont(Font.BOLD, 15.f));
					
				} else {
					
					label.setForeground(MiTabbedPaneUI.COLOR_TEXTO_NORMAL);
					label.setFont(Main.FUENTE.deriveFont(Font.PLAIN, 15.f));
					
				}
			}
		}
		
	}
	
	public static PanelPestanasBusqueda getPanelPestanasBusqueda() {
		return panelPestanasBusqueda;
	}
	
	public static String setError() {
		
		Component panelSeleccionado = panelPestanasBusqueda.getSelectedComponent();
		if (panelSeleccionado instanceof PanelAlojamientos) {
			return ((PanelAlojamientos) panelSeleccionado).setError();
		} else if (panelSeleccionado instanceof PanelViajes) {
			return ((PanelViajes) panelSeleccionado).setError();
		} else {
			return null;
		}
		
	}
	
	public static void setError(String error) {
		
		Component panelSeleccionado = panelPestanasBusqueda.getSelectedComponent();
		if (panelSeleccionado instanceof PanelAlojamientos) {
			((PanelAlojamientos) panelSeleccionado).setError(error);
		} else if (panelSeleccionado instanceof PanelViajes) {
			((PanelViajes) panelSeleccionado).setError(error);
		}
		
	}
	
	// Función que nos lleva al punto de inicio de la aplicación
	
	public static void resetAll() {
		
		panelPestanasBusqueda.setSelectedIndex(0);
		
		panelAlojamientos.resetAll();
		panelViajes.resetAll();
		
		PanelResultadosBusqueda.borrarBusqueda();
		
	}
	
}
