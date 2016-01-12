package redes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Proxy {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Uso: java proxy <porta> <arquivo>");
			System.exit(1);
		}
		BufferedReader in = null;
		int portNumber = Integer.parseInt(args[0]);		
		Servidor server = new Servidor(portNumber, args[1]);
		try {
			server.executaServidor();
		} catch (IOException e) {			
			e.printStackTrace();
		}

	}

}
