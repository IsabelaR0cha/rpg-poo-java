package telas;

import combate.Jogo;
import excecoes.ItemNaoEncontradoException;
import excecoes.ManaInsuficienteException;
import excecoes.PersistenciaException;
import itens.Item;
import itens.TipoItem;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import modelo.Arcanista;
import modelo.Atirador;
import modelo.Catalisador;
import modelo.Elemento;
import modelo.Espadachim;
import modelo.Heroi;
import modelo.Monstro;
import persistencia.GerenciadorPersistencia;

public class TelaJogo extends JFrame {
    private Jogo jogo;
    private final GerenciadorPersistencia persistencia;

    // Paleta de Cores Estilo Abismo Elemental / Genshin Impact
    private static final Color BG_ESCURO = new Color(18, 20, 26);
    private static final Color BG_CARD = new Color(26, 30, 41);
    private static final Color BG_CARD_ALT = new Color(34, 39, 53);
    private static final Color BORDA_DOURADA = new Color(197, 160, 89);
    private static final Color TEXTO_DOURADO = new Color(241, 196, 15);
    private static final Color TEXTO_CLARO = new Color(245, 246, 250);
    private static final Color TEXTO_SECUNDARIO = new Color(178, 190, 195);
    private static final Color COR_PYRO = new Color(231, 76, 60);
    private static final Color COR_HYDRO = new Color(52, 152, 219);
    private static final Color COR_ANEMO = new Color(26, 188, 156);
    private static final Color COR_ELECTRO = new Color(155, 89, 182);

    // Componentes de Criação e Controle Superior
    private JTextField campoNome;
    private JComboBox<String> comboClasse;
    private JComboBox<Elemento> comboElemento;
    private JButton btnIniciar;
    private JButton btnSalvar;
    private JButton btnCarregar;

    // Painel de Prévia do Personagem
    private JPanel painelPrevia;
    private JLabel lblPreviaTitulo;
    private JLabel lblPreviaAtributos;
    private JLabel lblPreviaHabilidade;
    private JLabel lblPreviaPassiva;
    private JLabel lblPreviaReacoes;

    // Componentes da Arena de Batalha
    private JLabel lblAndarTitulo;
    private JPanel cardHeroi;
    private JPanel cardMonstro;
    private JLabel lblNomeHeroi;
    private JLabel lblDetalhesHeroi;
    private JProgressBar barraHpHeroi;
    private JProgressBar barraManaHeroi;

    private JLabel lblNomeMonstro;
    private JLabel lblDetalhesMonstro;
    private JProgressBar barraHpMonstro;

    private JTextArea areaLog;

    // Ações de Combate
    private JButton btnAtacar;
    private JButton btnHabilidade;
    private JButton btnUsarItem;
    private JButton btnVerInventario;

    public TelaJogo() {
        super("Torre dos Elementos — RPG POO");
        this.persistencia = new GerenciadorPersistencia();

        configurarJanela();
        inicializarComponentes();
        atualizarCardPrevia();
        atualizarEstadoBotoes(false);
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 720);
        setMinimumSize(new Dimension(860, 650));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_ESCURO);
        setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        // ================= PAINEL SUPERIOR: CRIAÇÃO & PRÉVIA =================
        JPanel painelNorte = new JPanel();
        painelNorte.setLayout(new BoxLayout(painelNorte, BoxLayout.Y_AXIS));
        painelNorte.setBackground(BG_ESCURO);
        painelNorte.setBorder(BorderFactory.createEmptyBorder(10, 12, 6, 12));

        // Linha 1: Controles de Criação e Checkpoint
        JPanel linhaControles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        linhaControles.setBackground(BG_CARD);
        linhaControles.setBorder(criarBordaDourada("Criação de Personagem & Checkpoints"));

        campoNome = new JTextField("Viajante", 9);
        estilizarCampoTexto(campoNome);

        comboClasse = new JComboBox<>(new String[]{"Espadachim", "Arcanista", "Atirador", "Catalisador"});
        estilizarComboBox(comboClasse);

        comboElemento = new JComboBox<>(Elemento.values());
        estilizarComboBox(comboElemento);

        btnIniciar = criarBotaoEstilizado("⚔️ Iniciar Jornada", new Color(41, 128, 185));
        btnSalvar = criarBotaoEstilizado("💾 Salvar Checkpoint", new Color(39, 174, 96));
        btnCarregar = criarBotaoEstilizado("📂 Carregar Checkpoint", new Color(142, 68, 173));

        linhaControles.add(criarLabelClara("Nome:"));
        linhaControles.add(campoNome);
        linhaControles.add(criarLabelClara("Classe:"));
        linhaControles.add(comboClasse);
        linhaControles.add(criarLabelClara("Elemento:"));
        linhaControles.add(comboElemento);
        linhaControles.add(btnIniciar);
        linhaControles.add(btnSalvar);
        linhaControles.add(btnCarregar);

        painelNorte.add(linhaControles);
        painelNorte.add(Box.createVerticalStrut(6));

        // Linha 2: Card de Prévia Visual Dinâmica
        painelPrevia = new JPanel(new GridLayout(2, 1, 4, 4));
        painelPrevia.setBackground(BG_CARD_ALT);
        painelPrevia.setBorder(criarBordaDourada("Prévia da Ressonância Elemental"));

        lblPreviaTitulo = new JLabel("Prévia do Personagem", JLabel.LEFT);
        lblPreviaTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPreviaTitulo.setForeground(TEXTO_DOURADO);

        lblPreviaAtributos = new JLabel("-", JLabel.LEFT);
        lblPreviaAtributos.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblPreviaAtributos.setForeground(TEXTO_CLARO);

        lblPreviaHabilidade = new JLabel("-", JLabel.LEFT);
        lblPreviaHabilidade.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblPreviaHabilidade.setForeground(TEXTO_CLARO);

        lblPreviaPassiva = new JLabel("-", JLabel.LEFT);
        lblPreviaPassiva.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblPreviaPassiva.setForeground(TEXTO_SECUNDARIO);

        lblPreviaReacoes = new JLabel("-", JLabel.LEFT);
        lblPreviaReacoes.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblPreviaReacoes.setForeground(TEXTO_SECUNDARIO);

        JPanel previaLinha1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 2));
        previaLinha1.setOpaque(false);
        previaLinha1.add(lblPreviaTitulo);
        previaLinha1.add(lblPreviaAtributos);

        JPanel previaLinha2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 2));
        previaLinha2.setOpaque(false);
        previaLinha2.add(lblPreviaHabilidade);
        previaLinha2.add(lblPreviaPassiva);

        painelPrevia.add(previaLinha1);
        painelPrevia.add(previaLinha2);

        painelNorte.add(painelPrevia);
        add(painelNorte, BorderLayout.NORTH);

        // ================= PAINEL CENTRAL: ARENA DE COMBATE =================
        JPanel painelCentro = new JPanel(new BorderLayout(8, 8));
        painelCentro.setBackground(BG_ESCURO);
        painelCentro.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

        // Cabeçalho da Arena: Banner Central do Andar
        lblAndarTitulo = new JLabel("Torre dos Elementos — Selecione seu Herói", JLabel.CENTER);
        lblAndarTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblAndarTitulo.setForeground(TEXTO_DOURADO);
        lblAndarTitulo.setBorder(BorderFactory.createEmptyBorder(4, 0, 6, 0));

        // Arena: Cards do Herói e do Monstro
        JPanel painelArena = new JPanel(new GridLayout(1, 2, 14, 0));
        painelArena.setOpaque(false);

        // Card do Herói
        cardHeroi = new JPanel();
        cardHeroi.setLayout(new BoxLayout(cardHeroi, BoxLayout.Y_AXIS));
        cardHeroi.setBackground(BG_CARD);
        cardHeroi.setBorder(criarBordaDourada("Viajante (Você)"));

        lblNomeHeroi = new JLabel("Herói: Aguardando Início", JLabel.LEFT);
        lblNomeHeroi.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblNomeHeroi.setForeground(TEXTO_CLARO);

        lblDetalhesHeroi = new JLabel("ATQ: - | DEF: -", JLabel.LEFT);
        lblDetalhesHeroi.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblDetalhesHeroi.setForeground(TEXTO_SECUNDARIO);

        barraHpHeroi = criarBarraProgresso("HP: - / -", new Color(46, 204, 113));
        barraManaHeroi = criarBarraProgresso("Mana: - / -", new Color(52, 152, 219));

        cardHeroi.add(lblNomeHeroi);
        cardHeroi.add(Box.createVerticalStrut(4));
        cardHeroi.add(barraHpHeroi);
        cardHeroi.add(Box.createVerticalStrut(4));
        cardHeroi.add(barraManaHeroi);
        cardHeroi.add(Box.createVerticalStrut(4));
        cardHeroi.add(lblDetalhesHeroi);

        // Card do Monstro
        cardMonstro = new JPanel();
        cardMonstro.setLayout(new BoxLayout(cardMonstro, BoxLayout.Y_AXIS));
        cardMonstro.setBackground(BG_CARD);
        cardMonstro.setBorder(criarBordaDourada("Guardião do Andar"));

        lblNomeMonstro = new JLabel("Monstro: -", JLabel.LEFT);
        lblNomeMonstro.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblNomeMonstro.setForeground(TEXTO_CLARO);

        lblDetalhesMonstro = new JLabel("ATQ: - | DEF: -", JLabel.LEFT);
        lblDetalhesMonstro.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblDetalhesMonstro.setForeground(TEXTO_SECUNDARIO);

        barraHpMonstro = criarBarraProgresso("HP: - / -", new Color(231, 76, 60));

        cardMonstro.add(lblNomeMonstro);
        cardMonstro.add(Box.createVerticalStrut(4));
        cardMonstro.add(barraHpMonstro);
        cardMonstro.add(Box.createVerticalStrut(4));
        cardMonstro.add(lblDetalhesMonstro);
        cardMonstro.add(Box.createVerticalStrut(18)); // Alinhamento estético

        painelArena.add(cardHeroi);
        painelArena.add(cardMonstro);

        // Área de Log de Batalha Estilizada
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.setBackground(new Color(14, 16, 22));
        areaLog.setForeground(new Color(236, 240, 241));
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaLog.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(criarBordaDourada("Crônicas de Combate"));
        scrollLog.setBackground(BG_ESCURO);

        JPanel painelStatusECombate = new JPanel(new BorderLayout(0, 6));
        painelStatusECombate.setOpaque(false);
        painelStatusECombate.add(lblAndarTitulo, BorderLayout.NORTH);
        painelStatusECombate.add(painelArena, BorderLayout.CENTER);

        painelCentro.add(painelStatusECombate, BorderLayout.NORTH);
        painelCentro.add(scrollLog, BorderLayout.CENTER);

        add(painelCentro, BorderLayout.CENTER);

        // ================= PAINEL INFERIOR: AÇÕES DE BATALHA =================
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 10));
        painelAcoes.setBackground(BG_CARD);
        painelAcoes.setBorder(criarBordaDourada("Comandos de Combate"));

        btnAtacar = criarBotaoEstilizado("⚔️ Ataque Normal", new Color(192, 57, 43));
        btnHabilidade = criarBotaoEstilizado("✨ Habilidade Elemental", new Color(41, 128, 185));
        btnUsarItem = criarBotaoEstilizado("🧪 Usar Item Rápido", new Color(39, 174, 96));
        btnVerInventario = criarBotaoEstilizado("🎒 Ver Mochila", new Color(211, 84, 0));

        painelAcoes.add(btnAtacar);
        painelAcoes.add(btnHabilidade);
        painelAcoes.add(btnUsarItem);
        painelAcoes.add(btnVerInventario);

        add(painelAcoes, BorderLayout.SOUTH);

        registrarEventos();
    }

    private void registrarEventos() {
        comboClasse.addActionListener(e -> atualizarCardPrevia());
        comboElemento.addActionListener(e -> atualizarCardPrevia());

        btnIniciar.addActionListener(e -> iniciarNovoJogo());

        btnAtacar.addActionListener(e -> {
            if (jogo == null || jogo.isJogoFinalizado()) {
                return;
            }
            adicionarLog(jogo.atacarNoTurno());
            atualizarInterface();
        });

        btnHabilidade.addActionListener(e -> {
            if (jogo == null || jogo.isJogoFinalizado()) {
                return;
            }
            try {
                adicionarLog(jogo.usarHabilidadeNoTurno());
            } catch (ManaInsuficienteException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Energia Insuficiente", JOptionPane.WARNING_MESSAGE);
            }
            atualizarInterface();
        });

        btnUsarItem.addActionListener(e -> abrirSeletorItemRapido());

        btnVerInventario.addActionListener(e -> abrirJanelaInventario());

        btnSalvar.addActionListener(e -> {
            if (jogo == null) {
                return;
            }
            try {
                persistencia.salvarJogo(jogo);
                int andarCheckpoint = jogo.getAndarCheckpoint();
                JOptionPane.showMessageDialog(
                        this,
                        "Checkpoint salvo com sucesso no Andar " + andarCheckpoint + "!\n"
                        + "Nota: Ao recarregar, você reiniciará o combate no início deste andar com seus itens e atributos atuais.",
                        "Checkpoint Salvo",
                        JOptionPane.INFORMATION_MESSAGE
                );
                adicionarLog("\n💾 [CHECKPOINT] Progresso salvo no início do Andar " + andarCheckpoint + ".");
            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao Salvar", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCarregar.addActionListener(e -> {
            try {
                this.jogo = persistencia.carregarJogo();
                areaLog.setText("");
                int andar = jogo.getNumeroAndarAtual();
                JOptionPane.showMessageDialog(
                        this,
                        "Checkpoint do Andar " + andar + " carregado com sucesso!\n"
                        + "A batalha recomeça no início do andar.",
                        "Checkpoint Carregado",
                        JOptionPane.INFORMATION_MESSAGE
                );
                adicionarLog("✨ [CHECKPOINT CARREGADO] Você retomou sua jornada no início do Andar " + andar + ".");
                atualizarEstadoBotoes(true);
                atualizarInterface();
            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao Carregar", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void atualizarCardPrevia() {
        String classe = (String) comboClasse.getSelectedItem();
        Elemento elemento = (Elemento) comboElemento.getSelectedItem();

        if (classe == null || elemento == null) {
            return;
        }

        Heroi previa = criarHeroi(classe, "Prévia", elemento);
        Color corElem = obterCorDoElemento(elemento);

        lblPreviaTitulo.setText(classe + " — " + elemento.name() + " (" + obterIconeElemento(elemento) + ")");
        lblPreviaTitulo.setForeground(corElem);

        lblPreviaAtributos.setText("HP Base: " + previa.getHpMax() + " | Mana: " + previa.getManaMax()
                + " | ATQ: " + previa.getAtaque() + " | DEF: " + previa.getDefesa());

        lblPreviaHabilidade.setText("Habilidade: " + previa.getNomeHabilidade()
                + " (" + previa.getCustoManaHabilidade() + " Mana) — " + previa.getDescricaoHabilidade());

        lblPreviaPassiva.setText("Passiva: " + previa.getDescricaoPassiva());
    }

    private void iniciarNovoJogo() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            nome = "Viajante";
        }

        String classeEscolhida = (String) comboClasse.getSelectedItem();
        Elemento elementoEscolhido = (Elemento) comboElemento.getSelectedItem();

        Heroi heroi = criarHeroi(classeEscolhida, nome, elementoEscolhido);
        this.jogo = new Jogo(heroi);

        areaLog.setText("");
        adicionarLog("🌟 [INÍCIO DA EXPEDIÇÃO] " + heroi.getNome() + " (" + classeEscolhida + " | "
                + elementoEscolhido + ") ingressou na Torre dos Elementos!\n"
                + "Objetivo: Conquistar os 4 andares e subjugar os Arcontes Elementais.");

        atualizarEstadoBotoes(true);
        atualizarInterface();
    }

    private Heroi criarHeroi(String classe, String nome, Elemento elemento) {
        switch (classe) {
            case "Arcanista":
                return new Arcanista(nome, 90, 25, 5, 75, elemento);
            case "Atirador":
                return new Atirador(nome, 95, 24, 7, 55, elemento);
            case "Catalisador":
                return new Catalisador(nome, 85, 23, 5, 80, elemento);
            case "Espadachim":
            default:
                return new Espadachim(nome, 115, 22, 10, 40, elemento);
        }
    }

    private void atualizarInterface() {
        if (jogo == null) {
            return;
        }

        Heroi heroi = jogo.getHeroi();
        Monstro monstro = jogo.getMonstroAtual();

        // Atualização do Herói
        String queimando = heroi.estaQueimando() ? " <font color='#e74c3c'><b>(🔥 Queimando: " + heroi.getTurnosQueimando() + "t)</b></font>" : "";
        lblNomeHeroi.setText("<html><b>" + heroi.getNome() + "</b> (Nv. " + heroi.getNivel() + ") — "
                + heroi.getClass().getSimpleName() + " [" + heroi.getElemento() + "]" + queimando + "</html>");

        barraHpHeroi.setMaximum(heroi.getHpMax());
        barraHpHeroi.setValue(heroi.getHpAtual());
        barraHpHeroi.setString("HP: " + heroi.getHpAtual() + " / " + heroi.getHpMax());
        barraHpHeroi.setForeground(heroi.getHpAtual() < (heroi.getHpMax() * 0.35) ? new Color(231, 76, 60) : new Color(46, 204, 113));

        barraManaHeroi.setMaximum(heroi.getManaMax());
        barraManaHeroi.setValue(heroi.getManaAtual());
        barraManaHeroi.setString("Mana: " + heroi.getManaAtual() + " / " + heroi.getManaMax());

        lblDetalhesHeroi.setText("ATQ: " + heroi.getAtaque() + " | DEF: " + heroi.getDefesa()
                + " | Mochila: " + heroi.getInventario().getItens().size() + " itens");

        // Atualização do Monstro
        Color corElementoMonstro = obterCorDoElemento(monstro.getElemento());
        String tagMimico = monstro.isMimico() ? " <font color='#e74c3c'><b>[MÍMICO VORAZ!]</b></font>" : "";

        lblNomeMonstro.setText("<html><b>" + monstro.getNome() + "</b> [" + monstro.getElemento() + "]" + tagMimico + "</html>");

        barraHpMonstro.setMaximum(monstro.getHpMax());
        barraHpMonstro.setValue(monstro.getHpAtual());
        barraHpMonstro.setString("HP: " + monstro.getHpAtual() + " / " + monstro.getHpMax());
        barraHpMonstro.setForeground(corElementoMonstro);

        lblDetalhesMonstro.setText("ATQ: " + monstro.getAtaque() + " | DEF: " + monstro.getDefesa()
                + " | Andar " + monstro.getAndar());

        // Adaptação Visual da Borda conforme Elemento do Monstro/Andar
        cardMonstro.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(corElementoMonstro, 2),
                "Guardião Atual [" + monstro.getElemento() + "]",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12),
                corElementoMonstro
        ));

        // Cabeçalho Central do Andar
        String extraAndar = monstro.isMimico() ? " — ⚠️ EMBOSCADA DO MÍMICO!" : " — Guardião dos Elementos";
        lblAndarTitulo.setText("Torre dos Elementos — Andar " + jogo.getNumeroAndarAtual() + " / 4" + extraAndar);
        lblAndarTitulo.setForeground(corElementoMonstro);

        // Bloqueio de Ações se o jogo acabou
        if (jogo.isJogoFinalizado()) {
            atualizarEstadoBotoes(false);
            btnCarregar.setEnabled(true);
            btnIniciar.setEnabled(true);
        }
    }

    private void abrirSeletorItemRapido() {
        if (jogo == null || jogo.isJogoFinalizado()) {
            return;
        }

        List<Item> itens = jogo.getHeroi().getInventario().getItens();
        if (itens.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sua mochila está vazia!", "Sem Itens", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Agrupa nomes únicos de itens disponíveis
        List<String> opcoesItens = new ArrayList<>();
        for (Item item : itens) {
            String desc = item.getNome() + " (" + (item.getTipo() == TipoItem.CURA ? "+" + item.getValorEfeito() + " HP" : "+" + item.getValorEfeito() + " Mana") + ")";
            if (!opcoesItens.contains(desc)) {
                opcoesItens.add(desc);
            }
        }

        String itemEscolhido = (String) JOptionPane.showInputDialog(
                this,
                "Selecione o item que deseja consumir:",
                "Consumir Item",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoesItens.toArray(),
                opcoesItens.get(0)
        );

        if (itemEscolhido != null) {
            // Extrai o nome puro do item
            String nomePuro = itemEscolhido.substring(0, itemEscolhido.indexOf(" ("));
            try {
                adicionarLog(jogo.usarItemNoTurno(nomePuro));
            } catch (ItemNaoEncontradoException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Item Indisponível", JOptionPane.WARNING_MESSAGE);
            }
            atualizarInterface();
        }
    }

    private void abrirJanelaInventario() {
        if (jogo == null) {
            JOptionPane.showMessageDialog(this, "Inicie ou carregue um jogo para acessar a mochila.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Mochila do Viajante — Itens & Consumíveis", true);
        dialog.setSize(480, 360);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BG_ESCURO);
        dialog.setLayout(new BorderLayout(8, 8));

        DefaultListModel<String> modelLista = new DefaultListModel<>();
        Map<String, String> mapaDescricoes = new LinkedHashMap<>();

        atualizarModeloInventario(modelLista, mapaDescricoes);

        JList<String> listaItens = new JList<>(modelLista);
        listaItens.setBackground(BG_CARD);
        listaItens.setForeground(TEXTO_CLARO);
        listaItens.setFont(new Font("SansSerif", Font.PLAIN, 13));
        listaItens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaItens.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
                if (isSelected) {
                    label.setBackground(new Color(41, 128, 185));
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(index % 2 == 0 ? BG_CARD : BG_CARD_ALT);
                    label.setForeground(TEXTO_CLARO);
                }
                return label;
            }
        });

        JScrollPane scrollLista = new JScrollPane(listaItens);
        scrollLista.setBorder(criarBordaDourada("Itens na Mochila"));

        // Painel inferior de ações da mochila
        JPanel painelBotoesMochila = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        painelBotoesMochila.setBackground(BG_CARD);

        JButton btnUsarSelecionado = criarBotaoEstilizado("✨ Usar Selecionado", new Color(39, 174, 96));
        JButton btnFechar = criarBotaoEstilizado("Fechar", new Color(127, 140, 141));

        btnUsarSelecionado.setEnabled(!jogo.isJogoFinalizado());

        btnUsarSelecionado.addActionListener(e -> {
            String itemLinha = listaItens.getSelectedValue();
            if (itemLinha == null) {
                JOptionPane.showMessageDialog(dialog, "Selecione um item da lista para usar.", "Nenhum Item Selecionado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Extrai o nome real do item da linha selecionada (antes de ' x')
            String nomeItem = itemLinha.substring(itemLinha.indexOf("] ") + 2);
            if (nomeItem.contains(" x")) {
                nomeItem = nomeItem.substring(0, nomeItem.lastIndexOf(" x"));
            }

            try {
                adicionarLog(jogo.usarItemNoTurno(nomeItem));
                atualizarInterface();
                atualizarModeloInventario(modelLista, mapaDescricoes);
                if (modelLista.isEmpty()) {
                    btnUsarSelecionado.setEnabled(false);
                }
            } catch (ItemNaoEncontradoException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnFechar.addActionListener(e -> dialog.dispose());

        painelBotoesMochila.add(btnUsarSelecionado);
        painelBotoesMochila.add(btnFechar);

        dialog.add(scrollLista, BorderLayout.CENTER);
        dialog.add(painelBotoesMochila, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void atualizarModeloInventario(DefaultListModel<String> model, Map<String, String> mapaDescricoes) {
        model.clear();
        mapaDescricoes.clear();

        List<Item> itens = jogo.getHeroi().getInventario().getItens();
        if (itens.isEmpty()) {
            model.addElement("Sua mochila está vazia!");
            return;
        }

        // Agrupa por nome para contar quantidade
        Map<String, Integer> contagem = new LinkedHashMap<>();
        Map<String, Item> exemplos = new LinkedHashMap<>();

        for (Item it : itens) {
            contagem.put(it.getNome(), contagem.getOrDefault(it.getNome(), 0) + 1);
            exemplos.putIfAbsent(it.getNome(), it);
        }

        for (Map.Entry<String, Integer> entry : contagem.entrySet()) {
            Item it = exemplos.get(entry.getKey());
            String tipoTag = (it.getTipo() == TipoItem.CURA) ? "[CURA + " + it.getValorEfeito() + " HP]" : "[MANA + " + it.getValorEfeito() + " Mana]";
            String linha = tipoTag + " " + it.getNome() + " x" + entry.getValue();
            model.addElement(linha);
            mapaDescricoes.put(it.getNome(), linha);
        }
    }

    private void atualizarEstadoBotoes(boolean partidaAtiva) {
        btnAtacar.setEnabled(partidaAtiva);
        btnHabilidade.setEnabled(partidaAtiva);
        btnUsarItem.setEnabled(partidaAtiva);
        btnVerInventario.setEnabled(partidaAtiva);
        btnSalvar.setEnabled(partidaAtiva);
    }

    private void adicionarLog(String texto) {
        if (texto == null || texto.isEmpty()) {
            return;
        }
        if (!areaLog.getText().isEmpty()) {
            areaLog.append("\n\n");
        }
        areaLog.append(texto);
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    // ================= HELPERS VISUAIS E ESTILIZAÇÃO =================
    private TitledBorder criarBordaDourada(String titulo) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDA_DOURADA, 1),
                titulo,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12),
                TEXTO_DOURADO
        );
    }

    private JLabel criarLabelClara(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(TEXTO_CLARO);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return label;
    }

    private void estilizarCampoTexto(JTextField campo) {
        campo.setBackground(BG_CARD_ALT);
        campo.setForeground(TEXTO_CLARO);
        campo.setCaretColor(TEXTO_DOURADO);
        campo.setBorder(BorderFactory.createLineBorder(BORDA_DOURADA, 1));
        campo.setFont(new Font("SansSerif", Font.PLAIN, 12));
    }

    private void estilizarComboBox(JComboBox<?> combo) {
        combo.setBackground(BG_CARD_ALT);
        combo.setForeground(TEXTO_CLARO);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 12));
    }

    private JButton criarBotaoEstilizado(String texto, Color corFundo) {
        JButton btn = new JButton(texto);
        btn.setBackground(corFundo);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA_DOURADA, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        return btn;
    }

    private JProgressBar criarBarraProgresso(String textoInicial, Color corPreenchimento) {
        JProgressBar barra = new JProgressBar();
        barra.setStringPainted(true);
        barra.setString(textoInicial);
        barra.setForeground(corPreenchimento);
        barra.setBackground(new Color(24, 28, 38));
        barra.setFont(new Font("SansSerif", Font.BOLD, 11));
        barra.setBorder(BorderFactory.createLineBorder(new Color(60, 65, 80), 1));
        barra.setPreferredSize(new Dimension(barra.getPreferredSize().width, 22));
        return barra;
    }

    private Color obterCorDoElemento(Elemento elemento) {
        if (elemento == null) return BORDA_DOURADA;
        switch (elemento) {
            case PYRO: return COR_PYRO;
            case HYDRO: return COR_HYDRO;
            case ANEMO: return COR_ANEMO;
            case ELECTRO: return COR_ELECTRO;
            default: return BORDA_DOURADA;
        }
    }

    private String obterIconeElemento(Elemento elemento) {
        if (elemento == null) return "✨";
        switch (elemento) {
            case PYRO: return "🔥 Fogo";
            case HYDRO: return "💧 Água";
            case ANEMO: return "🌪️ Vento";
            case ELECTRO: return "⚡ Trovão";
            default: return "✨";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaJogo tela = new TelaJogo();
            tela.setVisible(true);
        });
    }
}
