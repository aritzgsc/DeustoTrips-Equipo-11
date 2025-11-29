package gui.main;

import java.awt.Dimension;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import domain.Alojamiento;
import gui.util.PanelAlojamiento;
import main.Main;

public class PanelResultadosBusqueda extends JScrollPane {

	private static final long serialVersionUID = 1L;
	
	private static PanelResultadosBusqueda panelResultadosBusqueda;
	private static JPanel panelResultados;
	
	public PanelResultadosBusqueda() {
		
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
	
	public static void borrarBusqueda() {
		
		panelResultados = new JPanel();
		panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
		
		panelResultadosBusqueda.setViewportView(panelResultados);
		
		panelResultadosBusqueda.setPreferredSize(new Dimension(0, 0));
		
		VentanaPrincipal.getVentanaPrincipal().setMinimumSize(new Dimension(1440, 500));
		
		if (VentanaPrincipal.getVentanaPrincipal().getSize().equals(VentanaPrincipal.getVentanaPrincipal().getMinimumSize())) {
			VentanaPrincipal.getVentanaPrincipal().setSize(new Dimension(1440, 500));		
		}
		
		VentanaPrincipal.getVentanaPrincipal().revalidate();
		VentanaPrincipal.getVentanaPrincipal().repaint();
		
	}
	
	public static void anadirAlojamientoEncontrado(Alojamiento alojamientoEncontrado, LocalDate fechaEntrada, LocalDate fechaSalida, int nPersonas) {
		
		// Guardamos la posición original de la scrollbar
		
		int posicionInicial = panelResultadosBusqueda.getVerticalScrollBar().getValue();
		
		// Añadimos el alojamiento
		
		int nNoches = (int) ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
		
		PanelAlojamiento panelAlojamiento = new PanelAlojamiento(alojamientoEncontrado, nPersonas, fechaEntrada, fechaSalida, alojamientoEncontrado.calcularPrecio(nPersonas, nNoches), -1, null, PanelAlojamiento.MODO_RESERVAR);
		
		panelResultados.add(panelAlojamiento);
		
		panelResultadosBusqueda.setPreferredSize(new Dimension(0, Math.min(400, 380 * panelResultados.getComponents().length)));
		
		VentanaPrincipal.getVentanaPrincipal().setMinimumSize(new Dimension(1440, 500 + Math.min(500, 380 * panelResultados.getComponents().length)));
		
		panelResultadosBusqueda.revalidate();
		panelResultadosBusqueda.repaint();
		
		VentanaPrincipal.getVentanaPrincipal().revalidate();
		VentanaPrincipal.getVentanaPrincipal().repaint();
		
		// Reestablecemos la posicion inicial 
		
		SwingUtilities.invokeLater(() -> panelResultadosBusqueda.getVerticalScrollBar().setValue(posicionInicial));
		
	}
	
}
