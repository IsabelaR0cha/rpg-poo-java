package modelo;

import combate.Combativel;

public abstract class Entidade implements Combativel {
    protected String nome;
    protected int hpAtual;
    protected int hpMax;
    protected int ataque;
    protected int defesa;

    public Entidade(String nome, int hpMax, int ataque, int defesa) {
        this.nome = nome;
        this.hpAtual = hpMax;
        this.hpMax = hpMax;
        this.ataque = ataque;
        this.defesa = defesa;
    }

    @Override
    public String getNome() {
        return this.nome;
    }

    @Override
    public int getHpAtual() {
        return this.hpAtual;
    }

    @Override
    public int getHpMax() {
        return this.hpMax;
    }

    @Override
    public boolean estaVivo() {
        return this.hpAtual > 0;
    }

    public int getAtaque() {
        return this.ataque;
    }

    public int getDefesa() {
        return this.defesa;
    }

    public void aumentarAtaque(int valor) {
        this.ataque += valor;
    }

    public void aumentarDefesa(int valor) {
        this.defesa += valor;
    }

    public void definirHpAtual(int hp) {
        this.hpAtual = Math.max(0, Math.min(hp, this.hpMax));
    }

    @Override
    public void receberDano(int dano) {
        int reducao = (int) (this.defesa * 0.60);
        int danoReal = Math.max(2, dano - reducao);

        this.hpAtual -= danoReal;

        if (this.hpAtual < 0) {
            this.hpAtual = 0;
        }
    }
}
