package gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import db.GestorDB;
import domain.Cliente;
import gui.util.MiButton;
import gui.util.MiTextField;
import main.Main;
import main.util.MailSender;

// Ventana emergente que te envía un correo nada más crear la instancia y si el código es correcto se crea un nuevo usuario en la BD e inicia sesión

public class VentanaConfirmarRegistro extends JDialog {

	private static final long serialVersionUID = 1L;

	private boolean confirmado = false;					// Usado en VentanaRegistrarse (para comprobar si el usuario ha salido cerrando esta ventana o confirmando su registro)
	
	public VentanaConfirmarRegistro(String nombre, String apellidos, String correoElectronico, String contrasena) {
		
		String codigo = enviarCodigo(correoElectronico);		// Llamada a la función que envía un código al mail especificado
		
		// Configuración de la ventana emergente
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setSize(540, 300);
		setLocationRelativeTo(VentanaRegistrarse.getVentanaRegistrarse());
		setResizable(false);
		setTitle("Confirmar registro");
		
		try {
			setIconImage(ImageIO.read(new File("resources/images/logo.jpg")));
		} catch (IOException e) {
			System.err.println("Error al cargar el logo");
			e.printStackTrace();
		}
		
		// FIN Configuración de la ventana emergente
		////
		// Panel Texto Error
		
		JLabel error = new JLabel("", SwingConstants.CENTER);
		error.setFont(Main.FUENTE.deriveFont(Font.ITALIC).deriveFont(14.f));
		error.setForeground(new Color(0xDC143C));
		error.setBorder(new EmptyBorder(20, 20, 0, 20));
		
		// FIN Panel Texto Error
		////
		// Panel introducir código
		
		JPanel panelIntroducir = new JPanel(new GridLayout(2, 1, 0, 10));
		panelIntroducir.setBorder(new EmptyBorder(0, 20, 10, 20));
		
		// Creación del contenido del panel 
		
		JLabel introducirL = new JLabel("<html><p align=\"center\">Introduzca el código que ha sido enviado al correo " + correoElectronico + ":</p></html>");
		introducirL.setFont(Main.FUENTE);
		
		JTextField introducirCodigoTF = new MiTextField();
		
		// FIN Creación del contenido del panel
		// Añadimos los componentes al panel correspondiente
		
		panelIntroducir.add(introducirL);
		panelIntroducir.add(introducirCodigoTF);
		
		// FIN Panel introducir código
		////
		// Panel botones
		
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panelBotones.setBorder(new EmptyBorder(10, 0, 20, 0));
		
		// Creación del boton que hace lo mismo que si cerraramos la ventana emergente
		
		MiButton cancelar = new MiButton("Cancelar");
		cancelar.setPreferredSize(new Dimension(165, 50));
		cancelar.addActionListener(e -> dispose());

		// FIN Creación del boton que hace lo mismo que si cerraramos la ventana emergente
		////
		// Creación del botón que comprueba si el código es correcto, en caso de serlo registra un nuevo usuario e inicia sesión
		
		MiButton confirmar = new MiButton("Confirmar");
		confirmar.setPreferredSize(new Dimension(300, 50));
		confirmar.addActionListener(e -> {
			
			if (codigo.equals(introducirCodigoTF.getText())) {			// Con esto verificamos dos cosas, que el correo existe y que el que crea la cuenta posee el correo verdaderamente
				
				confirmado = true;
				
				boolean usuarioRegistradoCorrectamente = GestorDB.registrarUsuario(new Cliente(correoElectronico, nombre, apellidos, contrasena, Color.decode(Main.COLORES_PERFIL[(int) (Math.random() * Main.COLORES_PERFIL.length)])));
				
				if (usuarioRegistradoCorrectamente) {
					
					GestorDB.iniciarSesion(correoElectronico, contrasena);
					
				} 
				
				dispose();
				
			} else {
				
				error.setText("Código erróneo");
				
			}
			
		});
		
		// Añadimos los botones al panel botones
		
		panelBotones.add(cancelar);
		panelBotones.add(confirmar);
		
		// FIN Panel botones
		// Añadimos los componentes en sus lugares correspondientes
		
		add(error, BorderLayout.NORTH);
		add(panelIntroducir, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);
		
		// Hacemos visible la ventana emergente
		
		setVisible(true);
		
	}
	
	// Función que envía un código al mail especificado
	
	public String enviarCodigo(String correoElectronico) {

		// Generamos un código aleatorio
		
		String codigo = "";
		
		String caracteresPosibles = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		
		for (int i = 0; i < 6; i++) {
			
			codigo += caracteresPosibles.charAt((int) (Math.random() * caracteresPosibles.length()));
			
		}
		
		// Código aleatorio generado
		////
		// Enviamos el código al correoElectronico especificado (Lo ponemos en formato HTML para que quede más bonito) - Cuerpo HTML diseñado por Gemini
		
		String asunto = "DeustoTrips - Código de registro";
		
		String cuerpoHTML = String.format("""
							              <div style="text-align: center; font-family: 'Comic Sans MS', 'Comic Sans', 'Chalkboard SE', sans-serif; color: #333;">
							                 
							                 <h1 style="color: #2980b9;">¡Hola! 👋</h1>
							                 <h2 style="color: #2c3e50;">Bienvenido a DeustoTrips 🚀</h2>
							                 
							                 <p style="font-size: 14pt;">¡Estamos encantados de que te unas a nosotros!</p>
							                 <p style="font-size: 14pt;">Para completar tu registro y empezar a viajar, introduce este código:</p>
							                 
							                 <div style="background-color: #e8f8f5; border: 2px dashed #1abc9c; padding: 15px; width: 50%%; margin: 20px auto; border-radius: 10px;">
							                     <span style="font-size: 24pt; font-weight: bold; letter-spacing: 5px; color: #16a085;">%s</span>
							                 </div>
							                 
							                 <p style="font-size: 12pt;">Este código es válido para verificar tu cuenta.</p>
							                 
							                 <hr style="width: 80%%; border: 1px solid #ccc; margin: 20px auto;">
							                 
							                 <p style="font-size: 10pt; color: #999;">El equipo de DeustoTrips</p>
							              </div>
							              """, codigo);
		
		MailSender.enviarCorreo(correoElectronico, asunto, cuerpoHTML);
		
		// Correo enviado
		////
		return codigo;		// Devolvemos el código
		
	}
	
	// Usado en VentanaRegistrarse (para comprobar si el usuario ha salido cerrando esta ventana o confirmando su registro)
	
	public boolean getConfirmado() {
		return confirmado;
	}
	
}
