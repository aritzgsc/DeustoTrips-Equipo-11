package gui.main.filtros;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

// Clase que nos permite crear un panel para seleccionar reseñas (nos ayudará para los filtros y para poner reseñas como usuarios)

public class PanelSelectorResena extends JPanel {
	
	public class Estrella extends JButton {

		private static final long serialVersionUID = 1L;
		
		private int posicion;
		private int indice;
		
		public static final int IZQUIERDA = 0;
		public static final int DERECHA = 1;		
		
		// Cargamos las imágenes previamente para que solo tengamos que cargarlas una vez
		
		private ImageIcon estrellaSeleccionadaIzq = new ImageIcon(new ImageIcon("resources/images/estrella_seleccionada_izq.png").getImage().getScaledInstance(25, 50, Image.SCALE_SMOOTH));
		private ImageIcon estrellaSeleccionadaDcha = new ImageIcon(new ImageIcon("resources/images/estrella_seleccionada_dcha.png").getImage().getScaledInstance(25, 50, Image.SCALE_SMOOTH));
		
		private ImageIcon estrellaNoSeleccionadaIzq = new ImageIcon(new ImageIcon("resources/images/estrella_no_seleccionada_izq.png").getImage().getScaledInstance(25, 50, Image.SCALE_SMOOTH));
		private ImageIcon estrellaNoSeleccionadaDcha = new ImageIcon(new ImageIcon("resources/images/estrella_no_seleccionada_dcha.png").getImage().getScaledInstance(25, 50, Image.SCALE_SMOOTH));
		
		public Estrella(int posicion, int indice, boolean enabled) {
			
			this.posicion = posicion;
			this.indice = indice;
			
			setFocusable(false);
			setContentAreaFilled(false);			// Para que el botón no se vea azul al hacer click sobre él
			setBackground(new Color(0xEEEEEE));
			setEnabled(enabled);
			
			// Le ponemos la imágen correspondiente al botón
			
			if (posicion == IZQUIERDA) {
				setIcon(estrellaNoSeleccionadaIzq);
				setBorder(new EmptyBorder(0, 5, 0, 0));
			} else if (posicion == DERECHA) {
				setIcon(estrellaNoSeleccionadaDcha);
				setBorder(new EmptyBorder(0, 0, 0, 5));
			}
			
		}
		
		// Método que nos permite decir si un botón está seleccionado o no
		
		public void setSeleccionado(boolean seleccionado) {
			
			if (seleccionado) {
				if (posicion == IZQUIERDA) {
					setIcon(estrellaSeleccionadaIzq);
				} else if (posicion == DERECHA) {
					setIcon(estrellaSeleccionadaDcha);
				}
			} else {
				if (posicion == IZQUIERDA) {
					setIcon(estrellaNoSeleccionadaIzq);
				} else if (posicion == DERECHA) {
					setIcon(estrellaNoSeleccionadaDcha);
				}
			}
			
		}
		
		public int getIndice() {
			return indice;
		}
		
	}
	
	private static final long serialVersionUID = 1L;
	
	private Estrella[] estrellas = new Estrella[10];
	private int indiceSeleccionado = -1;
	
	public PanelSelectorResena(boolean enabled) {
		
		// Rellenamos el array de estrellas
		
		for (int i = 0; i < estrellas.length; i++) {
			
			if (i % 2 == 0) {
				
				estrellas[i] = new Estrella(Estrella.IZQUIERDA, i, enabled);
				
			} else {
				
				estrellas[i] = new Estrella(Estrella.DERECHA, i, enabled);
				
			}
			
		}
		
		// FIN Rellenamos el array de estrellas
		////
		// Configuramos el panel
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		// FIN Configuramos el panel
		////
		// Añadimos las funcionalidades de las estrellas

		for (Estrella estrella : estrellas) {
			
			estrella.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseEntered(MouseEvent e) {
					pintarEstrellas(estrella.getIndice());
				}

				@Override
				public void mouseExited(MouseEvent e) {
					pintarEstrellas(indiceSeleccionado);
				}
				
			});
			
			estrella.addActionListener((e) -> {
				if (indiceSeleccionado != estrella.getIndice()) {
					indiceSeleccionado = estrella.getIndice();
					pintarEstrellas(indiceSeleccionado);
				} else {
					indiceSeleccionado = -1;
				}
			});
			
		}
		
		// Añadimos las funcionalidades del panel
		////
		// Añadimos las estrellas al panel
		
		for (Estrella estrella : estrellas) {
			
			add(estrella);
			
		}
				
		// FIN Añadimos las estrellas al panel
	}
	
	public void pintarEstrellas(int indice) {
		
		for (int i = 0; i <= indice; i++) {
			estrellas[i].setSeleccionado(true);
		}
		
		if (indice < estrellas.length - 1) {
		
			for (int i = indice + 1; i < estrellas.length; i++) {
				estrellas[i].setSeleccionado(false);
			}
			
		}
		
	}
	
	public int getIndiceSeleccionado() {
		return indiceSeleccionado;
	}
	
	public double getValor() {
		return (indiceSeleccionado + 1) * 0.5;
	}
	
	public void disableAll() {
		for (Estrella estrella : estrellas) {
			estrella.setEnabled(false);
		}
	}
	
	public void enableAll() {
		for (Estrella estrella : estrellas) {
			estrella.setEnabled(true);
		}
	}
	
	public void resetAll() {
		indiceSeleccionado = -1;
	}
	
}
