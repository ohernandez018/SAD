
import java.io.File;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.FixedDictionaryStringToWordVector;

public class MakeCompatible {

	/**
	 * 
	 * @param args
	 *            args[0] path del train.arff 
	 *            args[1] path del test.arff o dev.arff
	 *            args[2] path del diccionario
	 *            args[3] path para guardar el nuevo fichero compatible
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		if (args.length == 4) {
			Instances train = GestorFichero.getGestorFichero().cargarInstancias(args[0]);
			Instances test = GestorFichero.getGestorFichero().cargarInstancias(args[1]);
			FixedDictionaryStringToWordVector filtro = new FixedDictionaryStringToWordVector();
			filtro.setDictionaryFile(new File(args[2]));
			filtro.setInputFormat(train);
			Instances newData = Filter.useFilter(test, filtro);
			guardarDatos(args[3], newData);

		} else {
			System.out.println("=== MakeCompatible ===");
			System.out.println(
					"Este programa hace compatible el fichero test.arff o dev.arff con el train.arff, eliminado los atributos que no estan en el train y generando un nuevo arff compatible.");
			System.out.println("Ademas permite dar como salida una representacion Sparse o NonSparse");

			System.out.println("\n=== Precondicion ===");
			System.out.println("Entrara como parametro un .arff valido y existente");

			System.out.println("\n=== Poscondicion ===");
			System.out.println("El programa genera un .arff con los mismos atributos que el train");

			System.out.println("\n=== Lista de argumentos ===");
			System.out.println("1. Path del train");
			System.out.println("2. Path del test o dev para MakeCompatible");
			System.out.println("3. Path para recoger el diccionario");
			System.out.println("4. Path para guardar el nuevo fichero arff");

			System.out.println("\n=== Ejemplo de uso ===");
			System.out.println("java -jar MakeCompatible.jar train.arff test.arff diccionario testCompatible.arff");
			System.out.println("===========================");
		}
	}

	private static void guardarDatos(String path, Instances data) throws Exception {
		try {
			// Guardar datos
			File fi = new File(path);
			ArffSaver saver = new ArffSaver();
			saver.setInstances(data);
			saver.setFile(fi);
			saver.writeBatch();
			System.out.print(".arff compatible guardado correctamente");

		} catch (Exception e) {
			System.err.print("Error al guardar los datos en un fichero .arff");
			throw e;
		}
	}
}
