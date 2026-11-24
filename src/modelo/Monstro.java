package modelo;

import combate.Combativel;
import java.util.Random;

public class Monstro extends Entidade {
    private int andar;
    private Elemento elemento;
    private boolean eMimico;
    private static final Random random = new Random();

    public Monstro(String nome, int hpMax, int ataque, int defesa, int andar, Elemento elemento) {
        this(nome, hpMax, ataque, defesa, andar, elemento, false);
    }

    public Monstro(String nome, int hpMax, int ataque, int defesa, int andar, Elemento elemento, boolean eMimico) {
        super(nome, hpMax, ataque, defesa);
        this.andar = andar;
        this.elemento = elemento;
        this.eMimico = eMimico;
    }

    public int getAndar() {
        return this.andar;
    }

    @Override
    public Elemento getElemento() {
        return this.elemento;
    }

    public boolean isMimico() {
        return this.eMimico;
    }

    public double getChanceEsquiva() {
        // Guardião Anemo é ágil e tem alta chance de esquivar de golpes
        if (this.elemento == Elemento.ANEMO) {
            return 0.18;
        }
        return 0.06;
    }

    public double getChanceCritico() {
        return 0.12;
    }

    @Override
    public void atacar(Combativel alvo) {
        if (alvo != null && alvo.estaVivo()) {
            alvo.receberDano(this.ataque);
        }
    }

    public String agirNoTurno(Heroi heroi, int turno) {
        if (!estaVivo() || heroi == null || !heroi.estaVivo()) {
            return "";
        }

        // Verifica se o herói esquivou
        if (random.nextDouble() < heroi.getChanceEsquiva()) {
            return "💨 [ESQUIVA] " + heroi.getNome() + " esquivou com precisão do ataque de " + this.nome + "!";
        }

        int hpAntes = heroi.getHpAtual();
        int sorteioAcao = random.nextInt(100);

        // Habilidades Especiais dos Monstros
        if (this.eMimico && sorteioAcao < 45) {
            // Mímico: Mordida Voraz
            int dano = (int) (this.ataque * 1.35);
            heroi.receberDano(dano);
            int danoReal = hpAntes - heroi.getHpAtual();
            return "🦷 [HABILIDADE DO MÍMICO] " + this.nome + " cravou dentes afiados em "
                    + heroi.getNome() + " causando " + danoReal + " de dano crítico!";
        } else if (this.elemento == Elemento.PYRO && sorteioAcao < 40) {
            // Slime Pyro: Lança-Chamas Concentrado + Queimadura
            int dano = (int) (this.ataque * 1.25);
            heroi.receberDano(dano);
            heroi.aplicarQueimadura(2, 6);
            int danoReal = hpAntes - heroi.getHpAtual();
            return "🔥 [HABILIDADE ESPECIAL] " + this.nome + " expeliu um Lança-Chamas Ardente causando "
                    + danoReal + " de dano e incendiando " + heroi.getNome() + " por 2 turnos!";
        } else if (this.elemento == Elemento.HYDRO && this.hpAtual < (this.hpMax * 0.40) && sorteioAcao < 35) {
            // Espectro Hydro: Manto das Marés (Cura suave + Dano reduzido)
            int cura = 16;
            this.hpAtual = Math.min(this.hpMax, this.hpAtual + cura);
            int dano = (int) (this.ataque * 0.85);
            heroi.receberDano(dano);
            int danoReal = hpAntes - heroi.getHpAtual();
            return "💧 [HABILIDADE ESPECIAL] " + this.nome + " conjurou o Manto das Marés! Regenerou +"
                    + cura + " de HP e causou " + danoReal + " de dano em " + heroi.getNome() + "!";
        } else if (this.elemento == Elemento.ANEMO && sorteioAcao < 35) {
            // Guardião Anemo: Ciclone Penetrante
            int dano = (int) (this.ataque * 1.35);
            heroi.receberDano(dano);
            int danoReal = hpAntes - heroi.getHpAtual();
            return "🌪️ [HABILIDADE ESPECIAL] " + this.nome + " invocou um Ciclone Cortante causando "
                    + danoReal + " de dano perfurante em " + heroi.getNome() + "!";
        } else if (this.elemento == Elemento.ELECTRO && (turno % 4 == 0 || sorteioAcao < 25)) {
            // Arconte Electro: Raio Cataclísmico
            int dano = (int) (this.ataque * 1.35);
            heroi.receberDano(dano);
            heroi.gastarMana(8);
            int danoReal = hpAntes - heroi.getHpAtual();
            return "⚡ [GOLPE SUPREMO] " + this.nome + " descarregou o Raio Cataclísmico causando brutais "
                    + danoReal + " de dano e queimando 8 de mana de " + heroi.getNome() + "!";
        }

        // Ataque básico com chance de acerto crítico
        boolean critico = random.nextDouble() < getChanceCritico();
        int dano = critico ? (int) (this.ataque * 1.4) : this.ataque;
        heroi.receberDano(dano);
        int danoReal = hpAntes - heroi.getHpAtual();

        String msgCritico = critico ? " [GOLPE CRÍTICO DO MONSTRO!]" : "";
        return this.nome + " contra-atacou" + msgCritico + " causando "
                + danoReal + " de dano em " + heroi.getNome() + ".";
    }
}
