package gui.main.busqueda;

import java.awt.Color;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;

import main.Main;

// Clase que implementa un Spinner personalizado para que muestre un String que indique el número de personas (para obtener el número simplemente haremos un split del texto por " " y get(0))

public class MiSpinnerPersonas extends JSpinner {

	private static final long serialVersionUID = 1L;

	private static final int nPersonas = 9;					// Nº máximo de personas para las que reservar
	
	public MiSpinnerPersonas() {
		
		// Creación de la lista de Strings que podrá mostrar el Spinner
		
		String[] listaNPersonas = new String[nPersonas];
		
		for (int i = 1; i <= nPersonas; i++) {
			listaNPersonas[i - 1] = i == 1? i + " persona" : i + " personas";
		}
		
		// FIN Creación de la lista
		////
		// Asignamos el modelo y personalizamos el Spinner
		
		setModel(new SpinnerListModel(listaNPersonas));
		
		setBorder(Main.DEFAULT_LINE_BORDER);
		
		JTextField spinnerTextField = ((JSpinner.DefaultEditor) getEditor()).getTextField(); 	// Conseguimos el TextField del spinner para hacer con el lo que queramos
		
		spinnerTextField.setHorizontalAlignment(JTextField.CENTER);
		spinnerTextField.setFont(Main.FUENTE);
		spinnerTextField.setBackground(Color.WHITE);
		spinnerTextField.setCaretColor(spinnerTextField.getBackground());
		
		spinnerTextField.setEditable(false);
		
		// TODO Que SI sea editable y el usuario pueda escribir un número del 1 al nPersonas y se autocomplete (que solo pueda escribir 1 número)
		
	}
	
	public void resetAll() {
		this.setValue("1 persona");
	}
	
}
