package gui.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import db.GestorDB;
import domain.Alojamiento;
import domain.Apartamento;
import domain.Cliente;
import domain.Hotel;
import domain.Resena;
import gui.main.PanelPestanasBusqueda;
import gui.main.PanelResultadosBusqueda;
import gui.main.PanelVolverRegistrarseIniciarSesion;
import gui.main.VentanaMostrarResenas;
import main.Main;
import main.util.MailSender;

public class PanelAlojamiento extends JPanel {

	private static final long serialVersionUID = 1L;

	public static final int MODO_RESERVAR = 1;
	public static final int MODO_CANCELAR_O_DEJARRESENA = 2;

	private MiButton resenasB;

	private JLabel notaL;
	private JProgressBar ratingPB;
	
	public PanelAlojamiento(Alojamiento alojamiento, int nPersonas, LocalDate fechaInicio, LocalDate fechaFin, double precioRva, int idRva /*Si no tiene -1*/, Resena resena, int modo) {
		
		int nNoches = (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin);

		// Creación y configuracion del panel principal

		setLayout(new BorderLayout(20, 0));
		setMinimumSize(new Dimension(0, 340));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 340));
		setPreferredSize(new Dimension(0, 340));
		setBorder(BorderFactory.createCompoundBorder(Main.DEFAULT_LINE_BORDER, new EmptyBorder(20, 20, 20, 20)));

		// FIN Creación y configuración del panel principal
		////
		// Creacion del panel de la imágen

		MiSelectorImagenes panelImagen = new MiSelectorImagenes(alojamiento.getImagenes(), false);
		add(panelImagen, BorderLayout.WEST);

		// Panel central (datos apartamento)

		JPanel panelCentro = new JPanel();
		panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
		panelCentro.setOpaque(false);

		// Label nombre

		JLabel nombreL = new JLabel(alojamiento.getNombre());
		nombreL.setFont(Main.FUENTE.deriveFont(Font.BOLD, 24f));
		nombreL.setAlignmentX(Component.LEFT_ALIGNMENT);

		// FIN Label nombre
		////
		// Label ubicacion

		JLabel ubicacionL = new JLabel(alojamiento.getCiudad() + ", " + alojamiento.getDireccion());
		ubicacionL.setFont(Main.FUENTE.deriveFont(18f));
		ubicacionL.setForeground(Color.GRAY);
		ubicacionL.setAlignmentX(Component.LEFT_ALIGNMENT);

		// FIN Label ubicacion
		////
		// JTextArea descripcion

		JTextArea descripcionTxt = new JTextArea(alojamiento.getDescripcion());
		descripcionTxt.setFont(Main.FUENTE.deriveFont(16f));
		descripcionTxt.setLineWrap(true);
		descripcionTxt.setWrapStyleWord(true); // Para que salte de linea la palabra entera si no cabe (sin esto
												// saltaría solo la primera letra que no quepa)
		descripcionTxt.setEditable(false);
		descripcionTxt.setFocusable(false);
		descripcionTxt.setOpaque(false);
		descripcionTxt.setAlignmentX(Component.LEFT_ALIGNMENT);

		// FIN Descripcion
		////
		// Panel rating

		JPanel panelRating = new JPanel();
		panelRating.setLayout(new BoxLayout(panelRating, BoxLayout.X_AXIS));
		panelRating.setOpaque(false);
		panelRating.setAlignmentX(Component.LEFT_ALIGNMENT);

		double notaMedia = alojamiento.calcularNotaMedia();

		// Ponemos un label con una estrella por estética (para que se sepa que hablamos
		// del ratin)

		JLabel estrellaL = new JLabel("★ ");
		estrellaL.setFont(new Font("SansSerif", Font.BOLD, 24)); // 24px
		estrellaL.setForeground(new Color(255, 193, 7));

		// Label nota

		notaL = new JLabel(String.format("%.1f ", notaMedia));
		notaL.setFont(Main.FUENTE.deriveFont(Font.BOLD, 20f)); // 20px

		// Progress Bar de la valoracion media

		ratingPB = new JProgressBar(0, 500);
		ratingPB.setValue((int) (notaMedia * 100));
		ratingPB.setPreferredSize(new Dimension(150, 15));
		ratingPB.setMaximumSize(new Dimension(150, 15));
		ratingPB.setForeground(new Color(255, 193, 7));
		ratingPB.setBorder(Main.DEFAULT_LINE_BORDER);
		ratingPB.setBorderPainted(true);

		// Label numero de reseñas a la derecha de la barra

		JLabel nResenasL = new JLabel(" (" + alojamiento.getResenas().size() + ")");
		nResenasL.setFont(Main.FUENTE.deriveFont(14f));
		nResenasL.setForeground(Color.GRAY);

		// Añadimos los componentes al panel de rating

		panelRating.add(estrellaL);
		panelRating.add(notaL);
		panelRating.add(ratingPB);
		panelRating.add(nResenasL);

		// FIN Panel rating
		////
		// Añadimos todos los componentes en orden al panel central (usamos los
		// componentes extra de Box para estilizar)

		panelCentro.add(nombreL);
		panelCentro.add(Box.createVerticalStrut(5));
		panelCentro.add(ubicacionL);
		panelCentro.add(Box.createVerticalStrut(15));
		panelCentro.add(descripcionTxt);
		panelCentro.add(Box.createVerticalGlue());
		panelCentro.add(panelRating);

		// Añadimos el panel al panel principal

		add(panelCentro, BorderLayout.CENTER);

		// FIN Panel central
		////
		// Panel de la reserva (panel derecha)

		JPanel panelDerecha = new JPanel();
		panelDerecha.setLayout(new BoxLayout(panelDerecha, BoxLayout.Y_AXIS));
		panelDerecha.setPreferredSize(new Dimension(300, 0));
		panelDerecha.setOpaque(false);

		// Label Personas y nº de noches

		String perYNoc = "";
		if (alojamiento instanceof Apartamento) {
			perYNoc = nPersonas + " pers. / " + nNoches + " noches";
		} else {
			int habs = ((Hotel) alojamiento).nHabitacionesOcupadas(nPersonas);
			perYNoc = habs + " hab. / " + nNoches + " noches";
		}

		JLabel perYNocL = new JLabel(perYNoc);
		perYNocL.setAlignmentX(Component.CENTER_ALIGNMENT);
		perYNocL.setFont(Main.FUENTE.deriveFont(14f));
		perYNocL.setForeground(Color.GRAY);

		// Label precio total

		double precioTotal = precioRva;
		JLabel precioL = new JLabel(String.format("%.2f €", precioTotal));
		precioL.setAlignmentX(Component.CENTER_ALIGNMENT);
		precioL.setFont(Main.FUENTE.deriveFont(Font.BOLD, 24f));
		precioL.setForeground(new Color(50, 50, 50));

		// Boton de ver reseñas

		resenasB = new MiButton("Ver Reseñas");
		resenasB.setFont(Main.FUENTE.deriveFont(16f));
		resenasB.setAlignmentX(Component.CENTER_ALIGNMENT);
		resenasB.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
		resenasB.addActionListener(e -> new VentanaMostrarResenas(alojamiento));

		if (alojamiento.getResenas().size() == 0) {
			resenasB.setEnabled(false);
		}

		// Botones que dependen del modo

		// Boton reservar

		MiButton reservarB = new MiButton("Reservar");
		reservarB.setBackground(new Color(50, 50, 50));
		reservarB.setForeground(Color.WHITE);
		reservarB.setFont(Main.FUENTE.deriveFont(Font.BOLD, 20f));
		reservarB.setAlignmentX(Component.CENTER_ALIGNMENT);
		reservarB.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

		reservarB.addActionListener(e -> {

			if (PanelVolverRegistrarseIniciarSesion.isSesionIniciada()) {

				int nPerHab = (alojamiento instanceof Apartamento) ? nPersonas : ((Hotel) alojamiento).nHabitacionesOcupadas(nPersonas);
				boolean reservaCreadaCorrectamente = GestorDB.crearReservaAlojamiento(alojamiento, fechaInicio, fechaFin, nPerHab);

				if (reservaCreadaCorrectamente) {

					enviarMensajeReserva(alojamiento, fechaInicio, fechaFin, nPersonas);
					PanelResultadosBusqueda.borrarBusqueda();

					PanelPestanasBusqueda.setError("");

				}

			} else {

				PanelPestanasBusqueda.setError("Inicia sesión para reservar");

			}
			
		});

		// Botón cancelar

		MiButton cancelarB = new MiButton("Cancelar");
		cancelarB.setBackground(new Color(50, 50, 50));
		cancelarB.setForeground(Color.WHITE);
		cancelarB.setFont(Main.FUENTE.deriveFont(Font.BOLD, 20f));
		cancelarB.setAlignmentX(Component.CENTER_ALIGNMENT);
		cancelarB.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

		cancelarB.addActionListener((e) -> {

			// TODO Cancelar reserva

		});

		// Panel para dejar reseñas
		
		JPanel panelDejarResena = new JPanel(new BorderLayout(0, 15));
		
		PanelSelectorResena panelSelectorResena = new PanelSelectorResena(true, 30);
		if (resena != null) {
			
			panelSelectorResena.setValor(resena.getEstrellas());
			
		}
		
		JTextArea resenaTA = new JTextArea();
		if (resena != null) {
			
			resenaTA.setText(resena.getMensaje());
			
		} else {
			
			resenaTA.setText("Escriba su reseña aqui...");
			
		}
		resenaTA.setFont(Main.FUENTE.deriveFont(16.f));
		resenaTA.setLineWrap(true);
		resenaTA.setWrapStyleWord(true);
		resenaTA.setMargin(new Insets(5, 10, 5, 10));
		resenaTA.setBorder(Main.DEFAULT_LINE_BORDER);
		
		JScrollPane resenaSP = new JScrollPane(resenaTA);
		resenaSP.setBorder(null);
				
		// Añadimos componentes al panel de datos
		
		resenaTA.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				
				if (resenaTA.getText().equals("Escriba su reseña aqui...")) {
					
					resenaTA.setText("");
					resenaTA.setForeground(Color.BLACK);
					
				}
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				
				if (resenaTA.getText().equals("")) {
					
					resenaTA.setText("Escriba su reseña aqui...");
					resenaTA.setForeground(Color.GRAY);
					
				}
			
			}
		
		});
		
		MiButton guardarResB = new MiButton("Guardar reseña");
		guardarResB.setBackground(new Color(50, 50, 50));
		guardarResB.setForeground(Color.WHITE);
		guardarResB.setFont(Main.FUENTE.deriveFont(Font.BOLD, 18f));
		guardarResB.setAlignmentX(Component.CENTER_ALIGNMENT);
		guardarResB.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		
		guardarResB.addActionListener((e) -> {
			
			// TODO Guardar nueva reseña o actualizar la que ya existía
			
		});
		
		// Añadimos los componentes al panel
		
		panelDejarResena.add(panelSelectorResena, BorderLayout.NORTH);
		panelDejarResena.add(resenaSP, BorderLayout.CENTER);
		panelDejarResena.add(guardarResB, BorderLayout.SOUTH);
		
		// Añadimos los componentes de la derecha

		panelDerecha.add(Box.createVerticalStrut(modo == MODO_RESERVAR || (modo == MODO_RESERVAR && LocalDate.now().isBefore(fechaFin))? 25 : 5));
		panelDerecha.add(perYNocL);
		panelDerecha.add(precioL);
		panelDerecha.add(Box.createVerticalStrut(modo == MODO_RESERVAR || (modo == MODO_RESERVAR && LocalDate.now().isBefore(fechaFin))? 30 : 15));
		panelDerecha.add(resenasB);
		panelDerecha.add(Box.createVerticalStrut(modo == MODO_RESERVAR || (modo == MODO_RESERVAR && LocalDate.now().isBefore(fechaFin))? 20 : 15));

		if (modo == MODO_RESERVAR) {

			panelDerecha.add(reservarB);

		} else {

			if (LocalDate.now().isBefore(fechaFin)) {

				panelDerecha.add(cancelarB);

			} else {

				panelDerecha.add(panelDejarResena);

			}

		}

		add(panelDerecha, BorderLayout.EAST);
	}

	// Función que envía un mensaje que notifica al usuario de la reserva

		public void enviarMensajeReserva(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin,
				int nPersonas) {

			Cliente cliente = PanelVolverRegistrarseIniciarSesion.getCliente();

			int nNoches = (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin);

			// Enviamos el mensaje al correoElectronico del cliente con la sesion iniciada
			// (Lo ponemos en formato HTML para que quede más bonito)

			String asunto = "DeustoTrips - Reserva de alojamiento: " + alojamiento.getNombre();

			String cuerpoHTML = String.format(
					"""
												  <div style="text-align: center; font-family: 'Comic Sans MS', cursive; color: #333;">

							<h1 style="color: #2c3e50;">¡Todo listo, %s! ✅</h1>

							<p style="font-size: 16pt;">Tu reserva en <strong>%s</strong> se ha realizado con éxito.</p>

								<hr style="width: 80%%; border: 1px solid #ccc; margin: 20px auto;">

										<div style="background-color: #f9f9f9; border: 2px dashed #2c3e50; padding: 20px; width: 70%%; margin: 0 auto; border-radius: 10px;">
							           	<h2 style="margin-top: 0;">Detalles de la Reserva</h2>
							              <p style="font-size: 15pt; margin: 5px;">🏨 <strong>Alojamiento:</strong> %s</p>
							              <p style="font-size: 15pt; margin: 5px;">📍 <strong>Ubicación:</strong> %s</p>
							              <p style="font-size: 15pt; margin: 5px;">📅 <strong>Entrada:</strong> %s</p>
							              <p style="font-size: 15pt; margin: 5px;">📅 <strong>Salida:</strong> %s</p>
							              <br>
							              <p style="font-size: 18pt; margin: 5px; color: #27ae60;">💰 <strong>Precio Total:</strong> %.2f €</p>
							         </div>

							        <hr style="width: 80%%; border: 1px solid #ccc; margin: 20px auto;">

							      <p style="font-size: 14pt;">Gracias por confiar en <strong>DeustoTrips</strong> para tu próxima aventura.</p>
							      <p style="font-size: 10pt; color: #777;">Si tienes alguna duda, responde a este correo.</p>
							</div>
							      """,
					cliente.getNombre(), // 1. Nombre Cliente
					alojamiento.getNombre(), // 2. Nombre alojamiento (texto)
					alojamiento.getNombre(), // 3. Nombre alojamiento (lista)
					alojamiento.getCiudad() + ", " + alojamiento.getDireccion(), // 4. Ubicación
					fechaInicio, // 5. Fecha Inicio
					fechaFin, // 6. Fecha Fin
					alojamiento.calcularPrecio(nPersonas, nNoches) // 7. Precio total
			);

			MailSender.enviarCorreo(cliente.getCorreo(), asunto, cuerpoHTML);

			// Correo enviado

		}
	
}
