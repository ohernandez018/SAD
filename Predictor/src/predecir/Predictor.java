package predecir;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class Predictor {
	private static Classifier clasificador;
	private static FileWriter resultados = null;
	private static PrintWriter pw = null;

	private static Predictor predictor = new Predictor();

	private Predictor() {
	}

	public static Predictor getPredictor() {
		return predictor;
	}

	/**
	 * 
	 * @param pRutaModelo path del modelo
	 * @param pDatos conjunto de datos (Instances)
	 * @param pRutaResultados path donde guardar los resultados de las predicciones
	 */
	public void predecir(String pRutaModelo, Instances pDatos, String pRutaResultados) {

		try {

			pDatos.setClassIndex(pDatos.numAttributes() - 1);

			/***********************************
			 * IMPORTAR MODELO
			 ***********************************/
			clasificador = (Classifier) SerializationHelper.read(pRutaModelo);

			/***********************************
			 * HACER PREDICCIONES
			 ***********************************/
			resultados = new FileWriter(pRutaResultados);
			pw = new PrintWriter(resultados);
			int j = 0;
			for (int i = 0; i < pDatos.numInstances(); i++) {
				pw.println("ID: " + pDatos.instance(i).value(0) + ", actual: "
						+ pDatos.classAttribute().value((int) pDatos.instance(i).classValue()) + ", predicción: "
						+ pDatos.classAttribute().value((int) clasificador.classifyInstance(pDatos.instance(i))));
				j++;
			}
			resultados.close();
			System.out.println(" " + j + " instancias predecidas");
		} catch (FileNotFoundException f) {
			System.err.println("Error al cargar el modelo");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error al predecir instancias");
		}
	}
}
