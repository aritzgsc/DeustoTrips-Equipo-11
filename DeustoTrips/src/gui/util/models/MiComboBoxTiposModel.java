package gui.util.models;

import java.util.List;

import javax.swing.DefaultComboBoxModel;

public class MiComboBoxTiposModel extends DefaultComboBoxModel<String>{

	private static final long serialVersionUID = 1L;

	private List<String> tiposPosibles = List.of("Ida", "Ida y Vuelta");
	
	public MiComboBoxTiposModel() {
		
		addAll(tiposPosibles);
		
	}
	
}