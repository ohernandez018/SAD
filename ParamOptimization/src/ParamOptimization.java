import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

public class ParamOptimization {

	private static Instances data;
	private static MultilayerPerceptron multilayerPerceptron;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		String a = "C:\\Users\\Pablo\\Desktop\\iris.arff";
		String b = "C:\\Users\\Pablo\\Desktop\\calidad.txt";
		String c = "C:\\Users\\Pablo\\Desktop\\modelo.model";
		

		try {
			
			data = new DataSource(a).getDataSet();			
			data.setClassIndex(data.numAttributes()-1);
			

			multilayerPerceptron = new MultilayerPerceptron();

			double fMeasureOpt = Double.MIN_VALUE;
			double learningRateOpt = Double.MIN_VALUE;
			double momentumOpt = Double.MIN_VALUE;
			int trainingTimeOpt = Integer.MIN_VALUE;
			boolean decayOpt = false;
			String hiddenLayersOpt = "";

			ArrayList<String> hiddenlayers = new ArrayList<String>();
			hiddenlayers.add("a");
			hiddenlayers.add("i");
			hiddenlayers.add("o");
			hiddenlayers.add("t");

			multilayerPerceptron.setAutoBuild(true);

			multilayerPerceptron.setNominalToBinaryFilter(false);
			multilayerPerceptron.setNormalizeAttributes(false);
			multilayerPerceptron.setNormalizeNumericClass(false);
			// multilayerPerceptron.setGUI(true);

			for (int i = 0; i < hiddenlayers.size(); i++) {

				// loop para elegir el mejor HIDDEN LAYER. En busca del f-measure mas grande.
				multilayerPerceptron.setHiddenLayers(hiddenlayers.get(i));

				for (double rate = 0.2; rate <= 1; rate += 0.2) {
					multilayerPerceptron.setLearningRate(rate);

					for (double momentum = 0.2; momentum <= 1; momentum += 0.2) {
						multilayerPerceptron.setMomentum(momentum);

						for (int trainingtime = 5; trainingtime < 50; trainingtime += 5) {

							multilayerPerceptron.setTrainingTime(trainingtime);

							for (int decay = 0; decay < 2; decay++) {
								multilayerPerceptron.setDecay(decay < 1);

								Evaluation evaluation = evaluateHoldOut(0.70);

								// Comparar con la f-measure de la clase minoritaria
								double fMeasure = evaluation.fMeasure(claseMinoritaria(data));

					
								if (fMeasure > fMeasureOpt) {
									fMeasureOpt = fMeasure;
									hiddenLayersOpt = hiddenlayers.get(i);
									learningRateOpt = rate;
									momentumOpt = momentum;
									trainingTimeOpt = trainingtime;
									decayOpt = (decay < 1);

								}
							}
						}
					}
				}
			}

			System.out.println("Parametros optimos:\nhiddenlayers:" + hiddenLayersOpt + "\nrate:" + learningRateOpt
					+ "\nmomentum:" + momentumOpt + "\ntrainigtime:" + trainingTimeOpt + "\nDecay:" + decayOpt);

			MultilayerPerceptron multilayerPerceptronOpt = createOptimizedClasifier(learningRateOpt, momentumOpt,
					trainingTimeOpt, decayOpt, hiddenLayersOpt);

			exportModel(c, multilayerPerceptronOpt);
			
			
			evaluarCalidadYExportar(multilayerPerceptronOpt, b, data);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	/**
	 * Este método evalúa por 10-fold y por la evaluación no honesta las pInstances con el clasificador pMultilayerPerceptronOpt y exporta los resultado en la ruta pRuta
	 * 
	 * @param multilayerPerceptronOpt
	 * @param pRuta
	 * @param pInstancias
	 * @throws Exception
	 */
	private static void evaluarCalidadYExportar(MultilayerPerceptron pMultilayerPerceptronOpt, String pRuta, Instances pInstancias) throws Exception{
		
		Evaluation evaluator;	
		
		evaluator = new Evaluation(pInstancias);
		evaluator.crossValidateModel(pMultilayerPerceptronOpt, pInstancias, 10, new Random(1));
		escribirLinea("10 FOLD CROSS VALIDATION", pRuta);
		escribirLinea(evaluator.toSummaryString()+"F-measure de la clase minoritaria--> "+evaluator.fMeasure(claseMinoritaria(pInstancias)), pRuta);
		escribirLinea(evaluator.toClassDetailsString(), pRuta);
		escribirLinea(evaluator.toMatrixString(), pRuta);
		
		escribirLinea("\n\n", pRuta);

		evaluator = new Evaluation(pInstancias);
		evaluator.evaluateModel(pMultilayerPerceptronOpt, pInstancias);
		escribirLinea("NO HONESTA", pRuta);
		escribirLinea(evaluator.toSummaryString()+"F-measure de la clase minoritaria--> "+evaluator.fMeasure(claseMinoritaria(pInstancias)), pRuta);
		escribirLinea(evaluator.toClassDetailsString(), pRuta);
		escribirLinea(evaluator.toMatrixString(), pRuta);
				
	}
	
	
	/**
	 * Este método escribe en la ruta pRuta la línea de texto pTexto
	 * 
	 * @param pTexto
	 * @param pRuta
	 * @throws Exception
	 */
	public static void escribirLinea(String pTexto, String pRuta) throws Exception {
		BufferedWriter f = new BufferedWriter(new FileWriter(pRuta, true));
		f.write("\n" + pTexto);
		f.close();
	}

	
	/**
	 * Este método crea y devuelve el clasificador óptimo que construye con los parámetros indicados.
	 * 
	 * @param learningRateOpt
	 * @param momentumOpt
	 * @param trainingTimeOpt
	 * @param decayOpt
	 * @param hiddenLayersOpt
	 * @return
	 * @throws Exception
	 */
	private static MultilayerPerceptron createOptimizedClasifier(double learningRateOpt, double momentumOpt,
			int trainingTimeOpt, boolean decayOpt, String hiddenLayersOpt) throws Exception{

		MultilayerPerceptron aDevolver = new MultilayerPerceptron();
		
		aDevolver.setLearningRate(learningRateOpt);
		aDevolver.setMomentum(momentumOpt);
		aDevolver.setTrainingTime(trainingTimeOpt);
		aDevolver.setDecay(decayOpt);
		aDevolver.setHiddenLayers(hiddenLayersOpt);
		
		aDevolver.buildClassifier(data);

		return aDevolver;
	}

	/**
	 * Este método genera y exporta en pPath el modelo generado a partir de pMultilayerPerceptronOpt
	 * 
	 * @param path
	 * @param multilayerPerceptronOpt
	 * @throws Exception
	 */
	private static void exportModel(String pPath, MultilayerPerceptron pMultilayerPerceptronOpt) throws Exception {

		SerializationHelper.write(pPath, pMultilayerPerceptronOpt);
	}

	/**
	 * Este método realiza la evaluación hold-out con el porcentaje especificado en pPercentage
	 * 
	 * @param pPercentage
	 * @return
	 * @throws Exception
	 */
	private static Evaluation evaluateHoldOut(double pPercentage) throws Exception {

		Evaluation ev;

		data.randomize(new Random(1));
		RemovePercentage removePercentage = new RemovePercentage();
		removePercentage.setPercentage(pPercentage);

		removePercentage.setInvertSelection(true);
		removePercentage.setInputFormat(data);
		Instances train = Filter.useFilter(data, removePercentage);

		removePercentage.setInvertSelection(false);
		removePercentage.setInputFormat(data);
		Instances test = Filter.useFilter(data, removePercentage);
		
		
		multilayerPerceptron.buildClassifier(train);

		ev = new Evaluation(train);
		ev.evaluateModel(multilayerPerceptron, test);

		return ev;

	}
	
	/**
	 * Este método devuelve el índice de la clase minoritaria de pDatos
	 * 
	 * @param pDatos
	 * @return
	 */
	private static int claseMinoritaria(Instances pDatos) {
		int claseMenor = 0;
		int instanciasMenor = pDatos.numInstances();

		for (int i = 0; i < pDatos.numClasses(); i++) {
			if (pDatos.attributeStats(pDatos.classIndex()).nominalCounts[i] < instanciasMenor) {
				instanciasMenor = pDatos.attributeStats(pDatos.classIndex()).nominalCounts[i];
				claseMenor = i;
			}
		}

		return claseMenor;
	}

}
