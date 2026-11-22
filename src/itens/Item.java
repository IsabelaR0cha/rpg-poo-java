package itens;

public class Item {
    private String nome;
    private TipoItem tipo;
    private int valorEfeito;
    private String descricao;

    public Item(String nome, TipoItem tipo, int valorEfeito) {
        this(nome, tipo, valorEfeito, null);
    }

    public Item(String nome, TipoItem tipo, int valorEfeito, String descricao) {
        this.nome = nome;
        this.tipo = tipo;
        this.valorEfeito = valorEfeito;
        this.descricao = descricao;
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

    public String getDescricao() {
        if (this.descricao != null && !this.descricao.isEmpty()) {
            return this.descricao;
        }
        if (this.tipo == TipoItem.CURA) {
            return "Restaura " + this.valorEfeito + " pontos de vida (HP) e extingue queimaduras ativas.";
        } else {
            return "Restaura " + this.valorEfeito + " pontos de energia elemental (Mana).";
        }
    }

    @Override
    public String toString() {
        return this.nome + " (+" + this.valorEfeito + " " + this.tipo + ")";
    }
}
