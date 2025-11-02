package gui.util;

import java.awt.Dimension;

import javax.swing.JComboBox;

import gui.util.editores.MiComboBoxEditor;
import gui.util.models.MiComboBoxTiposModel;
import gui.util.renderers.MiComboBoxListCellRenderer;
import gui.util.uis.MiComboBoxUI;
import main.Main;

public class MiComboBoxTipos extends JComboBox<String> {
	
	private static final long serialVersionUID = 1L;

	public MiComboBoxTipos() {
		
		// Añadimos los datos a partir del modelo de datos recibido
		
		setModel(new MiComboBoxTiposModel());
		
		// Personalización del ComboBox
		
		setBorder(Main.DEFAULT_LINE_BORDER);
		setPreferredSize(new Dimension(getWidth(), 50));
		setSelectedIndex(0);
		setToolTipText("Tipo");
		
		// Personalización del UI del ComboBox
		
		setUI(new MiComboBoxUI());
		
		// Personalización del item seleccionado
		
		setEditable(true);																			// Hay que hacer que el comboBox sea editable para poder incorporarle un editor correctamente
		
		setEditor(new MiComboBoxEditor());
		
		// Personalización del popUp del ComboBox
		
		setRenderer(new MiComboBoxListCellRenderer());
		
	}
	
}
