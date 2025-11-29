package gui.util.renderers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import domain.Resena;
import gui.main.busqueda.MiSpinnerPersonas;
import gui.util.MiButton;
import gui.util.MiProgressBarTabla;
import gui.util.MiTextField;
import main.Main;

public class MiTablaResumenApartamentosRenderer implements TableCellRenderer {

	// Creamos los componentes fuera de la funcion principal para mayor rendimiento (no creamos tantos componentes todo el rato solo se crean una vez)
	
    private final MiTextField renderTF = new MiTextField();
    
    private final MiSpinnerPersonas spinner = new MiSpinnerPersonas();
    
    private final JPanel panelFechas = new JPanel(new BorderLayout());
    private final JLabel nochesL = new JLabel();
    private final MiButton botonCalendario = new MiButton();
    private final ImageIcon iconoCalendario;

    private final JPanel panelResenas = new JPanel(new BorderLayout());
    private final MiProgressBarTabla barraResenas = new MiProgressBarTabla();
    
    private final JLabel dineroL = new JLabel();


    public MiTablaResumenApartamentosRenderer() {
        
    	// Configuración de los TextFields
    	
    	renderTF.setBorder(null);
    	renderTF.setHorizontalAlignment(SwingConstants.CENTER);
    	renderTF.setOpaque(true);
    	
    	// Configuración del spinner
    	
    	spinner.setBorder(null);
    	spinner.setOpaque(true);
    	
        // Cargamos la imagen para el boton del panel fechas
    	
        ImageIcon imgTemp = new ImageIcon("resources/images/jcalendar_icon.png");
        iconoCalendario = new ImageIcon(imgTemp.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        
        // Configuración del Panel Fechas
        
        nochesL.setHorizontalAlignment(SwingConstants.CENTER);
        nochesL.setFont(Main.FUENTE);
        
        botonCalendario.setIcon(iconoCalendario);
        botonCalendario.setBorder(null);
        botonCalendario.setContentAreaFilled(false);
        
        panelFechas.setOpaque(true);
        
        // Añadimos los componentes al panel fechas
        
        panelFechas.add(nochesL, BorderLayout.CENTER);
        panelFechas.add(botonCalendario, BorderLayout.EAST);
        
        // Configurar Panel Reseñas
        
        panelResenas.setOpaque(true); 
        panelResenas.add(barraResenas, BorderLayout.CENTER);
        
        // Configurar Label Dinero
        
        dineroL.setHorizontalAlignment(SwingConstants.CENTER);
        dineroL.setFont(Main.FUENTE.deriveFont(20f));
        dineroL.setForeground(new Color(39, 174, 96));
        dineroL.setOpaque(true);
        
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

    	// Lógica para poner los fondos bonitos
    	
    	Color colorFondo;
    	Color colorTexto;
    
    	colorFondo = (row % 2 == 0) ? Color.WHITE : new Color(250, 250, 250);
    	colorTexto = table.getForeground();
    	
    	// Aplicamos los colores a todos los componentes base
    	
    	renderTF.setBackground(colorFondo);
    	renderTF.setForeground(colorTexto);
    	
    	spinner.setBackground(colorFondo);
    	((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setBackground(colorFondo);
    	((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setForeground(colorTexto);
    	
    	panelFechas.setBackground(colorFondo);
    	nochesL.setForeground(colorTexto);
    	
    	barraResenas.setBackground(colorFondo);
    	
    	dineroL.setBackground(colorFondo);
    	
        switch (column) {
            case 0:		// NO CERRAMOS 
            case 1 /*o 0*/:		// Nombre y Precio
            	
                renderTF.setText(value != null ? value.toString() : "");
                return renderTF;

            case 2:				// Capacidad máxima
            	
            	if (value instanceof Integer) {
                    spinner.setValue((int) value);
                } else {
                    spinner.setValue(1); 
                }
                return spinner;

            case 3:				// Nº de Fechas
                
            	int nNoches = 0;
                
            	if (value != null && value instanceof Map) {
                    
            		@SuppressWarnings("unchecked")
                    Map<LocalDate, LocalDate> mapa = (Map<LocalDate, LocalDate>) value;
                    
                	for (LocalDate entrada : mapa.keySet()) {
                        
                		nNoches += (int) ChronoUnit.DAYS.between(entrada, mapa.get(entrada));
                    
                	}
            	
            	}
	            	
                nochesL.setText(nNoches + " noches");
                
                return panelFechas;

            case 4: 			// Reseñas
            	
            	double media = 0;
            	int totalResenas = 0;
                
                if (value != null && value instanceof List) {
                    
                	@SuppressWarnings("unchecked")
                    List<Resena> lista = (List<Resena>) value;
                    
                	totalResenas = lista.size();
                    
                    if (!lista.isEmpty()) {
                    	
                    	for (Resena r : lista) {
                    	
                    		media += r.getEstrellas();
                    	}
                    	
                    	media = media / totalResenas;
                    	
                    }
            	}

                barraResenas.setDatos(media, totalResenas);

                return panelResenas;

            case 5: 			// Dinero total
                
            	double cantidad = 0;
            	
            	if (value != null) {
            		
            		cantidad = (double) value;
            	
            	}
            	
            	dineroL.setText(String.format("%.2f €", cantidad));
            	
                return dineroL;
                
        }

        return new JLabel("");
    }

}