
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class GestorFichero {
	
	private static GestorFichero gestorFichero = new GestorFichero();
	private DataSource fichero = null;
	
	/**
	 * constructora privada 
	 */
	private GestorFichero() {
	}
	
	/**
	 * Patr�n singleton
	 * @return devuelve la instancia del patr�n singleton: gestorFichero
	 */
	public static GestorFichero getGestorFichero() {
		return gestorFichero;
	}
	
	/**
	 * Abre el fichero .arrf de la ruta especificada.
	 * @param dirFichero Path del fichero .arff
	 */
	private void abrirFichero(String dirFichero) {
		try {
			fichero = new DataSource(dirFichero);
		} catch (Exception ex) {
			System.out.println("Error: Revisar la direccion del fichero --> " + dirFichero);
		}
	}
	
	/**
	 * Carga las instancias del fichero .arff de la ruta especificada.
	 * @param dirFichero Path de d�nde se cargan las instancias
	 * @return Devuelve instancias que ha le�do del fichero
	 */
	public Instances cargarInstancias(String dirFichero) {
		try {
			abrirFichero(dirFichero);
			if (fichero != null) {
				// Se cargan las instancias y se define la clase de las instancias
				Instances instancias = fichero.getDataSet();
				return instancias;
			} else
				return null;
		} catch (Exception ex) {
			System.out.println("Error al cargar las instancias del fichero.");
			return null;
		}
	}
	
}
