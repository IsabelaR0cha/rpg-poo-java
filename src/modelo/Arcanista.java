package modelo;
import combate.Combativel;

import excecoes.ManaInsuficienteException;

public class Arcanista extends Heroi {
    public Arcanista(String nome, int hpMax, int ataque, int defesa, int manaMax) {
        super(nome, hpMax, ataque, defesa, manaMax);
    }

    @Override
    public void atacar(Combativel alvo) {
        if(alvo != null && alvo.estaVivo()) {
            alvo.receberDano(this.ataque);
        }
    }

    @Override
    public void usarHabilidade(Combativel alvo) throws ManaInsuficienteException {        int custoMana = 20;
        if(!gastarMana(custoMana)) {
            throw new ManaInsuficienteException("[!] Mana insuficiente para usar habilidade.");
        }

        if(alvo != null && alvo.estaVivo()) {
            alvo.receberDano(this.ataque + 18);
        }
        
    }

}