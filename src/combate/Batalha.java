package combate;

import excecoes.ItemNaoEncontradoException;
import excecoes.ManaInsuficienteException;
import modelo.Heroi;
import modelo.Monstro;

public class Batalha {
    private Heroi heroi;
    private Monstro monstro;
    private int turno;

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

        int hpAntes = this.monstro.getHpAtual();
        this.heroi.atacar(this.monstro);
        int danoCausado = hpAntes - this.monstro.getHpAtual();

        StringBuilder log = new StringBuilder();
        log.append("[Turno ").append(this.turno).append("] ")
           .append(this.heroi.getNome()).append(" atacou ")
           .append(this.monstro.getNome()).append(" causando ")
           .append(danoCausado).append(" de dano.\n");

        if (this.monstro.estaVivo()) {
            log.append(turnoDoMonstro());
        } else {
            log.append(this.monstro.getNome()).append(" foi derrotado!");
        }

        return log.toString();
    }

    public String executarHabilidade() throws ManaInsuficienteException {
        if (batalhaEncerrada()) {
            return "A batalha já foi encerrada.";
        }

        int hpAntes = this.monstro.getHpAtual();
        this.heroi.usarHabilidade(this.monstro);
        int danoCausado = hpAntes - this.monstro.getHpAtual();

        StringBuilder log = new StringBuilder();
        log.append("[Turno ").append(this.turno).append("] ")
           .append(this.heroi.getNome()).append(" usou sua Habilidade Elemental em ")
           .append(this.monstro.getNome()).append(" causando ")
           .append(danoCausado).append(" de dano!\n");

        if (this.monstro.estaVivo()) {
            log.append(turnoDoMonstro());
        } else {
            log.append(this.monstro.getNome()).append(" foi derrotado!");
        }

        return log.toString();
    }

    public String executarUsoItem(String nomeItem) throws ItemNaoEncontradoException {
        if (batalhaEncerrada()) {
            return "A batalha já foi encerrada.";
        }

        this.heroi.usarItem(nomeItem);

        StringBuilder log = new StringBuilder();
        log.append("[Turno ").append(this.turno).append("] ")
           .append(this.heroi.getNome()).append(" usou ")
           .append(nomeItem).append(".\n");

        if (this.monstro.estaVivo()) {
            log.append(turnoDoMonstro());
        }

        return log.toString();
    }

    private String turnoDoMonstro() {
        int hpAntes = this.heroi.getHpAtual();
        this.monstro.atacar(this.heroi);
        int danoRecebido = hpAntes - this.heroi.getHpAtual();
        this.turno++;

        String resposta = this.monstro.getNome() + " contra-atacou causando "
                + danoRecebido + " de dano em " + this.heroi.getNome() + ".";

        if (!this.heroi.estaVivo()) {
            resposta += "\n" + this.heroi.getNome() + " foi derrotado...";
        }

        return resposta;
    }
}