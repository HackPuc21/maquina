package duarte.gabriel.maquina;

/**
 * Created by Yago on 22-Jul-17.
 */

public class Produto {

    private int id_produto;
    private String descricao;
    private int quantidade;
    private double valor;

    public Produto(int id_produto, String descricao, int quantidade, double valor){
        this.id_produto = id_produto;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public int getId_produto() {
        return id_produto;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getValor() {
        return valor;
    }
}
