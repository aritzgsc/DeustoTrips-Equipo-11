package gui.main.busqueda;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicComboBoxUI;

import java.util.*;

import domain.*;
import gui.util.MiTextField;
import main.Main;

public class MiSelectorDestino extends JLayeredPane {

	private static final long serialVersionUID = 1L;
	
	// Datos de prueba - cuando tengamos la BD lo rellenaremos con eso
	
	Pais pais1 = new Pais("España");
	Pais pais2 = new Pais("Francia");
	Pais pais3 = new Pais("Alemania");
	Pais pais4 = new Pais("UK");
	Pais pais5 = new Pais("Portugal");
	
	Ciudad ciudad1 = new Ciudad(pais1, "Madrid");
	Ciudad ciudad2 = new Ciudad(pais1, "Barcelona");
	Ciudad ciudad3 = new Ciudad(pais1, "Bilbao");
	Ciudad ciudad4 = new Ciudad(pais2, "Paris");
	Ciudad ciudad5 = new Ciudad(pais2, "Lyon");
	Ciudad ciudad10 = new Ciudad(pais4, "Londres");
	
	Aeropuerto aeropuerto1 = new Aeropuerto(ciudad1, "Aeropuerto de Barajas");
	Aeropuerto aeropuerto2 = new Aeropuerto(ciudad2, "Aeropuerto del Prat");
	Aeropuerto aeropuerto3 = new Aeropuerto(ciudad10, "Aeropuerto de Gatwick");
	Aeropuerto aeropuerto4 = new Aeropuerto(ciudad10, "Aeropuerto de Heathrow");
	Aeropuerto aeropuerto5 = new Aeropuerto(ciudad10, "Aeropuerto de Luton");
	
	private ArrayList<Destino> todosDestinos = new ArrayList<Destino>();
	
	private JComboBox<Destino> comboBoxDestinos;			// Sacamos el comboBox afuera para poder resetearlo con un método (también nos ayuda a tener menos problemas al crear los listeners - para tenerlo todo más ordenado)
	private MiTextField filtro;								// Sacamos el filtro afuera para poder resetearlo con un método (también nos ayuda a tener menos problemas al crear los listeners - para tenerlo todo más ordenado)
	
	public MiSelectorDestino(String defaultAns) {
		
		// Configuración del layout del selector
		
		setLayout(new OverlayLayout(this));
		
		// FIN Configuración del layout del selector
		// Datos de prueba - cuando tengamos la BD lo rellenaremos con eso
		
		todosDestinos.add(new Pais(defaultAns, true));
		todosDestinos.add(pais1);
		todosDestinos.add(pais2);
		todosDestinos.add(pais3);
		todosDestinos.add(pais4);
		todosDestinos.add(pais5);
		todosDestinos.add(ciudad1);
		todosDestinos.add(ciudad2);
		todosDestinos.add(ciudad3);
		todosDestinos.add(ciudad4);
		todosDestinos.add(ciudad5);
		todosDestinos.add(ciudad10);
		todosDestinos.add(aeropuerto1);
		todosDestinos.add(aeropuerto2);
		todosDestinos.add(aeropuerto3);
		todosDestinos.add(aeropuerto4);
		todosDestinos.add(aeropuerto5);
		
		// FIN Datos de prueba - cuando tengamos la BD lo rellenaremos con eso
		// Ordenamos la lista por su compareTo (En domain.Destino)
		
		Collections.sort(todosDestinos);
		
		// Creamos el JComboBox con la lista de destinos ya metida y lo personalizamos
		
		comboBoxDestinos = new JComboBox<Destino>(todosDestinos.toArray(new Destino[0]));	
		comboBoxDestinos.setBorder(Main.DEFAULT_LINE_BORDER);
		comboBoxDestinos.setPreferredSize(new Dimension(comboBoxDestinos.getWidth(), 50));
		comboBoxDestinos.setToolTipText(comboBoxDestinos.getSelectedItem().toString());
		comboBoxDestinos.setSelectedIndex(0);
		
		// Personalización del botón del JComboBox
		
		comboBoxDestinos.setUI(new BasicComboBoxUI() {

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
		
		comboBoxDestinos.setEditable(true);																			// Hay que hacer que el comboBox sea editable para que nos permita conseguir el textField incorporado
		
		JTextField comboBoxDestinosTextField = (JTextField) comboBoxDestinos.getEditor().getEditorComponent();		// Conseguimos el TextField que lleva incorporado el JComboBox para personalizarlo y normalizar su estilo
		comboBoxDestinosTextField.setVisible(true);																// En principio lo ponemos invisible para que no se solape con nuestro filtro (creado más tarde)
		
		comboBoxDestinosTextField.setBorder(null);
		comboBoxDestinosTextField.setFont(Main.FUENTE);
		comboBoxDestinosTextField.setBackground(Color.WHITE);
		comboBoxDestinosTextField.setCaretColor(comboBoxDestinosTextField.getBackground());
		comboBoxDestinosTextField.setHorizontalAlignment(SwingConstants.CENTER);
		
		// FIN Personalización de texto seleccionado del comboBox
		////
		// Personalización del popup de nuestro comboBox
		
		comboBoxDestinos.setRenderer(new DefaultListCellRenderer() {

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
		////
		// Ahora hay que gestionar que queremos que ocurra en cada caso para cada uno de estos dos componentes
		
		comboBoxDestinos.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {			// Al seleccionar un elemento, no quiero ver más mi filtro (creado más abajo) quiero ver lo seleccionado (sale en el JTextField personalizado que hemos puesto arriba)
				filtro.setVisible(false);
			}
			
		});
		
		MouseAdapter comboBoxDestinosMA = new MouseAdapter() {		// Al clicar en cualquier parte y de cualquier forma del comboBox o su textField quiero visibilizar mi filtro, que se me ponga automáticamente para editar y además que se vea el popup del comboBox

			@Override
			public void mousePressed(MouseEvent e) {
				filtro.setVisible(true);
				filtro.grabFocus();
				comboBoxDestinos.setPopupVisible(true);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				filtro.setVisible(true);
				filtro.grabFocus();
				comboBoxDestinos.setPopupVisible(true);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				filtro.setVisible(true);
				filtro.grabFocus();
				comboBoxDestinos.setPopupVisible(true);
			}
		
		};
		
		comboBoxDestinos.addMouseListener(comboBoxDestinosMA);				// Añadimos el MouseAdapter
		comboBoxDestinosTextField.addMouseListener(comboBoxDestinosMA);		// Añadimos el MouseAdapter
		
		// FIN Con esto acaba la gestión de lo que ocurrirá cuando se haga click en ellos
		////
		// Creación del filtro principal y lo personalizamos mínimamente
		
		filtro = new MiTextField();
		filtro.setHorizontalAlignment(JTextField.CENTER);
		filtro.setPreferredSize(new Dimension(filtro.getWidth(), 50));
		filtro.setVisible(false);
		
		// FIN Creación del filtro principal
		////
		// Como antes, tenemos que gestionar que ocurrirá con cada acción que hagamos con el filtro
		
		filtro.addFocusListener(new FocusListener() {				// Cuando esté escribiendo quiero que el textField del comboBox no sea visible, y cuando deje de escribir quiero hacerlo visible para que aparezca cual ha sido seleccionada (además hago invisible el filtro (luego podré hacerlo visible dando click al comboBox otra vez (mirar arriba)))
			
			@Override
			public void focusGained(FocusEvent e) {
				comboBoxDestinosTextField.setVisible(false);
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				filtro.setVisible(false);
				comboBoxDestinosTextField.setVisible(true);
			}
			
		});
		
		filtro.addMouseListener(new MouseAdapter() {				// Cuando haga click sobre el filtro quiero que se vea el popUp

			@Override
			public void mousePressed(MouseEvent e) {
				comboBoxDestinos.setPopupVisible(true);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				comboBoxDestinos.setPopupVisible(true);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				comboBoxDestinos.setPopupVisible(true);
			}
			
		});
		
		// FIN Con esto quedan gestionadas todas las acciones del filtro
		////
		// Creación del filtro principal
		
		filtro.getDocument().addDocumentListener(new DocumentListener() {	
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				filtrarDestinos(comboBoxDestinos, filtro.getText());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				filtrarDestinos(comboBoxDestinos, filtro.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				filtrarDestinos(comboBoxDestinos, filtro.getText());
			}
			
		});
		
		// FIN Creación del filtro principal
		// Añadimos los componentes en orden (queremos que el filtro esté por encima del comboBox (siempre y cuando esté visible))
		
		add(comboBoxDestinos, JLayeredPane.DEFAULT_LAYER);
		add(filtro, JLayeredPane.PALETTE_LAYER);
		
	}
	
	// Método que filtra los destinos entre todos los destinos de la lista cada vez que se escribe o borra algo en el filtro
	
	private void filtrarDestinos(JComboBox<Destino> comboBoxDestinos, String filtro) {
		
		ArrayList<Destino> destinosFiltrados = new ArrayList<Destino>();
		
		for (Destino destino : todosDestinos) {
			
			if (destino.toString().toUpperCase().contains(filtro.toUpperCase())) {
				
				destinosFiltrados.add(destino);
				
			}
			
		}
		
		Collections.sort(destinosFiltrados);
		
		comboBoxDestinos.setModel(new DefaultComboBoxModel<Destino>(destinosFiltrados.toArray(new Destino[0])));		// Actualizamos el modelo (los datos) cada vez (El renderer no se ve afectado)
		
		comboBoxDestinos.setPopupVisible(true);																			// Hacemos visible el popup cada vez porque si no se haría invisible
		
	}
	
	// Método que resetea todos los valores establecidos por el usuario
	
	public void resetAll() {
		comboBoxDestinos.setSelectedIndex(0);		// Cuando tengamos los datos finales en el primer valor estará algo como "Seleccione un destino"
		filtro.setText("");
	}
	
	// Método para obtener el destino seleccionado
	
	public Destino getDestinoSeleccionado() {
		return (Destino) comboBoxDestinos.getSelectedItem();
	}
	
}
