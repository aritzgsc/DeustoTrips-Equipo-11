package gui.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import db.GestorDB;
import domain.Apartamento;
import domain.Ciudad;
import gui.main.PanelVolverRegistrarseIniciarSesion;
import gui.main.VentanaMostrarResenas;
import gui.main.busqueda.MiSelectorDestino;
import gui.main.busqueda.MiSpinnerPersonas;
import main.Main;

public class PanelEditarApartamento extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int PRECIO_MAXIMO = 10000;
	
	private Apartamento apartamento;
	
	private MiSelectorImagenes selectorImagen;
	private MiTextField nombre;
	private MiSelectorDestino selectorCiudad;
	private MiTextField direccion;
	private JTextArea descripcion;
	private MiSpinnerPersonas spinnerCapMax;
	private MiTextField precioNP;
	
	private JLabel error;
	
	public PanelEditarApartamento(Apartamento apartamento, double dineroGenerado) {

		this.apartamento = apartamento;
		
		// Configuración general del panel
		
		setLayout(new BorderLayout(10, 10));
		setBorder(new CompoundBorder(Main.DEFAULT_LINE_BORDER, new EmptyBorder(10, 10, 10, 10)));
		setMinimumSize(new Dimension(1080, 350));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));
		setPreferredSize(new Dimension(1080, 350));
		
		// FIN Configuración general del panel
		////
		// Label de Error
		
		error = new JLabel("", SwingConstants.CENTER);
		error.setFont(Main.FUENTE.deriveFont(Font.ITALIC).deriveFont(14.f));
		error.setForeground(new Color(220, 20, 60));
		error.setBorder(new EmptyBorder(0, 0, 5, 0));
		
		// FIN Label de Error
		////
		// Panel de la imágen
		
		selectorImagen = new MiSelectorImagenes(apartamento.getImagenes(), true);
		
		// FIN Panel de la imágen
		////
		// Panel de datos del alojamiento
		
		JPanel panelDatos = new JPanel(new BorderLayout(0, 10));
		panelDatos.setBorder(new EmptyBorder(10, 5, 10, 5));
		
		// Panel Superior de datos
		
		JPanel panelArribaDatos = new JPanel(new GridLayout(2, 1, 0, 5));
		
		// TextField nombre
		
		nombre = new MiTextField();
		nombre.setText(apartamento.getNombre());
		nombre.setFont(Main.FUENTE.deriveFont(22.f).deriveFont(Font.BOLD));
		
		// Panel ubicación
		
		JPanel panelUbicacion = new JPanel(new GridLayout(1, 2, 5, 5));
		
		// Creamos un selector de ciudades a partir del selector de destinos que tenemos
		
		selectorCiudad = new MiSelectorDestino("Ciudad", Ciudad.class);
		selectorCiudad.setDestinoSeleccionado(apartamento.getCiudad());
		
		// FIN Creación del selector de ciudades
		////
		// TextField ubicación
		
		direccion = new MiTextField();
		direccion.setText(apartamento.getDireccion());
		direccion.setToolTipText("Dirección de la calle");
		
		// FIN TextField ubicación
		////
		// Añadimos los componentes al panel de ubicación
		
		panelUbicacion.add(selectorCiudad);
		panelUbicacion.add(direccion);
		
		// Añadimos componentes al panel superior
		
		panelArribaDatos.add(nombre);
		panelArribaDatos.add(panelUbicacion);
		
		// TextArea descripción
		
		descripcion = new JTextArea();
		descripcion.setText(apartamento.getDescripcion());
		descripcion.setFont(Main.FUENTE.deriveFont(16.f));
		descripcion.setLineWrap(true);
		descripcion.setWrapStyleWord(true);
		descripcion.setMargin(new Insets(5, 10, 5, 10));
		descripcion.setBorder(Main.DEFAULT_LINE_BORDER);
		
		JScrollPane descSP = new JScrollPane(descripcion);
		descSP.setBorder(null);
		
		// Añadimos componentes al panel de datos
		
		panelDatos.add(panelArribaDatos, BorderLayout.NORTH);
		panelDatos.add(descSP, BorderLayout.CENTER);
		
		// FIN Panel de datos del alojamiento
		////
		// Panel Derecho
		
		JPanel panelDerecha = new JPanel();
		panelDerecha.setLayout(new BoxLayout(panelDerecha, BoxLayout.Y_AXIS));
		panelDerecha.setPreferredSize(new Dimension(300, 300));
		panelDerecha.setBorder(new EmptyBorder(10, 5, 10, 10));
		
		// Contenido del panel derecho
		
		// Label Precio
		
		JLabel precioL = new JLabel("Precio / Noche (€)");
		precioL.setFont(Main.FUENTE.deriveFont(12f));
		precioL.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// TextField Precio
		
		precioNP = new MiTextField();
		precioNP.setText(String.valueOf(apartamento.getPrecioNP()));
		precioNP.setFont(Main.FUENTE.deriveFont(Font.BOLD, 18f));
		precioNP.setHorizontalAlignment(SwingConstants.CENTER);
		precioNP.setAlignmentX(Component.LEFT_ALIGNMENT);
		precioNP.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		
		precioNP.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!Character.toString(e.getKeyChar()).matches("[\\d\\.]") || 
					(!precioNP.getText().isEmpty() && Double.parseDouble(precioNP.getText() + e.getKeyChar()) > PRECIO_MAXIMO)) {
					e.consume();
				}
			}
		});
		
		// Label Dinero Generado
		
		JLabel dineroGenTitL = new JLabel("Total Generado:");
		dineroGenTitL.setFont(Main.FUENTE.deriveFont(12f));
		dineroGenTitL.setForeground(Color.GRAY);
		dineroGenTitL.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Label Dinero Generado
		
		JLabel dineroGenL = new JLabel(String.format("%.2f €", dineroGenerado));
		dineroGenL.setFont(Main.FUENTE.deriveFont(Font.BOLD, 20f));
		dineroGenL.setForeground(new Color(39, 174, 96));
		dineroGenL.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Label Capacidad
		
		JLabel capacidadL = new JLabel("Capacidad Máxima");
		capacidadL.setFont(Main.FUENTE.deriveFont(12f));
		capacidadL.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Spinner Capacidad
		
		spinnerCapMax = new MiSpinnerPersonas();
		spinnerCapMax.setValue(apartamento.getCapacidadMax());
		spinnerCapMax.setAlignmentX(Component.LEFT_ALIGNMENT);
		spinnerCapMax.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		
		// Panel rating
		
		JPanel panelRating = new JPanel();
		panelRating.setLayout(new BoxLayout(panelRating, BoxLayout.X_AXIS));
		panelRating.setOpaque(false);
		panelRating.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		double notaMedia = apartamento.calcularNotaMedia();
		
		// Ponemos un label con una estrella por estética

		JLabel estrellaL = new JLabel("★ ");
		estrellaL.setFont(new Font("SansSerif", Font.BOLD, 24));
		estrellaL.setForeground(new Color(255, 193, 7));

		// Label nota
		
		JLabel notaL = new JLabel(String.format("%.1f ", notaMedia));
		notaL.setFont(Main.FUENTE.deriveFont(Font.BOLD, 20f));
		
		// Progress Bar de la valoracion media
		
		JProgressBar ratingPB = new JProgressBar(0, 500);
		ratingPB.setValue((int)(notaMedia * 100));
		ratingPB.setPreferredSize(new Dimension(150, 15));
		ratingPB.setMaximumSize(new Dimension(150, 15));
		ratingPB.setForeground(new Color(255, 193, 7));
		ratingPB.setBorder(Main.DEFAULT_LINE_BORDER);
		ratingPB.setBorderPainted(true);
		
		// Label numero de reseñas a la derecha de la barra
		
		JLabel nResenasL = new JLabel(" (" + apartamento.getResenas().size() + ")");
		nResenasL.setFont(Main.FUENTE.deriveFont(14f));
		nResenasL.setForeground(Color.GRAY);
		
		// Añadimos los componentes al panel de rating
		
		panelRating.add(estrellaL);
		panelRating.add(notaL);
		panelRating.add(ratingPB);
		panelRating.add(nResenasL);
		
		// FIN Panel rating
		////
		// Boton ver reseñas
		
		MiButton botonVerResenas = new MiButton("Ver Reseñas");
		botonVerResenas.setFont(Main.FUENTE.deriveFont(14f));
		botonVerResenas.setAlignmentX(Component.LEFT_ALIGNMENT);
		botonVerResenas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		botonVerResenas.addActionListener(e -> new VentanaMostrarResenas(apartamento));
		
		if (apartamento.getResenas().size() == 0) {
			botonVerResenas.setEnabled(false);
		}
		
		// FIN Boton ver reseñas
		////
		// Boton guardar cambios
		
		MiButton botonGuardar = new MiButton("Guardar Cambios");
		botonGuardar.setFont(Main.FUENTE.deriveFont(Font.BOLD, 18f));
		botonGuardar.setBackground(new Color(50, 50, 50));
		botonGuardar.setForeground(Color.WHITE);
		botonGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);
		botonGuardar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		
		botonGuardar.addActionListener((e) -> {
			
			String errorStr = setError();
			
			if (PanelVolverRegistrarseIniciarSesion.isSesionIniciada() && errorStr.isEmpty()) {
				
				List<BufferedImage> imagenesSeleccionadas = selectorImagen.getImagenes();
				
				boolean apartamentoModificadoCorrectamente = GestorDB.modificarApartamento(apartamento.getId(), nombre.getText(), direccion.getText(), descripcion.getText(), Double.parseDouble(precioNP.getText()), spinnerCapMax.getNPersonas(), imagenesSeleccionadas, (Ciudad) selectorCiudad.getDestinoSeleccionado());
				
				if (apartamentoModificadoCorrectamente) {
					
					apartamento.setNombre(nombre.getText());
					apartamento.setDireccion(direccion.getText());
					apartamento.setDescripcion(descripcion.getText());
					apartamento.setPrecioNP(Double.parseDouble(precioNP.getText()));
					apartamento.setCapacidadMax(spinnerCapMax.getNPersonas());
					apartamento.setImagenes(imagenesSeleccionadas);
					apartamento.setCiudad((Ciudad) selectorCiudad.getDestinoSeleccionado());
					
				}
				
			}
			
		});
		
		// FIN Boton guardar cambios
		////
		// Añadimos los componentes al panel derecho
		
		panelDerecha.add(precioL);
		panelDerecha.add(Box.createVerticalStrut(2));
		panelDerecha.add(precioNP);
		panelDerecha.add(Box.createVerticalStrut(5));
		panelDerecha.add(dineroGenTitL);
		panelDerecha.add(Box.createVerticalStrut(2));
		panelDerecha.add(dineroGenL);
		panelDerecha.add(Box.createVerticalStrut(5));
		panelDerecha.add(capacidadL);
		panelDerecha.add(Box.createVerticalStrut(2));
		panelDerecha.add(spinnerCapMax);
		panelDerecha.add(Box.createVerticalStrut(5));
		panelDerecha.add(panelRating);
		panelDerecha.add(Box.createVerticalStrut(2));
		panelDerecha.add(botonVerResenas);
		panelDerecha.add(Box.createVerticalStrut(5));
		panelDerecha.add(botonGuardar);
		
		// FIN Panel Derecho
		////
		// Añadimos todos los paneles internos al panel principal
		
		add(error, BorderLayout.NORTH);
		add(selectorImagen, BorderLayout.WEST);
		add(panelDatos, BorderLayout.CENTER);
		add(panelDerecha, BorderLayout.EAST);
		
		// Hacemos visible

		setVisible(true);
		
	}
	
	// Método para actualizar datos (de la JTable a este panel)
	
	public void actualizarDatos() {
		
		nombre.setText(apartamento.getNombre());
	    precioNP.setText(String.valueOf(apartamento.getPrecioNP()));
	    spinnerCapMax.setValue(apartamento.getCapacidadMax());
		
	    this.revalidate();
	    this.repaint();
	    
	}
	
	public String setError() {
		
		String errorStr = ("<html><p style=\"text-align: center;\">" + (selectorImagen.getImagenes().isEmpty()? "Seleccione al menos una imágen para su apartamento, " : "") +
						  (nombre.getText().equals("") || nombre.getText().equals("Nombre del apartamento")? "Escriba un nombre para su apartamento, " : "") + 
						  (selectorCiudad.getDestinoSeleccionado() == null || selectorCiudad.getDestinoSeleccionado().isDefaultAns()? "Seleccione la ciudad en la que se encuentra el apartamento, " : "") + 
						  (direccion.getText().equals("") || direccion.getText().equals("Ubicación")? "Escriba la dirección de su apartamento " : "") + 
						  (descripcion.getText().equals("") || descripcion.getText().equals("Descripción corta")? "Escriba una breve descripción de su apartamento, " : "") + 
						  (precioNP.getText().equals("") || precioNP.getText().equals("Precio (€) / noche") || Double.parseDouble(precioNP.getText()) == 0 || Double.parseDouble(precioNP.getText()) > PRECIO_MAXIMO? "Introduzca un precio por noche y persona válido (0 ;" + PRECIO_MAXIMO + "], " : "") + "</p></html>"
						  ).replaceAll(",(?=([^,]*$))", "").replaceAll(",(?=([^,]*$))", " y");

		if (errorStr.equals("<html><p style=\"text-align: center;\"></p></html>")) {
			
			error.setText("");
			return "";
			
		} else {
			
			error.setText(errorStr);
			return errorStr;
			
		}
		
	}
	
}
