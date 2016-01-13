

public class Proxy {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Uso: java Proxy <porta> ou Proxy <porta> <arquivo>");
			System.exit(1);
		}
		
		boolean verificar;
		
		if (args.length == 1)
			verificar = false;
		else verificar = true;
		
		int portNumber = Integer.parseInt(args[0]);		
		Servidor server;
		
		if (verificar)
			server = new Servidor(portNumber, args[1], verificar);
		else server = new Servidor(portNumber, verificar);
		
		try {
			server.executaServidor();
		}
		catch (Exception e) {
			System.out.println("Ocorreu um erro.");
			System.out.println("Servidor encerrado");
		}

	}

}
