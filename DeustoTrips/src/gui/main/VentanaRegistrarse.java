package gui.main;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import db.Consulta;
import gui.util.MiPasswordField;
import gui.util.MiTextField;
import main.Main;

// Ventana emergente (JDialog) que permitirá a un usuario registrarse con un nombre, apellido(s), correo electrónico y contraseña 
// al hacerlo se comprobará que todos los campos son correctos (si no se mostrará lo erroneo arriba de la ventana) y si todo es correcto se enviará un correo de verificación al usuario al correo introducido

public class VentanaRegistrarse extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private static VentanaRegistrarse ventanaRegistrarse;		// Sacamos la propia ventana como variable estática para centrar las ventanas emergentes a esta ventana
	
	public VentanaRegistrarse() {
		
		ventanaRegistrarse = this;								// Le damos el valor a la variable nada más crear la instancia 
		
		// Configuración de la ventana emergente
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setSize(540, 500);
		setLocationRelativeTo(VentanaPrincipal.getVentanaPrincipal());
		setResizable(false);
		setTitle("Registrarse");
		
		// FIN Configuración de la ventana emergente
		////
		// Panel Texto Error
		
		JLabel error = new JLabel("", SwingConstants.CENTER);
		error.setFont(Main.FUENTE.deriveFont(Font.ITALIC).deriveFont(14.f));
		error.setForeground(new Color(0xDC143C));
		error.setBorder(new EmptyBorder(20, 20, 0, 20));
		
		// FIN Panel Texto Error
		////
		// Panel campos (Contendrá todos los campos)
		
		JPanel panelCampos = new JPanel(new GridLayout(3, 1, 0, 10));
		panelCampos.setBorder(new EmptyBorder(0, 20, 20, 20));
		
		// Panel Nombre y Apellidos (Los juntamos en un mismo JPanel para que estén ambos campos uno al lado del otro)
		
		JPanel nombreYApellidos = new JPanel(new GridLayout(2, 2, 10, 0));
		
		JLabel nombreL = new JLabel("Nombre:");
		nombreL.setFont(Main.FUENTE);
		nombreYApellidos.add(nombreL);
		
		JLabel apellidosL = new JLabel("Apellidos:");
		apellidosL.setFont(Main.FUENTE);
		nombreYApellidos.add(apellidosL);
		
		MiTextField nombreTF = new MiTextField();
		nombreYApellidos.add(nombreTF);
		
		MiTextField apellidosTF = new MiTextField();
		nombreYApellidos.add(apellidosTF);
		
		// FIN Panel Nombre y Apellidos
		////
		// Panel Correo electrónico
		
		JPanel correoElectronico = new JPanel(new GridLayout(2, 1, 10, 0));
		
		JLabel correoElectronicoL = new JLabel("Correo electrónico:");
		correoElectronicoL.setFont(Main.FUENTE);
		correoElectronico.add(correoElectronicoL);
		
		MiTextField correoElectronicoTF = new MiTextField();
		correoElectronico.add(correoElectronicoTF);
		
		// FIN Panel Correo electrónico
		////
		// Panel Contraseña
		
		JPanel contrasena = new JPanel(new GridLayout(2, 1, 10, 0));
		
		JLabel contrasenaL = new JLabel("Contraseña: (Mínimo 8 carácteres)");
		contrasenaL.setFont(Main.FUENTE);
		contrasena.add(contrasenaL);
		
		MiPasswordField contrasenaPF = new MiPasswordField();
		contrasena.add(contrasenaPF);
		
		// FIN Panel Contraseña
		////
		// Añadimos todos los campos al panel correspondiente
		
		panelCampos.add(nombreYApellidos);
		panelCampos.add(correoElectronico);
		panelCampos.add(contrasena);
		
		// FIN Panel campos
		////
		// Panel botones
		
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		panelBotones.setBorder(new EmptyBorder(20, 0, 20, 0));
		
		// Creación del boton que hace lo mismo que si cerraramos la ventana emergente
		
		JButton cancelar = new JButton("Cancelar");
		cancelar.setFocusable(false);
		cancelar.setBackground(Color.WHITE);
		cancelar.setFont(Main.FUENTE);
		cancelar.setPreferredSize(new Dimension(165, 50));
		cancelar.addActionListener(e -> dispose());
		

		// FIN Creación del boton que hace lo mismo que si cerraramos la ventana emergente
		////
		// Creación del boton que comprueba si los datos introducidos son correctos y envía un mail real (a través de la nueva ventana emergente) en caso de serlo 
		
		JButton confirmarReg = new JButton("Confirmar registro");
		confirmarReg.setFocusable(false);
		confirmarReg.setBackground(Color.WHITE);
		confirmarReg.setFont(Main.FUENTE);
		confirmarReg.setPreferredSize(new Dimension(300, 50));
		confirmarReg.addActionListener(e -> {
			if (!nombreTF.getText().isBlank() && !apellidosTF.getText().isBlank() && isCorreoValido(correoElectronicoTF.getText()) && contrasenaPF.isContrasenaValida()) {
				
				VentanaConfirmarRegistro ventanaConfirmarRegistro = new VentanaConfirmarRegistro(nombreTF.getText(), apellidosTF.getText(), correoElectronicoTF.getText(), contrasenaPF.getPassword());

				if (ventanaConfirmarRegistro.getConfirmado()) {
					
					dispose();		// Se cierra la ventana de registrarse solo si el usuario ha decidido registrarse (Si ha decidido salir no se cierra esta ventana)
					
				}
				
			} else {
				
				error.setText(getError(nombreTF.getText(), apellidosTF.getText(), correoElectronicoTF.getText(), contrasenaPF));		// Llamamos a la función que hemos creado que devuelve el error
				
			}
		});
		
		// FIN Creación del boton que comprueba si los datos introducidos son correctos y envía un mail real (a través de la nueva ventana emergente) en caso de serlo 
		////
		// Añadimos los botones al panel correspondiente
		
		panelBotones.add(cancelar);
		panelBotones.add(confirmarReg);
		
		// FIN Panel botones
		// Añadimos los paneles a sus lugares correspondientes
		
		add(error, BorderLayout.NORTH);
		add(panelCampos, BorderLayout.CENTER);
		add(panelBotones, BorderLayout.SOUTH);
		
		// Hacemos la ventana emergente visible
		
		setVisible(true);
		
	}
	
	private static boolean isCorreoValido(String correoElectronico) {
		return !Consulta.isCorreoInDB(correoElectronico); // TODO Comprobación del formato del correo y que el gmail no esté registrado ya en BD (db.Consulta)
	}
	
	// Función que devuelve los errores cometidos en formato String (Primero separa todo por , y luego remplaza la ultima , por un string vacio "" y la penúltima (si la hay) por " y"
	
	private String getError(String nombre, String apellidos, String correoElectronico, MiPasswordField contrasenaPF) {
		
		int n = 0;													// Contador de errores (para saber si es 1 o varios)
		
		if (nombre.isBlank()) n++;
		if (apellidos.isBlank()) n++;
		if (!isCorreoValido(correoElectronico)) n++;
		if (!contrasenaPF.isContrasenaValida()) n++;
		
		return ((nombre.isBlank()?"Nombre, ":"") + 
				(apellidos.isBlank()?"Apellidos, ":"") + 
				(!isCorreoValido(correoElectronico)?"Correo, ":"") + 
				(!contrasenaPF.isContrasenaValida()?"Contraseña, ":"") + 
				(n > 1?"son erróneos":"es erróneo")
				).replaceAll(",(?=([^,]*$))", "").replaceAll(",(?=([^,]*$))", " y");
	}
	
	// Hacemos un getter estático para obtener la instancia de la ventana emergente (para poder centrar la siguiente ventana emergente sobre esta)
	
	public static VentanaRegistrarse getVentanaRegistrarse() {
		return ventanaRegistrarse;
	}
	
}
