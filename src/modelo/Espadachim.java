package modelo;
import combate.Combativel;
import excecoes.ManaInsuficienteException;

public class Espadachim extends Heroi {
    public Espadachim(String nome, int hpMax, int ataque, int defesa, int manaMax, Elemento elemento) {
        super(nome, hpMax, ataque, defesa, manaMax, elemento);
    }

    @Override
    public void atacar(Combativel alvo) {
        if(alvo != null && alvo.estaVivo()){
            alvo.receberDano(this.ataque);
        }
    }

    @Override
    public void usarHabilidade(Combativel alvo) throws ManaInsuficienteException {        int custoMana = 15;
        if(!gastarMana(custoMana)) {
            throw new ManaInsuficienteException("[!] Mana insuficiente para usar habilidade.");
        }

        if(alvo != null && alvo.estaVivo()) {
            alvo.receberDano(this.ataque + 10);
        }
        
    }

}
