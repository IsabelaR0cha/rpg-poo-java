package persistencia;

import combate.Jogo;
import excecoes.PersistenciaException;
import itens.Item;
import itens.TipoItem;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import modelo.Arcanista;
import modelo.Atirador;
import modelo.Catalisador;
import modelo.Elemento;
import modelo.Espadachim;
import modelo.Heroi;

public class GerenciadorPersistencia {
    private static final String ARQUIVO_PADRAO = "save_jogo.txt";
    private static final String ARQUIVO_PLATINA = "conquistas_torre.txt";
    private final String caminhoArquivo;

    public GerenciadorPersistencia() {
        this(ARQUIVO_PADRAO);
    }

    public GerenciadorPersistencia(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public void registrarVitoriaClasse(String nomeClasse) {
        if (nomeClasse == null || nomeClasse.trim().isEmpty()) {
            return;
        }
        java.util.Set<String> classes = carregarClassesVitoriosas();
        classes.add(nomeClasse.trim());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_PLATINA))) {
            for (String c : classes) {
                writer.write(c);
                writer.newLine();
            }
        } catch (IOException ignored) {
        }
    }

    public java.util.Set<String> carregarClassesVitoriosas() {
        java.util.Set<String> classes = new java.util.HashSet<>();
        File f = new File(ARQUIVO_PLATINA);
        if (!f.exists()) {
            return classes;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String l;
            while ((l = reader.readLine()) != null) {
                if (!l.trim().isEmpty()) {
                    classes.add(l.trim());
                }
            }
        } catch (IOException ignored) {
        }
        return classes;
    }

    public void salvarJogo(Jogo jogo) throws PersistenciaException {
        if (jogo == null || jogo.getHeroi() == null) {
            throw new PersistenciaException("[!] Não há jogo ou herói válido para salvar.");
        }

        Heroi heroi = jogo.getHeroi();
        modelo.Monstro monstro = jogo.getMonstroAtual();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.caminhoArquivo))) {
            writer.write("ANDAR;" + jogo.getNumeroAndarAtual());
            writer.newLine();

            if (monstro != null && monstro.estaVivo()) {
                writer.write("MONSTRO;" + monstro.getHpAtual() + ";" + monstro.getHpMax());
                writer.newLine();
            }

            writer.write("HEROI;"
                    + heroi.getClass().getSimpleName() + ";"
                    + heroi.getNome() + ";"
                    + heroi.getElemento().name() + ";"
                    + heroi.getNivel() + ";"
                    + heroi.getHpAtual() + ";"
                    + heroi.getHpMax() + ";"
                    + heroi.getManaAtual() + ";"
                    + heroi.getManaMax() + ";"
                    + heroi.getAtaque() + ";"
                    + heroi.getDefesa());
            writer.newLine();

            for (Item item : heroi.getInventario().getItens()) {
                writer.write("ITEM;"
                        + item.getNome() + ";"
                        + item.getTipo().name() + ";"
                        + item.getValorEfeito());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new PersistenciaException("[!] Erro ao salvar o progresso do jogo em '" + this.caminhoArquivo + "'.", e);
        }
    }

    public Jogo carregarJogo() throws PersistenciaException {
        File arquivo = new File(this.caminhoArquivo);
        if (!arquivo.exists()) {
            throw new PersistenciaException("[!] Nenhum save encontrado em '" + this.caminhoArquivo + "'.");
        }

        int andarSalvo = 1;
        int hpMonstroSalvo = -1;
        Heroi heroiCarregado = null;
        boolean primeiroItem = true;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue;
                }

                String[] partes = linha.split(";");
                String tipoRegistro = partes[0];

                switch (tipoRegistro) {
                    case "ANDAR":
                        if (partes.length < 2) {
                            throw new PersistenciaException("[!] Linha ANDAR inválida no arquivo de save.");
                        }
                        andarSalvo = Integer.parseInt(partes[1]);
                        break;

                    case "MONSTRO":
                        if (partes.length >= 2) {
                            hpMonstroSalvo = Integer.parseInt(partes[1]);
                        }
                        break;

                    case "HEROI":
                        if (partes.length < 11) {
                            throw new PersistenciaException("[!] Linha HEROI incompleta no arquivo de save.");
                        }
                        heroiCarregado = recriarHeroi(partes);
                        break;

                    case "ITEM":
                        if (heroiCarregado == null) {
                            throw new PersistenciaException("[!] Item encontrado antes dos dados do herói.");
                        }
                        if (partes.length < 4) {
                            throw new PersistenciaException("[!] Linha ITEM inválida no arquivo de save.");
                        }
                        // Limpa os itens padrão iniciais apenas uma vez ao carregar os itens reais salvos
                        if (primeiroItem) {
                            limparInventario(heroiCarregado);
                            primeiroItem = false;
                        }
                        Item item = new Item(
                                partes[1],
                                TipoItem.valueOf(partes[2]),
                                Integer.parseInt(partes[3])
                        );
                        heroiCarregado.getInventario().adicionarItem(item);
                        break;

                    default:
                        throw new PersistenciaException("[!] Registro desconhecido no save: " + tipoRegistro);
                }
            }

            if (heroiCarregado == null) {
                throw new PersistenciaException("[!] Dados do herói ausentes no arquivo de save.");
            }

            if (primeiroItem) {
                limparInventario(heroiCarregado);
            }

            int indiceAndar = Math.max(0, Math.min(andarSalvo - 1, 3));
            return Jogo.restaurarJogo(heroiCarregado, indiceAndar, hpMonstroSalvo);

        } catch (IOException | IllegalArgumentException e) {
            if (e instanceof PersistenciaException) {
                throw (PersistenciaException) e;
            }
            throw new PersistenciaException("[!] Falha ao carregar o save de '" + this.caminhoArquivo + "'.", e);
        }
    }

    public boolean existeSave() {
        return new File(this.caminhoArquivo).exists();
    }

    private Heroi recriarHeroi(String[] partes) throws PersistenciaException {
        String classeHeroi = partes[1];
        String nome = partes[2];
        Elemento elemento = Elemento.valueOf(partes[3]);
        int nivel = Integer.parseInt(partes[4]);
        int hpAtual = Integer.parseInt(partes[5]);
        int hpMax = Integer.parseInt(partes[6]);
        int manaAtual = Integer.parseInt(partes[7]);
        int manaMax = Integer.parseInt(partes[8]);
        int ataque = Integer.parseInt(partes[9]);
        int defesa = Integer.parseInt(partes[10]);

        Heroi heroi;
        switch (classeHeroi) {
            case "Espadachim":
                heroi = new Espadachim(nome, hpMax, ataque, defesa, manaMax, elemento);
                break;
            case "Arcanista":
                heroi = new Arcanista(nome, hpMax, ataque, defesa, manaMax, elemento);
                break;
            case "Atirador":
                heroi = new Atirador(nome, hpMax, ataque, defesa, manaMax, elemento);
                break;
            case "Catalisador":
                heroi = new Catalisador(nome, hpMax, ataque, defesa, manaMax, elemento);
                break;
            default:
                throw new PersistenciaException("[!] Classe de herói desconhecida no save: " + classeHeroi);
        }

        for (int i = 1; i < nivel; i++) {
            heroi.subirNivel();
        }
        heroi.definirEstadoSalvo(hpAtual, hpMax, manaAtual, manaMax, ataque, defesa);
        return heroi;
    }

    private void limparInventario(Heroi heroi) throws PersistenciaException {
        while (!heroi.getInventario().estaVazio()) {
            Item primeiro = heroi.getInventario().getItens().get(0);
            try {
                heroi.getInventario().removerItem(primeiro.getNome());
            } catch (excecoes.ItemNaoEncontradoException e) {
                throw new PersistenciaException("[!] Erro ao sincronizar inventário carregado.", e);
            }
        }
    }
}