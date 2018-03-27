import java.io.File;
import java.io.FileNotFoundException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.TextDirectoryLoader;

public class ConversorDirectorio implements IConversor {

	private static ConversorDirectorio miConversorDirectorio = new ConversorDirectorio();

	private ConversorDirectorio() {
	}

	public static ConversorDirectorio getConversorDirectorio() {
		return miConversorDirectorio;
	}

	@Override
	public void convertir(String pathOrigenDirectorio, String pathDestinoARFF) throws Exception {
		try {

			System.out.println("Convirtiendo el directorio a .arff ...");
			// Se carga el directorio
			File file = new File(pathOrigenDirectorio);
			TextDirectoryLoader directoryLoader = new TextDirectoryLoader();
			directoryLoader.setSource(file);

			// Se obtienen los datos
			Instances data = directoryLoader.getDataSet();

			// Se guardan en un fichero arff
			File fi = new File(pathDestinoARFF);
			ArffSaver saver = new ArffSaver();
			saver.setInstances(data);
			saver.setFile(fi);
			saver.writeBatch();

		} catch (FileNotFoundException e) {
			System.err.println("No se ha encontrado el directorio: " + pathOrigenDirectorio);
			throw e;
		} catch (Exception e) {
			System.err.println("Se ha producido un error en la conversion del directorio.");
			throw e;

		}

	}

}
