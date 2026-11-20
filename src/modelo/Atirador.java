package modelo;

import combate.Combativel;
import excecoes.ManaInsuficienteException;

public class Atirador extends Heroi {
    public Atirador(String nome, int hpMax, int ataque, int defesa, int manaMax, Elemento elemento) {
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
        int custoMana = 12;
        if (!gastarMana(custoMana)) {
            throw new ManaInsuficienteException("[!] Mana insuficiente para usar a Rajada Precisa.");
        }

        if (alvo != null && alvo.estaVivo()) {
            int danoHabilidade = (int) (this.ataque * 1.4) + 5;
            alvo.receberDano(danoHabilidade);
        }
    }
}