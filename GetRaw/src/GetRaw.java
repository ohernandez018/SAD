import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.TextDirectoryLoader;

public class GetRaw {

	public static void main(String[] args) {

		if (args.length == 2) {

			File fichero = new File(args[0]);

			if (fichero.isDirectory()) {
				convertirDirectorio(args[0], args[1]);
			} else {
				convertirRaw(args[0], args[1]);
			}

		} else {
			System.out.println("=== LISTA DE ARGUMENTOS ===");
			System.out.println("1. Path del directorio que incluye los ficheros raw");
			System.out.println("2. Path de destino donde se guardará el fichero .arff");
			System.out.println("===========================");
		}

	}

	private static void guardarDatos(String path, Instances data) throws Exception {
		try {

			// guardar datos
			File fi = new File(path);
			ArffSaver saver = new ArffSaver();
			saver.setInstances(data);
			saver.setFile(fi);
			saver.writeBatch();

		} catch (Exception e) {
			System.err.print("Error al guardar los datos en un fichero .arff");
			e.printStackTrace();
			throw e;
		}
	}

	private static void convertirDirectorio(String pathDirectorio, String pathDestino) {
		try {

			// Se carga el directorio
			File file = new File(pathDirectorio);
			TextDirectoryLoader directoryLoader = new TextDirectoryLoader();
			directoryLoader.setSource(file);

			// Se obtienen los datos
			Instances data = directoryLoader.getDataSet();

			// Se guardan en un fichero arff
			guardarDatos(pathDestino, data);

		} catch (Exception e) {
			System.err.println("Se ha producido un error en la conversion del directorio.");
			e.printStackTrace();
		}

	}

	private static void convertirRaw(String pathFichero, String pathDestino) {

		File fichero = new File(pathFichero);
		String path = fichero.getPath();

		if (path.endsWith(".txt")) {
			try {
				// TODO
			} catch (Exception e) {
				System.err.println("Se ha producido un error en la conversion del fichero .txt");
				e.printStackTrace();
			}

		} else if (path.endsWith(".csv")) {
			try {
				CSVLoader csvLoader = new CSVLoader();
				csvLoader.setSource(fichero);

				csvLoader.setNoHeaderRowPresent(false);

				// Se obtienen los datos
				Instances data = csvLoader.getDataSet();

				// Se guardan en un fichero arff
				guardarDatos(pathDestino, data);
			} catch (Exception e) {
				System.err.println("Se ha producido un error en la conversion del fichero .csv.");
				e.printStackTrace();
			}
		}

	}

}
