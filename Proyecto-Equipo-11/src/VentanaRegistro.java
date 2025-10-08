import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class VentanaRegistro extends JFrame {
	
	
	private static final long serialVersionUID = 1L;
	



	public VentanaRegistro(){
		// Configuración General de la ventana
		this.setTitle("Registrarse");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(450, 300);
		this.setLocationRelativeTo(null);
		// Márgenes de la ventana
		EmptyBorder margenes = new EmptyBorder(0, 20, 0, 20);
		
		// Componentes de la ventana
		// Panel General
		JPanel general = new JPanel();
		general.setLayout(new GridLayout(4,1,7,7));
		general.setBorder(margenes);
		this.add(general);
		// Panel 1
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(2,2,9,0));
		general.add(panel1);
		// Áreas de texto / Labels
		JLabel nombre = new JLabel("Nombre:");
		nombre.setFont(new Font("Comic Sans MS",Font.PLAIN , 14));
		JLabel apellidos = new JLabel("Apellidos:");
		apellidos.setFont(new Font("Comic Sans MS",Font.PLAIN , 14));
		JTextField nom = new JTextField(30);
		nom.setFont(new Font("Comic Sans MS",Font.PLAIN , 14));
		JTextField apell = new JTextField(50);
		apell.setFont(new Font("Comic Sans MS",Font.PLAIN , 14));
		panel1.add(nombre);
		panel1.add(apellidos);
		panel1.add(nom);
		panel1.add(apell);
		
		// Panel 2
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		general.add(panel2);
		//Texto y label
		JLabel correo = new JLabel("Correo Electrónico:");
		correo.setFont(new Font("Comic Sans MS",Font.PLAIN , 14));
		correo.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel2.add(correo);
		panel2.add(Box.createVerticalStrut(7));
		JTextField correoE = new JTextField(50);
		correoE.setFont(new Font("Comic Sans MS",Font.PLAIN , 14));
		correoE.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel2.add(correoE);
		
		// Panel 3
		JPanel panel3 = new JPanel();
		panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
		general.add(panel3);
		//Texto y label
		JLabel contras = new JLabel("Contraseña:");
		contras.setFont(new Font("Comic Sans MS",Font.PLAIN , 14));
		contras.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel3.add(contras);
		panel3.add(Box.createVerticalStrut(7));
		JTextField contraseña = new JTextField();
		contraseña.setFont(new Font("Comic Sans MS",Font.PLAIN , 14));
		contraseña.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel3.add(contraseña);
		
		// Panel 4
		JPanel panel4 = new JPanel();
		panel4.setLayout(new FlowLayout());
		panel4.setBorder(new EmptyBorder(15, 0, 0, 0));
		general.add(panel4);
		//Botones
		JButton cancelar = new JButton("Cancelar");
		cancelar.setFont(new Font("Comic Sans MS",Font.PLAIN , 14));
		cancelar.setAlignmentY(BOTTOM_ALIGNMENT);
		panel4.add(cancelar);
		panel4.add(Box.createHorizontalStrut(7));
		JButton confirmar = new JButton("Confirmar registro");
		confirmar.setFont(new Font("Comic Sans MS",Font.PLAIN , 14));
		confirmar.setAlignmentY(CENTER_ALIGNMENT);
		panel4.add(confirmar);
		// Hacer visible la ventana
		this.setVisible(true);
	}
	
	
	
	public static void main(String[] args) {
		new VentanaRegistro();

	}

}
