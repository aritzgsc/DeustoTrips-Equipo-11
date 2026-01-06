package gui.main;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import db.GestorDB;
import domain.Viaje;
import domain.Viaje.TipoViaje;
import gui.main.busqueda.BotonBuscar;
import gui.util.MiButton;
import gui.util.MiSelectorImagenes;
import gui.util.PanelViaje;
import main.Main;

public class VentanaDetallesViaje extends JFrame {

	private static final long serialVersionUID = 1L;

	public static int MODO_RESERVAR = 1;
	public static int MODO_CANCELAR = 2;	
	
	public VentanaDetallesViaje(List<Viaje> viajesIda, LocalDate fechaSalidaIda, int nPersonas, int modo) {
		
		// Configuración básica de la ventana

		setTitle("Viaje a " + viajesIda.getLast().toString());
		setMinimumSize(new Dimension(500, 700));
		setResizable(false);
		setLocationRelativeTo(VentanaPrincipal.getVentanaPrincipal());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		try {
			setIconImage(ImageIO.read(new File("resources/images/logo.jpg")));
		} catch (IOException e) {
			System.err.println("Error al cargar el logo");
			e.printStackTrace();
		}
		
		// FIN Configuración básica de la ventana
		////
		// Creación del panel superior (Resumen)
		
		JPanel panelHeader = new JPanel();
		panelHeader.setLayout(new BoxLayout(panelHeader, BoxLayout.Y_AXIS));
		panelHeader.setBorder(new EmptyBorder(25, 25, 15, 25));
		
		String destinoFinal = viajesIda.getLast().getDestino().getNombre();
		JLabel tituloL = new JLabel("Tu viaje a " + destinoFinal);
		tituloL.setFont(Main.FUENTE.deriveFont(Font.BOLD, 22f));
		tituloL.setAlignmentX(Component.LEFT_ALIGNMENT);
		tituloL.setForeground(new Color(30, 30, 30));
		
		int minutosTotales = Viaje.getDuracionTotalViaje(viajesIda);
		int horas = minutosTotales / 60;
		int mins = minutosTotales % 60;
		int numEscalas = viajesIda.size() - 1;
		
		String resumenTexto = String.format("%s %s · %dh %02d min", numEscalas != 0? Integer.toString(numEscalas) : "", numEscalas != 0? (numEscalas == 1 ? "escala" : "escalas") : "Directo", horas, mins);
		
		JLabel subtituloL = new JLabel(resumenTexto);
		subtituloL.setFont(Main.FUENTE.deriveFont(14f));
		subtituloL.setForeground(Color.GRAY);
		subtituloL.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Añadimos los componentes al panel
		
		panelHeader.add(tituloL);
		panelHeader.add(Box.createVerticalStrut(5));
		panelHeader.add(subtituloL);
		
		add(panelHeader, BorderLayout.NORTH);
		
		// FIN Creación del panel superior
		////
		// Creación del panel central (Lista de tramos con timeline)

		JPanel panelContenido = new JPanel();
		panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
		panelContenido.setBorder(new EmptyBorder(10, 25, 20, 25));
		
		JScrollPane scrollPane = new JScrollPane(panelContenido);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		
		// Lógica de fechas
		
		LocalDate fechaActual = fechaSalidaIda;
		
		for (int i = 0; i < viajesIda.size(); i++) {
			
			Viaje viaje = viajesIda.get(i);
			
			// Si no es el primero, calculamos si ha habido cambio de día respecto al anterior
			
			if (i > 0) {
				
				Viaje anterior = viajesIda.get(i - 1);
				int diasDiferencia = (viaje.getDiaSemana().getId() - anterior.getDiaSemana().getId() + 7) % 7;
				
				// Corrección por si es el mismo día de la semana pero hora anterior (significa 1 semana después)
				if (diasDiferencia == 0 && viaje.getHora().isBefore(anterior.getHora())) {
					diasDiferencia = 7;
				}
				
				fechaActual = fechaActual.plusDays(diasDiferencia);
				
			}
			
			LocalDateTime fechaSalidaViaje = LocalDateTime.of(fechaActual, viaje.getHora());
			LocalDateTime fechaLlegadaViaje = fechaSalidaViaje.plusMinutes(viaje.getDuracion());
			
			// Nodo SALIDA (Círculo + Hora)
			
			boolean tieneLineaArriba = (i > 0);
			boolean arribaPunteada = (i > 0); // Si hay línea arriba, es porque vengo de escala -> punteada
			
			panelContenido.add(crearFilaNodo(viaje.getHora().toString(), fechaSalidaViaje.format(DateTimeFormatter.ofPattern("dd MMM")), viaje.getOrigen().toString(), tieneLineaArriba, arribaPunteada, true, false, null));
			
			// TRAYECTO (Línea + Tarjeta con Info)
			
			JPanel panelTrayecto = new JPanel(new BorderLayout());
			panelTrayecto.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelTrayecto.getPreferredSize().height)); 
			panelTrayecto.setBorder(new EmptyBorder(0, 0, 0, 0)); 
			
			// Línea vertical izquierda
			
			PanelDecoracionTimeline decoracionLinea = new PanelDecoracionTimeline(false, true, false, true, false, viaje.getTipoViaje());
			
			// Tarjeta con Logo e Info
			
			JPanel panelInfo = new JPanel(new BorderLayout());
			panelInfo.setBorder(new EmptyBorder(5, 5, 5, 15));
			
			// Info compañía (Derecha)
			
			JPanel panelInfoViaje = new JPanel(new BorderLayout(15, 0));
			panelInfoViaje.setBorder(new EmptyBorder(10, 10, 10, 10));
			
			// Logo compañía
			
			MiSelectorImagenes logo = new MiSelectorImagenes(Arrays.asList(viaje.getCompania().getLogo()), null, 50, 50, false, false, true);
			logo.getBotonSeleccionarImagen().setBackground(new Color(0, 0, 0, 0));
			logo.getBotonSeleccionarImagen().setBorder(null);
			logo.setBorder(null);
			
			// Texto
			
			JPanel panelInfoTexto = new JPanel(new GridLayout(2, 1, 0, 5));
			
			JLabel nombreAerolinea = new JLabel(viaje.getCompania().getNombre());
			nombreAerolinea.setFont(Main.FUENTE.deriveFont(Font.BOLD, 14f));
			nombreAerolinea.setForeground(new Color(66, 133, 244));
			
			int hVuelo = (int) (viaje.getDuracion() / 60);
			int mVuelo = viaje.getDuracion() % 60;
			JLabel duracionVuelo = new JLabel(String.format("Duración del viaje en %s: %dh %02d min", viaje.getTipoViaje().toString().toLowerCase(), hVuelo, mVuelo));
			duracionVuelo.setFont(Main.FUENTE.deriveFont(12f));
			duracionVuelo.setForeground(Color.GRAY);
			
			panelInfoTexto.add(nombreAerolinea);
			panelInfoTexto.add(duracionVuelo);
			
			// Añadimos los paneles al panel correspondiente
			
			panelInfoViaje.add(logo, BorderLayout.WEST);
			panelInfoViaje.add(panelInfoTexto, BorderLayout.CENTER);
			
			panelInfo.add(panelInfoViaje, BorderLayout.CENTER);

			panelTrayecto.add(decoracionLinea, BorderLayout.WEST);
			panelTrayecto.add(panelInfo, BorderLayout.CENTER);
			
			panelContenido.add(panelTrayecto);
			
			// Nodo LLEGADA
			
			boolean hayMasViajes = i < viajesIda.size() - 1;
			
			panelContenido.add(crearFilaNodo(fechaLlegadaViaje.toLocalTime().toString(), fechaLlegadaViaje.format(DateTimeFormatter.ofPattern("dd MMM")), viaje.getDestino().toString(), true, false, hayMasViajes, true, null));
			
			// ESCALA (Si existe)
			
			if (hayMasViajes) {
				
				Viaje siguiente = viajesIda.get(i + 1);
				
				int diasEscala = (siguiente.getDiaSemana().getId() - viaje.getDiaSemana().getId() + 7) % 7;
				if (diasEscala == 0 && siguiente.getHora().isBefore(viaje.getHora())) diasEscala = 7;
				
				// Calculamos la fecha de salida del siguiente para restar
				
				LocalDateTime salidaSiguiente = LocalDateTime.of(fechaActual.plusDays(diasEscala), siguiente.getHora());
				
				// Ajuste si la llegada fue dia X y la salida es dia X+1 (pero diasEscala da 1)
				
				if (salidaSiguiente.isBefore(fechaLlegadaViaje)) {
					salidaSiguiente = salidaSiguiente.plusDays(7);
				}

				long minutosEscala = Duration.between(fechaLlegadaViaje, salidaSiguiente).toMinutes();
				int hEscala = (int) (minutosEscala / 60);
				int mEscala = (int) (minutosEscala % 60);
				
				// Panel Visual Escala
				
				JPanel panelEscala = new JPanel(new BorderLayout());
				panelEscala.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelEscala.getPreferredSize().height)); 
				
				PanelDecoracionTimeline decoracionPunteada = new PanelDecoracionTimeline(false, true, true, true, true, null);
				
				JLabel labelEscala = new JLabel(String.format("Escala: %dh %02d min", hEscala, mEscala));
				labelEscala.setFont(Main.FUENTE.deriveFont(13f));
				labelEscala.setForeground(new Color(100, 100, 100));
				
				JPanel panelTextoEscala = new JPanel(new BorderLayout());
				panelTextoEscala.setBorder(new EmptyBorder(10, 10, 10, 10));
				panelTextoEscala.add(labelEscala, BorderLayout.WEST);

				panelEscala.add(decoracionPunteada, BorderLayout.WEST);
				panelEscala.add(panelTextoEscala, BorderLayout.CENTER);
				
				panelContenido.add(panelEscala);
			}
			
		}
		
		panelContenido.add(Box.createVerticalGlue());		// Glue final para empujar todo arriba
		
		add(scrollPane, BorderLayout.CENTER);

		// FIN Creación del panel central
		////
		// Creación del panel inferior (Precio y botón)
		
		JPanel panelInferior = new JPanel(new BorderLayout());
		panelInferior.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)), new EmptyBorder(20, 25, 20, 25)));
		
		double precioTotal = Viaje.calcularPrecioTotal(viajesIda, nPersonas);
		
		JPanel panelPrecio = new JPanel();
		panelPrecio.setLayout(new BoxLayout(panelPrecio, BoxLayout.Y_AXIS));
		
		JLabel labelTotal = new JLabel("Total para " + nPersonas + " " + (nPersonas == 1 ? "persona" : "personas"));
		labelTotal.setFont(Main.FUENTE.deriveFont(13f));
		labelTotal.setForeground(Color.GRAY);
		
		JLabel precioL = new JLabel(String.format("%.2f €", precioTotal));
		precioL.setFont(Main.FUENTE.deriveFont(Font.BOLD, 26f));
		precioL.setForeground(new Color(30, 30, 30));
		
		panelPrecio.add(labelTotal);
		panelPrecio.add(precioL);
		
		MiButton reservarB = new MiButton("Reservar Tramo");
		reservarB.setBackground(new Color(50, 50, 50));
		reservarB.setForeground(Color.WHITE);
		reservarB.setFont(Main.FUENTE.deriveFont(Font.BOLD, 18f));
		reservarB.setPreferredSize(new Dimension(220, 45));
		reservarB.setEnabled(modo == MODO_RESERVAR? true : false);
		reservarB.addActionListener(e -> {
			
			if (PanelVolverRegistrarseIniciarSesion.isSesionIniciada()) {

	            boolean reservaCreadacorrectamente = GestorDB.crearReservaViajeCompleto(viajesIda, fechaSalidaIda, nPersonas, precioTotal);
	            
	            if (reservaCreadacorrectamente) {
	                	
	                PanelViaje.enviarMensajeReservaIda(viajesIda, fechaSalidaIda, nPersonas);

	                BotonBuscar.pararBusqueda();
	                PanelResultadosBusqueda.borrarBusqueda();
	                    
	                PanelPestanasBusqueda.setError("");

	                dispose();
	                
	            } else {
	                 PanelPestanasBusqueda.setError("Error al reservar el viaje");
	            }

	        } else {
	            PanelPestanasBusqueda.setError("Inicia sesión para reservar");
	        }
			
		});
		
		panelInferior.add(panelPrecio, BorderLayout.WEST);
		panelInferior.add(reservarB, BorderLayout.EAST);
		
		add(panelInferior, BorderLayout.SOUTH);

		// FIN Creación del panel inferior
		////
		// Hacemos la ventana visible
		
		setVisible(true);
		
	}
	
	// Método auxiliar para crear filas de nodo (Círculo + Hora)
	
	private JPanel crearFilaNodo(String hora, String fecha, String lugar, boolean lineaArriba, boolean arribaPunteada, boolean lineaAbajo, boolean abajoPunteada, TipoViaje tipoViaje) {		
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height)); 
		
		// Decoración izquierda
		
		PanelDecoracionTimeline decoracion = new PanelDecoracionTimeline(true, lineaArriba, arribaPunteada, lineaAbajo, abajoPunteada, tipoViaje);
		
		// Texto
		
		JPanel panelTexto = new JPanel();
		panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
		
		JPanel filaHora = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		filaHora.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel lHora = new JLabel(hora);
		lHora.setFont(Main.FUENTE.deriveFont(Font.BOLD, 16f));
		lHora.setForeground(new Color(30, 30, 30));
		
		JLabel lFecha = new JLabel(fecha);
		lFecha.setFont(Main.FUENTE.deriveFont(13f));
		lFecha.setForeground(Color.GRAY);
		
		filaHora.add(lHora);
		filaHora.add(lFecha);
		
		JLabel lLugar = new JLabel(lugar);
		lLugar.setFont(Main.FUENTE.deriveFont(Font.BOLD, 13f));
		lLugar.setForeground(new Color(80, 80, 80));
		lLugar.setAlignmentX(Component.LEFT_ALIGNMENT);
		lLugar.setBorder(new EmptyBorder(2, 5, 0, 0));
		
		panelTexto.add(filaHora);
		panelTexto.add(lLugar);
		
		// Para evitar estiramientos verticales lo metemos todo a un wrapper
		
		JPanel panelPrincipal = new JPanel(new BorderLayout());
		panelPrincipal.add(decoracion, BorderLayout.WEST);
		panelPrincipal.add(panelTexto, BorderLayout.CENTER);
		
		// CAMBIO IMPORTANTE: Usamos CENTER en lugar de NORTH para que si el panel
		// se estira, la linea vertical ocupe todo el espacio y no queden huecos.
		panel.add(panelPrincipal, BorderLayout.CENTER);
		
		return panel;
	}

	// Clase interna para pintar las líneas y círculos
	
	private class PanelDecoracionTimeline extends JPanel {
			
		private static final long serialVersionUID = 1L;
			
		private boolean dibujarCirculo;
		private boolean lineaArriba;
		private boolean lineaArribaPunteada;
		private boolean lineaAbajo;
		private boolean lineaAbajoPunteada;
		private TipoViaje tipoViaje;
		
		public PanelDecoracionTimeline(boolean dibujarCirculo, boolean lineaArriba, boolean lineaArribaPunteada, boolean lineaAbajo, boolean lineaAbajoPunteada, TipoViaje tipoViaje) {
			this.dibujarCirculo = dibujarCirculo;
			this.lineaArriba = lineaArriba;
			this.lineaArribaPunteada = lineaArribaPunteada;
			this.lineaAbajo = lineaAbajo;
			this.lineaAbajoPunteada = lineaAbajoPunteada;
			this.tipoViaje = tipoViaje;
			setOpaque(false);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(50, super.getPreferredSize().height);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			int h = getHeight();
			int centroX = 25; 
			int centroYCirculo = 20; 
			int radio = 5;

			g2.setColor(Color.GRAY);
			
			Stroke solido = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
			Stroke punteado = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{6}, 0);

			if (!dibujarCirculo) {

				g2.setStroke(lineaArribaPunteada ? punteado : solido);
				g2.draw(new Line2D.Float(centroX, 0, centroX, h));
				
				if (tipoViaje != null && lineaArriba && lineaAbajo && !lineaArribaPunteada && !lineaAbajoPunteada) {
					
					BufferedImage icono = null;
					
					try {
						
						icono = ImageIO.read(new File("resources/images/" + tipoViaje.toString().toLowerCase() + ".png"));
						
					} catch (IOException e) {
						
						System.err.println("Error al cargar el icono");
						e.printStackTrace();
					
					}
					
					if (icono != null) {
					
						int iconW = 25; // Tamaño del icono
						int iconH = 25;
						int iconX = centroX - (iconW / 2);
						int iconY = (h / 2) - (iconH / 2);
						
						g2.setColor(Color.WHITE);
						g2.fillOval(iconX - 3, iconY - 3, iconW + 6, iconH + 6);
						
						// Pintamos el icono
						g2.drawImage(icono, iconX, iconY, iconW, iconH, this);
					
					}
					
				}
				
			} else {
				
				// 1. Línea Arriba
				if (lineaArriba) {
					g2.setStroke(lineaArribaPunteada ? punteado : solido); 
					g2.draw(new Line2D.Float(centroX, 0, centroX, centroYCirculo));
				}

				// 2. Línea Abajo
				if (lineaAbajo) {
					g2.setStroke(lineaAbajoPunteada ? punteado : solido);
					g2.draw(new Line2D.Float(centroX, centroYCirculo, centroX, h));
				}

				// 3. Círculo (encima de las líneas)
				g2.setStroke(new BasicStroke(2));
				g2.setColor(Color.WHITE);
				g2.fillOval(centroX - radio, centroYCirculo - radio, radio * 2, radio * 2);
				g2.setColor(Color.GRAY);
				g2.drawOval(centroX - radio, centroYCirculo - radio, radio * 2, radio * 2);
				
			}
			
		}
		
	}

}