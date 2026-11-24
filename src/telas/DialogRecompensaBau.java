package telas;

import itens.Item;
import itens.TipoItem;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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

public class DialogRecompensaBau extends JDialog {

    public DialogRecompensaBau(Frame parent, int nivelNovo, String eventoNome, String eventoDescricao, Item itemRecebido) {
        super(parent, "Recompensa do Andar — Torre dos Elementos", true);
        configurarDialogo();
        inicializarComponentes(nivelNovo, eventoNome, eventoDescricao, itemRecebido);
    }

    private void configurarDialogo() {
        setSize(520, 390);
        setLocationRelativeTo(getParent());
        setResizable(false);
        getContentPane().setBackground(TemaFantasia.PERGAMINHO_CLARO);
        setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes(int nivelNovo, String eventoNome, String eventoDescricao, Item itemRecebido) {
        // Cabeçalho da Vitória
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        painelTopo.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelTopo.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, TemaFantasia.MADEIRA_ESCURA));

        JLabel lblTitulo = new JLabel("🏆 ANDAR CONCLUÍDO!", JLabel.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 20));
        lblTitulo.setForeground(TemaFantasia.MADEIRA_ESCURA);
        painelTopo.add(lblTitulo);
        add(painelTopo, BorderLayout.NORTH);

        // Painel Central com os destaques da vitória
        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));
        painelCentro.setOpaque(false);
        painelCentro.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        // 1. Card de Subida de Nível (Level Up)
        JPanel cardLevelUp = new JPanel(new BorderLayout(6, 4));
        cardLevelUp.setBackground(TemaFantasia.PERGAMINHO_DESTACADO);
        cardLevelUp.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaFantasia.OURO_BORDA, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JLabel lblNivel = new JLabel("🆙 VOCÊ SUBIU PARA O NÍVEL " + nivelNovo + "!");
        lblNivel.setFont(new Font("Serif", Font.BOLD, 15));
        lblNivel.setForeground(new Color(180, 83, 9));

        JLabel lblBonus = new JLabel("<html>Bônus de Atributos: <b>" + modelo.Heroi.getDescricaoBonusNivel() + "</b><br>"
                + "<small style='color:#5C3A21;'>Vitalidade e energia foram parcialmente restauradas com a vitória!</small></html>");
        lblBonus.setFont(TemaFantasia.FONTE_TEXTO);
        lblBonus.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);

        cardLevelUp.add(lblNivel, BorderLayout.NORTH);
        cardLevelUp.add(lblBonus, BorderLayout.CENTER);
        painelCentro.add(cardLevelUp);
        painelCentro.add(Box.createVerticalStrut(10));

        // 2. Card de Recompensa de Baú / Evento
        JPanel cardEvento = new JPanel(new BorderLayout(6, 4));
        cardEvento.setBackground(TemaFantasia.PERGAMINHO_DESTACADO);
        cardEvento.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaFantasia.MADEIRA_ESCURA, 2, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        String tituloEvento = (eventoNome != null && !eventoNome.isEmpty()) ? eventoNome : "🎁 Baú de Tesouro do Andar";
        JLabel lblEventoTitulo = new JLabel(tituloEvento);
        lblEventoTitulo.setFont(new Font("Serif", Font.BOLD, 14));
        lblEventoTitulo.setForeground(TemaFantasia.MADEIRA_ESCURA);

        String textoEvento = (eventoDescricao != null && !eventoDescricao.isEmpty())
                ? eventoDescricao
                : "Você abriu o baú de tesouro ancestral deixado pelo guardião.";

        JLabel lblEventoDesc = new JLabel("<html>" + textoEvento + "</html>");
        lblEventoDesc.setFont(TemaFantasia.FONTE_TEXTO);
        lblEventoDesc.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);

        cardEvento.add(lblEventoTitulo, BorderLayout.NORTH);
        cardEvento.add(lblEventoDesc, BorderLayout.CENTER);

        // Se recebeu item específico, destaca detalhes do item
        if (itemRecebido != null) {
            JPanel cardItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
            cardItem.setOpaque(false);
            cardItem.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, TemaFantasia.OURO_BORDA));

            String badge = (itemRecebido.getTipo() == TipoItem.CURA) ? "[CURA + " + itemRecebido.getValorEfeito() + " HP]" : "[MANA + " + itemRecebido.getValorEfeito() + " Mana]";
            JLabel lblItemBadge = new JLabel(badge);
            lblItemBadge.setFont(TemaFantasia.FONTE_TEXTO_BOLD);
            lblItemBadge.setForeground(itemRecebido.getTipo() == TipoItem.CURA ? TemaFantasia.COR_ANEMO : TemaFantasia.COR_HYDRO);

            JLabel lblItemNome = new JLabel("<b>" + itemRecebido.getNome() + "</b>");
            lblItemNome.setFont(TemaFantasia.FONTE_TEXTO);
            lblItemNome.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);

            cardItem.add(lblItemBadge);
            cardItem.add(lblItemNome);
            cardEvento.add(cardItem, BorderLayout.SOUTH);
        }

        painelCentro.add(cardEvento);
        add(painelCentro, BorderLayout.CENTER);

        // Painel Inferior de Confirmação
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoes.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelBotoes.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, TemaFantasia.MADEIRA_ESCURA));

        JButton btnContinuar = TemaFantasia.criarBotaoMadeira("🏰 Continuar Aventura", new Color(180, 83, 9));
        btnContinuar.setPreferredSize(new Dimension(220, 36));
        btnContinuar.addActionListener(e -> dispose());

        painelBotoes.add(btnContinuar);
        add(painelBotoes, BorderLayout.SOUTH);
    }
}
