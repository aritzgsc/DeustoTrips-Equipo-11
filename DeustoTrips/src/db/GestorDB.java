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
import java.time.temporal.ChronoUnit;
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
import gui.main.PanelPestanasBusqueda;
import gui.main.PanelVolverRegistrarseIniciarSesion;
import gui.util.PanelAlojamiento;

// Clase que contiene todos los métodos que utilizan la BD

public class GestorDB {

	private static final String SQLITE_FILE = "resources/db/DBDeustoTrips.db";									// Nombre del archivo (Descargar archivo completo en https://drive.google.com/file/d/1kU5LFCCHnHNaajWIucd9xlAuA4OPNjuV/view?usp=sharing)
	private static final String CONNECTION_STRING = "jdbc:sqlite:" + SQLITE_FILE + "?journal_mode=WAL";			// Ponemos lo último para que la BD no se bloquee cuando se está leyendo algo (sin esto no podríamos buscar y registrarnos a la vez por ejemplo)

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

	// Función para cargar todos los destinos en el programa al ejecutarlo
	
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

				ImageIcon banderaPais = null;

				try {

					byte[] banderaBytes = paisesRS.getBytes("BANDERA");
					ByteArrayInputStream bais = new ByteArrayInputStream(banderaBytes);
					
					banderaPais = new ImageIcon(ImageIO.read(bais));

				} catch (IOException e) {

					System.err.println("Error al cargar la bandera de " + nomPais);
					 e.printStackTrace();

				}

				// Creamos el país

				Pais pais = new Pais(idPais, nomPais, latPais, lonPais, banderaPais);

				// Rellenamos los mapas correspondientes que utilizaremos más tarde (además de la lista que devolverá la función)

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

	// FIN Función para cargar todos los destinos en el programa al ejecutarlo 
	////
	// Función recursiva para obtener el destino a partir de su ID (para tareas más pequeñas)

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

					// En caso de que sea un país lo creamos directamente con su respectiva bandera
					
					byte[] imagenBytes = rs.getBytes("BANDERA");

					ImageIcon bandera = null;
					try {

						ByteArrayInputStream bais = new ByteArrayInputStream(imagenBytes);

						bandera = new ImageIcon(ImageIO.read(bais));

					} catch (IOException e) {

						System.err.println("Error al cargar la bandera del país");
						e.printStackTrace();

					}

					destino = new Pais(rs.getInt("ID_D"), rs.getString("NOM_D"), rs.getDouble("LAT_D"), rs.getDouble("LON_D"), bandera);

					break;

				case ID_TD_CIUDAD:

					// En caso de que sea una ciudad tenemos que llamar a la misma función pero con el ID_D_PADRE para encontrar el país al que pertenece la ciudad
					
					destino = new Ciudad(rs.getInt("ID_D"), (Pais) getDestino(rs.getInt("ID_D_PADRE")), rs.getString("NOM_D"), rs.getDouble("LAT_D"), rs.getDouble("LON_D"));

					break;

				case ID_TD_AEROPUERTO:

					// En caso de que sea un aeropuerto tenemos que llamar a la misma función pero con el ID_D_PADRE para encontrar la ciudad al que pertenece el aeropuerto
					
					destino = new Aeropuerto(rs.getInt("ID_D"), (Ciudad) getDestino(rs.getInt("ID_D_PADRE")), rs.getString("NOM_D"), rs.getDouble("LAT_D"), rs.getDouble("LON_D"));

					break;

				default:
					break;
				}

			} else {

				System.err.println("No existe destino con ese ID");

			}

		} catch (SQLException e) {

			System.err.println("Error al cargar el destino");
			e.printStackTrace();

		}

		return destino;

	}

	// FIN Función recursiva para obtener el destino a partir de su ID
	////
	// Función para obtener las ciudades de un país a partir de un código ISO (utilizado solamente para cargar datos en la BD)

	public static List<Ciudad> getCiudadesPais(String isoCode) {

		List<Ciudad> ciudadesPais = new ArrayList<Ciudad>();

		String sqlPais = """
						 SELECT ID_D, NOM_D, LAT_D, LON_D, BANDERA 
						 FROM DESTINO D, IMAGEN_DESTINO ID 
						 WHERE ID_TD = 1 AND D.ISO_CODE = ID.ISO_CODE AND D.ISO_CODE = ?;
						 """;
		
		String sqlCiudades = """
							 SELECT * 
							 FROM DESTINO 
							 WHERE ID_TD = 2 AND ISO_CODE = ?;
							 """;

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmtPais = con.prepareStatement(sqlPais);
			 PreparedStatement pstmtCiudades = con.prepareStatement(sqlCiudades)) {

			pstmtPais.setString(1, isoCode);

			ResultSet rsPais = pstmtPais.executeQuery();

			// Recuperamos el país al que pertenecerán todas las ciudades posteriormente
			
			Pais paisCiudades = null;

			if (rsPais.next()) {

				ImageIcon bandera = null;
				
				try {

					byte[] imagenBytes = rsPais.getBytes("BANDERA");
					ByteArrayInputStream bais = new ByteArrayInputStream(imagenBytes);

					bandera = new ImageIcon(ImageIO.read(bais));

				} catch (IOException e) {

					System.err.println("Error al cargar la bandera del país");
					e.printStackTrace();

				}

				paisCiudades = new Pais(rsPais.getInt("ID_D"), rsPais.getString("NOM_D"), rsPais.getDouble("LAT_D"), rsPais.getDouble("LON_D"), bandera);

				pstmtCiudades.setString(1, isoCode);

				ResultSet rsCiudades = pstmtCiudades.executeQuery();

				while (rsCiudades.next()) {

					// Una vez obtenido el país hacemos otra consulta y creamos las ciudades (y las añadimos a la lista que devolveremos)
					
					Ciudad ciudad = new Ciudad(rsCiudades.getInt("ID_D"), paisCiudades, rsCiudades.getString("NOM_D"), rsCiudades.getDouble("LAT_D"), rsCiudades.getDouble("LON_D"));

					ciudadesPais.add(ciudad);

				}

			}

		} catch (SQLException e) {

			System.err.println("Error al cargar las ciudades");
			e.printStackTrace();

		}

		return ciudadesPais;

	}

	// Función para obtener las ciudades de un país a partir de un código ISO
	////
	// Función para comprobar si un correo está registrado en la BD
	
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

				// Si no existe ningún correo así el correo no está registrado en la BD
				
				isCorreoInDB = false;

			}

		} catch (SQLException e) {

			System.err.println("Error al acceder a la BD");
			e.printStackTrace();

		}

		return isCorreoInDB;

	}

	// FIN Función para comprobar si un correo está registrado en la BD
	////
	// Función para registrar a un nuevo cliente (se comprueba antes de la llamada que no esté el correo en la BD)
	
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

				// Si se ejecuta correctamente debería dar > 0 => devolvemos true (para saber que se ha registrado correctamente)
				
				usuarioRegistradoCorrectamente = true;

			}

		} catch (SQLException e) {

			System.err.println("Error al registrar el usuario");
			e.printStackTrace();

		}

		return usuarioRegistradoCorrectamente;

	}

	// FIN Función para registrar a un nuevo cliente
	////
	// Función para cambiar los datos de un cliente existente
	
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

				// Si se ha ejecutado correctamente debería devolver > 0 => devolvemos true y además cambiamos los datos del cliente que teníamos por los nuevos
					
				datosCambiadosCorrectamente = true;

				PanelVolverRegistrarseIniciarSesion.setCliente(cliente);

			}

		} catch (SQLException e) {

			System.err.println("Error al cambiar los datos del usuario con correo: " + cliente.getCorreo());
			e.printStackTrace();

		}

		return datosCambiadosCorrectamente;

	}

	// FIN Función para cambiar los datos de un cliente existente
	////
	// Función para cambiar la contraseña de un usuario
	
	public static boolean cambiarContrasenaUsuario(String correoElectronico, String nuevaContrasena) {

		boolean contrasenaCambiadaCorrectamente = false;

		String sql = """
					 UPDATE CLIENTE 
					 SET CONTR_CLI = ? 
					 WHERE EMAIL_CLI = ?;
					 """;

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setString(1, PasswordSecurity.hashPassword(nuevaContrasena.trim()));
			pstmt.setString(2, correoElectronico.trim());

			int rowCount = pstmt.executeUpdate();

			if (rowCount > 0) {

				// Si se ha cambiado correctamente debería devolver > 0 => devolvemos true para saber que ha cambiado
				
				contrasenaCambiadaCorrectamente = true;

			}

		} catch (SQLException e) {

			System.err.println("Error al cambiar la contraseña del usuario con correo: " + correoElectronico);
			e.printStackTrace();

		}

		return contrasenaCambiadaCorrectamente;

	}

	// FIN Función para cambiar la contraseña de un usuario
	////
	// Función para iniciar sesión (comprueba si el correo escrito se corresponde con la contraseña escrita)
	
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

				// Cogemos el hash guardado en la BD y comprobamos si alguno de los hash de la contraseña escrita coincide con él
				
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
			e.printStackTrace();

		}

		return sesionIniciadaCorrectamente;

	}

	// FIN Función para iniciar sesión
	////
	// Función para buscar los IDs de los alojamientos que cumplan ciertas condiciones a partir de una serie de campos (los valores de los elementos de búsqueda)(hacemos con IDs para tener mayor rendimiento, si cargaramos todos los alojamientos completos directamente tardaría mucho)
	
	public static Map<Class<? extends Alojamiento>, List<Integer>> buscarIdsAlojamientos(Destino ubicacion, LocalDate fechaEntrada, LocalDate fechaSalida, int nPersonas, double precioMin, double precioMax, double valoracionMin) {

		Map<Class<? extends Alojamiento>, List<Integer>> idsAlojamientosEncontradosPorTipo = new HashMap<Class<? extends Alojamiento>, List<Integer>>();
		idsAlojamientosEncontradosPorTipo.put(Apartamento.class, new ArrayList<Integer>());
		idsAlojamientosEncontradosPorTipo.put(Hotel.class, new ArrayList<Integer>());

		// Apartamentos que => 1. Estén en el destino seleccionado (si lo seleccionado una ciudad el ID_D igual, si es un país el ID_D del padre (país) igual)
		//					   2. No estén reservados entre las fechas marcadas
		// 					   3. Su capacidad máxima sea mayor o igual que el número de personas para las que se quiere reservar
		//					   4. Su precio final esté entre el mínimo y el máximo asignado por el filtroPrecio
		//					   5. La media de las reseñas sea mayor o igual que la indicada en el filtroValoración
		
		String sqlSelectIdsApartamentos = """
										  SELECT ID_AP
										  FROM APARTAMENTO AP
										  WHERE ID_D IN (SELECT ID_D FROM DESTINO D WHERE ID_D = ? OR ID_D_PADRE = ?) AND
											    NOT EXISTS (SELECT *
											                FROM RESERVA_AP RVA_AP
											     			WHERE F_INI_RVA <= ? AND
											     			 	  F_FIN_RVA > ? AND
											     			 	  RVA_AP.ID_AP = AP.ID_AP) AND
											    CAP_MAX_AP >= ? AND
											    (PRECIO_NP_AP * ? * ?) BETWEEN ? AND ? AND
											    ? <= COALESCE((SELECT AVG(ESTRELLAS_R)
											     	   		   FROM RESENA R, RESERVA_AP RVA_AP
											     	   		   WHERE R.ID_R = RVA_AP.ID_R AND
											     	   		 		 RVA_AP.ID_AP = AP.ID_AP), 0);
										  """;

		// Hoteles que => 1. Estén en el destino seleccionado (si lo seleccionado una ciudad el ID_D igual, si es un país el ID_D del padre (país) igual)
		//				  2. No esté lleno en las fechas seleccionadas (Número de habitaciones del hotel - suma de habitaciones reservadas entre las fechas seleccionadas - número de habitaciones a ocupar >= 0
		//				  3. Su precio final esté entre el mínimo y el máximo asignado por el filtroPrecio
		//				  4. La media de las reseñas sea mayor o igual que la indicada en el filtroValoración
		// 				  5. No haya sido reservado previamente por el mismo usuario en las mismas fechas (un usuario no puede reservar dos veces el mismo hotel el mismo día)
		
		String sqlSelectIdsHoteles = """
									 SELECT ID_H
									 FROM HOTEL H
									 WHERE ID_D IN (SELECT ID_D FROM DESTINO D WHERE ID_D = ? OR ID_D_PADRE = ?) AND
									 	   (H.NUM_HABS - COALESCE((SELECT SUM(CEIL((N_PER * 1.0) / H.CAP_MAX_HAB))
						  		                                   FROM RESERVA_H RVA_H
								                                   WHERE F_INI_RVA <= ? AND
								                                         F_FIN_RVA > ? AND
								                                         RVA_H.ID_H = H.ID_H), 0) - CEIL(? / H.CAP_MAX_HAB)) >= 0 AND
									 	   (PRECIO_NHAB_H * CEIL(? / CAP_MAX_HAB) * ?) BETWEEN ? AND ? AND
									 	   ? <= COALESCE((SELECT AVG(ESTRELLAS_R)
									 	     	  		  FROM RESENA R, RESERVA_H RVA_H
									 	     	  		  WHERE R.ID_R = RVA_H.ID_R AND
									 	     	  			    RVA_H.ID_H = H.ID_H), 0) AND
									 	   NOT EXISTS(SELECT *
									 	  			  FROM RESERVA_H RVA_H
									 	  			  WHERE F_INI_RVA <= ? AND
									 	  			 	    F_FIN_RVA > ? AND
									 	  			 	    EMAIL_CLI = ? AND
									 	  			 	    RVA_H.ID_H = H.ID_H);
									 """;

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmtApartamentos = con.prepareStatement(sqlSelectIdsApartamentos);
			 PreparedStatement pstmtHoteles = con.prepareStatement(sqlSelectIdsHoteles);) {

			int nNoches = (int) ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);

			pstmtApartamentos.setInt(1, ubicacion.getId());
			pstmtApartamentos.setInt(2, ubicacion.getId());
			pstmtApartamentos.setString(3, fechaSalida.toString());
			pstmtApartamentos.setString(4, fechaEntrada.toString());
			pstmtApartamentos.setInt(5, nPersonas);
			pstmtApartamentos.setInt(6, nPersonas);
			pstmtApartamentos.setInt(7, nNoches);
			pstmtApartamentos.setDouble(8, precioMin);
			pstmtApartamentos.setDouble(9, precioMax);
			pstmtApartamentos.setDouble(10, valoracionMin);

			ResultSet rsAP = pstmtApartamentos.executeQuery();

			while (rsAP.next()) {

				// Añadimos los IDs de los apartamentos adecuados al mapa que devuelve la función
				
				List<Integer> listaMapa = idsAlojamientosEncontradosPorTipo.get(Apartamento.class);

				listaMapa.add(rsAP.getInt("ID_AP"));

			}

			pstmtHoteles.setInt(1, ubicacion.getId());
			pstmtHoteles.setInt(2, ubicacion.getId());
			pstmtHoteles.setString(3, fechaSalida.toString());
			pstmtHoteles.setString(4, fechaEntrada.toString());
			pstmtHoteles.setDouble(5, (double) nPersonas);
			pstmtHoteles.setDouble(6, (double) nPersonas);
			pstmtHoteles.setDouble(7, nNoches);
			pstmtHoteles.setDouble(8, precioMin);
			pstmtHoteles.setDouble(9, precioMax);
			pstmtHoteles.setDouble(10, valoracionMin);
			pstmtHoteles.setString(11, fechaSalida.toString());
			pstmtHoteles.setString(12, fechaEntrada.toString());
			pstmtHoteles.setString(13, PanelVolverRegistrarseIniciarSesion.getCliente() != null? PanelVolverRegistrarseIniciarSesion.getCliente().getCorreo() : "");

			ResultSet rsH = pstmtHoteles.executeQuery();

			while (rsH.next()) {

				// Añadimos los IDs de los hoteles adecuados al mapa que devuelve la función
				
				List<Integer> listaMapa = idsAlojamientosEncontradosPorTipo.get(Hotel.class);

				listaMapa.add(rsH.getInt("ID_H"));

			}

		} catch (SQLException e) {

			System.err.println("Error al buscar alojamientos");
			e.printStackTrace();

		}

		return idsAlojamientosEncontradosPorTipo;

	}

	// FIN Función para buscar los IDs de los alojamientos que cumplan ciertas condiciones a partir de una serie de campos
	////
	// Función para conseguir un alojamiento a partir de un ID y el tipo que es (indicar además si se quieren cargar todas las imágenes del alojamiento o solamente 1)
	
	public static Alojamiento getAlojamiento(Class<? extends Alojamiento> clase, int idAlojamiento, boolean cargaCompleta) {

		Alojamiento alojamiento = null;

		String sqlSelect = "";

		if (clase.equals(Apartamento.class)) {

			sqlSelect = """
						SELECT NOM_AP, DIR_AP, DESC_AP, PRECIO_NP_AP, CAP_MAX_AP, EMAIL_CLI, ID_D
						FROM APARTAMENTO
						WHERE ID_AP = ?;
						""";

		} else if (clase.equals(Hotel.class)) {

			sqlSelect = """
						SELECT NOM_H, DIR_H, DESC_H, NUM_HABS, CAP_MAX_HAB, PRECIO_NHAB_H, ID_D
						FROM HOTEL
						WHERE ID_H = ?;
						""";

		}

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmt = con.prepareStatement(sqlSelect)) {

			pstmt.setInt(1, idAlojamiento);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {

				// Creamos el alojamiento dependiendo de la clase a la que pertenezca con el número de imágenes dependiendo del campo cargaCompleta (Todas o solo 1)
				
				int limiteImagenes = cargaCompleta ? Integer.MAX_VALUE : 1;
				List<BufferedImage> imagenesAlojamiento = getImagenesAlojamiento(clase, idAlojamiento, limiteImagenes);

				if (clase.equals(Apartamento.class)) {

					alojamiento = new Apartamento(idAlojamiento, rs.getString("NOM_AP"), rs.getString("DIR_AP"), (Ciudad) getDestino(rs.getInt("ID_D")), rs.getString("DESC_AP"), getResenasAlojamiento(Apartamento.class, idAlojamiento), imagenesAlojamiento, rs.getDouble("PRECIO_NP_AP"), rs.getInt("CAP_MAX_AP"), rs.getString("EMAIL_CLI"));

				} else if (clase.equals(Hotel.class)) {

					alojamiento = new Hotel(idAlojamiento, rs.getString("NOM_H"), rs.getString("DIR_H"), (Ciudad) getDestino(rs.getInt("ID_D")), rs.getString("DESC_H"), getResenasAlojamiento(Hotel.class, idAlojamiento), imagenesAlojamiento, rs.getInt("NUM_HABS"), rs.getInt("CAP_MAX_HAB"), rs.getDouble("PRECIO_NHAB_H"));

				}

			}

		} catch (SQLException e) {

			System.err.println("Error al cargar el alojamiento");
			e.printStackTrace();
			
		}

		return alojamiento;

	}

	// FIN Función para buscar los IDs de los alojamientos que cumplan ciertas condiciones a partir de una serie de campos
	////
	// Función para obtener las imágenes de un alojamiento (tantas como el límite indique (normalmente todas o solo 1 por la función de arriba))
	
	public static List<BufferedImage> getImagenesAlojamiento(Class<? extends Alojamiento> clase, int idAlojamiento, int limiteImagenes) {

		List<BufferedImage> imagenesAlojamiento = new ArrayList<BufferedImage>();

		String sqlSelect = "";

		if (clase.equals(Apartamento.class)) {

			sqlSelect = """
						SELECT IMAGEN_AP AS IMAGEN
						FROM IMAGEN_APARTAMENTO
						WHERE ID_AP = ?
						ORDER BY ID_IAP;
						""";

		} else if (clase.equals(Hotel.class)) {

			sqlSelect = """
						SELECT IMAGEN_H AS IMAGEN
						FROM IMAGEN_HOTEL
						WHERE ID_H = ?
						ORDER BY ID_IH;
						""";

		}

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmt = con.prepareStatement(sqlSelect)) {

			pstmt.setInt(1, idAlojamiento);

			ResultSet rs = pstmt.executeQuery();

			// Cargamos las imágenes hasta que la lista tenga tantas como el límite indique o hasta que se acaben las que hay en la BD
			
			while (rs.next() && imagenesAlojamiento.size() < limiteImagenes) {

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
			e.printStackTrace();
			
		}

		return imagenesAlojamiento;

	}

	// FIN Función para obtener las imágenes de un alojamiento
	////
	// Función para conseguir el resto de imágenes (sin la primera) 
	
	public static List<BufferedImage> getRestoImagenesAlojamiento(Class<? extends Alojamiento> clase, int idAlojamiento) {

		List<BufferedImage> imagenesRestantes = new ArrayList<BufferedImage>();

		String sqlSelect = "";

		if (clase.equals(Apartamento.class)) {

			sqlSelect = """
						SELECT IMAGEN_AP AS IMAGEN
						FROM IMAGEN_APARTAMENTO
						WHERE ID_AP = ?
						ORDER BY ID_IAP;
						""";

		} else if (clase.equals(Hotel.class)) {

			sqlSelect = """
						SELECT IMAGEN_H AS IMAGEN
						FROM IMAGEN_HOTEL
						WHERE ID_H = ?
						ORDER BY ID_IH;
						""";

		}

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmt = con.prepareStatement(sqlSelect)) {
			
			pstmt.setInt(1, idAlojamiento);
			
			ResultSet rs = pstmt.executeQuery();
			
			boolean primeraSaltada = false;
			
			while (rs.next()) {
				
				if (!primeraSaltada) {
					primeraSaltada = true;
					continue;
				}
				
				try {

					byte[] imagenBytes = rs.getBytes("IMAGEN");
					ByteArrayInputStream bais = new ByteArrayInputStream(imagenBytes);

					imagenesRestantes.add(ImageIO.read(bais));

				} catch (IOException e) {

					e.printStackTrace();

				}
				
			}
			
		} catch (SQLException e) {
			
			System.err.println("Error al cargar el resto de imágenes");
			e.printStackTrace();
			
		}
		
		return imagenesRestantes;
		
	}

	// FIN Función para conseguir el resto de imágenes
	////
	// Función para conseguir todas las reseñas que le han puesto a un alojamiento
	
	public static List<Resena> getResenasAlojamiento(Class<? extends Alojamiento> clase, int idAlojamiento) {

		List<Resena> resenasAlojamiento = new ArrayList<Resena>();

		String sqlSelect = "";

		if (clase.equals(Apartamento.class)) {

			sqlSelect = """
						SELECT NOM_CLI, AP_CLI, ID_R, ESTRELLAS_R, MENSAJE_R, FECHA_R
						FROM RESENA R, CLIENTE CLI
						WHERE ID_R IN (SELECT ID_R
									   FROM RESERVA_AP RVA_AP
									   WHERE ID_AP = ? AND
									         CLI.EMAIL_CLI = RVA_AP.EMAIL_CLI);
						""";

		} else if (clase.equals(Hotel.class)) {

			sqlSelect = """
						SELECT NOM_CLI, AP_CLI, ID_R, ESTRELLAS_R, MENSAJE_R, FECHA_R
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

				Resena resena = new Resena(rs.getInt("ID_R"), rs.getString("NOM_CLI") + " " + rs.getString("AP_CLI"), rs.getDouble("ESTRELLAS_R"), rs.getString("MENSAJE_R"), LocalDate.parse(rs.getString("FECHA_R")));

				resenasAlojamiento.add(resena);

			}

		} catch (SQLException e) {

			System.err.println("Error al cargar las reseñas del alojamiento");
			e.printStackTrace();

		}

		return resenasAlojamiento;

	}

	// FIN Función para conseguir todas las reseñas que le han puesto a un alojamiento
	////
	// Función para guardar una nueva reseña asociada a un id de reserva (de apartamento u hotel)
	
	public static int guardarNuevaResena(Class<? extends Alojamiento> clase, int idRva, Resena resena) {

		int idR = -1;

		String sqlInsert = """
						   INSERT INTO RESENA (ESTRELLAS_R, MENSAJE_R, FECHA_R)
						   VALUES (?, ?, ?);
						   """;

		String sqlUpdate = "";

		if (clase.equals(Apartamento.class)) {

			sqlUpdate = """
						UPDATE RESERVA_AP
						SET ID_R = ?
						WHERE ID_RVA_AP = ?;
						""";

		} else if (clase.equals(Hotel.class)) {

			sqlUpdate = """
						UPDATE RESERVA_H
						SET ID_R = ?
						WHERE ID_RVA_H = ?;
						""";

		}

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmtInsert = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
			 PreparedStatement pstmtUpdate = con.prepareStatement(sqlUpdate)) {

			pstmtInsert.setDouble(1, resena.getEstrellas());
			pstmtInsert.setString(2, resena.getMensaje());
			pstmtInsert.setString(3, resena.getFecha().toString());

			// Creamos la reseña
			
			int rowCount1 = pstmtInsert.executeUpdate();

			if (rowCount1 > 0) {

				// Si ha sido creada correctamente recuperamos el ID_R generado por estar ese campo en modo Autoincrement
				
				try (ResultSet rsIdR = pstmtInsert.getGeneratedKeys()) {

					if (rsIdR.next()) {

						pstmtUpdate.setInt(1, rsIdR.getInt(1));
						pstmtUpdate.setInt(2, idRva);

						int rowCount2 = pstmtUpdate.executeUpdate();

						if (rowCount2 > 0) {

							// Devolvemos el ID de la reseña creada para luego crear una instancia de Reseña con ese ID y no crear nuevas sino actualizar esta
							
							idR = rsIdR.getInt(1);

						}

					}

				}

			}

		} catch (SQLException e) {

			System.err.println("Error al crear la reseña y asignarla a la reserva correspondiente");
			e.printStackTrace();
			
		}

		return idR;

	}

	// FIN Función para guardar una nueva reseña asociada a un id de reserva
	////
	// Función para actualizar una reseña
	
	public static boolean actualizarResena(Resena resena) {

		boolean resenaActualizada = false;

		String sqlUpdate = """
						   UPDATE RESENA
						   SET ESTRELLAS_R = ?, MENSAJE_R = ?, FECHA_R = ?
						   WHERE ID_R = ?;
						   """;

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmt = con.prepareStatement(sqlUpdate)) {

			pstmt.setDouble(1, resena.getEstrellas());
			pstmt.setString(2, resena.getMensaje());
			pstmt.setString(3, resena.getFecha().toString());
			pstmt.setInt(4, resena.getId());

			int rowCount = pstmt.executeUpdate();

			if (rowCount > 0) {

				resenaActualizada = true;

			}

		} catch (SQLException e) {

			System.err.println("Error al actualizar la reseña");

		}

		return resenaActualizada;

	}

	// FIN Función para actualizar una reseña
	////
	// Función para obtener las reservas del cliente con la sesión iniciada (devuelve directamente todas las instancias de PanelAlojamiento no el alojamiento tal cual) -> hacemos eso porque si no tendríamos que guardar todos los datos que usa el PanelAlojamiento de alguna forma y es más sencillo hacerlo así
	
	public static List<PanelAlojamiento> getReservasAlojamientos() {

		List<PanelAlojamiento> reservasAlojamientos = new ArrayList<PanelAlojamiento>();

		Cliente cliente = PanelVolverRegistrarseIniciarSesion.getCliente();

		String sqlSelectRvaAP = """
								SELECT ID_RVA_AP, F_INI_RVA, F_FIN_RVA, N_PER, P_RVA_AP, ID_AP, ID_R
								FROM RESERVA_AP
								WHERE EMAIL_CLI = ?;
								""";

		String sqlSelectRvaH = """
							   SELECT ID_RVA_H, F_INI_RVA, F_FIN_RVA, N_PER, P_RVA_H, ID_H, ID_R
							   FROM RESERVA_H
							   WHERE EMAIL_CLI = ?;
							   """;

		String sqlSelectResena = """
								 SELECT ID_R, ESTRELLAS_R, MENSAJE_R, FECHA_R
								 FROM RESENA
								 WHERE ID_R = ?;
								 """;

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmtSelectRvaAP = con.prepareStatement(sqlSelectRvaAP);
			 PreparedStatement pstmtSelectRvaH = con.prepareStatement(sqlSelectRvaH);
			 PreparedStatement pstmtSelectResena = con.prepareStatement(sqlSelectResena)) {

			pstmtSelectRvaAP.setString(1, cliente.getCorreo());

			ResultSet rsRvaAP = pstmtSelectRvaAP.executeQuery();

			while (rsRvaAP.next()) {

				pstmtSelectResena.setInt(1, rsRvaAP.getInt("ID_R"));

				ResultSet rsResena = pstmtSelectResena.executeQuery();

				Resena resena = null;

				if (rsResena.next()) {

					resena = new Resena(rsResena.getInt("ID_R"), cliente.getNombre() + " " + cliente.getApellidos().split(" ")[0], rsResena.getDouble("ESTRELLAS_R"), rsResena.getString("MENSAJE_R"), LocalDate.parse(rsResena.getString("FECHA_R")));

				}

				PanelAlojamiento panelAlojamiento = new PanelAlojamiento(getAlojamiento(Apartamento.class, rsRvaAP.getInt("ID_AP"), true), rsRvaAP.getInt("N_PER"), LocalDate.parse(rsRvaAP.getString("F_INI_RVA")), LocalDate.parse(rsRvaAP.getString("F_FIN_RVA")), rsRvaAP.getDouble("P_RVA_AP"), rsRvaAP.getInt("ID_RVA_AP"), resena, PanelAlojamiento.MODO_CANCELAR_O_DEJARRESENA);

				reservasAlojamientos.add(panelAlojamiento);

			}

			pstmtSelectRvaH.setString(1, cliente.getCorreo());

			ResultSet rsRvaH = pstmtSelectRvaH.executeQuery();

			while (rsRvaH.next()) {

				pstmtSelectResena.setInt(1, rsRvaH.getInt("ID_R"));

				ResultSet rsResena = pstmtSelectResena.executeQuery();

				Resena resena = null;

				if (rsResena.next()) {

					resena = new Resena(rsResena.getInt("ID_R"), cliente.getNombre() + " " + cliente.getApellidos().split(" ")[0], rsResena.getDouble("ESTRELLAS_R"), rsResena.getString("MENSAJE_R"), LocalDate.parse(rsResena.getString("FECHA_R")));

				}

				PanelAlojamiento panelAlojamiento = new PanelAlojamiento(getAlojamiento(Hotel.class, rsRvaH.getInt("ID_H"), true), rsRvaH.getInt("N_PER"), LocalDate.parse(rsRvaH.getString("F_INI_RVA")), LocalDate.parse(rsRvaH.getString("F_FIN_RVA")), rsRvaH.getDouble("P_RVA_H"), rsRvaH.getInt("ID_RVA_H"), resena, PanelAlojamiento.MODO_CANCELAR_O_DEJARRESENA);

				reservasAlojamientos.add(panelAlojamiento);

			}

		} catch (SQLException e) {

			System.err.println("Error al cargar las reservas de alojamientos");
			e.printStackTrace();

		}

		return reservasAlojamientos;

	}

	// FIN Función para obtener las reservas del cliente con la sesión iniciada
	////
	// Función para crear una nueva reserva de un alojamiento
		
	public static boolean crearReservaAlojamiento(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, int nPersonas) {

		boolean reservaCreadaCorrectamente = false;

		String sqlSelect = "";
		String sqlInsert = "";
		String sqlComprobacionHoteles = """
										SELECT COUNT(*) AS N_RVAS
										FROM RESERVA_H
										WHERE F_INI_RVA <= ? AND
										F_FIN_RVA > ? AND
										ID_H = ? AND
										EMAIL_CLI = ?
										""";
			
		if (alojamiento instanceof Apartamento) {

			sqlSelect = """
						SELECT COUNT(*) AS N_RVAS
						FROM RESERVA_AP
						WHERE F_INI_RVA <= ? AND
							  F_FIN_RVA > ? AND
							  ID_AP = ?;
						""";

			sqlInsert = """
						INSERT INTO
						RESERVA_AP (F_INI_RVA, F_FIN_RVA, N_PER, P_RVA_AP, EMAIL_CLI, ID_AP)
						VALUES (?, ?, ?, ?, ?, ?);
						""";

		} else if (alojamiento instanceof Hotel) {

			sqlSelect = """
						SELECT COALESCE(SUM(CEIL((N_PER * 1.0) / (SELECT CAP_MAX_HAB FROM HOTEL H WHERE H.ID_H = RVA_H.ID_H))), 0) AS N_HABS_RVDAS
						FROM RESERVA_H RVA_H
						WHERE F_INI_RVA <= ? AND
							  F_FIN_RVA > ? AND
							  ID_H = ?;
						""";
				
			sqlInsert = """
						INSERT INTO
						RESERVA_H (F_INI_RVA, F_FIN_RVA, N_PER, P_RVA_H, EMAIL_CLI, ID_H)
						VALUES (?, ?, ?, ?, ?, ?);
						""";

		}

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmtSelect = con.prepareStatement(sqlSelect);
			 PreparedStatement pstmtInsert = con.prepareStatement(sqlInsert);
			 PreparedStatement pstmtComp = con.prepareStatement(sqlComprobacionHoteles)) {

			pstmtSelect.setString(1, fechaFin.toString());
			pstmtSelect.setString(2, fechaInicio.toString());
			pstmtSelect.setInt(3, alojamiento.getId());

			ResultSet rs = pstmtSelect.executeQuery();

			int rowCount = 0;

			if (rs.next()) {

				if (alojamiento instanceof Hotel) {
					
					pstmtComp.setString(1, fechaFin.toString());
					pstmtComp.setString(2, fechaInicio.toString());
					pstmtComp.setInt(3, alojamiento.getId());
					pstmtComp.setString(4, PanelVolverRegistrarseIniciarSesion.getCliente().getCorreo());
					
					ResultSet rsComp = pstmtComp.executeQuery();
					
					if (rsComp.next()) {
						
						if (rsComp.getInt("N_RVAS") > 0) {
							
							PanelPestanasBusqueda.setError("Ya tienes una reserva para este hotel en esas fechas");
							return reservaCreadaCorrectamente;
							
						}
						
					}
					
				}
					
			if ((alojamiento instanceof Apartamento && rs.getInt("N_RVAS") == 0) || (alojamiento instanceof Hotel && (rs.getInt("N_HABS_RVDAS") + ((Hotel) alojamiento).nHabitacionesOcupadas(nPersonas)) <= ((Hotel) alojamiento).getNumHabs())) {
						
					int nNoches = (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin);

					pstmtInsert.setString(1, fechaInicio.toString());
					pstmtInsert.setString(2, fechaFin.toString());
					pstmtInsert.setInt(3, nPersonas);
					pstmtInsert.setDouble(4, alojamiento.calcularPrecio(nPersonas, nNoches));
					pstmtInsert.setString(5, PanelVolverRegistrarseIniciarSesion.getCliente().getCorreo());
					pstmtInsert.setInt(6, alojamiento.getId());

					rowCount = pstmtInsert.executeUpdate();

				}

				if (rowCount > 0) {

					reservaCreadaCorrectamente = true;

				}

			}

		} catch (SQLException e) {

			System.err.println("Error al reservar el hotel");
			e.printStackTrace();

		}

		return reservaCreadaCorrectamente;

	}

	// FIN Función para crear una nueva reserva de un alojamiento
	////
	// Función para cancelar una reserva de alojamiento
		
	public static boolean cancelarReservaAlojamiento(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, int nPersonas) {

		boolean reservaCanceladaCorrectamente = false;

		String sqlDelete = "";

		if (alojamiento instanceof Apartamento) {

			sqlDelete = """
						DELETE FROM RESERVA_AP
						WHERE EMAIL_CLI = ? AND
							  ID_AP = ? AND
							  F_INI_RVA = ? AND
							  F_FIN_RVA = ? AND
							  N_PER = ?;
						""";

		} else if (alojamiento instanceof Hotel) {

			sqlDelete = """
						DELETE FROM RESERVA_H
						WHERE EMAIL_CLI = ? AND
							  ID_H = ? AND
							  F_INI_RVA = ? AND
							  F_FIN_RVA = ? AND
							  N_PER = ?;
						""";

		}

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmt = con.prepareStatement(sqlDelete)) {

			pstmt.setString(1, PanelVolverRegistrarseIniciarSesion.getCliente().getCorreo().trim());
			pstmt.setInt(2, alojamiento.getId());
			pstmt.setString(3, fechaInicio.toString());
			pstmt.setString(4, fechaFin.toString());
			pstmt.setInt(5, nPersonas);

			int rowCount = pstmt.executeUpdate();

			if (rowCount > 0) {

				reservaCanceladaCorrectamente = true;

			}

		} catch (SQLException e) {

			System.err.println("Error al cancelar la reserva");
			e.printStackTrace();
				
		}

		return reservaCanceladaCorrectamente;

	}

	// FIN Función para cancelar una reserva de alojamiento
	////
	// Función que devuelve todos los apartamentos del cliente con la sesión iniciada y sus respectivas ganancias
		
	public static Map<Apartamento, Double> getApartamentos() {

		Cliente cliente = PanelVolverRegistrarseIniciarSesion.getCliente();

		Map<Apartamento, Double> dineroGenPorApartamento = new HashMap<Apartamento, Double>();

		String sqlSelectApartamentos = """
									   SELECT *
									   FROM APARTAMENTO
									   WHERE EMAIL_CLI = ?;
									   """;

		String sqlSelectReservas = """
								   SELECT P_RVA_AP
								   FROM RESERVA_AP
								   WHERE ID_AP = ?;
								   """;

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmtSelectAp = con.prepareStatement(sqlSelectApartamentos);
			 PreparedStatement pstmtSelectRvas = con.prepareStatement(sqlSelectReservas)) {

			pstmtSelectAp.setString(1, cliente.getCorreo().trim());

			ResultSet rsAP = pstmtSelectAp.executeQuery();

			while (rsAP.next()) {

				// Creacion del apartamento

				int idA = rsAP.getInt("ID_AP");
				String nombreA = rsAP.getString("NOM_AP");
				String dirA = rsAP.getString("DIR_AP");
				Ciudad ciudadA = (Ciudad) getDestino(rsAP.getInt("ID_D"));
				String descripcionA = rsAP.getString("DESC_AP");
				List<Resena> resenasA = getResenasAlojamiento(Apartamento.class, idA);
				List<BufferedImage> imagenesA = getImagenesAlojamiento(Apartamento.class, idA, Integer.MAX_VALUE);

				double precioNPA = rsAP.getDouble("PRECIO_NP_AP");
				int capMaxA = rsAP.getInt("CAP_MAX_AP");
				String correoPropA = rsAP.getString("EMAIL_CLI");

				Apartamento apartamento = new Apartamento(idA, nombreA, dirA, ciudadA, descripcionA, resenasA, imagenesA, precioNPA, capMaxA, correoPropA);

				// FIN Creacion del apartamento

				// Cálculo de ganancias

				double gananciasTotales = 0;

				pstmtSelectRvas.setInt(1, idA);

				ResultSet rsRVAs = pstmtSelectRvas.executeQuery();

				while (rsRVAs.next()) {

					gananciasTotales += rsRVAs.getDouble("P_RVA_AP");

				}

				dineroGenPorApartamento.put(apartamento, gananciasTotales);

			}

		} catch (SQLException e) {

			System.err.println("Error al cargar los apartamentos");
			e.printStackTrace();

		}

		return dineroGenPorApartamento;

	}

	// FIN Función que devuelve todos los apartamentos del cliente con la sesión iniciada y sus respectivas ganancias
	////
	// Función para registrar un nuevo apartamento con una serie de carácterísticas que recibe la función
	
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

				// Si el apartamento se ha creado correctamente recuperamos el ID_AP y creamos las imágenes
				
				try (ResultSet rsKey = pstmtInsertAp.getGeneratedKeys()) {

					if (rsKey.next()) {

						for (BufferedImage imagen : imagenesSeleccionadas) {

							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ImageIO.write(imagen, "jpg", baos);
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

	// FIN Función para registrar un nuevo apartamento con una serie de carácterísticas que recibe la función
	////
	// Función para modificar apartamentos
	
	public static boolean modificarApartamento(int id, String nombre, String direccion, String descripcion, double precioNP, int capMax, List<BufferedImage> imagenes, Ciudad ciudad) {

		boolean apartamentoModificadoCorrectamente = false;

		String sqlUpdate = """
						   UPDATE APARTAMENTO
						   SET NOM_AP = ?, DIR_AP = ?, DESC_AP = ?, PRECIO_NP_AP = ?, CAP_MAX_AP = ?, EMAIL_CLI = ?, ID_D = ?
						   WHERE ID_AP = ?;
						   """;

		String sqlDelete = """
						   DELETE FROM
						   IMAGEN_APARTAMENTO
						   WHERE ID_AP = ?;
						   """;

		String sqlInsert = """
						   INSERT INTO
						   IMAGEN_APARTAMENTO (IMAGEN_AP, ID_AP)
						   VALUES (?, ?);
						   """;

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmtUpdate = con.prepareStatement(sqlUpdate);
			 PreparedStatement pstmtDelete = con.prepareStatement(sqlDelete);
			 PreparedStatement pstmtInsert = con.prepareStatement(sqlInsert)) {

			pstmtUpdate.setString(1, nombre.trim());
			pstmtUpdate.setString(2, direccion.trim());
			pstmtUpdate.setString(3, descripcion.trim());
			pstmtUpdate.setDouble(4, precioNP);
			pstmtUpdate.setInt(5, capMax);
			pstmtUpdate.setString(6, PanelVolverRegistrarseIniciarSesion.getCliente().getCorreo().trim());
			pstmtUpdate.setInt(7, ciudad.getId());
			pstmtUpdate.setInt(8, id);

			int rowCount1 = pstmtUpdate.executeUpdate();

			if (rowCount1 > 0) {

				pstmtDelete.setInt(1, id);

				pstmtDelete.executeUpdate();

				try {

					for (BufferedImage imagen : imagenes) {

						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write(imagen, "jpg", baos);
						byte[] imagenBytes = baos.toByteArray();

						pstmtInsert.setBytes(1, imagenBytes);
						pstmtInsert.setInt(2, id);

						pstmtInsert.executeUpdate();

					}

				} catch (Exception e) {

					System.err.println("Error al actualizar las imágenes");

				}

				apartamentoModificadoCorrectamente = true;

			}

		} catch (SQLException e) {

			System.err.println("Error al registrar el nuevo apartamento");
			e.printStackTrace();

		}

		return apartamentoModificadoCorrectamente;

	}

	// FIN Función para modificar apartamentos
	////
	// Función para conseguir las fechas de las reservas de un apartamento 
	
	public static Map<LocalDate, LocalDate> getFechasReservasApartamento(Apartamento apartamento) {

		Map<LocalDate, LocalDate> fechasReservas = new HashMap<LocalDate, LocalDate>();

		String sql = """
					 SELECT F_INI_RVA, F_FIN_RVA
					 FROM RESERVA_AP
					 WHERE ID_AP = ?;
					 """;

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmt = con.prepareStatement(sql)) {

			pstmt.setInt(1, apartamento.getId());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				LocalDate fechaEntrada = LocalDate.parse(rs.getString("F_INI_RVA"));
				LocalDate fechaSalida = LocalDate.parse(rs.getString("F_FIN_RVA"));

				fechasReservas.put(fechaEntrada, fechaSalida);

			}

		} catch (SQLException e) {

			System.err.println("Error al cargar las fechas de las reservas del apartamento " + apartamento.getNombre());
			e.printStackTrace();

		}

		return fechasReservas;

	}

	// FIN Función para conseguir las fechas de las reservas de un apartamento
	
}
