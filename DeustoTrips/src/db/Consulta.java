package db;

// Clase que contiene todos los métodos que consultan a la BD

public class Consulta {

	public static boolean isCorreoInDB(String correoElectronico) {
		return correoElectronico.trim().matches("[\\d\\w\\.]+@[\\d\\w]+\\.[\\d\\w]+") && false; // TODO Y ver si está el correo en BD
	}
	
	public static boolean registrarUsuario(String nombre, String apellidos, String correoElectronico, String contrasena) {
		//TODO Registrar nuevo usuario en BD con los datos recibidos (no hace falta comprobar que el correo no esté en BD porque se hace previamente en VentanaRegistrarse.isCorreoValido(...))
		return true;
	}
	
	public static boolean iniciarSesion(String correoElectronico, String contrasena) {
		// TODO iniciar sesión, quitar botones y añadir opciones de usuario registrado si las credenciales son correctas (en BD) si no devuelve false -- Hacer llamando a funciones del packet db
		return true;
	}
	
}
