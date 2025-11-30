package gui.main.busqueda;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;

import gui.main.filtros.FiltroPrecio;
import main.Main;

// Clase que implementa un Spinner personalizado para que muestre un String que indique el número de personas (para obtener el número simplemente haremos un split del texto por " " y get(0))

public class MiSpinnerPersonas extends JSpinner {

	private static final long serialVersionUID = 1L;

	private static final int N_PERSONAS = 9;					// Nº máximo de personas para las que reservar
	
	private String[] listaNPersonas = new String[N_PERSONAS];
	
	public MiSpinnerPersonas() {
		
		// Creación de la lista de Strings que podrá mostrar el Spinner
		
		for (int i = 1; i <= N_PERSONAS; i++) {
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
		
		spinnerTextField.setEditable(true);
		
		// Para que se pueda cambiar el valor con los números del teclado
		
		spinnerTextField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				
				e.consume();
				
				if (Character.toString(e.getKeyChar()).matches("[123456789]")) {
			
					setValue(Integer.parseInt(Character.toString(e.getKeyChar())));
					
				}
				
			}
			
		});
		
		addChangeListener((e) -> FiltroPrecio.calcularPrecioMaximo());
		
	}
	
	public void setValue(int nPersonas) {
		this.setValue(listaNPersonas[nPersonas - 1]);
	}
	
	public int getNPersonas() {
		return Integer.parseInt(((String) this.getValue()).split(" ")[0]);
	}
	
	public void resetAll() {
		this.setValue(listaNPersonas[0]);
	}
	
}
