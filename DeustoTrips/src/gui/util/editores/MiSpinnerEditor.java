package gui.util.editores;

import java.awt.Color;
import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import gui.main.busqueda.MiSpinnerPersonas;

// No extendemos DefaultCellEditor porque JSpinner no puede entonces nos hacemos nuestro propio editor "de 0"

public class MiSpinnerEditor extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = 1L;

	private MiSpinnerPersonas spinner;
	
	public MiSpinnerEditor() {
		
		// Configuración básica del editor
		
		spinner = new MiSpinnerPersonas();
		spinner.setBorder(null);
		
		spinner.addChangeListener((e) -> fireEditingStopped());
		
		// FIN Configuración básica del editor
		JTextField editorInterno = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
		
		// Al pulsar Enter o salir del editor este, avisamos a la tabla de que hemos terminado de editar
		
		editorInterno.addActionListener((e) -> fireEditingStopped());
		
	}

	@Override
	public Object getCellEditorValue() {
		
		return spinner.getNPersonas();
		
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		
		// Ponemos el valor inicial al empezar a editar
		
		if (value instanceof Integer) {
			spinner.setValue((int) value);
		}
		
		// FIN Seteamos el valor inicial
		////
		// Configuración del color (misma lógica de zebreado que en el renderer)
		
		Color colorFondo;
		Color colorTexto;
			
		colorFondo = (row % 2 == 0) ? Color.WHITE : new Color(250, 250, 250);
		colorTexto = table.getForeground();
		
		spinner.setBackground(colorFondo);
		
		JTextField editorInterno = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
		editorInterno.setBackground(colorFondo);
		editorInterno.setForeground(colorTexto);
		
		// FIN Configuración del estilo visual
		
		return spinner;
		
	}
	
}