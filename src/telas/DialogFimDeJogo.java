package telas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import modelo.Heroi;

public class DialogFimDeJogo extends JDialog {

    public DialogFimDeJogo(
            Frame parent,
            boolean vitoria,
            Heroi heroi,
            int andar,
            boolean temSave,
            Runnable onRecarregar,
            Runnable onNovoJogo
    ) {
        super(parent, vitoria ? "Vitória Suprema — Torre dos Elementos" : "Fim da Expedição — Derrota", true);
        configurarDialogo();
        inicializarComponentes(vitoria, heroi, andar, temSave, onRecarregar, onNovoJogo);
    }

    private void configurarDialogo() {
        setSize(520, 360);
        setLocationRelativeTo(getParent());
        setResizable(false);
        getContentPane().setBackground(TemaFantasia.PERGAMINHO_CLARO);
        setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes(
            boolean vitoria,
            Heroi heroi,
            int andar,
            boolean temSave,
            Runnable onRecarregar,
            Runnable onNovoJogo
    ) {
        // Topo com Título e Cor de Destaque
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 12));
        painelTopo.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelTopo.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, TemaFantasia.MADEIRA_ESCURA));

        JLabel lblTitulo = new JLabel(vitoria ? "👑 VITÓRIA SUPREMA NA TORRE!" : "💀 FIM DA EXPEDIÇÃO (DERROTA)");
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 18));
        lblTitulo.setForeground(vitoria ? new Color(180, 83, 9) : TemaFantasia.COR_PYRO);
        painelTopo.add(lblTitulo);
        add(painelTopo, BorderLayout.NORTH);

        // Painel Central com Informações da Jornada
        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));
        painelCentro.setOpaque(false);
        painelCentro.setBorder(BorderFactory.createEmptyBorder(12, 18, 10, 18));

        JPanel cardMensagem = new JPanel(new BorderLayout(6, 6));
        cardMensagem.setBackground(TemaFantasia.PERGAMINHO_DESTACADO);
        cardMensagem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(vitoria ? TemaFantasia.OURO_BORDA : TemaFantasia.COR_PYRO, 2, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        String textoPrincipal;
        if (vitoria) {
            textoPrincipal = "<b>Parabéns, Viajante!</b> Você derrotou todos os 4 guardiões e conquistou o cume da Torre dos Elementos!<br><br>"
                    + "O fluxo de magia primordial foi reequilibrado pelo seu poder.";
        } else {
            textoPrincipal = "<b>Seu herói sucumbiu em combate no Andar " + andar + ".</b><br><br>"
                    + "As forças elementais da torre foram implacáveis desta vez. Não desista!";
        }

        JLabel lblMensagem = new JLabel("<html>" + textoPrincipal + "</html>");
        lblMensagem.setFont(TemaFantasia.FONTE_TEXTO);
        lblMensagem.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);
        cardMensagem.add(lblMensagem, BorderLayout.CENTER);

        // Resumo do Herói
        if (heroi != null) {
            JLabel lblResumoHeroi = new JLabel("<html>Herói: <b>" + heroi.getNome() + "</b> ("
                    + heroi.getClass().getSimpleName() + " - " + heroi.getElemento() + ") | Nível Final: <b>"
                    + heroi.getNivel() + "</b></html>");
            lblResumoHeroi.setFont(TemaFantasia.FONTE_TEXTO_BOLD);
            lblResumoHeroi.setForeground(TemaFantasia.TEXTO_SEPIA_MEDIO);
            lblResumoHeroi.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, TemaFantasia.OURO_BORDA));
            cardMensagem.add(lblResumoHeroi, BorderLayout.SOUTH);
        }

        painelCentro.add(cardMensagem);
        painelCentro.add(Box.createVerticalStrut(10));

        JLabel lblDica = new JLabel(
                vitoria
                        ? "Você pode iniciar uma nova aventura com outra classe ou elemento!"
                        : "Você pode recarregar o último checkpoint salvo ou iniciar com um novo herói.",
                JLabel.CENTER
        );
        lblDica.setFont(TemaFantasia.FONTE_PEQUENA);
        lblDica.setForeground(TemaFantasia.TEXTO_SEPIA_MEDIO);
        lblDica.setAlignmentX(CENTER_ALIGNMENT);
        painelCentro.add(lblDica);

        add(painelCentro, BorderLayout.CENTER);

        // Painel Inferior de Botões de Ação
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoes.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelBotoes.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, TemaFantasia.MADEIRA_ESCURA));

        // Botão Recarregar Checkpoint
        if (!vitoria && temSave && onRecarregar != null) {
            JButton btnRecarregar = TemaFantasia.criarBotaoMadeira("🔄 Tentar do Checkpoint", new Color(41, 128, 185));
            btnRecarregar.addActionListener(e -> {
                dispose();
                onRecarregar.run();
            });
            painelBotoes.add(btnRecarregar);
        }

        // Botão Criar Novo Herói
        JButton btnNovo = TemaFantasia.criarBotaoMadeira(vitoria ? "🌟 Nova Jornada" : "📜 Criar Novo Herói", new Color(180, 83, 9));
        btnNovo.addActionListener(e -> {
            dispose();
            if (onNovoJogo != null) {
                onNovoJogo.run();
            }
        });
        painelBotoes.add(btnNovo);

        // Botão Fechar Diálogo (para poder inspecionar o diário de combate)
        JButton btnFechar = TemaFantasia.criarBotaoMadeira("Ver Diário", TemaFantasia.MADEIRA_MEDIA);
        btnFechar.addActionListener(e -> dispose());
        painelBotoes.add(btnFechar);

        add(painelBotoes, BorderLayout.SOUTH);
    }
}
