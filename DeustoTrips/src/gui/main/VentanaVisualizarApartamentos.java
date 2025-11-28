package gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import db.GestorDB;
import domain.Apartamento;
import gui.util.PanelEditarApartamento;
import main.Main;

public class VentanaVisualizarApartamentos extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private Map<Apartamento, PanelEditarApartamento> cachePaneles = new HashMap<>();
	
	public VentanaVisualizarApartamentos() {
		
		// Configuración de la ventana
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(1100, 800));
		setLocationRelativeTo(VentanaPrincipal.getVentanaPrincipal());
		setTitle("Mis apartamentos");
				
		// FIN Configuración de la ventana
		////
		// Panel que contendrá todos los apartamentos
		
		JPanel panelApartamentos = new JPanel();
		panelApartamentos.setLayout(new BoxLayout(panelApartamentos, BoxLayout.Y_AXIS));
		
		Map<Apartamento, Double> apartamentos = GestorDB.getApartamentos(PanelVolverRegistrarseIniciarSesion.getCliente());
		
		Set<Apartamento> apartamentosKeySet = new TreeSet<Apartamento>(new Comparator<Apartamento>() {

			@Override
			public int compare(Apartamento a1, Apartamento a2) {			// Por dinero generado, Por nota (Descendente), Por nº de reseñas (Descendente), Por nombre (Ascendente)
				
				double dinero1 = (apartamentos.get(a1) != null) ? apartamentos.get(a1) : 0;
		        double dinero2 = (apartamentos.get(a2) != null) ? apartamentos.get(a2) : 0;
				
		        int comparacionDinero = Double.compare(dinero2, dinero1);
		        
		        if (comparacionDinero != 0) {
		        	return comparacionDinero;
		        }
		        
		        int comparacionNota = Double.compare(a2.calcularNotaMedia(), a1.calcularNotaMedia());
		        
		        if (comparacionNota != 0) {
		            return comparacionNota;
		        }
		        
		        int tamano1 = (a1.getResenas() != null) ? a1.getResenas().size() : 0;
		        int tamano2 = (a2.getResenas() != null) ? a2.getResenas().size() : 0;
		        
		        int comparacionCantidad = Integer.compare(tamano2, tamano1);
		        
		        if (comparacionCantidad != 0) {
		            return comparacionCantidad;
		        }
		  
		        return a1.getNombre().toLowerCase().compareTo(a2.getNombre().toLowerCase());
				
			}
			
		});
		
		apartamentosKeySet.addAll(apartamentos.keySet());
		
		ArrayList<Apartamento> apartamentosListaOrdenada = new ArrayList<Apartamento>(apartamentosKeySet);
		
		for (Apartamento apartamento : apartamentosListaOrdenada) {
			
			PanelEditarApartamento panel = new PanelEditarApartamento(apartamento, apartamentos.get(apartamento));
			cachePaneles.put(apartamento, panel);
			
			panelApartamentos.add(panel);
			
		}
		
		// FIN Panel principal
		////
		// ScrollPane donde se mostrará el panel que tiene todos los apartamentos inicialmente
		
		JScrollPane scrollPaneApartamentos = new JScrollPane(panelApartamentos);
		
		// FIN ScrollPane
		////
		// Tabla resumen de los apartamentos mostrados (inicialmente no colocada) TODO
		
//		MiTablaResumenApartamentos resumenApartamentos = new MiTablaResumenApartamentos(apartamentos, apartamentosListaOrdenada);
		
		// FIN Tabla TODO
		////
		// Creación del botón que mostrará el resumen de los apartamentos en esta misma ventana
		
		JToggleButton botonResumen = new JToggleButton("Mostrar resumen") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {

				g.setColor(new Color(50, 50, 50));
				g.fillRect(0, 0, getWidth(), getHeight());
				
				super.paintComponent(g);
			}
			
		};
		botonResumen.setContentAreaFilled(false);
		botonResumen.setBorderPainted(false);
		botonResumen.setFocusable(false);
		botonResumen.setBackground(new Color(50, 50, 50));
		botonResumen.setForeground(Color.WHITE);
		botonResumen.setFont(Main.FUENTE.deriveFont(Font.BOLD, 24f));
		botonResumen.setPreferredSize(new Dimension(Integer.MAX_VALUE, 75));
		
		botonResumen.addActionListener((e) -> {
			
			if (botonResumen.isSelected()) {
				
				botonResumen.setText("Ocultar resumen");
				
//				scrollPaneApartamentos.setViewportView(resumenApartamentos);
				
			} else {
				
				botonResumen.setText("Mostrar resumen");
				
				panelApartamentos.removeAll();
				panelApartamentos.setLayout(new BoxLayout(panelApartamentos, BoxLayout.Y_AXIS));
					
				for (Apartamento apartamento : apartamentosListaOrdenada) {
					
					PanelEditarApartamento panel = cachePaneles.get(apartamento);
					
					panel.actualizarDatos();
					
					panelApartamentos.add(panel);
						
				}
				
				scrollPaneApartamentos.setViewportView(panelApartamentos);
				
			}
			
			scrollPaneApartamentos.revalidate();
			scrollPaneApartamentos.repaint();
			
		});
		
		// FIN Botón
		////
		// Añadimos los componentes a la ventana
		
		add(scrollPaneApartamentos, BorderLayout.CENTER);
		add(botonResumen, BorderLayout.SOUTH);
		
		// Hacemos visible la ventana
		
		setVisible(true);
		
	}
	
}
