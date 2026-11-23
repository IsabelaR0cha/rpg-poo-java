# RPG POO Java — Torre dos Elementos ⚔️📜

![Java](https://img.shields.io/badge/Java-11%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Java%20Swing-blue?style=for-the-badge)
![POO](https://img.shields.io/badge/Conceitos-POO%20Avançado-brightgreen?style=for-the-badge)

## Apresentação do Projeto

**Torre dos Elementos** é um RPG tático em turnos desenvolvido em **Java puro** (`javax.swing` + `java.awt`) como trabalho final da disciplina de **Programação Orientada a Objetos** da **Universidade Federal de Uberlândia (UFU)**.

O jogador assume o papel de um Viajante que desbrava os 4 andares de uma torre mística guardada por colossos e mímicos elementais. O jogo combina mecânicas de reações elementais em cadeia inspiradas em *Genshin Impact* com uma identidade visual imersiva em estilo **Grimório / Pergaminho de Fantasia Medieval**.

---

## Equipe e Responsabilidades

| Integrante | Matrícula / Função | Responsabilidades Principais |
| :--- | :--- | :--- |
| **Isabela Rocha** | Coordenadora & Arquiteta | Organização do repositório, documentação acadêmica ABNT, diagramação UML, modelagem de classes e revisão de código. |
| **Isabela Dantas** | Engenheira de Domínio & Backend | Mecânicas de combate, cálculo de reações elementais, arquitetura de entidades, persistência em disco (saves/checkpoints) e hierarquia de exceções personalizadas. |
| **Celina** | Designer de UI & QA | Desenvolvimento da interface gráfica Swing (`telas`), customização de componentes visuais (Tema Fantasia), manual do usuário, casos de teste e validação de requisitos. |

---

## 🏛️ Arquitetura e Conceitos de POO

O projeto foi projetado com separação rigorosa de camadas e atende integralmente a todos os pilares da Orientação a Objetos:

- **Classes e Objetos**: Instanciação limpa e gerenciamento de ciclo de vida das entidades (`Heroi`, `Monstro`, `Batalha`, `Jogo`, `Item`).
- **Encapsulamento**: Atributos privados e protegidos, métodos de acesso defensivos e controle restrito de mutações.
- **Herança**: 
  - Hierarquia de personagens: `Entidade` (abstrata) $\rightarrow$ `Heroi` (abstrata) $\rightarrow$ `Espadachim`, `Arcanista`, `Atirador`, `Catalisador`.
  - Hierarquia de inimigos: `Entidade` $\rightarrow$ `Monstro` (com suporte a variantes e Mímicos).
- **Polimorfismo**: Sobrescrita dinâmica de `usarHabilidade(Combativel alvo)`, cálculo customizado de críticos, esquiva e reações elementais específicas por subclasse.
- **Classes Abstratas**: `Entidade` e `Heroi` estabelecem contratos de atributos e comportamentos essenciais.
- **Interfaces**: `Combativel` desacopla o motor de combate (`Batalha`) das classes concretas, permitindo que qualquer combatente receba dano, execute ações e processe estados.
- **Composição e Agregação**:
  - `Heroi` possui um `Inventario` (agregação/composição) composto por instâncias de `Item`.
  - `Jogo` orquestra o estado consolidado da expedição, delegando o combate à classe `Batalha`.
- **Persistência de Dados**: `GerenciadorPersistencia` salva e carrega checkpoints em arquivo texto formatado com validação de integridade.
- **Tratamento de Exceções**: Hierarquia com `ManaInsuficienteException`, `ItemNaoEncontradoException` e `PersistenciaException`, garantindo que ações inválidas sejam tratadas sem interromper o fluxo nem dessincronizar o combate.
- **Interface Gráfica (Swing + AWT)**: Construção modular em `src/telas/` com paleta de cores temática, cards dinâmicos, barras estilizadas e diálogos modais para inventário, baús de recompensa e fim de jogo.

---

## 🎮 Classes e Atributos Canônicos

| Classe | Ícone | HP Base | Mana Base | Ataque | Defesa | Custo Habilidade | Habilidade Elemental | Passiva |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| **Espadachim** | ⚔️ | **115** | **40** | **22** | **10** | **15 MP** | *Lâmina Elemental* (+16 dano + reação) | *Postura Inabalável* (16% esquiva) |
| **Arcanista** | 🔮 | **90** | **75** | **25** | **5** | **20 MP** | *Explosão Arcana* (+22 dano + reação +15%) | *Mestria Elemental* (+15% em reações) |
| **Atirador** | 🏹 | **95** | **55** | **24** | **7** | **14 MP** | *Disparo Perfurante* (145% ATQ + 8 + reação) | *Olho de Falcão* (28% crítico) |
| **Catalisador**| 📖 | **85** | **80** | **23** | **5** | **22 MP** | *Ressonância Suprema* (+25 dano + reação) | *Infusão Mágica* (ataque normal causa reação e crítico) |

### 📈 Bônus por Subida de Nível (Andar Concluído)
A cada guardião derrotado, o herói sobe de nível e recebe o bônus canônico centralizado em `Heroi`:
- **+15 HP Máximo**
- **+8 Mana Máxima**
- **+4 Ataque**
- **+2 Defesa**
- Restauração proporcional de vida e energia.

---

## ⚡ Sistema de Reações Elementais

O sistema segue o ciclo místico da Torre dos Elementos:

$$\text{PYRO} \xrightarrow{\times 1.5} \text{ANEMO} \xrightarrow{\times 1.4} \text{ELECTRO} \xrightarrow{\times 1.35} \text{HYDRO} \xrightarrow{\times 1.6} \text{PYRO}$$

- **Vantagem Elemental**: Causa dano amplificado de acordo com os multiplicadores do ciclo.
- **Desvantagem Elemental**: Causa dano reduzido ($\times 0.8$ ou $\times 0.65$).
- **Efeito Queimadura**: Inimigos do elemento PYRO possuem 35% de chance de infligir combustão no herói por 2 turnos (causando dano passivo contínuo no final de cada turno válido).

---

## 📦 Estrutura de Diretórios

```text
rpg-poo-java/
├── bin/                          # Bytecodes compilados (.class)
├── src/                          # Código-fonte Java modular
│   ├── modelo/                   # Entidades (Entidade, Heroi, Monstro, Elemento, Subclasses)
│   ├── combate/                  # Motor de combate (Batalha, Jogo, Combativel)
│   ├── itens/                    # Inventário e itens consumíveis (Item, Inventario, TipoItem)
│   ├── persistencia/             # Gravação e leitura de checkpoints em disco
│   ├── excecoes/                 # Exceções customizadas de domínio
│   └── telas/                    # Interface gráfica em Java Swing (TemaFantasia, Dialogs)
├── docs/                         # Documentação acadêmica e manuais
│   ├── manual/                   # Manual do Usuário e guia da Torre
│   ├── relatorio/                # Relatório acadêmico ABNT
│   ├── screenshots/              # Capturas das telas do jogo
│   └── uml/                      # Diagrama de Classes UML
├── README.md                     # Apresentação do projeto e instruções
└── .gitignore                    # Regras de exclusão do Git
```

---

## 🚀 Como Compilar e Executar

O projeto não requer dependências externas, bibliotecas de terceiros ou gerenciadores pesados. Basta ter o **Java SE JDK 11 ou superior** instalado.

### No Linux / macOS:
```bash
# 1. Clonar ou navegar até o diretório do projeto
cd rpg-poo-java

# 2. Compilar todas as classes com codificação UTF-8
javac -encoding UTF-8 -d bin $(find src -name "*.java")

# 3. Executar o jogo via interface gráfica Swing
java -cp bin telas.TelaJogo
```

### No Windows (PowerShell):
```powershell
# 1. Compilar
javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName

# 2. Executar
java -cp bin telas.TelaJogo
```

---

## 📖 Documentação Adicional
- [Manual do Usuário Completo](docs/manual/MANUAL_DO_USUARIO.md)
- [Relatório Acadêmico ABNT](docs/relatorio/)
- [Diagramas de Modelagem UML](docs/uml/)
