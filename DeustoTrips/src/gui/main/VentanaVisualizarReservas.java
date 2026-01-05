package gui.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import db.GestorDB;
import domain.PanelReserva;

// Ventana que nos permite visualizar todas las reservas de un usuario (el usuario con la sesión iniciada)

public class VentanaVisualizarReservas extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public VentanaVisualizarReservas() {
		
		// Configuración de la ventana
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(1100, 750));
		setLocationRelativeTo(VentanaPrincipal.getVentanaPrincipal());
		setTitle("Mis reservas");
		
		try {
			setIconImage(ImageIO.read(new File("resources/images/logo.jpg")));
		} catch (IOException e) {
			System.err.println("Error al cargar el logo");
			e.printStackTrace();
		}
		
		// FIN Configuración de la ventana
		////
		// Panel que contendrá todas las reservas
		
		JPanel panelReservas = new JPanel();
		panelReservas.setLayout(new BoxLayout(panelReservas, BoxLayout.Y_AXIS));
		
		List<PanelReserva> reservas = new ArrayList<PanelReserva>();
		
		reservas.addAll(GestorDB.getReservasAlojamientos());
		reservas.addAll(GestorDB.getReservasViajes());
		
		Collections.sort(reservas);

		for (PanelReserva panel : reservas) {
			
			panelReservas.add(panel);
			
		}
		
		// FIN Panel principal
		////
		// ScrollPane donde se mostrará el panel que tiene todos los apartamentos inicialmente
		
		JScrollPane scrollPaneApartamentos = new JScrollPane(panelReservas);
		
		// FIN ScrollPane
		////
		// Añadimos los componentes a la ventana
		
		add(scrollPaneApartamentos, BorderLayout.CENTER);
		
		// Hacemos visible la ventana
		
		if (reservas.isEmpty()) {
			dispose();
		} else {
			setVisible(true);
		}
		
	}

}
