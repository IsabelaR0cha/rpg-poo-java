package modelo;
import combate.Combativel;

public class Monstro extends Entidade {
    private int andar;
    private Elemento elemento;

    public Monstro(String nome, int hpMax, int ataque, int defesa, int andar, Elemento elemento) {
        super(nome, hpMax, ataque, defesa);

        this.andar = andar;
        this.elemento = elemento;

    }

    public int getAndar() {
        return this.andar;
    }

    public Elemento getElemento() {
        return this.elemento;
    }

    @Override
    public void atacar(Combativel alvo) {
        if(alvo != null && alvo.estaVivo()) {
            alvo.receberDano(this.ataque);
        }
    }
}