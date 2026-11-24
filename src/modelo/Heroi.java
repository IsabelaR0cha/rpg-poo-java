package modelo;

import combate.Combativel;
import excecoes.ItemNaoEncontradoException;
import excecoes.ManaInsuficienteException;
import itens.Inventario;
import itens.Item;
import itens.TipoItem;

public abstract class Heroi extends Entidade {
    public static final int GANHO_HP_NIVEL = 15;
    public static final int GANHO_MANA_NIVEL = 8;
    public static final int GANHO_ATAQUE_NIVEL = 4;
    public static final int GANHO_DEFESA_NIVEL = 2;

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

    public int getTurnosQueimando() {
        return this.turnosQueimando;
    }

    public String processarEfeitosInicioTurno() {
        return "";
    }

    public String processarEfeitosFimTurno() {
        if (this.turnosQueimando > 0) {
            this.turnosQueimando--;
            this.hpAtual = Math.max(0, this.hpAtual - this.danoQueimadura);
            return "\n🔥 [QUEIMADURA RESIDUAL] " + this.nome + " sofreu " + this.danoQueimadura
                    + " de dano contínuo de fogo! (Turnos restantes: " + this.turnosQueimando + ")";
        }
        return "";
    }

    @Override
    public void atacar(Combativel alvo) {
        atacar(alvo, false);
    }

    @Override
    public void atacar(Combativel alvo, boolean critico) {
        if (alvo != null && alvo.estaVivo()) {
            int dano = this.ataque;
            if (critico) {
                dano = (int) (dano * 1.5);
            }
            alvo.receberDano(dano);
        }
    }

    public void usarItem(String nomeItem) throws ItemNaoEncontradoException {
        this.inventario.usarItem(nomeItem, this);
    }

    public abstract void usarHabilidade(Combativel alvo) throws ManaInsuficienteException;

    public abstract String getNomeHabilidade();
    public abstract int getCustoManaHabilidade();
    public abstract String getDescricaoHabilidade();
    public abstract String getDescricaoPassiva();

    public static String getDescricaoBonusNivel() {
        return "Bônus de Atributos: +" + GANHO_HP_NIVEL + " HP Máximo | +"
                + GANHO_MANA_NIVEL + " Mana Máxima | +"
                + GANHO_ATAQUE_NIVEL + " Ataque | +"
                + GANHO_DEFESA_NIVEL + " Defesa";
    }

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
        this.hpMax += GANHO_HP_NIVEL;
        this.manaMax += GANHO_MANA_NIVEL;
        this.ataque += GANHO_ATAQUE_NIVEL;
        this.defesa += GANHO_DEFESA_NIVEL;

        // Ao vencer um andar e subir de nível, recupera uma porção revitalizante de vitalidade e energia
        this.hpAtual = Math.min(this.hpMax, this.hpAtual + 55);
        this.manaAtual = Math.min(this.manaMax, this.manaAtual + 35);
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
