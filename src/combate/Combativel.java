package combate;

import modelo.Elemento;

public interface Combativel {
    void atacar(Combativel alvo);

    default void atacar(Combativel alvo, boolean critico) {
        atacar(alvo);
    }

    void receberDano(int dano);

    boolean estaVivo();
    String getNome();
    int getHpAtual();
    int getHpMax();
    Elemento getElemento();
}
