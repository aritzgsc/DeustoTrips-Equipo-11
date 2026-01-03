package db;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

// Clase hecha por GEMINI para generar un CSV que utilizaremos para cargar las compañías en la DB

public class GeneradorCompaniasCSV {

	private static final String OUTPUT_FILE = "resources/db/companias.csv";

	// --- MAPA GIGANTE DE DOMINIOS PARA GOOGLE FAVICONS ---
    private static final Map<String, String> DOMINIOS_COMPANIAS = new HashMap<>();

    static {
        // ==========================================
        // 1. AEROLÍNEAS (Europa, América, Asia, África)
        // ==========================================
        
        // --- GIGANTES GLOBALES Y LOW COST ---
        DOMINIOS_COMPANIAS.put("Ryanair", "ryanair.com");
        DOMINIOS_COMPANIAS.put("EasyJet", "easyjet.com");
        DOMINIOS_COMPANIAS.put("Wizz Air", "wizzair.com");
        DOMINIOS_COMPANIAS.put("Emirates", "emirates.com");
        DOMINIOS_COMPANIAS.put("Qatar Airways", "qatarairways.com");
        DOMINIOS_COMPANIAS.put("Etihad", "etihad.com");
        DOMINIOS_COMPANIAS.put("Turkish Airlines", "turkishairlines.com");
        DOMINIOS_COMPANIAS.put("LATAM", "latamairlines.com");
        DOMINIOS_COMPANIAS.put("AirAsia", "airasia.com");
        DOMINIOS_COMPANIAS.put("Norwegian", "norwegian.com");
        DOMINIOS_COMPANIAS.put("Pegasus", "flypgs.com");

        // --- ESPAÑA ---
        DOMINIOS_COMPANIAS.put("Iberia", "iberia.com");
        DOMINIOS_COMPANIAS.put("Iberia Express", "iberiaexpress.com");
        DOMINIOS_COMPANIAS.put("Air Nostrum", "airnostrum.es");
        DOMINIOS_COMPANIAS.put("Air Europa", "aireuropa.com");
        DOMINIOS_COMPANIAS.put("Vueling", "vueling.com");
        DOMINIOS_COMPANIAS.put("Volotea", "volotea.com");
        DOMINIOS_COMPANIAS.put("Binter", "bintercanarias.com");
        DOMINIOS_COMPANIAS.put("Canaryfly", "canaryfly.es");
        DOMINIOS_COMPANIAS.put("Plus Ultra", "plusultra.com");
        DOMINIOS_COMPANIAS.put("Wamos Air", "wamosair.com");
        DOMINIOS_COMPANIAS.put("AlbaStar", "albastar.es");

        // --- EUROPA (Resto) ---
        DOMINIOS_COMPANIAS.put("Lufthansa", "lufthansa.com");
        DOMINIOS_COMPANIAS.put("Air France", "airfrance.com");
        DOMINIOS_COMPANIAS.put("KLM", "klm.com");
        DOMINIOS_COMPANIAS.put("British Airways", "britishairways.com");
        DOMINIOS_COMPANIAS.put("Virgin Atlantic", "virginatlantic.com");
        DOMINIOS_COMPANIAS.put("TAP Portugal", "flytap.com");
        DOMINIOS_COMPANIAS.put("SATA Azores", "azoresairlines.pt");
        DOMINIOS_COMPANIAS.put("ITA Airways", "ita-airways.com");
        DOMINIOS_COMPANIAS.put("Neos", "neosair.it");
        DOMINIOS_COMPANIAS.put("Air Dolomiti", "airdolomiti.eu");
        DOMINIOS_COMPANIAS.put("Aegean Airlines", "aegeanair.com");
        DOMINIOS_COMPANIAS.put("Olympic Air", "olympicair.com");
        DOMINIOS_COMPANIAS.put("Sky Express", "skyexpress.gr");
        DOMINIOS_COMPANIAS.put("SAS", "flysas.com");
        DOMINIOS_COMPANIAS.put("Finnair", "finnair.com");
        DOMINIOS_COMPANIAS.put("Icelandair", "icelandair.com");
        DOMINIOS_COMPANIAS.put("Play", "flyplay.com");
        DOMINIOS_COMPANIAS.put("Aer Lingus", "aerlingus.com");
        DOMINIOS_COMPANIAS.put("Swiss", "swiss.com");
        DOMINIOS_COMPANIAS.put("Edelweiss", "flyedelweiss.com");
        DOMINIOS_COMPANIAS.put("Austrian Airlines", "austrian.com");
        DOMINIOS_COMPANIAS.put("Brussels Airlines", "brusselsairlines.com");
        DOMINIOS_COMPANIAS.put("TUI fly", "tui.com");
        DOMINIOS_COMPANIAS.put("Condor", "condor.com");
        DOMINIOS_COMPANIAS.put("Eurowings", "eurowings.com");
        DOMINIOS_COMPANIAS.put("LOT Polish", "lot.com");
        DOMINIOS_COMPANIAS.put("Smartwings", "smartwings.com");
        DOMINIOS_COMPANIAS.put("Czech Airlines", "csa.cz");
        DOMINIOS_COMPANIAS.put("Croatia Airlines", "croatiaairlines.com");
        DOMINIOS_COMPANIAS.put("Air Serbia", "airserbia.com");
        DOMINIOS_COMPANIAS.put("Tarom", "tarom.ro");
        DOMINIOS_COMPANIAS.put("Bulgaria Air", "air.bg");
        DOMINIOS_COMPANIAS.put("Air Baltic", "airbaltic.com");
        DOMINIOS_COMPANIAS.put("Luxair", "luxair.lu");
        DOMINIOS_COMPANIAS.put("Air Malta", "airmalta.com");
        DOMINIOS_COMPANIAS.put("Transavia", "transavia.com");

        // --- NORTEAMÉRICA ---
        DOMINIOS_COMPANIAS.put("American Airlines", "aa.com");
        DOMINIOS_COMPANIAS.put("Delta", "delta.com");
        DOMINIOS_COMPANIAS.put("United Airlines", "united.com");
        DOMINIOS_COMPANIAS.put("Southwest", "southwest.com");
        DOMINIOS_COMPANIAS.put("JetBlue", "jetblue.com");
        DOMINIOS_COMPANIAS.put("Spirit", "spirit.com");
        DOMINIOS_COMPANIAS.put("Frontier", "flyfrontier.com");
        DOMINIOS_COMPANIAS.put("Alaska Airlines", "alaskaair.com");
        DOMINIOS_COMPANIAS.put("Hawaiian Airlines", "hawaiianairlines.com");
        DOMINIOS_COMPANIAS.put("Air Canada", "aircanada.com");
        DOMINIOS_COMPANIAS.put("WestJet", "westjet.com");
        DOMINIOS_COMPANIAS.put("Porter", "flyporter.com");
        DOMINIOS_COMPANIAS.put("Air Transat", "airtransat.com");
        DOMINIOS_COMPANIAS.put("Aeromexico", "aeromexico.com");
        DOMINIOS_COMPANIAS.put("Volaris", "volaris.com");
        DOMINIOS_COMPANIAS.put("Viva Aerobus", "vivaaerobus.com");

        // --- LATINOAMÉRICA ---
        DOMINIOS_COMPANIAS.put("Avianca", "avianca.com");
        DOMINIOS_COMPANIAS.put("Copa Airlines", "copaair.com");
        DOMINIOS_COMPANIAS.put("Aerolineas Argentinas", "aerolineas.com.ar");
        DOMINIOS_COMPANIAS.put("Flybondi", "flybondi.com");
        DOMINIOS_COMPANIAS.put("GOL", "voegol.com.br");
        DOMINIOS_COMPANIAS.put("Azul", "voeazul.com.br");
        DOMINIOS_COMPANIAS.put("Sky Airline", "skyairline.com");
        DOMINIOS_COMPANIAS.put("JetSmart", "jetsmart.com");
        DOMINIOS_COMPANIAS.put("Boliviana de Aviacion", "boa.bo");

        // --- ASIA, OCEANÍA Y ORIENTE MEDIO ---
        DOMINIOS_COMPANIAS.put("Singapore Airlines", "singaporeair.com");
        DOMINIOS_COMPANIAS.put("Scoot", "flyscoot.com");
        DOMINIOS_COMPANIAS.put("Cathay Pacific", "cathaypacific.com");
        DOMINIOS_COMPANIAS.put("ANA", "ana.co.jp");
        DOMINIOS_COMPANIAS.put("Japan Airlines", "jal.com");
        DOMINIOS_COMPANIAS.put("Peach", "flypeach.com");
        DOMINIOS_COMPANIAS.put("Korean Air", "koreanair.com");
        DOMINIOS_COMPANIAS.put("Asiana", "flyasiana.com");
        DOMINIOS_COMPANIAS.put("China Southern", "csair.com");
        DOMINIOS_COMPANIAS.put("China Eastern", "ceair.com");
        DOMINIOS_COMPANIAS.put("Air China", "airchina.com.cn");
        DOMINIOS_COMPANIAS.put("Hainan Airlines", "hainanairlines.com");
        DOMINIOS_COMPANIAS.put("EVA Air", "evaair.com");
        DOMINIOS_COMPANIAS.put("China Airlines", "china-airlines.com");
        DOMINIOS_COMPANIAS.put("Thai Airways", "thaiairways.com");
        DOMINIOS_COMPANIAS.put("Bangkok Airways", "bangkokair.com");
        DOMINIOS_COMPANIAS.put("Vietnam Airlines", "vietnamairlines.com");
        DOMINIOS_COMPANIAS.put("VietJet Air", "vietjetair.com");
        DOMINIOS_COMPANIAS.put("Garuda Indonesia", "garuda-indonesia.com");
        DOMINIOS_COMPANIAS.put("Lion Air", "lionair.co.id");
        DOMINIOS_COMPANIAS.put("Malaysia Airlines", "malaysiaairlines.com");
        DOMINIOS_COMPANIAS.put("Philippine Airlines", "philippineairlines.com");
        DOMINIOS_COMPANIAS.put("Cebu Pacific", "cebupacificair.com");
        DOMINIOS_COMPANIAS.put("IndiGo", "goindigo.in");
        DOMINIOS_COMPANIAS.put("Air India", "airindia.com");
        DOMINIOS_COMPANIAS.put("SpiceJet", "spicejet.com");
        DOMINIOS_COMPANIAS.put("Vistara", "airvistara.com");
        DOMINIOS_COMPANIAS.put("Saudia", "saudia.com");
        DOMINIOS_COMPANIAS.put("Flynas", "flynas.com");
        DOMINIOS_COMPANIAS.put("Oman Air", "omanair.com");
        DOMINIOS_COMPANIAS.put("Gulf Air", "gulfair.com");
        DOMINIOS_COMPANIAS.put("Kuwait Airways", "kuwaitairways.com");
        DOMINIOS_COMPANIAS.put("Royal Jordanian", "rj.com");
        DOMINIOS_COMPANIAS.put("El Al", "elal.com");
        DOMINIOS_COMPANIAS.put("Qantas", "qantas.com");
        DOMINIOS_COMPANIAS.put("Virgin Australia", "virginaustralia.com");
        DOMINIOS_COMPANIAS.put("Jetstar", "jetstar.com");
        DOMINIOS_COMPANIAS.put("Air New Zealand", "airnewzealand.com");

        // --- ÁFRICA ---
        DOMINIOS_COMPANIAS.put("Ethiopian Airlines", "ethiopianairlines.com");
        DOMINIOS_COMPANIAS.put("Royal Air Maroc", "royalairmaroc.com");
        DOMINIOS_COMPANIAS.put("EgyptAir", "egyptair.com");
        DOMINIOS_COMPANIAS.put("Kenya Airways", "kenya-airways.com");
        DOMINIOS_COMPANIAS.put("South African Airways", "flysaa.com");
        DOMINIOS_COMPANIAS.put("RwandAir", "rwandair.com");
        DOMINIOS_COMPANIAS.put("Tunisair", "tunisair.com");
        DOMINIOS_COMPANIAS.put("Air Algerie", "airalgerie.dz");

        // ==========================================
        // 2. TRENES (Alta Velocidad y Regionales)
        // ==========================================
        
        // --- ESPAÑA ---
        DOMINIOS_COMPANIAS.put("Renfe", "renfe.com");
        DOMINIOS_COMPANIAS.put("Ave", "renfe.com"); // Mismo dominio
        DOMINIOS_COMPANIAS.put("Avlo", "renfe.com");
        DOMINIOS_COMPANIAS.put("Alvia", "renfe.com");
        DOMINIOS_COMPANIAS.put("Avant", "renfe.com");
        DOMINIOS_COMPANIAS.put("Media Distancia", "renfe.com");
        DOMINIOS_COMPANIAS.put("Cercanias", "renfe.com");
        DOMINIOS_COMPANIAS.put("Feve", "renfe.com");
        DOMINIOS_COMPANIAS.put("Ouigo", "ouigo.com");
        DOMINIOS_COMPANIAS.put("Iryo", "iryo.eu");
        DOMINIOS_COMPANIAS.put("Euskotren", "euskotren.eus");
        DOMINIOS_COMPANIAS.put("FGC", "fgc.cat");

        // --- EUROPA ---
        DOMINIOS_COMPANIAS.put("SNCF", "sncf.com");
        DOMINIOS_COMPANIAS.put("TGV InOui", "sncf-connect.com");
        DOMINIOS_COMPANIAS.put("Ouigo France", "ouigo.com");
        DOMINIOS_COMPANIAS.put("Eurostar", "eurostar.com");
        DOMINIOS_COMPANIAS.put("Thalys", "eurostar.com"); // Ahora es Eurostar
        DOMINIOS_COMPANIAS.put("Deutsche Bahn", "bahn.de");
        DOMINIOS_COMPANIAS.put("ICE", "bahn.de");
        DOMINIOS_COMPANIAS.put("Flixtrain", "flixbus.com"); // Misma web que el bus
        DOMINIOS_COMPANIAS.put("Trenitalia", "trenitalia.com");
        DOMINIOS_COMPANIAS.put("Frecciarossa", "trenitalia.com");
        DOMINIOS_COMPANIAS.put("Italo", "italotreno.it");
        DOMINIOS_COMPANIAS.put("SBB", "sbb.ch");
        DOMINIOS_COMPANIAS.put("OBB", "oebb.at");
        DOMINIOS_COMPANIAS.put("Railjet", "oebb.at");
        DOMINIOS_COMPANIAS.put("Nightjet", "nightjet.com");
        DOMINIOS_COMPANIAS.put("Westbahn", "westbahn.at");
        DOMINIOS_COMPANIAS.put("NS", "ns.nl");
        DOMINIOS_COMPANIAS.put("NMBS/SNCB", "belgiantrain.be");
        DOMINIOS_COMPANIAS.put("DSB", "dsb.dk");
        DOMINIOS_COMPANIAS.put("SJ", "sj.se");
        DOMINIOS_COMPANIAS.put("Vy", "vy.no");
        DOMINIOS_COMPANIAS.put("VR", "vr.fi");
        DOMINIOS_COMPANIAS.put("CP", "cp.pt");
        DOMINIOS_COMPANIAS.put("Alfa Pendular", "cp.pt");
        DOMINIOS_COMPANIAS.put("PKP Intercity", "intercity.pl");
        DOMINIOS_COMPANIAS.put("Ceske Drahy", "cd.cz");
        DOMINIOS_COMPANIAS.put("RegioJet", "regiojet.com");
        DOMINIOS_COMPANIAS.put("Leo Express", "leoexpress.com");
        DOMINIOS_COMPANIAS.put("MAV", "mavcsoport.hu");
        DOMINIOS_COMPANIAS.put("CFR", "cfrcalatori.ro");
        DOMINIOS_COMPANIAS.put("OSE", "hellenictrain.gr");
        DOMINIOS_COMPANIAS.put("Hellenic Train", "hellenictrain.gr");
        DOMINIOS_COMPANIAS.put("TCDD", "tcdd.gov.tr");
        DOMINIOS_COMPANIAS.put("YHT", "tcddtasimacilik.gov.tr");

        // --- REINO UNIDO ---
        DOMINIOS_COMPANIAS.put("LNER", "lner.co.uk");
        DOMINIOS_COMPANIAS.put("GWR", "gwr.com");
        DOMINIOS_COMPANIAS.put("Avanti West Coast", "avantiwestcoast.co.uk");
        DOMINIOS_COMPANIAS.put("ScotRail", "scotrail.co.uk");
        DOMINIOS_COMPANIAS.put("CrossCountry", "crosscountrytrains.co.uk");
        DOMINIOS_COMPANIAS.put("Northern", "northernrailway.co.uk");
        DOMINIOS_COMPANIAS.put("TransPennine", "tpexpress.co.uk");

        // --- RESTO DEL MUNDO ---
        DOMINIOS_COMPANIAS.put("Amtrak", "amtrak.com");
        DOMINIOS_COMPANIAS.put("Acela", "amtrak.com");
        DOMINIOS_COMPANIAS.put("Brightline", "gobrightline.com");
        DOMINIOS_COMPANIAS.put("Via Rail", "viarail.ca");
        DOMINIOS_COMPANIAS.put("Rocky Mountaineer", "rockymountaineer.com");
        DOMINIOS_COMPANIAS.put("JR East", "jreast.co.jp");
        DOMINIOS_COMPANIAS.put("JR Central", "english.jr-central.co.jp");
        DOMINIOS_COMPANIAS.put("JR West", "westjr.co.jp");
        DOMINIOS_COMPANIAS.put("Shinkansen", "japanrailpass.net");
        DOMINIOS_COMPANIAS.put("China Railway", "12306.cn");
        DOMINIOS_COMPANIAS.put("CRH", "12306.cn");
        DOMINIOS_COMPANIAS.put("Korail", "letskorail.com");
        DOMINIOS_COMPANIAS.put("KTX", "letskorail.com");
        DOMINIOS_COMPANIAS.put("THSR", "thsrc.com.tw");
        DOMINIOS_COMPANIAS.put("Indian Railways", "indianrail.gov.in");
        DOMINIOS_COMPANIAS.put("Vande Bharat", "indianrail.gov.in");
        DOMINIOS_COMPANIAS.put("KTM", "ktmb.com.my");
        DOMINIOS_COMPANIAS.put("SAR", "sar.com.sa");
        DOMINIOS_COMPANIAS.put("Haramain", "hhr.sa");
        DOMINIOS_COMPANIAS.put("ONCF", "oncf.ma");
        DOMINIOS_COMPANIAS.put("Al Boraq", "oncf.ma");

        // ==========================================
        // 3. AUTOBUSES
        // ==========================================
        
        // --- ESPAÑA ---
        DOMINIOS_COMPANIAS.put("Alsa", "alsa.es");
        DOMINIOS_COMPANIAS.put("Alsa Supra", "alsa.es");
        DOMINIOS_COMPANIAS.put("Avanza", "avanzabus.com");
        DOMINIOS_COMPANIAS.put("Socibus", "socibus.es");
        DOMINIOS_COMPANIAS.put("Monbus", "monbus.es");
        DOMINIOS_COMPANIAS.put("Hife", "hife.es");
        DOMINIOS_COMPANIAS.put("Damas", "damas-sa.es");
        DOMINIOS_COMPANIAS.put("Vibasa", "monbus.es");
        DOMINIOS_COMPANIAS.put("Lycar", "lycar.es");
        DOMINIOS_COMPANIAS.put("Jimenez Dorado", "jimenezdorado.com");
        DOMINIOS_COMPANIAS.put("Samar", "samar.es");
        DOMINIOS_COMPANIAS.put("La Sepulvedana", "lasepulvedana.es");
        DOMINIOS_COMPANIAS.put("Teisa", "teisa-bus.com");
        DOMINIOS_COMPANIAS.put("Moventis", "moventis.es");
        DOMINIOS_COMPANIAS.put("Bilman Bus", "bilmanbus.es");
        DOMINIOS_COMPANIAS.put("Interbus", "interbus.es");
        DOMINIOS_COMPANIAS.put("Arriva Spain", "arriva.es");

        // --- EUROPA ---
        DOMINIOS_COMPANIAS.put("FlixBus", "flixbus.com");
        DOMINIOS_COMPANIAS.put("BlaBlaBus", "blablacar.es");
        DOMINIOS_COMPANIAS.put("Eurolines", "eurolines.de");
        DOMINIOS_COMPANIAS.put("RegioJet Bus", "regiojet.com");
        DOMINIOS_COMPANIAS.put("Leo Express Bus", "leoexpress.com");
        DOMINIOS_COMPANIAS.put("Student Agency", "studentagency.eu");
        DOMINIOS_COMPANIAS.put("Sindbad", "sindbad.pl");
        DOMINIOS_COMPANIAS.put("Lux Express", "luxexpress.eu");
        DOMINIOS_COMPANIAS.put("Ecolines", "ecolines.net");
        DOMINIOS_COMPANIAS.put("Itabus", "itabus.it");
        DOMINIOS_COMPANIAS.put("Marino Bus", "marinobus.it");
        DOMINIOS_COMPANIAS.put("Rede Expressos", "rede-expressos.pt");
        DOMINIOS_COMPANIAS.put("Eva Transportes", "eva-bus.com");
        DOMINIOS_COMPANIAS.put("National Express", "nationalexpress.com");
        DOMINIOS_COMPANIAS.put("Megabus", "megabus.com");
        DOMINIOS_COMPANIAS.put("Stagecoach", "stagecoachbus.com");
        DOMINIOS_COMPANIAS.put("Citylink", "citylink.co.uk");
        DOMINIOS_COMPANIAS.put("Bus Eireann", "buseireann.ie");
        DOMINIOS_COMPANIAS.put("Croatia Bus", "croatiabus.hr");
        DOMINIOS_COMPANIAS.put("Kamil Koc", "kamilkoc.com.tr");
        DOMINIOS_COMPANIAS.put("Pamukkale", "pamukkale.com.tr");
        DOMINIOS_COMPANIAS.put("Metro Turizm", "metroturizm.com.tr");

        // --- AMÉRICA ---
        DOMINIOS_COMPANIAS.put("Greyhound", "greyhound.com");
        DOMINIOS_COMPANIAS.put("Megabus USA", "us.megabus.com");
        DOMINIOS_COMPANIAS.put("BoltBus", "boltbus.com"); // (Nota: Fusionado con Greyhound, pero mantenemos)
        DOMINIOS_COMPANIAS.put("Coach USA", "coachusa.com");
        DOMINIOS_COMPANIAS.put("Peter Pan", "peterpanbus.com");
        DOMINIOS_COMPANIAS.put("Trailways", "trailways.com");
        DOMINIOS_COMPANIAS.put("Jefferson Lines", "jeffersonlines.com");
        DOMINIOS_COMPANIAS.put("Red Arrow", "redarrow.ca");
        DOMINIOS_COMPANIAS.put("Rider Express", "riderexpress.ca");
        DOMINIOS_COMPANIAS.put("ADO", "ado.com.mx");
        DOMINIOS_COMPANIAS.put("ADO Platino", "ado.com.mx");
        DOMINIOS_COMPANIAS.put("ADO GL", "ado.com.mx");
        DOMINIOS_COMPANIAS.put("OCC", "ado.com.mx");
        DOMINIOS_COMPANIAS.put("Estrella de Oro", "estrelladeoro.com.mx");
        DOMINIOS_COMPANIAS.put("Primera Plus", "primeraplus.com.mx");
        DOMINIOS_COMPANIAS.put("ETN", "etn.com.mx");
        DOMINIOS_COMPANIAS.put("Turistar", "etn.com.mx");
        DOMINIOS_COMPANIAS.put("Futura", "futura.com.mx");
        DOMINIOS_COMPANIAS.put("Chihuahuenses", "chihuahuenses.com.mx");
        
        // --- SUDAMÉRICA ---
        DOMINIOS_COMPANIAS.put("Cruz del Sur", "cruzdelsur.com.pe");
        DOMINIOS_COMPANIAS.put("Civa", "civa.com.pe");
        DOMINIOS_COMPANIAS.put("Oltursa", "oltursa.pe");
        DOMINIOS_COMPANIAS.put("Movil Bus", "movilbus.pe");
        DOMINIOS_COMPANIAS.put("Turbus", "turbus.cl");
        DOMINIOS_COMPANIAS.put("Pullman Bus", "pullmanbus.cl");
        DOMINIOS_COMPANIAS.put("Condor Bus", "condorbus.cl");
        DOMINIOS_COMPANIAS.put("EME Bus", "emebus.cl");
        DOMINIOS_COMPANIAS.put("Flecha Bus", "flechabus.com.ar");
        DOMINIOS_COMPANIAS.put("Via Bariloche", "viabariloche.com.ar");
        DOMINIOS_COMPANIAS.put("Chevallier", "nuevachevallier.com");
        DOMINIOS_COMPANIAS.put("Andesmar", "andesmar.com");
        DOMINIOS_COMPANIAS.put("Plusmar", "plusmar.com.ar");
        DOMINIOS_COMPANIAS.put("Urquiza", "generalurquiza.com.ar");
        DOMINIOS_COMPANIAS.put("Expreso Brasilia", "expresobrasilia.com");
        DOMINIOS_COMPANIAS.put("Copetran", "copetran.com");
        DOMINIOS_COMPANIAS.put("Bolivariano", "bolivariano.com.co");
        DOMINIOS_COMPANIAS.put("Berlinas", "berlinas.com");
        DOMINIOS_COMPANIAS.put("Itapemirim", "itapemirim.com.br");
        DOMINIOS_COMPANIAS.put("Cometa", "viacaocometa.com.br");
        DOMINIOS_COMPANIAS.put("1001", "autoviacao1001.com.br");
        DOMINIOS_COMPANIAS.put("Gontijo", "gontijo.com.br");
        DOMINIOS_COMPANIAS.put("Catarinense", "catarinense.com.br");

        // --- ASIA / OCEANÍA / ÁFRICA ---
        DOMINIOS_COMPANIAS.put("Willer Express", "willerexpress.com");
        DOMINIOS_COMPANIAS.put("JR Bus", "jrbus-dreamgo.jp");
        DOMINIOS_COMPANIAS.put("Nihon Kotsu", "nihonkotsu.co.jp");
        DOMINIOS_COMPANIAS.put("Greyhound Australia", "greyhound.com.au");
        DOMINIOS_COMPANIAS.put("Murrays", "murrays.com.au");
        DOMINIOS_COMPANIAS.put("Firefly Express", "fireflyexpress.com.au");
        DOMINIOS_COMPANIAS.put("InterCity", "intercity.co.nz");
        DOMINIOS_COMPANIAS.put("Nakhonchai Air", "nakhonchaiair.com");
        DOMINIOS_COMPANIAS.put("The Transport Co", "transport.co.th");
        DOMINIOS_COMPANIAS.put("Sombat Tour", "sombattour.com");
        DOMINIOS_COMPANIAS.put("Phuong Trang", "futabus.vn");
        DOMINIOS_COMPANIAS.put("Sinh Tourist", "thesinhtourist.vn");
        DOMINIOS_COMPANIAS.put("KSRTC", "ksrtc.in");
        DOMINIOS_COMPANIAS.put("MSRTC", "msrtc.gov.in");
        DOMINIOS_COMPANIAS.put("RedBus Operator", "redbus.in");
        DOMINIOS_COMPANIAS.put("CTM", "ctm.ma");
        DOMINIOS_COMPANIAS.put("Supratours", "supratours.ma");
        DOMINIOS_COMPANIAS.put("Intercape", "intercape.co.za");
        DOMINIOS_COMPANIAS.put("Greyhound ZA", "greyhound.co.za");
    }

	private static void write(PrintWriter writer, int tipo, String nombre, String iso, double factor) {
        String url;
        
        if (DOMINIOS_COMPANIAS.containsKey(nombre)) {
            // Construimos la URL mágica de Google
            String dominio = DOMINIOS_COMPANIAS.get(nombre);
            url = "https://www.google.com/s2/favicons?domain=" + dominio + "&sz=128";
        } else {
            // Si no está en la lista, usamos el generador de iniciales como backup
            String safeName = nombre.replace(" ", "+");
            url = "https://ui-avatars.com/api/?name=" + safeName + "&background=random&color=fff&size=128&format=png";
        }

        // Locale.US para asegurar el punto en el decimal
        String linea = String.format(java.util.Locale.US, "%d,%s,%s,%.2f,%s", tipo, nombre, iso, factor, url);
        writer.println(linea);
    }

	public static void main(String[] args) {
		System.out.println("Iniciando generación masiva de datos de transporte...");

		try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_FILE))) {
			// Cabecera CSV
			writer.println("TIPO,NOMBRE,ISO_PAIS,FACTOR,URL_LOGO");

			cargarAerolineas(writer);
			cargarTrenes(writer);
			cargarAutobuses(writer);

			System.out.println("--------------------------------------------------");
			System.out.println("¡PROCESO TERMINADO! Archivo " + OUTPUT_FILE + " creado.");
			System.out.println("--------------------------------------------------");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// --- CARGA DE AEROLÍNEAS (Clasificación Estricta) ---
	private static void cargarAerolineas(PrintWriter w) {
		System.out.println("Escribiendo Aerolíneas...");

		// 1. GIGANTES GLOBALES (Operativa mundial masiva o Pan-Regionales gigantes)
		// Usan "GLOB" para indicar que pueden aparecer en rutas internacionales complejas
		write(w, 1, "Ryanair", "GLOB", 0.5); // Rey del Low Cost Europa
		write(w, 1, "EasyJet", "GLOB", 0.6); // Pan-Europea
		write(w, 1, "Wizz Air", "GLOB", 0.5); // Pan-Europea Este/Oeste
		write(w, 1, "Emirates", "GLOB", 1.5); // Conector Mundial
		write(w, 1, "Qatar Airways", "GLOB", 1.5); // Conector Mundial
		write(w, 1, "Etihad", "GLOB", 1.4); // Conector Mundial
		write(w, 1, "Turkish Airlines", "GLOB", 1.2); // Vuela a más países que nadie
		write(w, 1, "United Airlines", "GLOB", 1.1);
		write(w, 1, "Delta", "GLOB", 1.2);
		write(w, 1, "American Airlines", "GLOB", 1.1);
		write(w, 1, "Lufthansa", "GLOB", 1.3);
		write(w, 1, "Air France", "GLOB", 1.2);
		write(w, 1, "KLM", "GLOB", 1.2);
		write(w, 1, "British Airways", "GLOB", 1.3);
		write(w, 1, "LATAM", "GLOB", 1.1); // Gigante Pan-Sudamericano
		write(w, 1, "AirAsia", "GLOB", 0.5); // Gigante Pan-Asiático

		// 2. EUROPA (Enfoque Nacional/Regional)
		// España
		write(w, 1, "Iberia", "ES", 1.2);
		write(w, 1, "Air Europa", "ES", 1.0);
		write(w, 1, "Vueling", "ES", 0.8);
		write(w, 1, "Volotea", "ES", 0.7);
		write(w, 1, "Binter", "ES", 1.0);
		write(w, 1, "Iberia Express", "ES", 0.8);
		write(w, 1, "Air Nostrum", "ES", 0.9);
		write(w, 1, "Canaryfly", "ES", 0.7);
		write(w, 1, "Plus Ultra", "ES", 0.9);
		write(w, 1, "Wamos Air", "ES", 0.9);
		write(w, 1, "AlbaStar", "ES", 0.8);

		// Resto Europa
		write(w, 1, "TAP Portugal", "PT", 1.0);
		write(w, 1, "SATA Azores", "PT", 1.1);
		write(w, 1, "ITA Airways", "IT", 1.1);
		write(w, 1, "Neos", "IT", 0.9);
		write(w, 1, "Air Dolomiti", "IT", 1.0);
		write(w, 1, "Aegean Airlines", "GR", 1.0);
		write(w, 1, "Olympic Air", "GR", 0.9);
		write(w, 1, "Sky Express", "GR", 0.8);
		write(w, 1, "SAS", "SE", 1.2); // Escandinavia (SE/NO/DK)
		write(w, 1, "Norwegian", "NO", 0.7);
		write(w, 1, "Finnair", "FI", 1.2);
		write(w, 1, "Icelandair", "IS", 1.1);
		write(w, 1, "Play", "IS", 0.6);
		write(w, 1, "Aer Lingus", "IE", 1.0);
		write(w, 1, "Swiss", "CH", 1.4);
		write(w, 1, "Edelweiss", "CH", 1.1);
		write(w, 1, "Austrian Airlines", "AT", 1.1);
		write(w, 1, "Brussels Airlines", "BE", 1.1);
		write(w, 1, "TUI fly", "DE", 0.8);
		write(w, 1, "Condor", "DE", 0.9);
		write(w, 1, "Eurowings", "DE", 0.8);
		write(w, 1, "LOT Polish", "PL", 1.0);
		write(w, 1, "Smartwings", "CZ", 0.8);
		write(w, 1, "Czech Airlines", "CZ", 1.0);
		write(w, 1, "Croatia Airlines", "HR", 1.0);
		write(w, 1, "Air Serbia", "RS", 0.9);
		write(w, 1, "Tarom", "RO", 0.9);
		write(w, 1, "Bulgaria Air", "BG", 0.8);
		write(w, 1, "Air Baltic", "LV", 1.0);
		write(w, 1, "Luxair", "LU", 1.3);
		write(w, 1, "Air Malta", "MT", 1.0);

		// 3. AMÉRICA (Norte y Sur - Nacionales)
		write(w, 1, "Southwest", "US", 0.9);
		write(w, 1, "JetBlue", "US", 1.0);
		write(w, 1, "Spirit", "US", 0.5);
		write(w, 1, "Frontier", "US", 0.6);
		write(w, 1, "Alaska Airlines", "US", 1.0);
		write(w, 1, "Hawaiian Airlines", "US", 1.2);
		write(w, 1, "Air Canada", "CA", 1.1);
		write(w, 1, "WestJet", "CA", 0.9);
		write(w, 1, "Porter", "CA", 1.0);
		write(w, 1, "Air Transat", "CA", 0.9);
		write(w, 1, "Aeromexico", "MX", 1.0);
		write(w, 1, "Volaris", "MX", 0.7);
		write(w, 1, "Viva Aerobus", "MX", 0.6);
		write(w, 1, "Avianca", "CO", 0.9);
		write(w, 1, "Copa Airlines", "PA", 1.1); // Hub de las Américas
		write(w, 1, "Aerolineas Argentinas", "AR", 1.0);
		write(w, 1, "Flybondi", "AR", 0.6);
		write(w, 1, "GOL", "BR", 0.8);
		write(w, 1, "Azul", "BR", 0.9);
		write(w, 1, "Sky Airline", "CL", 0.7);
		write(w, 1, "JetSmart", "CL", 0.6);
		write(w, 1, "Boliviana de Aviacion", "BO", 0.8);

		// 4. ASIA, OCEANÍA Y ORIENTE MEDIO (Nacionales)
		write(w, 1, "Singapore Airlines", "SG", 1.6);
		write(w, 1, "Scoot", "SG", 0.7);
		write(w, 1, "Cathay Pacific", "HK", 1.4);
		write(w, 1, "ANA", "JP", 1.5);
		write(w, 1, "Japan Airlines", "JP", 1.5);
		write(w, 1, "Peach", "JP", 0.7);
		write(w, 1, "Korean Air", "KR", 1.3);
		write(w, 1, "Asiana", "KR", 1.2);
		write(w, 1, "China Southern", "CN", 1.0);
		write(w, 1, "China Eastern", "CN", 1.0);
		write(w, 1, "Air China", "CN", 1.0);
		write(w, 1, "Hainan Airlines", "CN", 1.1);
		write(w, 1, "EVA Air", "TW", 1.2);
		write(w, 1, "China Airlines", "TW", 1.1);
		write(w, 1, "Thai Airways", "TH", 1.1);
		write(w, 1, "Bangkok Airways", "TH", 1.2);
		write(w, 1, "Vietnam Airlines", "VN", 0.9);
		write(w, 1, "VietJet Air", "VN", 0.6);
		write(w, 1, "Garuda Indonesia", "ID", 1.0);
		write(w, 1, "Lion Air", "ID", 0.6);
		write(w, 1, "Malaysia Airlines", "MY", 1.0);
		write(w, 1, "Philippine Airlines", "PH", 1.0);
		write(w, 1, "Cebu Pacific", "PH", 0.7);
		write(w, 1, "IndiGo", "IN", 0.6);
		write(w, 1, "Air India", "IN", 1.0);
		write(w, 1, "SpiceJet", "IN", 0.6);
		write(w, 1, "Vistara", "IN", 1.2);
		write(w, 1, "Saudia", "SA", 1.1);
		write(w, 1, "Flynas", "SA", 0.7);
		write(w, 1, "Oman Air", "OM", 1.2);
		write(w, 1, "Gulf Air", "BH", 1.1);
		write(w, 1, "Kuwait Airways", "KW", 1.0);
		write(w, 1, "Royal Jordanian", "JO", 1.0);
		write(w, 1, "El Al", "IL", 1.2);
		write(w, 1, "Qantas", "AU", 1.3);
		write(w, 1, "Virgin Australia", "AU", 1.0);
		write(w, 1, "Jetstar", "AU", 0.6);
		write(w, 1, "Air New Zealand", "NZ", 1.3);

		// 5. ÁFRICA
		write(w, 1, "Ethiopian Airlines", "ET", 1.0);
		write(w, 1, "Royal Air Maroc", "MA", 1.0);
		write(w, 1, "EgyptAir", "EG", 0.9);
		write(w, 1, "Kenya Airways", "KE", 1.0);
		write(w, 1, "South African Airways", "ZA", 1.1);
		write(w, 1, "RwandAir", "RW", 1.0);
		write(w, 1, "Tunisair", "TN", 0.8);
		write(w, 1, "Air Algerie", "DZ", 0.8);
	}

	// --- CARGA DE TRENES (Alta Velocidad y Regional) ---
	private static void cargarTrenes(PrintWriter w) {
		System.out.println("Escribiendo Trenes...");

		// España
		write(w, 2, "Renfe", "ES", 1.0);
		write(w, 2, "Ave", "ES", 1.3);
		write(w, 2, "Avlo", "ES", 0.7);
		write(w, 2, "Ouigo", "ES", 0.6);
		write(w, 2, "Iryo", "ES", 0.9);
		write(w, 2, "Alvia", "ES", 1.1);
		write(w, 2, "Avant", "ES", 1.0);
		write(w, 2, "Media Distancia", "ES", 0.8);
		write(w, 2, "Cercanias", "ES", 0.5);
		write(w, 2, "Feve", "ES", 0.6);
		write(w, 2, "Euskotren", "ES", 0.6);
		write(w, 2, "FGC", "ES", 0.6);

		// Europa (Alta Velocidad y Operadores Nacionales)
		write(w, 2, "SNCF", "FR", 1.1);
		write(w, 2, "TGV InOui", "FR", 1.4);
		write(w, 2, "Ouigo France", "FR", 0.7);
		write(w, 2, "Eurostar", "GB", 1.8); // Conecta GB, FR, BE, NL
		write(w, 2, "Thalys", "BE", 1.6);
		write(w, 2, "Deutsche Bahn", "DE", 1.2);
		write(w, 2, "ICE", "DE", 1.4);
		write(w, 2, "Flixtrain", "DE", 0.6);
		write(w, 2, "Trenitalia", "IT", 1.0);
		write(w, 2, "Frecciarossa", "IT", 1.3);
		write(w, 2, "Italo", "IT", 1.1);
		write(w, 2, "SBB", "CH", 1.6); // Suiza, caro pero puntual
		write(w, 2, "OBB", "AT", 1.1);
		write(w, 2, "Railjet", "AT", 1.2);
		write(w, 2, "Nightjet", "AT", 1.3); // Tren nocturno famoso
		write(w, 2, "Westbahn", "AT", 0.8);
		write(w, 2, "NS", "NL", 1.1);
		write(w, 2, "NMBS/SNCB", "BE", 1.0);
		write(w, 2, "DSB", "DK", 1.3);
		write(w, 2, "SJ", "SE", 1.2);
		write(w, 2, "Vy", "NO", 1.3);
		write(w, 2, "VR", "FI", 1.2);
		write(w, 2, "CP", "PT", 0.8);
		write(w, 2, "Alfa Pendular", "PT", 1.1);
		write(w, 2, "PKP Intercity", "PL", 0.8);
		write(w, 2, "Ceske Drahy", "CZ", 0.7);
		write(w, 2, "RegioJet", "CZ", 0.7);
		write(w, 2, "Leo Express", "CZ", 0.6);
		write(w, 2, "MAV", "HU", 0.7);
		write(w, 2, "CFR", "RO", 0.6);
		write(w, 2, "OSE", "GR", 0.8);
		write(w, 2, "Hellenic Train", "GR", 0.8);
		write(w, 2, "TCDD", "TR", 0.7);
		write(w, 2, "YHT", "TR", 1.0);

		// Reino Unido (Franquicias)
		write(w, 2, "LNER", "GB", 1.2);
		write(w, 2, "GWR", "GB", 1.1);
		write(w, 2, "Avanti West Coast", "GB", 1.3);
		write(w, 2, "ScotRail", "GB", 1.0);
		write(w, 2, "CrossCountry", "GB", 1.1);
		write(w, 2, "Northern", "GB", 0.8);
		write(w, 2, "TransPennine", "GB", 0.9);

		// Resto del Mundo
		write(w, 2, "Amtrak", "US", 1.4);
		write(w, 2, "Acela", "US", 1.8);
		write(w, 2, "Brightline", "US", 1.5);
		write(w, 2, "Via Rail", "CA", 1.3);
		write(w, 2, "Rocky Mountaineer", "CA", 2.5); // Lujo
		write(w, 2, "JR East", "JP", 1.1);
		write(w, 2, "JR Central", "JP", 1.2);
		write(w, 2, "JR West", "JP", 1.1);
		write(w, 2, "Shinkansen", "JP", 1.5); // Bala
		write(w, 2, "China Railway", "CN", 0.6);
		write(w, 2, "CRH", "CN", 0.9); // Alta velocidad china
		write(w, 2, "Korail", "KR", 0.9);
		write(w, 2, "KTX", "KR", 1.1);
		write(w, 2, "THSR", "TW", 1.1);
		write(w, 2, "Indian Railways", "IN", 0.4);
		write(w, 2, "Vande Bharat", "IN", 0.8);
		write(w, 2, "KTM", "MY", 0.6);
		write(w, 2, "SAR", "SA", 1.2); // Saudi Railway
		write(w, 2, "Haramain", "SA", 1.5); // Alta velocidad Meca-Medina
		write(w, 2, "ONCF", "MA", 0.7);
		write(w, 2, "Al Boraq", "MA", 1.1); // TGV Marruecos
	}

	// --- CARGA DE AUTOBUSES (Código de país estricto) ---
	private static void cargarAutobuses(PrintWriter w) {
		System.out.println("Escribiendo Autobuses...");

		// España (Extenso)
		write(w, 3, "Alsa", "ES", 1.0);
		write(w, 3, "Alsa Supra", "ES", 1.3);
		write(w, 3, "Avanza", "ES", 0.9);
		write(w, 3, "Socibus", "ES", 0.8);
		write(w, 3, "Monbus", "ES", 0.8);
		write(w, 3, "Hife", "ES", 0.9);
		write(w, 3, "Damas", "ES", 0.8);
		write(w, 3, "Vibasa", "ES", 0.9);
		write(w, 3, "Lycar", "ES", 0.7);
		write(w, 3, "Jimenez Dorado", "ES", 0.8);
		write(w, 3, "Samar", "ES", 0.7);
		write(w, 3, "La Sepulvedana", "ES", 0.8);
		write(w, 3, "Teisa", "ES", 0.8);
		write(w, 3, "Moventis", "ES", 0.9);
		write(w, 3, "Bilman Bus", "ES", 0.9);
		write(w, 3, "Interbus", "ES", 0.8);
		write(w, 3, "Arriva Spain", "ES", 0.8);

		// Europa (Grandes Conectores)
		write(w, 3, "FlixBus", "DE", 0.7); // Aunque opera global, base alemana
		write(w, 3, "BlaBlaBus", "FR", 0.6);
		write(w, 3, "Eurolines", "BE", 0.8);
		write(w, 3, "RegioJet Bus", "CZ", 0.9);
		write(w, 3, "Leo Express Bus", "CZ", 0.8);
		write(w, 3, "Student Agency", "CZ", 0.7);
		write(w, 3, "Sindbad", "PL", 0.8);
		write(w, 3, "Lux Express", "EE", 1.1); // Lujo en el báltico
		write(w, 3, "Ecolines", "LV", 0.8);
		write(w, 3, "Itabus", "IT", 0.7);
		write(w, 3, "Marino Bus", "IT", 0.9);
		write(w, 3, "Rede Expressos", "PT", 0.9);
		write(w, 3, "Eva Transportes", "PT", 0.8);
		write(w, 3, "National Express", "GB", 1.0);
		write(w, 3, "Megabus", "GB", 0.5); // Low cost extremo
		write(w, 3, "Stagecoach", "GB", 0.8);
		write(w, 3, "Citylink", "GB", 0.9);
		write(w, 3, "Bus Eireann", "IE", 0.9);
		write(w, 3, "Croatia Bus", "HR", 0.8);
		write(w, 3, "Kamil Koc", "TR", 0.9); // Gigante turco (propiedad de Flix)
		write(w, 3, "Pamukkale", "TR", 1.0);
		write(w, 3, "Metro Turizm", "TR", 0.8);

		// Norteamérica (USA / México / Canadá)
		write(w, 3, "Greyhound", "US", 0.8);
		write(w, 3, "Megabus USA", "US", 0.6);
		write(w, 3, "BoltBus", "US", 0.7);
		write(w, 3, "Coach USA", "US", 0.9);
		write(w, 3, "Peter Pan", "US", 0.8);
		write(w, 3, "Trailways", "US", 0.9);
		write(w, 3, "Jefferson Lines", "US", 0.8);
		write(w, 3, "Red Arrow", "CA", 1.2); // Lujo Canadá
		write(w, 3, "Rider Express", "CA", 0.8);
		write(w, 3, "ADO", "MX", 1.0); // El rey de México
		write(w, 3, "ADO Platino", "MX", 1.4); // Lujo extremo
		write(w, 3, "ADO GL", "MX", 1.2);
		write(w, 3, "OCC", "MX", 0.8);
		write(w, 3, "Estrella de Oro", "MX", 0.9);
		write(w, 3, "Primera Plus", "MX", 1.1); // Muy buen servicio
		write(w, 3, "ETN", "MX", 1.3); // Asientos tipo cama
		write(w, 3, "Turistar", "MX", 1.3);
		write(w, 3, "Futura", "MX", 0.9);
		write(w, 3, "Chihuahuenses", "MX", 0.8);

		// Sudamérica (Larga distancia real)
		write(w, 3, "Cruz del Sur", "PE", 1.3); // Top Perú
		write(w, 3, "Civa", "PE", 0.9);
		write(w, 3, "Oltursa", "PE", 1.1);
		write(w, 3, "Movil Bus", "PE", 0.9);
		write(w, 3, "Turbus", "CL", 1.0);
		write(w, 3, "Pullman Bus", "CL", 0.9);
		write(w, 3, "Condor Bus", "CL", 0.8);
		write(w, 3, "EME Bus", "CL", 1.1);
		write(w, 3, "Flecha Bus", "AR", 1.0);
		write(w, 3, "Via Bariloche", "AR", 1.1);
		write(w, 3, "Chevallier", "AR", 1.0);
		write(w, 3, "Andesmar", "AR", 1.0);
		write(w, 3, "Plusmar", "AR", 0.9);
		write(w, 3, "Urquiza", "AR", 0.9);
		write(w, 3, "Expreso Brasilia", "CO", 0.9);
		write(w, 3, "Copetran", "CO", 0.9);
		write(w, 3, "Bolivariano", "CO", 1.0);
		write(w, 3, "Berlinas", "CO", 1.1);
		write(w, 3, "Itapemirim", "BR", 0.9);
		write(w, 3, "Cometa", "BR", 1.0);
		write(w, 3, "1001", "BR", 1.0);
		write(w, 3, "Gontijo", "BR", 0.9);
		write(w, 3, "Catarinense", "BR", 1.0);

		// Asia / Oceanía / África
		write(w, 3, "Willer Express", "JP", 1.0); // Japón famoso rosa
		write(w, 3, "JR Bus", "JP", 1.1);
		write(w, 3, "Nihon Kotsu", "JP", 1.0);
		write(w, 3, "Greyhound Australia", "AU", 1.0);
		write(w, 3, "Murrays", "AU", 1.1);
		write(w, 3, "Firefly Express", "AU", 0.9);
		write(w, 3, "InterCity", "NZ", 1.0); // Nueva Zelanda
		write(w, 3, "Nakhonchai Air", "TH", 1.1); // Tailandia Lujo
		write(w, 3, "The Transport Co", "TH", 0.8);
		write(w, 3, "Sombat Tour", "TH", 0.9);
		write(w, 3, "Phuong Trang", "VN", 0.9); // Futa Bus
		write(w, 3, "Sinh Tourist", "VN", 0.8);
		write(w, 3, "KSRTC", "IN", 0.7);
		write(w, 3, "MSRTC", "IN", 0.6);
		write(w, 3, "RedBus Operator", "IN", 0.8); // Genérico para agregador
		write(w, 3, "CTM", "MA", 1.0); // Marruecos
		write(w, 3, "Supratours", "MA", 0.9);
		write(w, 3, "Intercape", "ZA", 1.1); // Sudáfrica
		write(w, 3, "Greyhound ZA", "ZA", 1.0);
	}

}