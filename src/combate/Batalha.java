package combate;

import excecoes.ItemNaoEncontradoException;
import excecoes.ManaInsuficienteException;
import java.util.Random;
import modelo.Heroi;
import modelo.Monstro;

public class Batalha {
    private Heroi heroi;
    private Monstro monstro;
    private int turno;
    private static final Random random = new Random();

    public Batalha(Heroi heroi, Monstro monstro) {
        this.heroi = heroi;
        this.monstro = monstro;
        this.turno = 1;
    }

    public Heroi getHeroi() {
        return this.heroi;
    }

    public Monstro getMonstro() {
        return this.monstro;
    }

    public int getTurno() {
        return this.turno;
    }

    public boolean batalhaEncerrada() {
        return !this.heroi.estaVivo() || !this.monstro.estaVivo();
    }

    public boolean heroiVenceu() {
        return this.heroi.estaVivo() && !this.monstro.estaVivo();
    }

    public String executarAtaqueBasico() {
        if (batalhaEncerrada()) {
            return "A batalha já foi encerrada.";
        }

        StringBuilder log = new StringBuilder();
        log.append("[Turno ").append(this.turno).append("]\n");

        // Processa efeitos de início de turno no herói (ex: queimadura)
        String efeitoInicio = this.heroi.processarEfeitosInicioTurno();
        if (!efeitoInicio.isEmpty()) {
            log.append(efeitoInicio);
            if (!this.heroi.estaVivo()) {
                log.append(this.heroi.getNome()).append(" sucumbiu às chamas...\n");
                return log.toString();
            }
        }

        // Verifica se o monstro esquiva
        if (random.nextDouble() < this.monstro.getChanceEsquiva()) {
            log.append("💨 ").append(this.monstro.getNome())
               .append(" esquivou agilmente do ataque de ").append(this.heroi.getNome()).append("!\n");
        } else {
            // Ataque com chance de acerto crítico
            boolean critico = random.nextDouble() < this.heroi.getChanceCritico();
            int hpAntes = this.monstro.getHpAtual();

            if (critico) {
                // Golpe Crítico causa 50% a mais de dano
                int danoBase = (int) (this.heroi.getAtaque() * 1.5);
                this.monstro.receberDano(danoBase);
            } else {
                this.heroi.atacar(this.monstro);
            }

            int danoCausado = hpAntes - this.monstro.getHpAtual();
            String msgCritico = critico ? " [💥 ACERTO CRÍTICO!]" : "";

            log.append(this.heroi.getNome()).append(" atacou ")
               .append(this.monstro.getNome()).append(msgCritico)
               .append(" causando ").append(danoCausado).append(" de dano.\n");
        }

        if (this.monstro.estaVivo()) {
            log.append(turnoDoMonstro());
        } else {
            log.append("\n⭐ ").append(this.monstro.getNome()).append(" foi derrotado!");
        }

        return log.toString();
    }

    public String executarHabilidade() throws ManaInsuficienteException {
        if (batalhaEncerrada()) {
            return "A batalha já foi encerrada.";
        }

        StringBuilder log = new StringBuilder();
        log.append("[Turno ").append(this.turno).append("]\n");

        // Processa efeitos de início de turno
        String efeitoInicio = this.heroi.processarEfeitosInicioTurno();
        if (!efeitoInicio.isEmpty()) {
            log.append(efeitoInicio);
            if (!this.heroi.estaVivo()) {
                log.append(this.heroi.getNome()).append(" sucumbiu às chamas...\n");
                return log.toString();
            }
        }

        int hpAntes = this.monstro.getHpAtual();
        this.heroi.usarHabilidade(this.monstro);
        int danoCausado = hpAntes - this.monstro.getHpAtual();

        String reacao = this.heroi.getElemento().getDescricaoReacao(this.monstro.getElemento());

        log.append("✨ ").append(this.heroi.getNome()).append(" usou sua Habilidade Elemental (")
           .append(this.heroi.getElemento()).append(") contra ")
           .append(this.monstro.getNome()).append(reacao)
           .append(" causando ").append(danoCausado).append(" de dano!\n");

        if (this.monstro.estaVivo()) {
            log.append(turnoDoMonstro());
        } else {
            log.append("\n⭐ ").append(this.monstro.getNome()).append(" foi derrotado!");
        }

        return log.toString();
    }

    public String executarUsoItem(String nomeItem) throws ItemNaoEncontradoException {
        if (batalhaEncerrada()) {
            return "A batalha já foi encerrada.";
        }

        StringBuilder log = new StringBuilder();
        log.append("[Turno ").append(this.turno).append("]\n");

        // Processa efeitos de início de turno
        String efeitoInicio = this.heroi.processarEfeitosInicioTurno();
        if (!efeitoInicio.isEmpty()) {
            log.append(efeitoInicio);
            if (!this.heroi.estaVivo()) {
                log.append(this.heroi.getNome()).append(" sucumbiu às chamas...\n");
                return log.toString();
            }
        }

        this.heroi.usarItem(nomeItem);

        log.append("🧪 ").append(this.heroi.getNome()).append(" utilizou ")
           .append(nomeItem).append(" do inventário.\n");

        if (this.monstro.estaVivo()) {
            log.append(turnoDoMonstro());
        }

        return log.toString();
    }

    private String turnoDoMonstro() {
        this.turno++;
        String acao = this.monstro.agirNoTurno(this.heroi, this.turno);

        if (!this.heroi.estaVivo()) {
            acao += "\n\n💀 " + this.heroi.getNome() + " foi derrotado em combate...";
        }

        return acao;
    }
}
