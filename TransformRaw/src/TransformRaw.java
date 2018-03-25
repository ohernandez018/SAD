import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.NonSparseToSparse;

public class TransformRaw {

	private final static String[] TYPE = { "-bow", "-tfidf" };
	private final static String[] SPARS = { "-s", "-ns" };

	public static void main(String[] args) {
		try {
			if (args.length == 4) {

				if (args[0].equals(TYPE[0]) || args[0].equals(TYPE[1])) {

					if (args[1].equals(SPARS[0]) || args[1].equals(SPARS[1])) {

						Instances data = GestorFichero.getGestorFichero().cargarInstancias(args[2]);

						// Tanto si es BoW como TF-IDF se aplica el StringToWordVector
						// sin tener en cuenta mayusculas y minisculas
						StringToWordVector stringToWordVector = new StringToWordVector();
						stringToWordVector.setLowerCaseTokens(true);
						// Para que establezca las veces que aparece cada palabra
						stringToWordVector.setOutputWordCounts(true);

						// Si ha escogido TF-IDF
						if (args[0].equals(TYPE[1])) {
							stringToWordVector.setIDFTransform(true);
							stringToWordVector.setTFTransform(true);
						}

						stringToWordVector.setInputFormat(data);
						Instances dataSTWV = Filter.useFilter(data, stringToWordVector);

						// Si el usuario quiere que sea Sparse (Disperso)
						if (args[1].equals(SPARS[0])) {
							NonSparseToSparse nonSparteToSparse = new NonSparseToSparse();
							nonSparteToSparse.setInputFormat(dataSTWV);
							dataSTWV = Filter.useFilter(dataSTWV, nonSparteToSparse);
						}

						guardarDatos(args[3], dataSTWV);

					} else {
						System.out.println("=== ERROR ===");
						System.out.println("2. Elegir entre SPARSE (-s) o NONSPARSE (-ns)");
					}

				} else {
					System.out.println("=== ERROR ===");
					System.out.println("1. Elegir entre BoW (-bow) o TF-IDF (-tfidf)");
				}

			} else {
				System.out.println("=== LISTA DE ARGUMENTOS ===");
				System.out.println("1. Elegir entre BoW (-bow) o TF-IDF (-tfidf)");
				System.out.println("2. Elegir entre SPARSE (-s) o NONSPARSE (-ns)");
				System.out.println("3. Fichero .arff");
				System.out.println("4. Ruta donde almacenar el nuevo fichero .arff");
				System.out.println("===========================");
			}
		} catch (Exception e) {
			e.printStackTrace();
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

		} catch (Exception e) {
			System.err.print("Error al guardar los datos en un fichero .arff");
			e.printStackTrace();
			throw e;
		}
	}

}
