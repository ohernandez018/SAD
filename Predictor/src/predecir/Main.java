package predecir;

import weka.core.Instances;

public class Main {
	
	/**
	 * args[0] Ruta del modelo clasificador (.model)
	 * args[1] Ruta de los datos a predecir
	 * args[2] Ruta para guardar los resultados de la predicción
	 * @param args
	 */
	
	public static void main(String[] args) {

		try {
			if (args.length == 3) {
				if (args[0].indexOf(".model") > -1) {
					Instances datosPredecir = GestorFichero.getGestorFichero().cargarInstancias(args[1]);
					Predictor.getPredictor().predecir(args[0], datosPredecir, args[2]);
				} else {
					System.out.println("El path del modelo es incorrecto.");
				}
			} else {
				System.out.println("********** PARÁMETROS NECESARIOS **********");
				System.out.println("1. Ruta del modelo clasificador (.model)");
				System.out.println("2. Ruta de los datos a predecir");
				System.out.println("3. Ruta para guardar los resultados de la predicción");
			}
		} catch (Exception e) {
			System.out.println("Los parametros no son correctos.");
		}
	}

}
