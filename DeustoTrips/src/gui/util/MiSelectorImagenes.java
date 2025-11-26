package gui.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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

import gui.main.VentanaPrincipal;
import main.Main;

public class MiSelectorImagenes extends JPanel {

	private static final long serialVersionUID = 1L;

	private List<BufferedImage> imagenes;
	private JButton botonSeleccionarImagen;
	
	private Runnable carruselImagenes;
	
	private Thread hiloCarruselImagenes;
	
	public MiSelectorImagenes(List<BufferedImage> imagenes, boolean enabled) {

		this.imagenes = imagenes;
		
		// Panel de la imágen

		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 5));
		setPreferredSize(new Dimension(300, 300));
		
		// Creamos el chooser
        
        JFileChooser selectorImagen = new JFileChooser();

        // Configuramos el chooser
        
        selectorImagen.setDialogTitle("Seleccionar Imagenes");
        selectorImagen.setAcceptAllFileFilterUsed(false);
        selectorImagen.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));
        selectorImagen.setFileSelectionMode(JFileChooser.FILES_ONLY);
        selectorImagen.setMultiSelectionEnabled(true);
        selectorImagen.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());

		// Configuramos la tarea del carrusel en un Runnable para luego poder reutilizar el código (no tener que copiar y pegar)
		
		carruselImagenes = () -> {
			
			int contador = 0;

			while (!Thread.currentThread().isInterrupted() && !imagenes.isEmpty()) {

				try {

					int contadorActual = contador;
					
					SwingUtilities.invokeLater(() -> botonSeleccionarImagen.setIcon(new ImageIcon(imagenes.get(contadorActual).getScaledInstance(300, 300, Image.SCALE_SMOOTH))));

					Thread.sleep(3000);

					contador = (contador + 1) % imagenes.size();

				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}

			}
			
		};
		
		// Label para Efecto Hover

		JLabel texto = new JLabel("Cambiar imágenes") {
			private static final long serialVersionUID = 1L;

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

		botonSeleccionarImagen = new JButton("Subir imágenes");
		botonSeleccionarImagen.setLayout(new BorderLayout());
		botonSeleccionarImagen.setBorder(Main.DEFAULT_LINE_BORDER);
		botonSeleccionarImagen.setFont(Main.FUENTE.deriveFont(14f));
		botonSeleccionarImagen.setBackground(new Color(0xCBCBCB));
		botonSeleccionarImagen.setForeground(new Color(0x828282));
		botonSeleccionarImagen.setFocusable(false);

		if (!imagenes.isEmpty()) {

			hiloCarruselImagenes = new Thread(carruselImagenes);
			hiloCarruselImagenes.start();
			
		}

		botonSeleccionarImagen.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseEntered(MouseEvent e) {
				if (enabled) {
					
					if (botonSeleccionarImagen.getIcon() != null) {
	
						if (hiloCarruselImagenes != null) {
						
							hiloCarruselImagenes.interrupt();
						
						}
							
						botonSeleccionarImagen.setIcon(new ImageIcon(imagenes.get(0).getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
						
						botonSeleccionarImagen.add(texto);
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
					
					botonSeleccionarImagen.remove(texto);
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
						
						for (File file : selectorImagen.getSelectedFiles()) {
							
							imagenes.add(ImageIO.read(file));
							
						}
	
						if (!imagenes.isEmpty()) {
							
							botonSeleccionarImagen.setText("");
	
							hiloCarruselImagenes = new Thread(carruselImagenes);
							hiloCarruselImagenes.start();
							
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
	
}
