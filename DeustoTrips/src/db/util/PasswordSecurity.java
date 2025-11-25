package db.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Clase que implementa la seguridad de las contraseñas que guardaremos en la BD

public class PasswordSecurity {

	private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();		// Creamos esto a partir de una librería que nos facilita el hashing de las contraseñas para garantizar la seguridad
	
	// Función para crear un hash de una contraseña, internamente no solo lo hashea 1 vez, sino multiples veces y además lo combina con un SALT garantizando una seguridad mucho mayor
	
	public static String hashPassword(String contrasena) {
		return passwordEncoder.encode(contrasena);
	}
	
	// Función que compara una contraseña con el hash que tenemos almacenado en la BD y dice si son iguales
	
	public static boolean checkPassword(String contrasena, String hashContrasenaBD) {
		return passwordEncoder.matches(contrasena, hashContrasenaBD);
	}
	
}
