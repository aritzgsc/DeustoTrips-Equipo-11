package main.util;

import java.text.Normalizer;

public class Utilidades {

	// Funciones para comparar strings (usadas para no descartar tantas ciudades que
	// podrían ser descartadas con equals)

	// Función que normaliza un String (le quita carácteres raros, lo pasa a
	// minusculas y le quita espacios al final)

	public static String normalizar(String input) {

		if (input == null) {
			return "";
		}
		
		String inputNorm = Normalizer.normalize(input, Normalizer.Form.NFD);

		return inputNorm.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase().trim();
		
	}

	// Función que devuelve un entero que indica la "distancia" entre dos strings
	// 0 si son iguales, 1 si hay 1 error, 2 si hay 2...
	// Recomendada por GEMINI

	public static int distanciaLevenshtein(String a, String b) {

		a = normalizar(a);
		b = normalizar(b);

		int[] costs = new int[b.length() + 1];

		for (int j = 0; j < costs.length; j++)

			costs[j] = j;

		for (int i = 1; i <= a.length(); i++) {

			costs[0] = i;

			int nw = i - 1;

			for (int j = 1; j <= b.length(); j++) {

				int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
				nw = costs[j];
				costs[j] = cj;

			}

		}

		return costs[b.length()];
	}

}
