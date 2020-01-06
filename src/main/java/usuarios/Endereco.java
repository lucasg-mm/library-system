/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usuarios;

import PersonalExceptions.CampoVazioException;
import PersonalExceptions.InvalidNumeroException;
import java.io.Serializable;

/**
 *
 * @author lucas
 */
public class Endereco implements Serializable{
    private String rua;
    private int numero;
    private String cidade;

    public Endereco() {
        rua = null;
        numero = 0;
        cidade = null;
    }
    
    public void setRua(String rua) throws CampoVazioException{
        if(rua.equals("") == true){
            throw new CampoVazioException();
        }
        this.rua = rua;
    }

    public void setNumero(int numero) throws InvalidNumeroException{
        if(numero < 0){
            throw new InvalidNumeroException();
        }
        this.numero = numero;
    }

    public void setCidade(String cidade) throws CampoVazioException{ 
        if(rua.equals("") == true){
            throw new CampoVazioException();
        }        
        this.cidade = cidade;
    }
}
