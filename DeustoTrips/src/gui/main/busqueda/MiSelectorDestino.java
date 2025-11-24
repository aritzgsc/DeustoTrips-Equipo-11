package gui.main.busqueda;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import domain.Destino;
import domain.Pais;
import gui.util.MiComboBoxDestinos;
import gui.util.MiTextField;
import gui.util.models.MiComboBoxDestinosModel;

public class MiSelectorDestino extends JLayeredPane {

	private static final long serialVersionUID = 1L;
	
	// Datos de prueba - cuando tengamos la BD lo rellenaremos con eso
	
	private List<Destino> todosDestinos = new ArrayList<Destino>();		// TODO Cargar de la BD
	private List<Destino> destinosFiltrados = todosDestinos;
	
	private Destino destinoSeleccionado = null;
	
	private MiComboBoxDestinos comboBoxDestinos;			// Sacamos el comboBox afuera para poder resetearlo con un método (también nos ayuda a tener menos problemas al crear los listeners - para tenerlo todo más ordenado)
	private MiTextField filtro;								// Sacamos el filtro afuera para poder resetearlo con un método (también nos ayuda a tener menos problemas al crear los listeners - para tenerlo todo más ordenado)
	
	public MiSelectorDestino(String defaultAns) {
		
		// Configuración del layout del selector
		
		setLayout(new OverlayLayout(this));
		
		// FIN Configuración del layout del selector
		// Datos de prueba - cuando tengamos la BD lo rellenaremos con eso
		
		todosDestinos.add(new Pais(defaultAns, true));
		
		// FIN Datos de prueba - cuando tengamos la BD lo rellenaremos con eso
		// Ordenamos la lista por su compareTo (En domain.Destino)
		
		Collections.sort(todosDestinos);
		
		MiComboBoxDestinosModel comboBoxDestinosModel = new MiComboBoxDestinosModel(todosDestinos);
		
		// Creación del ComboBox ya personalizado con el modelo de datos incorporado
		
		comboBoxDestinos = new MiComboBoxDestinos(comboBoxDestinosModel);
		
		// FIN Creación del comboBox
		////
		// Conseguimos el componente del editor del comboBox para poder jugar con el más adelante
		
		JTextField componenteEditorComboBox = (JTextField) comboBoxDestinos.getEditor().getEditorComponent();
		
		// No ponemos tooltiptext porque empieza con el por defecto (que sería null (mirar abajo en el actionListener))
		
		// FIN Conseguir el componente del editor
		////
		// Ahora hay que gestionar que queremos que ocurra en cada caso para cada uno de estos dos componentes
		
		comboBoxDestinos.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {			// Al seleccionar un elemento, no quiero ver más mi filtro (creado más abajo) quiero ver lo seleccionado (sale en el JTextField personalizado que hemos puesto arriba)
				filtro.setVisible(false);
				destinoSeleccionado = (Destino) comboBoxDestinos.getSelectedItem();
				
				componenteEditorComboBox.setToolTipText(destinoSeleccionado != null || !destinoSeleccionado.equals(new Pais(defaultAns, true))? destinoSeleccionado.toString() : null);
				filtro.setToolTipText(destinoSeleccionado != null || !destinoSeleccionado.equals(new Pais(defaultAns, true))? destinoSeleccionado.toString() : null);
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
		componenteEditorComboBox.addMouseListener(comboBoxDestinosMA);		// Añadimos el MouseAdapter
		
		// FIN Con esto acaba la gestión de lo que ocurrirá cuando se haga click en ellos
		////
		// Creación del filtro principal y lo personalizamos mínimamente
		
		filtro = new MiTextField();
		filtro.setHorizontalAlignment(JTextField.CENTER);
		filtro.setPreferredSize(new Dimension(filtro.getWidth(), 50));
		filtro.setVisible(false);											// Lo ponemos invisible al principio
		
		// FIN Creación del filtro principal
		////
		// Como antes, tenemos que gestionar que ocurrirá con cada acción que hagamos con el filtro
		
		filtro.addFocusListener(new FocusListener() {						// Cuando esté escribiendo quiero que el textField del comboBox no sea visible, y cuando deje de escribir quiero hacerlo visible para que aparezca cual ha sido seleccionada (además hago invisible el filtro (luego podré hacerlo visible dando click al comboBox otra vez (mirar arriba)))
			
			@Override
			public void focusGained(FocusEvent e) {
				componenteEditorComboBox.setVisible(false);
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				filtro.setVisible(false);
				componenteEditorComboBox.setVisible(true);
				
				if (destinoSeleccionado != null && destinosFiltrados.contains(destinoSeleccionado)) {
					comboBoxDestinos.setSelectedItem(destinoSeleccionado);
				} else {
					comboBoxDestinos.setSelectedIndex(0);
				}
				
			}
			
		});
		
		filtro.addMouseListener(new MouseAdapter() {						// Cuando haga click sobre el filtro quiero que se vea el popUp

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
	
	private void filtrarDestinos(MiComboBoxDestinos comboBoxDestinos, String filtroText) {
		
		if (comboBoxDestinos.getSelectedItem() != null) {
			destinoSeleccionado = (Destino) comboBoxDestinos.getSelectedItem();
		}
		
		destinosFiltrados = new ArrayList<Destino>();
		
		for (Destino destino : todosDestinos) {
			
			if (destino.toString().toUpperCase().contains(filtroText.toUpperCase()) || destino.isDefaultAns()) {
				
				destinosFiltrados.add(destino);
				
			}
			
		}
		
		Collections.sort(destinosFiltrados);
		
		comboBoxDestinos.setModel(new MiComboBoxDestinosModel(destinosFiltrados));;				// Actualizamos el modelo (los datos) cada vez (El renderer no se ve afectado)
		
		comboBoxDestinos.setPopupVisible(true);
		filtro.grabFocus();
		
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
