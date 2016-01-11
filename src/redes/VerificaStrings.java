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

    VerificaStrings(String pagina){
        this.pagina = pagina;
    }
    //retorna se a página html deve ou não ser retornada para o cliente
    public boolean paginaContemBadWord(){
        String linha; //linha arquivo badwords
        try {
            BufferedReader in = new BufferedReader(new FileReader("a"));// lê arquivo de badwords
            while ((linha = in.readLine()) != null){
                String[] badWords = linha.split(" ");
                for (int i = 0; i < badWords.length; i++){
                    if (pagina.contains(badWords[i])){
                        return true;
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
