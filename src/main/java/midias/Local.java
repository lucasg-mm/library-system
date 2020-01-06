/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package midias;

import PersonalExceptions.CampoVazioException;
import PersonalExceptions.InvalidNumeroException;
import java.io.Serializable;

/**
 *
 * @author lucas
 */
public class Local implements Serializable{
     private String section;
     private int andar;

    public void setSection(String section) throws CampoVazioException{
        if(section.equals("") == true){
            throw new CampoVazioException();
        }        
        this.section = section;
    }

    public void setAndar(int andar) throws InvalidNumeroException{ 
        if((andar < 1) || (andar > 3)){
            throw new InvalidNumeroException();
        }
        
        this.andar = andar;
    }

    public String getSection() {
        return section;
    }

    public int getAndar() {
        return andar;
    }    
    
}
