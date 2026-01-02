package gui.util.uis;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.plaf.basic.BasicTabbedPaneUI;

import gui.main.PanelPestanasBusqueda;

// Clase UI para el panel de las pestañas de búsqueda (Diseñada por GEMINI)

public class MiTabbedPaneUI extends BasicTabbedPaneUI {
	
    public static final Color COLOR_FONDO_APP = new Color(240, 242, 245);
    public static final Color COLOR_TAB_INACTIVO = new Color(230, 230, 230);
    public static final Color COLOR_TAB_ACTIVO = Color.WHITE;
    
    public static final Color COLOR_BORDE = new Color(0x7A8A99); 
    
    public static final Color COLOR_TEXTO_NORMAL = new Color(100, 100, 100);
    public static final Color COLOR_TEXTO_SELECCIONADO = new Color(30, 30, 30);

	private int radioEsquina = 10;

    @Override
    protected void installDefaults() {
    	
        super.installDefaults();
        contentBorderInsets = new Insets(0, 0, 0, 0); 
        tabAreaInsets = new Insets(0, 5, 0, 0);
        
    }

    // Fondo de pestaña
    
    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isSelected) {
        	
            g2.setColor(COLOR_TAB_ACTIVO);
            g2.fillRoundRect(x, y, w, h + 10, radioEsquina, radioEsquina);
            
        } else {
        	
            g2.setColor(COLOR_TAB_INACTIVO);
            g2.fillRoundRect(x, y + 3, w, h, radioEsquina, radioEsquina);
            
        }
    }

    // Borde pestaña
    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(COLOR_BORDE);

        if (isSelected) {
        	
            g2.drawRoundRect(x, y, w - 1, h + 10, radioEsquina, radioEsquina);
            
        } else {
        	
            g2.drawRoundRect(x, y + 3, w - 1, h - 3, radioEsquina, radioEsquina);
            
        }
    }

    // C) PINTAR EL BORDE DEL CONTENIDO (La caja grande)
    @Override
    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Coordenadas del panel de contenido
        int x = 9;
        int y = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight) + 9;
        int w = PanelPestanasBusqueda.getPanelPestanasBusqueda().getWidth() - 1 - 18;
        int h = PanelPestanasBusqueda.getPanelPestanasBusqueda().getHeight() - y - 1 - 8;

        g2.setColor(COLOR_BORDE);
        g2.drawRect(x, y, w, h);
        
        if (selectedIndex >= 0) {
            Rectangle tabRect = getTabBounds(selectedIndex, new Rectangle());
            
            g2.setColor(COLOR_TAB_ACTIVO); // Blanco
            g2.fillRect(tabRect.x + 1, y, tabRect.width - 1, 2);
            
        }
    }
}