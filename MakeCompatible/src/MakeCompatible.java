
import java.util.ArrayList;
import java.util.Enumeration;

import weka.core.Attribute;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class MakeCompatible {

	/**
	 * 
	 * @param args
	 * args[0] path del train.arff
	 * args[1] path del test.arff o dev.arff
	 * args[2] path para guardar el nuevo fichero compatible
	 */
	public static void main(String[] args) {

		if (args.length == 3) {
			Instances train = GestorFichero.getGestorFichero().cargarInstancias(args[0]);
			Instances test = GestorFichero.getGestorFichero().cargarInstancias(args[1]);

			Enumeration<Attribute> headboardTrain = train.enumerateAttributes();
			Enumeration<Attribute> headboardTest = test.enumerateAttributes();

			ArrayList<Integer> attributes = new ArrayList<Integer>();

			int contTrain = 0;
			int contTest = 0;
			while (headboardTest.hasMoreElements()) {
				Attribute attributeTest = headboardTest.nextElement();

				if (headboardTrain.hasMoreElements()) {
					Attribute attributeTrain = headboardTrain.nextElement();

					if (!attributeTrain.name().equals(attributeTest.name())) {
						attributes.add(attributeTest.index());
						attributeTest = headboardTest.nextElement();
						while (!(attributeTrain.name().equals(attributeTest.name()))
								&& headboardTest.hasMoreElements()) {
							attributeTest = headboardTest.nextElement();
							attributes.add(attributeTest.index());
							contTest++;
						}
						contTest++;
					}
					contTrain++;
				} else {
					attributes.add(attributeTest.index());
				}
				contTest++;
			}
			int[] indexAttributes = new int[attributes.size()];
			for (int i = 0; i < attributes.size(); i++) {
				indexAttributes[i] = attributes.get(i);
			}

			Instances newInstances = removeAttributes(indexAttributes, test);
			GestorFichero.getGestorFichero().exportarARFF(newInstances, args[2]);

			System.out.println("Atributos diferentes " + attributes.size());
			System.out.println("El conjunto train tiene " + contTrain + " atributos");
			System.out.println("El conjunto test tiene " + contTest + " atributos");

		} else {
			System.out.println("=== MakeCompatible ===");
			System.out.println(
					"Este programa hace compatible el fichero test.arff o dev.arff con el train.arff, eliminado los atributos que no estan en el train y generando un nuevo arff compatible.");
			System.out.println("Ademas permite dar como salida una representacion Sparse o NonSparse");

			System.out.println("\n=== Precondicion ===");
			System.out.println("Entrara como parametro un .arff valido y existente");

			System.out.println("\n=== Poscondicion ===");
			System.out.println("El programa genera un .arff con los mismo atributos que el train");

			System.out.println("\n=== Lista de argumentos ===");
			System.out.println("1. Path del train para recoger la cabecera correcta");
			System.out.println("2. Path del test o dev para MakeCompatible");
			System.out.println("2. Path para guardar el nuevo fichero arff");

			System.out.println("\n=== Ejemplo de uso ===");
			System.out.println("java -jar MakeCompatible.jar train.arff test.arff testCompatible.arff");
			System.out.println("===========================");

		}
	}
	
	/***
	 * Este metodo aplica el filtro remove a unas instancias
	 * @param pAttributes que contiene los índices con los atributos a eliminar
	 * @param pInstances tiene las instancias de test o dev
	 * @return devuelve las instancias con los mismos atributos que el train
	 */
	private static Instances removeAttributes(int[] pAttributes, Instances pInstances) {
		try {
			Remove remove = new Remove();
			remove.setAttributeIndicesArray(pAttributes);
			remove.setInvertSelection(false);
			remove.setInputFormat(pInstances);
			return Filter.useFilter(pInstances, remove);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
