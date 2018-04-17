import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.filters.unsupervised.instance.RemovePercentage;

public class Evaluacion {

	private static Evaluacion evaluacion = new Evaluacion();
	private static int SEED = 1;
	private static Classifier clasificador;
	private static Instances datos;

	/**
	 * Constructora privada
	 */
	private Evaluacion() {

	}

	/**
	 * Patrón singleton MAE Evaluacion 
	 * @return devuelve la instancia del patrón singleton: evaluacion
	 */
	public static Evaluacion getEvaluacion() {
		return evaluacion;
	}

	/**
	 * Establece el clasificador en la MAE.
	 * 
	 * @param pClasificador de tipo Classifier.
	 */
	public void setClasificador(Classifier pClasificador) {
		clasificador = pClasificador;
	}

	/**
	 * Estable el conjunto en la MAE.
	 * 
	 * @param pDatos
	 *            de tipo Instances.
	 */
	public void setDatos(Instances pDatos) {
		datos = pDatos;
	}

	/**
	 * Establece la clase al conjunto de datos de la MAE.
	 * La clase tiene que estar en primera posición.
	 */
	public void setClaseDatos() {
		datos.setClassIndex(0);
	}

	/**
	 * Evaluación Hold-Out proporciona un resultado más realista.
	 * 
	 * @param pPercentageSplit
	 *            Porcentaje para hacer la partición del train y dev.
	 * @return Devuelve la Evaluación Hold-Out.
	 */
	public Evaluation holdOut(double pPercentageSplit) {
		Evaluation evaluation = null;
		try {
			datos.randomize(new Random(SEED));
			RemovePercentage remove = new RemovePercentage();
			remove.setPercentage(pPercentageSplit);

			remove.setInputFormat(datos);
			remove.setInvertSelection(true);
			Instances trainData = RemovePercentage.useFilter(datos, remove);

			remove.setInputFormat(datos);
			remove.setInvertSelection(false);
			Instances testData = RemovePercentage.useFilter(datos, remove);

			clasificador.buildClassifier(trainData);

			evaluation = new Evaluation(trainData);
			evaluation.evaluateModel(clasificador, testData);
			return evaluation;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("ERROR al evaluar Hold Out");
			return null;
		}
	}

	/**
	 * Evaluación No Honesta proporciona un resultado optimista no realista.
	 * 
	 * @return Devuelve la evaluación No Honesta
	 */
	public Evaluation noHonesta() {
		try {
			clasificador.buildClassifier(datos);

			Evaluation evaluation = new Evaluation(datos);
			evaluation.evaluateModel(clasificador, datos);
			return evaluation;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("ERROR al evaluar No Honesto");
			return null;
		}
	}

	/**
	 * Evaluación K-fold Cross Validation proporciona un resultado más realista.
	 * 
	 * @param pK
	 *            Número de particiones del K-fold Cross Validation.
	 * @return Devuelve la evaluación K-fold Cross Validation.
	 */
	public Evaluation kFoldCrossValidation(int pK) {
		try {
			Evaluation evaluation = new Evaluation(datos);
			evaluation.crossValidateModel(clasificador, datos, pK, new Random(SEED));
			return evaluation;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("ERROR al evaluar k-Fold Cross Validation");
			return null;
		}
	}

	/**
	 * Exporta el modelo entrenado con todos los datos.
	 * @param pRutaDestino Path para exportar el fichero .model
	 */
	public void exportarModelo(String pRutaDestino) {
		try {
			clasificador.buildClassifier(datos);
			SerializationHelper.write(pRutaDestino, clasificador);
			System.out.println("Modelo exportado con éxito");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("ERROR al exportar el modelo");
		}
	}

	/**
	 * Crea String con todos los detalles del pEval que se le pasa por parámetro.
	 * Para funcionar correctamente se le pasa un pEval que ya haya evaluado un
	 * clasificador.
	 * 
	 * @param pEval
	 *            de tipo Evaluation.
	 * @param pNombreEval
	 *            Título que se le da a la evaluación. Por ejemplo Hold-Out.
	 * @return Devuelve un String con todos los datos correspondientes a la
	 *         evaluación que se le ha pasado como parámetro.
	 */
	public String detalles(Evaluation pEval, String pNombreEval) {
		try {
			int claseMinor =  claseMinoritaria();
			String detalles = "*********************** " + pNombreEval + " ***********************\n";
			detalles += pEval.toSummaryString() + "\n";
			detalles += "Minority class " + datos.attribute(claseMinor).name() + "			" + pEval.fMeasure(claseMinor) + "\n";
			detalles += pEval.toClassDetailsString() + "\n";
			detalles += pEval.toMatrixString() + "\n";
			return detalles;
		} catch (Exception ex) {
			System.err.println("ERROR al guardar los detalles");
			return null;
		}
	}

	private static int claseMinoritaria() {
		int claseMenor = 0;
		int instanciasMenor = datos.numInstances();

		for (int i = 0; i < datos.numClasses(); i++) {
			if (datos.attributeStats(datos.classIndex()).nominalCounts[i] < instanciasMenor) {
				instanciasMenor = datos.attributeStats(datos.classIndex()).nominalCounts[i];
				claseMenor = i;
			}
		}
		return claseMenor;
	}

}
