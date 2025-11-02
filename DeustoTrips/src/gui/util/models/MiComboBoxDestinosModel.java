package gui.util.models;

import java.util.List;

import javax.swing.DefaultComboBoxModel;

import domain.Destino;

public class MiComboBoxDestinosModel extends DefaultComboBoxModel<Destino>{

	private static final long serialVersionUID = 1L;

	public MiComboBoxDestinosModel(List<Destino> destinosPosibles) {
		
		addAll(destinosPosibles);
		
	}
	
}
