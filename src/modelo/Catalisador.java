package modelo;

import combate.Combativel;
import excecoes.ManaInsuficienteException;

public class Catalisador extends Heroi {
    public Catalisador(String nome, int hpMax, int ataque, int defesa, int manaMax, Elemento elemento) {
        super(nome, hpMax, ataque, defesa, manaMax, elemento);
    }

    @Override
    public void atacar(Combativel alvo) {
        // Os ataques normais do Catalisador também usam afinidade elemental!
        if (alvo != null && alvo.estaVivo()) {
            double mult = this.elemento.getMultiplicadorContra(alvo.getElemento());
            int dano = (int) (this.ataque * mult);
            alvo.receberDano(dano);
        }
    }

    @Override
    public void usarHabilidade(Combativel alvo) throws ManaInsuficienteException {
        int custoMana = 22;
        if (!gastarMana(custoMana)) {
            throw new ManaInsuficienteException("[!] Mana insuficiente para a Ressonância Elemental Suprema.");
        }

        if (alvo != null && alvo.estaVivo()) {
            double mult = this.elemento.getMultiplicadorContra(alvo.getElemento());
            int danoBase = this.ataque + 25;
            int danoFinal = (int) (danoBase * mult);
            alvo.receberDano(danoFinal);
        }
    }
}
