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
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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
import javax.swing.SwingUtilities;
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

    // Barra de Menu
    private JMenuItem itemMenuNovo;
    private JMenuItem itemMenuSalvar;
    private JMenuItem itemMenuCarregar;
    private JMenuItem itemMenuSair;
    private JMenuItem itemMenuGuia;
    private JMenuItem itemMenuSobre;

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
    private JLabel lblFeedbackCombate;
    private JProgressBar barraHpMonstro;
    private javax.swing.Timer timerFeedback;

    // Log e Ações
    private JTextArea areaLog;
    private JButton btnAtacar;
    private JButton btnHabilidade;
    private JButton btnMochila;
    private JButton btnSalvar;
    private JButton btnCarregar;
    private JButton btnReiniciar;

    public TelaJogo() {
        super("Torre dos Elementos — Diário de Aventuras RPG");
        this.persistencia = new GerenciadorPersistencia();

        configurarJanela();
        configurarMenuBar();
        inicializarComponentes();
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

    private void configurarMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(TemaFantasia.MADEIRA_ESCURA);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, TemaFantasia.OURO_BORDA));

        // Menu Jogo
        JMenu menuJogo = new JMenu("Jogo");
        menuJogo.setForeground(TemaFantasia.TEXTO_CLARO);
        menuJogo.setFont(TemaFantasia.FONTE_TEXTO_BOLD);

        int shortcutMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

        itemMenuNovo = new JMenuItem("Novo Jogo");
        itemMenuNovo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, shortcutMask));
        itemMenuNovo.addActionListener(e -> solicitarNovoJogo());

        itemMenuSalvar = new JMenuItem("Salvar Jogo");
        itemMenuSalvar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcutMask));
        itemMenuSalvar.setEnabled(false);
        itemMenuSalvar.addActionListener(e -> salvarProgresso());

        itemMenuCarregar = new JMenuItem("Carregar Jogo");
        itemMenuCarregar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, shortcutMask));
        itemMenuCarregar.addActionListener(e -> carregarJogoSalvo());

        itemMenuSair = new JMenuItem("Sair");
        itemMenuSair.addActionListener(e -> {
            int resp = JOptionPane.showConfirmDialog(this, "Deseja realmente encerrar a Torre dos Elementos?", "Sair do Jogo", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        menuJogo.add(itemMenuNovo);
        menuJogo.add(itemMenuSalvar);
        menuJogo.add(itemMenuCarregar);
        menuJogo.addSeparator();
        menuJogo.add(itemMenuSair);

        // Menu Ajuda
        JMenu menuAjuda = new JMenu("Ajuda");
        menuAjuda.setForeground(TemaFantasia.TEXTO_CLARO);
        menuAjuda.setFont(TemaFantasia.FONTE_TEXTO_BOLD);

        itemMenuGuia = new JMenuItem("Guia da Torre e Elementos");
        itemMenuGuia.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        itemMenuGuia.addActionListener(e -> new DialogGuiaTorre(this).setVisible(true));

        itemMenuSobre = new JMenuItem("Sobre");
        itemMenuSobre.addActionListener(e -> new DialogSobre(this).setVisible(true));

        menuAjuda.add(itemMenuGuia);
        menuAjuda.add(itemMenuSobre);

        menuBar.add(menuJogo);
        menuBar.add(menuAjuda);

        setJMenuBar(menuBar);
    }

    private void inicializarComponentes() {
        // ================= BARRA SUPERIOR: ABAS DE MARCADOR DE LIVRO =================
        JPanel painelAbasSuperiores = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        painelAbasSuperiores.setBackground(TemaFantasia.MADEIRA_ESCURA);
        painelAbasSuperiores.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, TemaFantasia.OURO_BORDA));

        tabSelecao = TemaFantasia.criarBotaoMadeira("📜 Ficha do Herói / Criação", TemaFantasia.MADEIRA_MEDIA);
        tabBatalha = TemaFantasia.criarBotaoMadeira("🏰 Torre dos Elementos (Batalha)", TemaFantasia.COR_PYRO);

        tabSelecao.setToolTipText("Configurar herói e iniciar nova jornada.");
        tabBatalha.setToolTipText("Arena de combate e exploração da torre.");

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

        campoNome = new JTextField("Lyra", 14);
        campoNome.setFont(TemaFantasia.FONTE_TEXTO_BOLD);
        campoNome.setBackground(TemaFantasia.PERGAMINHO_DESTACADO);
        campoNome.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);
        campoNome.setToolTipText("Digite um nome válido para o herói (não deixe em branco e evite o caractere ';').");
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
            JButton btnElem = TemaFantasia.criarBotaoMadeira("• " + elem.name(), TemaFantasia.obterCorElemento(elem));
            btnElem.setPreferredSize(new Dimension(140, 36));
            btnElem.setToolTipText("Afinidade Elemental: " + elem.name());
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

        JButton btnCarregarCheckpoint = TemaFantasia.criarBotaoMadeira("📂 Carregar Checkpoint Salvo", TemaFantasia.COR_HYDRO);
        btnCarregarCheckpoint.setPreferredSize(new Dimension(220, 40));
        btnCarregarCheckpoint.setToolTipText("Restaura o último checkpoint gravado no arquivo save_jogo.txt.");

        JButton btnIniciar = TemaFantasia.criarBotaoMadeira("⚔ Iniciar Aventura na Torre", new Color(180, 83, 9));
        btnIniciar.setPreferredSize(new Dimension(230, 40));
        btnIniciar.setFont(new Font("Serif", Font.BOLD, 14));
        btnIniciar.setToolTipText("Começar a expedição com o herói e elemento configurados.");

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

        String icone = "⚔";
        String arquetipo = "Guerreiro";
        String statusResumo = "HP: " + Espadachim.HP_BASE + " | Mana: " + Espadachim.MANA_BASE;
        String tooltipDica = "Espadachim: Equilíbrio e 16% de esquiva/bloqueio.";

        switch (nomeClasse) {
            case "Arcanista":
                icone = "🔮";
                arquetipo = "Mago Elemental";
                statusResumo = "HP: " + Arcanista.HP_BASE + " | Mana: " + Arcanista.MANA_BASE;
                tooltipDica = "Arcanista: +15% de dano em reações elementais da habilidade.";
                break;
            case "Atirador":
                icone = "🏹";
                arquetipo = "Caçador Crítico";
                statusResumo = "HP: " + Atirador.HP_BASE + " | Mana: " + Atirador.MANA_BASE;
                tooltipDica = "Atirador: 28% de chance de acerto crítico.";
                break;
            case "Catalisador":
                icone = "📖";
                arquetipo = "Mago Contínuo";
                statusResumo = "HP: " + Catalisador.HP_BASE + " | Mana: " + Catalisador.MANA_BASE;
                tooltipDica = "Catalisador: Ataques normais aplicam reação elemental e crítico.";
                break;
        }

        card.setToolTipText(tooltipDica);

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
        JPanel painel = new JPanel(new GridLayout(2, 2, 8, 4));
        painel.setBackground(TemaFantasia.PERGAMINHO_DESTACADO);
        painel.setBorder(TemaFantasia.criarBordaMadeiraPergaminho("Ficha de Prévia do Personagem"));

        lblPreviaNomeClasse = new JLabel("-");
        lblPreviaNomeClasse.setFont(new Font("Serif", Font.BOLD, 14));
        lblPreviaNomeClasse.setForeground(TemaFantasia.MADEIRA_ESCURA);

        lblPreviaAtributos = new JLabel("-");
        lblPreviaAtributos.setFont(TemaFantasia.FONTE_TEXTO_BOLD);
        lblPreviaAtributos.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);

        lblPreviaHabilidade = new JLabel("-");
        lblPreviaHabilidade.setFont(TemaFantasia.FONTE_TEXTO);
        lblPreviaHabilidade.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);

        lblPreviaPassiva = new JLabel("-");
        lblPreviaPassiva.setFont(TemaFantasia.FONTE_PEQUENA);
        lblPreviaPassiva.setForeground(TemaFantasia.TEXTO_SEPIA_MEDIO);

        painel.add(lblPreviaNomeClasse);
        painel.add(lblPreviaAtributos);
        painel.add(lblPreviaHabilidade);
        painel.add(lblPreviaPassiva);

        return painel;
    }

    private void atualizarPreviaSelecao() {
        Heroi h = instanciarHeroi(classeSelecionada, "Prévia", elementoSelecionado);
        Color corElem = TemaFantasia.obterCorElemento(elementoSelecionado);

        lblPreviaNomeClasse.setText("<html>Classe: <b>" + classeSelecionada + "</b> | Elemento: <font color='"
                + String.format("#%06x", (corElem.getRGB() & 0xFFFFFF)) + "'><b>" + elementoSelecionado.name() + "</b></font></html>");

        lblPreviaAtributos.setText("HP Base: " + h.getHpMax() + " | Mana: " + h.getManaMax()
                + " | ATQ: " + h.getAtaque() + " | DEF: " + h.getDefesa());

        lblPreviaHabilidade.setText("<html><b>Habilidade:</b> " + h.getNomeHabilidade()
                + " (Custo: " + h.getCustoManaHabilidade() + " Mana)<br><small>" + h.getDescricaoHabilidade() + "</small></html>");

        lblPreviaPassiva.setText("<html><b>Passiva:</b> " + h.getDescricaoPassiva() + "</html>");
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

        lblAndarBanner = new JLabel("Torre dos Elementos — Andar 1 / 4 — Guardião Elemental", JLabel.CENTER);
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

        barraHpHeroi = TemaFantasia.criarBarraProgresso("HP: 115 / 115", new Color(46, 125, 50), TemaFantasia.MADEIRA_ESCURA);
        barraManaHeroi = TemaFantasia.criarBarraProgresso("Mana: 40 / 40", new Color(25, 118, 210), TemaFantasia.MADEIRA_ESCURA);

        lblHeroiStatus = new JLabel("ATQ: 22 | DEF: 10 | Mochila: 2 itens");
        lblHeroiStatus.setFont(TemaFantasia.FONTE_TEXTO);
        lblHeroiStatus.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);

        cardBatalhaHeroi.add(lblHeroiTitulo);
        cardBatalhaHeroi.add(Box.createVerticalStrut(4));
        cardBatalhaHeroi.add(barraHpHeroi);
        cardBatalhaHeroi.add(Box.createVerticalStrut(4));
        cardBatalhaHeroi.add(barraManaHeroi);
        cardBatalhaHeroi.add(Box.createVerticalStrut(4));
        cardBatalhaHeroi.add(lblHeroiStatus);

        // Card do Monstro
        cardBatalhaMonstro = new JPanel();
        cardBatalhaMonstro.setLayout(new BoxLayout(cardBatalhaMonstro, BoxLayout.Y_AXIS));
        cardBatalhaMonstro.setBackground(TemaFantasia.PERGAMINHO_DESTACADO);
        cardBatalhaMonstro.setBorder(TemaFantasia.criarBordaMadeiraPergaminho("Guardião do Andar"));

        lblMonstroTitulo = new JLabel("Slime Gigante Pyro [PYRO]");
        lblMonstroTitulo.setFont(TemaFantasia.FONTE_SUBTITULO);
        lblMonstroTitulo.setForeground(TemaFantasia.COR_PYRO);

        barraHpMonstro = TemaFantasia.criarBarraProgresso("HP: 85 / 85", TemaFantasia.COR_PYRO, TemaFantasia.MADEIRA_ESCURA);

        lblFeedbackCombate = new JLabel(" ", JLabel.CENTER);
        lblFeedbackCombate.setFont(new Font("Serif", Font.BOLD, 13));
        lblFeedbackCombate.setForeground(TemaFantasia.COR_PYRO);
        lblFeedbackCombate.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblMonstroStatus = new JLabel("ATQ: 19 | DEF: 6 | Andar 1");
        lblMonstroStatus.setFont(TemaFantasia.FONTE_TEXTO);
        lblMonstroStatus.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);

        cardBatalhaMonstro.add(lblMonstroTitulo);
        cardBatalhaMonstro.add(Box.createVerticalStrut(4));
        cardBatalhaMonstro.add(barraHpMonstro);
        cardBatalhaMonstro.add(Box.createVerticalStrut(4));
        cardBatalhaMonstro.add(lblFeedbackCombate);
        cardBatalhaMonstro.add(Box.createVerticalStrut(6));
        cardBatalhaMonstro.add(lblMonstroStatus);
        cardBatalhaMonstro.add(Box.createVerticalStrut(10));

        painelCombatentes.add(cardBatalhaHeroi);
        painelCombatentes.add(cardBatalhaMonstro);

        // Área de Log Pergaminho com Cabeçalho e Botão Limpar Log
        JPanel painelLogContainer = new JPanel(new BorderLayout(4, 4));
        painelLogContainer.setOpaque(false);
        painelLogContainer.setBorder(TemaFantasia.criarBordaMadeiraPergaminho("Diário de Combate da Torre"));

        JPanel painelLogHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 2));
        painelLogHeader.setOpaque(false);

        JButton btnLimparLog = TemaFantasia.criarBotaoMadeira("Limpar Log", TemaFantasia.MADEIRA_MEDIA);
        btnLimparLog.setFont(TemaFantasia.FONTE_PEQUENA);
        btnLimparLog.setPreferredSize(new Dimension(100, 26));
        btnLimparLog.setToolTipText("Limpar histórico de texto do diário de combate.");
        btnLimparLog.addActionListener(e -> areaLog.setText(""));
        painelLogHeader.add(btnLimparLog);

        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.setBackground(TemaFantasia.PERGAMINHO_LOG);
        areaLog.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaLog.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createLineBorder(TemaFantasia.OURO_BORDA, 1));

        painelLogContainer.add(painelLogHeader, BorderLayout.NORTH);
        painelLogContainer.add(scrollLog, BorderLayout.CENTER);

        JPanel painelMeio = new JPanel(new BorderLayout(0, 8));
        painelMeio.setOpaque(false);
        painelMeio.add(painelCombatentes, BorderLayout.NORTH);
        painelMeio.add(painelLogContainer, BorderLayout.CENTER);

        painelCentro.add(painelMeio, BorderLayout.CENTER);
        painel.add(painelCentro, BorderLayout.CENTER);

        // Inferior: Botões de Ação de Combate
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        painelAcoes.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelAcoes.setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, TemaFantasia.MADEIRA_ESCURA));

        btnAtacar = TemaFantasia.criarBotaoMadeira("⚔ Atacar (A)", new Color(180, 40, 30));
        btnHabilidade = TemaFantasia.criarBotaoMadeira("✨ Habilidade (H)", new Color(25, 118, 210));
        btnMochila = TemaFantasia.criarBotaoMadeira("🎒 Mochila (M)", new Color(46, 125, 50));
        btnSalvar = TemaFantasia.criarBotaoMadeira("💾 Salvar", new Color(180, 83, 9));
        btnCarregar = TemaFantasia.criarBotaoMadeira("📂 Carregar", TemaFantasia.MADEIRA_MEDIA);
        btnReiniciar = TemaFantasia.criarBotaoMadeira("📜 Novo Herói", new Color(110, 71, 42));

        btnAtacar.setMnemonic(KeyEvent.VK_A);
        btnHabilidade.setMnemonic(KeyEvent.VK_H);
        btnMochila.setMnemonic(KeyEvent.VK_M);

        configurarAtalhosCombate(painel);

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
    private void solicitarNovoJogo() {
        if (jogo != null && !jogo.isJogoFinalizado()) {
            int resp = JOptionPane.showConfirmDialog(
                    this,
                    "Há uma expedição ativa no Andar " + jogo.getNumeroAndarAtual() + ".\nDeseja realmente retornar à criação e descartar o progresso não salvo?",
                    "Confirmar Novo Herói",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (resp != JOptionPane.YES_OPTION) {
                return;
            }
        }
        exibirTela(TELA_SELECAO);
    }

    private void iniciarNovaPartida() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, digite um nome válido para o herói.", "Nome Inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (nome.contains(";")) {
            JOptionPane.showMessageDialog(
                    this,
                    "O nome do herói não pode conter o caractere ';' (ponto e vírgula),\npois ele é utilizado na estrutura do arquivo de salvamento.",
                    "Caractere Inválido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (jogo != null && !jogo.isJogoFinalizado()) {
            int resp = JOptionPane.showConfirmDialog(
                    this,
                    "Já existe uma expedição ativa no Andar " + jogo.getNumeroAndarAtual() + ".\nDeseja iniciar uma nova jornada e descartar o progresso não salvo?",
                    "Confirmar Novo Jogo",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (resp != JOptionPane.YES_OPTION) {
                return;
            }
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
                return new Arcanista(nome, elem);
            case "Atirador":
                return new Atirador(nome, elem);
            case "Catalisador":
                return new Catalisador(nome, elem);
            case "Espadachim":
            default:
                return new Espadachim(nome, elem);
        }
    }

    private void configurarAtalhosCombate(JPanel painel) {
        painel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "acaoAtacar");
        painel.getActionMap().put("acaoAtacar", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (btnAtacar.isEnabled()) {
                    executarAtaque();
                }
            }
        });

        painel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0), "acaoHabilidade");
        painel.getActionMap().put("acaoHabilidade", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (btnHabilidade.isEnabled()) {
                    executarHabilidade();
                }
            }
        });

        painel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "acaoMochila");
        painel.getActionMap().put("acaoMochila", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (btnMochila.isEnabled()) {
                    abrirMochila();
                }
            }
        });
    }

    private void animarFeedbackCombate(String log) {
        if (lblFeedbackCombate == null || log == null) {
            return;
        }

        String textoAnimacao = "";
        Color cor = TemaFantasia.COR_PYRO;

        if (log.contains("CRÍTICO")) {
            textoAnimacao = "💥 ACERTO CRÍTICO! (150% DANO)";
            cor = new Color(220, 38, 38);
        } else if (log.contains("VAPORIZAÇÃO")) {
            textoAnimacao = "💧 VAPORIZAÇÃO! (x1.6 DANO)";
            cor = TemaFantasia.COR_HYDRO;
        } else if (log.contains("PROPAGAÇÃO")) {
            textoAnimacao = "🔥 DISPERSÃO ELEMENTAL! (x1.5 DANO)";
            cor = TemaFantasia.COR_PYRO;
        } else if (log.contains("SOBRECARGA")) {
            textoAnimacao = "⚡ SOBRECARGA ELÉTRICA! (x1.4 DANO)";
            cor = TemaFantasia.COR_ELECTRO;
        } else if (log.contains("ELETROCUSSÃO")) {
            textoAnimacao = "⚡ ELETROCUSSÃO INTENSA! (x1.6 DANO)";
            cor = TemaFantasia.COR_ELECTRO;
        } else if (log.contains("ESQUIVOU")) {
            textoAnimacao = "🛡️ ESQUIVA / BLOQUEIO!";
            cor = TemaFantasia.COR_ANEMO;
        } else if (log.contains("RESISTÊNCIA")) {
            textoAnimacao = "🛡 RESISTÊNCIA ELEMENTAL (-35%)";
            cor = TemaFantasia.TEXTO_SEPIA_MEDIO;
        } else if (log.contains("derrotou")) {
            textoAnimacao = "✨ INIMIGO DERROTADO!";
            cor = TemaFantasia.OURO_VELHO;
        }

        if (!textoAnimacao.isEmpty()) {
            lblFeedbackCombate.setText(textoAnimacao);
            lblFeedbackCombate.setForeground(cor);

            if (timerFeedback != null && timerFeedback.isRunning()) {
                timerFeedback.stop();
            }
            timerFeedback = new javax.swing.Timer(2500, evt -> {
                lblFeedbackCombate.setText(" ");
            });
            timerFeedback.setRepeats(false);
            timerFeedback.start();
        }
    }

    private void executarAtaque() {
        if (jogo == null || jogo.isJogoFinalizado()) {
            return;
        }

        String log = jogo.atacarNoTurno();
        adicionarLog(log);
        animarFeedbackCombate(log);
        verificarRecompensaEAtualizar();
    }

    private void executarHabilidade() {
        if (jogo == null || jogo.isJogoFinalizado()) {
            return;
        }

        try {
            String log = jogo.usarHabilidadeNoTurno();
            adicionarLog(log);
            animarFeedbackCombate(log);
            verificarRecompensaEAtualizar();
        } catch (ManaInsuficienteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Energia Insuficiente", JOptionPane.WARNING_MESSAGE);
            atualizarInterfaceBatalha();
        }
    }

    private void abrirMochila() {
        if (jogo == null) {
            JOptionPane.showMessageDialog(this, "Inicie uma jornada para acessar a mochila.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        DialogInventario dlg = new DialogInventario(this, jogo, () -> {
            atualizarInterfaceBatalha();
            verificarRecompensaEAtualizar();
        });
        dlg.setVisible(true);
    }

    private void verificarRecompensaEAtualizar() {
        atualizarInterfaceBatalha();

        // Se após a ação houve vitória de andar com recompensa, abre o modal estilizado!
        if (jogo != null && jogo.isHouveEventoRecompensa()) {
            DialogRecompensaBau dlgRecompensa = new DialogRecompensaBau(
                    this,
                    jogo.getHeroi().getNivel(),
                    jogo.getUltimoEventoNome(),
                    jogo.getUltimoEventoDescricao(),
                    jogo.getUltimoItemRecebido()
            );
            dlgRecompensa.setVisible(true);
            jogo.limparEventoRecompensa();
            atualizarInterfaceBatalha();
        }

        // Se o jogo encerrou (derrota ou vitória final), abre o modal de fim de jogo com opções
        if (jogo != null && jogo.isJogoFinalizado()) {
            DialogFimDeJogo dlgFim = new DialogFimDeJogo(
                    this,
                    jogo.isVitoriaFinal(),
                    jogo.getHeroi(),
                    jogo.getNumeroAndarAtual(),
                    persistencia.existeSave(),
                    () -> carregarJogoSalvo(),
                    () -> exibirTela(TELA_SELECAO)
            );
            dlgFim.setVisible(true);
        }
    }

    private void salvarProgresso() {
        if (jogo == null) {
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

        barraManaHeroi.setMaximum(heroi.getManaMax());
        barraManaHeroi.setValue(heroi.getManaAtual());
        barraManaHeroi.setString("Mana: " + heroi.getManaAtual() + " / " + heroi.getManaMax());

        lblHeroiStatus.setText("ATQ: " + heroi.getAtaque() + " | DEF: " + heroi.getDefesa()
                + " | Mochila: " + heroi.getInventario().getItens().size() + " itens | "
                + (heroi.estaQueimando() ? "Queimando (" + heroi.getTurnosQueimando() + (heroi.getTurnosQueimando() == 1 ? " turno)" : " turnos)") : "Normal"));

        // 2. Atualização do Monstro
        Color corElementoMonstro = TemaFantasia.obterCorElemento(monstro.getElemento());
        String tagMimico = monstro.isMimico() ? " <font color='#C0392B'><b>[MÍMICO VORAZ!]</b></font>" : "";

        lblMonstroTitulo.setText("<html><b>" + monstro.getNome() + "</b> [" + monstro.getElemento() + "]" + tagMimico + "</html>");
        lblMonstroTitulo.setForeground(corElementoMonstro);

        barraHpMonstro.setMaximum(monstro.getHpMax());
        barraHpMonstro.setValue(monstro.getHpAtual());
        barraHpMonstro.setString("HP: " + monstro.getHpAtual() + " / " + monstro.getHpMax());
        barraHpMonstro.setForeground(corElementoMonstro);

        lblMonstroStatus.setText("ATQ: " + monstro.getAtaque() + " | DEF: " + monstro.getDefesa()
                + " | Andar " + monstro.getAndar());

        // Moldura do Monstro adota a cor do elemento dominante
        cardBatalhaMonstro.setBorder(TemaFantasia.criarBordaMetalica(corElementoMonstro));

        // 3. Banner do Andar Atual
        String extra = monstro.isMimico() ? " — ⚠️ EMBOSCADA DE MÍMICO!" : " — Guardião Elemental";
        lblAndarBanner.setText("Torre dos Elementos — Andar " + jogo.getNumeroAndarAtual() + " / 4" + extra);
        lblAndarBanner.setForeground(corElementoMonstro);

        // 4. Estado dos Botões de Ação e Rótulos Dinâmicos
        boolean partidaAtiva = !jogo.isJogoFinalizado();
        btnAtacar.setEnabled(partidaAtiva);
        btnHabilidade.setEnabled(partidaAtiva);
        btnMochila.setEnabled(partidaAtiva);
        btnSalvar.setEnabled(partidaAtiva);
        btnCarregar.setEnabled(true);
        btnReiniciar.setEnabled(true);

        btnHabilidade.setText("✨ Habilidade (" + heroi.getCustoManaHabilidade() + " MP) [H]");
        btnMochila.setText("🎒 Mochila (" + heroi.getInventario().getItens().size() + ") [M]");

        // Atualização do Item de Menu Salvar
        itemMenuSalvar.setEnabled(partidaAtiva);

        // Tooltips de Ação
        btnAtacar.setToolTipText("Desfere um ataque físico básico com chance de acerto crítico (150% de dano).");
        btnHabilidade.setToolTipText("Conjura a habilidade elemental da classe (" + heroi.getCustoManaHabilidade() + " MP) aplicando reações elementais.");
        btnMochila.setToolTipText("Abre a mochila para usar itens de cura ou recuperação de mana.");
        btnSalvar.setToolTipText("Salva o checkpoint do início do Andar " + jogo.getNumeroAndarAtual() + " (Atalho: Ctrl+S).");
        btnCarregar.setToolTipText("Restaura o último checkpoint gravado (Atalho: Ctrl+O).");
        btnReiniciar.setToolTipText("Retorna à criação de personagem para iniciar com outro herói.");
        barraHpHeroi.setToolTipText("Pontos de Vida (HP) do Viajante: " + heroi.getHpAtual() + " / " + heroi.getHpMax());
        barraManaHeroi.setToolTipText("Energia Elemental (Mana) do Viajante: " + heroi.getManaAtual() + " / " + heroi.getManaMax());
        barraHpMonstro.setToolTipText("Pontos de Vida (HP) do Guardião: " + monstro.getHpAtual() + " / " + monstro.getHpMax());
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
