package fabriciocarvalhal.com.br.evry.model;

/**
 * Created by Fabrício Carvalhal on 04/07/2017.
 */

public class Curso {
    private  int id;
    private String nome;


    public Curso(){
    }

    public  Curso(int id, String nome){
        this.setId(id);
        this.setNome(nome);

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
