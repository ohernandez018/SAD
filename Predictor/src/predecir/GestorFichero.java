package predecir;

import java.io.FileNotFoundException;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class GestorFichero {
	private static GestorFichero gestorFichero = new GestorFichero();
	private DataSource fichero = null;

	private GestorFichero() {
	}

	public static GestorFichero getGestorFichero() {
		return gestorFichero;
	}

	/**
	 * Abre el fichero que se encuentra en la ruta indicada.
	 * 
	 * @param dirFichero
	 */
	private void abrirFichero(String dirFichero) throws Exception {
		try {

			fichero = new DataSource(dirFichero);

		} catch (FileNotFoundException ex) {
			System.err.println("Error: Revisar la direccion del fichero --> " + dirFichero);
			throw ex;
		} catch (Exception ex) {
			System.out.println("Se ha producido un error al obtener los datos del fichero.");
			throw ex;
		}
	}
	/**
	 * Carga las instancias y establece la clase del fichero arff de la ruta
	 * indicada.
	 * 
	 * @param dirFichero path del .txt
	 * @return devuelve el conjunto de instancias en formato .arff
	 * @throws Exception si algo falla devuelve el error producido.
	 */
	public Instances cargarInstancias(String dirFichero) throws Exception {
		try {

			abrirFichero(dirFichero);
			if (fichero != null) {
				// Se cargan las instancias y se define la clase de las instancias
				Instances instancias = fichero.getDataSet();
				instancias.setClassIndex(instancias.numAttributes() - 1);
				return instancias;

			} else {
				throw new Exception();
			}

		} catch (Exception ex) {
			System.err.println("Error al cargar las instancias del fichero.");
			throw ex;
		}
	}
}
