
import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;

public class GestorFichero {
	private static GestorFichero gestorFichero = new GestorFichero();
	private DataSource fichero = null;

	private GestorFichero() {
	}

	public static GestorFichero getGestorFichero() {
		return gestorFichero;
	}

	private void abrirFichero(String dirFichero) {
		try {
			fichero = new DataSource(dirFichero);
		} catch (Exception ex) {
			System.out.println("Error: Revisar la direccion del fichero --> " + dirFichero);
		}
	}

	public Instances cargarInstancias(String dirFichero) {
		try {
			abrirFichero(dirFichero);
			if (fichero != null) {
				// Se cargan las instancias y se define la clase de las instancias
				Instances instancias = fichero.getDataSet();
				instancias.setClassIndex(0);
				return instancias;
			} else
				return null;
		} catch (Exception ex) {
			System.out.println("Error al cargar las instancias del fichero.");
			return null;
		}
	}

	public void exportarARFF(Instances pData, String pRuta) {
		try {
			ArffSaver saver = new ArffSaver();
			saver.setInstances(pData);
			saver.setFile(new File(pRuta));
			saver.writeBatch();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}
