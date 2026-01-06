package gui.main;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import db.GestorDB;
import domain.Apartamento;
import domain.Cliente;
import domain.ReservaAp;
import gui.util.MiButton;
import gui.util.PanelItemChat;
import main.Main;

public class BotonPopUpMensajes extends MiButton {

    private static final long serialVersionUID = 1L;

    private int nuevosMensajesTotal = GestorDB.getNMensajesNuevos(null, null);
    public List<ReservaAp> reservas;
    
    public Thread hiloActualizar;
    
    public BotonPopUpMensajes(List<ReservaAp> reservas) {

    	this.reservas = reservas;
    	
    	// Configuración del botón
        
        setIcon(new ImageIcon("resources/images/mensajes.png")); 
        setPreferredSize(new Dimension(70, 70));
        
        hiloActualizar = new Thread(() -> {
           
        	try {
        		
                while (!Thread.currentThread().isInterrupted()) {
                	
                    Thread.sleep(2000);
                    
                    int check = GestorDB.getNMensajesNuevos(null, null);
                    
                    if (check != nuevosMensajesTotal) {
                    	
                        nuevosMensajesTotal = check;
                        repaint();
                    
                    }
                    
                    List<ReservaAp> reservasCheck = GestorDB.getApartamentosReservados();
                    
                    if (!reservasCheck.equals(reservas)) {
                    	
                    	this.reservas = GestorDB.getApartamentosReservados();
                    	revalidate();
                    	repaint();
                    	
                    }
                    
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        	
        });
        
        hiloActualizar.start();
        
        // FIN Configuración del botón
        
        addActionListener((e) -> {
            
        	// Creación del popupMenu
        	
            JPopupMenu popupMenu = new JPopupMenu();
            popupMenu.setBorder(Main.DEFAULT_LINE_BORDER);
            popupMenu.setBackground(Color.WHITE);
            
            // Panel que contendrá la información que mostraremos
            
            JPanel panelLista = new JPanel();
            panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
            panelLista.setBackground(Color.WHITE);

            // Llenamos el panel
            
            if (this.reservas == null || this.reservas.isEmpty()) {
            	
                JMenuItem itemVacio = new JMenuItem("No tienes conversaciones activas");
                itemVacio.setEnabled(false);
                itemVacio.setFont(Main.FUENTE);
                itemVacio.setBackground(Color.WHITE);
                panelLista.add(itemVacio);
                
            } else {
            	
                for (ReservaAp reserva : this.reservas) {
                	
                    JPanel chat = new JPanel();
                    chat.setLayout(new BorderLayout());
                    chat.setBackground(Color.WHITE);
                    chat.setPreferredSize(new Dimension(532, 160));
                    
                    // Añadimos el panel personalizado dentro del item
                    
                    chat.add(new PanelItemChat(reserva.getApartamento(), reserva.getCliente()));
                    
                    // Acción al clickar
                    
                    chat.addMouseListener(new MouseAdapter() {

						@Override
						public void mouseClicked(MouseEvent e) {
						
							Apartamento apartamento = reserva.getApartamento();
                        	
                            Cliente yo = PanelVolverRegistrarseIniciarSesion.getCliente();
                            Cliente otro = null;
                            
                            // Lógica de destinatario
                            if (!apartamento.getPropietario().equals(yo)) {
                            	
                                otro = apartamento.getPropietario();
                                
                            } else {
                            	
                            	otro = reserva.getCliente();

                            }
                            
                            if (otro != null) {
                            	
                                new VentanaChat(yo, otro, apartamento);
                                popupMenu.setVisible(false);
                                
                            }
                            
						}

						@Override
						public void mouseEntered(MouseEvent e) {
							chat.setBackground(new Color(0xB8CFE5));
						}

						@Override
						public void mouseExited(MouseEvent e) {
							chat.setBackground(Color.WHITE);
						}
                    	
                    });
                    
                    panelLista.add(chat);
                    
                }
            }
            
            // Crear el scrollPane donde irá la lista por si hay muchas conversaciones
            
            JScrollPane scrollPane = new JScrollPane(panelLista);
            scrollPane.setBorder(null);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);

            // Calculamos la altura dinamicamente
            
            int alturaReal = panelLista.getPreferredSize().height;
            int alturaFinal = Math.min(alturaReal, 482);
            
            // Aseguramos un mínimo por si está vacío
            
            if (alturaFinal < 30) alturaFinal = 30; 

            scrollPane.setPreferredSize(new Dimension(532, alturaFinal));

            // Añadimos el ScrollPane al PopupMenu
            
            popupMenu.add(scrollPane);
            
            // Mostrar
            
            popupMenu.show(this, 0, getHeight());
            
        });
    }
    
    public void matarHilo() {
    	hiloActualizar.interrupt();
    }
    
    // Bolita roja diseñada por GEMINI

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (nuevosMensajesTotal > 0) {
        	
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int diametro = 22;
            int x = getWidth() - diametro - 5; 
            int y = 5;
            
            g2.setColor(new Color(220, 50, 50));
            g2.fillOval(x, y, diametro, diametro);
            
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x, y, diametro, diametro);
            
            g2.setFont(Main.FUENTE.deriveFont(Font.BOLD, 12f));
            String texto = nuevosMensajesTotal > 9 ? "+9" : String.valueOf(nuevosMensajesTotal);
            
            int textWidth = g2.getFontMetrics().stringWidth(texto);
            int textHeight = g2.getFontMetrics().getAscent();
            
            g2.drawString(texto, x + 1 + (diametro - textWidth) / 2, y + (diametro + textHeight) / 2 - 2);
            
        }
        
    }
    
}