package telas;

import combate.Jogo;
import excecoes.ItemNaoEncontradoException;
import itens.Item;
import itens.TipoItem;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class DialogInventario extends JDialog {
    private final Jogo jogo;
    private final Runnable callbackAposUso;

    private DefaultListModel<ItemAgrupado> modelLista;
    private JList<ItemAgrupado> listaItens;
    private JLabel lblDescricaoDetalhada;
    private JButton btnUsar;
    private JButton btnFechar;

    public static class ItemAgrupado {
        private final Item item;
        private final int quantidade;

        public ItemAgrupado(Item item, int quantidade) {
            this.item = item;
            this.quantidade = quantidade;
        }

        public Item getItem() {
            return item;
        }

        public int getQuantidade() {
            return quantidade;
        }

        @Override
        public String toString() {
            String badge = (item.getTipo() == TipoItem.CURA) ? "[CURA]" : "[MANA]";
            return badge + " " + item.getNome() + "  (x" + quantidade + ")  —  Efeito: +" + item.getValorEfeito()
                    + (item.getTipo() == TipoItem.CURA ? " HP" : " Mana");
        }
    }

    public DialogInventario(Frame parent, Jogo jogo, Runnable callbackAposUso) {
        super(parent, "Mochila de Itens do Viajante — Torre dos Elementos", true);
        this.jogo = jogo;
        this.callbackAposUso = callbackAposUso;

        configurarDialogo();
        inicializarComponentes();
        carregarItens();
    }

    private void configurarDialogo() {
        setSize(540, 420);
        setLocationRelativeTo(getParent());
        setResizable(false);
        getContentPane().setBackground(TemaFantasia.PERGAMINHO_CLARO);
        setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        // Cabeçalho estilo página de caderno de itens
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        painelTopo.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelTopo.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, TemaFantasia.MADEIRA_ESCURA));

        JLabel lblTitulo = new JLabel("📜 Mochila de Itens & Alquimia", JLabel.CENTER);
        lblTitulo.setFont(TemaFantasia.FONTE_TITULO);
        lblTitulo.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);
        painelTopo.add(lblTitulo);
        add(painelTopo, BorderLayout.NORTH);

        // Painel Central com Lista de Itens
        JPanel painelCentro = new JPanel(new BorderLayout(6, 6));
        painelCentro.setOpaque(false);
        painelCentro.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

        modelLista = new DefaultListModel<>();
        listaItens = new JList<>(modelLista);
        listaItens.setBackground(TemaFantasia.PERGAMINHO_DESTACADO);
        listaItens.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);
        listaItens.setFont(TemaFantasia.FONTE_TEXTO_BOLD);
        listaItens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaItens.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, TemaFantasia.OURO_BORDA),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));

                if (isSelected) {
                    label.setBackground(TemaFantasia.MADEIRA_MEDIA);
                    label.setForeground(TemaFantasia.TEXTO_CLARO);
                } else {
                    label.setBackground(index % 2 == 0 ? TemaFantasia.PERGAMINHO_CLARO : TemaFantasia.PERGAMINHO_DESTACADO);
                    label.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);
                }

                if (value instanceof ItemAgrupado) {
                    ItemAgrupado agrupado = (ItemAgrupado) value;
                    if (agrupado.getItem().getTipo() == TipoItem.CURA) {
                        label.setIcon(null);
                    }
                }
                return label;
            }
        });

        listaItens.addListSelectionListener(e -> {
            ItemAgrupado sel = listaItens.getSelectedValue();
            if (sel != null) {
                lblDescricaoDetalhada.setText("<html><b>" + sel.getItem().getNome() + ":</b> "
                        + sel.getItem().getDescricao() + "</html>");
                btnUsar.setEnabled(jogo != null && !jogo.isJogoFinalizado());
            } else {
                lblDescricaoDetalhada.setText("Selecione um item da mochila para ver os detalhes.");
                btnUsar.setEnabled(false);
            }
        });

        JScrollPane scroll = new JScrollPane(listaItens);
        scroll.setBorder(TemaFantasia.criarBordaMadeiraPergaminho("Itens Disponíveis"));
        painelCentro.add(scroll, BorderLayout.CENTER);

        // Painel de Detalhes do Item
        JPanel painelDetalhe = new JPanel(new BorderLayout(4, 4));
        painelDetalhe.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelDetalhe.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaFantasia.MADEIRA_ESCURA, 2),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        lblDescricaoDetalhada = new JLabel("Selecione um item da mochila para ver os detalhes.", JLabel.LEFT);
        lblDescricaoDetalhada.setFont(TemaFantasia.FONTE_TEXTO);
        lblDescricaoDetalhada.setForeground(TemaFantasia.TEXTO_SEPIA_ESCURO);
        painelDetalhe.add(lblDescricaoDetalhada, BorderLayout.CENTER);

        painelCentro.add(painelDetalhe, BorderLayout.SOUTH);
        add(painelCentro, BorderLayout.CENTER);

        // Painel Inferior de Botões de Ação
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        painelBotoes.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelBotoes.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, TemaFantasia.MADEIRA_ESCURA));

        btnUsar = TemaFantasia.criarBotaoMadeira("✨ Usar Item Selecionado", new Color(46, 125, 50));
        btnFechar = TemaFantasia.criarBotaoMadeira("Fechar Mochila", TemaFantasia.MADEIRA_MEDIA);

        btnUsar.setEnabled(false);

        btnUsar.addActionListener(e -> usarItemSelecionado());
        btnFechar.addActionListener(e -> dispose());

        painelBotoes.add(btnUsar);
        painelBotoes.add(btnFechar);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    public void carregarItens() {
        modelLista.clear();
        if (jogo == null || jogo.getHeroi() == null) {
            return;
        }

        List<Item> itens = jogo.getHeroi().getInventario().getItens();
        if (itens.isEmpty()) {
            lblDescricaoDetalhada.setText("Sua mochila está vazia no momento.");
            btnUsar.setEnabled(false);
            return;
        }

        Map<String, Item> exemplos = new LinkedHashMap<>();
        Map<String, Integer> contagem = new LinkedHashMap<>();

        for (Item it : itens) {
            exemplos.putIfAbsent(it.getNome(), it);
            contagem.put(it.getNome(), contagem.getOrDefault(it.getNome(), 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : contagem.entrySet()) {
            modelLista.addElement(new ItemAgrupado(exemplos.get(entry.getKey()), entry.getValue()));
        }

        if (!modelLista.isEmpty()) {
            listaItens.setSelectedIndex(0);
        }
    }

    private void usarItemSelecionado() {
        ItemAgrupado selecionado = listaItens.getSelectedValue();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um item da mochila para usar.", "Nenhum Item", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            jogo.usarItemNoTurno(selecionado.getItem().getNome());
            if (callbackAposUso != null) {
                callbackAposUso.run();
            }
            carregarItens();
        } catch (ItemNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao Usar Item", JOptionPane.ERROR_MESSAGE);
        }
    }
}
