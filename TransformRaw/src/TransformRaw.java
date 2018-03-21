import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.unsupervised.instance.SparseToNonSparse;

public class TransformRaw {

	private final static String[] TYPE = { "-bow", "-tfidf" };
	private final static String[] TYPE2 = { "-s", "-ns" };

	public static void main(String[] args) {
		try {
			if (args.length == 3) {

				if (args[0].equals(TYPE[0]) || args[0].equals(TYPE[1])) {

					if (args[0].equals(TYPE2[0]) || args[0].equals(TYPE2[1])) {

						// TODO
						StringToWordVector swv = new StringToWordVector();
						swv.setLowerCaseTokens(true);

						if (args[0].equals(TYPE2[1])) {
							SparseToNonSparse sns = new SparseToNonSparse();
							Instances data = GestorFichero.getGestorFichero().cargarInstancias(args[2]);
							sns.setInputFormat(data);
							Filter.useFilter(data, sns);
						}

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
				System.out.println("===========================");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
