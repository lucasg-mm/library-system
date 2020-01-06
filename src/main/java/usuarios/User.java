/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usuarios;

import PersonalExceptions.CampoVazioException;
import PersonalExceptions.InvalidNumeroException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedList;
import midias.Media;

/**
 *
 * @author lucas
 */
public class User implements Serializable{
    private LinkedList <Media> midias_pegas;  //Lista de midias na posse do usuario atualmente.
    private String nome;
    private Endereco endereco;
    private String nusp;
    private LocalDate nascimento;

    public void setNome(String nome) throws CampoVazioException{
        
        if(nome.equals("") == true){
            throw new CampoVazioException();
        }
        
        this.nome = nome;
    }

    public void setMidias_pegas(LinkedList<Media> midias_pegas) {
        this.midias_pegas = midias_pegas;
    }    
    
    public LinkedList<Media> getMidias_pegas() {
        return midias_pegas;
    }

    public String getNome() {
        return nome;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    public String getNusp() {
        return nusp;
    }
    
    public void setNusp(String nusp) throws InvalidNumeroException, CampoVazioException{  //Verifica se o nusp possui sete digitos        
        char aux;
        
        if(nusp.equals("") == true){
            throw new CampoVazioException();
        }
        
        if(nusp.length() != 7){  //Tamanho errado.
            throw new InvalidNumeroException();
        }
        
        for(int i = 0; i < 7; i++){
            aux = nusp.charAt(i);
            if(!Character.isDigit(aux)){
                throw new InvalidNumeroException();
            }
        }

        this.nusp = nusp;  
    }    

    public User() {
        this.midias_pegas = new LinkedList();
        this.nome = null;
        this.endereco = null;
        this.nusp = null;
        this.nascimento = null;
    }
    
}
