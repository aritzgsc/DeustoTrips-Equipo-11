package gui.util.editores;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

import com.toedter.calendar.JCalendar;

import gui.util.MiButton;
import gui.util.uis.MiComboBoxUI;
import main.Main;

public class MiEditorCalendario extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = 1L;
	
	// Componentes visuales (Panel Editor)
	
	private JPanel panelEditor;
	private JLabel labelNoches;
	private MiButton botonIcono;
	
	// Datos
	
	private Map<LocalDate, LocalDate> mapaReservasActual;
	private List<LocalDate> listaDiasOcupados; 
	
	private JDialog dialogoPopUp; 
	private JCalendar jCalendar;

	public MiEditorCalendario() {
		
		// Configuración del panel contenedor
		
		panelEditor = new JPanel(new BorderLayout());
		panelEditor.setOpaque(true);
		
		// Configuración del Label (Texto)
		
		labelNoches = new JLabel();
		labelNoches.setHorizontalAlignment(SwingConstants.CENTER);
		labelNoches.setFont(Main.FUENTE);
		
		// Configuración del Botón (Icono)
		
		botonIcono = new MiButton();
        ImageIcon imgTemp = new ImageIcon("resources/images/jcalendar_icon.png");
        botonIcono.setIcon(new ImageIcon(imgTemp.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        botonIcono.setBorder(null);
        botonIcono.setContentAreaFilled(false);
        
        // Añadimos componentes al panel
        
        panelEditor.add(labelNoches, BorderLayout.CENTER);
        panelEditor.add(botonIcono, BorderLayout.EAST);
		
		// FIN Configuración de componentes visuales
		////
		// Lógica de disparo
		
        botonIcono.addActionListener(e -> mostrarCalendario());
        
        MouseListener disparadorListener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mostrarCalendario();
			}
		};
		
		panelEditor.addMouseListener(disparadorListener);
		labelNoches.addMouseListener(disparadorListener);
        
		// FIN Lógica de disparo
		
	}

	@Override
	public Object getCellEditorValue() {
		return mapaReservasActual;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		
		// Ponemos los datos en el panel
		
		if (value instanceof Map) {
			
			@SuppressWarnings("unchecked")
			Map<LocalDate, LocalDate> mapa = (Map<LocalDate, LocalDate>) value;
			
			this.mapaReservasActual = mapa;

			int nNoches = 0;
			
			if (mapa != null) {
			
	        	for (LocalDate entrada : mapa.keySet()) {
	                nNoches += (int) ChronoUnit.DAYS.between(entrada, mapa.get(entrada));
	            }
	        	
			}
			
        	labelNoches.setText(nNoches + " noches");
		}
		
		// Le damos color bonito (como el del renderer)
		
		panelEditor.setBackground((row % 2 == 0) ? Color.WHITE : new Color(250, 250, 250));
		labelNoches.setForeground(table.getForeground());
		
		return panelEditor;
		
	}
	
	private void mostrarCalendario() {
		
		if (panelEditor.isShowing()) {
			
			// Creación y personalización del calendario
			
			jCalendar = new JCalendar();
			jCalendar.setPreferredSize(new Dimension(245, 250));
			jCalendar.setDecorationBackgroundColor(Color.WHITE);
			jCalendar.setWeekOfYearVisible(false);
			jCalendar.getDayChooser().setAlwaysFireDayProperty(false);
			jCalendar.setFont(Main.FUENTE.deriveFont(13f));
			jCalendar.setWeekdayForeground(Color.BLACK);
			
			jCalendar.addPropertyChangeListener("calendar", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					// Solo pintamos si el popup existe y está visible
					if (dialogoPopUp != null && dialogoPopUp.isVisible()) {
						pintarDiasEnElCalendario();
					}
				}
			});
			
			// Personalización del comboBox
			
			JComboBox<?> comboBoxCalendario = (JComboBox<?>) jCalendar.getMonthChooser().getComboBox();		// Se pone así para que no lanze warnings aunque sabemos que va a ser de Strings
			comboBoxCalendario.setBorder(Main.DEFAULT_LINE_BORDER);
			
			MiComboBoxEditor editorComboBoxCalendario = new MiComboBoxEditor();
			
			comboBoxCalendario.setUI(new MiComboBoxUI());
			
			comboBoxCalendario.setEditable(true);			// Hacemos esto para que se vea el Componente de Editor bien
			comboBoxCalendario.setEditor(editorComboBoxCalendario);
			
			JTextField componenteEditorComboBoxCalendario = (JTextField) editorComboBoxCalendario.getEditorComponent();
			
			// Le establecemos un MouseListener para que se despliegue el popUp aunque hagamos click en el texto
			
			componenteEditorComboBoxCalendario.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					comboBoxCalendario.setPopupVisible(true);
				}
				
			});
			
			// FIN Personalización del comboBox
			////
			// Personalización del Spinner (cambiar el borde y centrar texto)
			
			JSpinner spinnerCalendario = (JSpinner) jCalendar.getYearChooser().getSpinner();
			spinnerCalendario.setBorder(Main.DEFAULT_LINE_BORDER);
			
			// Personalización del textField del spinner
			
			JTextField editorSpinnerCalendario = (JTextField) spinnerCalendario.getEditor();
			editorSpinnerCalendario.setHorizontalAlignment(JTextField.CENTER);
			
			// FIN Personalización del Spinner
			////
			// Creación y personalización del PopUp
			
			dialogoPopUp = new JDialog(SwingUtilities.getWindowAncestor(panelEditor));
			
			dialogoPopUp.setUndecorated(true);
			dialogoPopUp.setModal(false);
			
			// Listener para cerrar si se pierde el foco
			
			dialogoPopUp.addWindowFocusListener(new WindowAdapter() {
				@Override
				public void windowLostFocus(WindowEvent e) {
					dialogoPopUp.dispose();
					fireEditingStopped();
				}
			});
			
			// Creamos un panel con borde por estética
			
			JPanel panelBorde = new JPanel(new BorderLayout());
			panelBorde.setBorder(Main.DEFAULT_LINE_BORDER);
			panelBorde.add(jCalendar, BorderLayout.CENTER);
			
			// Metemos el panel al dialogo
			
			dialogoPopUp.add(panelBorde);
			dialogoPopUp.pack();
			
			generarListaDiasOcupados();
			pintarDiasEnElCalendario();
			
			Point loc = panelEditor.getLocationOnScreen();
			dialogoPopUp.setLocation(loc.x - 1, loc.y + panelEditor.getHeight());
			dialogoPopUp.setVisible(true);
			
			// FIN Creación y personalización del PopUp
			
		}
		
	}
	
	private void generarListaDiasOcupados() {
		
		listaDiasOcupados = new ArrayList<>();
		
		if (mapaReservasActual != null) {
			
			for (Map.Entry<LocalDate, LocalDate> entry : mapaReservasActual.entrySet()) {
				
				LocalDate inicio = entry.getKey();
				LocalDate fin = entry.getValue();
				
				long dias = ChronoUnit.DAYS.between(inicio, fin);
				
				for (int i = 0; i <= dias - 1; i++) {
					listaDiasOcupados.add(inicio.plusDays(i));
				}
				
			}
		}
		
	}
	
	private void pintarDiasEnElCalendario() {
		
		JPanel panelDia = jCalendar.getDayChooser().getDayPanel();
		Calendar calActual = jCalendar.getCalendar();
		int mesActual = calActual.get(Calendar.MONTH) + 1; 
		int anioActual = calActual.get(Calendar.YEAR);
		
		for (Component componente : panelDia.getComponents()) {
			
			if (componente instanceof JButton) {
				
				JButton botonDia = (JButton) componente;
				
				botonDia.setBackground(Color.WHITE);
				botonDia.setForeground(Color.BLACK);
				
				try {
					
					int diaNumero = Integer.parseInt(botonDia.getText());
					LocalDate fechaBoton = LocalDate.of(anioActual, mesActual, diaNumero);
					
					if (listaDiasOcupados.contains(fechaBoton)) {
						
						botonDia.setBackground(new Color(255, 140, 0)); 
						botonDia.setForeground(Color.WHITE);
						
					}
					
				} catch (Exception ex) { }
				
			}
			
		}
		
	}

}
