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
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class DialogGuiaTorre extends JDialog {

    public DialogGuiaTorre(Frame parent) {
        super(parent, "Guia da Torre e Elementos", true);
        configurarDialogo();
        inicializarComponentes();
    }

    private void configurarDialogo() {
        setSize(600, 520);
        setLocationRelativeTo(getParent());
        setResizable(false);
        getContentPane().setBackground(TemaFantasia.PERGAMINHO_CLARO);
        setLayout(new BorderLayout(8, 8));
    }

    private void inicializarComponentes() {
        // Topo com Título
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        painelTopo.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelTopo.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, TemaFantasia.MADEIRA_ESCURA));

        JLabel lblTitulo = new JLabel("📜 Guia da Torre e Elementos", JLabel.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 18));
        lblTitulo.setForeground(TemaFantasia.MADEIRA_ESCURA);
        painelTopo.add(lblTitulo);
        add(painelTopo, BorderLayout.NORTH);

        // Centro: Texto formatado com as regras do jogo
        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        textPane.setBackground(TemaFantasia.PERGAMINHO_DESTACADO);
        textPane.setFont(TemaFantasia.FONTE_TEXTO);

        String conteudoHtml = "<html><body style='font-family:sans-serif; color:#2C1810; padding:10px;'>"
                + "<h3 style='color:#8B5A2B; margin-bottom:4px;'>1. Ciclo de Vantagens Elementais</h3>"
                + "<p style='margin-top:2px;'><b>Ciclo principal:</b> Pyro &rarr; Anemo &rarr; Electro &rarr; Hydro &rarr; Pyro</p>"
                + "<ul>"
                + "<li><b>Pyro:</b> x1.5 contra Anemo (<i>Propagação de Chamas</i>), x1.4 contra Electro (<i>Sobrecarga</i>), x0.8 contra Hydro (<i>Chamas Apagadas</i>).</li>"
                + "<li><b>Anemo:</b> x1.35 contra qualquer elemento diferente (<i>Dispersão Elemental</i>).</li>"
                + "<li><b>Electro:</b> x1.6 contra Hydro (<i>Eletrocussão Intensa</i>), x1.3 contra Pyro (<i>Sobrecarga</i>).</li>"
                + "<li><b>Hydro:</b> x1.6 contra Pyro (<i>Vaporização Crítica</i>), x1.3 contra Electro (<i>Condução Elétrica</i>).</li>"
                + "<li><b>Mesmo elemento:</b> x0.65 (<i>Resistência Elemental, -35% de dano</i>).</li>"
                + "</ul>"
                + "<h3 style='color:#8B5A2B; margin-bottom:4px;'>2. As 4 Classes de Heróis</h3>"
                + "<ul>"
                + "<li><b>Espadachim:</b> HP 115 | Mana 40 | ATQ 22 | DEF 10<br>"
                + "<i>Habilidade:</i> Lâmina Elemental (15 Mana) | <i>Passiva:</i> 16% de chance de esquiva/bloqueio.</li>"
                + "<li><b>Arcanista:</b> HP 90 | Mana 75 | ATQ 25 | DEF 5<br>"
                + "<i>Habilidade:</i> Explosão Arcana (20 Mana) | <i>Passiva:</i> +15% de dano em reações elementais.</li>"
                + "<li><b>Atirador:</b> HP 95 | Mana 55 | ATQ 24 | DEF 7<br>"
                + "<i>Habilidade:</i> Disparo Perfurante (14 Mana) | <i>Passiva:</i> 28% de chance de acerto crítico (150% de dano).</li>"
                + "<li><b>Catalisador:</b> HP 85 | Mana 80 | ATQ 23 | DEF 5<br>"
                + "<i>Habilidade:</i> Ressonância Suprema (22 Mana) | <i>Passiva:</i> Ataques normais aplicam reação elemental e crítico.</li>"
                + "</ul>"
                + "<h3 style='color:#8B5A2B; margin-bottom:4px;'>3. Os 4 Guardiões da Torre</h3>"
                + "<ul>"
                + "<li><b>Andar 1: Slime Gigante Pyro:</b> Pode aplicar Queimadura contínua por 2 turnos.</li>"
                + "<li><b>Andar 2: Espectro das Marés (Hydro):</b> Pode conjurar Manto das Marés para atacar e regenerar vida.</li>"
                + "<li><b>Andar 3: Guardião dos Ventos (Anemo):</b> Alta esquiva (18%) e golpe Ciclone Cortante.</li>"
                + "<li><b>Andar 4: Arconte da Tempestade (Electro):</b> Chefe final; disfere Raio Cataclísmico e queima 10 de Mana.</li>"
                + "</ul>"
                + "</body></html>";

        textPane.setText(conteudoHtml);
        textPane.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane(textPane);
        scroll.setBorder(TemaFantasia.criarBordaMadeiraPergaminho("Manual de Regras"));
        add(scroll, BorderLayout.CENTER);

        // Rodapé com Botão Fechar
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        painelRodape.setBackground(TemaFantasia.PERGAMINHO_MEDIO);
        painelRodape.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, TemaFantasia.MADEIRA_ESCURA));

        JButton btnFechar = TemaFantasia.criarBotaoMadeira("Fechar", TemaFantasia.MADEIRA_MEDIA);
        btnFechar.setPreferredSize(new Dimension(140, 32));
        btnFechar.addActionListener(e -> dispose());
        painelRodape.add(btnFechar);

        add(painelRodape, BorderLayout.SOUTH);
    }
}
