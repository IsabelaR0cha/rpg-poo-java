package modelo;

import combate.Combativel;
import excecoes.ManaInsuficienteException;

public class Espadachim extends Heroi {
    public Espadachim(String nome, int hpMax, int ataque, int defesa, int manaMax, Elemento elemento) {
        super(nome, hpMax, ataque, defesa, manaMax, elemento);
    }

    @Override
    public double getChanceEsquiva() {
        return 0.16; // Maestria em bloqueio e esquiva
    }

    @Override
    public void usarHabilidade(Combativel alvo) throws ManaInsuficienteException {
        int custoMana = getCustoManaHabilidade();
        if (!gastarMana(custoMana)) {
            throw new ManaInsuficienteException("[!] Mana insuficiente para usar a " + getNomeHabilidade() + ".");
        }

        if (alvo != null && alvo.estaVivo()) {
            double mult = this.elemento.getMultiplicadorContra(alvo.getElemento());
            int danoBase = this.ataque + 16;
            int danoFinal = (int) (danoBase * mult);
            alvo.receberDano(danoFinal);
        }
    }

    @Override
    public String getNomeHabilidade() {
        return "Lâmina Elemental";
    }

    @Override
    public int getCustoManaHabilidade() {
        return 15;
    }

    @Override
    public String getDescricaoHabilidade() {
        return "Golpe de espada fortalecido com infusão elemental (+16 Dano Base * Reação Elemental).";
    }

    @Override
    public String getDescricaoPassiva() {
        return "Postura Inabalável: 16% de chance de esquivar ou aparar ataques inimigos.";
    }
}
