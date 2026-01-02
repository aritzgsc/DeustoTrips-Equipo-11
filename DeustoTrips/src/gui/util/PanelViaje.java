package gui.util;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import db.GestorDB;
import domain.Cliente;
import domain.Compania;
import domain.Viaje;
import domain.Viaje.TipoViaje;
import gui.main.PanelPestanasBusqueda;
import gui.main.PanelResultadosBusqueda;
import gui.main.PanelVolverRegistrarseIniciarSesion;
import gui.main.busqueda.BotonBuscar;
import main.Main;
import main.util.MailSender;

public class PanelViaje extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public static int MODO_RESERVAR = 1;
	public static int MODO_CANCELAR = 2;
	
	public PanelViaje(int idRvaVin, List<Viaje> viajesIda, LocalDate fechaSalidaIda, int nPersonas, int modo) {
		
		// Creación y configuracion del panel principal

		setLayout(new BorderLayout(20, 0));
		setMinimumSize(new Dimension(0, 200));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
		setPreferredSize(new Dimension(0, 200));
		setBorder(BorderFactory.createCompoundBorder(Main.DEFAULT_LINE_BORDER, new EmptyBorder(20, 20, 20, 20)));

		// FIN Creación y configuración del panel principal
		////		
		// Creación del panel para los logos (IZQUIERDA)
		
		JPanel panelLogos = new JPanel();
		panelLogos.setOpaque(false);
		panelLogos.setPreferredSize(new Dimension(100, 0));
		
		List<Compania> companiasViaje = Viaje.getCompaniasViaje(viajesIda);
		int n = companiasViaje.size();
		
		// Logica de distribucion:
		// Si hay 4 o menos, usamos 1 columna.
		// Si hay 5 o más, usamos 2 columnas para aprovechar el ancho.
		
		int columnas = (n > 4) ? 2 : 1;
		
		// Calculamos cuántas filas reales tendremos

		int filas = (int) Math.ceil((double) n / columnas);
		panelLogos.setLayout(new GridLayout(1, columnas, 5, 2)); 
		
		// Calculo del tamaño
		// Espacio vertical útil aprox: 150px
		
		int altoPorFila = 140 / filas;
		
		int maxPorAncho = (columnas == 1) ? 60 : 45; 
		
		// Calculamos tamaño final respetando el alto disponible y el ancho máximo
		int tamanoFinal = Math.min(maxPorAncho, Math.max(25, altoPorFila));
		
		// Si está lleno, añadimos directos
		
		JPanel celda = new JPanel();
		celda.setLayout(new BoxLayout(celda, BoxLayout.Y_AXIS));
		
		celda.add(Box.createVerticalGlue());
		
		for (int i = 0 ; i < companiasViaje.size() ; i++) {
			
		    MiSelectorImagenes logo = new MiSelectorImagenes(Arrays.asList(companiasViaje.get(i).getLogo()), tamanoFinal, tamanoFinal, false, false, true);
		    logo.setToolTipText(companiasViaje.get(i).getNombre());
		    logo.getBotonSeleccionarImagen().setBackground(new Color(0, 0, 0, 0));
		    logo.getBotonSeleccionarImagen().setBorder(null);
		    logo.setBorder(null);
		    
		    celda.add(logo);
		    celda.add(Box.createVerticalStrut(3));
		    
		    if ((celda.getComponentCount() - 1) / 2 >= filas) {
		    	
		    	celda.add(Box.createVerticalGlue()); // Glue final para centrar
		    	panelLogos.add(celda);
		    	
		    	if (i + 1 < n) {
		    		
		    		celda = new JPanel();
		    		celda.setLayout(new BoxLayout(celda, BoxLayout.Y_AXIS));
		    		celda.add(Box.createVerticalGlue());
		    		
		    	}
		    	
		    }
		    
		}
		
		if (panelLogos.getComponentCount() < columnas) {
			
			celda.add(Box.createVerticalGlue());
			panelLogos.add(celda);
			
		}
		
		add(panelLogos, BorderLayout.WEST);
		
		// FIN Creación del panel para los logos
		////
		// Creación del panel central (Datos del trayecto)
		
		JPanel panelCentral = new JPanel(new GridLayout(1, 3, 10, 0)); // 3 columnas: Salida - Duración - Llegada
		panelCentral.setOpaque(false);
		
		// Columna Salida
		
		JPanel panelDatosSalida = new JPanel();
		panelDatosSalida.setLayout(new BoxLayout(panelDatosSalida, BoxLayout.Y_AXIS));
		panelDatosSalida.setOpaque(false);
		
		JLabel horaSalida = new JLabel(viajesIda.getFirst().getHora().toString());
		horaSalida.setFont(Main.FUENTE.deriveFont(Font.BOLD, 28f));
		horaSalida.setAlignmentX(Component.CENTER_ALIGNMENT);
			
		JLabel fechaSalida = new JLabel(fechaSalidaIda.format(DateTimeFormatter.ofPattern("dd MMM")));
		fechaSalida.setFont(Main.FUENTE.deriveFont(16f));
		fechaSalida.setForeground(Color.GRAY);
		fechaSalida.setAlignmentX(Component.CENTER_ALIGNMENT);
			
		JLabel origen = new JLabel(viajesIda.getFirst().getOrigen().getNombre());
		origen.setFont(Main.FUENTE.deriveFont(Font.BOLD, 14f));
		origen.setForeground(Color.GRAY);
		origen.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Añadimos los componentes al panel correspondiente
		
		panelDatosSalida.add(Box.createVerticalGlue());
		panelDatosSalida.add(horaSalida);
		panelDatosSalida.add(fechaSalida);
		panelDatosSalida.add(Box.createVerticalStrut(5));
		panelDatosSalida.add(origen);
		panelDatosSalida.add(Box.createVerticalGlue());
		
		// Columna Central (Duración y Escalas)
		
		JPanel panelDatosViaje = new JPanel();	
		panelDatosViaje.setLayout(new BoxLayout(panelDatosViaje, BoxLayout.Y_AXIS));
		panelDatosViaje.setOpaque(false);
		
		// Cálculo de duración y formato
		
		int minutosTotales = Viaje.getDuracionTotalViaje(viajesIda);
		int horas = minutosTotales / 60;
		int mins = minutosTotales % 60;
		String duracionTexto = String.format("%dh %02dm", horas, mins);
		
		JLabel duracionL = new JLabel(duracionTexto);
		duracionL.setFont(Main.FUENTE.deriveFont(16f));
		duracionL.setForeground(Color.GRAY);
		duracionL.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Decoración visual (una línea o flecha)
		
		JPanel panelIconos = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)) {

			private static final long serialVersionUID = 1L;
			
			// Decoración hecha por GEMINI
			
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				
				// Suavizado
				
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				int h = getHeight();
				int w = getWidth();
				
				// Dibujar Línea Central (Track)
				
				g2.setColor(Color.LIGHT_GRAY);
				g2.setStroke(new BasicStroke(2));
				
				// Dibujamos de lado a lado con un pequeño margen
				
				g2.draw(new Line2D.Float(15, h / 2, w - 15, h / 2));
				
				// Círculos en los extremos de la línea
				
				g2.fillOval(15 - 3, (h/2) - 3, 6, 6);
				g2.fillOval(w - 15 - 3, (h/2) - 3, 6, 6);
				
			}
			
		};
		
		panelIconos.setBorder(new EmptyBorder(12, 0, 0, 0));
		
		Set<TipoViaje> tiposUnicos = new HashSet<>();
		for (Viaje viaje : viajesIda) tiposUnicos.add(viaje.getTipoViaje());
		
		for (TipoViaje tipo : tiposUnicos) {
			
			BufferedImage icono = null;
			
			try {
				
				icono = ImageIO.read(new File("resources/images/" + tipo.toString().toLowerCase() + ".png"));
				
			} catch (IOException e) {
				
				System.err.println("Error al cargar el icono");
				e.printStackTrace();
			
			}
			
			MiSelectorImagenes iconoP = new MiSelectorImagenes(Arrays.asList(icono), 30, 30, false, false, true);
			iconoP.getBotonSeleccionarImagen().setBackground(new Color(0, 0, 0, 0));
			iconoP.getBotonSeleccionarImagen().setBorder(null);
			iconoP.setBorder(null);
			
			iconoP.setOpaque(false); 
			
			panelIconos.add(iconoP);
			
		}
		
		// Lógica de Escalas
		
		int numEscalas = viajesIda.size() - 1;
		JLabel escalasL;
			
		if (numEscalas == 0) {
			escalasL = new JLabel("Directo");
			escalasL.setFont(Main.FUENTE.deriveFont(Font.BOLD, 16f));
			escalasL.setForeground(new Color(46, 125, 50));
		} else {
			String textoEscalas = numEscalas + (numEscalas == 1 ? " escala" : " escalas");
			escalasL = new JLabel(textoEscalas);
			escalasL.setFont(Main.FUENTE.deriveFont(16f));
			escalasL.setForeground(Color.GRAY);
		}
		escalasL.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Añadimos todos los componentes al panel correspondiente
		
		panelDatosViaje.add(Box.createVerticalGlue());
		panelDatosViaje.add(escalasL);
		panelDatosViaje.add(panelIconos);
		panelDatosViaje.add(duracionL);
		panelDatosViaje.add(Box.createVerticalGlue());
		
		// Columna Llegada
		
		JPanel panelDatosLlegada = new JPanel();
		panelDatosLlegada.setLayout(new BoxLayout(panelDatosLlegada, BoxLayout.Y_AXIS));
		panelDatosLlegada.setOpaque(false);
		
		Viaje ultimoViaje = viajesIda.getLast();

		LocalDateTime fechaHoraSalida = LocalDateTime.of(fechaSalidaIda, viajesIda.getFirst().getHora());
		LocalDateTime fechaHoraLlegada = fechaHoraSalida.plusMinutes(minutosTotales);
		
		JLabel horaLlegada = new JLabel(ultimoViaje.getHora().plusMinutes(ultimoViaje.getDuracion()).toString());
		horaLlegada.setFont(Main.FUENTE.deriveFont(Font.BOLD, 28f));
		horaLlegada.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel fechaLlegada = new JLabel(fechaHoraLlegada.format(DateTimeFormatter.ofPattern("dd MMM")));
		fechaLlegada.setFont(Main.FUENTE.deriveFont(16f));
		fechaLlegada.setForeground(Color.GRAY);
		fechaLlegada.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel destino = new JLabel(ultimoViaje.getDestino().getNombre());
		destino.setFont(Main.FUENTE.deriveFont(Font.BOLD, 14f));
		destino.setForeground(Color.GRAY);
		destino.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Añadimos los componentes al panel correspondiente
		
		panelDatosLlegada.add(Box.createVerticalGlue());
		panelDatosLlegada.add(horaLlegada);
		panelDatosLlegada.add(fechaLlegada);
		panelDatosLlegada.add(Box.createVerticalStrut(5));
		panelDatosLlegada.add(destino);
		panelDatosLlegada.add(Box.createVerticalGlue());
		
		// Añadimos las columnas al panel central
		
		panelCentral.add(panelDatosSalida);
		panelCentral.add(panelDatosViaje);
		panelCentral.add(panelDatosLlegada);
		
		add(panelCentral, BorderLayout.CENTER);
		
		// FIN Creación del panel central
		////
		// Creación del panel de reserva (DERECHA)
		
		JPanel panelDerecha = new JPanel();
		panelDerecha.setLayout(new BoxLayout(panelDerecha, BoxLayout.Y_AXIS));
		panelDerecha.setPreferredSize(new Dimension(300, 0));
		panelDerecha.setOpaque(false);
		
		// Datos precio
		
		JLabel personasL = new JLabel(nPersonas + (nPersonas == 1 ? " persona" : " personas"));
		personasL.setAlignmentX(Component.CENTER_ALIGNMENT);
		personasL.setFont(Main.FUENTE.deriveFont(14f));
		personasL.setForeground(Color.GRAY);
		
		double precioTotal = Viaje.calcularPrecioTotal(viajesIda, nPersonas);
		JLabel precioL = new JLabel(String.format("%.2f €", precioTotal));
		precioL.setAlignmentX(Component.CENTER_ALIGNMENT);
		precioL.setFont(Main.FUENTE.deriveFont(Font.BOLD, 24f));
		precioL.setForeground(new Color(50, 50, 50));
		
		// Botones
		
		MiButton detallesB = new MiButton("Ver Detalles");
		detallesB.setFont(Main.FUENTE.deriveFont(16f));
		detallesB.setAlignmentX(Component.CENTER_ALIGNMENT);
		detallesB.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		
		detallesB.addActionListener(e -> {
//			new VentanaDetallesViaje(viajesIda, fechaSalidaIda, nPersonas, modo);
		});
		
		MiButton reservarB = new MiButton("Reservar");
		reservarB.setBackground(new Color(50, 50, 50));
		reservarB.setForeground(Color.WHITE);
		reservarB.setFont(Main.FUENTE.deriveFont(Font.BOLD, 20f));
		reservarB.setAlignmentX(Component.CENTER_ALIGNMENT);
		reservarB.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		reservarB.setEnabled(LocalDateTime.now().isBefore(LocalDateTime.of(fechaSalidaIda, viajesIda.getFirst().getHora())));
		
		reservarB.addActionListener(e -> {
			
			if (PanelVolverRegistrarseIniciarSesion.isSesionIniciada() && LocalDateTime.now().isBefore(LocalDateTime.of(fechaSalidaIda, viajesIda.getFirst().getHora()))) {

				boolean reservaCreadaCorrectamente = GestorDB.crearReservaViajeCompleto(viajesIda, fechaSalidaIda, nPersonas, precioTotal);

				if (reservaCreadaCorrectamente) {

					enviarMensajeReservaIda(viajesIda, fechaSalidaIda, nPersonas);

					BotonBuscar.pararBusqueda();
					PanelResultadosBusqueda.borrarBusqueda();

					PanelPestanasBusqueda.setError("");

				} else {
					
					PanelPestanasBusqueda.setError("Error al reservar el viaje");
					
				}

			} else {

				if (LocalDateTime.now().isAfter(LocalDateTime.of(fechaSalidaIda, viajesIda.getFirst().getHora()))) {
					
					reservarB.setEnabled(false);
					
				}
				
				if (PanelVolverRegistrarseIniciarSesion.isSesionIniciada()) {
				
					PanelPestanasBusqueda.setError("Inicia sesión para reservar");

				}
				
			}
			
		});
		
		MiButton cancelarB = new MiButton("Cancelar");
		cancelarB.setBackground(new Color(50, 50, 50));
		cancelarB.setForeground(Color.WHITE);
		cancelarB.setFont(Main.FUENTE.deriveFont(Font.BOLD, 20f));
		cancelarB.setAlignmentX(Component.CENTER_ALIGNMENT);
		cancelarB.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		cancelarB.setEnabled(LocalDateTime.now().isBefore(LocalDateTime.of(fechaSalidaIda, viajesIda.getFirst().getHora())));
		
		cancelarB.addActionListener(e -> {
			
			if (LocalDateTime.now().isBefore(LocalDateTime.of(fechaSalidaIda, viajesIda.getFirst().getHora()))) {
			
				MiButton botonSi = new MiButton("Si");
				MiButton botonNo = new MiButton("No");
	
				Object[] opciones = { botonSi, botonNo };
	
				JLabel mensaje = new JLabel("¿Estás seguro de que quieres cancelar esta reserva?");
				mensaje.setFont(Main.FUENTE.deriveFont(16f));
	
				JOptionPane pregunta = new JOptionPane(mensaje, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, new ImageIcon(new ImageIcon("resources/images/icono_imagen.jpg").getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH)), opciones, botonSi);
				pregunta.setValue(JOptionPane.NO_OPTION);
				
				botonSi.addActionListener((e1) -> pregunta.setValue(JOptionPane.YES_OPTION));
				botonNo.addActionListener((e1) -> pregunta.setValue(JOptionPane.NO_OPTION));
	
				JDialog dialog = pregunta.createDialog(this, "Confirmación");
				dialog.setVisible(true);
	
				if ((int) pregunta.getValue() == JOptionPane.YES_OPTION) {
	
					boolean reservaCanceladaCorrectamente = GestorDB.cancelarReservaViaje(idRvaVin);
	
					if (reservaCanceladaCorrectamente) {
	
						Container parent = this.getParent();
	
						if (parent != null) {
	
							parent.remove(this);
	
							if (parent.getComponents().length == 0) {
								
								// Si al parent ya no le quedan componentes matamos la ventana
								
								Window ventana = SwingUtilities.getWindowAncestor(parent);			// Para conseguir la ventana sobre la que está el parent (Parent => PanelAlojamiento ; Ventana => VentanaVisualizarReservas (en este caso))
								
								if (ventana != null) {
									ventana.dispose();
								}
								
							}
							
							parent.revalidate();
							parent.repaint();
	
						}
	
						enviarMensajeCancelacionIda(viajesIda, fechaSalidaIda, nPersonas);
	
					}
	
				}
				
			} else {
				
				cancelarB.setEnabled(false);
				
			}
			
		});
		
		// Añadimos los componentes al panel correspondiente
		
		panelDerecha.add(Box.createVerticalGlue()); // Centra verticalmente el bloque
		panelDerecha.add(personasL);
		panelDerecha.add(precioL);
		panelDerecha.add(Box.createVerticalStrut(20));
		panelDerecha.add(detallesB);
		panelDerecha.add(Box.createVerticalStrut(10));
		panelDerecha.add(modo == MODO_RESERVAR? reservarB : cancelarB);
		panelDerecha.add(Box.createVerticalGlue());
		
		add(panelDerecha, BorderLayout.EAST);
		
		// FIN Creación del panel de reserva
		
	}
	
	public static void enviarMensajeReservaIda(List<Viaje> viajes, LocalDate fechaSalida, int nPersonas) {

	    Cliente cliente = PanelVolverRegistrarseIniciarSesion.getCliente();

	    Viaje primerViaje = viajes.getFirst();
	    Viaje ultimoViaje = viajes.getLast();
	    
	    LocalDateTime fechaHoraSalida = LocalDateTime.of(fechaSalida, primerViaje.getHora());
	    
	    int minutosTotales = Viaje.getDuracionTotalViaje(viajes);
	    LocalDateTime fechaHoraLlegada = fechaHoraSalida.plusMinutes(minutosTotales);
	    
	    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd 'de' MMMM");
	    DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

	    int numEscalas = viajes.size() - 1;
	    String textoEscalas = (numEscalas == 0) ? "(Directo)" : "(" + numEscalas + (numEscalas == 1 ? " escala)" : " escalas)");

	    String asunto = "DeustoTrips - Tu viaje de Ida a " + ultimoViaje.getDestino().getNombre();

	    String cuerpoHTML = String.format(
	            """
	            <div style="text-align: center; font-family: 'Comic Sans MS', 'Comic Sans', 'Chalkboard SE', sans-serif; color: #333;">

	                <h1 style="color: #2c3e50;">¡Buen viaje, %s! ✈️</h1>

	                <p style="font-size: 16pt;">Tu trayecto hacia <strong>%s</strong> está confirmado.</p>

	                <hr style="width: 80%%; border: 1px solid #ccc; margin: 20px auto;">

	                <div style="background-color: #f9f9f9; border: 2px dashed #2c3e50; padding: 20px; width: 70%%; margin: 0 auto; border-radius: 10px;">
	                    <h2 style="margin-top: 0;">Detalles del Vuelo/Trayecto</h2>
	                    
	                    <p style="font-size: 15pt; margin: 5px;">🛫 <strong>Salida:</strong> %s</p>
	                    <p style="color: #777; font-size: 12pt; margin-top: 0;">%s a las %s</p>
	                    
	                    <p style="font-size: 15pt; margin: 15px 5px 5px 5px;">🛬 <strong>Llegada:</strong> %s</p>
	                    <p style="color: #777; font-size: 12pt; margin-top: 0;">%s a las %s</p>

	                    <br>
	                    <p style="font-size: 14pt; margin: 5px;">⏱ <strong>Duración:</strong> %dh %02dm <span style="color: #e67e22;">%s</span></p>
	                    
	                    <br>
	                    <p style="font-size: 18pt; margin: 5px; color: #27ae60;">💰 <strong>Precio Total:</strong> %.2f €</p>
	                </div>

	                <hr style="width: 80%%; border: 1px solid #ccc; margin: 20px auto;">

	                <p style="font-size: 14pt;">Prepara las maletas y disfruta con <strong>DeustoTrips</strong>.</p>
	                <p style="font-size: 10pt; color: #777;">Recibirás tus tarjetas de embarque 24h antes.</p>
	            </div>
	            """,
	            cliente.getNombre(),                                      // 1. Nombre Cliente
	            ultimoViaje.getDestino().getNombre(),                     // 2. Ciudad Destino (Intro)
	            
	            primerViaje.getOrigen().getNombre(),                      // 3. Ciudad Origen
	            fechaHoraSalida.format(formatoFecha),                     // 4. Fecha Salida
	            fechaHoraSalida.format(formatoHora),                      // 5. Hora Salida
	            
	            ultimoViaje.getDestino().getNombre(),                     // 6. Ciudad Destino
	            fechaHoraLlegada.format(formatoFecha),                    // 7. Fecha Llegada
	            fechaHoraLlegada.format(formatoHora),                     // 8. Hora Llegada
	            
	            minutosTotales / 60,                                      // 9. Horas duración
	            minutosTotales % 60,                                      // 10. Minutos duración
	            textoEscalas,                                             // 11. Texto Escalas
	            
	            Viaje.calcularPrecioTotal(viajes, nPersonas)              // 12. Precio Total
	    );

	    MailSender.enviarCorreo(cliente.getCorreo(), asunto, cuerpoHTML);
	    
	}
	
	public void enviarMensajeCancelacionIda(List<Viaje> viajesIda, LocalDate fechaSalidaIda, int nPersonas) {

	    Cliente cliente = PanelVolverRegistrarseIniciarSesion.getCliente();

	    // Datos del primer y último tramo para saber Origen y Destino final
	    Viaje primerViaje = viajesIda.getFirst();
	    Viaje ultimoViaje = viajesIda.getLast();
	    
	    // Formateadores
	    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd 'de' MMM yyyy");
	    DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
	    
	    // Calculamos el importe a devolver
	    double precioTotal = Viaje.calcularPrecioTotal(viajesIda, nPersonas);

	    String asunto = "DeustoTrips - Cancelación de viaje a " + ultimoViaje.getDestino().getNombre();

	    String cuerpoHTML = String.format(
	            """
	            <div style="text-align: center; font-family: 'Comic Sans MS', 'Comic Sans', 'Chalkboard SE', sans-serif; color: #333;">

	                <h1 style="color: #c0392b;">Viaje Cancelado ❌</h1>
	                <p style="font-size: 16pt;">Hola %s, tu reserva de transporte ha sido anulada.</p>

	                <hr style="width: 80%%; border: 1px solid #ccc; margin: 20px auto;">

	                <div style="background-color: #fff5f5; border: 2px dashed #c0392b; padding: 20px; width: 75%%; margin: 0 auto; border-radius: 10px; text-align: left;">
	                    <h2 style="margin-top: 0; color: #c0392b; text-align: center;">Detalles de la Cancelación</h2>
	                    
	                    <p style="font-size: 15pt; margin: 10px 0;">🚍 <strong>Trayecto:</strong> %s ➝ %s</p>
	                    <p style="font-size: 15pt; margin: 10px 0;">📅 <strong>Fecha Salida:</strong> %s</p>
	                    <p style="font-size: 15pt; margin: 10px 0;">⏰ <strong>Hora Salida:</strong> %s</p>
	                    <p style="font-size: 15pt; margin: 10px 0;">👥 <strong>Pasajeros:</strong> %d</p>
	                    
	                    <br>
	                    <div style="text-align: center; border-top: 1px solid #c0392b; padding-top: 10px;">
	                        <p style="font-size: 18pt; margin: 5px; color: #c0392b;">💰 <strong>Importe reembolsado:</strong> %.2f €</p>
	                    </div>
	                </div>

	                <hr style="width: 80%%; border: 1px solid #ccc; margin: 20px auto;">

	                <p style="font-size: 14pt;">El reembolso se procesará en los próximos días.</p>
	                <p style="font-size: 12pt; color: #777;">Esperamos verte pronto en <strong>DeustoTrips</strong>.</p>
	            </div>
	            """,
	            cliente.getNombre(),                                    // 1. Nombre
	            primerViaje.getOrigen().getNombre(),                    // 2. Origen
	            ultimoViaje.getDestino().getNombre(),                   // 3. Destino
	            fechaSalidaIda.format(formatoFecha),                    // 4. Fecha
	            primerViaje.getHora().format(formatoHora),              // 5. Hora
	            nPersonas,                                              // 6. Nº Personas
	            precioTotal                                             // 7. Precio
	    );

	    MailSender.enviarCorreo(cliente.getCorreo(), asunto, cuerpoHTML);
	    
	}
	
}
