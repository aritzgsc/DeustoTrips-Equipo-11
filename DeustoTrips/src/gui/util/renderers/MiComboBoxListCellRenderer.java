package gui.util.renderers;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import main.Main;

public class MiComboBoxListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);		// Hacemos esto para conseguir el label default que obtendríamos sin editar el método y poder trabajar sobre ella
		
		label.setFont(Main.FUENTE.deriveFont(16.f));
		
		label.setOpaque(true);
		
		return label;
		
	}
	
}
