package gui.util.renderers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import domain.Resena;
import gui.util.PanelSelectorResena;
import main.Main;

public class MiListaResenasRenderer implements ListCellRenderer<Resena> {

	@Override
	public Component getListCellRendererComponent(JList<? extends Resena> list, Resena value, int index, boolean isSelected, boolean cellHasFocus) {
		
		// Creación y configuración del panel principal
		
		JPanel resultado = new JPanel();
		resultado.setLayout(new BorderLayout(0, 8)); 
		resultado.setOpaque(true); 
		resultado.setBorder(BorderFactory.createCompoundBorder(Main.DEFAULT_LINE_BORDER, new EmptyBorder(15, 15, 15, 15)));
		
		// FIN Creación y configuración del panel principal
		////
		// Creación del panel cabecera (nombre + fecha)
		
		JPanel panelCabecera = new JPanel(new BorderLayout());
		panelCabecera.setOpaque(false);
		
		// Label nombre
		
		JLabel lblNombre = new JLabel(value.getNombreYApellido());
		lblNombre.setFont(Main.FUENTE.deriveFont(Font.BOLD, 16f));
		lblNombre.setForeground(new Color(30, 30, 30));
		
		// FIN Label nombre
		////
		// Label Fecha
		
		JLabel lblFecha = new JLabel();
		lblFecha.setText(value.getFecha().format(DateTimeFormatter.ofPattern("dd - MM - yyyy")));
		lblFecha.setFont(Main.FUENTE.deriveFont(13f));
		lblFecha.setForeground(Color.GRAY);
		
		// FIN Label fecha
		////
		// Añadimos los componentes al panel cabecera
		
		panelCabecera.add(lblNombre, BorderLayout.WEST);
		panelCabecera.add(lblFecha, BorderLayout.EAST);
		
		// FIN Panel cabecera
		////
		// Creación de los paneles de estrellas 
		
		JPanel panelEstrellasIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
		panelEstrellasIzq.setOpaque(false);
		
		// Creamos el panel que contiene las estrellas con la valoración de la reseña
		
		PanelSelectorResena estrellas = new PanelSelectorResena(false, 18);
		estrellas.setValor(value.getEstrellas());
		estrellas.setOpaque(false);
		
		// FIN panel que contiene las estrellas
		////
		// Añadimos el panel al panel que lo alínea a la izquierda
		
		panelEstrellasIzq.add(estrellas);
		
		// FIN Panel estrellas
		////
		// Label mensaje
		
		JLabel lblMensaje = new JLabel();
		lblMensaje.setFont(Main.FUENTE.deriveFont(Font.PLAIN, 14f));
		lblMensaje.setForeground(new Color(50, 50, 50));
		lblMensaje.setText("<html><body style='width: 300px'>" + value.getMensaje() + "</body></html>");		// HTML Para obligar a saltar de línea cuando se llega a cierto width
		
		// FIN Label mensaje
		////
		// Panel para juntar los dos paneles de cabecera y estrellas
		
		JPanel panelSuperior = new JPanel(new GridLayout(2, 1)); 
		panelSuperior.setOpaque(false);
		panelSuperior.add(panelCabecera);
		panelSuperior.add(panelEstrellasIzq);
		
		// FIN Panel que junta los paneles cabecera y estrellas
		////
		// Añadimos los paneles al panel principal (resultado)
		
		resultado.add(panelSuperior, BorderLayout.NORTH);
		resultado.add(lblMensaje, BorderLayout.CENTER);
		
		return resultado;
		
	}
	
}