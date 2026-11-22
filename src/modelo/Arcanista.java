package modelo;

import combate.Combativel;
import excecoes.ManaInsuficienteException;

public class Arcanista extends Heroi {
    public Arcanista(String nome, int hpMax, int ataque, int defesa, int manaMax, Elemento elemento) {
        super(nome, hpMax, ataque, defesa, manaMax, elemento);
    }

    @Override
    public void usarHabilidade(Combativel alvo) throws ManaInsuficienteException {
        int custoMana = getCustoManaHabilidade();
        if (!gastarMana(custoMana)) {
            throw new ManaInsuficienteException("[!] Mana insuficiente para conjurar a " + getNomeHabilidade() + ".");
        }

        if (alvo != null && alvo.estaVivo()) {
            double mult = this.elemento.getMultiplicadorContra(alvo.getElemento()) * 1.15;
            int danoBase = this.ataque + 22;
            int danoFinal = (int) (danoBase * mult);
            alvo.receberDano(danoFinal);
        }
    }

    @Override
    public String getNomeHabilidade() {
        return "Explosão Arcana";
    }

    @Override
    public int getCustoManaHabilidade() {
        return 20;
    }

    @Override
    public String getDescricaoHabilidade() {
        return "Concentra magia primordial em impacto destrutivo (+22 Dano Base * Reação Elemental +15%).";
    }

    @Override
    public String getDescricaoPassiva() {
        return "Mestria Elemental: Aumenta o dano de reações elementais da habilidade em +15%.";
    }
}
