package db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import domain.Aeropuerto;
import domain.Ciudad;
import domain.Pais;
import main.util.Utilidades;

// NO EJECUTAR VARIAS VECES (TENER CUIDADO CON COMO SE EJECUTA)
// Clase para cargar datos dentro de la BD (estructura de la BD creada con SQLite)

public class LoaderDB {

	private static final String SQLITE_FILE = "resources/db/DBDeustoTrips.db";
	private static final String CONNECTION_STRING = "jdbc:sqlite:" + SQLITE_FILE;

	private static final int ID_TD_PAIS = 1;
	private static final int ID_TD_CIUDAD = 2;
	private static final int ID_TD_AEROPUERTO = 3;

	public LoaderDB() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.err.format("* Error al cargar el driver de la BBDD: %s\n", e.getMessage());
		}
	}

	public static void main(String[] args) {

		// Carga de DESTINOS

		// Primero cargamos las banderas

//		cargarBanderasEnDB();

		// Luego cargamos todos los países

//		cargarPaisesEnDB();

		// Luego las ciudades

//		cargarCiudadesEnDB();

		// Por último los aeropuertos

//		cargarAeropuertosEnDB();

		// FIN Carga de DESTINOS

	}

	// Cargar banderas en DB (cargarBanderasEnDB)

	public static Set<String> cargarCodigosISOCSV() {

		Set<String> codigosISO = new TreeSet<>();

		int nLinea = 0;

		try {

			File fichero = new File("resources/db/countries.csv");
			Scanner sc = new Scanner(fichero);

			while (sc.hasNextLine()) {

				nLinea++;

				if (nLinea == 1) {
					sc.nextLine();
					continue; // Primera línea es para determinar el tipo de datos
				}

				String linea = sc.nextLine();
				String[] campos = linea.split(",");

				if (!campos[1].isEmpty() && !campos[2].isEmpty()
						&& !campos[3].replaceAll("\"|(\\[.*\\])", "").isEmpty()) {

					codigosISO.add(campos[0]);

				}
			}

			sc.close();

		} catch (Exception e) {

			System.err.println("Error al cargar los códigos de los países desde CSV: (linea " + nLinea + ")");
			e.printStackTrace();

		}

		return codigosISO;

	}

	public static void cargarBanderasEnDB() {

		Set<String> codigosISO = cargarCodigosISOCSV();

		for (String string : codigosISO) {

			insertarBanderaEnDB(string, "https://flagcdn.com/h40/" + string.toLowerCase() + ".png");

		}

	}

	private static void insertarBanderaEnDB(String iso, String urlString) {

		String sql = "INSERT INTO IMAGEN_DESTINO (ISO_CODE, BANDERA) VALUES(?, ?)";

		try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
				PreparedStatement pstmt = conn.prepareStatement(sql);
				InputStream inputStream = URI.create(urlString).toURL().openStream();) {

			// Convertimos la imagen de la web a bytes en memoria

			byte[] imageBytes = inputStream.readAllBytes();

			// Asignamos los valores

			pstmt.setString(1, iso);

			// Guardamos los bytes en el campo BLOB

			pstmt.setBytes(2, imageBytes);

			int rowCount = pstmt.executeUpdate();

			if (rowCount > 0) {
				System.out.println("¡Bandera de " + iso + " guardada con éxito!");
			}

		} catch (Exception e) {
			System.out.println("Error al guardar la imagen: " + e.getMessage());
			e.printStackTrace();
		}

	}

	// FIN Cargar Banderas en DB
	////
	// Cargar países en DB (cargarPaisesEnDB)

	public static Map<String, Pais> cargarPaisesCSV() {

		Map<String, Pais> paisesPorISO = new TreeMap<String, Pais>();

		int nLinea = 0;

		// Definimos el idioma de la aplicación (Español de España)

		Locale idiomaEspañol = Locale.of("es", "ES");

		try {

			File fichero = new File("resources/db/countries.csv");
			Scanner sc = new Scanner(fichero);

			while (sc.hasNextLine()) {

				nLinea++;

				if (nLinea == 1) {
					sc.nextLine();
					continue; // Primera línea es para determinar el tipo de datos
				}

				String linea = sc.nextLine();
				String[] campos = linea.split(",");

				if (campos.length > 3 && !campos[1].isEmpty() && !campos[2].isEmpty()) {

					String isoCode = campos[0];

					Locale localePais = Locale.of("", isoCode);

					// Le pedimos el nombre de ese país para mostrarlo en Español

					String nombreEnEspañol = localePais.getDisplayCountry(idiomaEspañol);

					// Si por alguna razón Java no tiene traducción, usamos la del CSV en inglés
					// como respaldo

					if (nombreEnEspañol.isEmpty() || nombreEnEspañol.equals(isoCode)) {

						nombreEnEspañol = campos[3].replaceAll("\"|(\\[.*\\])", "");

					}

					// Usamos nombreEnEspañol en lugar de campos[3] para guardar los nombres de los
					// países en español

					Pais pais = new Pais(100000 + nLinea, nombreEnEspañol, Double.parseDouble(campos[1]), Double.parseDouble(campos[2]));

					paisesPorISO.put(isoCode, pais);

				}
			}

			sc.close();

		} catch (Exception e) {

			System.err.println("Error al cargar los países desde CSV: (linea " + nLinea + ")");
			e.printStackTrace();

		}

		return paisesPorISO;

	}

	public static void cargarPaisesEnDB() {

		Map<String, Pais> paisesPorISO = cargarPaisesCSV();

		String sql = "INSERT INTO DESTINO (ID_D, NOM_D, LAT_D, LON_D, ID_TD, ISO_CODE) VALUES (?, ?, ?, ?, ?, ?);";

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
				PreparedStatement pstmt = con.prepareStatement(sql)) {

			for (String iso : paisesPorISO.keySet()) {

				Pais pais = paisesPorISO.get(iso);

				pstmt.setInt(1, pais.getId());
				pstmt.setString(2, pais.getNombre());
				pstmt.setDouble(3, pais.getLatitud());
				pstmt.setDouble(4, pais.getLongitud());
				pstmt.setInt(5, ID_TD_PAIS);
				pstmt.setString(6, iso);

				int rowCount = pstmt.executeUpdate();
				if (rowCount > 0) {

					System.out.println("¡Pais " + iso + " (" + pais.getNombre() + ") guardado con éxito!");

				}

			}

		} catch (SQLException e) {

			System.err.println("Error al guardar el país: " + e.getMessage());
			e.printStackTrace();

		}

	}

	// FIN Cargar países en DB
	////
	// Cargar ciudades en DB (cargarCiudadesEnDB)

	public static Map<String, Set<Ciudad>> cargarCiudadesCSV() {

		Map<String, Pais> paisesPorISO = cargarPaisesCSV();

		Map<String, Set<Ciudad>> ciudadesPorISO = new TreeMap<String, Set<Ciudad>>();

		int nLinea = 0;

		File fichero = new File("resources/db/cities5000.txt");

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(fichero), StandardCharsets.UTF_8))) {

			String linea;
			while ((linea = br.readLine()) != null) {

				nLinea++;

				try {

					String[] campos = linea.split("\t");

					if (campos.length > 8) {

						Pais paisCiudad = paisesPorISO.get(campos[8]);

						if (paisCiudad != null) {

							Ciudad ciudad = new Ciudad(200000 + nLinea, paisCiudad, campos[1], Double.parseDouble(campos[4]), Double.parseDouble(campos[5]));

							if (ciudadesPorISO.get(campos[8]) == null) {

								ciudadesPorISO.put(campos[8], new TreeSet<Ciudad>());

							}

							ciudadesPorISO.get(campos[8]).add(ciudad);

						}

					}

				} catch (Exception e) {

					System.err.println("Error al cargar ciudades desde CSV: (linea " + nLinea + ")");
					e.printStackTrace();

				}

			}

		} catch (Exception e) {

			System.err.println("Error abriendo el archivo: " + e.getMessage());
			e.printStackTrace();

		}

		return ciudadesPorISO;

	}

	public static void cargarCiudadesEnDB() {

		Map<String, Set<Ciudad>> ciudadesPorISO = cargarCiudadesCSV();

		String sql = "INSERT INTO DESTINO (ID_D, NOM_D, LAT_D, LON_D, ID_TD, ISO_CODE, ID_D_PADRE) VALUES (?, ?, ?, ?, ?, ?, ?);";

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
				PreparedStatement pstmt = con.prepareStatement(sql)) {

			for (String iso : ciudadesPorISO.keySet()) {

				for (Ciudad ciudad : ciudadesPorISO.get(iso)) {

					pstmt.setInt(1, ciudad.getId());
					pstmt.setString(2, ciudad.getNombre());
					pstmt.setDouble(3, ciudad.getLatitud());
					pstmt.setDouble(4, ciudad.getLongitud());
					pstmt.setInt(5, ID_TD_CIUDAD);
					pstmt.setString(6, iso);
					pstmt.setInt(7, ciudad.getPais().getId());

					int rowCount = pstmt.executeUpdate();
					if (rowCount > 0) {

						System.out.println("¡Ciudad " + iso + " (" + ciudad.getNombre() + ") guardada con éxito!");

					}

				}

			}

		} catch (SQLException e) {

			System.err.println("Error al guardar el país: " + e.getMessage());
			e.printStackTrace();

		}

	}

	// FIN Cargar ciudades
	////
	// Cargar aeropuertos

	public static Map<String, Set<Aeropuerto>> cargarAeropuertosCSV() {

		Map<String, Set<Ciudad>> ciudadesPorISO = cargarCiudadesCSV();

		Map<String, Set<Aeropuerto>> aeropuertosPorISO = new TreeMap<String, Set<Aeropuerto>>();

		int nLinea = 0;

		File fichero = new File("resources/db/airports.csv");

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(fichero), StandardCharsets.UTF_8))) {

			String linea;

			while ((linea = br.readLine()) != null) {

				nLinea++;

				if (nLinea == 1) {
					continue;
				}

				try {

					// Solo corta por , si no está entre comillas

					String[] campos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

					for (int i = 0; i < campos.length; i++) {

						campos[i] = campos[i].replace("\"", "");

					}

					if (campos.length > 13 && campos[13].length() == 3) {

						Set<Ciudad> ciudadesPosibles = ciudadesPorISO.get(campos[8]);

						if (ciudadesPosibles != null) {

							Ciudad ciudadAeropuerto = null;
							int mejorDistancia = 100;

							for (Ciudad ciudad : ciudadesPosibles) {

								// Calculamos como de parecidos son los nombres de las ciudades (del aeropuerto y la que nos toca)

								int distanciaTexto = Utilidades.distanciaLevenshtein(ciudad.getNombre(), campos[10]);

								boolean seContienen = Utilidades.normalizar(ciudad.getNombre()).contains(Utilidades.normalizar(campos[10])) || Utilidades.normalizar(campos[10]).contains(Utilidades.normalizar(ciudad.getNombre()));

								if (distanciaTexto < 5 || (seContienen && distanciaTexto < 7)) {

									// Si el nombre solo se parece, verificamos que la latitud no varíe más de 1 grado (~110km)

									double diffLat = Math.abs(ciudad.getLatitud() - Double.parseDouble(campos[4]));
									double diffLon = Math.abs(ciudad.getLongitud() - Double.parseDouble(campos[5]));

									if (diffLat < 1 && diffLon < 1) {

										// Si el nombre es idéntico nos lo quedamos y fin
										
										if (distanciaTexto == 0) {

											ciudadAeropuerto = ciudad;
											break;

										}
										
										// Si solo se parece y es mejor candidato que el anterior, nos lo quedamos pero seguimos

										if (distanciaTexto < mejorDistancia) {

											mejorDistancia = distanciaTexto;
											ciudadAeropuerto = ciudad;

										}

									}

								}

							}

							if (ciudadAeropuerto != null) {

								Aeropuerto aeropuerto = new Aeropuerto(300000 + nLinea, ciudadAeropuerto,
										campos[3] + " [" + campos[13] + "]", Double.parseDouble(campos[4]),
										Double.parseDouble(campos[5]));

								if (aeropuertosPorISO.get(campos[8]) == null) {

									aeropuertosPorISO.put(campos[8], new TreeSet<Aeropuerto>());

								}

								aeropuertosPorISO.get(campos[8]).add(aeropuerto);

							}

						}

					}

				} catch (Exception e) {

					System.err.println("Error al cargar ciudades desde CSV: (linea " + nLinea + ")");
					e.printStackTrace();

				}

			}

		} catch (Exception e) {

			System.err.println("Error abriendo el archivo: " + e.getMessage());
			e.printStackTrace();

		}

		return aeropuertosPorISO;

	}

	public static void cargarAeropuertosEnDB() {

		Map<String, Set<Aeropuerto>> aeropuertosPorISO = cargarAeropuertosCSV();

		String sql = "INSERT INTO DESTINO (ID_D, NOM_D, LAT_D, LON_D, ID_TD, ISO_CODE, ID_D_PADRE) VALUES (?, ?, ?, ?, ?, ?, ?);";

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
				PreparedStatement pstmt = con.prepareStatement(sql)) {

			for (String iso : aeropuertosPorISO.keySet()) {

				for (Aeropuerto aeropuerto : aeropuertosPorISO.get(iso)) {
					
					pstmt.setInt(1, aeropuerto.getId());
					pstmt.setString(2, aeropuerto.getNombre());
					pstmt.setDouble(3, aeropuerto.getLatitud());
					pstmt.setDouble(4, aeropuerto.getLongitud());
					pstmt.setInt(5, ID_TD_AEROPUERTO);
					pstmt.setString(6, iso);
					pstmt.setInt(7, aeropuerto.getCiudad().getId());

					int rowCount = pstmt.executeUpdate();
					if (rowCount > 0) {

						System.out.println(
								"¡Aeropuerto " + iso + " (" + aeropuerto.getNombre() + ") guardado con éxito!");

					}

				}

			}

		} catch (SQLException e) {

			System.err.println("Error al guardar el aeropuerto: " + e.getMessage());
			e.printStackTrace();

		}

	}

}
