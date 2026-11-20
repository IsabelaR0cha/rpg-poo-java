package modelo;

import combate.Combativel;
import excecoes.ManaInsuficienteException;

public class Atirador extends Heroi {
    public Atirador(String nome, int hpMax, int ataque, int defesa, int manaMax, Elemento elemento) {
        super(nome, hpMax, ataque, defesa, manaMax, elemento);
    }

    @Override
    public double getChanceCritico() {
        // Atirador possui mira certeira e maior chance de acerto crítico (28%)
        return 0.28;
    }

    @Override
    public void atacar(Combativel alvo) {
        if (alvo != null && alvo.estaVivo()) {
            alvo.receberDano(this.ataque);
        }
    }

    @Override
    public void usarHabilidade(Combativel alvo) throws ManaInsuficienteException {
        int custoMana = 14;
        if (!gastarMana(custoMana)) {
            throw new ManaInsuficienteException("[!] Mana insuficiente para o Disparo Elemental Perfurante.");
        }

        if (alvo != null && alvo.estaVivo()) {
            double mult = this.elemento.getMultiplicadorContra(alvo.getElemento());
            int danoBase = (int) (this.ataque * 1.45) + 8;
            int danoFinal = (int) (danoBase * mult);
            alvo.receberDano(danoFinal);
        }
    }
}
