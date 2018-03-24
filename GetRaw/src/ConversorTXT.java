import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class ConversorTXT {

	public static ConversorTXT miConversor = new ConversorTXT();

	private ConversorTXT() {

	}

	public static ConversorTXT getConversor() {
		return miConversor;
	}

	public static void convertirTXTaARFF(String pRutaTXT, String pRutaARFF) {

		try {

			String cabecera = "@relation spam\n\n@attribute text string\n@attribute Class {spam,ham}\n";
			String datos = "\n@data";
			ArrayList<String> array = arrayDeLineas(pRutaTXT);

			for (String linea : array) {
				datos = datos + "\n'" + getTexto(linea).replace("'", "") + "', '" + getClase(linea) + "'";
			}

			exportarTextoArchivo(cabecera + datos, pRutaARFF);
		}

		catch (Exception e) {
			System.out.println("Se ha producido un error al convertir el TXT a ARFF");
		}

	}

	private static void exportarTextoArchivo(String pTexto, String pRuta) throws Exception {
		BufferedWriter f = new BufferedWriter(new FileWriter(pRuta, true));
		f.write(pTexto);
		f.close();
	}

	private static ArrayList<String> arrayDeLineas(String pRutaArchivo) throws Exception {

		BufferedReader b = new BufferedReader(new FileReader(pRutaArchivo));
		ArrayList<String> aDevolver = new ArrayList<String>();
		Iterator<String> itr = b.lines().iterator();
		String lineaAct;

		while (itr.hasNext()) {
			lineaAct = itr.next();
			aDevolver.add(lineaAct);
		}

		b.close();
		return aDevolver;

	}

	private static String getClase(String pLinea) {
		return pLinea.split("\t")[0];
	}

	private static String getTexto(String pLinea) {
		int i = 0;
		while (pLinea.charAt(i) != '\t') {
			i++;
		}
		return pLinea.substring(i + 1);
	}

}
