import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;

public interface IConversor {

	void convertir(String pathOrigen, String pathDestino) throws Exception;

	default void exportarARFF(Instances pData, String pRuta) throws Exception {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(pData);
		saver.setFile(new File(pRuta));
		saver.writeBatch();
	}

}
