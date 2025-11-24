package gui.main.busqueda;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import gui.util.MiComboBoxTipos;
import main.Main;

public class MiSelectorTipo extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private JComboBox<String> comboBoxTipos;
	
	public MiSelectorTipo() {
		
		// Personalización del Panel
		
		setLayout(new GridLayout(1, 2, 0, 0));
		
		// FIN Personalización del Panel
		////
		// Creamos el label tipo y lo personalizamos
		
		JLabel tipoL = new JLabel("Tipo:");
		tipoL.setHorizontalAlignment(SwingConstants.CENTER);
		tipoL.setBackground(Color.WHITE);
		tipoL.setBorder(new MatteBorder(1, 1, 1, 0, new Color(0x7A8A99)));
		tipoL.setFont(Main.FUENTE);
		tipoL.setOpaque(true);
		
		// Creamos el label tipo y lo personalizamos
		////
		// Creamos el JComboBox con la lista de destinos ya metida y lo personalizamos
		
		comboBoxTipos = new MiComboBoxTipos();

		// FIN Creamos el JComboBox personalizado
		////
		// Obtenemos el editor del comboBox para jugar con el
		
		JTextField componenteEditorComboBoxTipos = (JTextField) comboBoxTipos.getEditor().getEditorComponent();
		componenteEditorComboBoxTipos.setToolTipText(comboBoxTipos.getSelectedItem().toString());
		componenteEditorComboBoxTipos.setVisible(true);
		
		// Obtenemos el editor del comboBox para jugar con el
		////
		// Actualizamos el toolTipText del editor cuando seleccionemos algo
		
		comboBoxTipos.addActionListener((e) -> {
			componenteEditorComboBoxTipos.setToolTipText(comboBoxTipos.getSelectedItem().toString());
		});
		
		// FIN Actualizamos el toolTipText del editor cuando seleccionemos algo		
		////
		// Implementación del listener que nos permite hacer que al clicar el textfield también aparezca el popup del combobox
		
		MouseAdapter popUpMA = new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				comboBoxTipos.setPopupVisible(true);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				comboBoxTipos.setPopupVisible(true);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				comboBoxTipos.setPopupVisible(true);
			}
		
		};
		
		componenteEditorComboBoxTipos.addMouseListener(popUpMA);
		tipoL.addMouseListener(popUpMA);
		
		// FIN Implementación del listener que nos permite hacer que al clicar el textfield también aparezca el popup del combobox
		////
		// Personalización del popup de nuestro comboBox
		
		comboBoxTipos.setRenderer(new DefaultListCellRenderer() {

			private static final long serialVersionUID = 1L;
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				
				JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);		// Hacemos esto para conseguir el label default que obtendríamos sin editar el método y poder trabajar sobre ella
				
				label.setFont(Main.FUENTE.deriveFont(16.f));
						
				label.setOpaque(true);
						
				return label;
						
			}
					
		});
				
		// FIN Personalización del popup de nuestro comboBox
		// Anadimos los componentes al panel
		
		add(tipoL);
		add(comboBoxTipos);
		
	}
	
	public JComboBox<String> getComboBox() {
		return comboBoxTipos;
	}
	
	public void resetAll() {
		comboBoxTipos.setSelectedIndex(0);
	}
	
}
