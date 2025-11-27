package db;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import db.util.PasswordSecurity;
import domain.Aeropuerto;
import domain.Alojamiento;
import domain.Apartamento;
import domain.Ciudad;
import domain.Cliente;
import domain.Destino;
import domain.Hotel;
import domain.Pais;
import domain.Resena;
import gui.main.PanelVolverRegistrarseIniciarSesion;

// Clase que contiene todos los métodos que utilizan la BD

public class GestorDB {

	private static final String SQLITE_FILE = "resources/db/DBDeustoTrips.db";
	private static final String CONNECTION_STRING = "jdbc:sqlite:" + SQLITE_FILE;

	private static final int ID_TD_PAIS = 1;
	private static final int ID_TD_CIUDAD = 2;
	private static final int ID_TD_AEROPUERTO = 3;

	public GestorDB() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.err.format("Error al cargar el driver de la BBDD: %s\n", e.getMessage());
		}
	}

	public static List<Destino> cargarDestinos() {

		Map<Integer, Pais> paisPorID = new HashMap<Integer, Pais>();

		Map<Integer, Ciudad> ciudadPorID = new HashMap<Integer, Ciudad>();

		List<Destino> destinosList = new ArrayList<Destino>();

		String sqlSelectPaises = """
								 SELECT ID_D, NOM_D, LAT_D, LON_D, BANDERA
								 FROM DESTINO D, TIPO_DESTINO TD, IMAGEN_DESTINO ID
								 WHERE D.ID_TD = TD.ID_TD AND
								 TD.NOM_TD = 'País' AND
								 D.ISO_CODE = ID.ISO_CODE;
								 """;

		String sqlSelectCiudades = """
								   SELECT ID_D, NOM_D, LAT_D, LON_D, ID_D_PADRE
								   FROM DESTINO D, TIPO_DESTINO TD
								   WHERE D.ID_TD = TD.ID_TD AND
								   TD.NOM_TD = 'Ciudad';
								   """;

		String sqlSelectAeropuertos = """
									  SELECT ID_D, NOM_D, LAT_D, LON_D, ID_D_PADRE
									  FROM DESTINO D, TIPO_DESTINO TD
									  WHERE D.ID_TD = TD.ID_TD AND
									  TD.NOM_TD = 'Aeropuerto';
									  """;

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
				PreparedStatement pstmtPaises = con.prepareStatement(sqlSelectPaises);
				PreparedStatement pstmtCiudades = con.prepareStatement(sqlSelectCiudades);
				PreparedStatement pstmtAeropuertos = con.prepareStatement(sqlSelectAeropuertos)) {

			// Primero debemos rellenar los datos de todos los países (porque lo demás depende de ellos)

			ResultSet paisesRS = pstmtPaises.executeQuery();

			while (paisesRS.next()) {

				// Recogemos los datos del país

				int idPais = paisesRS.getInt("ID_D");
				String nomPais = paisesRS.getString("NOM_D");
				double latPais = paisesRS.getDouble("LAT_D");
				double lonPais = paisesRS.getDouble("LON_D");

				// Recuperamos la imágen de la BD

				byte[] banderaBytes = paisesRS.getBytes("BANDERA");

				ImageIcon banderaPais = null;

				try {

					ByteArrayInputStream bais = new ByteArrayInputStream(banderaBytes);
					banderaPais = new ImageIcon(ImageIO.read(bais));

				} catch (IOException e) {

					System.err.println("Error al cargar la bandera de " + nomPais);
					// e.printStackTrace();

				}

				// Creamos el país

				Pais pais = new Pais(idPais, nomPais, latPais, lonPais, banderaPais);

				// Rellenamos los mapas correspondientes que utilizaremos más tarde (además del mapa que devolverá la función)

				paisPorID.put(idPais, pais);
				destinosList.add(pais);

			}

			// Luego hacemos parecido con las ciudades

			ResultSet ciudadesRS = pstmtCiudades.executeQuery();

			while (ciudadesRS.next()) {

				// Recogemos los datos de la ciudad

				int idCiudad = ciudadesRS.getInt("ID_D");
				String nomCiudad = ciudadesRS.getString("NOM_D");
				double latCiudad = ciudadesRS.getDouble("LAT_D");
				double lonCiudad = ciudadesRS.getDouble("LON_D");
				int idPaisCiudad = ciudadesRS.getInt("ID_D_PADRE");

				// Conseguimos el país a través del mapa que hemos creado con los paises por iso

				Pais paisCiudad = paisPorID.get(idPaisCiudad);

				Ciudad ciudad = null;
				
				if (paisCiudad != null) {

					// Creamos la ciudad
					
					ciudad = new Ciudad(idCiudad, paisCiudad, nomCiudad, latCiudad, lonCiudad);

					// Rellenamos los mapas correspondientes

					ciudadPorID.put(idCiudad, ciudad);
					destinosList.add(ciudad);
					
				}

			}

			// Por último hacemos parecido para los aeropuertos (este es un poco más complicado y costoso)

			ResultSet aeropuertosRS = pstmtAeropuertos.executeQuery();

			while (aeropuertosRS.next()) {

				int idAeropuerto = aeropuertosRS.getInt("ID_D");
				String nomAeropuerto = aeropuertosRS.getString("NOM_D");
				double latAeropuerto = aeropuertosRS.getDouble("LAT_D");
				double lonAeropuerto = aeropuertosRS.getDouble("LON_D");
				int idCiudadAeropuerto = aeropuertosRS.getInt("ID_D_PADRE");

				// Conseguimos la ciudad del aeropuerto a través del mapa que hemos creado antes
				
				Ciudad ciudadAeropuerto = ciudadPorID.get(idCiudadAeropuerto);
				
				Aeropuerto aeropuerto = null;
				
				if (ciudadAeropuerto != null) {
					
					// Creamos el aeropuerto

					aeropuerto = new Aeropuerto(idAeropuerto, ciudadAeropuerto, nomAeropuerto, latAeropuerto, lonAeropuerto);
					
					// Rellenamos el mapa correspondiente

					destinosList.add(aeropuerto);
					
				}


			}

		} catch (SQLException e) {

			System.err.println("Error al cargar el destino desde la BD");

			 e.printStackTrace();

		}

		return destinosList;

	}

	// Función recursiva para obtener el destino a partir de su ID (para tareas más
	// pequeñas)

	public static Destino getDestino(int idDestino) {

		Destino destino = null;

		String sql = """
				SELECT ID_D, NOM_D, LAT_D, LON_D, ID_TD, BANDERA, ID_D_PADRE
				FROM DESTINO D, IMAGEN_DESTINO ID
				WHERE D.ISO_CODE = ID.ISO_CODE AND ID_D = ?;
				""";

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
				PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setInt(1, idDestino);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {

				switch (rs.getInt("ID_TD")) {
				case ID_TD_PAIS:

					byte[] imagenBytes = rs.getBytes("BANDERA");

					ImageIcon bandera = null;
					try {

						ByteArrayInputStream bais = new ByteArrayInputStream(imagenBytes);

						bandera = new ImageIcon(ImageIO.read(bais));

					} catch (IOException e) {

						System.err.println("Error al cargar la bandera del país");
						e.printStackTrace();

					}

					destino = new Pais(rs.getInt("ID_D"), rs.getString("NOM_D"), rs.getDouble("LAT_D"),
							rs.getDouble("LON_D"), bandera);

					break;

				case ID_TD_CIUDAD:

					destino = new Ciudad(rs.getInt("ID_D"), (Pais) getDestino(rs.getInt("ID_D_PADRE")),
							rs.getString("NOM_D"), rs.getDouble("LAT_D"), rs.getDouble("LON_D"));

					break;

				case ID_TD_AEROPUERTO:

					destino = new Aeropuerto(rs.getInt("ID_D"), (Ciudad) getDestino(rs.getInt("ID_D_PADRE")),
							rs.getString("NOM_D"), rs.getDouble("LAT_D"), rs.getDouble("LON_D"));

					break;

				default:
					break;
				}

			} else {

				System.err.println("No existe destino con ese ID");

			}

		} catch (SQLException e) {

			System.err.println("Error al cargar el destino");
//			e.printStackTrace();

		}

		return destino;

	}

	public static boolean isCorreoInDB(String correoElectronico) {

		boolean isCorreoInDB = true;

		String sql = """
				SELECT *
				FROM CLIENTE
				WHERE EMAIL_CLI = ?;
				""";

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
				PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, correoElectronico.trim());

			ResultSet rs = pstmt.executeQuery();

			if (!rs.next()) {

				isCorreoInDB = false;

			}

		} catch (SQLException e) {

			System.err.println("Error al acceder a la BD");
//			e.printStackTrace();

		}

		return isCorreoInDB;

	}

	public static boolean registrarUsuario(Cliente cliente) {

		boolean usuarioRegistradoCorrectamente = false;

		String sql = """
				INSERT INTO
				CLIENTE (EMAIL_CLI, NOM_CLI, AP_CLI, CONTR_CLI)
				VALUES (?, ?, ?, ?);
				""";

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
				PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, cliente.getCorreo().trim());
			pstmt.setString(2, cliente.getNombre().trim());
			pstmt.setString(3, cliente.getApellidos().trim());
			pstmt.setString(4, PasswordSecurity.hashPassword(cliente.getContrasena().trim()));

			int rowCount = pstmt.executeUpdate();

			if (rowCount > 0) {

				usuarioRegistradoCorrectamente = true;

			}

		} catch (SQLException e) {

			System.err.println("Error al registrar el usuario");
//			e.printStackTrace();

		}

		return usuarioRegistradoCorrectamente;

	}

	public static boolean cambiarContrasenaUsuario(String correoElectronico, String nuevaContrasena) {

		boolean contrasenaCambiadaCorrectamente = false;

		String sql = "UPDATE CLIENTE SET CONTR_CLI = ? WHERE EMAIL_CLI = ?";

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
				PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, PasswordSecurity.hashPassword(nuevaContrasena.trim()));
			pstmt.setString(2, correoElectronico.trim());

			int rowCount = pstmt.executeUpdate();

			if (rowCount > 0) {

				contrasenaCambiadaCorrectamente = true;

			}

		} catch (SQLException e) {

			System.err.println("Error al cambiar la contraseña del usuario con correo: " + correoElectronico);
//			e.printStackTrace();

		}

		return contrasenaCambiadaCorrectamente;

	}
	
	public static boolean cambiarDatosUsuario(Cliente cliente) {

		boolean datosCambiadosCorrectamente = false;

		String sql = """
				UPDATE CLIENTE
				SET NOM_CLI = ?, AP_CLI = ?, CONTR_CLI = ?
				WHERE EMAIL_CLI = ?
				""";

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
				PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, cliente.getNombre().trim());
			pstmt.setString(2, cliente.getApellidos().trim());
			pstmt.setString(3, PasswordSecurity.hashPassword(cliente.getContrasena().trim()));
			pstmt.setString(4, cliente.getCorreo().trim());

			int rowCount = pstmt.executeUpdate();

			if (rowCount > 0) {

				datosCambiadosCorrectamente = true;

				PanelVolverRegistrarseIniciarSesion.setCliente(cliente);
				
			}

		} catch (SQLException e) {

			System.err.println("Error al cambiar los datos del usuario con correo: " + cliente.getCorreo());
//			e.printStackTrace();

		}

		return datosCambiadosCorrectamente;

	}
	
	public static boolean iniciarSesion(String correoElectronico, String contrasena) {

		boolean sesionIniciadaCorrectamente = false;

		String sql = """
				SELECT *
				FROM CLIENTE
				WHERE EMAIL_CLI = ?
				""";

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
				PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, correoElectronico.trim());

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {

				String hashContrasenaBD = rs.getString("CONTR_CLI");

				if (PasswordSecurity.checkPassword(contrasena.trim(), hashContrasenaBD)) {

					String nombre = rs.getString("NOM_CLI");
					String apellidos = rs.getString("AP_CLI");

					PanelVolverRegistrarseIniciarSesion.iniciarSesion(new Cliente(correoElectronico.trim(), nombre.trim(), apellidos.trim(), contrasena.trim()));

					sesionIniciadaCorrectamente = true;

				}

			}

		} catch (SQLException e) {

			System.err.println("Error al iniciar sesión");
//			e.printStackTrace();

		}

		return sesionIniciadaCorrectamente;

	}

public static List<BufferedImage> getImagenesAlojamiento(Class<? extends Alojamiento> clase, int idAlojamiento) {
		
		List<BufferedImage> imagenesAlojamiento = new ArrayList<BufferedImage>();
		
		String sqlSelect = "";
		
		if (clase.equals(Apartamento.class)) {

			sqlSelect = """
						SELECT IMAGEN_AP AS IMAGEN
						FROM IMAGEN_APARTAMENTO
						WHERE ID_AP = ?;
						""";

		} else if (clase.equals(Hotel.class)) {

			sqlSelect = """
						SELECT IMAGEN_H AS IMAGEN
						FROM IMAGEN_HOTEL
						WHERE ID_H = ?;
						""";

		}
		
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmt = con.prepareStatement(sqlSelect)) {
			
			pstmt.setInt(1, idAlojamiento);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				try {
					
					byte[] imagenBytes = rs.getBytes("IMAGEN");
					ByteArrayInputStream bais = new ByteArrayInputStream(imagenBytes);
					
					BufferedImage imagenCargada = ImageIO.read(bais);
					
					imagenesAlojamiento.add(imagenCargada);
					
				} catch (IOException e) {
					
					e.printStackTrace();
					
				}
				
			}
			
		} catch (SQLException e) {
			
			System.err.println("Error al cargar las imágenes del alojamiento");
			
		}
		
		return imagenesAlojamiento;
		
	}
	
	public static List<Resena> getResenasAlojamiento(Class<? extends Alojamiento> clase, int idAlojamiento) {

		List<Resena> resenasAlojamiento = new ArrayList<Resena>();

		String sqlSelect = "";

		if (clase.equals(Apartamento.class)) {

			sqlSelect = """
					SELECT NOM_CLI, AP_CLI, ESTRELLAS_R, MENSAJE_R, FECHA_R
					FROM RESENA R, CLIENTE CLI
					WHERE ID_R IN (SELECT ID_R
								   FROM RESERVA_AP RVA_AP
								   WHERE ID_AP = ? AND
								         CLI.EMAIL_CLI = RVA_AP.EMAIL_CLI);
					""";

		} else if (clase.equals(Hotel.class)) {

			sqlSelect = """
					SELECT NOM_CLI, AP_CLI, ESTRELLAS_R, MENSAJE_R, FECHA_R
					FROM RESENA R, CLIENTE CLI
					WHERE ID_R IN (SELECT ID_R
								   FROM RESERVA_H RVA_H
								   WHERE ID_H = ? AND
								         CLI.EMAIL_CLI = RVA_H.EMAIL_CLI);
					""";

		}

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
				PreparedStatement pstmt = con.prepareStatement(sqlSelect)) {

			pstmt.setInt(1, idAlojamiento);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				Resena resena = new Resena(rs.getString("NOM_CLI") + " " + rs.getString("AP_CLI"),
						rs.getDouble("ESTRELLAS_R"), rs.getString("MENSAJE_R"),
						LocalDate.parse(rs.getString("FECHA_R")));

				resenasAlojamiento.add(resena);

			}

		} catch (SQLException e) {

			System.err.println("Error al cargar las reseñas del alojamiento");
//			e.printStackTrace();

		}

		return resenasAlojamiento;

	}
	
	public static boolean registrarApartamento(String nombre, String direccion, String descripcion, double precioNP, int capMax, List<BufferedImage> imagenesSeleccionadas, Ciudad ciudad) {

		boolean apartamentoRegistradoCorrectamente = false;

		String sqlInsertAp = """
						     INSERT INTO
						     APARTAMENTO (NOM_AP, DIR_AP, DESC_AP, PRECIO_NP_AP, CAP_MAX_AP, EMAIL_CLI, ID_D)
						     VALUES (?, ?, ?, ? ,?, ?, ?);
						     """;
		
		String sqlInsertImg = """
							  INSERT INTO 
							  IMAGEN_APARTAMENTO (IMAGEN_AP, ID_AP)
							  VALUES (?, ?);
							  """;
		
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmtInsertAp = con.prepareStatement(sqlInsertAp, Statement.RETURN_GENERATED_KEYS);
			 PreparedStatement pstmtInsertImg = con.prepareStatement(sqlInsertImg)) {

			pstmtInsertAp.setString(1, nombre.trim());
			pstmtInsertAp.setString(2, direccion.trim());
			pstmtInsertAp.setString(3, descripcion.trim());
			pstmtInsertAp.setDouble(4, precioNP);
			pstmtInsertAp.setInt(5, capMax);
			pstmtInsertAp.setString(6, PanelVolverRegistrarseIniciarSesion.getCliente().getCorreo().trim());
			pstmtInsertAp.setInt(7, ciudad.getId());

			int rowCount = pstmtInsertAp.executeUpdate();

			if (rowCount > 0) {

				try (ResultSet rsKey = pstmtInsertAp.getGeneratedKeys()) {
					
					if (rsKey.next()) {
						
						for (BufferedImage imagen : imagenesSeleccionadas) {
							
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ImageIO.write(imagen, "png", baos);
							byte[] imagenBytes = baos.toByteArray();
							
							pstmtInsertImg.setBytes(1, imagenBytes);
							pstmtInsertImg.setInt(2, rsKey.getInt(1));
							
							pstmtInsertImg.executeUpdate();
							
						}
						
						apartamentoRegistradoCorrectamente = true;
						
					}
					
				} catch (Exception e) {
					
					System.err.println("Error al guardar las imágenes");
					
				}

			}

			

		} catch (SQLException e) {

			System.err.println("Error al registrar el nuevo apartamento");
			e.printStackTrace();

		}

		return apartamentoRegistradoCorrectamente;

	}
	
}
