package gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import domain.Ciudad;
import domain.Destino;
import domain.Pais;
import gui.main.busqueda.BotonBuscar;
import gui.main.busqueda.MiSelectorDestino;
import gui.main.busqueda.MiSelectorMultiplesFechas;
import gui.main.busqueda.MiSpinnerPersonas;
import gui.main.filtros.FiltroPrecio;
import gui.main.filtros.FiltroResenas;
import gui.main.filtros.FiltroTipoViaje;
import main.Main;

// Panel que contendrá componentes que permitan al usuario introducir información para la búsqueda de Alojamientos

public class PanelViajeAlojamiento extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private MiSelectorDestino selectorDestino;
	private MiSelectorMultiplesFechas selectorFechas;
	private MiSpinnerPersonas spinnerCantidadPersonas;
	
	private FiltroPrecio filtroPrecio;
	private FiltroTipoViaje filtroTipoViaje;
	private FiltroResenas filtroResenas;
	
	private JLabel error;
	
	public PanelViajeAlojamiento() {
		
		// Configuración del panel principal
		
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));
		
		// FIN Configuración del panel principal
		// Creación del contenedor del panel búsqueda y error
		
		JPanel panelBusquedaError = new JPanel(new GridLayout(2, 1, 10, 0));
		
		// FIN Creación del contenedor del panel búsqueda y error
		////
		// Panel búsqueda
		
		JPanel panelBusqueda = new JPanel(new GridLayout(1, 3, 2, 0));
		
		// Creación de todos los componentes necesarios para la búsqueda
		
		selectorDestino = new MiSelectorDestino("¿A dónde vas?", Pais.class, Ciudad.class);
		selectorFechas = new MiSelectorMultiplesFechas("Fecha de entrada", "Fecha de salida", 1);			// Lo ponemos a 1 porque en un alojamiento mínimo hay que estar 1 noche (no puedes estar 0 noches)
		spinnerCantidadPersonas = new MiSpinnerPersonas();
		BotonBuscar botonBuscar = new BotonBuscar();
		
		// FIN Creación de todos los componentes necesarios para la búsqueda
		////
		// Separamos esto en 2 para que quede bien
		
		JPanel panelSpinnerBuscar= new JPanel(new GridLayout(1, 2, 6, 0));
		
		panelSpinnerBuscar.add(spinnerCantidadPersonas);
		panelSpinnerBuscar.add(botonBuscar);
		
		// Añadimos todos los componentes a sus correspondientes paneles
		
		panelBusqueda.add(selectorDestino);
		panelBusqueda.add(selectorFechas);
		panelBusqueda.add(panelSpinnerBuscar);
		
		// Añadimos el panel de búsqueda al panel correspondiente
		
		panelBusquedaError.add(panelBusqueda);
		
		// FIN Panel búsqueda
		////
		// Panel filtros (SplitPane porque hay muchos filtros y para que quede bien)

		JSplitPane panelFiltros = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		panelFiltros.setBorder(null);
		panelFiltros.setDividerSize(0);
		SwingUtilities.invokeLater(() -> panelFiltros.setDividerLocation((int) (panelFiltros.getWidth() / 2)));
		
		// Panel izquierdo
		
		JPanel panelFiltrosIzq = new JPanel();
		panelFiltrosIzq.setLayout(new BoxLayout(panelFiltrosIzq, BoxLayout.Y_AXIS));
		
		JLabel filtrosLabel = new JLabel("Filtros: ");
		filtrosLabel.setFont(Main.FUENTE.deriveFont(16.f));
		
		filtroPrecio = new FiltroPrecio();
		filtroResenas = new FiltroResenas();
		
		panelFiltrosIzq.add(filtrosLabel);
		panelFiltrosIzq.add(filtroPrecio);
		panelFiltrosIzq.add(filtroResenas);
		
		// FIN Panel izquierdo
		////
		// Panel derecho
		
		JPanel panelFiltrosDcha = new JPanel();
		panelFiltrosDcha.setLayout(new BoxLayout(panelFiltrosDcha, BoxLayout.Y_AXIS));
		
		filtroTipoViaje = new FiltroTipoViaje();
		
		panelFiltrosDcha.add(Box.createVerticalStrut(30));
		panelFiltrosDcha.add(Box.createVerticalGlue());
		panelFiltrosDcha.add(filtroTipoViaje);
		panelFiltrosDcha.add(Box.createVerticalGlue());
		
		// FIN Panel derecho
		////
		// Añadimos una funcionalidad para que al redimensionar la ventana el divider quede en el centro

		VentanaPrincipal.getVentanaPrincipal().addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				
				panelFiltros.setDividerLocation((int) (panelFiltros.getWidth() / 2));
				
			}
			
		});
		
		// FIN Añadimos una funcionalidad para que al redimensionar la ventana el divider quede en el centro
		// Añadimos los paneles a su panel correspondiente
		
		panelFiltros.setLeftComponent(panelFiltrosIzq);
		panelFiltros.setRightComponent(panelFiltrosDcha);
		
		add(panelFiltros/*, BorderLayout.CENTRE*/);
		
		// FIN Panel filtros
		//// 
		// Panel errores
		
		error = new JLabel();
		error.setFont(Main.FUENTE.deriveFont(Font.ITALIC));
		error.setForeground(Color.RED);
		error.setHorizontalAlignment(SwingConstants.CENTER);
		
		panelBusquedaError.add(error);
		
		// Panel errores
		// Añadimos el panel de búsqueda y errores al panel principal
		
		add(panelBusquedaError, BorderLayout.NORTH);
		
	}
	
	// Función para borrar todos los datos de los componentes que puede tocar el usuario (para PanelVolverRegistrarseIniciarSesion)
	
	public void resetAll() {
		selectorDestino.resetAll();
		selectorFechas.resetAll();
		spinnerCantidadPersonas.resetAll();

		filtroPrecio.resetAll();
		filtroTipoViaje.resetAll();
		filtroResenas.resetAll();
		
		error.setText("");
	}
	
	// Función para obtener el error
	
	public String setError() {
		
		String errorStr = ("<html>" + (selectorDestino.getDestinoSeleccionado() == null || selectorDestino.getDestinoSeleccionado().isDefaultAns()? "Seleccione un destino válido, " : "") +
						   (selectorFechas.getSelectorFecha1().getDate() == null || selectorFechas.getSelectorFecha2().getDate() == null? "Seleccione un rango de fechas correcto, " : "") + "</html>"
				           ).replaceAll("(?<=, )Seleccione ", "").replaceAll(",(?=([^,]*$))", "").replaceAll(",(?=([^,]*$))", " y");
		
		error.setText(errorStr);
		
		if (errorStr.equals("<html></html>")) {
			return "";
		} else {
			return errorStr;
		}
				
	}
	
	public void setError(String errorStr) {
		error.setText(errorStr);
	}
	
	public Destino getDestinoSeleccionado() {
		return selectorDestino.getDestinoSeleccionado();
	}
	
	public LocalDate getFechaEntrada() {
		if (selectorFechas.getSelectorFecha1().getDate() == null) {
			return null;
		}
		return LocalDate.ofInstant(selectorFechas.getSelectorFecha1().getDate().toInstant(), ZoneId.systemDefault());
	}
	
	public LocalDate getFechaSalida() {
		if (selectorFechas.getSelectorFecha2().getDate() == null) {
			return null;
		}
		return LocalDate.ofInstant(selectorFechas.getSelectorFecha2().getDate().toInstant(), ZoneId.systemDefault());
	}
	
	public int getNPersonas() {
		return spinnerCantidadPersonas.getNPersonas();
	}
	
	public int getNNoches() {
		if (getFechaEntrada() == null || getFechaSalida() == null) {
			return 1;
		}
		return (int) ChronoUnit.DAYS.between(getFechaEntrada(), getFechaSalida());
	}
	
	public int getPrecioMin() {
		return filtroPrecio.getLowValue();
	}
	
	public int getPrecioMax() {
		return filtroPrecio.getHighValue();
	}
	
	public double getValoracionMin() {
		return filtroResenas.getValor();
	}
	
}