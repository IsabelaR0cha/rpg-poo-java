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

    // Dados de suporte para Modal de Recompensa de Baú e Subida de Nível
    private boolean houveEventoRecompensa = false;
    private boolean subiuNivelNoTurno = false;
    private Item ultimoItemRecebido = null;
    private String ultimoEventoNome = null;
    private String ultimoEventoDescricao = null;

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
        // 4 andares temáticos da Torre dos Elementos com dificuldade calibrada e fluida
        this.guardioesAndares.add(new Monstro("Slime Gigante Pyro", 75, 17, 5, 1, Elemento.PYRO));
        this.guardioesAndares.add(new Monstro("Espectro das Marés", 105, 22, 6, 2, Elemento.HYDRO));
        this.guardioesAndares.add(new Monstro("Guardião dos Ventos", 135, 27, 8, 3, Elemento.ANEMO));
        this.guardioesAndares.add(new Monstro("Arconte da Tempestade", 185, 33, 11, 4, Elemento.ELECTRO));
    }

    public void iniciarBatalhaDoAndar() {
        if (this.andarAtual < this.guardioesAndares.size()) {
            Monstro monstroDoAndar = this.guardioesAndares.get(this.andarAtual);
            this.batalhaAtual = new Batalha(this.heroi, monstroDoAndar);
        }
    }

    public void limparEventoRecompensa() {
        this.houveEventoRecompensa = false;
        this.subiuNivelNoTurno = false;
        this.ultimoItemRecebido = null;
        this.ultimoEventoNome = null;
        this.ultimoEventoDescricao = null;
    }

    public String atacarNoTurno() {
        limparEventoRecompensa();
        if (this.jogoFinalizado) {
            return "O jogo já foi finalizado. Inicie um Novo Jogo ou Carregue um Checkpoint.";
        }
        if (this.batalhaAtual == null) {
            return "Nenhuma batalha ativa no momento.";
        }
        if (this.batalhaAtual.batalhaEncerrada()) {
            return "A batalha já foi encerrada." + verificarPosTurno();
        }

        String resultado = this.batalhaAtual.executarAtaqueBasico();
        return resultado + verificarPosTurno();
    }

    public String usarHabilidadeNoTurno() throws ManaInsuficienteException {
        limparEventoRecompensa();
        if (this.jogoFinalizado) {
            return "O jogo já foi finalizado. Inicie um Novo Jogo ou Carregue um Checkpoint.";
        }
        if (this.batalhaAtual == null) {
            return "Nenhuma batalha ativa no momento.";
        }
        if (this.batalhaAtual.batalhaEncerrada()) {
            return "A batalha já foi encerrada." + verificarPosTurno();
        }

        String resultado = this.batalhaAtual.executarHabilidade();
        return resultado + verificarPosTurno();
    }

    public String usarItemNoTurno(String nomeItem) throws ItemNaoEncontradoException {
        limparEventoRecompensa();
        if (this.jogoFinalizado) {
            return "O jogo já foi finalizado. Inicie um Novo Jogo ou Carregue um Checkpoint.";
        }
        if (this.batalhaAtual == null) {
            return "Nenhuma batalha ativa no momento.";
        }
        if (this.batalhaAtual.batalhaEncerrada()) {
            return "A batalha já foi encerrada." + verificarPosTurno();
        }

        String resultado = this.batalhaAtual.executarUsoItem(nomeItem);
        return resultado + verificarPosTurno();
    }

    private String verificarPosTurno() {
        if (this.jogoFinalizado) {
            return "";
        }
        if (this.batalhaAtual == null || !this.batalhaAtual.batalhaEncerrada()) {
            return "";
        }

        if (this.batalhaAtual.heroiVenceu()) {
            Monstro monstroDerrotado = this.batalhaAtual.getMonstro();

            // Caso tenha vencido um Mímico surpresa
            if (monstroDerrotado.isMimico()) {
                Item recompensaLendaria = new Item("Elixir dos Arcontes", TipoItem.CURA, 75, "Restaura 75 pontos de vida e regenera a vitalidade por completo.");
                this.heroi.getInventario().adicionarItem(recompensaLendaria);
                this.andarAtual++;

                this.houveEventoRecompensa = true;
                this.ultimoItemRecebido = recompensaLendaria;
                this.ultimoEventoNome = "👑 Baú Lendário dos Arcontes";
                this.ultimoEventoDescricao = "Você eliminou o Mímico Voraz e encontrou o sagrado " + recompensaLendaria.getNome() + "!";

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

            // Derrotou o guardião principal do andar -> sobe de nível
            this.heroi.subirNivel();
            this.subiuNivelNoTurno = true;

            // Se derrotou o último chefe (Andar 4)
            if (this.andarAtual >= this.guardioesAndares.size() - 1) {
                this.jogoFinalizado = true;
                this.vitoriaFinal = true;
                this.houveEventoRecompensa = true;
                this.ultimoEventoNome = "🏆 Triunfo sobre a Torre dos Elementos";
                this.ultimoEventoDescricao = "Você subjugou o Arconte da Tempestade e libertou o fluxo elemental da Torre!";
                return "\n\n🏆 [VITÓRIA ÉPICA] Você subjugou o Arconte da Tempestade e conquistou a Torre dos Elementos!";
            }

            // Sorteio de Evento Aleatório entre os andares
            int sorteioEvento = random.nextInt(100);

            if (sorteioEvento < 18) {
                // Evento 1: Baú Mímico (Armadilha de combate surpresa - probabilidade equilibrada em 18%)
                Elemento elementoMimico = (this.heroi.getElemento() == Elemento.PYRO) ? Elemento.HYDRO : Elemento.PYRO;
                Monstro mimico = new Monstro("Mímico Voraz", 75 + (this.andarAtual * 18), 20 + (this.andarAtual * 4), 6, this.andarAtual + 1, elementoMimico, true);
                this.batalhaAtual = new Batalha(this.heroi, mimico);

                this.houveEventoRecompensa = true;
                this.ultimoEventoNome = "⚠️ Emboscada: Baú Mímico!";
                this.ultimoEventoDescricao = "Ao abrir o baú dourado das ruínas, presas afiadas saltam de dentro! Um Mímico Voraz [" + elementoMimico + "] atacou!";

                return "\n\n[SUBIU DE NÍVEL] Você avançou para o Nível " + this.heroi.getNivel() + "!"
                        + "\n⚠️ [EVENTO SURPRESA: BAÚ MÍMICO!]"
                        + "\nAo abrir o baú dourado das ruínas, dentes e garras afiadas saltam de dentro!"
                        + "\nUm MÍMICO VORAZ [" + elementoMimico + "] atacou em emboscada! Defenda-se!";
            } else if (sorteioEvento < 45) {
                // Evento 2: Fonte Mística Sagrada
                this.heroi.curar(50);
                this.heroi.recuperarMana(35);
                this.andarAtual++;
                iniciarBatalhaDoAndar();

                this.houveEventoRecompensa = true;
                this.ultimoEventoNome = "✨ Fonte Mística Sagrada";
                this.ultimoEventoDescricao = "Você bebeu das águas sagradas entre as ruínas e recuperou +50 de Vida (HP) e +35 de Energia (Mana)!";

                return "\n\n[SUBIU DE NÍVEL] Você avançou para o Nível " + this.heroi.getNivel() + "!"
                        + "\n✨ [EVENTO: FONTE MÍSTICA]"
                        + "\nVocê encontrou uma fonte de águas sagradas entre as pedras antigas."
                        + "\nVocê bebeu e recuperou +50 de HP e +35 de Mana!"
                        + "\nAvançando revitalizado para o Andar " + (this.andarAtual + 1) + "...";
            } else if (sorteioEvento < 70) {
                // Evento 3: Altar dos Titãs (Aumento de status permanente)
                this.heroi.aumentarAtaque(5);
                this.heroi.aumentarDefesa(2);
                this.andarAtual++;
                iniciarBatalhaDoAndar();

                this.houveEventoRecompensa = true;
                this.ultimoEventoNome = "🏛️ Altar dos Titãs";
                this.ultimoEventoDescricao = "Você tocou a relíquia central e obteve a bênção elemental permanente: +5 de Ataque e +2 de Defesa!";

                return "\n\n[SUBIU DE NÍVEL] Você avançou para o Nível " + this.heroi.getNivel() + "!"
                        + "\n🏛️ [EVENTO: ALTAR DOS TITÃS]"
                        + "\nVocê tocou a relíquia no altar central e absorveu essência pura: +5 de Ataque e +2 de Defesa permanentes!"
                        + "\nAvançando para o Andar " + (this.andarAtual + 1) + "...";
            } else {
                // Evento 4: Baú de Tesouro com Item Poderoso (30% de chance)
                Item recompensaBau = abrirBauDoAndar(this.andarAtual + 1);
                this.heroi.getInventario().adicionarItem(recompensaBau);
                this.andarAtual++;
                iniciarBatalhaDoAndar();

                this.houveEventoRecompensa = true;
                this.ultimoItemRecebido = recompensaBau;
                this.ultimoEventoNome = "🎁 Baú de Tesouro do Andar " + (this.andarAtual);
                this.ultimoEventoDescricao = "Você desvendou o segredo do baú e encontrou: " + recompensaBau.getNome() + "!";

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
            return new Item("Poção de Cura Maior", TipoItem.CURA, 45, "Poção alquímica refinada que cura 45 HP e alivia queimaduras.");
        } else if (numeroAndar == 2) {
            return new Item("Éter Concentrado", TipoItem.MANA, 40, "Frasco de essência arcana que restaura 40 de Mana.");
        } else {
            return new Item("Elixir Elemental Sagrado", TipoItem.CURA, 65, "Elixir lendário que cura 65 HP e purifica o corpo de debuffs.");
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

    public int getAndarCheckpoint() {
        return this.andarAtual + 1;
    }

    public boolean isJogoFinalizado() {
        return this.jogoFinalizado;
    }

    public boolean isVitoriaFinal() {
        return this.vitoriaFinal;
    }

    public boolean isHouveEventoRecompensa() {
        return this.houveEventoRecompensa;
    }

    public boolean isSubiuNivelNoTurno() {
        return this.subiuNivelNoTurno;
    }

    public Item getUltimoItemRecebido() {
        return this.ultimoItemRecebido;
    }

    public String getUltimoEventoNome() {
        return this.ultimoEventoNome;
    }

    public String getUltimoEventoDescricao() {
        return this.ultimoEventoDescricao;
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
