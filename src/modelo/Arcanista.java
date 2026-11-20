package modelo;

import combate.Combativel;
import excecoes.ManaInsuficienteException;

public class Arcanista extends Heroi {
    public Arcanista(String nome, int hpMax, int ataque, int defesa, int manaMax, Elemento elemento) {
        super(nome, hpMax, ataque, defesa, manaMax, elemento);
    }

    @Override
    public void atacar(Combativel alvo) {
        if (alvo != null && alvo.estaVivo()) {
            alvo.receberDano(this.ataque);
        }
    }

    @Override
    public void usarHabilidade(Combativel alvo) throws ManaInsuficienteException {
        int custoMana = 20;
        if (!gastarMana(custoMana)) {
            throw new ManaInsuficienteException("[!] Mana insuficiente para conjurar a Explosão Arcana.");
        }

        if (alvo != null && alvo.estaVivo()) {
            // Arcanista extrai ainda mais poder da reação elemental (+20% sobre o multiplicador)
            double mult = this.elemento.getMultiplicadorContra(alvo.getElemento()) * 1.15;
            int danoBase = this.ataque + 22;
            int danoFinal = (int) (danoBase * mult);
            alvo.receberDano(danoFinal);
        }
    }
}
