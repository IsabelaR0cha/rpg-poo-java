package combate;

import excecoes.ItemNaoEncontradoException;
import excecoes.ManaInsuficienteException;
import itens.Item;
import itens.TipoItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private static final Random random = new Random();

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
        // 4 andares temáticos e balanceados da Torre dos Elementos
        this.guardioesAndares.add(new Monstro("Slime Gigante Pyro", 85, 19, 6, 1, Elemento.PYRO));
        this.guardioesAndares.add(new Monstro("Espectro das Marés", 120, 26, 8, 2, Elemento.HYDRO));
        this.guardioesAndares.add(new Monstro("Guardião dos Ventos", 155, 33, 10, 3, Elemento.ANEMO));
        this.guardioesAndares.add(new Monstro("Arconte da Tempestade", 215, 42, 14, 4, Elemento.ELECTRO));
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
            Monstro monstroDerrotado = this.batalhaAtual.getMonstro();

            // Caso tenha vencido um Mímico surpresa
            if (monstroDerrotado.isMimico()) {
                Item recompensaLendaria = new Item("Elixir dos Arcontes", TipoItem.CURA, 75);
                this.heroi.getInventario().adicionarItem(recompensaLendaria);
                this.andarAtual++;

                if (this.andarAtual >= this.guardioesAndares.size()) {
                    this.jogoFinalizado = true;
                    this.vitoriaFinal = true;
                    return "\n\n👑 [VITÓRIA ABSOLUTA] Você sobreviveu ao Mímico e conquistou os 4 andares da Torre!";
                } else {
                    iniciarBatalhaDoAndar();
                    return "\n\n💎 [MÍMICO EXTERMINADO] Você eliminou a criatura voraz e obteve: "
                            + recompensaLendaria.getNome() + "!\nAvançando para o Andar " + (this.andarAtual + 1) + "...";
                }
            }

            // Derrotou o guardião do andar
            this.heroi.subirNivel();

            // Se derrotou o último chefe (Andar 4)
            if (this.andarAtual >= this.guardioesAndares.size() - 1) {
                this.jogoFinalizado = true;
                this.vitoriaFinal = true;
                return "\n\n🏆 [VITÓRIA ÉPICA] Você subjugou o Arconte da Tempestade e conquistou a Torre dos Elementos!";
            }

            // Sorteio de Evento Aleatório entre os andares
            int sorteioEvento = random.nextInt(100);

            if (sorteioEvento < 30) {
                // Evento 1: Baú Mímico (Armadilha de combate surpresa!)
                Elemento elementoMimico = (this.heroi.getElemento() == Elemento.PYRO) ? Elemento.HYDRO : Elemento.PYRO;
                Monstro mimico = new Monstro("Mímico Voraz", 80 + (this.andarAtual * 20), 22 + (this.andarAtual * 5), 7, this.andarAtual + 1, elementoMimico, true);
                this.batalhaAtual = new Batalha(this.heroi, mimico);

                return "\n\n[SUBIU DE NÍVEL] Você avançou para o Nível " + this.heroi.getNivel() + "!"
                        + "\n⚠️ [EVENTO SURPRESA: BAÚ MÍMICO!]"
                        + "\nAo abrir o baú dourado das ruínas, dentes e garras afiadas saltam de dentro!"
                        + "\nUm MÍMICO VORAZ [" + elementoMimico + "] atacou em emboscada! Defenda-se!";
            } else if (sorteioEvento < 55) {
                // Evento 2: Fonte Mística Sagrada
                this.heroi.curar(50);
                this.heroi.recuperarMana(35);
                this.andarAtual++;
                iniciarBatalhaDoAndar();

                return "\n\n[SUBIU DE NÍVEL] Você avançou para o Nível " + this.heroi.getNivel() + "!"
                        + "\n✨ [EVENTO: FONTE MÍSTICA]"
                        + "\nVocê encontrou uma fonte de águas sagradas entre as pedras antigas."
                        + "\nVocê bebeu e recuperou +50 de HP e +35 de Mana!"
                        + "\nAvançando revitalizado para o Andar " + (this.andarAtual + 1) + "...";
            } else if (sorteioEvento < 75) {
                // Evento 3: Altar dos Titãs (Aumento de status permanente)
                this.heroi.aumentarAtaque(5);
                this.heroi.aumentarDefesa(2);
                this.andarAtual++;
                iniciarBatalhaDoAndar();

                return "\n\n[SUBIU DE NÍVEL] Você avançou para o Nível " + this.heroi.getNivel() + "!"
                        + "\n🏛️ [EVENTO: ALTAR DOS TITÃS]"
                        + "\nVocê tocou a relíquia no altar central e absorveu essência pura: +5 de Ataque e +2 de Defesa permanentes!"
                        + "\nAvançando para o Andar " + (this.andarAtual + 1) + "...";
            } else {
                // Evento 4: Baú de Tesouro com Item Poderoso
                Item recompensaBau = abrirBauDoAndar(this.andarAtual + 1);
                this.heroi.getInventario().adicionarItem(recompensaBau);
                this.andarAtual++;
                iniciarBatalhaDoAndar();

                return "\n\n[SUBIU DE NÍVEL] Você avançou para o Nível " + this.heroi.getNivel() + "!"
                        + "\n🎁 [EVENTO: BAÚ DE TESOURO]"
                        + "\nVocê encontrou no baú das ruínas: " + recompensaBau.getNome()
                        + "!\nAvançando para o Andar " + (this.andarAtual + 1) + "...";
            }
        } else {
            this.jogoFinalizado = true;
            this.vitoriaFinal = false;
            return "\n\n💀 [DERROTA] Fim de jogo no Andar " + (this.andarAtual + 1) + ".";
        }
    }

    private Item abrirBauDoAndar(int numeroAndar) {
        if (numeroAndar == 1) {
            return new Item("Poção de Cura Maior", TipoItem.CURA, 45);
        } else if (numeroAndar == 2) {
            return new Item("Éter Concentrado", TipoItem.MANA, 40);
        } else {
            return new Item("Elixir Elemental Sagrado", TipoItem.CURA, 65);
        }
    }

    public Heroi getHeroi() {
        return this.heroi;
    }

    public Batalha getBatalhaAtual() {
        return this.batalhaAtual;
    }

    public Monstro getMonstroAtual() {
        if (this.batalhaAtual != null) {
            return this.batalhaAtual.getMonstro();
        }
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

    public static Jogo restaurarJogo(Heroi heroi, int indiceAndar) {
        Jogo jogo = new Jogo(heroi);
        jogo.andarAtual = Math.max(0, Math.min(indiceAndar, jogo.guardioesAndares.size() - 1));
        jogo.jogoFinalizado = false;
        jogo.vitoriaFinal = false;
        jogo.iniciarBatalhaDoAndar();
        return jogo;
    }
}
