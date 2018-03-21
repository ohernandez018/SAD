
public class Main {

	public static void main(String[] args) {

		if (args.length != 6) {

			// Convertimos el texto en formato arff
			Preprocesador.getPreprocesador().convertirAFormatoArff(args[0], args[1]);
			Preprocesador.getPreprocesador().convertirAFormatoArff(args[2], args[3]);
			Preprocesador.getPreprocesador().convertirAFormatoArff(args[4], args[5]);

			// Convertimos el atributo String del fichero arff
			Preprocesador.getPreprocesador().convertirAStringToWordVector(args[1]);
			Preprocesador.getPreprocesador().convertirAStringToWordVector(args[3]);
			Preprocesador.getPreprocesador().convertirAStringToWordVector(args[5]);

		} else {
			System.out.println("Parametros mal");
		}

	}

}
