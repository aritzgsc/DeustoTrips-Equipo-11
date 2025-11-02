package gui.main;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import db.Consulta;
import main.util.Funcion;
import gui.util.MiTextField;
import main.Main;

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
		
		JLabel introducirL = new JLabel("Introduzca el código que ha sido enviado al correo " + correoElectronico + ":");
		introducirL.setFont(Main.FUENTE);
		
		MiTextField introducirCodigoTF = new MiTextField();
		
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
		
		JButton cancelar = new JButton("Cancelar");
		cancelar.setFocusable(false);
		cancelar.setBackground(Color.WHITE);
		cancelar.setFont(Main.FUENTE);
		cancelar.setPreferredSize(new Dimension(165, 50));
		cancelar.addActionListener(e -> dispose());

		// FIN Creación del boton que hace lo mismo que si cerraramos la ventana emergente
		////
		// Creación del botón que comprueba si el código es correcto, en caso de serlo registra un nuevo usuario e inicia sesión
		
		JButton confirmar = new JButton("Confirmar");
		confirmar.setFocusable(false);
		confirmar.setBackground(Color.WHITE);
		confirmar.setFont(Main.FUENTE);
		confirmar.setPreferredSize(new Dimension(300, 50));
		confirmar.addActionListener(e -> {
			
			if (codigo.equals(introducirCodigoTF.getText())) {			// Con esto verificamos dos cosas, que el correo existe y que el que crea la cuenta posee el correo verdaderamente
				
				confirmado = true;
				
				Consulta.registrarUsuario(nombre, apellidos, correoElectronico, contrasena);
				Consulta.iniciarSesion(correoElectronico, contrasena);
				
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
		// Enviamos el código al correoElectronico especificado (Lo ponemos en formato HTML para que quede más bonito)
		
		String asunto = "DeustoTrips - Código para registro";
		
		String cuerpoHTML =  """
				  			 <div style="text-align: center; font-family: 'Comic Sans MS', cursive; font-size: 15pt;">
				    		 	 Introduzca el siguiente código en la aplicación:
							 </div>
							 <div style="text-align: center; font-family: 'Comic Sans MS', cursive; font-size: 20pt; font-weight: bold; margin-top: 10px;">
				  			     """ + codigo + """
				  			 </div>
				  			 """;
		
		Funcion.enviarCorreo(correoElectronico, asunto, cuerpoHTML);
		
		// Correo enviado
		////
		return codigo;		// Devolvemos el código
		
	}
	
	// Usado en VentanaRegistrarse (para comprobar si el usuario ha salido cerrando esta ventana o confirmando su registro)
	
	public boolean getConfirmado() {
		return confirmado;
	}
	
}
