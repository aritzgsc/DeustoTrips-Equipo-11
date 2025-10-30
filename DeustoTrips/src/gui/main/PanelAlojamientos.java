package gui.main;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import gui.main.busqueda.*;
import gui.main.filtros.*;
import main.Main;

// Panel que contendrá componentes que permitan al usuario introducir información para la búsqueda de Alojamientos

public class PanelAlojamientos extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private MiSelectorDestino selectorDestino;
	private MiSelectorMultiplesFechas selectorFechas;
	private MiSpinnerPersonas spinnerCantidadPersonas;
	
	private FiltroPrecio filtroPrecio;
//	private FiltroResenas filtroResenas;
	
	private JLabel error;
	
	public PanelAlojamientos() {
		
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
		
		selectorDestino = new MiSelectorDestino("¿A dónde vas?");
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
		// Panel filtros

		JPanel panelFiltros = new JPanel();
		panelFiltros.setLayout(new BoxLayout(panelFiltros, BoxLayout.Y_AXIS));
		
		JLabel filtrosLabel = new JLabel("Filtros: ");
		filtrosLabel.setFont(Main.FUENTE.deriveFont(16.f));
		
		filtroPrecio = new FiltroPrecio();
		
//		filtroResenas = new FiltroResenas();
		
		panelFiltros.add(filtrosLabel);
		panelFiltros.add(filtroPrecio);
//		panelFiltros.add(filtroResenas);
		
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
		selectorDestino.resetAll();
		selectorFechas.resetAll();
		spinnerCantidadPersonas.resetAll();

		filtroPrecio.resetAll();
//		filtroResenas.resetAll();
		
		error.setText("");
	}
	
	// Función para obtener el error
	
	public String setError() {
		
		String errorStr = ((selectorDestino.getDestinoSeleccionado() == null || selectorDestino.getDestinoSeleccionado().isDefaultAns()? "Seleccione un destino válido, " : "") +
						   (selectorFechas.getSelectorFecha1().getDate() == null || selectorFechas.getSelectorFecha2().getDate() == null? "Seleccione un rango de fechas correcto, " : "")
				           ).replaceAll("(?<=, )Seleccione ", "").replaceAll(",(?=([^,]*$))", "").replaceAll(",(?=([^,]*$))", " y");
		
		error.setText(errorStr);
		
		return errorStr;
				
	}
	
}
