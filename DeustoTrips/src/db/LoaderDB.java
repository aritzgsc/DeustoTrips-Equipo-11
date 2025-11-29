package db;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import com.google.gson.Gson;

import domain.Aeropuerto;
import domain.Ciudad;
import domain.Hotel;
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
		////
		// Carga de HOTELES

//		cargarHotelesEnDB();

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

					Pais pais = new Pais(100000 + nLinea, nombreEnEspañol, Double.parseDouble(campos[1]),
							Double.parseDouble(campos[2]));

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

							Ciudad ciudad = new Ciudad(200000 + nLinea, paisCiudad, campos[1],
									Double.parseDouble(campos[4]), Double.parseDouble(campos[5]));

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

								// Calculamos como de parecidos son los nombres de las ciudades (del aeropuerto
								// y la que nos toca)

								int distanciaTexto = Utilidades.distanciaLevenshtein(ciudad.getNombre(), campos[10]);

								boolean seContienen = Utilidades.normalizar(ciudad.getNombre())
										.contains(Utilidades.normalizar(campos[10]))
										|| Utilidades.normalizar(campos[10])
												.contains(Utilidades.normalizar(ciudad.getNombre()));

								if (distanciaTexto < 5 || (seContienen && distanciaTexto < 7)) {

									// Si el nombre solo se parece, verificamos que la latitud no varíe más de 1
									// grado (~110km)

									double diffLat = Math.abs(ciudad.getLatitud() - Double.parseDouble(campos[4]));
									double diffLon = Math.abs(ciudad.getLongitud() - Double.parseDouble(campos[5]));

									if (diffLat < 1 && diffLon < 1) {

										// Si el nombre es idéntico nos lo quedamos y fin

										if (distanciaTexto == 0) {

											ciudadAeropuerto = ciudad;
											break;

										}

										// Si solo se parece y es mejor candidato que el anterior, nos lo quedamos pero
										// seguimos

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

	// FIN Cargar aeropuertos
	////
	// Cargar hoteles

	// Creamos una clase auxiliar con el formato del JSON que vamos a leer

	private static class IndiceGiata {

		// Se debe llamar "urls" porque así viene en el JSON

		List<String> urls;

	}

	// Cargamos las URLs de los hoteles desde un JSON índice a través de una URL

	public static List<String> getURLsHoteles() {

		List<String> URLsHoteles = new ArrayList<String>();

		String url = "https://giatadrive.com/hotel-directory/json";

		try (Reader reader = new InputStreamReader(URI.create(url).toURL().openStream(), "UTF-8")) {

			// Usamos la librería Gson para leer el JSON fácilmente

			Gson gson = new Gson();

			// Indicamos el tipo de lista que queremos que nos cree a partir de las URLs

			IndiceGiata respuesta = gson.fromJson(reader, IndiceGiata.class);

			// Si no hay errores devolvemos la lista que nos genera gson leyendo el json al
			// que nos hemos conectado por medio del reader

			if (respuesta != null) {

				URLsHoteles = respuesta.urls;

			}

		} catch (Exception e) {

			System.err.println("Error al leer el JSON: " + e.getMessage());

		}

		return URLsHoteles;

	}

	// Cargamos los Hoteles en la BD a partir de los JSON que recibimos de la
	// función de arriba para ello vamos a utilizar una función auxiliar que procesa
	// los hoteles por nosotros

	public static void cargarHotelesEnDB() {

		List<String> URLsHoteles = getURLsHoteles();

		String sqlInsertHotel = """
				INSERT INTO HOTEL (NOM_H, DIR_H, DESC_H, NUM_HABS, CAP_MAX_HAB, PRECIO_NHAB_H, ID_D)
				VALUES (?, ?, ?, ?, ?, ?, ?);
				""";

		String sqlInsertImagen = """
				INSERT INTO IMAGEN_HOTEL (IMAGEN_H, ID_H)
				VALUES (?, ?);
				""";

		int contadorSeguridad = 0;

		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
				PreparedStatement pstmtInsertHotel = con.prepareStatement(sqlInsertHotel,
						Statement.RETURN_GENERATED_KEYS);
				PreparedStatement pstmtInsertImagen = con.prepareStatement(sqlInsertImagen)) {

			for (String urlHotel : URLsHoteles) {

				Hotel hotel = procesarHotel(urlHotel);

				if (hotel != null) {

					cargarHotelEnDB(con, pstmtInsertHotel, pstmtInsertImagen, hotel);
					contadorSeguridad++;

					if (contadorSeguridad % 25 == 0) {

						System.out.println("Guardados " + contadorSeguridad + " hoteles");

					}

				}

			}

		} catch (Exception e) {

			System.err.println("Error al cargar el hotel en DB");
			e.printStackTrace();

		}

	}

	public static void cargarHotelEnDB(Connection con, PreparedStatement pstmtInsertHotel,
			PreparedStatement pstmtInsertImagen, Hotel hotel) {

		try {

			pstmtInsertHotel.setString(1, hotel.getNombre());
			pstmtInsertHotel.setString(2, hotel.getDireccion());
			pstmtInsertHotel.setString(3, hotel.getDescripcion());
			pstmtInsertHotel.setInt(4, hotel.getNumHabs());
			pstmtInsertHotel.setInt(5, hotel.getCapMaxHab());
			pstmtInsertHotel.setDouble(6, hotel.getPrecioNHab());
			pstmtInsertHotel.setInt(7, hotel.getCiudad().getId());

			int rowCount = pstmtInsertHotel.executeUpdate();

			if (rowCount > 0) {

				try (ResultSet rsHotelId = pstmtInsertHotel.getGeneratedKeys()) {

					if (rsHotelId.next()) {

						for (BufferedImage imagen : hotel.getImagenes()) {

							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							ImageIO.write(imagen, "jpg", baos);
							byte[] imagenBytes = baos.toByteArray();

							pstmtInsertImagen.setBytes(1, imagenBytes);
							pstmtInsertImagen.setInt(2, rsHotelId.getInt(1));

							pstmtInsertImagen.addBatch();

						}

						pstmtInsertImagen.executeBatch();

					}

				}

			}

		} catch (Exception e) {

			System.err.println("Error al cargar el hotel en la BD");
//			e.printStackTrace();

		}

	}

	// Creamos la clase madre (cada una de estas representará un hotel completo (de
	// información que nos interesa) contenido en cada JSON)

	private static class HotelGiata {

		List<NombreHotelGiata> names;
		PaisHotelGiata country;
		List<UbicacionHotelGiata> addresses;
		List<ImagenPorTamanoHotelGiata> images;
		Map<String, TextoHotelGiata> texts;

	}

	// Creamos las subclases de la clase madre con los nombres tal y como vienen en
	// el JSON

	private static class NombreHotelGiata {
		String value;
		String locale;
	}

	private static class PaisHotelGiata {
		String code;
	}

	private static class UbicacionHotelGiata {
		String street;
		String cityName;
		String poBox;
		String zip;
	}

	private static class ImagenPorTamanoHotelGiata {
		Map<String, ImagenHotelGiata> sizes;
	}

	private static class ImagenHotelGiata {
		String href;
	}

	private static class TextoHotelGiata {
		List<SeccionHotelGiata> sections;
	}

	private static class SeccionHotelGiata {
		String para;
	}

	// Función para procesar el Hotel (Hecha con ayuda de Gemini para la parte de
	// Gson y la descripción)

	public static Hotel procesarHotel(String urlHotelJSON) {

		try (Reader reader = new InputStreamReader(URI.create(urlHotelJSON).toURL().openStream(), "UTF-8")) {

			// Usamos la librería Gson para leer el JSON fácilmente

			Gson gson = new Gson();

			// Creamos la instancia del HotelGiata (sin procesar)

			HotelGiata hotelGiata = gson.fromJson(reader, HotelGiata.class);

			// A partir de aquí empezamos a procesar los datos que hemos recibido del JSON y
			// los convertimos al formato que nos interesa para nuestro Hotel

			// Nombre

			String nombre = "";

			if (hotelGiata.names != null) {

				for (NombreHotelGiata name : hotelGiata.names) {

					if (name.locale.equals("de")) {

						nombre = name.value;

					}

				}

			} else {
				System.err.println("Nombre nulo");
				return null;
			}

			// Dirección

			String direccion = "";

			if (hotelGiata.addresses.get(0).zip != null || hotelGiata.addresses.get(0).poBox != null
					|| hotelGiata.addresses.get(0).street != null) {

				if (hotelGiata.addresses.get(0).zip != null) {

					direccion = hotelGiata.addresses.get(0).zip;

				}

				if (hotelGiata.addresses.get(0).poBox != null) {

					if (direccion.equals("")) {

						direccion = hotelGiata.addresses.get(0).poBox;

					} else {

						direccion = hotelGiata.addresses.get(0).poBox + ", " + direccion;

					}

				}

				if (hotelGiata.addresses.get(0).street != null) {

					if (direccion.equals("")) {

						direccion = hotelGiata.addresses.get(0).street;

					} else {

						direccion = hotelGiata.addresses.get(0).street + ", " + direccion;

					}

				}

			} else {
				System.err.println("Dirección nula");
				return null;
			}

			// Ciudad

			List<Ciudad> ciudadesPosibles = null;

			if (hotelGiata.country != null) {

				ciudadesPosibles = GestorDB.getCiudades(hotelGiata.country.code);

			} else {
				return null;
			}

			Ciudad ciudad = null;

			if (hotelGiata.addresses.get(0).cityName != null) {

				int minimaDistanciaTexto = hotelGiata.addresses.get(0).cityName.length();

				for (Ciudad ciudadPosible : ciudadesPosibles) {

					int distanciaTexto = Utilidades.distanciaLevenshtein(ciudadPosible.getNombre(),
							hotelGiata.addresses.get(0).cityName);

					boolean seContienen = Utilidades.normalizar(ciudadPosible.getNombre())
							.contains(Utilidades.normalizar(hotelGiata.addresses.get(0).cityName))
							|| Utilidades.normalizar(hotelGiata.addresses.get(0).cityName)
									.contains(Utilidades.normalizar(ciudadPosible.getNombre()));

					if ((distanciaTexto < 5 || (seContienen && distanciaTexto < 7))
							&& distanciaTexto < minimaDistanciaTexto) {

						ciudad = ciudadPosible;
						minimaDistanciaTexto = distanciaTexto;

						if (distanciaTexto == 0) {
							break;
						}

					}

				}

			} else {
				System.err.println("No hay ciudades posibles");
				return null;
			}

			if (ciudad == null) {
				System.err.println("Ciudad nula");
				return null;
			}

			// Descripción (Hecho con Gemini)

			String textoCompleto = "";
			if (hotelGiata.texts != null && hotelGiata.texts.containsKey("es")) {

				TextoHotelGiata textoEs = hotelGiata.texts.get("es");

				if (textoEs != null && textoEs.sections != null) {

					StringBuilder sb = new StringBuilder();

					for (SeccionHotelGiata sec : textoEs.sections) {

						if (sec.para != null)
							sb.append(sec.para).append(" ");

					}

					textoCompleto = sb.toString();

				}

			}

			String descripcion = generarResumenGemini(textoCompleto);

			if (descripcion.isBlank()) {
				System.err.println("Descripción nula");
				return null;
			}

			// Imágenes

			List<BufferedImage> imagenes = new ArrayList<BufferedImage>();

			if (hotelGiata.images != null && !hotelGiata.images.isEmpty()) {

				for (ImagenPorTamanoHotelGiata mapa : hotelGiata.images) {

					String url = mapa.sizes.get("800").href;

					try (InputStream inputStream = URI.create(url).toURL().openStream()) {

						byte[] imagenBytes = inputStream.readAllBytes();
						ByteArrayInputStream bais = new ByteArrayInputStream(imagenBytes);

						BufferedImage imagen = ImageIO.read(bais);

						imagenes.add(imagen);

						if (imagenes.size() >= 15) {
							break;
						}

					}

				}

			} else {
				System.err.println("El hotel no tiene imágenes");
				return null;
			}

			// Nº de habitaciones

			int habitaciones = 10 + (int) (Math.random() * 41);

			// Máximo nº de personas/habitación

			int maxPerHab = 2 + (int) (Math.random() * 6);

			// Precio / habitación y noche

			double precio = 30 + (Math.random() * 270);
			precio = Math.round(precio * 100.0) / 100.0;

			// Creamos el hotel

			if (nombre != null && direccion != null && ciudad != null && descripcion != null && imagenes != null
					&& !imagenes.isEmpty()) {

				System.out.println("Hotel " + nombre + " creado correctamente (" + ciudad.toString() + ")");
				return new Hotel(-1, nombre, direccion, ciudad, descripcion, null, imagenes, habitaciones, maxPerHab,
						precio);

			} else {
				return null;
			}

		} catch (Exception e) {

			System.err.println("Error al procesar el hotel: " + e.getMessage());
			return null;

		}

	}

	// A partir de aqui mucho hecho con GEMINI
	
	// Configuración de Gemini
	
    private static final String GEMINI_API_KEY = "AIzaSyAU6MIieGck3vrgpPIMZ7GfUsizWfxG48g"; 
    
    // Modelo rápido y eficiente.
    
    private static final String GEMINI_MODEL = "gemini-2.0-flash"; 

	static class GeminiRequest { List<GeminiContent> contents = new ArrayList<>(); }
    static class GeminiContent { List<GeminiPart> parts = new ArrayList<>(); }
    static class GeminiPart { String text; }
    static class GeminiResponse { List<GeminiCandidate> candidates; }
    static class GeminiCandidate { GeminiContent content; }

    private static String generarResumenGemini(String textoCompleto) {
        if (textoCompleto == null || textoCompleto.trim().isEmpty()) return "Sin descripción.";

        // Seguridad básica
        
        if (GEMINI_API_KEY.length() < 10) {
             return resumenSimple(textoCompleto);
        }

        try {
            return llamarGemini(textoCompleto);
        } catch (Exception e) {
            // Si falla algo (ej. 500 server error), resumen simple
        	
            System.err.println("   [Fallo IA] " + e.getMessage());
            return resumenSimple(textoCompleto);
        }
    }

    private static String llamarGemini(String textoOriginal) throws Exception {
        
        String urlString = "https://generativelanguage.googleapis.com/v1beta/models/" + GEMINI_MODEL + ":generateContent?key=" + GEMINI_API_KEY.trim();
        
        HttpURLConnection con = (HttpURLConnection) URI.create(urlString).toURL().openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        String textoLimpio = textoOriginal.replace("\"", "'").replace("\n", " ");
        if (textoLimpio.length() > 5000) textoLimpio = textoLimpio.substring(0, 5000); 

        // Prompt de Copywriter
        
        String prompt = "Actúa como un experto copywriter de viajes. Escribe un resumen comercial y atractivo de este hotel en ESPAÑOL. " +
                        "Máximo 60 palabras. Destaca SOLO: ubicación, instalaciones principales y ambiente. " +
                        "IMPORTANTE: No uses frases introductorias (ej: 'Aquí tienes'). Ve directo al grano. " +
                        "Texto original: " + textoLimpio;

        // Petición
        
        GeminiRequest req = new GeminiRequest();
        GeminiContent content = new GeminiContent();
        GeminiPart part = new GeminiPart();
        part.text = prompt;
        content.parts.add(part);
        req.contents.add(content);

        Gson gson = new Gson();
        String jsonBody = gson.toJson(req);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int status = con.getResponseCode();
        if (status != 200) {
            throw new Exception("HTTP " + status);
        }

        // Respuesta
        
        try (Reader reader = new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8)) {
            
        	GeminiResponse response = gson.fromJson(reader, GeminiResponse.class);
            
        	if (response != null && !response.candidates.isEmpty()) {
                
        		GeminiContent candContent = response.candidates.get(0).content;
                
        		if (candContent != null && !candContent.parts.isEmpty()) {
                    
        			String resultado = candContent.parts.get(0).text.trim();
                    
        			if (resultado.startsWith("\"") && resultado.endsWith("\"")) {
        				resultado = resultado.substring(1, resultado.length() - 1);
        			}
        			
        			return resultado;
                }
            }
        }
        
        return resumenSimple(textoOriginal); 
    }

	private static String resumenSimple(String texto) {
		String[] palabras = texto.split("\\s+");
		int limite = 60;
		if (palabras.length <= limite) {

			return texto;

		}

		StringBuilder r = new StringBuilder();

		for (int i = 0; i < limite; i++) {

			r.append(palabras[i]).append(" ");

		}

		return r.append("...").toString().trim();
	}

}
