package gui.main.busqueda;

import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.time.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import com.toedter.calendar.*;		// Añadimos esta librería para crear calendarios y selectores de fechas más fácilmente

import gui.util.editores.MiComboBoxEditor;
import gui.util.uis.MiComboBoxUI;
import main.Main;

// Clase que crea un selector de fechas personalizado con un calendario incorporado A PARTIR DE UNA LIBRERÍA DESCARGADA que utiliza componentes de Swing para crearlo

public class MiSelectorFecha extends JDateChooser {

	private static final long serialVersionUID = 1L;

	private static final long anosDiferenciaMaxima = 2;
	private static final Date fechaMinima = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());										// Como JDateChooser usa Date (Clase antigua) esta es la única forma de pasarle la fecha de hoy (aparecerá bastante)
	private static final Date fechaMaxima = Date.from(LocalDateTime.now().plusYears(anosDiferenciaMaxima).atZone(ZoneId.systemDefault()).toInstant());	
	
	private JDateChooser selectorFecha;					// Lo sacamos para poder obtener su tamaño para ponerle al calendar el mismo
	
	private JTextField selectorFechaTextField;			// Lo sacamos para usarlo en la función resetAll()
	private String placeholder;						// Lo sacamos para usarlo en la función resetAll()
	
	public MiSelectorFecha(String placeholder) {
		
		selectorFecha = this;
		
		this.placeholder = placeholder;
		
		// Configuración del JDateChooser
		
		setBorder(Main.DEFAULT_LINE_BORDER);
		setDateFormatString("dd/MM/yyyy");
		
		// FIN Configuración del JDateChooser
		////
		// Establecemos las fechas mínima y máxima seleccionables
		
		setMinSelectableDate(fechaMinima);
		setMaxSelectableDate(fechaMaxima);
		
		// FIN Establecemos las fechas mínima y máxima seleccionables
		////
		// Personalización del JTextField que incluye el JDateChooser
		
		selectorFechaTextField = ((JTextField) getDateEditor().getUiComponent());		// Conseguimos el JTextField para normalizar su estilo al establecido en el resto de la aplicación
		
		selectorFechaTextField.setText(placeholder);
		selectorFechaTextField.setToolTipText(null);
		selectorFechaTextField.setFont(Main.FUENTE.deriveFont(16.f));
		selectorFechaTextField.setHorizontalAlignment(JTextField.CENTER);
		selectorFechaTextField.setBorder(null);
		selectorFechaTextField.setBackground(Color.WHITE);
		
		selectorFechaTextField.addFocusListener(new FocusListener() {
					
			@Override
			public void focusGained(FocusEvent e) {
				if (!selectorFechaTextField.getText().matches("\\d{2}/\\d{2}/\\d{4}")) {
					selectorFechaTextField.setText("");
					selectorFechaTextField.setForeground(Color.BLACK);
				}
			}
					
			@Override
			public void focusLost(FocusEvent e) {
				if (!selectorFechaTextField.getText().matches("\\d{2}/\\d{2}/\\d{4}")) {
					setDate(null);
					selectorFechaTextField.setText(placeholder);
					selectorFechaTextField.setForeground(Color.BLACK);
				}
			}
					
		});
		
		addPropertyChangeListener("date", (e) -> {
			selectorFechaTextField.setToolTipText(getDate() != null? DateFormat.getDateInstance(DateFormat.MEDIUM).format(getDate()) : null);
		});
		
		// FIN Personalización del JTextField del JDateChooser
		////
		// Personalización del JButton que abre el JCalendar (para dejar todo con el mismo formato)

		JButton botonCalendario = getCalendarButton();
		
		botonCalendario.setBorder(new MatteBorder(0, 1, 0, 0, new Color(0x7A8A99)));
		botonCalendario.setFocusable(false);
		botonCalendario.setBackground(new Color(0xEEEEEE));
		botonCalendario.setPreferredSize(new Dimension(50, 50));
		botonCalendario.setToolTipText(placeholder);
		
		botonCalendario.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				botonCalendario.setBorder(new CompoundBorder(new LineBorder(new Color(0xB8CFE5)), new CompoundBorder(Main.DEFAULT_LINE_BORDER, new LineBorder(new Color(0xB8CFE5)))));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				botonCalendario.setBorder(new MatteBorder(0, 1, 0, 0, new Color(0x7A8A99)));
			}
			
		});
		
		// Personalización del JButton que abre el JCalendar (para dejar todo con el mismo formato)
		////
		// Personalización del JCalendar que se muestra
		
		botonCalendario.addActionListener((e) -> {
					
				JCalendar calendario = getJCalendar();
				
				calendario.setPreferredSize(new Dimension(selectorFecha.getWidth() - 3, 250));
				calendario.setFont(Main.FUENTE.deriveFont(13f));
				calendario.setWeekdayForeground(Color.BLACK);
				calendario.setWeekOfYearVisible(false);
				
				// Personalización de los botones del calendario
				
				SwingUtilities.invokeLater(() -> {	
					
					estilizarDias(calendario);
					
					calendario.getMonthChooser().addPropertyChangeListener("month", (evt) -> estilizarDias(calendario));
					
					calendario.getYearChooser().addPropertyChangeListener("year", (evt) -> estilizarDias(calendario));
					
				});
				
				// FIN Personalización de los botones del calendario
				////
				// Personalización del comboBox
				
				JComboBox<?> comboBoxCalendario = (JComboBox<?>) calendario.getMonthChooser().getComboBox();		// Se pone así para que no lanze warnings aunque sabemos que va a ser de Strings
				comboBoxCalendario.setBorder(Main.DEFAULT_LINE_BORDER);
				
				MiComboBoxEditor editorComboBoxCalendario = new MiComboBoxEditor();
				
				comboBoxCalendario.setUI(new MiComboBoxUI());
				
				comboBoxCalendario.setEditable(true);			// Hacemos esto para que se vea el Componente de Editor bien
				comboBoxCalendario.setEditor(editorComboBoxCalendario);
		
				JTextField componenteEditorComboBoxCalendario = (JTextField) editorComboBoxCalendario.getEditorComponent();
				componenteEditorComboBoxCalendario.setFont(componenteEditorComboBoxCalendario.getFont().deriveFont(13f));
				
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
				
				JSpinner spinnerCalendario = (JSpinner) calendario.getYearChooser().getSpinner();
				spinnerCalendario.setBorder(Main.DEFAULT_LINE_BORDER);
		
				// Personalización del textField del spinner
				
				JTextField editorSpinnerCalendario = (JTextField) spinnerCalendario.getEditor();
				editorSpinnerCalendario.setHorizontalAlignment(JTextField.CENTER);
				
				// FIN Personalización del Spinner
			
		});
			
		// FIN Personalización del JCalendar que se muestra
	}
	
	// Getters de fechas mínima y máxima (Para MiSelectorMultiplesFechas)
	
	public static Date getFechaMinima() {
		return fechaMinima;
	}

	public static Date getFechaMaxima() {
		return fechaMaxima;
	}
	
	// FIN Getters de fechas mínima y máxima
	////
	// Método para dar estilo a los días del calendario
	
	private void estilizarDias(JCalendar calendario) {
			
		JPanel panelDia = calendario.getDayChooser().getDayPanel();
			
		for (Component componente : panelDia.getComponents()) {
				
			if (componente instanceof JButton) {
					
				JButton botonDia = (JButton) componente;
					
				botonDia.setFocusable(false);
					
				if (botonDia.isEnabled()) {
						
					botonDia.setBackground(Color.WHITE);
						
				}
					
			}
				
		}
			
	}
	////
	// Método que nos permite resetear la fecha establecida si la hay
	
	public void resetAll() {
		selectorFechaTextField.setText(placeholder);
		selectorFechaTextField.setToolTipText(placeholder);
		selectorFechaTextField.setForeground(Color.BLACK);
	}
	
}
