/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package midias;

import PersonalExceptions.CampoVazioException;
import PersonalExceptions.InvalidNumeroException;
import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author lucas
 */
public class Media implements Serializable{
    private String titulo;
    private String autor;
    private String tipo;
    private int ano;
    private String editora;
    private Local localizacao;
    private LocalDate data_locacao;  //Data em que ocorreu a locação (se ocorreu)

    public String getTitulo() {
        return titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) throws CampoVazioException{
        if(tipo.equals("") == true){
            throw new CampoVazioException();
        }        
        this.tipo = tipo;
    }

    public String getAutor() {
        return autor;
    }

    public String getEditora() {
        return editora;
    }

    public LocalDate getData_locacao() {
        return data_locacao;
    }    

    public int getAno() {
        return ano;
    }  
    
    public void setTitulo(String titulo) throws CampoVazioException{ 
        if(titulo.equals("") == true){
            throw new CampoVazioException();
        }        
        this.titulo = titulo;
    }

    public void setAutor(String autor) throws CampoVazioException{
        if(autor.equals("") == true){
            throw new CampoVazioException();
        }        
        this.autor = autor;
    }

    public void setAno(int ano) throws InvalidNumeroException{
        if(ano < 0){  //Ano inválido!!
            throw new InvalidNumeroException();
        }
        
        this.ano = ano;
    }

    public void setEditora(String editora) throws CampoVazioException{
        if(editora.equals("") == true){
            throw new CampoVazioException();
        }        
        this.editora = editora;
    }

    public void setLocalizacao(Local localizacao) {
        this.localizacao = localizacao;
    }

    public Local getLocalizacao() {
        return localizacao;
    }

    public void setData_locacao(LocalDate data_locacao) {
        this.data_locacao = data_locacao;
    }    
}
