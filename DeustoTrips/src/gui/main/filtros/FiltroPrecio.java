package gui.main.filtros;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jidesoft.plaf.basic.BasicRangeSliderUI;	// Añadimos esta librería para importar la UI del RangeSlider para que sea más bonito
import com.jidesoft.swing.RangeSlider;				// Añadimos esta librería para crear un slider con dos thumbs que actúen de mínimo y máximo más fácilmente (es decir, un slider de rangos)

import gui.main.PanelAlojamientos;
import gui.main.PanelPestanasBusqueda;
import gui.main.PanelViajeAlojamiento;
import gui.main.PanelViajes;
import gui.util.MiTextField;
import main.Main;

// Clase que contendrá el filtro de precio con un rango

public class FiltroPrecio extends JPanel {

	private static final long serialVersionUID = 1L;

	private JCheckBox checkBoxPrecio;
	private RangeSlider sliderRangoPrecios;
	private MiTextField minimoTF;
	private MiTextField maximoTF;
	
	private static int precioMaximo = 0;
	
	private static List<FiltroPrecio> misFiltrosPrecio = new ArrayList<FiltroPrecio>();
	
	public FiltroPrecio() {
		
		// Añadimos la instancia a la lista estática
		
		misFiltrosPrecio.add(this);
		
		// Configuración del panel
		
		setAlignmentX(LEFT_ALIGNMENT);
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		
		// FIN Configuración del panel
		////
		// Creación del botón que controlará si aparece o desaparece el filtro
		
		checkBoxPrecio = new JCheckBox(" Precio: ");
		checkBoxPrecio.setPreferredSize(new Dimension(100, 50));
		checkBoxPrecio.setBorder(Main.DEFAULT_LINE_BORDER);
		checkBoxPrecio.setFont(Main.FUENTE);
		checkBoxPrecio.setFocusable(false);
		
		// FIN Creación del botón que controlará si parece o desaparece el filtro
		////
		// Creación y personalización de los componentes que irán dentro del panel
		
		sliderRangoPrecios = new RangeSlider(0, precioMaximo, 0, precioMaximo);
		sliderRangoPrecios.setPreferredSize(new Dimension(380, 25));
		sliderRangoPrecios.setUI(new BasicRangeSliderUI(sliderRangoPrecios));		// Le ponemos esta UI preestablecida para que el Slider sea más bonito
		sliderRangoPrecios.setFocusable(false);
		sliderRangoPrecios.setRangeDraggable(false);
		sliderRangoPrecios.setVisible(false);
		
		minimoTF = new MiTextField();
		minimoTF.setColumns(5);
		minimoTF.setHorizontalAlignment(JTextField.CENTER);
		minimoTF.setText(String.valueOf(0));
		minimoTF.setVisible(false);
		
		maximoTF = new MiTextField();
		maximoTF.setColumns(5);
		maximoTF.setHorizontalAlignment(JTextField.CENTER);
		maximoTF.setText(String.valueOf(precioMaximo));
		maximoTF.setVisible(false);
		
		// FIN Creación y personalización de los componentes que irán dentro del panel
		////
		// Creamos las acciones que tienen que obedecer los componentes
		
		// Implementación del funcionamiento de checkbox: Si se selecciona la checkBox se hace visible el filtro, si no, se hace invisible
		
		checkBoxPrecio.addActionListener((e) -> {	
			
			if (checkBoxPrecio.isSelected()) {
				
				sliderRangoPrecios.setVisible(true);
				minimoTF.setVisible(true);
				maximoTF.setVisible(true);
				
			} else {
				
				sliderRangoPrecios.setVisible(false);
				minimoTF.setVisible(false);
				maximoTF.setVisible(false);
				
			}
			
		});
		
		// FIN Implementación del funcionamiento de checkbox
		////
		// Implementación de valores posibles introducidos en los TextFields
		
		minimoTF.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if (!Character.toString(e.getKeyChar()).matches("\\d") || Integer.parseInt(minimoTF.getText() + e.getKeyChar()) > precioMaximo) {
					e.consume();
				}
			}
			
		});
		
		maximoTF.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if (!Character.toString(e.getKeyChar()).matches("\\d") || Integer.parseInt(maximoTF.getText() + e.getKeyChar()) > precioMaximo) {
					e.consume();
				}
			}
			
		});
		
		// FIN Implementación de valores posibles introducidos en los TextFields
		////
		// Implementación del funcionamiento de la interacción entre los TextFields y el RangeSlider
		
		sliderRangoPrecios.addChangeListener(new ChangeListener() {									// Si se reajusta el valor de uno de los thumbs se actualizan los TextFields
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if (sliderRangoPrecios.getValueIsAdjusting()) {
					
					SwingUtilities.invokeLater(() -> {
						
						minimoTF.setText(String.valueOf(sliderRangoPrecios.getLowValue()));
						maximoTF.setText(String.valueOf(sliderRangoPrecios.getHighValue()));
						
					});
					
				}
			}
			
		});
		
        minimoTF.getDocument().addDocumentListener(new DocumentListener() {							// Si se escribe algo en el TextField cambia el valor minimo del RangeSlider

            private void actualizarMinimo() {
            	
                if (!minimoTF.getText().isBlank()) {
                	
                	sliderRangoPrecios.setLowValue(Integer.parseInt(minimoTF.getText()));
                	
                }
                
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarMinimo();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarMinimo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarMinimo();
            }
        });
		
        maximoTF.getDocument().addDocumentListener(new DocumentListener() {							// Si se escribe algo en el TextField cambia el valor maximo del RangeSlider

            private void actualizarMaximo() {
            	
                if (!maximoTF.getText().isBlank()) {
                	
                	sliderRangoPrecios.setHighValue(Integer.parseInt(maximoTF.getText()));
                	
                }
                
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarMaximo();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarMaximo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarMaximo();
            }
        });
		
        // Calculamos el precio máximo
		
     		SwingUtilities.invokeLater(() -> calcularPrecioMaximo());
        
        // FIN Implementación del funcionamiento de la interacción entre los TextFields y el RangeSlider
        // Añadimos en orden todos los componentes del filtro
        
		add(checkBoxPrecio);
		add(sliderRangoPrecios);
		add(minimoTF);
		add(maximoTF);
		
	}
	
	public RangeSlider getRangeSlider() {
		return sliderRangoPrecios;
	}
	
	public MiTextField getMaximoTF() {
		return maximoTF;
	}
	
	public int getLowValue() {
		return sliderRangoPrecios.getLowValue();
	}
	
	public int getHighValue() {
		return sliderRangoPrecios.getHighValue();
	}
	
	public void resetAll() {
		checkBoxPrecio.setSelected(false);
		sliderRangoPrecios.setLowValue(0);
		sliderRangoPrecios.setHighValue(precioMaximo);	
		sliderRangoPrecios.setVisible(false);
		minimoTF.setVisible(false);
		maximoTF.setVisible(false);
	}
	
	public static int calcularPrecioMaximo() {
		
		Component panelSeleccionado = PanelPestanasBusqueda.getPanelPestanasBusqueda().getSelectedComponent();
		
		if (panelSeleccionado instanceof PanelAlojamientos) {
			
			precioMaximo = 10000 * ((PanelAlojamientos) panelSeleccionado).getNPersonas() * ((PanelAlojamientos) panelSeleccionado).getNNoches();
			
			for (FiltroPrecio filtroPrecio : misFiltrosPrecio) {
				filtroPrecio.getRangeSlider().setMaximum(precioMaximo);
				filtroPrecio.getMaximoTF().setText(Integer.toString(precioMaximo));
			}
			
			return 10000 * ((PanelAlojamientos) panelSeleccionado).getNPersonas() * ((PanelAlojamientos) panelSeleccionado).getNNoches();
			
		} else if (panelSeleccionado instanceof PanelViajes) {
			
			precioMaximo = 1000 * ((PanelViajes) panelSeleccionado).getNPersonas() * (((PanelViajes) panelSeleccionado).getTipo().equals("Ida")? 1 : 2);
			
			for (FiltroPrecio filtroPrecio : misFiltrosPrecio) {
				filtroPrecio.getRangeSlider().setMaximum(precioMaximo);
				filtroPrecio.getMaximoTF().setText(Integer.toString(precioMaximo));
			}
			
			return 1000 * ((PanelViajes) panelSeleccionado).getNPersonas() * (((PanelViajes) panelSeleccionado).getTipo().equals("Ida")? 1 : 2);
			
		} else if (panelSeleccionado instanceof PanelViajeAlojamiento) {
			
			precioMaximo = 10000 * ((PanelViajeAlojamiento) panelSeleccionado).getNPersonas() * ((PanelViajeAlojamiento) panelSeleccionado).getNNoches() + 1000 * ((PanelViajeAlojamiento) panelSeleccionado).getNPersonas() * 2;
			
			for (FiltroPrecio filtroPrecio : misFiltrosPrecio) {
				filtroPrecio.getRangeSlider().setMaximum(precioMaximo);
				filtroPrecio.getMaximoTF().setText(Integer.toString(precioMaximo));
			}
			
			return 10000 * ((PanelViajeAlojamiento) panelSeleccionado).getNPersonas() * ((PanelViajeAlojamiento) panelSeleccionado).getNNoches() + 1000 * ((PanelViajeAlojamiento) panelSeleccionado).getNPersonas() * 2;
			
		}
		
		precioMaximo = 10000;
		
		for (FiltroPrecio filtroPrecio : misFiltrosPrecio) {
			filtroPrecio.getRangeSlider().setMaximum(precioMaximo);
			filtroPrecio.getMaximoTF().setText(Integer.toString(precioMaximo));
		}
		
		return 10000;
	}
	
	public boolean isEnabled() {
		return checkBoxPrecio.isEnabled();
	}
	
}
