import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.SparseToNonSparse;

public class TransformRaw {

	private final static String[] TYPE = { "-bow", "-tfidf" };
	private final static String[] SPARS = { "-s", "-ns" };

	/**
	 * Realiza las tranformaciones de los datos en funcion de los parametros
	 * indicados 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 5 && (args[0].equals(TYPE[0]) || args[0].equals(TYPE[1]))
					&& (args[1].equals(SPARS[0]) || args[1].equals(SPARS[1]))) {

				Instances data = GestorFichero.getGestorFichero().cargarInstancias(args[2]);
				data = transformBoWOrTFIDF(data, args[0], args[4]);

				// Si el usuario quiere que sea Sparse (Disperso)
				if (args[1].equals(SPARS[0])) {
					data = transformToSparse(data);
				}

				guardarDatos(args[3], data);

				System.out.println("La transformaci�n se ha completado correctamente!");

			} else {
				System.out.println("=== TransformRaw ===");
				System.out.println(
						"Este programa transforma el espacio de atributos del conjunto de entrenamiento a BoW o TF�IDF");
				System.out.println("Ademas permite dar como salida una representacion Sparse o NonSparse");

				System.out.println("\n=== Precondicion ===");
				System.out.println("Entrara como parametro un .arff valido y existente");

				System.out.println("\n=== Poscondicion ===");
				System.out.println("El programa genera un .arff con las transformaciones realizadas");

				System.out.println("\n=== Lista de argumentos ===");
				System.out.println("1. Elegir entre BoW (-bow) o TF-IDF (-tfidf)");
				System.out.println("2. Elegir entre SPARSE (-s) o NONSPARSE (-ns)");
				System.out.println("3. Fichero .arff");
				System.out.println("4. Ruta donde almacenar el nuevo fichero .arff");
				System.out.println("5. Ruta donde almacenar el diccionario");

				System.out.println("\n=== Ejemplo de uso ===");
				System.out.println("java -jar TransformRaw.jar -bow -s train.arff trainBOW.arff");
				System.out.println("===========================");
			}

		} catch (Exception e) {
			// Error
		}

	}

	/**
	 * Genera un fichero de instancias con formato .arff en la ruta indicada.
	 * 
	 * @param path
	 * @param data
	 * @throws Exception
	 */
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
			throw e;
		}
	}

	/**
	 * Transforma las instancias raw a formato BoW o TF-IDF.
	 * 
	 * @param data
	 * @param transformType
	 * @throws Exception
	 */
	private static Instances transformBoWOrTFIDF(Instances data, String transformType, String pathDiccionario) throws Exception {

		try {
			StringToWordVector stringToWordVector = new StringToWordVector();
			// Establece si se desea o no igualar mayusculas y minusculas
			stringToWordVector.setLowerCaseTokens(true);
			// Establece si se desea o no la frecuencia de aparicion de cada palabra en el
			// texto
			stringToWordVector.setOutputWordCounts(false);
			// Establece el numero de palabras que intenta mantener
			stringToWordVector.setWordsToKeep(1000000);

			// Si el tipo escogido es TF-IDF se le anaden estas opciones
			if (transformType.equals(TYPE[1])) {
				stringToWordVector.setIDFTransform(true);
				stringToWordVector.setTFTransform(true);
				stringToWordVector.setOutputWordCounts(true);
			}
			
			stringToWordVector.setInputFormat(data);
			//Guardamos el diccionario para poder hacer el test o dev compatible
			stringToWordVector.setDictionaryFileToSaveTo(new File(pathDiccionario));
			Instances dataSTWV = Filter.useFilter(data, stringToWordVector);
			
			
			
			return dataSTWV;

		} catch (Exception e) {
			System.err.print("La transformaci�n a " + transformType + " a fallado.");
			throw e;
		}

	}

	/**
	 * Transforma los valores de formato no disperso a formato disperso
	 * 
	 * @param data
	 * @throws Exception
	 */
	private static Instances transformToSparse(Instances data) throws Exception {
		try {
			// Se aplica el filtro para convertir las instances de NonSparse a Sparse
			SparseToNonSparse sparseToNonSparse = new SparseToNonSparse();
			sparseToNonSparse.setInputFormat(data);
			Instances dataSparse = Filter.useFilter(data, sparseToNonSparse);
			return dataSparse;

		} catch (Exception e) {
			System.err.print("La transformaci�n a SPARSE a fallado.");
			throw e;
		}

	}

}
