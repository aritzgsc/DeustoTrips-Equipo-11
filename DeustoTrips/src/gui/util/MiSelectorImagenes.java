package gui.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import domain.Cliente;
import gui.main.PanelVolverRegistrarseIniciarSesion;
import gui.main.VentanaPrincipal;
import main.Main;
import main.util.Utilidades;

// Panel que implementa un selector de imágenes con un carrusel

public class MiSelectorImagenes extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private List<BufferedImage> imagenes;
	private JButton botonSeleccionarImagen;
	
	private Runnable carruselImagenes;
	
	private Thread hiloCarruselImagenes;
	
	public MiSelectorImagenes(List<BufferedImage> imagenes, int width, int height, boolean enabled, boolean multiSelect, boolean logo) {

		this.imagenes = imagenes;
		
		// Panel de la imágen

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(width, height));
		if (width == 300 && height == 300) {
			
			setBorder(new EmptyBorder(10, 10, 10, 5));
			
		}
		
		// Creamos el chooser
        
        JFileChooser selectorImagen = new JFileChooser();

        // Configuramos el chooser
        
        selectorImagen.setDialogTitle("Seleccionar Imagenes");
        selectorImagen.setAcceptAllFileFilterUsed(false);
        selectorImagen.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));
        selectorImagen.setFileSelectionMode(JFileChooser.FILES_ONLY);
        selectorImagen.setMultiSelectionEnabled(multiSelect);
        selectorImagen.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());

		// Configuramos la tarea del carrusel en un Runnable para luego poder reutilizar el código (no tener que copiar y pegar)
		
		carruselImagenes = () -> {
			
			int contador = 0;

			if (logo && !imagenes.isEmpty() && imagenes.get(0) == null) {
					
				SwingUtilities.invokeLater(() -> {
						
					Cliente cliente = PanelVolverRegistrarseIniciarSesion.getCliente();
						
					botonSeleccionarImagen.setText(Character.toString(cliente.getNombre().split(" ")[0].toUpperCase().charAt(0)) + Character.toString(cliente.getApellidos().split(" ")[0].toUpperCase().charAt(0)));
					botonSeleccionarImagen.setBackground(cliente.getColor());
					botonSeleccionarImagen.setForeground(Utilidades.getContrastColor(cliente.getColor()));
					botonSeleccionarImagen.setFont(Main.FUENTE.deriveFont(Font.BOLD, 16f));
						
				});
					
				
			} else {
			
				if (logo && !imagenes.isEmpty()) {
					
					if (imagenes.get(0) != null) SwingUtilities.invokeLater(() -> botonSeleccionarImagen.setIcon(new ImageIcon(imagenes.get(0).getScaledInstance(width, height, Image.SCALE_SMOOTH))));
					
				} else {
				
					while (!Thread.currentThread().isInterrupted() && !imagenes.isEmpty()) {
			
						try {
			
							int contadorActual = contador;
								
							if (imagenes.get(contadorActual) != null) SwingUtilities.invokeLater(() -> botonSeleccionarImagen.setIcon(new ImageIcon(imagenes.get(contadorActual).getScaledInstance(width, height, Image.SCALE_SMOOTH))));
			
							Thread.sleep(3000);
			
							contador = (contador + 1) % imagenes.size();
			
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
			
					}
				
				}
				
			}
			
		};
		
		// Label para Efecto Hover 

		JLabel texto = new JLabel("Cambiar imágenes") {
			
			private static final long serialVersionUID = 1L;

			// Cambiamos este método para que no se le pueda clicar (aun estando por encima)
			
			@Override
			public boolean contains(int x, int y) {
				return false;
			}

		};
		texto.setBackground(new Color(0xCB, 0xCB, 0xCB, 128));
		texto.setForeground(new Color(0x353535));
		texto.setFont(Main.FUENTE.deriveFont(14.f));
		texto.setHorizontalAlignment(SwingConstants.CENTER);

		texto.setOpaque(true);

		// Botón para seleccionar la imágen

		botonSeleccionarImagen = new JButton("Subir imágenes") {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean contains(int x, int y) {
				return enabled? super.contains(x, y) : false;
			}
			
		};
		botonSeleccionarImagen.setLayout(new BorderLayout());
		botonSeleccionarImagen.setBorder(logo? null : Main.DEFAULT_LINE_BORDER);
		botonSeleccionarImagen.setFont(Main.FUENTE.deriveFont(14f));
		botonSeleccionarImagen.setBackground(new Color(0xCBCBCB));
		botonSeleccionarImagen.setForeground(new Color(0x828282));
		botonSeleccionarImagen.setFocusable(false);

		if (!imagenes.isEmpty()) {

			botonSeleccionarImagen.setText("");
			
			hiloCarruselImagenes = new Thread(carruselImagenes);
			hiloCarruselImagenes.start();
			
		}
		
		botonSeleccionarImagen.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseEntered(MouseEvent e) {
				if (enabled) {
					
					if (botonSeleccionarImagen.getIcon() != null || logo) {
	
						if (hiloCarruselImagenes != null) {
						
							hiloCarruselImagenes.interrupt();
						
						}
						
						if (botonSeleccionarImagen.getIcon() != null) {
						
							botonSeleccionarImagen.setIcon(new ImageIcon(imagenes.get(0).getScaledInstance(width, height, Image.SCALE_SMOOTH)));
						
						}
						
						botonSeleccionarImagen.add(texto);		// Añadimos el texto con efecto
						botonSeleccionarImagen.revalidate();
						botonSeleccionarImagen.repaint();
	
					}
					
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (enabled) {
					
					if (hiloCarruselImagenes != null && hiloCarruselImagenes.isAlive()) {
			             hiloCarruselImagenes.interrupt();
			        }
					
					if (!imagenes.isEmpty()) {
					
						hiloCarruselImagenes = new Thread(carruselImagenes);
						hiloCarruselImagenes.start();
					
					}
					
					botonSeleccionarImagen.remove(texto);		// Quitamos el texto con efecto
					botonSeleccionarImagen.revalidate();
					botonSeleccionarImagen.repaint();
					
				}
			}
		});

		botonSeleccionarImagen.addActionListener((e) -> {

			if (enabled) {
				
				int resultado = selectorImagen.showOpenDialog(VentanaPrincipal.getVentanaPrincipal());
	
				if (resultado == JFileChooser.APPROVE_OPTION) {
	
					try {
	
						if (hiloCarruselImagenes != null && hiloCarruselImagenes.isAlive()) {
						
							hiloCarruselImagenes.interrupt();
						
						}
							
						imagenes.clear();
						
						if (selectorImagen.isMultiSelectionEnabled()) {
						
							for (File file : selectorImagen.getSelectedFiles()) {
								
								if (file.exists()) imagenes.add(ImageIO.read(file));
								
							}
	
						} else {
							
							File file = selectorImagen.getSelectedFile();
							
							if (file.exists()) imagenes.add(ImageIO.read(file));
							
						}
							
						if (!imagenes.isEmpty()) {
							
							botonSeleccionarImagen.setText("");
	
							hiloCarruselImagenes = new Thread(carruselImagenes);
							hiloCarruselImagenes.start();
							
						} else {
							
							botonSeleccionarImagen.setIcon(null);
							
							if (logo) {
								
								Cliente cliente = PanelVolverRegistrarseIniciarSesion.getCliente();
								
								botonSeleccionarImagen.setText(Character.toString(cliente.getNombre().split(" ")[0].toUpperCase().charAt(0)) + Character.toString(cliente.getApellidos().split(" ")[0].toUpperCase().charAt(0)));
								botonSeleccionarImagen.setBackground(cliente.getColor());
								botonSeleccionarImagen.setForeground(Utilidades.getContrastColor(cliente.getColor()));
								botonSeleccionarImagen.setBorder(null);
								botonSeleccionarImagen.setFont(Main.FUENTE.deriveFont(Font.BOLD, 16f));
								
							} else {
								
								botonSeleccionarImagen.setText("Subir imágenes");
								botonSeleccionarImagen.setLayout(new BorderLayout());
								botonSeleccionarImagen.setBorder(Main.DEFAULT_LINE_BORDER);
								botonSeleccionarImagen.setFont(Main.FUENTE.deriveFont(14f));
								botonSeleccionarImagen.setBackground(new Color(0xCBCBCB));
								botonSeleccionarImagen.setForeground(new Color(0x828282));
								botonSeleccionarImagen.setFocusable(false);
								
								botonSeleccionarImagen.remove(texto);		// Quitamos el texto con efecto
								botonSeleccionarImagen.revalidate();
								botonSeleccionarImagen.repaint();
								
							}
							
						}
	
					} catch (IOException ex) {
	
						System.err.println("Error al cargar las imágenes");
	
					}
					
				}
			
			}
		});

		// Añadimos el boton al panel imagen

		add(botonSeleccionarImagen, BorderLayout.CENTER);

	}

	public List<BufferedImage> getImagenes() {
		return imagenes;
	}

	public JButton getBotonSeleccionarImagen() {
		return botonSeleccionarImagen;
	}
	
}
