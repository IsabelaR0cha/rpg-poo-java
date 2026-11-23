package telas;

import combate.Jogo;
import excecoes.ManaInsuficienteException;
import excecoes.PersistenciaException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;
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

    // Gerenciador de Telas / Abas do Caderno
    private CardLayout cardLayout;
    private JPanel painelContainer;
    private static final String TELA_SELECAO = "SELECAO";
    private static final String TELA_BATALHA = "BATALHA";

    // Abas de Marcador de Livro (Top Tabs)
    private JButton tabSelecao;
    private JButton tabBatalha;

    // ================= CONTROLES DA TELA DE SELEÇÃO =================
    private JTextField campoNome;
    private String classeSelecionada = "Espadachim";
    private Elemento elementoSelecionado = Elemento.PYRO;

    private JPanel[] cardsClasse;
    private final String[] nomesClasses = {"Espadachim", "Arcanista", "Atirador", "Catalisador"};
    private JButton[] botoesElementos;

    // Prévia do Herói na Seleção
    private JLabel lblPreviaNomeClasse;
    private JLabel lblPreviaAtributos;
    private JLabel lblPreviaHabilidade;
    private JLabel lblPreviaPassiva;
    private JButton btnIniciar;
    private JButton btnCarregarCheckpoint;

    // ================= CONTROLES DA TELA DE BATALHA =================
    private JLabel lblAndarBanner;
    private JPanel painelCabecalhoAndar;

    // Card do Herói na Batalha
    private JPanel cardBatalhaHeroi;
    private JLabel lblHeroiTitulo;
    private JLabel lblHeroiStatus;
    private JProgressBar barraHpHeroi;
    private JProgressBar barraManaHeroi;

    // Card do Monstro na Batalha
    private JPanel cardBatalhaMonstro;
    private JLabel lblMonstroTitulo;
    private JLabel lblMonstroStatus;
    private JProgressBar barraHpMonstro;

    // Log e Ações
    private JTextArea areaLog;
    private JButton btnLimparLog;
    private JButton btnAtacar;
    private JButton btnHabilidade;
    private JButton btnMochila;
    private JButton btnSalvar;
    private JButton btnCarregar;
    private JButton btnReiniciar;
    private JMenuItem menuItemSalvar;

    public TelaJogo() {
        super("Torre dos Elementos — Diário de Aventuras RPG");
        this.persistencia = new GerenciadorPersistencia();

        configurarJanela();
        inicializarComponentes();
        configurarTooltips();
        atualizarPreviaSelecao();
        exibirTela(TELA_SELECAO);
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 720);
        setMinimumSize(new Dimension(900, 680));
        setLocationRelativeTo(null);
        getContentPane().setBackground(TemaFantasia.MADEIRA_ESCURA);
        setLayout(new BorderLayout(0, 0));
    }

    private void inicializarComponentes() {
        setJMenuBar(criarBarraMenu());

        // ================= BARRA SUPERIOR: ABAS DE MARCADOR DE LIVRO =================
        JPanel painelAbasSuperiores = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        painelAbasSuperiores.setBackground(TemaFantasia.MADEIRA_ESCURA);
        painelAbasSuperiores.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, TemaFantasia.OURO_BORDA));

        tabSelecao = TemaFantasia.criarBotaoMadeira("📜 Ficha do Herói / Criação", TemaFantasia.MADEIRA_MEDIA);
        tabBatalha = TemaFantasia.criarBotaoMadeira("🏰 Torre dos Elementos (Batalha)", TemaFantasia.COR_PYRO);

        tabSelecao.addActionListener(e -> exibirTela(TELA_SELECAO));
        tabBatalha.addActionListener(e -> {
            if (jogo != null) {
                exibirTela(TELA_BATALHA);
            } else {
                JOptionPane.showMessageDialog(this, "Inicie uma Nova Aventura ou Carregue um Checkpoint primeiro.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        painelAbasSuperiores.add(tabSelecao);
        painelAbasSuperiores.add(tabBatalha);
        add(painelAbasSuperiores, BorderLayout.NORTH);

        // ================= CONTAINER COM CARDLAYOUT =================
        cardLayout = new CardLayout();
        painelContainer = new JPanel(cardLayout);
        painelContainer.setBackground(TemaFantasia.PERGAMINHO_CLARO);

        painelContainer.add(criarPainelSelecao(), TELA_SELECAO);
        painelContainer.add(criarPainelBatalha(), TELA_BATALHA);

        add(painelContainer, BorderLayout.CENTER);
    }

    // =========================================================================
    // TELA 1: SELEÇÃO DE HERÓI (ESTILO CHARACTER / CARDS)
    // =========================================================================
    private JPanel criarPainelSelecao() {
        JPanel painel = new JPanel(new BorderLayout(12, 12));
        painel.setBackground(TemaFantasia.PERGAMINHO_CLARO);
        painel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // Título e Nome do Viajante
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        painelTopo.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelTopo.setBorder(TemaFantasia.criarBordaMadeiraPergaminho("Identidade do Herói"));

        JLabel lblNome = new JLabel("Nome do Viajante:");
        lblNome.setFont(TemaFantasia.FONTE_TEXTO_BOLD);
        lblNome.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);

        campoNome = new JTextField("Aether", 14);
        campoNome.setFont(TemaFantasia.FONTE_TEXTO_BOLD);
        campoNome.setBackground(TemaFantasia.PERGAMINHO_DESTACADO);
        campoNome.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);
        campoNome.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaFantasia.MADEIRA_ESCURA, 2),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        painelTopo.add(lblNome);
        painelTopo.add(campoNome);

        painel.add(painelTopo, BorderLayout.NORTH);

        // Centro: Cards de Classes e Seleção de Elemento
        JPanel painelCentro = new JPanel(new BorderLayout(10, 10));
        painelCentro.setOpaque(false);

        // Grade com os 4 Cards de Classe
        JPanel painelClasses = new JPanel(new GridLayout(1, 4, 10, 0));
        painelClasses.setOpaque(false);
        painelClasses.setBorder(TemaFantasia.criarBordaMadeiraPergaminho("Escolha sua Classe de Combate"));

        cardsClasse = new JPanel[nomesClasses.length];
        for (int i = 0; i < nomesClasses.length; i++) {
            final String nomeClasse = nomesClasses[i];
            cardsClasse[i] = criarCardClasse(nomeClasse);
            painelClasses.add(cardsClasse[i]);
        }

        // Seletor de Elementos em Abas Coloridas
        JPanel painelElementos = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 8));
        painelElementos.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelElementos.setBorder(TemaFantasia.criarBordaMadeiraPergaminho("Afinidade Elemental Dominante"));

        Elemento[] elementos = Elemento.values();
        botoesElementos = new JButton[elementos.length];

        for (int i = 0; i < elementos.length; i++) {
            final Elemento elem = elementos[i];
            JButton btnElem = TemaFantasia.criarBotaoMadeira(TemaFantasia.obterIconeElemento(elem), TemaFantasia.obterCorElemento(elem));
            btnElem.setPreferredSize(new Dimension(150, 36));
            btnElem.addActionListener(e -> {
                this.elementoSelecionado = elem;
                atualizarBotoesElementos();
                atualizarPreviaSelecao();
            });
            botoesElementos[i] = btnElem;
            painelElementos.add(btnElem);
        }

        JPanel blocoSelecao = new JPanel(new BorderLayout(8, 8));
        blocoSelecao.setOpaque(false);
        blocoSelecao.add(painelClasses, BorderLayout.CENTER);
        blocoSelecao.add(painelElementos, BorderLayout.SOUTH);

        // Card de Prévia Completa
        JPanel painelPrevia = criarPainelPreviaCompleta();

        painelCentro.add(blocoSelecao, BorderLayout.CENTER);
        painelCentro.add(painelPrevia, BorderLayout.SOUTH);
        painel.add(painelCentro, BorderLayout.CENTER);

        // Painel Inferior de Confirmação e Carregamento
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 10));
        painelBotoes.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelBotoes.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, TemaFantasia.MADEIRA_ESCURA));

        btnIniciar = TemaFantasia.criarBotaoMadeira("⚔️ Iniciar Aventura na Torre", new Color(180, 83, 9));
        btnIniciar.setPreferredSize(new Dimension(230, 40));
        btnIniciar.setFont(new Font("Serif", Font.BOLD, 14));

        btnCarregarCheckpoint = TemaFantasia.criarBotaoMadeira("📂 Carregar Checkpoint Salvo", TemaFantasia.COR_HYDRO);
        btnCarregarCheckpoint.setPreferredSize(new Dimension(220, 40));

        btnIniciar.addActionListener(e -> iniciarNovaPartida());
        btnCarregarCheckpoint.addActionListener(e -> carregarJogoSalvo());

        painelBotoes.add(btnCarregarCheckpoint);
        painelBotoes.add(btnIniciar);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        atualizarSelecaoCardClasse();
        atualizarBotoesElementos();

        return painel;
    }

    private JPanel criarCardClasse(String nomeClasse) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(TemaFantasia.PERGAMINHO_CLARO);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaFantasia.MADEIRA_CLARA, 1, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        card.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        String icone = "⚔️";
        String arquetipo = "Guerreiro";
        String statusResumo = "HP: 115 | Mana: 40";

        switch (nomeClasse) {
            case "Arcanista":
                icone = "🔮";
                arquetipo = "Mago Elemental";
                statusResumo = "HP: 90 | Mana: 75";
                break;
            case "Atirador":
                icone = "🏹";
                arquetipo = "Caçador Crítico";
                statusResumo = "HP: 95 | Mana: 55";
                break;
            case "Catalisador":
                icone = "📖";
                arquetipo = "Mago Contínuo";
                statusResumo = "HP: 85 | Mana: 80";
                break;
        }

        JLabel lblIcone = new JLabel(icone, JLabel.CENTER);
        lblIcone.setFont(new Font("SansSerif", Font.PLAIN, 28));
        lblIcone.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNome = new JLabel(nomeClasse, JLabel.CENTER);
        lblNome.setFont(TemaFantasia.FONTE_SUBTITULO);
        lblNome.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);
        lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblArq = new JLabel(arquetipo, JLabel.CENTER);
        lblArq.setFont(TemaFantasia.FONTE_PEQUENA);
        lblArq.setForeground(TemaFantasia.TEXTO_SEPIA_MEDIO);
        lblArq.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblStatus = new JLabel(statusResumo, JLabel.CENTER);
        lblStatus.setFont(TemaFantasia.FONTE_PEQUENA);
        lblStatus.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblIcone);
        card.add(Box.createVerticalStrut(4));
        card.add(lblNome);
        card.add(lblArq);
        card.add(Box.createVerticalStrut(6));
        card.add(lblStatus);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                classeSelecionada = nomeClasse;
                atualizarSelecaoCardClasse();
                atualizarPreviaSelecao();
            }
        });

        return card;
    }

    private void atualizarSelecaoCardClasse() {
        for (int i = 0; i < nomesClasses.length; i++) {
            boolean ativo = nomesClasses[i].equals(classeSelecionada);
            if (ativo) {
                cardsClasse[i].setBackground(TemaFantasia.PERGAMINHO_DESTACADO);
                cardsClasse[i].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(TemaFantasia.MADEIRA_ESCURA, 3, true),
                        BorderFactory.createLineBorder(TemaFantasia.OURO_BORDA, 1)
                ));
            } else {
                cardsClasse[i].setBackground(TemaFantasia.PERGAMINHO_CLARO);
                cardsClasse[i].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(TemaFantasia.MADEIRA_CLARA, 1, true),
                        BorderFactory.createEmptyBorder(2, 2, 2, 2)
                ));
            }
        }
        revalidate();
        repaint();
    }

    private void atualizarBotoesElementos() {
        Elemento[] elementos = Elemento.values();
        for (int i = 0; i < elementos.length; i++) {
            boolean ativo = (elementos[i] == elementoSelecionado);
            if (ativo) {
                botoesElementos[i].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 2, true),
                        BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            } else {
                botoesElementos[i].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(TemaFantasia.MADEIRA_ESCURA, 1, true),
                        BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            }
        }
    }

    private JPanel criarPainelPreviaCompleta() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(TemaFantasia.PERGAMINHO_DESTACADO);
        painel.setBorder(TemaFantasia.criarBordaMadeiraPergaminho("Ficha de Prévia do Personagem"));

        JPanel linhaSuperior = new JPanel(new GridLayout(1, 2, 16, 0));
        linhaSuperior.setOpaque(false);
        linhaSuperior.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblPreviaNomeClasse = new JLabel("-");
        lblPreviaNomeClasse.setFont(new Font("Serif", Font.BOLD, 14));
        lblPreviaNomeClasse.setForeground(TemaFantasia.MADEIRA_ESCURA);

        lblPreviaAtributos = new JLabel("-");
        lblPreviaAtributos.setFont(TemaFantasia.FONTE_TEXTO_BOLD);
        lblPreviaAtributos.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);

        linhaSuperior.add(lblPreviaNomeClasse);
        linhaSuperior.add(lblPreviaAtributos);

        JPanel linhaInferior = new JPanel(new GridLayout(1, 2, 16, 0));
        linhaInferior.setOpaque(false);
        linhaInferior.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblPreviaHabilidade = new JLabel("-");
        lblPreviaHabilidade.setFont(TemaFantasia.FONTE_TEXTO);
        lblPreviaHabilidade.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);
        lblPreviaHabilidade.setVerticalAlignment(JLabel.TOP);

        lblPreviaPassiva = new JLabel("-");
        lblPreviaPassiva.setFont(TemaFantasia.FONTE_PEQUENA);
        lblPreviaPassiva.setForeground(TemaFantasia.TEXTO_SEPIA_MEDIO);
        lblPreviaPassiva.setVerticalAlignment(JLabel.TOP);

        linhaInferior.add(lblPreviaHabilidade);
        linhaInferior.add(lblPreviaPassiva);

        painel.add(linhaSuperior);
        painel.add(Box.createVerticalStrut(6));
        painel.add(linhaInferior);

        return painel;
    }

    private void atualizarPreviaSelecao() {
        Heroi h = instanciarHeroi(classeSelecionada, "Prévia", elementoSelecionado);
        Color corElem = TemaFantasia.obterCorElemento(elementoSelecionado);

        lblPreviaNomeClasse.setText("<html>Classe: <b>" + classeSelecionada + "</b> | Elemento: <font color='"
                + String.format("#%06x", (corElem.getRGB() & 0xFFFFFF)) + "'><b>" + elementoSelecionado.name() + "</b></font></html>");

        lblPreviaAtributos.setText("HP Base: " + h.getHpMax() + "  |  Mana: " + h.getManaMax()
                + "  |  ATQ: " + h.getAtaque() + "  |  DEF: " + h.getDefesa());

        lblPreviaHabilidade.setText("<html><div style='width:305px;'><b>Habilidade:</b> " + h.getNomeHabilidade()
                + " (Custo: " + h.getCustoManaHabilidade() + " Mana)<br><small>" + h.getDescricaoHabilidade() + "</small></div></html>");

        lblPreviaPassiva.setText("<html><div style='width:305px;'><b>Passiva:</b> " + h.getDescricaoPassiva() + "</div></html>");
    }

    // =========================================================================
    // TELA 2: BATALHA NA TORRE DOS ELEMENTOS (ESTILO LIVRO DE AVENTURA)
    // =========================================================================
    private JPanel criarPainelBatalha() {
        JPanel painel = new JPanel(new BorderLayout(10, 8));
        painel.setBackground(TemaFantasia.PERGAMINHO_CLARO);
        painel.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        // Topo: Banner do Andar Atual com a cor do Elemento Dominante
        painelCabecalhoAndar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        painelCabecalhoAndar.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelCabecalhoAndar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaFantasia.MADEIRA_ESCURA, 2, true),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));

        lblAndarBanner = new JLabel("Andar 1 / 4 — Slime Gigante Pyro [PYRO]", JLabel.CENTER);
        lblAndarBanner.setFont(new Font("Serif", Font.BOLD, 16));
        lblAndarBanner.setForeground(TemaFantasia.MADEIRA_ESCURA);
        painelCabecalhoAndar.add(lblAndarBanner);

        painel.add(painelCabecalhoAndar, BorderLayout.NORTH);

        // Centro: Cards do Herói e do Monstro + Diário de Batalha (Log)
        JPanel painelCentro = new JPanel(new BorderLayout(8, 8));
        painelCentro.setOpaque(false);

        // Painel lado a lado (Ficha Herói e Ficha Monstro)
        JPanel painelCombatentes = new JPanel(new GridLayout(1, 2, 14, 0));
        painelCombatentes.setOpaque(false);

        // Card do Herói
        cardBatalhaHeroi = new JPanel();
        cardBatalhaHeroi.setLayout(new BoxLayout(cardBatalhaHeroi, BoxLayout.Y_AXIS));
        cardBatalhaHeroi.setBackground(TemaFantasia.PERGAMINHO_DESTACADO);
        cardBatalhaHeroi.setBorder(TemaFantasia.criarBordaMadeiraPergaminho("Viajante (Herói)"));

        lblHeroiTitulo = new JLabel("Aether (Nv. 1) — Espadachim [PYRO]");
        lblHeroiTitulo.setFont(TemaFantasia.FONTE_SUBTITULO);
        lblHeroiTitulo.setForeground(TemaFantasia.MADEIRA_ESCURA);
        lblHeroiTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblHeroiTitulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        barraHpHeroi = TemaFantasia.criarBarraProgresso("HP: 115 / 115", new Color(46, 125, 50), TemaFantasia.MADEIRA_ESCURA);
        barraHpHeroi.setAlignmentX(Component.LEFT_ALIGNMENT);
        barraHpHeroi.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        barraManaHeroi = TemaFantasia.criarBarraProgresso("Mana: 40 / 40", new Color(25, 118, 210), TemaFantasia.MADEIRA_ESCURA);
        barraManaHeroi.setAlignmentX(Component.LEFT_ALIGNMENT);
        barraManaHeroi.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        lblHeroiStatus = new JLabel("ATQ: 22 | DEF: 10 | Mochila: 2 itens");
        lblHeroiStatus.setFont(TemaFantasia.FONTE_TEXTO);
        lblHeroiStatus.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);
        lblHeroiStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblHeroiStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));

        cardBatalhaHeroi.add(lblHeroiTitulo);
        cardBatalhaHeroi.add(Box.createVerticalStrut(6));
        cardBatalhaHeroi.add(barraHpHeroi);
        cardBatalhaHeroi.add(Box.createVerticalStrut(6));
        cardBatalhaHeroi.add(barraManaHeroi);
        cardBatalhaHeroi.add(Box.createVerticalStrut(6));
        cardBatalhaHeroi.add(lblHeroiStatus);

        // Card do Monstro
        cardBatalhaMonstro = new JPanel();
        cardBatalhaMonstro.setLayout(new BoxLayout(cardBatalhaMonstro, BoxLayout.Y_AXIS));
        cardBatalhaMonstro.setBackground(TemaFantasia.PERGAMINHO_DESTACADO);
        cardBatalhaMonstro.setBorder(TemaFantasia.criarBordaMetalicaComTitulo("Guardião do Andar", TemaFantasia.COR_PYRO));

        lblMonstroTitulo = new JLabel("Slime Gigante Pyro [PYRO]");
        lblMonstroTitulo.setFont(TemaFantasia.FONTE_SUBTITULO);
        lblMonstroTitulo.setForeground(TemaFantasia.COR_PYRO);
        lblMonstroTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMonstroTitulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        barraHpMonstro = TemaFantasia.criarBarraProgresso("HP: 85 / 85", TemaFantasia.COR_PYRO, TemaFantasia.MADEIRA_ESCURA);
        barraHpMonstro.setAlignmentX(Component.LEFT_ALIGNMENT);
        barraHpMonstro.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        lblMonstroStatus = new JLabel("ATQ: 19 | DEF: 6 | Andar 1");
        lblMonstroStatus.setFont(TemaFantasia.FONTE_TEXTO);
        lblMonstroStatus.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);
        lblMonstroStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMonstroStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));

        cardBatalhaMonstro.add(lblMonstroTitulo);
        cardBatalhaMonstro.add(Box.createVerticalStrut(6));
        cardBatalhaMonstro.add(barraHpMonstro);
        cardBatalhaMonstro.add(Box.createVerticalStrut(12));
        cardBatalhaMonstro.add(lblMonstroStatus);
        cardBatalhaMonstro.add(Box.createVerticalGlue());

        painelCombatentes.add(cardBatalhaHeroi);
        painelCombatentes.add(cardBatalhaMonstro);

        // Área de Log Pergaminho
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.setBackground(TemaFantasia.PERGAMINHO_LOG);
        areaLog.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaLog.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        DefaultCaret caret = (DefaultCaret) areaLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JPanel painelLog = new JPanel(new BorderLayout(0, 4));
        painelLog.setBackground(TemaFantasia.PERGAMINHO_CLARO);
        painelLog.setBorder(TemaFantasia.criarBordaMadeiraPergaminho("Diário de Combate da Torre"));

        JPanel painelTopoLog = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 2));
        painelTopoLog.setOpaque(false);

        btnLimparLog = TemaFantasia.criarBotaoMadeira("Limpar Log", TemaFantasia.MADEIRA_MEDIA);
        btnLimparLog.setFont(TemaFantasia.FONTE_PEQUENA);
        btnLimparLog.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        btnLimparLog.addActionListener(e -> limparLog());
        painelTopoLog.add(btnLimparLog);

        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createLineBorder(TemaFantasia.OURO_BORDA, 1));

        painelLog.add(painelTopoLog, BorderLayout.NORTH);
        painelLog.add(scrollLog, BorderLayout.CENTER);

        JPanel painelMeio = new JPanel(new BorderLayout(0, 8));
        painelMeio.setOpaque(false);
        painelMeio.add(painelCombatentes, BorderLayout.NORTH);
        painelMeio.add(painelLog, BorderLayout.CENTER);

        painelCentro.add(painelMeio, BorderLayout.CENTER);
        painel.add(painelCentro, BorderLayout.CENTER);

        // Inferior: Botões de Ação de Combate
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        painelAcoes.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelAcoes.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, TemaFantasia.MADEIRA_ESCURA));

        btnAtacar = TemaFantasia.criarBotaoMadeira("⚔️ Atacar", new Color(180, 40, 30));
        btnHabilidade = TemaFantasia.criarBotaoMadeira("✨ Habilidade", new Color(25, 118, 210));
        btnMochila = TemaFantasia.criarBotaoMadeira("🎒 Mochila", new Color(46, 125, 50));
        btnSalvar = TemaFantasia.criarBotaoMadeira("💾 Salvar", new Color(180, 83, 9));
        btnCarregar = TemaFantasia.criarBotaoMadeira("📂 Carregar", TemaFantasia.MADEIRA_MEDIA);
        btnReiniciar = TemaFantasia.criarBotaoMadeira("📜 Novo Herói", new Color(110, 71, 42));

        btnAtacar.addActionListener(e -> executarAtaque());
        btnHabilidade.addActionListener(e -> executarHabilidade());
        btnMochila.addActionListener(e -> abrirMochila());
        btnSalvar.addActionListener(e -> salvarProgresso());
        btnCarregar.addActionListener(e -> carregarJogoSalvo());
        btnReiniciar.addActionListener(e -> solicitarNovoJogo());

        painelAcoes.add(btnAtacar);
        painelAcoes.add(btnHabilidade);
        painelAcoes.add(btnMochila);
        painelAcoes.add(btnSalvar);
        painelAcoes.add(btnCarregar);
        painelAcoes.add(btnReiniciar);

        painel.add(painelAcoes, BorderLayout.SOUTH);

        return painel;
    }

    private void exibirTela(String nomeTela) {
        cardLayout.show(painelContainer, nomeTela);
        if (TELA_SELECAO.equals(nomeTela)) {
            tabSelecao.setBackground(TemaFantasia.OURO_VELHO);
            tabBatalha.setBackground(TemaFantasia.MADEIRA_MEDIA);
        } else {
            tabSelecao.setBackground(TemaFantasia.MADEIRA_MEDIA);
            tabBatalha.setBackground(TemaFantasia.COR_PYRO);
        }
    }

    // =========================================================================
    // LÓGICA DE AÇÕES E EVENTOS
    // =========================================================================
    private boolean confirmarAbandonoPartidaAtiva(String acao) {
        if (jogo == null || jogo.isJogoFinalizado()) {
            return true;
        }
        int opcao = JOptionPane.showConfirmDialog(
                this,
                "Há uma partida em andamento no Andar " + jogo.getNumeroAndarAtual()
                        + ".\nDeseja realmente " + acao + "? O progresso não salvo será perdido.",
                "Partida em Andamento",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        return opcao == JOptionPane.YES_OPTION;
    }

    private void solicitarNovoJogo() {
        if (confirmarAbandonoPartidaAtiva("iniciar um novo jogo")) {
            exibirTela(TELA_SELECAO);
            campoNome.requestFocusInWindow();
        }
    }

    private void limparLog() {
        if (!areaLog.getText().isEmpty()) {
            areaLog.setText("");
            areaLog.setCaretPosition(0);
        }
    }

    private void iniciarNovaPartida() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe um nome para o seu herói antes de iniciar a aventura.",
                    "Nome Obrigatório",
                    JOptionPane.WARNING_MESSAGE
            );
            campoNome.requestFocusInWindow();
            return;
        }
        if (nome.contains(";")) {
            JOptionPane.showMessageDialog(
                    this,
                    "O nome do herói não pode conter o caractere ';'.",
                    "Nome Inválido",
                    JOptionPane.WARNING_MESSAGE
            );
            campoNome.requestFocusInWindow();
            return;
        }
        if (!confirmarAbandonoPartidaAtiva("iniciar uma nova partida")) {
            return;
        }

        Heroi heroi = instanciarHeroi(classeSelecionada, nome, elementoSelecionado);
        this.jogo = new Jogo(heroi);

        areaLog.setText("");
        adicionarLog("📜 [INÍCIO DA EXPEDIÇÃO] " + heroi.getNome() + " (" + classeSelecionada + " - "
                + elementoSelecionado + ") ingressou nos 4 andares da Torre dos Elementos!\n"
                + "Aventure-se pelas ruínas e conquiste os segredos do Abismo Elemental.");

        atualizarInterfaceBatalha();
        exibirTela(TELA_BATALHA);
    }

    private Heroi instanciarHeroi(String classe, String nome, Elemento elem) {
        switch (classe) {
            case "Arcanista":
                return new Arcanista(nome, 90, 25, 5, 75, elem);
            case "Atirador":
                return new Atirador(nome, 95, 24, 7, 55, elem);
            case "Catalisador":
                return new Catalisador(nome, 85, 23, 5, 80, elem);
            case "Espadachim":
            default:
                return new Espadachim(nome, 115, 22, 10, 40, elem);
        }
    }

    private void executarAtaque() {
        if (jogo == null || jogo.isJogoFinalizado()) {
            return;
        }

        String log = jogo.atacarNoTurno();
        adicionarLog(log);
        verificarRecompensaEAtualizar();
    }

    private void executarHabilidade() {
        if (jogo == null || jogo.isJogoFinalizado()) {
            return;
        }

        try {
            String log = jogo.usarHabilidadeNoTurno();
            adicionarLog(log);
            verificarRecompensaEAtualizar();
        } catch (ManaInsuficienteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Energia Insuficiente", JOptionPane.WARNING_MESSAGE);
            atualizarInterfaceBatalha();
        }
    }

    private void abrirMochila() {
        if (jogo == null || jogo.isJogoFinalizado()) {
            JOptionPane.showMessageDialog(this, "Não há batalha ativa para acessar a mochila.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        DialogInventario dlg = new DialogInventario(this, jogo, log -> {
            adicionarLog(log);
            verificarRecompensaEAtualizar();
        });
        dlg.setVisible(true);
    }

    private void verificarRecompensaEAtualizar() {
        atualizarInterfaceBatalha();

        if (jogo != null && jogo.isJogoFinalizado()) {
            jogo.limparEventoRecompensa();
            DialogFimDeJogo dlgFim = new DialogFimDeJogo(
                    this,
                    jogo.isVitoriaFinal(),
                    jogo.getHeroi(),
                    jogo.getNumeroAndarAtual(),
                    persistencia.existeSave(),
                    () -> carregarJogoSalvo(),
                    () -> solicitarNovoJogo()
            );
            dlgFim.setVisible(true);
            return;
        }

        if (jogo != null && jogo.isHouveEventoRecompensa()) {
            DialogRecompensaBau dlgRecompensa = new DialogRecompensaBau(
                    this,
                    jogo.getHeroi().getNivel(),
                    jogo.isSubiuNivelNoTurno(),
                    jogo.getUltimoEventoNome(),
                    jogo.getUltimoEventoDescricao(),
                    jogo.getUltimoItemRecebido()
            );
            dlgRecompensa.setVisible(true);
            jogo.limparEventoRecompensa();
            atualizarInterfaceBatalha();
        }
    }

    private void salvarProgresso() {
        if (jogo == null || jogo.isJogoFinalizado()) {
            return;
        }
        try {
            persistencia.salvarJogo(jogo);
            int andarCheckpoint = jogo.getAndarCheckpoint();
            JOptionPane.showMessageDialog(
                    this,
                    "Checkpoint salvo com sucesso no início do Andar " + andarCheckpoint + "!\n"
                    + "Ao carregar, você continuará a partir do início deste andar.",
                    "Checkpoint Salvo",
                    JOptionPane.INFORMATION_MESSAGE
            );
            adicionarLog("💾 [CHECKPOINT SALVO] Progresso preservado no início do Andar " + andarCheckpoint + ".");
            atualizarTooltipsCheckpoint();
        } catch (PersistenciaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao Salvar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarJogoSalvo() {
        try {
            this.jogo = persistencia.carregarJogo();
            areaLog.setText("");
            int andar = jogo.getNumeroAndarAtual();
            JOptionPane.showMessageDialog(
                    this,
                    "Checkpoint do Andar " + andar + " carregado com sucesso!\n"
                    + "O combate foi reiniciado no início do andar.",
                    "Checkpoint Carregado",
                    JOptionPane.INFORMATION_MESSAGE
            );
            adicionarLog("✨ [CHECKPOINT CARREGADO] Retomando a jornada no início do Andar " + andar + ".");
            atualizarInterfaceBatalha();
            exibirTela(TELA_BATALHA);
        } catch (PersistenciaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao Carregar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarInterfaceBatalha() {
        if (jogo == null) {
            return;
        }

        Heroi heroi = jogo.getHeroi();
        Monstro monstro = jogo.getMonstroAtual();

        // 1. Atualização do Herói
        String queimando = heroi.estaQueimando() ? " <font color='#C0392B'><b>(🔥 Em Chamas!)</b></font>" : "";
        lblHeroiTitulo.setText("<html><b>" + heroi.getNome() + "</b> (Nv. " + heroi.getNivel() + ") — "
                + heroi.getClass().getSimpleName() + " [" + heroi.getElemento() + "]" + queimando + "</html>");

        barraHpHeroi.setMaximum(heroi.getHpMax());
        barraHpHeroi.setValue(heroi.getHpAtual());
        barraHpHeroi.setString("HP: " + heroi.getHpAtual() + " / " + heroi.getHpMax());
        barraHpHeroi.setForeground(heroi.getHpAtual() < (heroi.getHpMax() * 0.35) ? TemaFantasia.COR_PYRO : new Color(46, 125, 50));
        String dicaHpHeroi = "Vida do herói: " + heroi.getHpAtual() + " de " + heroi.getHpMax();
        if (heroi.estaQueimando()) {
            dicaHpHeroi += " (Queimadura ativa por " + heroi.getTurnosQueimando() + " turno(s))";
        } else if (heroi.getHpAtual() < (heroi.getHpMax() * 0.35)) {
            dicaHpHeroi += " (HP crítico! Considere usar uma poção de cura)";
        }
        barraHpHeroi.setToolTipText(dicaHpHeroi);

        barraManaHeroi.setMaximum(heroi.getManaMax());
        barraManaHeroi.setValue(heroi.getManaAtual());
        barraManaHeroi.setString("Mana: " + heroi.getManaAtual() + " / " + heroi.getManaMax());
        barraManaHeroi.setToolTipText("Mana atual: " + heroi.getManaAtual() + " / " + heroi.getManaMax()
                + " (Habilidade custa " + heroi.getCustoManaHabilidade() + " MP)");

        int qtdItens = heroi.getInventario().getItens().size();
        String statusQueimadura = heroi.estaQueimando()
                ? " | Queimando (" + heroi.getTurnosQueimando() + "t)"
                : "";
        lblHeroiStatus.setText("ATQ: " + heroi.getAtaque() + " | DEF: " + heroi.getDefesa()
                + " | Mochila: " + qtdItens + (qtdItens == 1 ? " item" : " itens") + statusQueimadura);

        // 2. Atualização do Monstro
        Color corElementoMonstro = TemaFantasia.obterCorElemento(monstro.getElemento());
        String tagMimico = monstro.isMimico() ? " <font color='#C0392B'><b>[MÍMICO VORAZ!]</b></font>" : "";

        lblMonstroTitulo.setText("<html><b>" + monstro.getNome() + "</b> [" + monstro.getElemento() + "]" + tagMimico + "</html>");
        lblMonstroTitulo.setForeground(corElementoMonstro);

        barraHpMonstro.setMaximum(monstro.getHpMax());
        barraHpMonstro.setValue(monstro.getHpAtual());
        barraHpMonstro.setString("HP: " + monstro.getHpAtual() + " / " + monstro.getHpMax());
        barraHpMonstro.setForeground(corElementoMonstro);
        barraHpMonstro.setToolTipText("HP de " + monstro.getNome() + ": " + monstro.getHpAtual() + " / " + monstro.getHpMax());

        lblMonstroStatus.setText("ATQ: " + monstro.getAtaque() + " | DEF: " + monstro.getDefesa()
                + " | Andar " + monstro.getAndar());

        // Moldura do Monstro adota a cor do elemento dominante
        cardBatalhaMonstro.setBorder(TemaFantasia.criarBordaMetalicaComTitulo(
                monstro.isMimico() ? "Emboscada — Mímico Voraz" : "Guardião do Andar",
                corElementoMonstro
        ));

        // 3. Banner do Andar Atual e Alertas de Vitória/Derrota
        if (jogo.isJogoFinalizado() && jogo.isVitoriaFinal()) {
            painelCabecalhoAndar.setBackground(new Color(232, 245, 233));
            lblAndarBanner.setText("VITÓRIA NA TORRE DOS ELEMENTOS — Todos os 4 Andares Conquistados!");
            lblAndarBanner.setForeground(new Color(27, 94, 32));
        } else if (jogo.isJogoFinalizado() && !jogo.isVitoriaFinal()) {
            painelCabecalhoAndar.setBackground(new Color(253, 236, 234));
            lblAndarBanner.setText("DERROTA NO ANDAR " + jogo.getNumeroAndarAtual()
                    + " / 4 — Carregue o Checkpoint ou inicie um Novo Jogo");
            lblAndarBanner.setForeground(TemaFantasia.COR_PYRO);
        } else {
            String extra = monstro.isMimico() ? " — ⚠️ EMBOSCADA DE MÍMICO!" : " — Guardião Elemental";
            painelCabecalhoAndar.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
            lblAndarBanner.setText("Torre dos Elementos — Andar " + jogo.getNumeroAndarAtual() + " / 4" + extra);
            lblAndarBanner.setForeground(corElementoMonstro);
        }

        // Estado e Dicas dos Botões de Ação
        boolean partidaAtiva = !jogo.isJogoFinalizado();
        btnAtacar.setEnabled(partidaAtiva);
        btnHabilidade.setEnabled(partidaAtiva);
        btnHabilidade.setText("✨ Habilidade (" + heroi.getCustoManaHabilidade() + " MP)");
        btnHabilidade.setToolTipText("<html><b>" + heroi.getNomeHabilidade() + "</b> ("
                + heroi.getCustoManaHabilidade() + " Mana)<br>" + heroi.getDescricaoHabilidade() + "</html>");
        btnMochila.setEnabled(partidaAtiva);
        btnMochila.setText("🎒 Mochila (" + qtdItens + ")");
        btnSalvar.setEnabled(partidaAtiva);
        btnCarregar.setEnabled(true);
        btnReiniciar.setEnabled(true);
        if (menuItemSalvar != null) {
            menuItemSalvar.setEnabled(partidaAtiva);
        }
        atualizarTooltipsCheckpoint();
    }

    private void configurarTooltips() {
        tabSelecao.setToolTipText("Abre a ficha de criação e personalização do herói");
        tabBatalha.setToolTipText("Alterna para a tela de combate na Torre dos Elementos");
        campoNome.setToolTipText("Digite o nome do seu viajante (não utilize ';')");

        for (int i = 0; i < nomesClasses.length; i++) {
            Heroi exemplo = instanciarHeroi(nomesClasses[i], nomesClasses[i], Elemento.PYRO);
            cardsClasse[i].setToolTipText("<html><b>" + nomesClasses[i] + "</b> — HP: " + exemplo.getHpMax()
                    + " | Mana: " + exemplo.getManaMax() + " | ATQ: " + exemplo.getAtaque()
                    + " | DEF: " + exemplo.getDefesa() + "<br>Habilidade: " + exemplo.getNomeHabilidade()
                    + " (" + exemplo.getCustoManaHabilidade() + " MP)<br>" + exemplo.getDescricaoPassiva() + "</html>");
        }

        Elemento[] elementos = Elemento.values();
        for (int i = 0; i < elementos.length; i++) {
            botoesElementos[i].setToolTipText(obterDicaElemento(elementos[i]));
        }

        btnIniciar.setToolTipText("Inicia a aventura na Torre dos Elementos com o herói selecionado");
        btnAtacar.setToolTipText("Realiza um ataque básico físico contra o guardião atual");
        btnHabilidade.setToolTipText("Conjura a habilidade elemental da classe (consome Mana)");
        btnMochila.setToolTipText("Abre o inventário para usar poções de cura ou éteres de mana");
        btnSalvar.setToolTipText("Salva o progresso atual no arquivo save_jogo.txt");
        btnReiniciar.setToolTipText("Retorna à criação de personagem para iniciar com um novo herói");
        btnLimparLog.setToolTipText("Limpa todo o histórico exibido no diário de combate");
        barraHpHeroi.setToolTipText("Vida atual do herói");
        barraManaHeroi.setToolTipText("Mana disponível para conjurar habilidades elementais");
        barraHpMonstro.setToolTipText("Vida restante do guardião do andar");

        atualizarTooltipsCheckpoint();
    }

    private String obterDicaElemento(Elemento elem) {
        switch (elem) {
            case PYRO:
                return "PYRO: forte contra Anemo (x1.5) e Electro (x1.4); fraco contra Hydro (x0.8)";
            case HYDRO:
                return "HYDRO: forte contra Pyro (x1.6) e Electro (x1.3)";
            case ANEMO:
                return "ANEMO: causa Dispersão Elemental (x1.35) contra qualquer outro elemento";
            case ELECTRO:
                return "ELECTRO: forte contra Hydro (x1.6) e Pyro (x1.3)";
            default:
                return elem.name();
        }
    }

    private void atualizarTooltipsCheckpoint() {
        String dica = persistencia.existeSave()
                ? "Carrega o último checkpoint salvo em save_jogo.txt"
                : "Nenhum checkpoint salvo encontrado no momento (save_jogo.txt)";
        if (btnCarregarCheckpoint != null) {
            btnCarregarCheckpoint.setToolTipText(dica);
        }
        if (btnCarregar != null) {
            btnCarregar.setToolTipText(dica);
        }
    }

    private JMenuBar criarBarraMenu() {
        JMenuBar barraMenu = new JMenuBar();
        barraMenu.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        barraMenu.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, TemaFantasia.MADEIRA_ESCURA));

        JMenu menuJogo = new JMenu("Jogo");
        menuJogo.setFont(TemaFantasia.FONTE_TEXTO_BOLD);
        menuJogo.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);

        JMenuItem itemNovo = new JMenuItem("Novo Jogo");
        itemNovo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        itemNovo.addActionListener(e -> solicitarNovoJogo());

        menuItemSalvar = new JMenuItem("Salvar Jogo");
        menuItemSalvar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        menuItemSalvar.setEnabled(false);
        menuItemSalvar.addActionListener(e -> salvarProgresso());

        JMenuItem itemCarregar = new JMenuItem("Carregar Jogo");
        itemCarregar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        itemCarregar.addActionListener(e -> carregarJogoSalvo());

        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> {
            int opcao = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente sair de Torre dos Elementos?",
                    "Confirmar Saída",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (opcao == JOptionPane.YES_OPTION) {
                dispose();
                System.exit(0);
            }
        });

        menuJogo.add(itemNovo);
        menuJogo.add(menuItemSalvar);
        menuJogo.add(itemCarregar);
        menuJogo.addSeparator();
        menuJogo.add(itemSair);

        JMenu menuAjuda = new JMenu("Ajuda");
        menuAjuda.setFont(TemaFantasia.FONTE_TEXTO_BOLD);
        menuAjuda.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);

        JMenuItem itemGuia = new JMenuItem("Guia da Torre e Elementos");
        itemGuia.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        itemGuia.addActionListener(e -> exibirGuiaDoJogo());

        JMenuItem itemSobre = new JMenuItem("Sobre");
        itemSobre.addActionListener(e -> exibirSobre());

        menuAjuda.add(itemGuia);
        menuAjuda.add(itemSobre);

        barraMenu.add(menuJogo);
        barraMenu.add(menuAjuda);
        return barraMenu;
    }

    private void exibirGuiaDoJogo() {
        JDialog dialogo = new JDialog(this, "Guia da Torre e Elementos", true);
        dialogo.setSize(660, 560);
        dialogo.setLocationRelativeTo(this);
        dialogo.setResizable(false);
        dialogo.getContentPane().setBackground(TemaFantasia.PERGAMINHO_CLARO);
        dialogo.setLayout(new BorderLayout(10, 10));

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        painelTopo.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelTopo.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, TemaFantasia.MADEIRA_ESCURA));

        JLabel lblTitulo = new JLabel("Guia da Torre e Elementos", JLabel.CENTER);
        lblTitulo.setFont(TemaFantasia.FONTE_TITULO);
        lblTitulo.setForeground(TemaFantasia.MADEIRA_ESCURA);
        painelTopo.add(lblTitulo);
        dialogo.add(painelTopo, BorderLayout.NORTH);

        JPanel painelConteudo = new PainelGuiaRolavel();
        painelConteudo.setLayout(new BoxLayout(painelConteudo, BoxLayout.Y_AXIS));
        painelConteudo.setBackground(TemaFantasia.PERGAMINHO_CLARO);
        painelConteudo.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        String textoElementos = "Ciclo principal: Pyro -> Anemo -> Electro -> Hydro -> Pyro\n\n"
                + "• Pyro: x1.5 contra Anemo (Propagação) | x1.4 contra Electro (Sobrecarga) | x0.8 contra Hydro.\n"
                + "• Anemo: x1.35 contra qualquer outro elemento (Dispersão Elemental).\n"
                + "• Electro: x1.6 contra Hydro (Eletrocussão) | x1.3 contra Pyro (Sobrecarga).\n"
                + "• Hydro: x1.6 contra Pyro (Vaporização) | x1.3 contra Electro (Condução Elétrica).\n"
                + "• Mesmo elemento: x0.65 (Resistência Elemental: -35% de dano).";

        String textoClasses = "• Espadachim (HP 115 | Mana 40 | ATQ 22 | DEF 10)\n"
                + "  Habilidade: Lâmina Elemental (15 Mana) | Passiva: 16% de chance de esquiva.\n\n"
                + "• Arcanista (HP 90 | Mana 75 | ATQ 25 | DEF 5)\n"
                + "  Habilidade: Explosão Arcana (20 Mana) | Passiva: +15% de dano em reações da habilidade.\n\n"
                + "• Atirador (HP 95 | Mana 55 | ATQ 24 | DEF 7)\n"
                + "  Habilidade: Disparo Perfurante (14 Mana) | Passiva: 28% de chance de acerto crítico (150%).\n\n"
                + "• Catalisador (HP 85 | Mana 80 | ATQ 23 | DEF 5)\n"
                + "  Habilidade: Ressonância Suprema (22 Mana) | Passiva: ataques normais aplicam reação elemental.";

        String textoTorre = "• Andar 1: Slime Gigante Pyro (PYRO) — pode causar queimadura por 2 turnos.\n"
                + "• Andar 2: Espectro das Marés (HYDRO) — pode regenerar HP com o Manto das Marés.\n"
                + "• Andar 3: Guardião dos Ventos (ANEMO) — 18% de esquiva e golpe Ciclone Cortante.\n"
                + "• Andar 4: Arconte da Tempestade (ELECTRO) — chefe final que drena 10 de mana no Raio Cataclísmico.\n\n"
                + "Eventos entre os andares:\n"
                + "Ao vencer um guardião, seu herói sobe de nível e pode encontrar um Baú de Tesouro (novas poções e elixires), "
                + "uma Fonte Mística (+50 HP e +35 Mana), o Altar dos Titãs (+5 ATQ e +2 DEF permanentes) ou cair na emboscada "
                + "de um Baú Mímico (combate surpresa contra o Mímico Voraz, que recompensa com o Elixir dos Arcontes).";

        painelConteudo.add(criarSecaoGuia("1. Ciclo de Vantagens Elementais", textoElementos));
        painelConteudo.add(Box.createVerticalStrut(10));
        painelConteudo.add(criarSecaoGuia("2. As 4 Classes de Heróis", textoClasses));
        painelConteudo.add(Box.createVerticalStrut(10));
        painelConteudo.add(criarSecaoGuia("3. Os 4 Andares, Baús e Mímicos", textoTorre));

        JScrollPane scroll = new JScrollPane(painelConteudo);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        dialogo.add(scroll, BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> scroll.getViewport().setViewPosition(new Point(0, 0)));

        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelRodape.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelRodape.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, TemaFantasia.MADEIRA_ESCURA));

        JButton btnFechar = TemaFantasia.criarBotaoMadeira("Fechar", TemaFantasia.MADEIRA_MEDIA);
        btnFechar.addActionListener(e -> dialogo.dispose());
        painelRodape.add(btnFechar);
        dialogo.add(painelRodape, BorderLayout.SOUTH);

        dialogo.setVisible(true);
    }

    private JPanel criarSecaoGuia(String titulo, String conteudo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(TemaFantasia.PERGAMINHO_DESTACADO);
        card.setBorder(TemaFantasia.criarBordaMadeiraPergaminho(titulo));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea area = new JTextArea(conteudo);
        area.setColumns(46);
        area.setEditable(false);
        area.setFocusable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        area.setFont(TemaFantasia.FONTE_TEXTO);
        area.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);
        card.add(area, BorderLayout.CENTER);
        return card;
    }

    private static class PainelGuiaRolavel extends JPanel implements Scrollable {
        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 64;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    private void exibirSobre() {
        String mensagem = "Torre dos Elementos - Projeto Final de POO - UFU\n\n"
                + "Disciplina: Programação Orientada a Objetos (Sistemas de Informação)\n"
                + "Grupo: Celina, Isabela Dantas e Isabela Rocha\n\n"
                + "Implementado em Java com interface gráfica em Java Swing (javax.swing).";
        JOptionPane.showMessageDialog(this, mensagem, "Sobre", JOptionPane.INFORMATION_MESSAGE);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaJogo tela = new TelaJogo();
            tela.setVisible(true);
        });
    }
}
