package gui.util.models;

import java.util.List;

import javax.swing.DefaultListModel;

import domain.Resena;

public class MiListaResenasModel extends DefaultListModel<Resena>{

	private static final long serialVersionUID = 1L;
	
	public MiListaResenasModel(List<Resena> resenas) {
		
		addAll(resenas);
		
	}
	
}
