package combate;

import excecoes.ItemNaoEncontradoException;
import excecoes.ManaInsuficienteException;
import itens.Item;
import itens.TipoItem;
import java.util.ArrayList;
import java.util.List;
import modelo.Elemento;
import modelo.Heroi;
import modelo.Monstro;

public class Jogo {
    private Heroi heroi;
    private List<Monstro> guardioesAndares;
    private int andarAtual; // índice de 0 a 3 (Andares 1 a 4)
    private Batalha batalhaAtual;
    private boolean jogoFinalizado;
    private boolean vitoriaFinal;

    public Jogo(Heroi heroi) {
        this.heroi = heroi;
        this.guardioesAndares = new ArrayList<>();
        this.andarAtual = 0;
        this.jogoFinalizado = false;
        this.vitoriaFinal = false;

        inicializarAndares();
        iniciarBatalhaDoAndar();
    }

    private void inicializarAndares() {
        // 4 andares temáticos da Torre dos Elementos
        this.guardioesAndares.add(new Monstro("Slime Gigante Pyro", 65, 14, 4, 1, Elemento.PYRO));
        this.guardioesAndares.add(new Monstro("Espectro das Marés", 85, 18, 6, 2, Elemento.HYDRO));
        this.guardioesAndares.add(new Monstro("Guardião dos Ventos", 110, 22, 8, 3, Elemento.ANEMO));
        this.guardioesAndares.add(new Monstro("Arconte da Tempestade", 145, 28, 12, 4, Elemento.ELECTRO));
    }

    private void iniciarBatalhaDoAndar() {
        if (this.andarAtual < this.guardioesAndares.size()) {
            Monstro monstroDoAndar = this.guardioesAndares.get(this.andarAtual);
            this.batalhaAtual = new Batalha(this.heroi, monstroDoAndar);
        }
    }

    public String atacarNoTurno() {
        String resultado = this.batalhaAtual.executarAtaqueBasico();
        return resultado + verificarPosTurno();
    }

    public String usarHabilidadeNoTurno() throws ManaInsuficienteException {
        String resultado = this.batalhaAtual.executarHabilidade();
        return resultado + verificarPosTurno();
    }

    public String usarItemNoTurno(String nomeItem) throws ItemNaoEncontradoException {
        String resultado = this.batalhaAtual.executarUsoItem(nomeItem);
        return resultado + verificarPosTurno();
    }

    private String verificarPosTurno() {
        if (!this.batalhaAtual.batalhaEncerrada()) {
            return "";
        }

        if (this.batalhaAtual.heroiVenceu()) {
            this.heroi.subirNivel();
            Item recompensaBau = abrirBauDoAndar(this.andarAtual + 1);
            this.heroi.getInventario().adicionarItem(recompensaBau);

            this.andarAtual++;

            if (this.andarAtual >= this.guardioesAndares.size()) {
                this.jogoFinalizado = true;
                this.vitoriaFinal = true;
                return "\n\n[VITÓRIA] Você conquistou os 4 andares da Torre dos Elementos!";
            } else {
                iniciarBatalhaDoAndar();
                return "\n\n[ANDAR CONCLUÍDO] Você subiu para o nível " + this.heroi.getNivel()
                        + " e encontrou no baú: " + recompensaBau.getNome()
                        + "!\nAvançando para o Andar " + (this.andarAtual + 1) + "...";
            }
        } else {
            this.jogoFinalizado = true;
            this.vitoriaFinal = false;
            return "\n\n[DERROTA] Fim de jogo no Andar " + (this.andarAtual + 1) + ".";
        }
    }

    private Item abrirBauDoAndar(int numeroAndar) {
        if (numeroAndar == 1) {
            return new Item("Poção de Cura Maior", TipoItem.CURA, 40);
        } else if (numeroAndar == 2) {
            return new Item("Éter Concentrado", TipoItem.MANA, 35);
        } else {
            return new Item("Elixir Elemental", TipoItem.CURA, 60);
        }
    }

    public Heroi getHeroi() {
        return this.heroi;
    }

    public Batalha getBatalhaAtual() {
        return this.batalhaAtual;
    }

    public Monstro getMonstroAtual() {
        return this.guardioesAndares.get(this.andarAtual < this.guardioesAndares.size() ? this.andarAtual : this.guardioesAndares.size() - 1);
    }

    public int getNumeroAndarAtual() {
        return this.andarAtual + 1;
    }

    public boolean isJogoFinalizado() {
        return this.jogoFinalizado;
    }

    public boolean isVitoriaFinal() {
        return this.vitoriaFinal;
    }
}