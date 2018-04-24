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

	public void predecir(String pRutaModelo, Instances pDatos, String pRutaResultados) {

		try {

			pDatos.setClassIndex(pDatos.numAttributes()-1);

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
				pw.println("ID: " + pDatos.instance(i).value(0) + ", actual: " + pDatos.classAttribute().value((int) pDatos.instance(i).classValue()) + ", predicción: "
						+ pDatos.classAttribute().value((int) clasificador.classifyInstance(pDatos.instance(i))));
				j++;
			}
			resultados.close();
			System.out.println(" " + j + " instancias predecidas");
		} catch (FileNotFoundException f) {
			System.out.println("Error al cargar el modelo");
		} catch (Exception e) {
			System.out.println("Error al predecir instancias");
		}
	}
}
