import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;

public interface IConversor {

	/**
	 * Dado una ruta de origen y una de destino, convierte lo que haya en la ruta de
	 * origen a fichero .arff en la ruta de destino.
	 * 
	 * @param pathOrigen
	 * @param pathDestino
	 * @throws Exception
	 */
	void convertir(String pathOrigen, String pathDestino) throws Exception;

	/**
	 * Genera un fichero .arff en la ruta indicada.
	 * 
	 * @param pData
	 * @param pRuta
	 * @throws Exception
	 */
	default void exportarARFF(Instances pData, String pRuta) throws Exception {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(pData);
		saver.setFile(new File(pRuta));
		saver.writeBatch();
	}

}
