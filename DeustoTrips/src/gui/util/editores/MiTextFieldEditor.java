package gui.util.editores;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

import gui.util.MiTextField;

public class MiTextFieldEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;
	
	private MiTextField textField;

	// Constructor para la primera columna (nombre)
	
	public MiTextFieldEditor() {
		
		super(new MiTextField());
		
		// Configuración básica
		
		this.textField = (MiTextField) getComponent();
		
		// FIN Configuración básica
		
	}
	
	// Constructor para la segunda columna (Precio / N)
	
	public MiTextFieldEditor(boolean soloNumeros) {
		
		super(new MiTextField());
		
		// Configuración básica con validación
		
		this.textField = (MiTextField) getComponent();
		
		if (soloNumeros) {
			
			this.textField.addKeyListener(new KeyAdapter() {
				
				@Override
				public void keyTyped(KeyEvent e) {

					// Solo permitimos números y el punto decimal
					
					if (!Character.isDigit(e.getKeyChar()) && e.getKeyChar() != '.') {
						e.consume(); 
					}
					
					// Evitar doble punto decimal
					
					if (e.getKeyChar() == '.' && textField.getText().contains(".")) {
						e.consume();
					}
					
				}
				
			});
			
		}
		
		// FIN Configuración básica con validación
		
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		
		// Obtenemos el componente base configurado por Swing
		
		JTextField editor = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
		
		// Configuración de los colores
		// Aplicamos la misma lógica que en el Renderer para que no haya salto visual al editar
		
		Color colorFondo = (row % 2 == 0) ? Color.WHITE : new Color(250, 250, 250);
		editor.setBackground(colorFondo);
		
		editor.setForeground(table.getForeground());
		
		// FIN Configuración de los colores
		
		return editor;
		
	}

}