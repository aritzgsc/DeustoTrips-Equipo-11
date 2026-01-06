package gui.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JProgressBar;
import javax.swing.border.MatteBorder;

import main.Main;

// Para no hacerlo en la misma clase de la tabla (código feo)

public class MiProgressBarTabla extends JProgressBar {
    
	private static final long serialVersionUID = 1L;
	
	private double media;
    private int total;

    public MiProgressBarTabla() {
    	
        super(0, 500);
        
        setPreferredSize(new Dimension(150, 40));
        setForeground(new Color(255, 193, 7));
        setStringPainted(false);
        setBorder(new MatteBorder(0, 1, 0, 1, new Color(0x7A8A99)));
        
    }

    public void setDatos(double media, int total) {
    	
        this.media = media;
        this.total = total;
        setValue((int) (media * 100));
        
    }

    // Para pintar la media y el total de reseñas
    
    @Override
    protected void paintComponent(Graphics g) {
    	
    	Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);	// Para pintar el resto de la barra
        
        // Mejoramos la calidad del dibujado
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Para pintar el String como lo queremos -> X.X (X)
        
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