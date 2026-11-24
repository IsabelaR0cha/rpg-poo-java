package testes;

import combate.Batalha;
import combate.Jogo;
import excecoes.ItemNaoEncontradoException;
import excecoes.ManaInsuficienteException;
import excecoes.PersistenciaException;
import itens.Item;
import itens.TipoItem;
import java.io.File;
import modelo.Arcanista;
import modelo.Atirador;
import modelo.Catalisador;
import modelo.Elemento;
import modelo.Espadachim;
import modelo.Heroi;
import modelo.Monstro;
import persistencia.GerenciadorPersistencia;

/**
 * Suíte de Testes Automatizados de Regras de Negócio e POO.
 * Executável via linha de comando sem necessidade de interface gráfica.
 * 
 * Disciplina: Programação Orientada a Objetos — UFU
 * Equipe: Celina, Isabela Dantas, Isabela Rocha
 */
public class TesteIntegracao {

    private static int testesExecutados = 0;
    private static int testesAprovados = 0;

    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("   TORRE DOS ELEMENTOS — SUÍTE DE TESTES AUTOMATIZADOS (POO)     ");
        System.out.println("==================================================================");

        testarConstantesEConstrutoresDasClasses();
        testarHerancaEPolimorfismoNasHabilidades();
        testarCicloEMultiplicadoresElementais();
        testarDanoCriticoEElementalDoCatalisador();
        testarProgressaoESubidaDeNivel();
        testarTratamentoDeExcecoesEControleDeTurnos();
        testarInventarioEConsumoDeItens();
        testarPersistenciaECheckpointPorAndar();

        System.out.println("\n------------------------------------------------------------------");
        System.out.println("RESULTADO FINAL: " + testesAprovados + "/" + testesExecutados + " testes aprovados!");
        System.out.println("------------------------------------------------------------------");

        if (testesAprovados == testesExecutados) {
            System.out.println(">>> SUCESSO: Todas as regras de negócio e contratos de POO passaram! <<<");
        } else {
            System.err.println(">>> FALHA: Alguns testes falharam! Revise o log acima. <<<");
            System.exit(1);
        }
    }

    private static void afirme(boolean condicao, String mensagem) {
        testesExecutados++;
        if (condicao) {
            testesAprovados++;
            System.out.println("  [OK] " + mensagem);
        } else {
            System.err.println("  [FALHA] " + mensagem);
            throw new AssertionError("Falha no teste: " + mensagem);
        }
    }

    private static void testarConstantesEConstrutoresDasClasses() {
        System.out.println("\n[1] Testando Atributos Base Canônicos e Construtores das 4 Classes...");

        Espadachim esp = new Espadachim("Arthur", Elemento.PYRO);
        afirme(esp.getHpMax() == 115, "Espadachim possui 115 de HP Máximo");
        afirme(esp.getManaMax() == 40, "Espadachim possui 40 de Mana Máxima");
        afirme(esp.getAtaque() == 22, "Espadachim possui 22 de Ataque");
        afirme(esp.getDefesa() == 10, "Espadachim possui 10 de Defesa");
        afirme(esp.getCustoManaHabilidade() == 15, "Espadachim gasta 15 de Mana na Habilidade");
        afirme(esp.getChanceEsquiva() == 0.16, "Espadachim possui 16% de Esquiva passiva");

        Arcanista arc = new Arcanista("Merlin", Elemento.HYDRO);
        afirme(arc.getHpMax() == 90, "Arcanista possui 90 de HP Máximo");
        afirme(arc.getManaMax() == 75, "Arcanista possui 75 de Mana Máxima");
        afirme(arc.getAtaque() == 25, "Arcanista possui 25 de Ataque");
        afirme(arc.getDefesa() == 5, "Arcanista possui 5 de Defesa");
        afirme(arc.getCustoManaHabilidade() == 20, "Arcanista gasta 20 de Mana na Habilidade");

        Atirador ati = new Atirador("Robin", Elemento.ANEMO);
        afirme(ati.getHpMax() == 95, "Atirador possui 95 de HP Máximo");
        afirme(ati.getManaMax() == 55, "Atirador possui 55 de Mana Máxima");
        afirme(ati.getAtaque() == 24, "Atirador possui 24 de Ataque");
        afirme(ati.getDefesa() == 7, "Atirador possui 7 de Defesa");
        afirme(ati.getCustoManaHabilidade() == 14, "Atirador gasta 14 de Mana na Habilidade");
        afirme(ati.getChanceCritico() == 0.28, "Atirador possui 28% de Chance de Crítico");

        Catalisador cat = new Catalisador("Klee", Elemento.ELECTRO);
        afirme(cat.getHpMax() == 85, "Catalisador possui 85 de HP Máximo");
        afirme(cat.getManaMax() == 80, "Catalisador possui 80 de Mana Máxima");
        afirme(cat.getAtaque() == 23, "Catalisador possui 23 de Ataque");
        afirme(cat.getDefesa() == 5, "Catalisador possui 5 de Defesa");
        afirme(cat.getCustoManaHabilidade() == 22, "Catalisador gasta 22 de Mana na Habilidade");
    }

    private static void testarHerancaEPolimorfismoNasHabilidades() {
        System.out.println("\n[2] Testando Polimorfismo e Habilidades Especiais...");

        Heroi heroiPolimorfico = new Arcanista("Vento", Elemento.ANEMO);
        Monstro monstroAlvo = new Monstro("Treino", 500, 10, 0, 1, Elemento.ELECTRO);

        int manaInicial = heroiPolimorfico.getManaAtual();
        int hpMonstroAntes = monstroAlvo.getHpAtual();

        try {
            heroiPolimorfico.usarHabilidade(monstroAlvo);
            afirme(heroiPolimorfico.getManaAtual() == manaInicial - heroiPolimorfico.getCustoManaHabilidade(),
                    "Polimorfismo: uso de habilidade deduziu a mana correta da classe");
            afirme(monstroAlvo.getHpAtual() < hpMonstroAntes,
                    "Polimorfismo: dano da habilidade especial foi aplicado ao alvo");
        } catch (ManaInsuficienteException e) {
            afirme(false, "Não deveria falhar com mana suficiente");
        }
    }

    private static void testarCicloEMultiplicadoresElementais() {
        System.out.println("\n[3] Testando Tabela de Reações e Multiplicadores Elementais...");

        afirme(Elemento.PYRO.getMultiplicadorContra(Elemento.ANEMO) == 1.5, "Pyro x Anemo = 1.5 (Propagação)");
        afirme(Elemento.PYRO.getMultiplicadorContra(Elemento.ELECTRO) == 1.4, "Pyro x Electro = 1.4 (Sobrecarga)");
        afirme(Elemento.PYRO.getMultiplicadorContra(Elemento.HYDRO) == 0.8, "Pyro x Hydro = 0.8 (Apagado)");

        afirme(Elemento.ANEMO.getMultiplicadorContra(Elemento.PYRO) == 1.35, "Anemo x Pyro = 1.35 (Dispersão)");
        afirme(Elemento.ANEMO.getMultiplicadorContra(Elemento.HYDRO) == 1.35, "Anemo x Hydro = 1.35 (Dispersão)");
        afirme(Elemento.ANEMO.getMultiplicadorContra(Elemento.ELECTRO) == 1.35, "Anemo x Electro = 1.35 (Dispersão)");

        afirme(Elemento.ELECTRO.getMultiplicadorContra(Elemento.HYDRO) == 1.6, "Electro x Hydro = 1.6 (Eletrocussão)");
        afirme(Elemento.ELECTRO.getMultiplicadorContra(Elemento.PYRO) == 1.3, "Electro x Pyro = 1.3 (Sobrecarga)");

        afirme(Elemento.HYDRO.getMultiplicadorContra(Elemento.PYRO) == 1.6, "Hydro x Pyro = 1.6 (Vaporização)");
        afirme(Elemento.HYDRO.getMultiplicadorContra(Elemento.ELECTRO) == 1.3, "Hydro x Electro = 1.3 (Condução)");

        afirme(Elemento.PYRO.getMultiplicadorContra(Elemento.PYRO) == 0.65, "Mesmo Elemento (Pyro x Pyro) = 0.65 (Resistência)");
        afirme(Elemento.HYDRO.getMultiplicadorContra(Elemento.HYDRO) == 0.65, "Mesmo Elemento (Hydro x Hydro) = 0.65 (Resistência)");
    }

    private static void testarDanoCriticoEElementalDoCatalisador() {
        System.out.println("\n[4] Testando Dano Crítico e Elemental do Catalisador...");

        Catalisador catHydro = new Catalisador("Barbara", Elemento.HYDRO);
        Monstro monstroPyro = new Monstro("Slime Fogo", 600, 10, 0, 1, Elemento.PYRO); // Hydro x Pyro = 1.6x

        int hpAntes = monstroPyro.getHpAtual();
        catHydro.atacar(monstroPyro, false);
        int danoNormal = hpAntes - monstroPyro.getHpAtual();
        int danoEsperadoNormal = (int) (23 * 1.6);
        afirme(danoNormal == danoEsperadoNormal, "Dano normal do Catalisador aplica multiplicador elemental (23 * 1.6 = 36)");

        hpAntes = monstroPyro.getHpAtual();
        catHydro.atacar(monstroPyro, true);
        int danoCritico = hpAntes - monstroPyro.getHpAtual();
        int danoEsperadoCritico = (int) (danoEsperadoNormal * 1.5);
        afirme(danoCritico == danoEsperadoCritico, "Dano crítico do Catalisador multiplica sobre o dano elemental (36 * 1.5 = 54)");
        afirme(danoCritico > danoNormal, "Ataque crítico do Catalisador é estritamente maior que o ataque normal");
    }

    private static void testarProgressaoESubidaDeNivel() {
        System.out.println("\n[5] Testando Subida de Nível e Bônus Canônicos...");

        Espadachim heroi = new Espadachim("Zack", Elemento.ANEMO);
        int hpBase = heroi.getHpMax();
        int manaBase = heroi.getManaMax();
        int atqBase = heroi.getAtaque();
        int defBase = heroi.getDefesa();

        heroi.subirNivel();

        afirme(heroi.getNivel() == 2, "Herói sobe para o Nível 2");
        afirme(heroi.getHpMax() == hpBase + Heroi.GANHO_HP_NIVEL, "Ganho de HP por nível é exatamente +" + Heroi.GANHO_HP_NIVEL);
        afirme(heroi.getManaMax() == manaBase + Heroi.GANHO_MANA_NIVEL, "Ganho de Mana por nível é exatamente +" + Heroi.GANHO_MANA_NIVEL);
        afirme(heroi.getAtaque() == atqBase + Heroi.GANHO_ATAQUE_NIVEL, "Ganho de Ataque por nível é exatamente +" + Heroi.GANHO_ATAQUE_NIVEL);
        afirme(heroi.getDefesa() == defBase + Heroi.GANHO_DEFESA_NIVEL, "Ganho de Defesa por nível é exatamente +" + Heroi.GANHO_DEFESA_NIVEL);
    }

    private static void testarTratamentoDeExcecoesEControleDeTurnos() {
        System.out.println("\n[6] Testando Exceções Customizadas e Não-Avanço de Turnos...");

        Espadachim heroi = new Espadachim("Cloud", Elemento.PYRO);
        heroi.gastarMana(heroi.getManaAtual()); // Esvazia toda a mana

        Monstro monstro = new Monstro("Treinador", 200, 0, 0, 1, Elemento.ANEMO);
        Batalha batalha = new Batalha(heroi, monstro);

        // 1. ManaInsuficienteException
        try {
            batalha.executarHabilidade();
            afirme(false, "Deveria lançar ManaInsuficienteException ao usar habilidade sem mana");
        } catch (ManaInsuficienteException ex) {
            afirme(batalha.getTurno() == 1, "Turno NÃO avança quando ManaInsuficienteException é lançada");
        }

        // 2. ItemNaoEncontradoException
        Jogo jogo = new Jogo(heroi);
        try {
            jogo.usarItemNoTurno("PocaoInexistenteDoCaos");
            afirme(false, "Deveria lançar ItemNaoEncontradoException ao tentar consumir item inexistente");
        } catch (ItemNaoEncontradoException ex) {
            afirme(jogo.getBatalhaAtual().getTurno() == 1, "Turno NÃO avança quando ItemNaoEncontradoException é lançada");
        }
    }

    private static void testarInventarioEConsumoDeItens() {
        System.out.println("\n[7] Testando Agregação de Itens no Inventário e Efeito de Cura...");

        Espadachim heroi = new Espadachim("Sora", Elemento.HYDRO);
        heroi.receberDano(50);
        int hpAposDano = heroi.getHpAtual();

        heroi.getInventario().adicionarItem(new Item("Poção Teste", TipoItem.CURA, 30, "Cura 30 HP"));
        afirme(heroi.getInventario().getItens().size() == 3, "Inventário inicial (2) + 1 novo item = 3 itens");

        try {
            heroi.getInventario().usarItem("Poção Teste", heroi);
            afirme(heroi.getHpAtual() == hpAposDano + 30, "Uso da poção de cura restaurou corretamente 30 pontos de HP");
        } catch (ItemNaoEncontradoException e) {
            afirme(false, "Item recém-adicionado deveria existir na mochila");
        }
    }

    private static void testarPersistenciaECheckpointPorAndar() {
        System.out.println("\n[8] Testando Gravação e Carregamento de Checkpoint em Disco...");

        String arquivoTeste = "checkpoint_teste_unitario.txt";
        GerenciadorPersistencia gp = new GerenciadorPersistencia(arquivoTeste);

        Atirador atirador = new Atirador("Sinon", Elemento.ELECTRO);
        Jogo jogoOriginal = new Jogo(atirador);

        try {
            gp.salvarJogo(jogoOriginal);
            afirme(gp.existeSave(), "Arquivo de checkpoint foi gerado no disco com sucesso");

            Jogo jogoRecuperado = gp.carregarJogo();
            afirme(jogoRecuperado.getHeroi().getNome().equals("Sinon"), "Nome do herói recuperado com fidelidade");
            afirme(jogoRecuperado.getHeroi().getClass() == Atirador.class, "Classe correta do herói instanciada na leitura");
            afirme(jogoRecuperado.getHeroi().getElemento() == Elemento.ELECTRO, "Afinidade elemental preservada");
            afirme(jogoRecuperado.getNumeroAndarAtual() == 1, "Retomada garantida no início do andar (checkpoint)");
        } catch (PersistenciaException e) {
            afirme(false, "Falha de I/O na persistência: " + e.getMessage());
        } finally {
            File f = new File(arquivoTeste);
            if (f.exists()) {
                f.delete();
            }
        }
    }
}
