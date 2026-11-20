package modelo;

import combate.Combativel;
import excecoes.ItemNaoEncontradoException;
import excecoes.ManaInsuficienteException;
import itens.Inventario;
import itens.Item;
import itens.TipoItem;

public abstract class Heroi extends Entidade {
    protected int manaAtual;
    protected int manaMax;
    protected int nivel;
    protected Elemento elemento;
    protected Inventario inventario;

    protected int turnosQueimando = 0;
    protected int danoQueimadura = 0;

    public Heroi(String nome, int hpMax, int ataque, int defesa, int manaMax, Elemento elemento) {
        super(nome, hpMax, ataque, defesa);
        this.manaMax = manaMax;
        this.manaAtual = manaMax;
        this.nivel = 1;
        this.elemento = elemento;
        this.inventario = new Inventario();
        this.inventario.adicionarItem(new Item("Poção de Cura", TipoItem.CURA, 30));
        this.inventario.adicionarItem(new Item("Éter de Mana", TipoItem.MANA, 25));
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

    @Override
    public Elemento getElemento() {
        return this.elemento;
    }

    public Inventario getInventario() {
        return this.inventario;
    }

    public double getChanceCritico() {
        return 0.15;
    }

    public double getChanceEsquiva() {
        return 0.08;
    }

    public void aplicarQueimadura(int turnos, int danoPorTurno) {
        this.turnosQueimando = turnos;
        this.danoQueimadura = danoPorTurno;
    }

    public boolean estaQueimando() {
        return this.turnosQueimando > 0;
    }

    public String processarEfeitosInicioTurno() {
        if (this.turnosQueimando > 0) {
            this.turnosQueimando--;
            this.hpAtual = Math.max(0, this.hpAtual - this.danoQueimadura);
            String msg = "🔥 [QUEIMADURA] " + this.nome + " sofreu " + this.danoQueimadura
                    + " de dano contínuo de fogo! (Turnos restantes: " + this.turnosQueimando + ")\n";
            return msg;
        }
        return "";
    }

    public void usarItem(String nomeItem) throws ItemNaoEncontradoException {
        this.inventario.usarItem(nomeItem, this);
    }

    public abstract void usarHabilidade(Combativel alvo) throws ManaInsuficienteException;

    public boolean gastarMana(int custo) {
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
        // Poções de cura limpam queimaduras
        this.turnosQueimando = 0;
    }

    public void recuperarMana(int quantidade) {
        this.manaAtual += quantidade;
        if (this.manaAtual > this.manaMax) {
            this.manaAtual = this.manaMax;
        }
    }

    public void subirNivel() {
        this.nivel++;
        this.hpMax += 15;
        this.manaMax += 8;
        this.ataque += 4;
        this.defesa += 2;

        this.hpAtual = Math.min(this.hpMax, this.hpAtual + 35);
        this.manaAtual = Math.min(this.manaMax, this.manaAtual + 25);
        this.turnosQueimando = 0;
    }

    public void definirEstadoSalvo(int hpAtual, int hpMax, int manaAtual, int manaMax, int ataque, int defesa) {
        this.hpMax = hpMax;
        this.hpAtual = Math.max(0, Math.min(hpAtual, hpMax));
        this.manaMax = manaMax;
        this.manaAtual = Math.max(0, Math.min(manaAtual, manaMax));
        this.ataque = ataque;
        this.defesa = defesa;
    }
}
