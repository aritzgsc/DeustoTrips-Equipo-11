package gui.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import domain.Cliente;
import gui.util.MiButton;
import main.Main;

// Panel de arriba de la ventana principal, contendrá los botones para volver a la pestaña inicial, iniciar sesión, registrarse ; estos dos ultimos no deberán mostrarse en caso de ya estar con la sesión iniciada

public class PanelVolverRegistrarseIniciarSesion extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static JPanel panelDerecha;
	
	private static MiButton registrarse;
	private static MiButton iniciarSesion;
	
	private static MiButton registrarApartamento;
	
	private static Cliente cliente = null;
	private static boolean sesionIniciada = false;
	
	public PanelVolverRegistrarseIniciarSesion() {
		
		// Configuración del panel
		
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(20, 20, 5, 20));
		
		// FIN Configuración del panel
		////
		// Creación de un panel para los botones de iniciar sesión y registrarse
		
		panelDerecha = new JPanel(new GridLayout(1, 2, 5, 0));
		
		// FIN Creación de un panel para los botones de iniciar sesión y registrarse
		////
		// Creación de los botones de registrarse e iniciar sesión
		
		registrarse = new MiButton("Registrarse");
		registrarse.addActionListener(e -> new VentanaRegistrarse());			// Abrimos ventana emergente configurada en su propia clase
		
		iniciarSesion = new MiButton("Iniciar Sesión");
		iniciarSesion.addActionListener(e -> new VentanaIniciarSesion());		// Abrimos ventana emergente configurada en su propia clase
		
		// FIN Creación de los botones de registrarse e iniciar sesión
		////
		// Creación de los botones que aparecerán cuando esté la sesión iniciada
		
		registrarApartamento = new MiButton("Registrar Apartamento");
//		registrarApartamento.addActionListener((e) -> new VentanaRegistrarApartamento());
		
		// FIN Creación de los botones que aparecerán cuando esté la sesión iniciada
		////
		// Creación del botón volver
		
		MiButton volver = new MiButton(Main.NOMBRE_APP);
		volver.setPreferredSize(new Dimension(350, 50));
		volver.addActionListener(e -> PanelPestanasBusqueda.resetAll());		// Cuando hagamos click en volver se resetearán todas las selecciones y datos escritos
		
		// FIN Creación del botón volver
		////
		// Añadimos los botones a sus correspondientes sitios
		
		add(volver, BorderLayout.WEST);
		add(panelDerecha, BorderLayout.EAST);
		
		panelDerecha.add(registrarse);
		panelDerecha.add(iniciarSesion);
		
	}
	
	public static void iniciarSesion(Cliente nuevoCliente) {
		
		if (!sesionIniciada) {
			
			sesionIniciada = true;
			
			cliente = nuevoCliente;
			
			panelDerecha.removeAll();
			panelDerecha.setLayout(new GridLayout(1, 2, 5, 0));
			panelDerecha.add(registrarApartamento);
			panelDerecha.add(new BotonPopUpAccionesPerfil(nuevoCliente));
			
			panelDerecha.revalidate();
			panelDerecha.repaint();
			
		}
		
	}
	
	public static void cerrarSesion() {
		
		if (sesionIniciada) {
			
			sesionIniciada = false;
			
			cliente = null;
			
			panelDerecha.removeAll();
			panelDerecha.add(registrarse);
			panelDerecha.add(iniciarSesion);
			
			panelDerecha.revalidate();
			panelDerecha.repaint();
			
		}
		
	}
	
	public static Cliente getCliente() {
		return cliente;
	}
	
	public static boolean isSesionIniciada() {
		return sesionIniciada;
	}
	
}
