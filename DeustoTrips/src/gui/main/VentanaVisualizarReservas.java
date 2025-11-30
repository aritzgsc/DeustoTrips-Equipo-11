package gui.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import db.GestorDB;
import gui.util.PanelAlojamiento;

// Ventana que nos permite visualizar todas las reservas de un usuario (el usuario con la sesión iniciada)

public class VentanaVisualizarReservas extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public VentanaVisualizarReservas() {
		
		// Configuración de la ventana
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(1100, 750));
		setLocationRelativeTo(VentanaPrincipal.getVentanaPrincipal());
		setTitle("Mis apartamentos");
				
		// FIN Configuración de la ventana
		////
		// Panel que contendrá todos los apartamentos
		
		// TODO Modificar esto para que acepte viajes (cuando esté hecha esa parte)
		
		JPanel panelAlojamientos = new JPanel();
		panelAlojamientos.setLayout(new BoxLayout(panelAlojamientos, BoxLayout.Y_AXIS));
		
		List<PanelAlojamiento> reservasAlojamientos = GestorDB.getReservasAlojamientos();
		
		for (PanelAlojamiento panel : reservasAlojamientos) {
			
			panelAlojamientos.add(panel);
			
		}
		
		// FIN Panel principal
		////
		// ScrollPane donde se mostrará el panel que tiene todos los apartamentos inicialmente
		
		JScrollPane scrollPaneApartamentos = new JScrollPane(panelAlojamientos);
		
		// FIN ScrollPane
		////
		// Añadimos los componentes a la ventana
		
		add(scrollPaneApartamentos, BorderLayout.CENTER);
		
		// Hacemos visible la ventana
		
		if (reservasAlojamientos.isEmpty()) {
			dispose();
		} else {
			setVisible(true);
		}
		
	}

}
