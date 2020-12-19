package com.example.macrocounter.UI.cifrado;

import java.util.Date;
import java.util.Random;

public class RandomCifrado {

    private final Random random;

    public RandomCifrado() {

        this.random = new Random(new Date().getTime());

    }

    public String cifrar(String msgToEncrypt){

        String encryptedMsg = "ERROR ENCRYPTING";
        int randomMethod = this.random.nextInt(4);

        switch (randomMethod){
            case 0:
                int cifradoValue = random.nextInt(60000);
                encryptedMsg = Cesar.encriptar(msgToEncrypt,cifradoValue)+"|"+cifradoValue +"0";
                break;
            case 1:
                encryptedMsg = CifradoPropio.cifrar(msgToEncrypt) +"1";
                break;
            case 2:
                encryptedMsg = Transposicion.cifrar(msgToEncrypt,false) +"2";

                break;
            case 3:
                try {
                    encryptedMsg = Vigerene.cifrar(msgToEncrypt) +"3";
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }

        return encryptedMsg;
    }

    public String descifrar(String msgToDecrypt){

        String encryptedMsg = "ERROR DECRYPTING";
        char charMethod = msgToDecrypt.charAt(msgToDecrypt.length()-1);
        int randomMethod = Integer.parseInt(charMethod+"");
        msgToDecrypt = msgToDecrypt.substring(0,msgToDecrypt.length()-1);

        switch (randomMethod){
            case 0:
                int indexPalito = msgToDecrypt.lastIndexOf("|")+1;
                int cifradoValue = Integer.parseInt(msgToDecrypt.substring( indexPalito ) );
                encryptedMsg = Cesar.desencriptar(msgToDecrypt.substring(0,indexPalito-1),cifradoValue);
                break;
            case 1:
                encryptedMsg = CifradoPropio.decifrar(msgToDecrypt);
                break;
            case 2:
                try {
                    encryptedMsg = Transposicion.decifrar(msgToDecrypt);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case 3:
                try {
                    encryptedMsg = Vigerene.decifrar(msgToDecrypt);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

        }

        return encryptedMsg;
    }

}
