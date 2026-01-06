package gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import db.GestorDB;
import domain.Apartamento;
import domain.Cliente;
import domain.Mensaje;
import gui.util.MiButton;
import gui.util.MiSelectorImagenes;
import gui.util.MiTextField;
import main.Main;

public class VentanaChat extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel panelChat;
    private MiTextField campoTexto;
    private MiButton enviarB;
    private JScrollPane scroll;
    
    // Datos de la sesión
    private Cliente yo;
    private Cliente otro;
    private Apartamento apartamento;

    // Mensajes en un momento dado
    private List<Mensaje> mensajes = new ArrayList<Mensaje>();
    
    // Hilo timer para actualizar periodicamente el chat (chequeo de nuevos mensajes)
    private Thread hiloActualizar;
    
    public VentanaChat(Cliente yo, Cliente otro, Apartamento apartamento) {
        
    	this.yo = yo;
        this.otro = otro;
        this.apartamento = apartamento;

        Cliente cliente = apartamento.getPropietario().equals(yo)? otro : yo;
        
        // Configuración de la ventana
        
        setTitle("Chat con " + otro.getNombre() + " sobre " + apartamento.getNombre());
        setSize(600, 700);
        setLocationRelativeTo(VentanaPrincipal.getVentanaPrincipal());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
			setIconImage(ImageIO.read(new File("resources/images/logo.jpg")));
		} catch (IOException e) {
			System.err.println("Error al cargar el logo");
			e.printStackTrace();
		}
        
        // FIN Configuración de la ventana
        ////
        // Panel de mensajes enviados
        
        panelChat = new JPanel();
        panelChat.setLayout(new BoxLayout(panelChat, BoxLayout.Y_AXIS));
        scroll = new JScrollPane(panelChat);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
        
        // FIN Panel de mensajes enviados
        ////
        // Panel de envío de mensajes
        
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setPreferredSize(new Dimension(getPreferredSize().width, 80));
        
        campoTexto = new MiTextField();
        campoTexto.removeKeyListener(Main.ANTI_CARACTERES_RAROS);
        campoTexto.addActionListener(e -> enviarMensaje());
        
        enviarB = new MiButton("Enviar");
        enviarB.addActionListener(e -> enviarMensaje());
        
        panelInferior.add(campoTexto, BorderLayout.CENTER);
        panelInferior.add(enviarB, BorderLayout.EAST);
        add(panelInferior, BorderLayout.SOUTH);

        // FIN Panel de envío de mensajes
        ////
        // Carga inicial
        
        actualizarChat(true);
        
        // FIN Carga inicial
        ////
        // Hilo timer periodico (checkeo de nuevos mensajes)

        hiloActualizar = new Thread(() -> {
        	
        	try {
	        	while (!Thread.currentThread().isInterrupted()) {
	        		
					Thread.sleep(2000);
	        		
					Cliente clienteActual = PanelVolverRegistrarseIniciarSesion.getCliente();
					
					if (clienteActual != null && clienteActual.equals(yo)) {
						
						actualizarChat(GestorDB.getNMensajesNuevos(apartamento, cliente) > 0 || GestorDB.getNMensajes(apartamento, cliente) != mensajes.size());
						
					} else {
						
						SwingUtilities.invokeLater(() -> dispose());
						break;
						
					}

	        	}
        	} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
        	
        });
        
        hiloActualizar.start();
        
        // FIN Hilo timer
        ////
        // Hacemos la ventana visible
        
        setVisible(true);
        
    }
    
    private void enviarMensaje() {
    	
        String texto = campoTexto.getText().trim();
        
        if (!texto.isEmpty()) {
        	
            GestorDB.enviarMensaje(texto, yo, otro, apartamento.getId());
            campoTexto.setText("");
            actualizarChat(true); // Refrescar al momento para ver mi propio mensaje
            
        }
        
    }

    private void actualizarChat(boolean actualizar) {
    	
    	List<Mensaje> todosMensajes = GestorDB.cargarChat(yo, otro, apartamento.getId());
        
    	List<Mensaje> nuevosMensajes = new ArrayList<Mensaje>();
    	
    	for (Mensaje mensaje : todosMensajes) {
			if (!mensajes.contains(mensaje)) nuevosMensajes.add(mensaje);
		}
    	
    	mensajes = todosMensajes;
    	
        // Usamos un formateador para la hora del mensaje
        DateTimeFormatter horaFmt = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter fechaFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        List<JPanel> panelesMensajes = new ArrayList<JPanel>();
        
        for (Mensaje mensaje : nuevosMensajes) {
        	
            Cliente emisor = mensaje.getEmisor();
            boolean soyYo = emisor.equals(yo);

            Color colorFondo = soyYo ? new Color(0xD6EAF8) : new Color(0xF2F4F5);

            // Configuración del contenedor de fila (sirve solo para alinear a izquierda o derecha)
            
            JPanel contenedorFila = new JPanel();
            contenedorFila.setLayout(new BoxLayout(contenedorFila, BoxLayout.X_AXIS));
            contenedorFila.setOpaque(false);

            // Configuración del mensaje
            
            JPanel panelMensaje = new JPanel(new BorderLayout(8, 8)); // Gap horizontal y vertical
            panelMensaje.setBackground(colorFondo);
            panelMensaje.setBorder(new CompoundBorder(new LineBorder(new Color(200, 200, 200), 1, true), new EmptyBorder(10, 10, 5, 10)));
            
            // Limitamos el ancho máximo de la burbuja para que no ocupe toda la pantalla en mensajes cortos
            
            panelMensaje.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));

            // Nombre del emisor
            
            JLabel nombreL = new JLabel(emisor.getNombre());
            nombreL.setFont(Main.FUENTE.deriveFont(Font.BOLD, 14f));
            
            if (soyYo) {
                nombreL.setForeground(new Color(80, 80, 80));
                nombreL.setHorizontalAlignment(SwingConstants.RIGHT);
            } else {
                nombreL.setForeground(new Color(180, 80, 0)); 
                nombreL.setHorizontalAlignment(SwingConstants.LEFT);
            }
            
            // Texto
                        
            JTextArea textoArea = new JTextArea(mensaje.getTexto());
            textoArea.setFont(Main.FUENTE.deriveFont(14f));
            textoArea.setLineWrap(true);
            textoArea.setWrapStyleWord(true);
            textoArea.setEditable(false);
            textoArea.setOpaque(false);
            textoArea.setFocusable(false);
            
            // Fecha y hora
            
            String fechaTexto;
            if (mensaje.getFechaHora().toLocalDate().equals(LocalDate.now())) {
            	
                fechaTexto = mensaje.getFechaHora().format(horaFmt); // Si es hoy, solo hora
                
            } else {
            	
                fechaTexto = mensaje.getFechaHora().format(fechaFmt) + " " + mensaje.getFechaHora().format(horaFmt);
                
            }
            
            JLabel fechaL = new JLabel(fechaTexto);
            fechaL.setFont(Main.FUENTE.deriveFont(10f));
            fechaL.setForeground(Color.GRAY);
            fechaL.setHorizontalAlignment(SwingConstants.RIGHT); // Hora siempre a la derecha dentro de la burbuja
            
            // Añadimos texto y fecha a la burbuja
            
            panelMensaje.add(nombreL, BorderLayout.NORTH);
            panelMensaje.add(textoArea, BorderLayout.CENTER);
            panelMensaje.add(fechaL, BorderLayout.SOUTH);

            // FIN Panel mensaje
            ////
            // Avatar de persona
            
            MiSelectorImagenes avatar = new MiSelectorImagenes(Arrays.asList(emisor.getImagen()), emisor, 50, 50, false, false, true);
            avatar.setMinimumSize(new Dimension(50, 50));
            avatar.setMaximumSize(new Dimension(50, 50));
            avatar.setPreferredSize(new Dimension(50, 50));
            avatar.setBackground(new Color(0, 0, 0, 0));
            
            // FIN Avatar de persona
            ////
            // Añadimos todo al contenedor de la fila correctamente
            
            if (soyYo) {
            	
                // Alineación a la derecha
            	
                contenedorFila.add(Box.createHorizontalGlue()); // Empuja todo a la derecha
                contenedorFila.add(panelMensaje);
                contenedorFila.add(Box.createHorizontalStrut(10));
                contenedorFila.add(avatar);
                contenedorFila.add(Box.createHorizontalStrut(10));
                
            } else {
            	
                // Alineación a la izquierda
            	
            	contenedorFila.add(Box.createHorizontalStrut(10));
                contenedorFila.add(avatar);
                contenedorFila.add(Box.createHorizontalStrut(10));
                contenedorFila.add(panelMensaje);
                contenedorFila.add(Box.createHorizontalGlue()); // Empuja todo a la izquierda
                
            }

            panelesMensajes.add(contenedorFila);
            
        }
        
        // Solo actualizamos si el texto ha cambiado para evitar parpadeos
        
        if (actualizar) {
        	
        	for (JPanel mensaje : panelesMensajes) {
				
        		if (!mensaje.equals(panelesMensajes.getFirst())) panelChat.add(Box.createVerticalStrut(15));
        		
        		panelChat.add(mensaje);
        		
        		if (mensaje.equals(panelesMensajes.getLast())) panelChat.add(Box.createVerticalStrut(15));
        		
			}
        	
            SwingUtilities.invokeLater(() -> scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum()));		// Para hacer scroll automático al final de la página
	        
            revalidate();
	        repaint();
             
        }
        
    }
    
    // Paramos el hilo al cerrar la ventana
    @Override
    public void dispose() {	
	    if (hiloActualizar != null && hiloActualizar.isAlive()) hiloActualizar.interrupt();
	    super.dispose();
    }
    
}
