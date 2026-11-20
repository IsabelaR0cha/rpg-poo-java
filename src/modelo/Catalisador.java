package modelo;

import combate.Combativel;
import excecoes.ManaInsuficienteException;

public class Catalisador extends Heroi {
    public Catalisador(String nome, int hpMax, int ataque, int defesa, int manaMax, Elemento elemento) {
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
        int custoMana = 25;
        if (!gastarMana(custoMana)) {
            throw new ManaInsuficienteException("[!] Mana insuficiente para usar a Ressonância Elemental.");
        }

        if (alvo != null && alvo.estaVivo()) {
            int danoHabilidade = this.ataque + 22;
            alvo.receberDano(danoHabilidade);
        }
    }
}