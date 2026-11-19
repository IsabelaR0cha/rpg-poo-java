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
    public String getNome(){
        return this.nome;
    }
    
    @Override
    public int getHpAtual(){
        return this.hpAtual;
    }
    
    @Override
    public int getHpMax(){
        return this.hpMax;
    }
    
    @Override
    public boolean estaVivo(){
        return this.hpAtual > 0;
    }

    @Override
    public void receberDano(int dano) {
        int danoReal = dano - this.defesa;

        if(danoReal < 0){
            danoReal = 0;
        }

        this.hpAtual -= danoReal;

        if(this.hpAtual < 0){
            this.hpAtual = 0;
        }
    }
}