package gui.main.busqueda;

import java.awt.GridLayout;

import javax.swing.*;

public class MiSelectorMultiplesDestinos extends JPanel {

	private static final long serialVersionUID = 1L;

	MiSelectorDestino selectorDestino1;						// Tendremos que sacarlos del constructor para posteriormente poder jugar con ellos a la hora de implementar la funcionalidad del botonBuscar
	MiSelectorDestino selectorDestino2;						// Tendremos que sacarlos del constructor para posteriormente poder jugar con ellos a la hora de implementar la funcionalidad del botonBuscar
	
	public MiSelectorMultiplesDestinos(String defaultAns1, String defaultAns2) {
		
		// Configuracion del panel que contendrá los selectores de fecha
		
		setLayout(new GridLayout(1, 2, 2, 0));
		setBorder(null);
		
		// FIN Configuracion del panel que contendrá los selectores de fecha
		////
		// Creación de los selectores de fecha
		
		selectorDestino1 = new MiSelectorDestino(defaultAns1);
		selectorDestino2 = new MiSelectorDestino(defaultAns2);
		
		// Añadimos los selectores al panel
		
		add(selectorDestino1);
		add(selectorDestino2);
		
	}

	// Getters de los selectores de fecha
	
	public MiSelectorDestino getSelectorDestino1() {
		return selectorDestino1;
	}

	public MiSelectorDestino getSelectorDestino2() {
		return selectorDestino2;
	}
	
	// FIN Getters de los selectores de fecha
	////
	// Método que nos permite resetear todos los valores del panel de fechas
	
	public void resetAll() {
		selectorDestino1.resetAll();;
		selectorDestino2.resetAll();;
	}
	
}
