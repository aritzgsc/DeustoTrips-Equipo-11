package gui.main;

import java.awt.Dimension;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import domain.Alojamiento;
import domain.Viaje;
import gui.util.PanelAlojamiento;
import gui.util.PanelViaje;
import main.Main;

public class PanelResultadosBusqueda extends JScrollPane {

	private static final long serialVersionUID = 1L;
	
	private static PanelResultadosBusqueda panelResultadosBusqueda;
	private static JPanel panelResultados;
	
	public PanelResultadosBusqueda() {
		
		getVerticalScrollBar().setUnitIncrement(16);	// Para que sea más agradable scrollear
		
		panelResultadosBusqueda = this;
		
		setMinimumSize(new Dimension(0, 400));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
		setPreferredSize(new Dimension(0, 0));
		setBorder(new CompoundBorder(new EmptyBorder(10, 20, 20, 20), Main.DEFAULT_LINE_BORDER));
		
		// Configuración del panel principal (contendrá todos los resultados encontrados)
		
		panelResultados = new JPanel();
		panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
		
		// Metemos el panel principal al scrollPane
		
		setViewportView(panelResultados);
		
	}
	
	// Función para borrar las búsquedas realizadas (también cambia el tamaño de la ventana)
	
	public static void borrarBusqueda() {
		
		panelResultados = new JPanel();
		panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
		
		panelResultadosBusqueda.setViewportView(panelResultados);
		
		panelResultadosBusqueda.setPreferredSize(new Dimension(0, 0));
		
		if (VentanaPrincipal.getVentanaPrincipal().getSize().equals(VentanaPrincipal.getVentanaPrincipal().getMinimumSize())) {
			
			VentanaPrincipal.getVentanaPrincipal().setMinimumSize(new Dimension(1440, 520));
			VentanaPrincipal.getVentanaPrincipal().setSize(new Dimension(1440, 520));	
			
		} else {
			
			VentanaPrincipal.getVentanaPrincipal().setMinimumSize(new Dimension(1440, 520));
			
		}
		
		VentanaPrincipal.getVentanaPrincipal().revalidate();
		VentanaPrincipal.getVentanaPrincipal().repaint();
		
	}
	
	// Función para añadir un alojamiento (en forma de PanelAlojamiento) a las búsquedas realizadas (también cambia el tamaño de la ventana)
	
	public static PanelAlojamiento anadirAlojamientoEncontrado(Alojamiento alojamientoEncontrado, LocalDate fechaEntrada, LocalDate fechaSalida, int nPersonas) {
		
		// Guardamos la posición original de la scrollbar
		
		int posicionInicial = panelResultadosBusqueda.getVerticalScrollBar().getValue();
		
		// Añadimos el alojamiento
		
		int nNoches = (int) ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
		
		PanelAlojamiento panelAlojamiento = new PanelAlojamiento(alojamientoEncontrado, nPersonas, fechaEntrada, fechaSalida, alojamientoEncontrado.calcularPrecio(nPersonas, nNoches), -1, null, PanelAlojamiento.MODO_RESERVAR);
		
		panelResultados.add(panelAlojamiento);
		
		panelResultadosBusqueda.setPreferredSize(new Dimension(0, 400));
		
		VentanaPrincipal.getVentanaPrincipal().setMinimumSize(new Dimension(1440, 920));
		
		panelResultadosBusqueda.revalidate();
		panelResultadosBusqueda.repaint();
		
		VentanaPrincipal.getVentanaPrincipal().revalidate();
		VentanaPrincipal.getVentanaPrincipal().repaint();
		
		// Reestablecemos la posicion inicial 
		
		SwingUtilities.invokeLater(() -> panelResultadosBusqueda.getVerticalScrollBar().setValue(posicionInicial));
		
		// Devolvemos el panel para jugar con el en el botón buscar
		
		return panelAlojamiento;
		
	}
	
	// Función para añadir un viaje (en forma de PanelViaje) a las búsquedas realizadas (también cambia el tamaño de la ventana)
	
	public static void anadirViajeEncontrado(List<Viaje> viajesIda, List<Viaje> viajesVuelta, LocalDate fechaSalidaIda, LocalDate fechaSalidaVuelta, int nPersonas) {
		
		// Guardamos la posición original de la scrollbar
		
		int posicionInicial = panelResultadosBusqueda.getVerticalScrollBar().getValue();
		
		// Añadimos el viaje
		
		PanelViaje panelViaje = null;
		
		if (viajesVuelta == null || viajesVuelta.isEmpty() || fechaSalidaVuelta == null) {
			
			panelViaje = new PanelViaje(0, viajesIda, fechaSalidaIda, nPersonas, PanelViaje.MODO_RESERVAR);
			
		} else {
			
			panelViaje = new PanelViaje(0, viajesIda, viajesVuelta, fechaSalidaIda, fechaSalidaVuelta, nPersonas, PanelViaje.MODO_RESERVAR);
			
		}
		
		panelResultados.add(panelViaje);
		
		panelResultadosBusqueda.setPreferredSize(new Dimension(0, 400));
		
		VentanaPrincipal.getVentanaPrincipal().setMinimumSize(new Dimension(1440, 920));
		
		panelResultadosBusqueda.revalidate();
		panelResultadosBusqueda.repaint();
		
		VentanaPrincipal.getVentanaPrincipal().revalidate();
		VentanaPrincipal.getVentanaPrincipal().repaint();
		
		// Reestablecemos la posicion inicial 
		
		SwingUtilities.invokeLater(() -> panelResultadosBusqueda.getVerticalScrollBar().setValue(posicionInicial));
		
	}
	
}
