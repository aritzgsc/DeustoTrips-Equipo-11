package gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

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
		error.setFont(Main.FUENTE.deriveFont(Font.ITALIC).deriveFont(14.f));
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
		correoElectronicoL.setFont(Main.FUENTE);
		
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
		contrasenaL.setFont(Main.FUENTE);
		
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
		
		
		MiButton cancelar = new MiButton("Cancelar");
		cancelar.setPreferredSize(new Dimension(165, 50));
		cancelar.addActionListener(e -> dispose());

		// FIN Creación del boton que hace lo mismo que si cerraramos la ventana emergente
		////
		// Creación del boton que comprueba si los datos introducidos son correctos e inicia sesión en caso de ser válidos 
		
		MiButton iniciarSesion = new MiButton("Iniciar sesión");
		iniciarSesion.setPreferredSize(new Dimension(300, 50));
		iniciarSesion.addActionListener(e -> {
			if (GestorDB.iniciarSesion(correoElectronicoTF.getText(), contrasenaPF.getPassword())) {
				
				dispose();
				
			} else {
				
				if (GestorDB.isCorreoInDB(correoElectronicoTF.getText())) {
					
					error.setText("<html><p align=\"center\">Contraseña incorrecta</p></html>");
					
				} else {
					
					error.setText("<html><p align=\"center\">Correo electrónico no registrado</p></html>");
					
				}
				
				
				
			}
		});
		
		// FIN Creación del boton que comprueba si los datos introducidos son correctos e inicia sesión en caso de ser válidos 
		////
		// Creación del boton que comprueba si el correo está registrado y envía un mail real (a través de la nueva ventana emergente) en caso de serlo 
		
		MiButton contrasenaOlvidada = new MiButton("He olvidado mi contraseña");
		contrasenaOlvidada.setPreferredSize(new Dimension(485, 50));
		contrasenaOlvidada.addActionListener(e -> {
			if (isCorreoValido(correoElectronicoTF.getText())) {
				
				VentanaContrasenaOlvidada ventanaContrasenaOlvidada = new VentanaContrasenaOlvidada(correoElectronicoTF.getText());
				
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
	
	private static boolean isCorreoValido(String correoElectronico) {
		return GestorDB.isCorreoInDB(correoElectronico.trim()); // Comprobación del formato del correo y que el gmail esté registrado ya en BD (db.Consulta)
	}
	
	// Hacemos un getter estático para obtener la instancia de la ventana emergente (para poder centrar la siguiente ventana emergente sobre esta)
	
	public static VentanaIniciarSesion getVentanaIniciarSesion() {
		return ventanaIniciarSesion;
	}
	
}
