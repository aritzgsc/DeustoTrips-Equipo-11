package gui.util.renderers;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;

import domain.Destino;

public class MiComboBoxDestinosListCellRenderer extends MiComboBoxListCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		
		JLabel label = (JLabel) super.getListCellRendererComponent(list, ((Destino) value).getNombre(), index, isSelected, cellHasFocus);
		
		label.setIcon(((Destino) value).getBandera());
		
		return label;
		
	}

	
	
}
