import java.awt.Component;
//import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class VentanaInicioSesion extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public VentanaInicioSesion() {
		// Configuración General de la ventana
		this.setTitle("Iniciar Sesión");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(400, 320);
		this.setLocationRelativeTo(null);
		// Márgenes de la ventana 
		EmptyBorder margenes = new EmptyBorder(10, 20, 0, 20);
		// Componentes de la ventana
		
		// Panel general
		JPanel general = new JPanel();
		general.setLayout(new GridLayout(3, 1));
		general.setBorder(margenes);
		this.add(general);
		
		// Panel 1
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		general.add(panel1);
		JLabel correo = new JLabel("Correo Electrónico:");
		correo.setFont(new Font("Comic Sans MS",Font.PLAIN , 16));
		correo.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel1.add(correo);
		panel1.add(Box.createVerticalStrut(7));
		JTextField correoE = new JTextField();
		correoE.setFont(new Font("Comic Sans MS",Font.PLAIN , 16));
		correoE.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel1.add(correoE);
		panel1.add(Box.createVerticalStrut(15));
		
		// Panel 2
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		panel2.setBorder(new EmptyBorder(0, 0, 0, 0));
		general.add(panel2);
		JLabel contraseña = new JLabel("Contraseña:");
		contraseña.setFont(new Font("Comic Sans MS",Font.PLAIN , 16));
		contraseña.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel2.add(contraseña);
		panel2.add(Box.createVerticalStrut(7));
		JTextField contraseñaE = new JTextField();
		contraseñaE.setAlignmentX(Component.LEFT_ALIGNMENT);
		contraseñaE.setFont(new Font("Comic Sans MS",Font.PLAIN , 16));
		panel2.add(contraseñaE);
		JLabel nota = new JLabel("Aquí se pone el mensaje avisando de lo de los datos erróneos");
		nota.setFont(new Font("Comic Sans MS",Font.PLAIN , 12));
		panel2.add(nota);
		
		// Panel 3 (Botones)
		JPanel panel3 = new JPanel();
		panel3.setLayout(new FlowLayout());
		panel3.setBorder(new EmptyBorder(15, 0, 0, 0));
		general.add(panel3);
		JButton cancelar = new JButton("Cancelar");
		cancelar.setFont(new Font("Comic Sans MS",Font.PLAIN , 14));
		panel3.add(cancelar);
		panel3.add(Box.createHorizontalStrut(5));
		JButton iniciarSesion = new JButton("Iniciar Sesión");
		iniciarSesion.setFont(new Font("Comic Sans MS",Font.PLAIN , 14));
		panel3.add(iniciarSesion);
		panel3.add(Box.createVerticalStrut(8));
		JButton olvidado = new JButton("He olvidado mi contraseña");
		olvidado.setFont(new Font("Comic Sans MS",Font.PLAIN , 14));
		panel3.add(olvidado);
		
		// Hacer la ventana visible
		this.setVisible(true);
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		new VentanaInicioSesion();
	}

}
