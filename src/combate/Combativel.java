package combate;

public interface Combativel {
    void atacar(Combativel alvo);
    void receberDano(int dano);

    boolean estaVivo();
    String getNome();
    int getHpAtual();
    int getHpMax();
}