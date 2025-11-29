package gui.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;

import domain.Apartamento;
import gui.main.VentanaMostrarResenas;
import gui.util.editores.MiEditorCalendario;
import gui.util.editores.MiSpinnerEditor;
import gui.util.editores.MiTextFieldEditor;
import gui.util.models.MiTablaResumenApartamentosModel;
import gui.util.renderers.MiTablaResumenApartamentosRenderer;
import main.Main;

public class MiTablaResumenApartamentos extends JTable {

	private static final long serialVersionUID = 1L;
	
	public MiTablaResumenApartamentos(Map<Apartamento, Double> dineroGenPorApartamento, List<Apartamento> apartamentosListaOrdenada) {
		
		// Asignamos el modelo a la tabla
		
		MiTablaResumenApartamentosModel modelo = new MiTablaResumenApartamentosModel(dineroGenPorApartamento, apartamentosListaOrdenada);
		setModel(modelo);
		
		// Configuramos la visualización general de la tabla
		
		setRowHeight(50); 
		setShowVerticalLines(false);
		setGridColor(new Color(230, 230, 230));
		setBorder(Main.DEFAULT_LINE_BORDER);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		getColumnModel().getColumn(0).setPreferredWidth(230); // Nombre
		getColumnModel().getColumn(1).setPreferredWidth(140); // Precio
		getColumnModel().getColumn(2).setPreferredWidth(140);  // Capacidad
		getColumnModel().getColumn(3).setPreferredWidth(250); // Noches
		getColumnModel().getColumn(4).setPreferredWidth(220); // Reseñas
		getColumnModel().getColumn(5).setPreferredWidth(120); // Ganancias
		
		// Configuramos la visualización general de la cabecera de la tabla
		
		JTableHeader header = getTableHeader();
		header.setReorderingAllowed(false);
		header.setFont(Main.FUENTE.deriveFont(14f));
		header.setBackground(Color.WHITE);
		header.setForeground(Color.DARK_GRAY);
		header.setPreferredSize(new Dimension(0, 60));
		header.setBorder(Main.DEFAULT_LINE_BORDER);
		
		// Creación del renderer
		
		MiTablaResumenApartamentosRenderer renderer = new MiTablaResumenApartamentosRenderer();
		
		// Le asignamos el renderer a todas las columnas de la tabla (hacemos esto para que asigne a las columnas con clases Integer o Double que por defecto utilizan sus propios renderers)
		
		for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
			getColumnModel().getColumn(i).setCellRenderer(renderer);
		}
		
		// Asignamos los editores a las columnas correspondientes
		
		getColumnModel().getColumn(0).setCellEditor(new MiTextFieldEditor());
		getColumnModel().getColumn(1).setCellEditor(new MiTextFieldEditor(true));
		
		getColumnModel().getColumn(2).setCellEditor(new MiSpinnerEditor());
		
		getColumnModel().getColumn(3).setCellEditor(new MiEditorCalendario());
		
		// Añadimos un MouseListener para que cuando se haga click en la 5ta columna se nos cree una instancia de VentanaMostrarResenas del apartamento que esté en la fila que hemos clicado
		
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				int fila = rowAtPoint(e.getPoint());
				int columna = columnAtPoint(e.getPoint());
				
				if (fila != -1 && columna == 4 && !apartamentosListaOrdenada.get(fila).getResenas().isEmpty()) {
					
					new VentanaMostrarResenas(apartamentosListaOrdenada.get(fila));
					
				}
			}

		});
		
	}
}