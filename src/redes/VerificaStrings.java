package redes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by juan on 11/01/16.
 */
public class VerificaStrings {
    String pagina;

    VerificaStrings(String pagina){
        this.pagina = pagina;
    }

    boolean paginaContemBadWord(){
        String linha;
        try {
            BufferedReader in = new BufferedReader(new FileReader("a"));
            while ((linha = in.readLine()) != null){
                String[] badWords = linha.split(" ");
                for (int i = 0; i < badWords.length; i++){
                    if (pagina.contains(badWords[i])){
                        return false;
                    }
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
