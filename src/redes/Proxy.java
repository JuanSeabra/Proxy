package redes;

import java.io.IOException;

public class Proxy {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Uso: java proxy <porta>");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[0]);
		
		Servidor server = new Servidor(portNumber);
		try {
			server.executaServidor();
		} catch (IOException e) {			
			e.printStackTrace();
		}

	}

}
