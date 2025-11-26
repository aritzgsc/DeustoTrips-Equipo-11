package gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import db.GestorDB;
import domain.Cliente;
import gui.util.MiButton;
import gui.util.MiPasswordField;
import gui.util.MiTextField;
import main.Main;

public class VentanaModificarCuenta extends JFrame {

	private static final long serialVersionUID = 1L;

	public VentanaModificarCuenta() {
		
		Cliente cliente = PanelVolverRegistrarseIniciarSesion.getCliente();
		
		// Configuración de la ventana
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(540, 500);
		setLocationRelativeTo(VentanaPrincipal.getVentanaPrincipal());
		setResizable(false);
		setTitle("Modificar cuenta");
		
		// FIN Configuración de la ventana
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
		nombreTF.setText(cliente.getNombre());
		nombreYApellidos.add(nombreTF);
		
		MiTextField apellidosTF = new MiTextField();
		apellidosTF.setText(cliente.getApellidos());
		nombreYApellidos.add(apellidosTF);
		
		// FIN Panel Nombre y Apellidos
		////
		// Panel Correo electrónico
		
		JPanel correoElectronicoP = new JPanel(new GridLayout(2, 1, 10, 0));
		
		JLabel correoElectronicoL = new JLabel("Correo electrónico:");
		correoElectronicoL.setFont(Main.FUENTE);
		correoElectronicoP.add(correoElectronicoL);
		
		MiTextField correoElectronicoTF = new MiTextField();
		correoElectronicoTF.setText(cliente.getCorreo());
		correoElectronicoTF.setEditable(false);
		correoElectronicoTF.setFocusable(false);
		correoElectronicoTF.setBackground(new Color(0xEEEEEE));
		correoElectronicoP.add(correoElectronicoTF);
		
		// FIN Panel Correo electrónico
		////
		// Panel Contraseña
		
		JPanel contrasenaP = new JPanel(new GridLayout(2, 1, 10, 0));
		
		JLabel contrasenaL = new JLabel("Contraseña: (Mínimo 8 carácteres)");
		contrasenaL.setFont(Main.FUENTE);
		contrasenaP.add(contrasenaL);
		
		MiPasswordField contrasenaPF = new MiPasswordField();
		contrasenaPF.setText(cliente.getContrasena());
		contrasenaP.add(contrasenaPF);
		
		// FIN Panel Contraseña
		////
		// Añadimos todos los campos al panel correspondiente
		
		panelCampos.add(nombreYApellidos);
		panelCampos.add(correoElectronicoP);
		panelCampos.add(contrasenaP);
		
		// FIN Panel campos
		////
		// Panel botones
		
		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		panelBotones.setBorder(new EmptyBorder(20, 0, 20, 0));
		
		// Creación del boton que hace lo mismo que si cerraramos la ventana emergente
		
		MiButton cancelar = new MiButton("Cancelar");
		cancelar.setPreferredSize(new Dimension(165, 50));
		cancelar.addActionListener(e -> dispose());
		

		// FIN Creación del boton que hace lo mismo que si cerraramos la ventana emergente
		////
		// Creación del boton que comprueba si los datos introducidos son correctos y envía un mail real (a través de la nueva ventana emergente) en caso de serlo 
		
		MiButton confirmarReg = new MiButton("Guardar cambios");
		confirmarReg.setPreferredSize(new Dimension(300, 50));
		confirmarReg.addActionListener(e -> {
			if (getError(nombreTF.getText(), apellidosTF.getText(), contrasenaPF).equals("<html><p align=\"center\"></p></html>")) {

				boolean datosCambiadosCorrectamente = GestorDB.cambiarDatosUsuario(new Cliente(cliente.getCorreo(), nombreTF.getText(), apellidosTF.getText(), contrasenaPF.getPassword()));
				
				if (datosCambiadosCorrectamente) {
				
					dispose();
				
				}
					
			} else {
				
				error.setText(getError(nombreTF.getText(), apellidosTF.getText(), contrasenaPF));		// Llamamos a la función que hemos creado que devuelve el error
				
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
	
	// Función que devuelve los errores cometidos en formato String (Primero separa todo por , y luego remplaza la ultima , por un string vacio "" y la penúltima (si la hay) por " y"
	
	private String getError(String nombre, String apellidos, MiPasswordField contrasenaPF) {
		
		if (nombre.isBlank());
		if (apellidos.isBlank());
		if (!contrasenaPF.isContrasenaValida());
		
		return "<html><p align=\"center\">" + ((nombre.isBlank()?"Nombre inválido, ":"") + 
				(apellidos.isBlank()?"Apellidos inválidos, ":"") + 
				(!contrasenaPF.isContrasenaValida()?"Contraseña inválida, ":"")  
				).replaceAll(",(?=([^,]*$))", "").replaceAll(",(?=([^,]*$))", " y") + "</p></html>";
	}
	
}
