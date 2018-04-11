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

	private Evaluacion() {

	}

	public static Evaluacion getEvaluacion() {
		return evaluacion;
	}

	public void setClasificador(Classifier pClasificador) {
		clasificador = pClasificador;
	}

	public void setDatos(Instances pDatos) {
		datos = pDatos;
	}

	public void setClaseDatos() {
		datos.setClassIndex(this.buscarClase(datos));
	}

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

			evaluation = new Evaluation(testData);
			evaluation.evaluateModel(clasificador, testData);
			return evaluation;
		} catch (Exception e) {
			System.out.println("ERROR al evaluar Hold Out");
			return null;
		}
	}

	public Evaluation noHonesto() {
		try {
			clasificador.buildClassifier(datos);

			Evaluation evaluation = new Evaluation(datos);
			evaluation.evaluateModel(clasificador, datos);
			return evaluation;
		} catch (Exception e) {
			System.out.println("ERROR al evaluar No Honesto");
			return null;
		}
	}

	public Evaluation kFoldCrossValidation(int pK) {
		try {
			clasificador.buildClassifier(datos);

			Evaluation evaluation = new Evaluation(datos);
			evaluation.crossValidateModel(clasificador, datos, pK, new Random(SEED));
			return evaluation;
		} catch (Exception e) {
			System.out.println("ERROR al evaluar k-Fold Cross Validation");
			return null;
		}
	}

	public void exportarModelo(String pRutaDestino) {
		try {
			clasificador.buildClassifier(datos);
			SerializationHelper.write(pRutaDestino, clasificador);
			System.out.println("Modelo exportado con éxito");			
		} catch (Exception e) {
			System.out.println("ERROR al exportar el modelo");
		}
	}

	public String detalles(Evaluation pEval, String pNombreEval) {
		try {
			String detalles = "*********************** " + pNombreEval + " ***********************\n";
			detalles += pEval.toSummaryString() + "\n";
			detalles += pEval.toClassDetailsString() + "\n";
			detalles += pEval.toMatrixString() + "\n";
			return detalles;
		} catch (Exception ex) {
			System.out.println("Error al guardar los detalles");
			return null;
		}
	}

	private int buscarClase(Instances data) {
		int a = -1;
		for (int i = 0; i < data.numAttributes(); i++) {
			if (data.attribute(i).name().equals("class")) {
				a = i;
				break;
			}
		}
		return a;
	}
}
