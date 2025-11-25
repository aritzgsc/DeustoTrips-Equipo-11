package db;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import domain.Aeropuerto;
import domain.Ciudad;
import domain.Destino;
import domain.Pais;

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
		return false;
	}

	public static boolean registrarUsuario(String nombre, String apellidos, String correo, String contrasena) {
		return false;
	}

	public static boolean iniciarSesion(String correoElectronico, String contrasena) {
		return true;
	}

}
