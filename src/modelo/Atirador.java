package modelo;

import combate.Combativel;
import excecoes.ManaInsuficienteException;

public class Atirador extends Heroi {
    public Atirador(String nome, int hpMax, int ataque, int defesa, int manaMax, Elemento elemento) {
        super(nome, hpMax, ataque, defesa, manaMax, elemento);
    }

    @Override
    public double getChanceCritico() {
        return 0.28; // Precisão mortal (28% de crítico)
    }

    @Override
    public void usarHabilidade(Combativel alvo) throws ManaInsuficienteException {
        int custoMana = getCustoManaHabilidade();
        if (!gastarMana(custoMana)) {
            throw new ManaInsuficienteException("[!] Mana insuficiente para o " + getNomeHabilidade() + ".");
        }

        if (alvo != null && alvo.estaVivo()) {
            double mult = this.elemento.getMultiplicadorContra(alvo.getElemento());
            int danoBase = (int) (this.ataque * 1.45) + 8;
            int danoFinal = (int) (danoBase * mult);
            alvo.receberDano(danoFinal);
        }
    }

    @Override
    public String getNomeHabilidade() {
        return "Disparo Perfurante";
    }

    @Override
    public int getCustoManaHabilidade() {
        return 14;
    }

    @Override
    public String getDescricaoHabilidade() {
        return "Dispara projétil perfurante em ponto fraco (145% ATQ + 8 * Reação Elemental).";
    }

    @Override
    public String getDescricaoPassiva() {
        return "Olho de Falcão: 28% de chance de acerto crítico (causa 150% de dano).";
    }
}
