package gui.main.busqueda;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;

import main.Main;

public class MiSelectorTipo extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private static String[] arrayTipos = (new ArrayList<String>(Arrays.asList("Ida", "Ida y Vuelta"))).toArray(new String[0]); 
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
		
		comboBoxTipos = new JComboBox<String>(arrayTipos);
		comboBoxTipos.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0x7A8A99)));
				
		// Personalización del botón del JComboBox
				
		comboBoxTipos.setUI(new BasicComboBoxUI() {

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
				
				return botonFlecha;
					
			}
			
		});
		// FIN Personalización del botón del JComboBox
		////
		// Personalización de texto seleccionado del comboBox
			
		comboBoxTipos.setEditable(true);																			// Hay que hacer que el comboBox sea editable para que nos permita conseguir el textField incorporado
		
		JTextField comboBoxTextField = (JTextField) comboBoxTipos.getEditor().getEditorComponent();		// Conseguimos el TextField que lleva incorporado el JComboBox para personalizarlo y normalizar su estilo
		comboBoxTextField.setVisible(true);																// En principio lo ponemos invisible para que no se solape con nuestro filtro (creado más tarde)
		
		comboBoxTextField.setBorder(null);
		comboBoxTextField.setFont(Main.FUENTE);
		comboBoxTextField.setBackground(Color.WHITE);
		comboBoxTextField.setCaretColor(comboBoxTextField.getBackground());
		comboBoxTextField.setHorizontalAlignment(SwingConstants.CENTER);
		
		// FIN Personalización de texto seleccionado del comboBox
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
		
		comboBoxTextField.addMouseListener(popUpMA);
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
