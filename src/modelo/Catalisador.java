package modelo;

import combate.Combativel;
import excecoes.ManaInsuficienteException;

public class Catalisador extends Heroi {
    public static final int HP_BASE = 85;
    public static final int MANA_BASE = 80;
    public static final int ATAQUE_BASE = 23;
    public static final int DEFESA_BASE = 5;
    public static final int CUSTO_MANA = 22;

    public Catalisador(String nome, Elemento elemento) {
        super(nome, HP_BASE, ATAQUE_BASE, DEFESA_BASE, MANA_BASE, elemento);
    }

    public Catalisador(String nome, int hpMax, int ataque, int defesa, int manaMax, Elemento elemento) {
        super(nome, hpMax, ataque, defesa, manaMax, elemento);
    }

    @Override
    public void atacar(Combativel alvo, boolean critico) {
        // Ataques normais do Catalisador aplicam a vantagem elemental e escalam com acerto crítico!
        if (alvo != null && alvo.estaVivo()) {
            double mult = this.elemento.getMultiplicadorContra(alvo.getElemento());
            int danoElemental = (int) (this.ataque * mult);
            if (critico) {
                danoElemental = (int) (danoElemental * 1.5);
            }
            alvo.receberDano(danoElemental);
        }
    }

    @Override
    public void atacar(Combativel alvo) {
        atacar(alvo, false);
    }

    @Override
    public void usarHabilidade(Combativel alvo) throws ManaInsuficienteException {
        int custoMana = getCustoManaHabilidade();
        if (!gastarMana(custoMana)) {
            throw new ManaInsuficienteException("[!] Mana insuficiente para a " + getNomeHabilidade() + ".");
        }

        if (alvo != null && alvo.estaVivo()) {
            double mult = this.elemento.getMultiplicadorContra(alvo.getElemento());
            int danoBase = this.ataque + 25;
            int danoFinal = (int) (danoBase * mult);
            alvo.receberDano(danoFinal);
        }
    }

    @Override
    public String getNomeHabilidade() {
        return "Ressonância Suprema";
    }

    @Override
    public int getCustoManaHabilidade() {
        return 22;
    }

    @Override
    public String getDescricaoHabilidade() {
        return "Vórtice mágico de alta densidade (+25 Dano Base * Reação Elemental).";
    }

    @Override
    public String getDescricaoPassiva() {
        return "Infusão Arcana: Todos os ataques normais utilizam reações elementais e escalam com crítico.";
    }
}
