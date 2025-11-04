package gui.main;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import gui.main.busqueda.*;
import gui.main.filtros.*;
import main.Main;

// Panel que contendrá componentes que permitan al usuario introducir información para la búsqueda de Viajes

public class PanelViajes extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private MiSelectorMultiplesDestinos selectorMultiplesDestinos;
	private MiSelectorTipo selectorTipo;
	private MiSelectorFecha selectorFecha;
	private MiSelectorMultiplesFechas selectorFechas;
	private MiSpinnerPersonas spinnerCantidadPersonas;
	
	private FiltroPrecio filtroPrecio;
	private FiltroTipoViaje filtroTipoViaje;	
	
	private JLabel error;
	
	public PanelViajes() {
		
		// Configuración del panel principal
		
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));
		
		// FIN Configuración del panel principal
		// Creación del contenedor del panel búsqueda y error
		
		JPanel panelBusquedaError = new JPanel(new GridLayout(2, 1, 10, 0));
		
		// FIN Creación del contenedor del panel búsqueda y error
		////
		// Panel búsqueda
		
		JPanel panelBusqueda = new JPanel(new GridLayout(1, 4, 2, 0));
		
		// Creación de todos los componentes necesarios para la búsqueda
		
		selectorMultiplesDestinos = new MiSelectorMultiplesDestinos("Origen", "Destino");
		selectorTipo = new MiSelectorTipo();
		selectorFecha = new MiSelectorFecha("Fecha de ida");			
		selectorFechas = new MiSelectorMultiplesFechas("Fecha de ida", "Fecha de vuelta", 0);			// Lo ponemos a 0 porque en un viaje no hay mínimo de tiempo (se puede ir y volver el mismo día)
		spinnerCantidadPersonas = new MiSpinnerPersonas();
		BotonBuscar botonBuscar = new BotonBuscar();
		
		// Implementación de la funcionalidad del selector de tipo de viaje
		
		JComboBox<String> comboBoxTipo = selectorTipo.getComboBox();
		comboBoxTipo.addActionListener((e) -> {
			if (comboBoxTipo.getSelectedItem().equals("Ida")) {
				panelBusqueda.remove(selectorFechas);
				panelBusqueda.add(selectorFecha, 2);
			} else if (comboBoxTipo.getSelectedItem().equals("Ida y Vuelta")) {
				panelBusqueda.remove(selectorFecha);
				panelBusqueda.add(selectorFechas, 2);
			}
			revalidate();
			repaint();
		});
		
		// FIN Implementación de la funcionalidad del selector de tipo de viaje
		// FIN Creación de todos los componentes necesarios para la búsqueda
		////
		// Separamos esto en 2 para que quede bien
		
		JPanel panelSpinnerBuscar= new JPanel(new GridLayout(1, 2, 6, 0));
		
		panelSpinnerBuscar.add(spinnerCantidadPersonas);
		panelSpinnerBuscar.add(botonBuscar);
		
		// Añadimos todos los componentes a sus correspondientes paneles
		
		panelBusqueda.add(selectorMultiplesDestinos);
		panelBusqueda.add(selectorTipo);
		panelBusqueda.add(selectorFecha);
		panelBusqueda.add(panelSpinnerBuscar);
		
		// Añadimos el panel de búsqueda al panel correspondiente
		
		panelBusquedaError.add(panelBusqueda);
		
		// FIN Panel búsqueda
		////
		// Panel filtros

		JPanel panelFiltros = new JPanel();
		panelFiltros.setLayout(new BoxLayout(panelFiltros, BoxLayout.Y_AXIS));
		
		JLabel filtrosLabel = new JLabel("Filtros: ");
		filtrosLabel.setFont(Main.FUENTE.deriveFont(16.f));
		
		filtroPrecio = new FiltroPrecio();
		
		filtroTipoViaje = new FiltroTipoViaje();
		
		panelFiltros.add(filtrosLabel);
		panelFiltros.add(filtroPrecio);
		panelFiltros.add(filtroTipoViaje);
		
		add(panelFiltros/*, BorderLayout.CENTRE*/);
		
		// FIN Panel filtros
		//// 
		// Panel errores
		
		error = new JLabel();
		error.setFont(Main.FUENTE);
		error.setForeground(Color.RED);
		error.setHorizontalAlignment(SwingConstants.CENTER);
		
		panelBusquedaError.add(error);
		
		// Panel errores
		// Añadimos el panel de búsqueda y errores al panel principal
		
		add(panelBusquedaError, BorderLayout.NORTH);
		
	}
	
	// Función para borrar todos los datos de los componentes que puede tocar el usuario (para PanelVolverRegistrarseIniciarSesion)
	
	public void resetAll() {
		selectorMultiplesDestinos.resetAll();
		selectorTipo.resetAll();
		selectorFecha.resetAll();
		selectorFechas.resetAll();
		spinnerCantidadPersonas.resetAll();

		filtroPrecio.resetAll();
		filtroTipoViaje.resetAll();
		
		error.setText("");
	}
	
	// Función para obtener el error
	
	public String setError() {
		
		String errorStr = ((selectorMultiplesDestinos.getSelectorDestino1().getDestinoSeleccionado() == null || selectorMultiplesDestinos.getSelectorDestino2().getDestinoSeleccionado() == null ||
						   selectorMultiplesDestinos.getSelectorDestino1().getDestinoSeleccionado().isDefaultAns() || selectorMultiplesDestinos.getSelectorDestino2().getDestinoSeleccionado().isDefaultAns() ||
						   selectorMultiplesDestinos.getSelectorDestino1().getDestinoSeleccionado().equals(selectorMultiplesDestinos.getSelectorDestino2().getDestinoSeleccionado())? "Seleccione unos destinos válidos, " : "") +
						  ((selectorTipo.getComboBox().getSelectedItem().equals("Ida")) && selectorFecha.getDate() == null? "Seleccione una fecha válida, " : "") +
						  ((selectorTipo.getComboBox().getSelectedItem().equals("Ida y Vuelta")) && (selectorFechas.getSelectorFecha1().getDate() == null || selectorFechas.getSelectorFecha2().getDate() == null)? "Seleccione un rango de fechas correcto, " : "")
				).replaceAll("(?<=, )Seleccione ", "").replaceAll(",(?=([^,]*$))", "").replaceAll(",(?=([^,]*$))", " y");
		
		error.setText(errorStr);
		
		return errorStr;
				
	}
	
}
