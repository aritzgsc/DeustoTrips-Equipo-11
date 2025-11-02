package gui.util.editores;

import java.awt.Color;
import java.awt.Component;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;

import main.Main;

public class MiComboBoxEditor extends BasicComboBoxEditor {

	@Override
	public Component getEditorComponent() {
		
		JTextField editor = (JTextField) super.getEditorComponent();
		
		editor.setBorder(null);
		editor.setFont(Main.FUENTE);
		editor.setBackground(Color.WHITE);
		editor.setHorizontalAlignment(SwingConstants.CENTER);
		editor.setOpaque(true);
		editor.setEditable(false);
		
		return editor;
		
	}
	
}
