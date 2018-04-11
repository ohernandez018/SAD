import java.io.FileWriter;
import java.io.PrintWriter;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class GetBaselineModel {

	private static FileWriter report = null;
	private static PrintWriter pw = null;

	/**
	 * 
	 * @param args
	 * args[0] -> ruta del .arff para entrenar con NaiveBayes
	 * args[1] -> porcentaje que se quiere aplicar en la evaluaion Hold-Out
	 * args[2] -> ruta guardar el modelo y la calidad estimada de la evaluacion realizada.
	 *    
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 3) {
				Instances datos = GestorFichero.getGestorFichero().cargarInstancias(args[0]);
				
				// Baseline NaiveBayes
				NaiveBayes clasificador = new NaiveBayes();
				Evaluacion.getEvaluacion().setClasificador(clasificador);
				Evaluacion.getEvaluacion().setDatos(datos);
				Evaluacion.getEvaluacion().setClaseDatos();

				report = new FileWriter(args[3] + "/NaiveBayes_CalidadEstimada.txt");
				pw = new PrintWriter(report);

				Evaluation evaluacion = Evaluacion.getEvaluacion().holdOut(Double.parseDouble(args[1]));
				pw.println(Evaluacion.getEvaluacion().detalles(evaluacion, "HOLD OUT"));

				evaluacion = Evaluacion.getEvaluacion().noHonesto();
				pw.println(Evaluacion.getEvaluacion().detalles(evaluacion, "NO HONESTO"));

				report.close();

				Evaluacion.getEvaluacion().exportarModelo(args[3]+"/NaiveBayes.model");

			} else {
				System.out.println("=== GetBaselineModel ===");
				System.out.println(
						"Con este software se obtiene la estimación de la calidad del clasificador NaiveBayes."
						+ "El conjunto de datos tiene que ser el mismo al que se le va a aplicar el clasificardor"
						+ "Multilayer Perceptron.");
				System.out.println("********** PARÁMETROS NECESARIOS **********");
				System.out.println("1. Ruta del fichero de datos (.arff)");
				System.out.println("2. Porcentaje para Hold Out");
				System.out.println("3. Ruta para guardar los resultados de la evaluación y el modelo");
			}
		} catch (

		Exception e) {
			System.out.println("Los parametros no son correctos.");
		}
	}
}