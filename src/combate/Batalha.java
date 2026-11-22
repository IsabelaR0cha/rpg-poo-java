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

        // Verifica se o monstro esquiva
        if (random.nextDouble() < this.monstro.getChanceEsquiva()) {
            log.append("💨 ").append(this.monstro.getNome())
               .append(" esquivou agilmente do ataque de ").append(this.heroi.getNome()).append("!\n");
        } else {
            boolean critico = random.nextDouble() < this.heroi.getChanceCritico();
            int hpAntes = this.monstro.getHpAtual();

            this.heroi.atacar(this.monstro, critico);

            int danoCausado = hpAntes - this.monstro.getHpAtual();
            String msgCritico = critico ? " [💥 ACERTO CRÍTICO!]" : "";

            log.append(this.heroi.getNome()).append(" atacou ")
               .append(this.monstro.getNome()).append(msgCritico)
               .append(" causando ").append(danoCausado).append(" de dano.\n");
        }

        finalizarTurnoAposAcaoHeroi(log);
        return log.toString();
    }

    public String executarHabilidade() throws ManaInsuficienteException {
        if (batalhaEncerrada()) {
            return "A batalha já foi encerrada.";
        }

        // 1. Tenta usar a habilidade ANTES de qualquer efeito de turno.
        // Se lançar ManaInsuficienteException, nada do turno ou de efeitos é alterado!
        int hpAntes = this.monstro.getHpAtual();
        this.heroi.usarHabilidade(this.monstro);
        int danoCausado = hpAntes - this.monstro.getHpAtual();

        StringBuilder log = new StringBuilder();
        log.append("[Turno ").append(this.turno).append("]\n");

        String reacao = this.heroi.getElemento().getDescricaoReacao(this.monstro.getElemento());

        log.append("✨ ").append(this.heroi.getNome()).append(" usou ")
           .append(this.heroi.getNomeHabilidade()).append(" (")
           .append(this.heroi.getElemento()).append(") contra ")
           .append(this.monstro.getNome()).append(reacao)
           .append(" causando ").append(danoCausado).append(" de dano!\n");

        finalizarTurnoAposAcaoHeroi(log);
        return log.toString();
    }

    public String executarUsoItem(String nomeItem) throws ItemNaoEncontradoException {
        if (batalhaEncerrada()) {
            return "A batalha já foi encerrada.";
        }

        // 1. Tenta usar o item ANTES de qualquer efeito de turno.
        // Se lançar ItemNaoEncontradoException, nada do turno ou de efeitos é alterado!
        this.heroi.usarItem(nomeItem);

        StringBuilder log = new StringBuilder();
        log.append("[Turno ").append(this.turno).append("]\n");

        log.append("🧪 ").append(this.heroi.getNome()).append(" utilizou ")
           .append(nomeItem).append(" da mochila.\n");

        finalizarTurnoAposAcaoHeroi(log);
        return log.toString();
    }

    private void finalizarTurnoAposAcaoHeroi(StringBuilder log) {
        // Se o monstro sobreviveu, realiza o contra-ataque
        if (this.monstro.estaVivo()) {
            String acaoMonstro = this.monstro.agirNoTurno(this.heroi, this.turno);
            log.append(acaoMonstro);
        } else {
            log.append("\n⭐ ").append(this.monstro.getNome()).append(" foi derrotado!");
        }

        // Fim de um turno concluído com sucesso: processa efeitos contínuos válidos (ex: Queimadura)
        if (this.heroi.estaVivo()) {
            String efeitoResidual = this.heroi.processarEfeitosFimTurno();
            if (!efeitoResidual.isEmpty()) {
                log.append(efeitoResidual);
            }
        }

        if (!this.heroi.estaVivo()) {
            log.append("\n\n💀 ").append(this.heroi.getNome()).append(" foi derrotado...");
        }

        this.turno++;
    }
}
