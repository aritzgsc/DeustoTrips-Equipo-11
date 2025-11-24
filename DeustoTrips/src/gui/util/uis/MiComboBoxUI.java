package gui.util.uis;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

import main.Main;

public class MiComboBoxUI extends BasicComboBoxUI {

	@Override
	protected JButton createArrowButton() {
		
		JButton botonFlecha = new JButton("▼");
		botonFlecha.setFocusable(false);
		botonFlecha.setBackground(new Color(0xEEEEEE));
		botonFlecha.setBorder(new MatteBorder(0, 1, 0, 0, new Color(0x7A8A99)));
		botonFlecha.setForeground(new Color(0x7A8A99));
		
		botonFlecha.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				botonFlecha.setBorder(new CompoundBorder(new LineBorder(new Color(0xB8CFE5)), new CompoundBorder(Main.DEFAULT_LINE_BORDER, new LineBorder(new Color(0xB8CFE5)))));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				botonFlecha.setBorder(new MatteBorder(0, 1, 0, 0, new Color(0x7A8A99)));
			}
			
		});
		
		// Ajustamos el tamaño de la flecha al tamaño del botón
		
		botonFlecha.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				botonFlecha.setFont(botonFlecha.getFont().deriveFont((long) (botonFlecha.getHeight() / 4)));
			}
		
		});
		
		return botonFlecha;
		
	}
	
}
