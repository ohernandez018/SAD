package predecir;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

public class Predictor {
	private static Classifier clasificador;
	private static FileWriter resultados = null;
	private static PrintWriter pw = null;

	public void predecir(String pRutaModelo, Instances pDatos, String pRutaResultados) {

		try {

			pDatos.setClassIndex(this.buscarClase(pDatos));

			/***********************************
			 * IMPORTAR MODELO
			 ***********************************/
			clasificador = (Classifier) SerializationHelper.read(pRutaModelo);

			/***********************************
			 * HACER PREDICCIONES
			 ***********************************/
			int j = 0;
			for (int i = 0; i < pDatos.numInstances(); i++) {
				System.out.print("ID: " + pDatos.instance(i).value(0));
				System.out.print(", actual: " + pDatos.classAttribute().value((int) pDatos.instance(i).classValue()));
				System.out.println(", predicción: "
						+ pDatos.classAttribute().value((int) clasificador.classifyInstance(pDatos.instance(i))));
				j++;
			}
			System.out.println(" " + j + " instancias predecidas");
		} catch (FileNotFoundException f) {
			System.out.println("Error al cargar el modelo");
		} catch (Exception e) {
			System.out.println("Error al predecir instancias");
		}
	}

	private int buscarClase(Instances data) {
		int a = -1;
		try {
			for (int i = 0; i < data.numAttributes(); i++) {
				if (data.attribute(i).name().equals("class")) {
					a = i;
					break;
				}
			}
			return a;
		} catch (Exception e) {
			System.out.println("Error al establecer la clase");
			return a;
		}

	}
}
