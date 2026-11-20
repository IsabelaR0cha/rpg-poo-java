package telas;

import combate.Jogo;
import excecoes.ItemNaoEncontradoException;
import excecoes.ManaInsuficienteException;
import excecoes.PersistenciaException;
import itens.Item;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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

    private JComboBox<String> comboClasse;
    private JComboBox<Elemento> comboElemento;
    private JTextField campoNome;

    private JLabel lblStatusHeroi;
    private JLabel lblStatusMonstro;
    private JLabel lblAndar;
    private JTextArea areaLog;

    private JButton btnIniciar;
    private JButton btnAtacar;
    private JButton btnHabilidade;
    private JButton btnUsarItem;
    private JButton btnSalvar;
    private JButton btnCarregar;

    public TelaJogo() {
        super("Torre dos Elementos - RPG POO");
        this.persistencia = new GerenciadorPersistencia();

        configurarJanela();
        inicializarComponentes();
        atualizarEstadoBotoes(false);
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(760, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
    }

    private void inicializarComponentes() {
        // Painel superior: criação de personagem + salvar/carregar
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        painelTopo.setBorder(BorderFactory.createTitledBorder("Criação & Progresso"));

        campoNome = new JTextField("Viajante", 10);
        comboClasse = new JComboBox<>(new String[]{"Espadachim", "Arcanista", "Atirador", "Catalisador"});
        comboElemento = new JComboBox<>(Elemento.values());

        btnIniciar = new JButton("Novo Jogo");
        btnSalvar = new JButton("Salvar");
        btnCarregar = new JButton("Carregar");

        painelTopo.add(new JLabel("Nome:"));
        painelTopo.add(campoNome);
        painelTopo.add(new JLabel("Classe:"));
        painelTopo.add(comboClasse);
        painelTopo.add(new JLabel("Elemento:"));
        painelTopo.add(comboElemento);
        painelTopo.add(btnIniciar);
        painelTopo.add(btnSalvar);
        painelTopo.add(btnCarregar);

        add(painelTopo, BorderLayout.NORTH);

        // Painel central: status do herói, andar/monstro e área de log
        JPanel painelCentro = new JPanel(new BorderLayout(8, 8));
        painelCentro.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        JPanel painelStatus = new JPanel(new GridLayout(1, 3, 8, 8));
        lblStatusHeroi = new JLabel("Herói: -", JLabel.CENTER);
        lblAndar = new JLabel("Andar: -", JLabel.CENTER);
        lblStatusMonstro = new JLabel("Monstro: -", JLabel.CENTER);

        painelStatus.add(lblStatusHeroi);
        painelStatus.add(lblAndar);
        painelStatus.add(lblStatusMonstro);

        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Registro da Batalha"));

        painelCentro.add(painelStatus, BorderLayout.NORTH);
        painelCentro.add(scrollLog, BorderLayout.CENTER);

        add(painelCentro, BorderLayout.CENTER);

        // Painel inferior: ações de combate
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        painelAcoes.setBorder(BorderFactory.createTitledBorder("Ações do Turno"));

        btnAtacar = new JButton("Atacar");
        btnHabilidade = new JButton("Habilidade");
        btnUsarItem = new JButton("Usar Item");

        painelAcoes.add(btnAtacar);
        painelAcoes.add(btnHabilidade);
        painelAcoes.add(btnUsarItem);

        add(painelAcoes, BorderLayout.SOUTH);

        registrarEventos();
    }

    private void registrarEventos() {
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
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Sem Mana", JOptionPane.WARNING_MESSAGE);
            }
            atualizarInterface();
        });

        btnUsarItem.addActionListener(e -> {
            if (jogo == null || jogo.isJogoFinalizado()) {
                return;
            }
            String nomeItem = JOptionPane.showInputDialog(
                    this,
                    "Digite o nome do item (ex: Poção de Cura, Éter de Mana):",
                    "Usar Item",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (nomeItem != null && !nomeItem.trim().isEmpty()) {
                try {
                    adicionarLog(jogo.usarItemNoTurno(nomeItem.trim()));
                } catch (ItemNaoEncontradoException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Item Indisponível", JOptionPane.WARNING_MESSAGE);
                }
                atualizarInterface();
            }
        });

        btnSalvar.addActionListener(e -> {
            if (jogo == null) {
                return;
            }
            try {
                persistencia.salvarJogo(jogo);
                JOptionPane.showMessageDialog(this, "Progresso salvo com sucesso!", "Salvar Jogo", JOptionPane.INFORMATION_MESSAGE);
            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao Salvar", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCarregar.addActionListener(e -> {
            try {
                this.jogo = persistencia.carregarJogo();
                areaLog.setText("");
                adicionarLog("Jogo carregado! Continuando no Andar " + jogo.getNumeroAndarAtual() + ".");
                atualizarEstadoBotoes(true);
                atualizarInterface();
            } catch (PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao Carregar", JOptionPane.ERROR_MESSAGE);
            }
        });
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
        adicionarLog("Aventura iniciada! " + heroi.getNome() + " (" + classeEscolhida + " - "
                + elementoEscolhido + ") entrou no Andar " + jogo.getNumeroAndarAtual() + ".");

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

        StringBuilder itensStr = new StringBuilder();
        for (Item item : heroi.getInventario().getItens()) {
            if (itensStr.length() > 0) {
                itensStr.append(", ");
            }
            itensStr.append(item.getNome());
        }
        if (itensStr.length() == 0) {
            itensStr.append("vazio");
        }

        String statusQueimando = heroi.estaQueimando() ? " <font color='red'><b>(🔥 Em Chamas!)</b></font>" : "";

        lblStatusHeroi.setText("<html><b>" + heroi.getNome() + "</b> (Nv." + heroi.getNivel() + " | "
                + heroi.getElemento() + ")" + statusQueimando + "<br>HP: " + heroi.getHpAtual() + "/" + heroi.getHpMax()
                + " | Mana: " + heroi.getManaAtual() + "/" + heroi.getManaMax()
                + "<br><small>Itens: " + itensStr + "</small></html>");

        String detalheAndar = monstro.isMimico() ? " <font color='red'><b>(⚠️ MÍMICO!)</b></font>" : "";
        lblAndar.setText("<html><b>Andar " + jogo.getNumeroAndarAtual() + " / 4" + detalheAndar + "</b></html>");

        lblStatusMonstro.setText("<html><b>" + monstro.getNome() + "</b> [" + monstro.getElemento()
                + "]<br>HP: " + monstro.getHpAtual() + "/" + monstro.getHpMax()
                + " | ATQ: " + monstro.getAtaque() + " | DEF: " + monstro.getDefesa() + "</html>");

        if (jogo.isJogoFinalizado()) {
            atualizarEstadoBotoes(false);
            btnCarregar.setEnabled(true);
            btnIniciar.setEnabled(true);
        }
    }

    private void atualizarEstadoBotoes(boolean partidaAtiva) {
        btnAtacar.setEnabled(partidaAtiva);
        btnHabilidade.setEnabled(partidaAtiva);
        btnUsarItem.setEnabled(partidaAtiva);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaJogo tela = new TelaJogo();
            tela.setVisible(true);
        });
    }
}