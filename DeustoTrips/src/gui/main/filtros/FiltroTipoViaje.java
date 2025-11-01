package gui.main.filtros;

import java.awt.*;

import javax.swing.*;

import main.Main;

public class FiltroTipoViaje extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JCheckBox checkBoxTipo;
	private ButtonGroup grupo;
	private JPanel panelSelectorTipo;
	
	public FiltroTipoViaje() {

		// Configuración del panel
		
		setAlignmentX(LEFT_ALIGNMENT);
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
				
		// FIN Configuración del panel
		////
		// Creación del botón que controlará si aparece o desaparece el filtro
				
		checkBoxTipo = new JCheckBox(" Tipo de viaje: ");
		checkBoxTipo.setPreferredSize(new Dimension(200, 50));
		checkBoxTipo.setBorder(Main.DEFAULT_LINE_BORDER);
		checkBoxTipo.setFont(Main.FUENTE);
		checkBoxTipo.setFocusable(false);
				
		// FIN Creación del botón que controlará si parece o desaparece el filtro
		// Implementación del funcionamiento de checkbox: Si se selecciona la checkBox se hace visible el filtro, si no, se hace invisible
		
		checkBoxTipo.addActionListener((e) -> {	
					
			if (checkBoxTipo.isSelected()) {
						
				panelSelectorTipo.setVisible(true);
						
			} else {
						
				panelSelectorTipo.setVisible(false);
						
			}
					
		});
				
		// FIN Implementación del funcionamiento de checkbox
		////
		// Creación y personalización de los componentes que irán dentro del panel
		
		panelSelectorTipo = new JPanel();
		panelSelectorTipo.setLayout(new BoxLayout(panelSelectorTipo, BoxLayout.Y_AXIS));
		panelSelectorTipo.setVisible(false);
		
		JRadioButton botonAvion = new JRadioButton("Avión");
		botonAvion.setFocusable(false);
		botonAvion.setFont(Main.FUENTE);
		
		JRadioButton botonTren = new JRadioButton("Tren");
		botonTren.setFocusable(false);
		botonTren.setFont(Main.FUENTE);
		
		JRadioButton botonBus = new JRadioButton("Autobus");
		botonBus.setFocusable(false);
		botonBus.setFont(Main.FUENTE);
		
		grupo = new ButtonGroup();
		
		grupo.add(botonAvion);
		grupo.add(botonTren);
		grupo.add(botonBus);
		
		panelSelectorTipo.add(botonAvion);
		panelSelectorTipo.add(botonTren);
		panelSelectorTipo.add(botonBus);
		
		// Añadimos todo al panel principal
		
		add(checkBoxTipo);
		add(panelSelectorTipo);
		
	}

	public void resetAll() {
		grupo.clearSelection();
		checkBoxTipo.setSelected(false);
		panelSelectorTipo.setVisible(false);
	}
	
}
