package gui.util.uis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.AbstractButton;
import javax.swing.Icon;

// Diseño hecho por GEMINI

public class IconoCheckbox implements Icon {

    private static final int TAMANO = 14;
    
    private Color colorBorde = new Color(0x7A8A99);
    private Color colorFondoSelected = new Color(50, 150, 250);
    private Color colorTick = Color.WHITE;

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AbstractButton boton = (AbstractButton) c;
        boolean seleccionado = boton.isSelected();

        // Fondo

        if (seleccionado) {

            g2.setColor(colorFondoSelected);
            g2.fillRoundRect(x, y, TAMANO, TAMANO, 4, 4); 
            g2.setColor(colorFondoSelected);
            g2.drawRoundRect(x, y, TAMANO, TAMANO, 4, 4);
            
            // Tick
            
            g2.setColor(colorTick);
            g2.setStroke(new BasicStroke(2.0f)); // Grosor del tick
            
            // Coordenadas a ojo para que quede centrado en 18x18
            // Puntos: (Izquierda, Abajo, Arriba-Derecha)
            
            g2.drawLine(x + 3, y + 7, x + 6, y + 10); // Palito corto
            g2.drawLine(x + 6, y + 10, x + 11, y + 4); // Palito largo
            
        } else {
        	
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(x, y, TAMANO, TAMANO, 4, 4);
            g2.setColor(colorBorde);
            g2.drawRoundRect(x, y, TAMANO, TAMANO, 4, 4);
            
        }
    }

    @Override
    public int getIconWidth() { return TAMANO; }

    @Override
    public int getIconHeight() { return TAMANO; }
}