package gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;

import domain.Cliente;
import gui.util.MiButton;
import gui.util.MiMenuItem;
import gui.util.MiSelectorImagenes;
import main.Main;

public class BotonPopUpAccionesPerfil extends MiButton {

	private static final long serialVersionUID = 1L;

	public BotonPopUpAccionesPerfil(Cliente cliente) {
		
		setMargin(new Insets(0, 0, 0, 0));
		setLayout(new BorderLayout(0, 0));
		
		String nombreMostrar = cliente.getNombre() + " ";
		
		for (String inicialAp : cliente.getApellidos().split(" ")) {
			nombreMostrar += inicialAp.charAt(0);
		}
		
		JLabel nombre = new JLabel(nombreMostrar);
		nombre.setHorizontalAlignment(JLabel.CENTER);
		nombre.setBackground(Color.WHITE);
		nombre.setFont(Main.FUENTE);
		
		add(nombre);
		add(new MiSelectorImagenes(new ArrayList<BufferedImage>(Arrays.asList(cliente.getImagen())), cliente, 64, 64, false, false, true), BorderLayout.EAST);
		
		addActionListener((e) -> {
			
			// Creación del popupMenu 
			
			JPopupMenu accionesPerfil = new JPopupMenu();
			accionesPerfil.setBorder(Main.DEFAULT_LINE_BORDER);
			
			// FIN Creación del popupMenu
			////
			// Creación de los componentes que irán dentro del popupMenu y les asignamos sus respectivas acciones
			
			MiMenuItem modificarCuenta = new MiMenuItem("Mi cuenta", getWidth(), 40);
			MiMenuItem visualizarReservas = new MiMenuItem("Mis reservas", getWidth(), 40);
			MiMenuItem visualizarApartamentos = new MiMenuItem("Mis apartamentos", getWidth(), 40);
			MiMenuItem cerrarSesion = new MiMenuItem("Cerrar sesión", getWidth(), 40);
			
			modificarCuenta.addActionListener((e1) -> new VentanaModificarCuenta());
			visualizarReservas.addActionListener((e1) -> new Thread(() -> new VentanaVisualizarReservas()).start());				// Lo llamamos en un hilo aparte porque es muy costoso
			visualizarApartamentos.addActionListener((e1) -> new Thread(() -> new VentanaVisualizarApartamentos()).start());		// Lo llamamos en un hilo aparte porque es muy costoso
			cerrarSesion.addActionListener((e1) -> PanelVolverRegistrarseIniciarSesion.cerrarSesion());
			
			// FIN Creación de los componentes del popupMenu
			////
			// Metemos todos los componentes dentro del popupMenu
			
			accionesPerfil.add(modificarCuenta);
			accionesPerfil.add(visualizarReservas);
			accionesPerfil.add(visualizarApartamentos);
			accionesPerfil.add(cerrarSesion);
			
			// Mostramos el popupMenu
			
			accionesPerfil.setPopupSize(getWidth(), 160);
			accionesPerfil.show(this, 0, getHeight());
			
		});
		
	}
	
}
