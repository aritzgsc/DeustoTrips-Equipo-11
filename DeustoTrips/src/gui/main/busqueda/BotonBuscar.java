package gui.main.busqueda;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;

import db.GestorDB;
import domain.Alojamiento;
import domain.Apartamento;
import domain.Destino;
import domain.Hotel;
import domain.Viaje;
import domain.Viaje.TipoViaje;
import gui.main.PanelAlojamientos;
import gui.main.PanelPestanasBusqueda;
import gui.main.PanelResultadosBusqueda;
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
			
				pararBusqueda();
				
				PanelResultadosBusqueda.borrarBusqueda();
				
				// Hilo principal
				
				hiloIniciarBusqueda = new Thread (() -> {

					if (panelSeleccionado instanceof PanelAlojamientos) {
						
						PanelAlojamientos panelAlojamientos = (PanelAlojamientos) panelSeleccionado;
						
						Destino destino = panelAlojamientos.getDestinoSeleccionado();
						LocalDate fechaEntrada = panelAlojamientos.getFechaEntrada();
						LocalDate fechaSalida = panelAlojamientos.getFechaSalida();
						int nPersonas = panelAlojamientos.getNPersonas();
						int precioMin = panelAlojamientos.getPrecioMin();
						int precioMax = panelAlojamientos.getPrecioMax();
						double valoracion = panelAlojamientos.getValoracionMin();
						
						panelAlojamientos.setInfo("Buscando Alojamientos en " + destino.toString());
						
						Map<Class<? extends Alojamiento>, List<Integer>> idsAlojamientosEncontrados = GestorDB.buscarIdsAlojamientos(destino, fechaEntrada, fechaSalida, nPersonas, precioMin, precioMax, valoracion);
						
						// Creamos un pool de 4 hilos para cargar alojamientos más rápidamente (si no solo de 1 en 1 => lento)
						
						executorBusqueda = Executors.newFixedThreadPool(4);
						
						if (!idsAlojamientosEncontrados.get(Apartamento.class).isEmpty() || !idsAlojamientosEncontrados.get(Hotel.class).isEmpty()) {
						
							for (Class<? extends Alojamiento> clase : idsAlojamientosEncontrados.keySet()) {
								
								List<Integer> listaIds = idsAlojamientosEncontrados.get(clase);
								
								for (Integer id : listaIds) {
									
									if (Thread.currentThread().isInterrupted()) {
										pararBusqueda();
										return;
									}
									
									// Creamos los hilos que cargarán todos los alojamientos encontrados, gracias al pool de 4 hilos se encolan todas las tareas y se van ejecutando siempre y cuando haya al menos 1 hilo de los 4 libre
									
									executorBusqueda.submit(() -> {
										
										Alojamiento alojamiento = GestorDB.getAlojamiento(clase, id, false);
										
										if (Thread.currentThread().isInterrupted()) {
											return;
										}
										
										panelAlojamientos.setInfo("Cargando Alojamientos de " + destino.toString());
										
										SwingUtilities.invokeLater(() -> {
											
											// Añadimos el panel (y lo recuperamos por medio de la función de PanelResultadosBusqueda para cargarle el resto de imágenes ya que en la función solo estamos cargando una)
											
											PanelAlojamiento panelCreado = PanelResultadosBusqueda.anadirAlojamientoEncontrado(alojamiento, fechaEntrada, fechaSalida, nPersonas);
											
											panelCreado.cargarImagenesRestantes();
											
											if (alojamiento.getId() == listaIds.getLast()) panelAlojamientos.setError("");
											
										});
										
									});
									
								}
								
							}
						
						} else {
							
							panelAlojamientos.setError("No se han encontrado Alojamientos en " + destino.toString());
							
						}
						
						executorBusqueda.shutdown();
						
					} else if (panelSeleccionado instanceof PanelViajes) {
						
						PanelViajes panelViajes = (PanelViajes) panelSeleccionado;
						
						Destino origen = panelViajes.getOrigenSeleccionado();
						Destino destino = panelViajes.getDestinoSeleccionado();
						String tipo = panelViajes.getTipo();
						LocalDate fechaIda = panelViajes.getFechaIda();
						LocalDate fechaVuelta = null;
						if (tipo.equals("Ida y Vuelta")) fechaVuelta = panelViajes.getFechaVuelta();
						int nPersonas = panelViajes.getNPersonas();
						int precioMin = panelViajes.getPrecioMin();
						int precioMax = panelViajes.getPrecioMax();
						List<TipoViaje> tiposViaje = panelViajes.getTiposViaje();
						
						panelViajes.setInfo("Buscando Viajes de " + origen.toString() + " a " + destino.toString());
						
						List<List<Viaje>> viajesDisponiblesIda = new ArrayList<List<Viaje>>();
//						List<List<Viaje>> viajesDisponiblesIda = GestorDB.generarViajes(origen, destino, fechaIda, nPersonas, precioMin, precioMax, tiposViaje);
						viajesDisponiblesIda.sort(Main.comparadorViajesCompletos);
						
						List<List<Viaje>> viajesDisponiblesVuelta = new ArrayList<List<Viaje>>();
						
						if (tipo.equals("Ida y Vuelta")) {
							
//							viajesDisponiblesVuelta = GestorDB.generarViajes(destino, origen, fechaVuelta, nPersonas, precioMin, precioMax, tiposViaje);
							viajesDisponiblesVuelta.sort(Main.comparadorViajesCompletos);
						
						}
						
						if (!viajesDisponiblesIda.isEmpty()) {
							
							int contadorMostrar = 0;
							
							panelViajes.setInfo("Cargando Viajes de " + origen.toString() + " a " + destino.toString());
							
							for (List<Viaje> ida : viajesDisponiblesIda) {
								
								if (Thread.currentThread().isInterrupted()) {
									pararBusqueda();
									return;
								}
								
								if (contadorMostrar >= 30) break;
								
								if (viajesDisponiblesVuelta == null) {
									
									PanelResultadosBusqueda.anadirViajeEncontrado(ida, null, fechaIda, null, nPersonas);
									contadorMostrar++;
									
								} else {
									
									if (!viajesDisponiblesVuelta.isEmpty()) {
										
										for (List<Viaje> vuelta : viajesDisponiblesVuelta) {
											
											if (Thread.currentThread().isInterrupted()) {
												pararBusqueda();
												return;
											}
											
											PanelResultadosBusqueda.anadirViajeEncontrado(ida, vuelta, fechaIda, fechaVuelta, nPersonas);
											contadorMostrar++;
		
											if (contadorMostrar > 30 || contadorMostrar % 4 == 0) break;
											
										}
										
									} else {
										
										panelViajes.setError("No se han encontrado viajes de vuelta desde " + destino.toString() + " hasta " + origen.toString());
										break;
										
									}
									
								}
								
							} 
							
							panelViajes.setError("");
							
						} else {
							
							panelViajes.setError("No se han encontrado viajes de ida desde " + origen.toString() + " hasta " + destino.toString());
							
						}
						
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
