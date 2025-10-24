package gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import db.Consulta;
import main.Main;

// Ventana emergente (JDialog) que permitirá a un usuario iniciar sesión con su correo electrónico y contraseña 
// al hacerlo se comprobará que todos los campos son correctos (si no se mostrará un error arriba de la ventana) y si todo es correcto se iniciará sesión
// Además, esta ventana tendrá un boton que permitirá al usuario restablecer su contraseña dado un mail introducido

public class VentanaIniciarSesion extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private static VentanaIniciarSesion ventanaIniciarSesion;		// Sacamos la propia ventana como variable estática para centrar las ventanas emergentes a esta ventana
	
	public VentanaIniciarSesion() {
		
		ventanaIniciarSesion = this;								// Le damos el valor a la variable nada más crear la instancia 
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setSize(540, 450);
		setLocationRelativeTo(VentanaPrincipal.getVentanaPrincipal());
		setResizable(false);
		setTitle("Iniciar Sesión");
		
		// Panel Texto Error
		
		JLabel error = new JLabel("", SwingConstants.CENTER);
		error.setFont(Main.fuente.deriveFont(Font.ITALIC).deriveFont(14.f));
		error.setForeground(new Color(0xDC143C));
		error.setBorder(new EmptyBorder(20, 20, 0, 20));
		
		// FIN Panel Texto Error
		////
		// Panel campos
		
		JPanel panelCampos = new JPanel(new GridLayout(2, 1, 0, 10));
		panelCampos.setBorder(new EmptyBorder(0, 20, 20, 20));
		
		// Panel Correo electrónico
		
		JPanel correoElectronico = new JPanel(new GridLayout(2, 1, 10, 0));
		
		// Creación del contenido del panel correo
		
		JLabel correoElectronicoL = new JLabel("Correo electrónico:");
		correoElectronicoL.setFont(Main.fuente);
		
		MiTextField correoElectronicoTF = new MiTextField();
		
		// FIN Creación del contenido del panel correo
		// Añadimos el contenido al panel correo
		
		correoElectronico.add(correoElectronicoL);
		correoElectronico.add(correoElectronicoTF);
		
		// FIN Panel Correo electrónico
		////
		// Panel Contraseña
		
		JPanel contrasena = new JPanel(new GridLayout(2, 1, 10, 0));
		
		// Creación del contenido del panel contraseña
		
		JLabel contrasenaL = new JLabel("Contraseña:");
		contrasenaL.setFont(Main.fuente);
		
		MiPasswordField contrasenaPF = new MiPasswordField();
		
		// FIN Creación del contenido del panel contraseña
		// Añadimos el contenido al panel contraseña
		
		contrasena.add(contrasenaL);
		contrasena.add(contrasenaPF);
		
		// FIN Panel Contraseña
		// Añadimos los paneles al panel correspondiente
		
		panelCampos.add(correoElectronico);
		panelCampos.add(contrasena);
		
		// Panel campos
		////
		// Panel botones
		
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panelBotones.setPreferredSize(new Dimension(540, 150));
		panelBotones.setBorder(new EmptyBorder(10, 0, 20, 0));
		
		// Creación del boton que hace lo mismo que si cerraramos la ventana emergente
		
		
		JButton cancelar = new JButton("Cancelar");
		cancelar.setFocusable(false);
		cancelar.setBackground(Color.WHITE);
		cancelar.setFont(Main.fuente);
		cancelar.setPreferredSize(new Dimension(165, 50));
		cancelar.addActionListener(e -> dispose());

		// FIN Creación del boton que hace lo mismo que si cerraramos la ventana emergente
		////
		// Creación del boton que comprueba si los datos introducidos son correctos e inicia sesión en caso de ser válidos 
		
		JButton iniciarSesion = new JButton("Iniciar sesión");
		iniciarSesion.setFocusable(false);
		iniciarSesion.setBackground(Color.WHITE);
		iniciarSesion.setFont(Main.fuente);
		iniciarSesion.setPreferredSize(new Dimension(300, 50));
		iniciarSesion.addActionListener(e -> {
			if (Consulta.iniciarSesion(correoElectronicoTF.getText(), contrasenaPF.getPassword())) {
				
				dispose();
				
			} else {
				
				error.setText("Correo electrónico o Contraseña erróneos");
				
			}
		});
		
		// FIN Creación del boton que comprueba si los datos introducidos son correctos e inicia sesión en caso de ser válidos 
		////
		// Creación del boton que comprueba si el correo está registrado y envía un mail real (a través de la nueva ventana emergente) en caso de serlo 
		
		JButton contrasenaOlvidada = new JButton("He olvidado mi contraseña");
		contrasenaOlvidada.setFocusable(false);
		contrasenaOlvidada.setBackground(Color.WHITE);
		contrasenaOlvidada.setFont(Main.fuente);
		contrasenaOlvidada.setPreferredSize(new Dimension(485, 50));
		contrasenaOlvidada.addActionListener(e -> {
			if (Consulta.isCorreoInDB(correoElectronicoTF.getText())) {
				
				VentanaContrasenaOlvidada ventanaContrasenaOlvidada = new VentanaContrasenaOlvidada(correoElectronicoTF.getText());		// Para probar poner db.Consulta.isCorreoInDB a true de momento
				
				if (ventanaContrasenaOlvidada.getConfirmado()) {
					
					dispose();		// Se cierra la ventana de registrarse solo si el usuario ha decidido registrarse (Si ha decidido salir no se cierra esta ventana)
					
				}
				
			} else {
				
				error.setText("Correo electrónico no registrado");
				
			}
		});
		
		// FIN Creación del boton que comprueba si el correo está registradoy envía un mail real (a través de la nueva ventana emergente) en caso de serlo 
		// Añadimos los botones al panel botones
		
		panelBotones.add(cancelar);
		panelBotones.add(iniciarSesion);
		panelBotones.add(contrasenaOlvidada);
		
		// FIN Panel botones
		// Añadimos los componentes en sus lugares correspondientes
		
		add(error, BorderLayout.NORTH);
		add(panelCampos, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);
		
		// Hacemos visible la ventana emergente
		
		setVisible(true);
		
	}
	
	// Hacemos un getter estático para obtener la instancia de la ventana emergente (para poder centrar la siguiente ventana emergente sobre esta)
	
	public static VentanaIniciarSesion getVentanaIniciarSesion() {
		return ventanaIniciarSesion;
	}
	
}
