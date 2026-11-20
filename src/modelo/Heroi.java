package modelo;

import combate.Combativel;
import excecoes.ManaInsuficienteException;
import excecoes.ItemNaoEncontradoException;

public abstract class Heroi extends Entidade {
    protected int manaAtual;
    protected int manaMax;
    protected int nivel;
    protected Elemento elemento;
    protected Inventario inventario;

    public Heroi(String nome, int hpMax, int ataque, int defesa, int manaMax, Elemento elemento) {
        super(nome, hpMax, ataque, defesa);
        this.manaMax = manaMax;
        this.manaAtual = manaMax;
        this.nivel = 1;
        this.elemento = elemento;
        this.inventario = new Inventario();
        this.inventario.adicionarItem(new Item("Poção de Cura", TipoItem.CURA, 25));
        this.inventario.adicionarItem(new Item("Éter de Mana", TipoItem.MANA, 20));
    }

    public int getManaAtual() {
        return this.manaAtual;
    }

    public int getManaMax() {
        return this.manaMax;
    }

    public int getNivel() {
        return this.nivel;
    }

    public Elemento getElemento() {
        return this.elemento;
    }

    public Inventario getInventario() {
        return this.inventario;
    }

    public void usarItem(String nomeItem) throws ItemNaoEncontradoException {
        this.inventario.usarItem(nomeItem, this);
    }

    public abstract void usarHabilidade(Combativel alvo) throws ManaInsuficienteException;

    protected boolean gastarMana(int custo) {
        if (this.manaAtual >= custo) {
            this.manaAtual -= custo;
            return true;
        }
        return false;
    }

    public void curar(int quantidade) {
        this.hpAtual += quantidade;
        if (this.hpAtual > this.hpMax) {
            this.hpAtual = this.hpMax;
        }
    }

    public void recuperarMana(int quantidade) {
        this.manaAtual += quantidade;
        if (this.manaAtual > this.manaMax) {
            this.manaAtual = this.manaMax;
        }
    }

    public void subirNivel() {
        this.nivel++;
        this.hpMax += 12;
        this.manaMax += 5;
        this.ataque += 20;

        this.hpAtual = this.hpMax;
        this.manaAtual = this.manaMax;
    }
}