import java.io.File;

public class GetRaw {

	public static void main(String[] args) {
		try {

			if (args.length == 2) {

				String pathOrigen = args[0];
				String pathDestino = args[1];

				if (new File(pathOrigen).isDirectory()) {

					ConversorDirectorio.getConversorDirectorio().convertir(pathOrigen, pathDestino);

				} else if (pathOrigen.endsWith(".txt")) {

					ConversorTXT.getConversor().convertir(pathOrigen, pathDestino);

				} else if (pathOrigen.endsWith(".csv")) {

					// TODO ConversorCSV

				}

				System.out.println("Transformación completada con éxito!");

			} else {
				System.out.println("=== LISTA DE ARGUMENTOS ===");
				System.out.println("1. Path del origen (raw)");
				System.out.println("2. Path del destino (arff)");
				System.out.println("===========================");
			}

		} catch (Exception e) {

		}

	}

}
