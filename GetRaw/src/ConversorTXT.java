import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class ConversorTXT {

	public static ConversorTXT miConversor = new ConversorTXT();

	private ConversorTXT() {

	}

	public static ConversorTXT getConversor() {
		return miConversor;
	}

	public static void convertirTXTaARFF(String pRutaTXT, String pRutaARFF) {

		try {
			
			ArrayList<String> lineas = arrayDeLineas(pRutaTXT);
			Instances data = convertirAInstancias(lineas);
			exportarARFF(data, pRutaARFF);
			
		}

		catch (Exception e) {
			System.out.println("Se ha producido un error al convertir el TXT a ARFF");
		}

	}

	private static ArrayList<String> arrayDeLineas(String pRutaArchivo) throws Exception {

		BufferedReader b = new BufferedReader(new FileReader(pRutaArchivo));
		ArrayList<String> aDevolver = new ArrayList<String>();
		Iterator<String> itr = b.lines().iterator();
		String lineaAct;

		while (itr.hasNext()) {
			lineaAct = itr.next();
			aDevolver.add(lineaAct);
		}

		b.close();
		return aDevolver;

	}

	private static String getClase(String pLinea) {
		return pLinea.split("\t")[0];
	}

	private static String getTexto(String pLinea) {
		int i = 0;
		while (pLinea.charAt(i) != '\t') {
			i++;
		}
		return pLinea.substring(i + 1).replace("'", "\'");
	}
	
	private static Instances convertirAInstancias(ArrayList<String> pInstancias) {
		
		ArrayList<String> valores = new ArrayList<String>();
		valores.add("spam");
		valores.add("ham");		
		Attribute texto = new Attribute("text", true);
		Attribute clase = new Attribute("class", valores);	
		ArrayList<Attribute> atributos = new ArrayList<Attribute>();	
		atributos.add(texto);
		atributos.add(clase);
		Instances aDevolver = new Instances ("spam", atributos, pInstancias.size());
		
		for (String ins:pInstancias) {
			Instance i = new DenseInstance(2);
			i.setValue(texto, getTexto(ins));
			i.setValue(clase, getClase(ins));
			aDevolver.add(i);
		}
		
		return aDevolver;
	}
	
	private static void exportarARFF(Instances pData, String pRuta) throws Exception{
		ArffSaver saver = new ArffSaver();
		saver.setInstances(pData);
		saver.setFile(new File(pRuta));
		saver.writeBatch();
	}
	
}