package modelo;

public class Item {
    private String nome;
    private TipoItem tipo;
    private int valorEfeito;

    public Item(String nome, TipoItem tipo, int valorEfeito) {
        this.nome = nome;
        this.tipo = tipo;
        this.valorEfeito = valorEfeito;
    }

    public String getNome() {
        return this.nome;
    }

    public TipoItem getTipo() {
        return this.tipo;
    }

    public int getValorEfeito() {
        return this.valorEfeito;
    }

    @Override
    public String toString() {
        return this.nome + " (+" + this.valorEfeito + " " + this.tipo + ")";
    }
}