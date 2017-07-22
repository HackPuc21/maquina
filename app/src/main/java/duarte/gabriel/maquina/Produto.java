package duarte.gabriel.maquina;

/**
 * Created by Yago on 22-Jul-17.
 */

public class Produto {

    private String id;
    private String descricao;
    private int quantidade;
    private long valor;

    public Produto(String id, String descricao, int quantidade, long valor){
        this.id = id;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public long getValor() {
        return valor;
    }
}
