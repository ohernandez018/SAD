import java.io.FileWriter;
import java.io.PrintWriter;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class GetBaselineModel {

	private static FileWriter report = null;
	private static PrintWriter pw = null;

	/**
	 * @param args son los siguientes: 
	 * args[0] : ruta del .arff para entrenar con NaiveBayes
	 * args[1] : porcentaje que se quiere aplicar en la evaluaion Hold-Out
	 * args[2] : número de particiones del k-fold cross validation.
	 * args[3] : ruta guardar el modelo y la calidad estimada de la evaluacion realizada.
	 * 
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 4) {
				Instances datos = GestorFichero.getGestorFichero().cargarInstancias(args[0]);
				
				// Baseline NaiveBayes
				NaiveBayes clasificador = new NaiveBayes();
				Evaluacion.getEvaluacion().setClasificador(clasificador);
				Evaluacion.getEvaluacion().setDatos(datos);
				Evaluacion.getEvaluacion().setClaseDatos();

				report = new FileWriter(args[3] + "/NaiveBayes_CalidadEstimada.txt");
				pw = new PrintWriter(report);

				Evaluation  evaluacion = Evaluacion.getEvaluacion().noHonesta();
				pw.println(Evaluacion.getEvaluacion().detalles(evaluacion, "NO HONESTO"));
				
				evaluacion = Evaluacion.getEvaluacion().holdOut(Double.parseDouble(args[1]));
				pw.println(Evaluacion.getEvaluacion().detalles(evaluacion, "HOLD OUT"));
				
				evaluacion = Evaluacion.getEvaluacion().kFoldCrossValidation(Integer.parseInt(args[2]));
				pw.println(Evaluacion.getEvaluacion().detalles(evaluacion, "K-FOLD CROSS VALIDATION"));

				report.close();

				Evaluacion.getEvaluacion().exportarModelo(args[3]+"/NaiveBayes.model");

			} else {
				System.out.println("================= GetBaselineModel =================");
				System.out.println(
						"Con este software se obtiene la estimación de la calidad del modelo Naive Bayes.\n"
						+ "El conjunto de datos tiene que ser el mismo al que se le va a aplicar el modelo"
						+ "Multilayer Perceptron.\n");
				System.out.println("********** PARÁMETROS NECESARIOS **********");
				System.out.println("1. Ruta del fichero de datos (.arff)");
				System.out.println("2. Porcentaje para Hold Out");
				System.out.println("3. Número de particiones del k-fold Cross Validation");
				System.out.println("4. Ruta para guardar los resultados de la evaluación y el modelo");
			}
		} catch (

		Exception e) {
			System.out.println("Los parametros no son correctos.");
		}
	}
}