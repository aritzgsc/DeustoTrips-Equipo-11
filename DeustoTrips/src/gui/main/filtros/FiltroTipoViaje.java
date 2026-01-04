package gui.main.filtros;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import domain.Viaje.TipoViaje;
import gui.util.uis.IconoCheckbox;
import main.Main;

public class FiltroTipoViaje extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JCheckBox checkBoxTipo;
	private JCheckBox botonAvion;
	private JCheckBox botonOtro;
	private JPanel panelSelectorTipo;
	
	public FiltroTipoViaje() {

		// Configuración del panel
		
		setAlignmentX(LEFT_ALIGNMENT);
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
				
		// FIN Configuración del panel
		////
		// Creación del botón que controlará si aparece o desaparece el filtro
				
		checkBoxTipo = new JCheckBox("\tTipo de viaje: ");
		checkBoxTipo.setPreferredSize(new Dimension(200, 50));
		checkBoxTipo.setBorder(Main.DEFAULT_LINE_BORDER);
		checkBoxTipo.setFont(Main.FUENTE);
		checkBoxTipo.setIcon(new IconoCheckbox());
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
		
		botonAvion = new JCheckBox("\tAvión");
		botonAvion.setSelected(true);
		botonAvion.setFocusable(false);
		botonAvion.setFont(Main.FUENTE);
		botonAvion.setIcon(new IconoCheckbox());
		
		botonAvion.addActionListener((e) -> {
			
			if (!botonAvion.isSelected()) {
				
				botonAvion.setSelected(false);
				botonOtro.setEnabled(false);
				
			} else {
				
				botonAvion.setSelected(true);
				botonOtro.setEnabled(true);
				
			}
			
		});
		
		botonOtro = new JCheckBox("\tOtro");
		botonOtro.setSelected(true);
		botonOtro.setFocusable(false);
		botonOtro.setFont(Main.FUENTE);
		botonOtro.setIcon(new IconoCheckbox());
		
		botonOtro.addActionListener((e) -> {
			
			if (!botonOtro.isSelected()) {
				
				botonOtro.setSelected(false);
				botonAvion.setEnabled(false);
				
			} else {
				
				botonOtro.setSelected(true);
				botonAvion.setEnabled(true);
				
			}
			
		});
		
		panelSelectorTipo.add(botonAvion);
		panelSelectorTipo.add(botonOtro);
		
		// Añadimos todo al panel principal
		
		add(checkBoxTipo);
		add(panelSelectorTipo);
		
	}

	public void resetAll() {
		botonAvion.setSelected(true);
		botonOtro.setSelected(true);
		checkBoxTipo.setSelected(false);
		panelSelectorTipo.setVisible(false);
	}
	
	public List<TipoViaje> getValues() {
		
		List<TipoViaje> valoresSeleccionados = new ArrayList<TipoViaje>();
		
		if (botonAvion.isSelected()) valoresSeleccionados.add(TipoViaje.AVION);
		if (botonOtro.isSelected()) valoresSeleccionados.addAll(Arrays.asList(TipoViaje.TREN, TipoViaje.AUTOBUS));
		
		return valoresSeleccionados;
		
	}
	
	public boolean isEnabled() {
		return checkBoxTipo.isSelected();
	}
	
}
