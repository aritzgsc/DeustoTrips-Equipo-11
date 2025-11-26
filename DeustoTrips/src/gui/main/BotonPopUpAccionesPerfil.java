package gui.main;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import domain.Cliente;
import gui.util.MiButton;
import gui.util.MiMenuItem;
import main.Main;

public class BotonPopUpAccionesPerfil extends MiButton {

	private static final long serialVersionUID = 1L;

	public BotonPopUpAccionesPerfil(Cliente cliente) {
		
		setText(cliente.getNombre() + " " + cliente.getApellidos().split(" ")[0]); 		// El botón será el nombre y el primer apellido del que haya iniciado sesión
		setHorizontalTextPosition(SwingConstants.LEFT);
		setHorizontalAlignment(SwingConstants.RIGHT);
		
		SwingUtilities.invokeLater(() -> setIcon(new ImageIcon(new ImageIcon("resources/images/icono_imagen.jpg").getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH))));		// El icono del boton será simplemente el icono de la aplicación TODO - CAMBIAR SI ES POSIBLE HACER OTRA COSA (BANDERA/FOTO DE PERFIL DE GOOGLE)
		
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
//			visualizarReservas.addActionListener((e1) -> new VentanaVisualizarReservas());
//			visualizarApartamentos.addActionListener((e1) -> new VentanaVisualizarApartamentos());
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
			accionesPerfil.show(this, 0, 50);
			
		});
		
	}
	
}
