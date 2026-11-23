package telas;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import modelo.Elemento;

/**
 * Paleta de cores, tipografia e componentes visuais com estética de
 * pergaminho claro, molduras em madeira escura e metal, e selos elementais.
 */
public final class TemaFantasia {
    // Páginas de Pergaminho
    public static final Color PERGAMINHO_CLARO = new Color(243, 226, 193); // #F3E2C1
    public static final Color PERGAMINHO_MEDIO = new Color(230, 207, 165); // #E6CFA5
    public static final Color PERGAMINHO_DESTACADO = new Color(255, 243, 205); // #FFF3CD
    public static final Color PERGAMINHO_LOG = new Color(247, 236, 215);

    // Madeira Escura e Metal
    public static final Color MADEIRA_ESCURA = new Color(59, 35, 20);      // #3B2314
    public static final Color MADEIRA_MEDIA = new Color(74, 46, 27);       // #4A2E1B
    public static final Color MADEIRA_CLARA = new Color(110, 71, 42);      // #6E472A
    public static final Color METAL_ESCURO = new Color(46, 52, 64);        // #2E3440
    public static final Color METAL_CLARO = new Color(76, 86, 106);

    // Ouro e Detalhes
    public static final Color OURO_VELHO = new Color(184, 134, 11);        // #B8860B
    public static final Color OURO_BRILHANTE = new Color(212, 175, 55);    // #D4AF37
    public static final Color OURO_BORDA = new Color(197, 160, 89);

    // Tintas e Textos
    public static final Color TEXTO_SEPIA_ESCURO = new Color(44, 24, 16);  // #2C1810
    public static final Color TEXTO_SEPIA_MEDIO = new Color(92, 58, 33);   // #5C3A21
    public static final Color TEXTO_CLARO = new Color(250, 246, 238);

    // Elementos da Torre
    public static final Color COR_PYRO = new Color(192, 57, 43);           // #C0392B Fogo
    public static final Color COR_HYDRO = new Color(41, 128, 185);         // #2980B9 Água
    public static final Color COR_ANEMO = new Color(22, 160, 133);         // #16A085 Vento
    public static final Color COR_ELECTRO = new Color(142, 68, 173);       // #8E44AD Trovão

    // Fontes
    public static final Font FONTE_TITULO = new Font("Serif", Font.BOLD, 18);
    public static final Font FONTE_SUBTITULO = new Font("Serif", Font.BOLD, 14);
    public static final Font FONTE_TEXTO_BOLD = new Font("SansSerif", Font.BOLD, 12);
    public static final Font FONTE_TEXTO = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FONTE_PEQUENA = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font FONTE_LOG = new Font("Monospaced", Font.PLAIN, 12);

    private TemaFantasia() {}

    public static Color obterCorElemento(Elemento elemento) {
        if (elemento == null) return OURO_VELHO;
        switch (elemento) {
            case PYRO: return COR_PYRO;
            case HYDRO: return COR_HYDRO;
            case ANEMO: return COR_ANEMO;
            case ELECTRO: return COR_ELECTRO;
            default: return OURO_VELHO;
        }
    }

    public static String obterIconeElemento(Elemento elemento) {
        if (elemento == null) return "✨";
        switch (elemento) {
            case PYRO: return "🔥 PYRO";
            case HYDRO: return "💧 HYDRO";
            case ANEMO: return "🌪️ ANEMO";
            case ELECTRO: return "⚡ ELECTRO";
            default: return "✨";
        }
    }

    public static Border criarBordaMadeiraPergaminho(String titulo) {
        Border bordaExterna = BorderFactory.createLineBorder(MADEIRA_ESCURA, 3, true);
        Border bordaInterna = BorderFactory.createLineBorder(OURO_BORDA, 1);
        Border composta = BorderFactory.createCompoundBorder(bordaExterna, bordaInterna);

        if (titulo == null || titulo.trim().isEmpty()) {
            return BorderFactory.createCompoundBorder(composta, BorderFactory.createEmptyBorder(6, 8, 6, 8));
        }

        return BorderFactory.createTitledBorder(
                composta,
                " " + titulo + " ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                FONTE_SUBTITULO,
                MADEIRA_ESCURA
        );
    }

    public static Border criarBordaMetalica(Color corDestaque) {
        Border bordaExterna = BorderFactory.createLineBorder(METAL_ESCURO, 2, true);
        Border bordaInterna = BorderFactory.createLineBorder(corDestaque != null ? corDestaque : OURO_BORDA, 2);
        return BorderFactory.createCompoundBorder(bordaExterna, bordaInterna);
    }

    public static JButton criarBotaoMadeira(String texto, Color corDestaque) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color corAtual = getBackground() != null ? getBackground() : corDestaque;
                Color corBase;
                Color corBorda;
                if (!isEnabled()) {
                    corBase = new Color(58, 53, 49);
                    corBorda = new Color(125, 116, 104);
                } else {
                    corBase = getModel().isPressed()
                            ? MADEIRA_ESCURA.darker()
                            : (getModel().isRollover() ? corAtual.darker() : corAtual);
                    corBorda = OURO_BORDA;
                }

                g2.setColor(corBase);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2.setColor(corBorda);
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 7, 7);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setBackground(corDestaque);
        btn.setFont(FONTE_TEXTO_BOLD);
        btn.setForeground(TEXTO_CLARO);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return btn;
    }

    public static JProgressBar criarBarraProgresso(String textoInicial, Color corPreenchimento, Color corFundo) {
        JProgressBar barra = new JProgressBar();
        barra.setStringPainted(true);
        barra.setString(textoInicial);
        barra.setForeground(corPreenchimento);
        barra.setBackground(corFundo != null ? corFundo : MADEIRA_ESCURA);
        barra.setFont(new Font("SansSerif", Font.BOLD, 11));
        barra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MADEIRA_ESCURA, 2, true),
                BorderFactory.createLineBorder(OURO_BORDA, 1)
        ));
        barra.setPreferredSize(new Dimension(barra.getPreferredSize().width, 24));
        return barra;
    }
}
