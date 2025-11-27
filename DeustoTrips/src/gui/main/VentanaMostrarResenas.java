package gui.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import domain.Alojamiento;
import domain.Resena;
import gui.util.models.MiListaResenasModel;
import gui.util.renderers.MiListaResenasRenderer;
import main.Main;

public class VentanaMostrarResenas extends JFrame {

	private static final long serialVersionUID = 1L;

	public VentanaMostrarResenas(Alojamiento alojamiento) {
		
		// Configuración de la ventana
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(720, 620));
		setLocationRelativeTo(VentanaPrincipal.getVentanaPrincipal());
		setResizable(false);
		setTitle("Reseñas de " + alojamiento.getNombre());
		
		// FIN Configuración de la ventana
		////
		// Creación del panel principal
		
		JScrollPane panelResenas = new JScrollPane();
		panelResenas.setBorder(new CompoundBorder(new TitledBorder(Main.DEFAULT_LINE_BORDER, alojamiento.getNombre(), TitledBorder.LEFT, TitledBorder.TOP, Main.FUENTE.deriveFont(Font.BOLD, 16f), Color.BLACK), new EmptyBorder(10, 10, 10, 10)));
		
		// Creación del componente que irá dentro del panel principal
		
		JList<Resena> listaResenas = new JList<Resena>(new MiListaResenasModel(alojamiento.getResenas()));
		
		listaResenas.setCellRenderer(new MiListaResenasRenderer());
		
		// FIN Creacion del componente
		////
		// Añadimos la lista al panel principal
		
		panelResenas.setViewportView(listaResenas);
		
		// Añadimos el panel principal a la ventana
		
		add(panelResenas);
		
		// FIN Creacion del panel principal
		////
		// Hacemos la ventana emergente visible
		
		setVisible(true);
		
	}
	
}
