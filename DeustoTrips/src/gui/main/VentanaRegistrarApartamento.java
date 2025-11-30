package gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import db.GestorDB;
import domain.Ciudad;
import gui.main.busqueda.MiSelectorDestino;
import gui.main.busqueda.MiSpinnerPersonas;
import gui.util.MiButton;
import gui.util.MiSelectorImagenes;
import gui.util.MiTextField;
import main.Main;

// Ventana que nos permite registrar un nuevo apartamento

public class VentanaRegistrarApartamento extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int PRECIO_MAXIMO = 10000;
	
	private MiSelectorImagenes selectorImagenes;
	private MiTextField nombre;
	private MiSelectorDestino selectorCiudad;
	private MiTextField direccion;
	private JTextArea descripcion;
	private MiSpinnerPersonas spinnerCapMax;
	private MiTextField precioNP;
	
	private JLabel error;
	
	public VentanaRegistrarApartamento() {

		// Configuración de la ventana
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(1100, 425));
		setLocationRelativeTo(VentanaPrincipal.getVentanaPrincipal());
		setTitle("Registrar apartamento");
		
		// Panel de contenido (como la clase extiende JFrame hacemos esto para actuar sobre el panel como un panel y luego lo metemos a la ventana)
		
		JPanel contentPane = new JPanel(new BorderLayout(10, 10));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		
		// FIN Configuración de la ventana
		////
		// Error
		
		error = new JLabel("", SwingConstants.CENTER);
		error.setFont(Main.FUENTE.deriveFont(Font.ITALIC).deriveFont(14.f));
		error.setForeground(new Color(0xDC143C));
		error.setBorder(new EmptyBorder(0, 0, 5, 0));
		
		// FIN Error
		// Creación y configuración de los paneles internos
				
		// Panel de la imágen
			
		selectorImagenes = new MiSelectorImagenes(new ArrayList<BufferedImage>(), true);
		
		// FIN Panel de la imágen
		////
		// Panel de datos del alojamiento
				
		JPanel panelDatos = new JPanel(new BorderLayout(0, 10));
		panelDatos.setBorder(new EmptyBorder(10, 5, 10, 5));
		
		// Contenido del panel de datos
		////
		// Creación del panel para el nombre y la ubicación (Agrupados arriba)
		
		JPanel panelArribaDatos = new JPanel(new GridLayout(2, 1, 0, 5));
		
		// TextField nombre
				
		nombre = new MiTextField("Nombre del apartamento");
		nombre.setFont(Main.FUENTE.deriveFont(22.f).deriveFont(Font.BOLD));
				
		// FIN TextField nombre
		////
		// Panel ubicación
		
		JPanel panelUbicacion = new JPanel(new GridLayout(1, 2, 5, 5));
		
		// Creamos un selector de ciudades a partir del selector de destinos que tenemos
		
		selectorCiudad = new MiSelectorDestino("Ciudad", Ciudad.class);
		
		// FIN Creación del selector de ciudades
		////
		// TextField ubicación
		
		direccion = new MiTextField("Dirección");
		direccion.setFont(Main.FUENTE);
				
		// FIN TextField ubicación
		////
		// Añadimos los componentes al panel de ubicación
		
		panelUbicacion.add(selectorCiudad);
		panelUbicacion.add(direccion);
		
		// FIN Panel ubicación
		////
		// Añadimos los componentes al panel superior
		
		panelArribaDatos.add(nombre);
		panelArribaDatos.add(panelUbicacion);
		
		// FIN Panel superior
		////
		// TextArea descripción
		
		descripcion = new JTextArea();
		descripcion.setText("Descripción corta");
		descripcion.setFont(Main.FUENTE.deriveFont(16.f));
		descripcion.setLineWrap(true);
		descripcion.setWrapStyleWord(true);
		descripcion.setMargin(new Insets(5, 10, 5, 10));
		descripcion.setBorder(Main.DEFAULT_LINE_BORDER);
				
		JScrollPane descSP = new JScrollPane(descripcion);
		descSP.setBorder(null);
				
		// Añadimos componentes al panel de datos
		
		descripcion.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				
				if (descripcion.getText().equals("Descripción corta")) {
					
					descripcion.setText("");
					descripcion.setForeground(Color.BLACK);
					
				}
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				
				if (descripcion.getText().equals("")) {
					
					descripcion.setText("Descripción corta");
					descripcion.setForeground(Color.GRAY);
					
				}
			
			}
		
		});
		
		// FIN TextArea descripción
		////
		// Añadimos todos los componentes al panel de datos (Norte y Centro)
				
		panelDatos.add(panelArribaDatos, BorderLayout.NORTH);
		panelDatos.add(descSP, BorderLayout.CENTER);
				
		// FIN Panel de datos del alojamiento
		////
		// Panel de reserva
				
		JPanel panelDerecha = new JPanel(new GridLayout(2, 1, 5, 5));
		panelDerecha.setBorder(new EmptyBorder(10, 5, 10, 10));
		panelDerecha.setPreferredSize(new Dimension(300, 300));
		
		// Contenido del panel de reserva
		
		// Panel datos derecha
		
		JPanel panelDatosDerecha = new JPanel(new GridLayout(2, 1, 5, 5));
		
		// Spinner Capacidad Máxima 
		
		spinnerCapMax = new MiSpinnerPersonas();
		
		// FIN Spinner Capacidad Máxima
		// Precio / noche TextField
				
		precioNP = new MiTextField("Precio (€) / noche");
		precioNP.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if (!Character.toString(e.getKeyChar()).matches("[\\d\\.]") || Double.parseDouble(precioNP.getText() + e.getKeyChar()) > PRECIO_MAXIMO) {
					e.consume();
				}
			}
			
		});
		precioNP.setHorizontalAlignment(SwingConstants.CENTER);
		precioNP.setFont(Main.FUENTE.deriveFont(20.f).deriveFont(Font.BOLD));
		
		// FIN Precio / noche
		// Añadimos los componentes al panel de datos
		
		panelDatosDerecha.add(spinnerCapMax);
		panelDatosDerecha.add(precioNP);
		
		// FIN Panel datos derecha
		////
		// Boton registrar
				
		MiButton botonRegistrar = new MiButton("Registrar");
		botonRegistrar.setFont(Main.FUENTE.deriveFont(22.f).deriveFont(Font.BOLD));
		botonRegistrar.setBackground(new Color(50, 50, 50));
		botonRegistrar.setForeground(Color.WHITE);
		botonRegistrar.addActionListener((e) -> {
					
			String errorStr = setError();
			
			// Comprobamos también que la descripción no sea el placeholder
			boolean descripcionValida = !descripcion.getText().equals("Descripción corta") && !descripcion.getText().isBlank();
			
			if (PanelVolverRegistrarseIniciarSesion.isSesionIniciada() && errorStr.equals("<html><p style=\"text-align: center;\"></p></html>") && descripcionValida) {
					
				boolean apartamentoRegistradoCorrectamente = GestorDB.registrarApartamento(nombre.getText(), direccion.getText(), descripcion.getText(), Double.parseDouble(precioNP.getText()), spinnerCapMax.getNPersonas(), selectorImagenes.getImagenes(), (Ciudad) selectorCiudad.getDestinoSeleccionado());
						
				if (apartamentoRegistradoCorrectamente) {
					
					dispose();
					
				}
					
			} else if (!descripcionValida && errorStr.equals("<html><p style=\"text-align: center;\"></p></html>")) {
				error.setText("<html><p style=\"text-align: center;\">Escriba una breve descripción de su apartamento</p></html>");
			}
					
		});
				
		// FIN Boton registrar
		////
		// Añadimos los componentes al panel de reserva
				
		panelDerecha.add(panelDatosDerecha);
		panelDerecha.add(botonRegistrar);
				
		// FIN Creación y configuración de los paneles internos
		////
		// Añadimos todos los paneles internos al panel principal
		
		add(error, BorderLayout.NORTH);
		add(selectorImagenes, BorderLayout.WEST);
		add(panelDatos, BorderLayout.CENTER);
		add(panelDerecha, BorderLayout.EAST);

		// Hacemos la ventana visible
		
		setVisible(true);
		
	}
	
	public String setError() {
		
		String errorStr = ("<html><p style=\"text-align: center;\">" + (selectorImagenes.getImagenes().isEmpty()? "Seleccione al menos una imágen para su apartamento, " : "") +
						  (nombre.getText().equals("") || nombre.getText().equals("Nombre del apartamento")? "Escriba un nombre para su apartamento, " : "") + 
						  (selectorCiudad.getDestinoSeleccionado() == null || selectorCiudad.getDestinoSeleccionado().isDefaultAns()? "Seleccione la ciudad en la que se encuentra el apartamento, " : "") + 
						  (direccion.getText().equals("") || direccion.getText().equals("Ubicación")? "Escriba la dirección de su apartamento " : "") + 
						  (descripcion.getText().equals("") || descripcion.getText().equals("Descripción corta")? "Escriba una breve descripción de su apartamento, " : "") + 
						  (precioNP.getText().equals("") || precioNP.getText().equals("Precio (€) / noche") || Double.parseDouble(precioNP.getText()) == 0 || Double.parseDouble(precioNP.getText()) > PRECIO_MAXIMO? "Introduzca un precio por noche y persona válido (0 ; " + PRECIO_MAXIMO + "], " : "") + "</p></html>"
						  ).replaceAll(",(?=([^,]*$))", "").replaceAll(",(?=([^,]*$))", " y");

		error.setText(errorStr);

		return errorStr;
		
	}
	
}
