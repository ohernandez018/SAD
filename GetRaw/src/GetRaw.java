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

					ConversorTXT.getConversorTXT().convertir(pathOrigen, pathDestino);

				} else if (pathOrigen.endsWith(".csv")) {

					ConversorCSV.getConversorCSV().convertir(pathOrigen, pathDestino);

				}

				System.out.println("Transformación completada con éxito!");

			} else {
				
				System.out.println("=== GetRaw ===");
				System.out.println(
						"Este programa transforma los datos (de un directorio, de un CSV o de un TXT) a ARFF");
				

				System.out.println("\n=== Precondicion ===");
				System.out.println("Entrara como parametro un directorio, un archivo .csv o un archivo .txt válidos y existentes");

				System.out.println("\n=== Poscondicion ===");
				System.out.println("El programa genera un .arff con los datos");

				System.out.println("\n=== Lista de argumentos ===");
				System.out.println("1. Path del origen (raw)");
				System.out.println("2. Path del destino (arff)");
				

				System.out.println("\n=== Ejemplo de uso ===");
				System.out.println("java -jar GetRaw.jar /home/euiti/datos.csv archivo.arff");
				System.out.println("===========================");
			}

		} catch (Exception e) {

		}

	}

}
