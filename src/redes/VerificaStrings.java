package redes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 * Created by juan on 11/01/16.
 */
public class VerificaStrings {
    String pagina; // texto página html
    BufferedReader in;

    VerificaStrings(String pagina, String arquivo){
        this.pagina = pagina;
        try {
			this.in = new BufferedReader(new FileReader(arquivo));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
    //retorna se a página html contem ou não badwords
    public boolean paginaContemBadWord(){   
        try { 
            for (String line; (line = in.readLine()) != null;) {                                   	
                if (pagina.contains(line)){
                   return true;
                }                
        	}
            
        } catch (FileNotFoundException e){
            e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }   
        return false;
    }
}
