package gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import db.GestorDB;
import gui.util.MiButton;
import gui.util.MiPasswordField;
import gui.util.MiTextField;
import main.Main;
import main.util.MailSender;

// Ventana emergente que te envía un correo nada más crear la instancia y si el código es correcto se crea un nuevo usuario en la BD e inicia sesión

public class VentanaContrasenaOlvidada extends JDialog {

	private static final long serialVersionUID = 1L;

	private boolean confirmado = false;					// Usado en VentanaIniciarSesion (para comprobar si el usuario ha salido cerrando esta ventana o confirmando su registro)
	
	public VentanaContrasenaOlvidada(String correoElectronico) {
		
		String codigo = enviarCodigo(correoElectronico);		// Llamada a la función que envía un código al mail especificado
		
		// Configuración de la ventana emergente
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setSize(540, 300);
		setLocationRelativeTo(VentanaIniciarSesion.getVentanaIniciarSesion());
		setResizable(false);
		setTitle("He olvidado mi contraseña");
		
		// FINC onfiguración de la ventana emergente
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
		
		MiTextField introducirCodigoTF = new MiTextField();
		
		// FIN Creación del contenido del panel 
		// Añadimos los componentes al panel correspondiente

		panelIntroducir.add(introducirL);
		panelIntroducir.add(introducirCodigoTF);
		
		// FIN Panel introducir
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
			
			if (codigo.equals(introducirCodigoTF.getText())) {
				
				// Resetear panel error
				
				error.setText("");
				
				// FIN Resetear panel error
				////
				// Cambiar panel introducir
				
				introducirL.setText("<html><p align=\"center\">Introduzca una nueva contraseña para el usuario con correo " + correoElectronico + ":</p></html>");
				panelIntroducir.remove(introducirCodigoTF);
				
				MiPasswordField introducirContrasenaPF = new MiPasswordField();
				introducirContrasenaPF.setFont(Main.FUENTE);
				introducirContrasenaPF.addKeyListener(Main.ANTI_CARACTERES_RAROS);
				panelIntroducir.add(introducirContrasenaPF);
				
				// FIN Cambiar panel introducir
				////
				// Cambiar panel botones
				
				panelBotones.remove(confirmar);
				
				MiButton establecer = new MiButton("Establecer nueva contraseña");
				establecer.setPreferredSize(new Dimension(300, 50));
				establecer.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if (introducirContrasenaPF.isContrasenaValida()) {
							
							confirmado = true;

							boolean contrasenaCambiadaCorrectamente = GestorDB.cambiarContrasenaUsuario(correoElectronico, introducirContrasenaPF.getPassword());
							
							if (contrasenaCambiadaCorrectamente) {
								
								boolean sesionIniciadaCorrectamente = GestorDB.iniciarSesion(correoElectronico, introducirContrasenaPF.getPassword());
								
								if (sesionIniciadaCorrectamente) {
								
									dispose();
									
								}
								
							}
								
						} else {
							
							error.setText("Contraseña inválida");
							
						}
					}
				});
				
				panelBotones.add(establecer);
				
				// FIN Cambiar panel botones
				
			} else {
				
				error.setText("Código erróneo");
				
			}
			
		});
		
		// Creación del botón que comprueba si el código es correcto, en caso de serlo registra un nuevo usuario e inicia sesión
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
	
	public String enviarCodigo(String correoElectronico) {

		// Generamos un código aleatorio
		
		String codigo = "";
		
		String caracteresPosibles = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		
		for (int i = 0; i < 6; i++) {
			
			codigo += caracteresPosibles.charAt((int) (Math.random() * caracteresPosibles.length()));
			
		}
		
		// Código aleatorio generado
		////
		// Enviamos el código al correoElectronico especificado
		
		String asunto = "DeustoTrips - Código de Verificación";
		
		String cuerpoHTML = String.format("""
							              <div style="text-align: center; font-family: 'Comic Sans MS', cursive; color: #333;">
							                 
							                 <h1 style="color: #2c3e50;">🔐 Recuperación de Cuenta</h1>
							                 
							                 <p style="font-size: 14pt;">Hemos recibido una solicitud para cambiar tu contraseña.</p>
							                 <p style="font-size: 14pt;">Usa el siguiente código para continuar:</p>
							                 
							                 <div style="background-color: #f0f4f8; border: 2px dashed #2c3e50; padding: 15px; width: 50%%; margin: 20px auto; border-radius: 10px;">
							                     <span style="font-size: 24pt; font-weight: bold; letter-spacing: 5px; color: #e74c3c;">%s</span>
							                 </div>
							                 
							                 <p style="font-size: 12pt; color: #777;">Si no has solicitado este cambio, ignora este correo. Tu cuenta sigue segura.</p>
							                 
							                 <hr style="width: 80%%; border: 1px solid #ccc; margin: 20px auto;">
							                 
							                 <p style="font-size: 10pt; color: #999;">DeustoTrips Security Team</p>
							              </div>
							              """, codigo);
		
		MailSender.enviarCorreo(correoElectronico, asunto, cuerpoHTML);
		
		// Correo enviado
		////
		return codigo;		// Devolvemos el código
		
	}
	
	// Usado en VentanaIniciarSesion (para comprobar si el usuario ha salido cerrando esta ventana o confirmando su registro)
	
		public boolean getConfirmado() {
			return confirmado;
		}
	
}
