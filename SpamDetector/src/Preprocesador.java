import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.unsupervised.attribute.Remove;

public class Preprocesador {

	private static Preprocesador miPreprocesador = new Preprocesador();

	private Preprocesador() {

	}

	public static Preprocesador getPreprocesador() {
		return miPreprocesador;
	}

	public void convertirAFormatoArff(String pathOrigen, String pathDestino) {
		try {

			// Se carga el directorio
			File file = new File(pathOrigen);
			TextDirectoryLoader directoryLoader = new TextDirectoryLoader();
			directoryLoader.setSource(file);

			// Se obtienen los datos
			Instances data = directoryLoader.getDataSet();
			data.setClassIndex(data.numAttributes() - 1); // Se define la clase

			String valorClase = data.instance(0).classAttribute().value(0);

			if (!valorClase.equals("pos") && !valorClase.equals("neg")) {

				Remove remove = new Remove();
				int[] indice = { 1 };
				remove.setAttributeIndicesArray(indice);
				// Atributo nominal class
				List<String> b = new ArrayList<String>();
				b.add("pos");
				b.add("neg");
				Attribute atributo = new Attribute("@@class@@", b);
				data.replaceAttributeAt(atributo, 1);

				for (Instance instancia : data) {
					instancia.setClassMissing();
				}
			}

			// Se guardan en un fichero arff
			file = new File(pathDestino);
			ArffSaver saver = new ArffSaver();
			saver.setInstances(data);
			saver.setFile(file);
			saver.writeBatch();

		} catch (IOException e) {
			System.out.println("Se ha producido un error en la conversion.");
			e.printStackTrace();
		}
	}

}
