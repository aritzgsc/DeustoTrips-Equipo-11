package db;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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
import domain.Compania;
import domain.Destino;
import domain.Hotel;
import domain.Pais;
import domain.Viaje;
import domain.Viaje.DiaSemana;
import domain.Viaje.TipoViaje;
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

	public static Map<Integer, Destino> destinoPorIndice;
	
	public static Map<Integer, Compania> companiaPorIndice;
	
	public static Map<Integer, List<Aeropuerto>> aeropuertosPorIndiceCiudad;
	
	public static void main(String[] args) {
		
		destinoPorIndice = new HashMap<Integer, Destino>();
		
		List<Destino> destinos = GestorDB.cargarDestinos();
		destinos.add(GestorDB.getDestino(0));
		
		for (Destino destino : destinos) {
			if (!destinoPorIndice.containsKey(destino.getId())) destinoPorIndice.put(destino.getId(), destino);
		}
		
		companiaPorIndice = new HashMap<Integer, Compania>();
		
		List<Compania> companias = GestorDB.getCompanias();
		
		for (Compania compania : companias) {
			if (!companiaPorIndice.containsKey(compania.getId())) companiaPorIndice.put(compania.getId(), compania);
		}
		
		aeropuertosPorIndiceCiudad = new HashMap<Integer, List<Aeropuerto>>();
		
		for (Destino destino : destinos) {
			if (destino instanceof Aeropuerto) {
				if (!aeropuertosPorIndiceCiudad.containsKey(((Aeropuerto) destino).getCiudad().getId())) aeropuertosPorIndiceCiudad.put(((Aeropuerto) destino).getCiudad().getId(), new ArrayList<Aeropuerto>());
				aeropuertosPorIndiceCiudad.get(((Aeropuerto) destino).getCiudad().getId()).add((Aeropuerto) destino);
			}
		}
		
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

		// FIN Carga de HOTELES
		////
		// Carga de VIAJES (Itinerarios de viajes (suponemos que todas las semanas habrán los mismos viajes a las mismas horas))
		
		// Primero cargamos las compañías a partir del CSV que hemos creado con la otra clase
		
//		cargarCompaniasEnBD();
		
		// Luego cargamos todos los viajes
		
//		cargarViajesEnDB(false);												// Todas las ciudades
//		cargarViajesEnDB(true);													// Ciudades grandes
		
		// FIN Carga de VIAJES
		
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

	public static List<Ciudad> cargarCiudadesGrandesCSV() {

		Map<String, Pais> paisesPorISO = cargarPaisesCSV();

		List<Ciudad> ciudadesGrandes = new ArrayList<Ciudad>();

		int nLinea = 0;

		File fichero = new File("resources/db/cities15000.txt");

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(fichero), StandardCharsets.UTF_8))) {

			String linea;
			while ((linea = br.readLine()) != null) {

				nLinea++;

				try {

					String[] campos = linea.split("\t");

					if (campos.length > 14) {

						int poblacion = 0;
						
						if (!campos[14].isEmpty()) poblacion = Integer.parseInt(campos[14]);
						
						if (poblacion > 200000) {
						
							Pais paisCiudad = paisesPorISO.get(campos[8]);
	
							if (paisCiudad != null) {
	
								Ciudad ciudad = new Ciudad(200000 + nLinea, paisCiudad, campos[1], Double.parseDouble(campos[4]), Double.parseDouble(campos[5]));
	
								ciudadesGrandes.add(ciudad);
	
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

		return ciudadesGrandes;

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

								boolean seContienen = Utilidades.normalizar(ciudad.getNombre()).contains(Utilidades.normalizar(campos[10])) || Utilidades.normalizar(campos[10]).contains(Utilidades.normalizar(ciudad.getNombre()));

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

								Aeropuerto aeropuerto = new Aeropuerto(300000 + nLinea, ciudadAeropuerto, campos[3] + " [" + campos[13] + "]", Double.parseDouble(campos[4]), Double.parseDouble(campos[5]));

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

						System.out.println("¡Aeropuerto " + iso + " (" + aeropuerto.getNombre() + ") guardado con éxito!");

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

	// Cargamos los Hoteles en la BD a partir de los JSON que recibimos de la función de arriba para ello vamos a utilizar una función auxiliar que procesa los hoteles por nosotros

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

	// Función para cargar un único hotel en la BD
	
	public static void cargarHotelEnDB(Connection con, PreparedStatement pstmtInsertHotel, PreparedStatement pstmtInsertImagen, Hotel hotel) {

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

							pstmtInsertImagen.addBatch();		// Para que no se vayan metiendo una a una y se metan de golpe abajo

						}

						pstmtInsertImagen.executeBatch();		// Aquí se meten todas las imágenes en la BD de golpe

					}

				}

			}

		} catch (Exception e) {

			System.err.println("Error al cargar el hotel en la BD");
			e.printStackTrace();

		}

	}

	// Creamos la clase madre (cada una de estas representará un hotel completo (de información que nos interesa) contenido en cada JSON)

	private static class HotelGiata {

		List<NombreHotelGiata> names;
		PaisHotelGiata country;
		List<UbicacionHotelGiata> addresses;
		List<ImagenPorTamanoHotelGiata> images;
		Map<String, TextoHotelGiata> texts;

	}

	// Creamos las subclases de la clase madre con los nombres tal y como vienen en el JSON

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

	// Función para procesar el Hotel (Hecha con ayuda de Gemini para la parte de Gson y la descripción)

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

				ciudadesPosibles = GestorDB.getCiudadesPais(hotelGiata.country.code);

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

			// Creamos el hotel con los datos que hemos recibido / creado

			if (nombre != null && direccion != null && ciudad != null && descripcion != null && imagenes != null && !imagenes.isEmpty()) {

				System.out.println("Hotel " + nombre + " creado correctamente (" + ciudad.toString() + ")");
				return new Hotel(-1, nombre, direccion, ciudad, descripcion, null, imagenes, habitaciones, maxPerHab, precio);

			} else {
				return null;
			}

		} catch (Exception e) {

			System.err.println("Error al procesar el hotel: " + e.getMessage());
			return null;

		}

	}

	// A partir de aqui mucho hecho con GEMINI
	
	// Configuración de Gemini API
	
    private static final String GEMINI_API_KEY = "AIzaSyAU6MIieGck3vrgpPIMZ7GfUsizWfxG48g"; 
    
    // Modelo rápido y eficiente
    
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
        	
            System.err.println("[Fallo IA] " + e.getMessage());
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

        // Prompt para buenas respuestas
        
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

    // Resumen simple si falla Gemini
    
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

	// FIN Cargar hoteles
	////
	// Cargar viajes
	
	public static List<Compania> cargarCompaniasCSV() {

		List<Compania> companias = new ArrayList<Compania>();

		int nLinea = 0;

		try {

			File fichero = new File("resources/db/companias.csv");
			Scanner sc = new Scanner(fichero);

			while (sc.hasNextLine()) {

				nLinea++;

				if (nLinea == 1) {
					sc.nextLine();
					continue; // Primera línea es para determinar el tipo de datos
				}

				String linea = sc.nextLine();
				String[] campos = linea.split(",");

				BufferedImage logo = null;
				
				try {
				
					InputStream inputStream = URI.create(campos[4]).toURL().openStream();				
					logo = ImageIO.read(inputStream);
				
				} catch (IOException e) {
					
					System.err.println("Error al cargar la imagen (linea " + nLinea + ") ; Descargando logo sencillo");
					
					String safeName = campos[1].replace(" ", "+");
					
					InputStream inputStream = URI.create("https://ui-avatars.com/api/?name=" + safeName + "&background=random&color=fff&size=128&format=png").toURL().openStream();
					logo = ImageIO.read(inputStream);
					
				}
				
				companias.add(new Compania(0, campos[1], Double.parseDouble(campos[3]), logo, TipoViaje.getTipoViaje(Integer.parseInt(campos[0])), GestorDB.getPais(campos[2])));
				
			}

			sc.close();

		} catch (Exception e) {

			System.err.println("Error al cargar las companias desde CSV: (linea " + nLinea + ")");
			e.printStackTrace();

		}

		return companias;

	}
	
	public static void cargarCompaniasEnBD() {
		
		List<Compania> companias = cargarCompaniasCSV();
		
		String sql = """
					 INSERT INTO COMPANIA (NOM_COMP, FACTOR_PRECIO, LOGO_COMP, ID_TV, ID_D)
					 VALUES (?, ?, ?, ?, ?);
					 """;
		
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			for (Compania compania : companias) {
				
				byte[] logoBytes = null;
				
				try {
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(compania.getLogo(), "png", baos);
					logoBytes = baos.toByteArray();
					
				} catch (IOException e) {

					System.err.println("Error al cargar la imagen en la BD");
					e.printStackTrace();
					
				}
				
				pstmt.setString(1, compania.getNombre());
				pstmt.setDouble(2, compania.getFactorPrecio());
				pstmt.setBytes(3, logoBytes);
				pstmt.setInt(4, compania.getTipoViaje().getId());
				pstmt.setInt(5, compania.getPaisOrigen().getId());
				
				pstmt.addBatch();
				
			}
			
			pstmt.executeBatch();
			
		} catch (SQLException e) {
			
			System.err.println("Error al cargar compañías en BD");
			e.printStackTrace();
			
		}
		
	}
	
	private static final int MAX_DISTANCIA_BUS = 1200;
    private static final int MAX_DISTANCIA_TREN = 2500;
    private static final int MAX_DISTANCIA_AVION = 6000; // NO GLOBAL
	
	public static void cargarViajesEnDB(boolean soloCiudadesGrandes) {
		
		List<Destino> destinos = GestorDB.cargarDestinos();
		
		List<Ciudad> ciudadesGrandes = cargarCiudadesGrandesCSV();
		
		List<Ciudad> ciudades = new ArrayList<Ciudad>();
		List<Aeropuerto> aeropuertos = new ArrayList<Aeropuerto>();
		
		for (Destino destino : destinos) {
			if (destino instanceof Ciudad) {
				
				if (soloCiudadesGrandes) {
					
					for (Ciudad ciudad : ciudadesGrandes) {
						
						if (ciudad.getNombre().toLowerCase().equals(destino.getNombre().toLowerCase()) && ciudad.getNombrePais().toLowerCase().equals(destino.getNombrePais().toLowerCase())) {
							
							ciudades.add((Ciudad) destino);
							break;
							
						}
						
					}
				
				} else {
					
					ciudades.add((Ciudad) destino);
					
				}
				
			}
			if (destino instanceof Aeropuerto) {
				
				if (soloCiudadesGrandes) {
					
					for (Ciudad ciudad : ciudadesGrandes) {
						
						if (ciudad.getNombre().toLowerCase().equals(((Aeropuerto) destino).getCiudad().getNombre().toLowerCase()) && ciudad.getNombrePais().toLowerCase().equals(destino.getNombrePais().toLowerCase())) {
							
							aeropuertos.add((Aeropuerto) destino);
							break;
							
						} 
						
					}
					
				} else {
					
					aeropuertos.add((Aeropuerto) destino);								
					
				}
			}
		}
		
		List<Compania> companias = GestorDB.getCompanias();
		
		String sql = """
					 INSERT INTO VIAJE (HORA_V, PRECIO_P_V, NPLAZAS_V, ID_DS, ID_TV, ID_COMP, ID_D_ORIG, ID_D_DEST)
					 VALUES (?, ?, ?, ?, ?, ?, ?, ?),
					 		(?, ?, ?, ?, ?, ?, ?, ?);
					 """;
		
		int inserciones = 0;
		
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			for (DiaSemana diaSemana : DiaSemana.values()) {		// Iteramos por cada día de la semana (Todos los días tienen que tener viajes)
				
				for (Compania compania : companias) {				// También iteramos por cada compañía que existe (Todas las compañías tienen que tener viajes)
					
					// Una vez obtenida la lista de destinos origen comenzamos a crear los viajes 
					// Crearemos cierto rango de viajes dependiendo del tipo de compañía (Si es global tendrá mayor rango)
					// Tendremos en cuenta: -- La hora de salida será una hora aleatoria siempre múltiplo de 5 minutos
					// 						-- El precio será calculado por una fórmula que tendrá en cuenta el tipo de viaje, el precio base por km del tipo de viaje y el factor multiplicativo de la compañía
					// 						-- El destino origen será cualquiera de la lista filtrada
					// 						-- El destino destino será cualquiera de la lista completa
					//						-- Todos los viajes tendrán su rotacion (dependiendo de la hora a la que lleguen al destino) --> Madrid -> Londres => Londres -> Madrid
					
					if (compania.getTipoViaje() == TipoViaje.AVION) {																								// Si la compañía es de aviones
						
						List<Aeropuerto> aeropuertosOrigen = new ArrayList<Aeropuerto>();
						
						for (Aeropuerto aeropuerto : aeropuertos) {
							if ((compania.getPaisOrigen().getId() == 0 || aeropuerto.getPais().equals(compania.getPaisOrigen()))) aeropuertosOrigen.add(aeropuerto);	// Utilizaremos una lista de aeropuertos (como origen) filtrada por el pais de origen (Si la compañía es global => ID del país de la comp = 0 todos los aeropuertos son válidos)
						}
						
						if (aeropuertosOrigen.isEmpty()) {
					        System.out.println("Saltando compañía " + compania.getId() + " (Sin aeropuertos compatibles)");
					        continue; 
					    }
						
						int minViajes = compania.getPaisOrigen().getId() == 0? 2000 + (int) (Math.random() * 2000) : 300 + (int) (Math.random() * 300);		// Compañía grande => 2000 - 4000 viajes ; Compañía mediana => 300 - 600 viajes
						
						while (minViajes > 0) {
							
							// Aeropuertos
							
							Aeropuerto aeropuertoOrigen = aeropuertosOrigen.get((int) (Math.random() * aeropuertosOrigen.size()));
							Aeropuerto aeropuertoDestino = null;
							
							double distanciaKm = 0;
							
							int intentos = 0;
							
							do {
							
								aeropuertoDestino = aeropuertos.get((int) (Math.random() * aeropuertos.size()));
							
								distanciaKm = Utilidades.calcularDistancia(aeropuertoOrigen, aeropuertoDestino);
								
								intentos++;
								
							} while ((aeropuertoOrigen.equals(aeropuertoDestino) || (compania.getPaisOrigen().getId() != 0 && distanciaKm > MAX_DISTANCIA_AVION)) && intentos < 100);
							
							if (intentos >= 100) continue;
							
							// Nº de plazas
							
							int nPlazas = (int) (150 + (Math.random() * 200));													// Avion -> 150 - 350 plazas
							
							// Tiempos
							
							int hora = (int) (Math.random() * 24);
							int minuto = ((int) (Math.random() * 12)) * 5;
							
							LocalTime horaSalida = LocalTime.of(hora, minuto);
							
							int minutosDuracion = (int) ((((distanciaKm * Utilidades.getFactorDesviacion(compania.getTipoViaje())) / Utilidades.getVelocidadMedia(compania.getTipoViaje())) * 60));	
							
							while (minutosDuracion % 5 != 0) minutosDuracion++;
							
							LocalTime horaSalidaVuelta = horaSalida.plusMinutes(minutosDuracion + Utilidades.getTiempoRotacion(compania.getTipoViaje()));
							
							DiaSemana diaSemanaVuelta = diaSemana.plusDays((int) ((horaSalida.getHour() * 60 + horaSalida.getMinute() + minutosDuracion + Utilidades.getTiempoRotacion(TipoViaje.AVION)) / (24 * 60)));
							
							// Precio
							
							double precioBase = distanciaKm * Utilidades.getPrecioBasePorKm(TipoViaje.AVION) * compania.getFactorPrecio();		
							
							double precioIda = Math.round((precioBase * (0.8 + 0.4 * Math.random())) * 100) / 100.0;			// Factor aleatorio +-20%
							double precioVuelta = Math.round((precioBase * (0.8 + 0.4 * Math.random())) * 100) / 100.0;			// Factor aleatorio +-20%
							
							// Creamos los viajes
							
							Viaje ida = new Viaje(0, diaSemana, horaSalida, precioIda, nPlazas, TipoViaje.AVION, compania, aeropuertoOrigen, aeropuertoDestino);
							Viaje vuelta = new Viaje(0, diaSemanaVuelta, horaSalidaVuelta, precioVuelta, nPlazas, TipoViaje.AVION, compania, aeropuertoDestino, aeropuertoOrigen);
							
							// Metemos los viajes a la BD
							// Ida
							
							pstmt.setString(1, ida.getHora().toString());
							pstmt.setDouble(2, ida.getPrecioPorP());
							pstmt.setInt(3, ida.getNPlazas());
							pstmt.setInt(4, ida.getDiaSemana().getId());
							pstmt.setInt(5, ida.getTipoViaje().getId());
							pstmt.setInt(6, ida.getCompania().getId());
							pstmt.setInt(7, ida.getOrigen().getId());
							pstmt.setInt(8, ida.getDestino().getId());					
							pstmt.setString(9, vuelta.getHora().toString());
							pstmt.setDouble(10, vuelta.getPrecioPorP());
							pstmt.setInt(11, vuelta.getNPlazas());
							pstmt.setInt(12, vuelta.getDiaSemana().getId());
							pstmt.setInt(13, vuelta.getTipoViaje().getId());
							pstmt.setInt(14, vuelta.getCompania().getId());
							pstmt.setInt(15, vuelta.getOrigen().getId());
							pstmt.setInt(16, vuelta.getDestino().getId());
							
							pstmt.addBatch();
							
							minViajes -= 2;
							
							inserciones += 2;
							
						}
						
					} else {																																		// Si la compañía es de otra cosa (autobuses o trenes)
						
						List<Ciudad> ciudadesOrigen = new ArrayList<Ciudad>();
						
						for (Ciudad ciudad : ciudades) {
							if (compania.getPaisOrigen().getId() == 0 || ciudad.getPais().equals(compania.getPaisOrigen())) ciudadesOrigen.add(ciudad);				// Utilizaremos una lista de aeropuertos (como origen) filtrada por el pais de origen (Si la compañía es global => ID del país de la comp = 0 todos los aeropuertos son válidos)
						}
						
						if (ciudadesOrigen.isEmpty()) {
					        System.out.println("Saltando compañía " + compania.getId() + " (Sin aeropuertos compatibles)");
					        continue; 
					    }
						
						int minViajes = compania.getTipoViaje() == TipoViaje.TREN? 600 + (int) (Math.random() * 400) : 1000 + (int) (Math.random() * 1000);			// Compañía de trenes => 600 - 1000 viajes ; Compañía de autobuses => 1000 - 2000 viajes
						
						while (minViajes > 0) {
							
							// Ciudades
							
							Ciudad ciudadOrigen = ciudadesOrigen.get((int) (Math.random() * ciudadesOrigen.size()));
							Ciudad ciudadDestino = null;
							
							double distanciaKm = 0;
							
							int intentos = 0;
							
							do {
							
								ciudadDestino = ciudades.get((int) (Math.random() * ciudades.size()));
							
								distanciaKm = Utilidades.calcularDistancia(ciudadOrigen, ciudadDestino);
								
								intentos++;
								
							} while ((ciudadOrigen.equals(ciudadDestino) || (compania.getTipoViaje() == TipoViaje.TREN && distanciaKm > MAX_DISTANCIA_TREN) || (compania.getTipoViaje() == TipoViaje.AUTOBUS && distanciaKm > MAX_DISTANCIA_BUS)) && intentos < 100);
							
							if (intentos >= 100) continue;
							
							// Nº de plazas
							
							int nPlazas = compania.getTipoViaje() == TipoViaje.TREN? (int) (300 + (Math.random() * 300)) : (int) (40 + (Math.random() * 20));			// Tren -> 300 - 600 plazas ; Bus -> 40 - 60 plazas
							
							// Tiempos
							
							int hora = (int) (Math.random() * 24);
							int minuto = ((int) (Math.random() * 12)) * 5;
							
							LocalTime horaSalida = LocalTime.of(hora, minuto);
							
							int minutosDuracion = (int) ((((distanciaKm * Utilidades.getFactorDesviacion(compania.getTipoViaje())) / Utilidades.getVelocidadMedia(compania.getTipoViaje())) * 60));	
							
							while (minutosDuracion % 5 != 0) minutosDuracion++;
							
							LocalTime horaSalidaVuelta = horaSalida.plusMinutes(minutosDuracion + Utilidades.getTiempoRotacion(compania.getTipoViaje()));
							
							DiaSemana diaSemanaVuelta = diaSemana.plusDays((int) ((horaSalida.getHour() * 60 + horaSalida.getMinute() + minutosDuracion + Utilidades.getTiempoRotacion(compania.getTipoViaje())) / (24 * 60)));
							
							// Precio
							
							double precioBase = distanciaKm * Utilidades.getPrecioBasePorKm(compania.getTipoViaje()) * compania.getFactorPrecio();		
							
							double precioIda = Math.round((precioBase * (0.8 + 0.4 * Math.random())) * 100) / 100.0;			// Factor aleatorio +-20%
							double precioVuelta = Math.round((precioBase * (0.8 + 0.4 * Math.random())) * 100) / 100.0;			// Factor aleatorio +-20%
							
							// Creamos los viajes
							
							Viaje ida = new Viaje(0, diaSemana, horaSalida, precioIda, nPlazas, compania.getTipoViaje(), compania, ciudadOrigen, ciudadDestino);
							Viaje vuelta = new Viaje(0, diaSemanaVuelta, horaSalidaVuelta, precioVuelta, nPlazas, compania.getTipoViaje(), compania, ciudadDestino, ciudadOrigen);
							
							// Metemos los viajes a la BD
							// Ida
							
							pstmt.setString(1, ida.getHora().toString());
							pstmt.setDouble(2, ida.getPrecioPorP());
							pstmt.setInt(3, ida.getNPlazas());
							pstmt.setInt(4, ida.getDiaSemana().getId());
							pstmt.setInt(5, ida.getTipoViaje().getId());
							pstmt.setInt(6, ida.getCompania().getId());
							pstmt.setInt(7, ida.getOrigen().getId());
							pstmt.setInt(8, ida.getDestino().getId());					
							pstmt.setString(9, vuelta.getHora().toString());
							pstmt.setDouble(10, vuelta.getPrecioPorP());
							pstmt.setInt(11, vuelta.getNPlazas());
							pstmt.setInt(12, vuelta.getDiaSemana().getId());
							pstmt.setInt(13, vuelta.getTipoViaje().getId());
							pstmt.setInt(14, vuelta.getCompania().getId());
							pstmt.setInt(15, vuelta.getOrigen().getId());
							pstmt.setInt(16, vuelta.getDestino().getId());
							
							pstmt.addBatch();
							
							minViajes -= 2;
							
							inserciones += 2;
							
						}
						
					}
					
					pstmt.executeBatch();
					
					System.out.println("Viajes insertados: " + inserciones + " ; Compañía: " + compania.getId() + "/300 ; Día: " + (diaSemana.ordinal() + 1) + "/7");
					
				}
				
			}
			
			System.out.println("------------------");
			System.out.println("Proceso terminado!");
			System.out.println("------------------");
			
		} catch (SQLException e) {
			
			System.err.println("Error al cargar viajes en BD");
			
		}
		
	}
	
	private static final int N_MAX_CIUDADES_CERCANAS = 5;
	
	public static void cargarViajesCiudadesSinAerACiudadesConAerCercanasEnDB() {
		
		List<Destino> destinos = GestorDB.cargarDestinos();
		
		Map<Pais, List<Compania>> companiasTerrestresPorPais = new HashMap<Pais, List<Compania>>();

		for (Integer indice : companiaPorIndice.keySet()) {
			
			Compania compania = companiaPorIndice.get(indice);
			
			if (compania.getTipoViaje() == TipoViaje.AVION) continue;
			
			if (!companiasTerrestresPorPais.containsKey(compania.getPaisOrigen())) companiasTerrestresPorPais.put(compania.getPaisOrigen(), new ArrayList<Compania>());
			
			companiasTerrestresPorPais.get(compania.getPaisOrigen()).add(compania);
			
		}
		
		List<Ciudad> ciudadesSinAer = new ArrayList<Ciudad>();
		List<Ciudad> ciudadesConAer = new ArrayList<Ciudad>();
		
		for (Destino destino : destinos) {
			
			if (destino instanceof Ciudad) {
				
				if (GestorDB.tieneAeropuertos((Ciudad) destino)) {
					ciudadesConAer.add((Ciudad) destino);
				} else {
					ciudadesSinAer.add((Ciudad) destino);
				}
				
			}
			
		}
		
		String sql = """
					 INSERT INTO VIAJE (HORA_V, PRECIO_P_V, NPLAZAS_V, ID_DS, ID_TV, ID_COMP, ID_D_ORIG, ID_D_DEST)
					 VALUES (?, ?, ?, ?, ?, ?, ?, ?),
					 		(?, ?, ?, ?, ?, ?, ?, ?);
					 """;
		
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			int contadorCiudadesSinAer = 0;
			
			for (Ciudad ciudadSinAer : ciudadesSinAer) {
			
				contadorCiudadesSinAer++;
				
				List<Ciudad> ciudadesConAerCercanas = new ArrayList<Ciudad>();
				
				for (Ciudad ciudadConAer : ciudadesConAer) {
					
					if (Utilidades.calcularDistancia(ciudadSinAer, ciudadConAer) <= 200) ciudadesConAerCercanas.add(ciudadConAer);
					
				}
				
				ciudadesConAerCercanas.sort(new Comparator<Ciudad>() {

					@Override
					public int compare(Ciudad c1, Ciudad c2) {
						return Double.compare(Utilidades.calcularDistancia(ciudadSinAer, c1), Utilidades.calcularDistancia(ciudadSinAer, c2));
					}
					
				});
				
				ciudadesConAerCercanas = ciudadesConAerCercanas.subList(0, Math.min(N_MAX_CIUDADES_CERCANAS, ciudadesConAerCercanas.size()));
				
				for (Ciudad ciudadConAerCercana : ciudadesConAerCercanas) {
					
					for (DiaSemana diaSemana : DiaSemana.values()) {
					
						for (int i = 0; i < 2; i++) {
							
							// Distancia
							
							double distanciaKm = Utilidades.calcularDistancia(ciudadSinAer, ciudadConAerCercana);
							
							// Compañía
							
							Compania compania = null;
							
							if (companiasTerrestresPorPais.get(ciudadSinAer.getPais()) != null) {
								
								compania = companiasTerrestresPorPais.get(ciudadSinAer.getPais()).get((int) (Math.random() * companiasTerrestresPorPais.get(ciudadSinAer.getPais()).size()));
								
							} else {
								
								break;
								
							}
							
							// Nº de plazas
							
							int nPlazas = compania.getTipoViaje() == TipoViaje.TREN? (int) (300 + (Math.random() * 300)) : (int) (40 + (Math.random() * 20));			// Tren -> 300 - 600 plazas ; Bus -> 40 - 60 plazas
							
							// Tiempos
							
						    int hora = (i == 0) ? 6 : 15 + (int) (Math.random() * 4); // Variación de 4 horas
						    int minuto = ((int) (Math.random() * 12)) * 5;
						    
						    LocalTime horaSalida = LocalTime.of(hora, minuto);
							
							int minutosDuracion = (int) ((((distanciaKm * Utilidades.getFactorDesviacion(compania.getTipoViaje())) / Utilidades.getVelocidadMedia(compania.getTipoViaje())) * 60));	
							
							while (minutosDuracion % 5 != 0) minutosDuracion++;
							
							LocalTime horaSalidaVuelta = horaSalida.plusMinutes(minutosDuracion + Utilidades.getTiempoRotacion(compania.getTipoViaje()));
							
							DiaSemana diaSemanaVuelta = diaSemana.plusDays((int) ((horaSalida.getHour() * 60 + horaSalida.getMinute() + minutosDuracion + Utilidades.getTiempoRotacion(compania.getTipoViaje())) / (24 * 60)));
							
							// Precio
							
							double precioBase = distanciaKm * Utilidades.getPrecioBasePorKm(compania.getTipoViaje()) * compania.getFactorPrecio();		
							
							double precioIda = Math.round((precioBase * (0.8 + 0.4 * Math.random())) * 100) / 100.0;			// Factor aleatorio +-20%
							double precioVuelta = Math.round((precioBase * (0.8 + 0.4 * Math.random())) * 100) / 100.0;			// Factor aleatorio +-20%
							
							// Creamos los viajes
							
							Viaje ida = new Viaje(0, diaSemana, horaSalida, precioIda, nPlazas, compania.getTipoViaje(), compania, ciudadSinAer, ciudadConAerCercana);
							Viaje vuelta = new Viaje(0, diaSemanaVuelta, horaSalidaVuelta, precioVuelta, nPlazas, compania.getTipoViaje(), compania, ciudadConAerCercana, ciudadSinAer);
							
							// Metemos los viajes a la BD
							// Ida
							
							pstmt.setString(1, ida.getHora().toString());
							pstmt.setDouble(2, ida.getPrecioPorP());
							pstmt.setInt(3, ida.getNPlazas());
							pstmt.setInt(4, ida.getDiaSemana().getId());
							pstmt.setInt(5, ida.getTipoViaje().getId());
							pstmt.setInt(6, ida.getCompania().getId());
							pstmt.setInt(7, ida.getOrigen().getId());
							pstmt.setInt(8, ida.getDestino().getId());					
							pstmt.setString(9, vuelta.getHora().toString());
							pstmt.setDouble(10, vuelta.getPrecioPorP());
							pstmt.setInt(11, vuelta.getNPlazas());
							pstmt.setInt(12, vuelta.getDiaSemana().getId());
							pstmt.setInt(13, vuelta.getTipoViaje().getId());
							pstmt.setInt(14, vuelta.getCompania().getId());
							pstmt.setInt(15, vuelta.getOrigen().getId());
							pstmt.setInt(16, vuelta.getDestino().getId());
							
							pstmt.addBatch();
							
						}
					
					}
					
				}

				pstmt.executeBatch();
				
				System.out.println("Ciudades sin aeropuerto:" + contadorCiudadesSinAer + "/" + ciudadesSinAer.size());
				
			}
			
		} catch (SQLException e) {
			
			System.err.println("Error al cargar los viajes en la BD");
			e.printStackTrace();
			
		}
		
	}
	
	public static final int N_MAX_CIUDADES_GRANDES_CERCANAS = 30;
	
	public static void cargarViajesCiudadesGrandesConAerACiudadesGrandesConAerCercanasEnDB() {
		
		List<Destino> destinos = GestorDB.cargarDestinos();
		
		Map<Pais, List<Compania>> companiasPorPais = new HashMap<Pais, List<Compania>>();

		for (Integer indice : companiaPorIndice.keySet()) {
			
			Compania compania = companiaPorIndice.get(indice);
			
			if (!companiasPorPais.containsKey(compania.getPaisOrigen())) companiasPorPais.put(compania.getPaisOrigen(), new ArrayList<Compania>());
			
			companiasPorPais.get(compania.getPaisOrigen()).add(compania);
			
		}

		List<Ciudad> ciudadesGrandes = cargarCiudadesGrandesCSV();
		List<Ciudad> ciudadesConAer = new ArrayList<Ciudad>();
		
		for (Destino destino : destinos) {
			
			if (destino instanceof Ciudad && GestorDB.tieneAeropuertos((Ciudad) destino)) {
				
				for (Ciudad ciudad : ciudadesGrandes) {
					
					if (ciudad.getNombre().toLowerCase().equals(destino.getNombre().toLowerCase()) && ciudad.getNombrePais().toLowerCase().equals(destino.getNombrePais().toLowerCase())) {
						
						ciudadesConAer.add((Ciudad) destino);
						break;
						
					} 
					
				}
				
			}
			
		}
		
		String sql = """
					 INSERT INTO VIAJE (HORA_V, PRECIO_P_V, NPLAZAS_V, ID_DS, ID_TV, ID_COMP, ID_D_ORIG, ID_D_DEST)
					 VALUES (?, ?, ?, ?, ?, ?, ?, ?),
					 		(?, ?, ?, ?, ?, ?, ?, ?);
					 """;
		
		try (Connection con = DriverManager.getConnection(CONNECTION_STRING);
			 PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			int contadorCiudadesConAer = 0;
			
			for (Ciudad ciudadConAerOrig : ciudadesConAer) {
			
				contadorCiudadesConAer++;
				
				List<Ciudad> ciudadesConAerCercanas = new ArrayList<Ciudad>();
				
				for (Ciudad ciudadConAerDest : ciudadesConAer) {
					
					if (Utilidades.calcularDistancia(ciudadConAerOrig, ciudadConAerDest) <= 800 && !ciudadConAerOrig.equals(ciudadConAerDest)) ciudadesConAerCercanas.add(ciudadConAerDest);
					
				}
				
				ciudadesConAerCercanas.sort(new Comparator<Ciudad>() {

					@Override
					public int compare(Ciudad c1, Ciudad c2) {
						return Double.compare(Utilidades.calcularDistancia(ciudadConAerOrig, c1), Utilidades.calcularDistancia(ciudadConAerOrig, c2));
					}
					
				});
				
				ciudadesConAerCercanas = ciudadesConAerCercanas.subList(0, Math.min(N_MAX_CIUDADES_GRANDES_CERCANAS, ciudadesConAerCercanas.size()));
				
				for (Ciudad ciudadConAerCercana : ciudadesConAerCercanas) {
					
					for (DiaSemana diaSemana : DiaSemana.values()) {
					
						for (int i = 0; i < 2; i++) {
							
							// Distancia
							
							double distanciaKm = Utilidades.calcularDistancia(ciudadConAerOrig, ciudadConAerCercana);
							
							// Compañía
							
							Compania compania = null;
							
							int intentos = 0;
							
							if (companiasPorPais.get(ciudadConAerOrig.getPais()) != null) {
							
								if (distanciaKm < 250) {
									
									do {
										
										compania = companiasPorPais.get(ciudadConAerOrig.getPais()).get((int) (Math.random() * companiasPorPais.get(ciudadConAerOrig.getPais()).size()));
										intentos++;
										
									} while ((compania == null || (compania != null && compania.getTipoViaje() == TipoViaje.AVION)) && intentos < 100);
									
								} else {
									
									do {
										
										compania = companiasPorPais.get(ciudadConAerOrig.getPais()).get((int) (Math.random() * companiasPorPais.get(ciudadConAerOrig.getPais()).size()));
										intentos++;
										
									} while (compania == null && intentos < 100);
									
								}
							
							} else {
								
								Pais global = new Pais(0, "Global", 0, 0);
								
								if (distanciaKm > 250) {
									
									do {
										
										compania = companiasPorPais.get(global).get((int) (Math.random() * companiasPorPais.get(global).size()));
										intentos++;
										
									} while (compania == null && intentos < 100);
									
								}
								
							}
							
							if (intentos >= 100 || compania == null) continue;
							
							// Origen y destino
							
							Destino origen = compania.getTipoViaje() != TipoViaje.AVION? ciudadConAerOrig : aeropuertosPorIndiceCiudad.get(ciudadConAerOrig.getId()).get((int) (Math.random() * aeropuertosPorIndiceCiudad.get(ciudadConAerOrig.getId()).size()));
							Destino destino = compania.getTipoViaje() != TipoViaje.AVION? ciudadConAerCercana : aeropuertosPorIndiceCiudad.get(ciudadConAerCercana.getId()).get((int) (Math.random() * aeropuertosPorIndiceCiudad.get(ciudadConAerCercana.getId()).size()));
							
							// Nº de plazas
							
							int nPlazas = compania.getTipoViaje() == TipoViaje.AVION? (int) (150 + (Math.random() * 200)) : compania.getTipoViaje() == TipoViaje.TREN? (int) (300 + (Math.random() * 300)) : (int) (40 + (Math.random() * 20));			// Avion -> 150 - 350 plazas ; Tren -> 300 - 600 plazas ; Bus -> 40 - 60 plazas
							
							// Tiempos
							
						    int hora = ((i == 0) ? 6 : 15) + (int) (Math.random() * 4); // Variación de 4 horas
						    int minuto = ((int) (Math.random() * 12)) * 5;
						    
						    LocalTime horaSalida = LocalTime.of(hora, minuto);
							
							int minutosDuracion = (int) ((((distanciaKm * Utilidades.getFactorDesviacion(compania.getTipoViaje())) / Utilidades.getVelocidadMedia(compania.getTipoViaje())) * 60));	
							
							while (minutosDuracion % 5 != 0) minutosDuracion++;
							
							LocalTime horaSalidaVuelta = horaSalida.plusMinutes(minutosDuracion + Utilidades.getTiempoRotacion(compania.getTipoViaje()));
							
							DiaSemana diaSemanaVuelta = diaSemana.plusDays((int) ((horaSalida.getHour() * 60 + horaSalida.getMinute() + minutosDuracion + Utilidades.getTiempoRotacion(compania.getTipoViaje())) / (24 * 60)));
							
							// Precio
							
							double precioBase = distanciaKm * Utilidades.getPrecioBasePorKm(compania.getTipoViaje()) * compania.getFactorPrecio();		
							
							double precioIda = Math.round((precioBase * (0.8 + 0.4 * Math.random())) * 100) / 100.0;			// Factor aleatorio +-20%
							double precioVuelta = Math.round((precioBase * (0.8 + 0.4 * Math.random())) * 100) / 100.0;			// Factor aleatorio +-20%
							
							// Creamos los viajes
							
							Viaje ida = new Viaje(0, diaSemana, horaSalida, precioIda, nPlazas, compania.getTipoViaje(), compania, origen, destino);
							Viaje vuelta = new Viaje(0, diaSemanaVuelta, horaSalidaVuelta, precioVuelta, nPlazas, compania.getTipoViaje(), compania, destino, origen);
							
							// Metemos los viajes a la BD
							// Ida
							
							pstmt.setString(1, ida.getHora().toString());
							pstmt.setDouble(2, ida.getPrecioPorP());
							pstmt.setInt(3, ida.getNPlazas());
							pstmt.setInt(4, ida.getDiaSemana().getId());
							pstmt.setInt(5, ida.getTipoViaje().getId());
							pstmt.setInt(6, ida.getCompania().getId());
							pstmt.setInt(7, ida.getOrigen().getId());
							pstmt.setInt(8, ida.getDestino().getId());					
							pstmt.setString(9, vuelta.getHora().toString());
							pstmt.setDouble(10, vuelta.getPrecioPorP());
							pstmt.setInt(11, vuelta.getNPlazas());
							pstmt.setInt(12, vuelta.getDiaSemana().getId());
							pstmt.setInt(13, vuelta.getTipoViaje().getId());
							pstmt.setInt(14, vuelta.getCompania().getId());
							pstmt.setInt(15, vuelta.getOrigen().getId());
							pstmt.setInt(16, vuelta.getDestino().getId());
							
							pstmt.addBatch();
							
						}
					
					}
					
				}

				pstmt.executeBatch();
				
				System.out.println("Ciudades sin aeropuerto:" + contadorCiudadesConAer + "/" + ciudadesConAer.size());
				
			}
			
		} catch (SQLException e) {
			
			System.err.println("Error al cargar los viajes en la BD");
			e.printStackTrace();
			
		}
		
	}
	
}
