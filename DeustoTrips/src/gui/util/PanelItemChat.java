package gui.util;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import db.GestorDB;
import domain.Apartamento;
import domain.Cliente;
import main.Main;

public class PanelItemChat extends JPanel {

    private static final long serialVersionUID = 1L;

    public PanelItemChat(Apartamento apartamento, Cliente cliente) {
        
        // Configuración del panel
    	
        setLayout(new BorderLayout(15, 0));
        setBorder(new EmptyBorder(8, 10, 8, 10));
        setPreferredSize(new Dimension(532, 160));
        setOpaque(false);
        
        // FIN Configuración del panel
        ////
        // Imagenes a la izquierda
        
        MiSelectorImagenes selectorImg = new MiSelectorImagenes(apartamento.getImagenes(), null, 140, 140, false, true, false);        
        add(selectorImg, BorderLayout.WEST);

        // FIN Imagenes
        ////
        // Nombre y ubicación en el centro
        
        JPanel panelTextos = new JPanel(new GridLayout(2, 1, 0, 30));
        panelTextos.setOpaque(false);
        
        // Nombre (Arriba)
        JLabel nombreL = new JLabel(apartamento.getNombre());
        nombreL.setFont(Main.FUENTE.deriveFont(Font.BOLD, 22f));
        nombreL.setVerticalAlignment(JLabel.BOTTOM);
        
        // Ubicación (Abajo)
        JLabel ubicacionL = new JLabel(apartamento.getCiudad().toString() + ", " + apartamento.getDireccion());
        ubicacionL.setFont(Main.FUENTE.deriveFont(Font.PLAIN, 18f));
        ubicacionL.setForeground(Color.GRAY);
        ubicacionL.setVerticalAlignment(JLabel.TOP);
        
        panelTextos.add(nombreL);
        panelTextos.add(ubicacionL);
        
        add(panelTextos, BorderLayout.CENTER);

        // FIN Nombre y ubicación
        ////
        // Número de mensajes nuevos
        
        int mensajesSinLeer = GestorDB.getNMensajesNuevos(apartamento, cliente);

        if (mensajesSinLeer > 0) {
        	
            BadgeMensajes badge = new BadgeMensajes(mensajesSinLeer);
            // Lo metemos en un panel auxiliar para centrarlo verticalmente
            JPanel panelBadge = new JPanel(new BorderLayout()); 
            panelBadge.setOpaque(false);
            panelBadge.add(badge, BorderLayout.CENTER);
            add(panelBadge, BorderLayout.EAST);
            
        }
        
    }

    // Clase para dibujar el circulito rojo con el número de mensajes nuevos para ese apartamento
    
    private class BadgeMensajes extends JPanel {
    	
        private static final long serialVersionUID = 1L;
        
        private int numero;

        public BadgeMensajes(int numero) {
        	
            this.numero = numero;
            setOpaque(false);
            setPreferredSize(new Dimension(65, 65));
            
        }

        // Bolita roja diseñada por GEMINI
        
        @Override
        protected void paintComponent(Graphics g) {
        	
        	Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int diametro = 30;
            int x = 0; 
            int y = (getHeight() - 20) / 2;
            
            g2.setColor(new Color(220, 50, 50));
            g2.fillOval(x, y, diametro, diametro);
            
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x, y, diametro, diametro);
            
            g2.setFont(Main.FUENTE.deriveFont(Font.BOLD, 15f));
            String texto = numero > 9 ? "+9" : String.valueOf(numero);
            
            int textWidth = g2.getFontMetrics().stringWidth(texto);
            int textHeight = g2.getFontMetrics().getAscent();
            
            g2.drawString(texto, x + 1 + (diametro - textWidth) / 2, y + (diametro + textHeight) / 2 - 2);
            
        }
    }
}