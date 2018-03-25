public class ConversorCSV implements IConversor {

	private static ConversorCSV miConversorCSV = new ConversorCSV();

	private ConversorCSV() {
	}

	public static ConversorCSV getConversorCSV() {
		return miConversorCSV;
	}

	@Override
	public void convertir(String pathOrigenCSV, String pathDestinoARFF) throws Exception {

		// TODO
		System.err.println("Todavía estoy sin implementar!!!");

	}

}
