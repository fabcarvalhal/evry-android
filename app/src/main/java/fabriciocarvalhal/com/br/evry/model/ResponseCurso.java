package fabriciocarvalhal.com.br.evry.model;

import java.util.List;



public class ResponseCurso {
    private boolean erro;
    private List<Curso> data;

    public ResponseCurso(boolean erro,  List<Curso> data) {
        this.erro = erro;
        this.data = data;
    }


    public boolean isErro() {
        return erro;
    }

    public void setErro(boolean erro) {
        this.erro = erro;
    }

    public List<Curso> getData() {
        return data;
    }

    public void setData(List<Curso> data) {
        this.data = data;
    }

}
