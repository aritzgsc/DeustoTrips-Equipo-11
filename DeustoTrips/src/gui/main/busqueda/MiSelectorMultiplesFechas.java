package gui.main.busqueda;

import java.awt.GridLayout;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.*;

public class MiSelectorMultiplesFechas extends JPanel {

	private static final long serialVersionUID = 1L;

	MiSelectorFecha selectorFecha1;						// Tendremos que sacarlos del constructor para posteriormente poder jugar con ellos a la hora de implementar la funcionalidad del botonBuscar
	MiSelectorFecha selectorFecha2;						// Tendremos que sacarlos del constructor para posteriormente poder jugar con ellos a la hora de implementar la funcionalidad del botonBuscar
	
	public MiSelectorMultiplesFechas(String textoInicial1, String textoInicial2, int minDias) {
		
		// Configuracion del panel que contendrá los selectores de fecha
		
		setLayout(new GridLayout(1, 2, 2, 0));
		setBorder(null);
		
		// FIN Configuracion del panel que contendrá los selectores de fecha
		////
		// Creación de los selectores de fecha
		
		selectorFecha1 = new MiSelectorFecha(textoInicial1);
		selectorFecha2 = new MiSelectorFecha(textoInicial2);
		
		// FIN Creación de los selectores de fecha
		// Para la lógica de su funcionamiento, aparte de tener unas fechas mínimas y máximas prestablecidas (por el constructor de nuestra clase) también tendremos que evitar que el selectorFecha1 tenga una fecha mayor que el selectorFecha2 o que el selectorFecha2 tenga una fecha menor que el selectorFecha1
		
		selectorFecha1.addPropertyChangeListener("date", (e) -> {
			if (selectorFecha1.getDate() != null) {
				selectorFecha2.setMinSelectableDate(Date.from(selectorFecha1.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(minDias).atStartOfDay(ZoneId.systemDefault()).toInstant()));		// Ponemos de fecha mínima la fecha seleccionada + el mínimo de días que dura el tipo de reserva
			} else {
				selectorFecha2.setMinSelectableDate(MiSelectorFecha.getFechaMinima());
			}
		});
		
		selectorFecha2.addPropertyChangeListener("date", (e) -> {
			if (selectorFecha2.getDate() != null) {
				selectorFecha1.setMaxSelectableDate(Date.from(selectorFecha2.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(-minDias).atStartOfDay(ZoneId.systemDefault()).toInstant()));		// Ponemos de fecha máxima la fecha seleccionada - el mínimo de días que dura el tipo de reserva
				} else {
				selectorFecha1.setMaxSelectableDate(MiSelectorFecha.getFechaMinima());
			}
		});
		
		// FIN Lógica de funcionamiento
		// Añadimos los selectores al panel
		
		add(selectorFecha1);
		add(selectorFecha2);
		
	}

	// Getters de los selectores de fecha
	
	public MiSelectorFecha getSelectorFecha1() {
		return selectorFecha1;
	}

	public MiSelectorFecha getSelectorFecha2() {
		return selectorFecha2;
	}
	
	// FIN Getters de los selectores de fecha
	////
	// Método que nos permite resetear todos los valores del panel de fechas
	
	public void resetAll() {
		selectorFecha1.resetAll();;
		selectorFecha2.resetAll();;
	}
	
}
