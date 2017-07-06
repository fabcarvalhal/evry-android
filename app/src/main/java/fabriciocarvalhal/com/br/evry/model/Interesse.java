package fabriciocarvalhal.com.br.evry.model;

/**
 * Created by fabricio on 05/07/17.
 */

public class Interesse {

    private int id;
    private String nome;


    public Interesse(){

    }

    public Interesse(int id, String nome){
        this.id = id;
        this.nome= nome;
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
