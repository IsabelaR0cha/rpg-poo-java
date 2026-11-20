package modelo;

import combate.Combativel;
import excecoes.ManaInsuficienteException;

public class Espadachim extends Heroi {
    public Espadachim(String nome, int hpMax, int ataque, int defesa, int manaMax, Elemento elemento) {
        super(nome, hpMax, ataque, defesa, manaMax, elemento);
    }

    @Override
    public double getChanceEsquiva() {
        // Espadachim possui maestria defensiva para aparar e bloquear golpes (16% de esquiva/bloqueio)
        return 0.16;
    }

    @Override
    public void atacar(Combativel alvo) {
        if (alvo != null && alvo.estaVivo()) {
            alvo.receberDano(this.ataque);
        }
    }

    @Override
    public void usarHabilidade(Combativel alvo) throws ManaInsuficienteException {
        int custoMana = 15;
        if (!gastarMana(custoMana)) {
            throw new ManaInsuficienteException("[!] Mana insuficiente para usar a Lâmina Elemental.");
        }

        if (alvo != null && alvo.estaVivo()) {
            double mult = this.elemento.getMultiplicadorContra(alvo.getElemento());
            int danoBase = this.ataque + 16;
            int danoFinal = (int) (danoBase * mult);
            alvo.receberDano(danoFinal);
        }
    }
}
