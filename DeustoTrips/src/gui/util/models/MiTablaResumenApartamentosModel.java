package gui.util.models;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import db.GestorDB;
import domain.Apartamento;

public class MiTablaResumenApartamentosModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private static final String[] cabeceras = {"Nombre AP.", "Precio (€) / noche", "Capacidad", "Noches Reservadas", "Reseñas", "Ganancias"};
    
    private final List<Apartamento> apartamentosListaOrdenada;
    private final Map<Apartamento, Double> dineroGenPorApartamento;
    
    private final Map<Apartamento, Map<LocalDate, LocalDate>> mapaReservasPorApartamento; 

    public MiTablaResumenApartamentosModel(Map<Apartamento, Double> dineroGenPorApartamento, List<Apartamento> apartamentosListaOrdenada) {
        
    	this.dineroGenPorApartamento = dineroGenPorApartamento;
        this.apartamentosListaOrdenada = apartamentosListaOrdenada;
        
        this.mapaReservasPorApartamento = new HashMap<Apartamento, Map<LocalDate, LocalDate>>();
        
        for(Apartamento apartamento : apartamentosListaOrdenada) {
        	
             this.mapaReservasPorApartamento.put(apartamento, GestorDB.getFechasReservas(apartamento));
             
        }
        
    }

    @Override
    public int getRowCount() {
        return apartamentosListaOrdenada.size();
    }

    @Override
    public int getColumnCount() {
        return cabeceras.length;
    }

    @Override
    public String getColumnName(int column) {
        return cabeceras[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        
    	switch (columnIndex) {
        	case 0: return String.class;
            case 1: return Double.class;
            case 2: return Integer.class;
            case 3: return Map.class;
            case 4: return List.class;
            case 5: return Double.class;
            default: return Object.class;
        }
    	
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column < 4; 
    }

    @Override
    public Object getValueAt(int row, int column) {
        Apartamento ap = apartamentosListaOrdenada.get(row);
        
        switch (column) {
            case 0: return ap.getNombre();
            case 1: return ap.getPrecioNP();
            case 2: return ap.getCapacidadMax();
            case 3: return mapaReservasPorApartamento.get(ap);
            case 4: return ap.getResenas();
            case 5: return dineroGenPorApartamento.get(ap);
            default: return null;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
       
    	Apartamento ap = apartamentosListaOrdenada.get(row);
        
        try {
            switch (column) {
                case 0:
                    ap.setNombre((String) value);
                    break;
                case 1:
                    ap.setPrecioNP(Double.parseDouble((String) value));
                    break;
                case 2:
                    ap.setCapacidadMax((int) value);
                    break;
            }
            
            fireTableCellUpdated(row, column);
            
            GestorDB.modificarApartamento(ap.getId(), ap.getNombre(), ap.getDireccion(), ap.getDescripcion(), ap.getPrecioNP(), ap.getCapacidadMax(), ap.getImagenes(), ap.getCiudad());
            
        } catch (Exception e) {
            System.err.println("Error al editar celda: " + e.getMessage());
        }
        
    }
}
