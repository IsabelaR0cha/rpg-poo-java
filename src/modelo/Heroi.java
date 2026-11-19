package modelo;
import combate.Combativel;
import excecoes.ManaInsuficienteException;

public abstract class Heroi extends Entidade {
    protected  int manaAtual;
    protected int manaMax;
    protected  int nivel;

    public Heroi(String nome, int hpMax, int ataque, int defesa, int manaMax){
        super(nome, hpMax, ataque, defesa);
        this.manaMax = manaMax;
        this.manaAtual = manaMax;
        this.nivel = 1;
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

    public abstract void usarHabilidade(Combativel alvo) throws ManaInsuficienteException;

    protected boolean gastarMana(int custo) {
        if(this.manaAtual >= custo) {
            this.manaAtual = this.manaAtual - custo;
            return true;
        }else{
            return false;
        }
    }

    public void subirNivel() {
        this.nivel++;
        this.hpMax = this.hpMax + 12;
        this.manaMax = this.manaMax + 5;
        this.ataque = this.ataque + 20;

        this.hpAtual = this.hpMax;
        this.manaAtual = this.manaMax;
    }
}