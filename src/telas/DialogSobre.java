package telas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DialogSobre extends JDialog {

    public DialogSobre(Frame parent) {
        super(parent, "Sobre o Projeto — Torre dos Elementos", true);
        configurarDialogo();
        inicializarComponentes();
    }

    private void configurarDialogo() {
        setSize(480, 320);
        setLocationRelativeTo(getParent());
        setResizable(false);
        getContentPane().setBackground(TemaFantasia.PERGAMINHO_CLARO);
        setLayout(new BorderLayout(8, 8));
    }

    private void inicializarComponentes() {
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        painelTopo.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelTopo.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, TemaFantasia.MADEIRA_ESCURA));

        JLabel lblTitulo = new JLabel("🏰 Torre dos Elementos", JLabel.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 18));
        lblTitulo.setForeground(TemaFantasia.MADEIRA_ESCURA);
        painelTopo.add(lblTitulo);
        add(painelTopo, BorderLayout.NORTH);

        JPanel painelCentro = new JPanel(new BorderLayout());
        painelCentro.setBackground(TemaFantasia.PERGAMINHO_DESTACADO);
        painelCentro.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(12, 16, 12, 16),
                TemaFantasia.criarBordaMadeiraPergaminho("Informações Acadêmicas")
        ));

        String texto = "<html><body style='font-family:sans-serif; color:#2C1810; padding:10px; line-height:1.5;'>"
                + "<b>Disciplina:</b> Programação Orientada a Objetos (POO)<br>"
                + "<b>Curso:</b> Sistemas de Informação — Universidade Federal de Uberlândia (UFU)<br><br>"
                + "<b>Integrantes da Equipe:</b><br>"
                + "&bull; Celina<br>"
                + "&bull; Isabela Dantas<br>"
                + "&bull; Isabela Rocha<br><br>"
                + "<b>Tecnologias:</b> Java SE puro e Java Swing (<code>javax.swing</code> e <code>java.awt</code>)"
                + "</body></html>";

        JLabel lblInfo = new JLabel(texto);
        painelCentro.add(lblInfo, BorderLayout.CENTER);
        add(painelCentro, BorderLayout.CENTER);

        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        painelRodape.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelRodape.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, TemaFantasia.MADEIRA_ESCURA));

        JButton btnFechar = TemaFantasia.criarBotaoMadeira("Fechar", TemaFantasia.MADEIRA_MEDIA);
        btnFechar.setPreferredSize(new Dimension(120, 32));
        btnFechar.addActionListener(e -> dispose());
        painelRodape.add(btnFechar);

        add(painelRodape, BorderLayout.SOUTH);
    }
}
