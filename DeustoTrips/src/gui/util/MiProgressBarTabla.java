package gui.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JProgressBar;

import main.Main;

public class MiProgressBarTabla extends JProgressBar {
    
	private static final long serialVersionUID = 1L;
	
	private double media;
    private int total;

    public MiProgressBarTabla() {
    	
        super(0, 500);
        
        setPreferredSize(new Dimension(150, 40));
        setForeground(new Color(255, 193, 7));
        setBorderPainted(false);
        setStringPainted(false);
        
    }

    public void setDatos(double media, int total) {
    	
        this.media = media;
        this.total = total;
        setValue((int) (media * 100));
        
    }

    @Override
    protected void paintComponent(Graphics g) {
    	
        super.paintComponent(g);	// Para pintar el resto de la barra
        
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setFont(Main.FUENTE.deriveFont(16f));
        String texto = String.format("%.1f (%d)", media, total);
        
        // Centrar texto
        FontMetrics fm = g2.getFontMetrics();
        
        int x = (getWidth() - fm.stringWidth(texto)) / 2;
        int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
        
        g2.setColor(Color.DARK_GRAY);
        g2.drawString(texto, x, y);
        
    }
}