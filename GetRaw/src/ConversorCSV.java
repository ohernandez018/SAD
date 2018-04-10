import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;

import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class ConversorCSV implements IConversor {

	private static ConversorCSV miConversorCSV = new ConversorCSV();

	// Expresion regular que se utiliza para detectar el patron: ","
	private final String regEx = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

	private ConversorCSV() {
	}

	public static ConversorCSV getConversorCSV() {
		return miConversorCSV;
	}

	@Override
	public void convertir(String pathOrigen, String pathDestino) throws Exception {

		try {
			System.out.println("Convirtiendo el fichero .csv a .arff ...");

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(pathOrigen))));

			File tempFile = new File(new File(pathOrigen).getName() + ".csv");
			FileWriter writer = new FileWriter(tempFile);

			String line;
			String[] instanciaElemento;
			String scapedText;
			Boolean salir = false;
			int contIntentos = 0;

			while (!salir) {
				line = reader.readLine();
				if (line != null && !line.isEmpty()) {
					contIntentos = 0;
					instanciaElemento = line.split(regEx);

					if (instanciaElemento.length == 5) {

						// Se limpia la linea
						scapedText = "\"" + instanciaElemento[4].replaceAll("\"", "").replaceAll("—", "-") + "\"";

						// Se annade la linea al fichero temporal
						writer.write(instanciaElemento[0] + "," + instanciaElemento[1] + "," + instanciaElemento[2]
								+ "," + instanciaElemento[3] + "," + scapedText + "\n");
					}

				} else {
					contIntentos++;
					if (contIntentos == 3) {
						salir = true;
					}
				}

			}

			reader.close();
			writer.close();

			Instances data = cargarCSV(tempFile);
			exportarARFF(data, pathDestino);
		}

		catch (FileNotFoundException e) {
			System.err.println("No se ha encontrado el fichero .csv en la ruta especificada: " + pathOrigen);
			throw e;
		} catch (Exception e) {
			System.err.println("Se ha producido un error al convertir el CSV a ARFF");
			throw e;
		}

	}

	/**
	 * Dado un fichero csv, devuelve los datos contenidos en este
	 * 
	 * @param pFile
	 * @return Instances
	 * @throws Exception
	 */
	private Instances cargarCSV(File pFile) throws Exception {
		CSVLoader loader = new CSVLoader();
		loader.setSource(pFile);
		loader.setNoHeaderRowPresent(false);
		return loader.getDataSet();

	}
}