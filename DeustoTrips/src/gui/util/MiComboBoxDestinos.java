package gui.util;

import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import domain.Destino;
import gui.util.editores.MiComboBoxEditor;
import gui.util.renderers.MiComboBoxListCellRenderer;
import gui.util.uis.MiComboBoxUI;
import main.Main;

public class MiComboBoxDestinos extends JComboBox<Destino> {
	
	private static final long serialVersionUID = 1L;

	public MiComboBoxDestinos(DefaultComboBoxModel<Destino> modelo) {
		
		// Añadimos los datos a partir del modelo de datos recibido
		
		setModel(modelo);
		
		// Personalización del ComboBox
		
		setBorder(Main.DEFAULT_LINE_BORDER);
		setPreferredSize(new Dimension(getWidth(), 50));
		setSelectedIndex(0);
		setToolTipText(getSelectedItem().toString());												// El defaultAns
		
		// Personalización del UI del ComboBox
		
		setUI(new MiComboBoxUI());
		
		// Personalización del item seleccionado
		
		setEditable(true);																			// Hay que hacer que el comboBox sea editable para poder incorporarle un editor correctamente
		
		MiComboBoxEditor editorComboBox = new MiComboBoxEditor();
		setEditor(editorComboBox);
		
		// Personalización del popUp del ComboBox
		
		setRenderer(new MiComboBoxListCellRenderer());
		
	}
	
}
