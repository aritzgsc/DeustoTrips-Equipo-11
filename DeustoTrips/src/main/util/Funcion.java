package main.util;

import java.util.*;

import jakarta.mail.*;				// Librerias para enviar mails
import jakarta.mail.internet.*;

// Clase para crear funciones útiles no relacionadas con la GUI, el dominio o la BD

public class Funcion {

	// Función que envía un correo electrónico (utiliza la librería externa jakarta.mail que nos permite enviar un correo dado un correo remitente y su contraseña, y el correo del destinatario (además del asunto y el cuerpo) a través del protocolo SMTP cifrado)
	
	public static void enviarCorreo(String correoDestinatario, String asunto, String cuerpoHTML) {
		
		// Lanzamos la operación en un hilo aparte para que no se quede nada congelada la pantalla
		
		Thread hiloEnvioCorreo = new Thread(() -> {
		
			String remitente = "deustotrips@gmail.com";			// Credenciales de aplicacion	dirección de correo
			String password = "yziforbrpjnwnyrp";				// Credenciales de aplicacion	contraseña de aplicacion
			
			String destinatario = correoDestinatario;			// Correo de destinatario
			
			Properties props = new Properties();				// Configuración del servidor SMTP para enviar un correo desde gmail
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
			
			Session sesion = Session.getInstance(props, new Authenticator() {
			
				protected PasswordAuthentication getPasswordAuthentication() {
					
					return new PasswordAuthentication(remitente, password);
					
				}
				
			});
			
			try {
				
				Message mensaje = new MimeMessage(sesion);
				mensaje.setFrom(new InternetAddress(remitente));
				mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
				mensaje.setSubject(asunto);
				mensaje.setContent(cuerpoHTML, "text/html; charset=utf-8");
				
				Transport.send(mensaje);
				
			} catch (MessagingException e) {
				
				e.printStackTrace();
				
			}
			
		});
		
		hiloEnvioCorreo.start();
		
	}
	
}
