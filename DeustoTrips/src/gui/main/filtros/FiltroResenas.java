package gui.main.filtros;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;

import main.Main;

public class FiltroResenas extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JCheckBox checkBoxResenas;
	private PanelSelectorResena panelSelectorResena;
	
	public FiltroResenas() {

		// Configuración del panel
		
		setAlignmentX(LEFT_ALIGNMENT);
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
				
		// FIN Configuración del panel
		////
		// Creación del botón que controlará si aparece o desaparece el filtro
				
		checkBoxResenas = new JCheckBox(" Nota media mínima: ");
		checkBoxResenas.setPreferredSize(new Dimension(200, 50));
		checkBoxResenas.setBorder(Main.DEFAULT_LINE_BORDER);
		checkBoxResenas.setFont(Main.FUENTE);
		checkBoxResenas.setFocusable(false);
				
		// FIN Creación del botón que controlará si parece o desaparece el filtro
		// Implementación del funcionamiento de checkbox: Si se selecciona la checkBox se hace visible el filtro, si no, se hace invisible
		
		checkBoxResenas.addActionListener((e) -> {	
					
			if (checkBoxResenas.isSelected()) {
						
				panelSelectorResena.setVisible(true);
						
			} else {
						
				panelSelectorResena.setVisible(false);
						
			}
					
		});
				
		// FIN Implementación del funcionamiento de checkbox
		////
		// Creación y personalización de los componentes que irán dentro del panel
		
		panelSelectorResena = new PanelSelectorResena(true);
		panelSelectorResena.setVisible(false);
		
		// Añadimos todo al panel principal
		
		add(checkBoxResenas);
		add(panelSelectorResena);
		
	}
	
	public double getValor() {
		return panelSelectorResena.getValor();
	}
	
	public void resetAll() {
		checkBoxResenas.setSelected(false);
		panelSelectorResena.resetAll();
		panelSelectorResena.setVisible(false);
	}
	
}
