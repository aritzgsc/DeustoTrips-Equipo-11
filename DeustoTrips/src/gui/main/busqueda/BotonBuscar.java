package gui.main.busqueda;

import java.awt.Color;

import javax.swing.*;

import gui.main.PanelPestanasBusqueda;
import main.Main;

// Clase que implementa un Botón que realiza la búsqueda según los parámetros 

public class BotonBuscar extends JButton {

	private static final long serialVersionUID = 1L;
	
	public BotonBuscar() {
		
		// Personalización del botón buscar
		
		setText("Buscar");
		setFocusable(false);
		setBackground(Color.WHITE);
		setFont(Main.FUENTE);
		
		// FIN Personalización del botón buscar
		
		addActionListener((e) -> {
			if (PanelPestanasBusqueda.setError().length() != 0) {
				// TODO Funcionalidad (Consulta BD y devolverá una List de Alojamientos y/o Viajes según el tipo del botón (que dependerá del panel en el que esté) ; Se accederá a los parámetros para buscar mediante las variables estáticas que hemos ido creando en las clases )
			}
		});
		
	}
	
}
