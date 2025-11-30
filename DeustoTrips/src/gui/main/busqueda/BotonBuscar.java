package gui.main.busqueda;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;

import db.GestorDB;
import domain.Alojamiento;
import domain.Destino;
import gui.main.PanelAlojamientos;
import gui.main.PanelPestanasBusqueda;
import gui.main.PanelResultadosBusqueda;
import gui.main.PanelViajeAlojamiento;
import gui.main.PanelViajes;
import gui.util.MiButton;
import gui.util.PanelAlojamiento;
import main.Main;

// Clase que implementa un Botón que realiza la búsqueda según los parámetros 

public class BotonBuscar extends MiButton {

	private static final long serialVersionUID = 1L;
	
	// La parte del executorService nos la ha recomendado Gemini para resolver problemas de rendimiento (es basicamente un pool de hilos)(tenemos demasiados hoteles como para hacer esto con un hilo básico)
	
	private static ExecutorService executorBusqueda;
	private static Thread hiloIniciarBusqueda;
	
	public BotonBuscar() {
		
		// Personalización del botón buscar
		
		setBackground(new Color(50, 50, 50));
		setForeground(Color.WHITE);
		setFont(Main.FUENTE.deriveFont(Font.BOLD, 18f));
		setText("Buscar");
		
		// FIN Personalización del botón buscar
		
		addActionListener((e) -> {
			
			if (PanelPestanasBusqueda.setError().length() == 0) {
				
				Component panelSeleccionado = PanelPestanasBusqueda.getPanelPestanasBusqueda().getSelectedComponent();
			
				if (hiloIniciarBusqueda != null && hiloIniciarBusqueda.isAlive()) {
					hiloIniciarBusqueda.interrupt();
				}
				
				if (executorBusqueda != null && !executorBusqueda.isTerminated()) {
					executorBusqueda.shutdownNow();
				}
				
				PanelResultadosBusqueda.borrarBusqueda();
				
				// Hilo principal
				
				Thread hiloIniciarBusqueda = new Thread (() -> {

					if (panelSeleccionado instanceof PanelAlojamientos) {
						
						PanelAlojamientos panelAlojamientos = (PanelAlojamientos) panelSeleccionado;
						
						Destino destino = panelAlojamientos.getDestinoSeleccionado();
						LocalDate fechaEntrada = panelAlojamientos.getFechaEntrada();
						LocalDate fechaSalida = panelAlojamientos.getFechaSalida();
						int nPersonas = panelAlojamientos.getNPersonas();
						int precioMin = panelAlojamientos.getPrecioMin();
						int precioMax = panelAlojamientos.getPrecioMax();
						double valoracion = panelAlojamientos.getValoracionMin();
						
						Map<Class<? extends Alojamiento>, List<Integer>> idsAlojamientosEncontrados = GestorDB.buscarIdsAlojamientos(destino, fechaEntrada, fechaSalida, nPersonas, precioMin, precioMax, valoracion);
						
						// Creamos un pool de 4 hilos para cargar alojamientos más rápidamente (si no solo de 1 en 1 => lento)
						
						executorBusqueda = Executors.newFixedThreadPool(4);
						
						for (Class<? extends Alojamiento> clase : idsAlojamientosEncontrados.keySet()) {
							
							List<Integer> listaIds = idsAlojamientosEncontrados.get(clase);
							
							for (Integer id : listaIds) {
								
								if (Thread.currentThread().isInterrupted()) {
									executorBusqueda.shutdownNow();
									return;
								}
								
								// Creamos los hilos que cargarán todos los alojamientos encontrados, gracias al pool de 4 hilos se encolan todas las tareas y se van ejecutando siempre y cuando haya al menos 1 hilo de los 4 libre
								
								executorBusqueda.submit(() -> {
									
									Alojamiento alojamiento = GestorDB.getAlojamiento(clase, id, false);
									
									if (Thread.currentThread().isInterrupted()) {
										return;
									}
									
									SwingUtilities.invokeLater(() -> {
										
										// Añadimos el panel (y lo recuperamos por medio de la función de PanelResultadosBusqueda para cargarle el resto de imágenes ya que en la función solo estamos cargando una)
										
										PanelAlojamiento panelCreado = PanelResultadosBusqueda.anadirAlojamientoEncontrado(alojamiento, fechaEntrada, fechaSalida, nPersonas);
										
										panelCreado.cargarImagenesRestantes();
										
									});
									
								});
								
							}
							
						}
						
						executorBusqueda.shutdown();
						
					} else if (panelSeleccionado instanceof PanelViajes) {
						
						// TODO Interfaz viajes y cargar viajes a la BD
						
					} else if (panelSeleccionado instanceof PanelViajeAlojamiento) {
						
						// TODO Interfaz viajes y lógica
						
					}
				
				});
				
				hiloIniciarBusqueda.start();
			}
			
		});
		
	}
	
	// Función para interrumpir la búsqueda
	
	public static void pararBusqueda() {
		
		if (hiloIniciarBusqueda != null && hiloIniciarBusqueda.isAlive()) {
			hiloIniciarBusqueda.interrupt();
		}
		
		if (executorBusqueda != null && !executorBusqueda.isTerminated()) {
			executorBusqueda.shutdownNow();
		}
		
	}
	
}
