package gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import main.Main;

// Panel que contendrá componentes que permitan al usuario introducir información para la búsqueda de Alojamientos

public class PanelAlojamientos extends JPanel {

	private static final long serialVersionUID = 1L;
	
//	private MiSelectorDestino selectorDestino;
//	private MiSelectorMultiplesFechas selectorFechas;
//	private MiSpinnerPersonas spinnerCantidadPersonas;
	
	public PanelAlojamientos() {
		
		// Configuración del panel principal
		
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));
		
		// FIN Configuración del panel principal
		////
		// Panel búsqueda
		
		JPanel panelBusqueda = new JPanel(new GridLayout(1, 3, 0, 0));
		panelBusqueda.setBorder(new EmptyBorder(0, 0, 5, 0));
		
		// Creación de todos los componentes necesarios para la búsqueda
		
//		selectorDestino = new MiSelectorDestino();													// TODO MiSelectorDestino
//		selectorFechas = new MiSelectorMultiplesFechas("Fecha de entrada", "Fecha de salida", 1);	// TODO MiSelectorFecha y MiSelectorMultiplesFechas (Manejará las fechas seleccionadas)
//		spinnerCantidadPersonas = new MiSpinnerPersonas();											// TODO Clase MiSpinnerPersonas
//		BotonBuscar botonBuscar = new BotonBuscar(0);												// TODO Clase BotonBuscar (maneja tipos)
		
		// FIN Creación de todos los componentes necesarios para la búsqueda
		////
		// Separamos esto en 2 para que quede bien
		
		JPanel panelSpinnerBuscar= new JPanel(new GridLayout(1, 2, 5, 0));
		
//		panelSpinnerBuscar.add(spinnerCantidadPersonas);
//		panelSpinnerBuscar.add(botonBuscar);
		
		// Añadimos todos los componentes a sus correspondientes paneles
		
//		panelBusqueda.add(selectorDestino);
//		panelBusqueda.add(selectorFechas);
//		panelBusqueda.add(panelSpinnerBuscar);
		
		// Añadimos el panel de búsqueda al panel principal
		
		add(panelBusqueda, BorderLayout.NORTH);
		
		// FIN Panel búsqueda
		////
		// Panel filtros

		JPanel panelFiltros = new JPanel();
		panelFiltros.setBorder(new EmptyBorder(5, 0, 0, 0));
		panelFiltros.setLayout(new BoxLayout(panelFiltros, BoxLayout.Y_AXIS));
		
		JLabel filtrosLabel = new JLabel("Filtros: ");
		filtrosLabel.setFont(Main.fuente.deriveFont(16.f));
		
//		FiltroPrecio filtroPrecio = new FiltroPrecio();
		
		// TODO Filtro reseñas
//		FiltroResenas filtroResenas = new FiltroResenas();
		
		panelFiltros.add(filtrosLabel);
//		panelFiltros.add(filtroPrecio);
		
		add(panelFiltros/*, BorderLayout.CENTRE*/);
		
		// FIN Panel filtros
		//// 
		// Panel errores
		
		// TODO Se indicará el error en caso de faltar algo
		
		// Panel errores
		
	}
	
	// Función para borrar todos los datos de los componentes que puede tocar el usuario (para PanelVolverRegistrarseIniciarSesion)
	
	public void resetAll() {
//		selectorDestino.resetAll();
//		selectorFechas.resetAll();
//		spinnerCantidadPersonas.resetAll();
		// TODO función resetAll de los filtros
	}
	
}
