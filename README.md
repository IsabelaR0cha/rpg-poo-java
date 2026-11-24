# RPG POO Java — Torre dos Elementos ⚔️📜

![Java](https://img.shields.io/badge/Java-11%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Java%20Swing-blue?style=for-the-badge)
![POO](https://img.shields.io/badge/Conceitos-POO-brightgreen?style=for-the-badge)

## Apresentação do Projeto

**Torre dos Elementos** é um RPG tático em turnos desenvolvido em **Java puro** (`javax.swing` + `java.awt`) como trabalho final da disciplina de **Programação Orientada a Objetos** do curso de Sistemas de Informação da **Universidade Federal de Uberlândia (UFU)**.

O jogador assume o papel de um Viajante que desbrava os 4 andares de uma torre mística guardada por guardiões elementais, enfrentando eventos aleatórios entre os andares, como baús de tesouro e emboscadas de mímicos. O jogo combina mecânicas de reações elementais inspiradas em *Genshin Impact* com uma identidade visual em estilo **Grimório / Pergaminho de Fantasia Medieval**.

---

## Equipe e Responsabilidades

| Integrante | Função | Responsabilidades Principais |
| :--- | :--- | :--- |
| **Isabela Rocha** | Coordenadora & Arquiteta | Organização do repositório, documentação acadêmica ABNT, diagramação UML, modelagem de classes e revisão de código. |
| **Isabela Dantas** | Engenheira de Domínio & Backend | Mecânicas de combate, cálculo de reações elementais, arquitetura de entidades, persistência em disco (saves/checkpoints) e hierarquia de exceções personalizadas. |
| **Celina** | Designer de UI & QA | Desenvolvimento da interface gráfica Swing (`telas`), customização de componentes visuais (Tema Fantasia), manual do usuário, casos de teste e validação de requisitos. |

---

## 🏛️ Arquitetura e Conceitos de POO

O projeto está organizado em pacotes por responsabilidade (`modelo`, `combate`, `itens`, `persistencia`, `excecoes` e `telas`) e aplica os conceitos de Orientação a Objetos exigidos na disciplina:

- [x] **Classes e Objetos**: as entidades do jogo (`Heroi`, `Monstro`, `Batalha`, `Jogo`, `Item`, `Inventario`) são instanciadas conforme o andamento da partida.
- [x] **Encapsulamento**: atributos privados em `Item`, `Inventario`, `Monstro`, `Batalha` e `Jogo`; atributos protegidos em `Entidade` e `Heroi`; alterações de vida e mana feitas por métodos que validam limites, e `Inventario.getItens()` devolve uma lista não modificável.
- [x] **Herança**:
  - Hierarquia de personagens: `Entidade` (abstrata) → `Heroi` (abstrata) → `Espadachim`, `Arcanista`, `Atirador`, `Catalisador`.
  - Hierarquia de inimigos: `Entidade` → `Monstro` (guardiões dos andares e o Mímico Voraz).
- [x] **Polimorfismo**: sobrescrita de `usarHabilidade(Combativel alvo)` em cada classe de herói, de `atacar` no `Catalisador` e de `getChanceEsquiva` / `getChanceCritico` no `Espadachim` e no `Atirador`.
- [x] **Classes Abstratas**: `Entidade` e `Heroi`; `Heroi` declara a habilidade especial, o custo de mana e as descrições como métodos abstratos.
- [x] **Interfaces**: `Combativel` define o contrato de quem participa do combate (atacar, receber dano, estado de vida, nome, HP e elemento), inclusive um método `default`.
- [x] **Composição e Agregação**:
  - `Heroi` cria e mantém seu próprio `Inventario` (composição).
  - `Inventario` agrega objetos `Item` criados fora dele (agregação).
  - `Jogo` cria os guardiões dos andares e a `Batalha` atual (composição).
- [x] **Persistência de Dados**: `GerenciadorPersistencia` salva e carrega checkpoints em arquivo de texto (`save_jogo.txt`), validando o formato de cada linha na leitura.
- [x] **Tratamento de Exceções**: `ManaInsuficienteException`, `ItemNaoEncontradoException` e `PersistenciaException` (todas verificadas), tratadas na interface sem encerrar o jogo; uma ação recusada não faz o turno avançar.
- [x] **Interface Gráfica (Swing + AWT)**: telas em `src/telas/` com `CardLayout`, paleta temática (`TemaFantasia`), cartões de classe, barras de vida e mana, retorno visual após cada ação e diálogos modais para inventário, eventos, fim de jogo, guia e sobre.
- [x] **Testes Automatizados**: `testes/TesteIntegracao.java`, independente da interface, com 53 verificações em 8 grupos (atributos, polimorfismo, multiplicadores elementais, Catalisador, subida de nível, exceções, inventário e persistência).

---

## 🎮 Classes e Atributos Iniciais

| Classe | Ícone | HP Base | Mana Base | Ataque | Defesa | Custo Habilidade | Habilidade Elemental | Passiva |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| **Espadachim** | ⚔️ | **115** | **40** | **22** | **10** | **15 MP** | *Lâmina Elemental* (ATQ + 16, × multiplicador elemental) | *Postura Inabalável* (16% de esquiva) |
| **Arcanista** | 🔮 | **90** | **75** | **25** | **5** | **20 MP** | *Explosão Arcana* (ATQ + 22, × multiplicador elemental × 1,15) | *Mestria Elemental* (+15% no multiplicador da habilidade) |
| **Atirador** | 🏹 | **95** | **55** | **24** | **7** | **14 MP** | *Disparo Perfurante* (145% do ATQ + 8, × multiplicador elemental) | *Olho de Falcão* (28% de crítico) |
| **Catalisador**| 📖 | **85** | **80** | **23** | **5** | **22 MP** | *Ressonância Suprema* (ATQ + 25, × multiplicador elemental) | *Infusão Arcana* (ataque básico aplica o multiplicador elemental e escala com crítico) |

Sem passiva específica, o herói tem 15% de chance de crítico (dano × 1,5) e 8% de esquiva.

### 📈 Bônus por Subida de Nível
A cada guardião derrotado (inclusive o chefe final), o herói sobe de nível e recebe o bônus definido em `Heroi`:
- **+15 HP Máximo**
- **+8 Mana Máxima**
- **+4 Ataque**
- **+2 Defesa**
- Recupera **+35 HP** e **+25 Mana** (sem ultrapassar os máximos) e tem a queimadura removida.

Vencer o Mímico Voraz não concede nível, mas leva o herói ao andar seguinte.

---

## ⚡ Sistema de Reações Elementais

As habilidades especiais (e o ataque básico do Catalisador) aplicam um multiplicador de acordo com o elemento do atacante e o do defensor, definido em `Elemento.getMultiplicadorContra`:

| Atacante \ Defensor | PYRO | HYDRO | ANEMO | ELECTRO |
| :--- | :---: | :---: | :---: | :---: |
| **PYRO** | 0,65 | 0,8 | 1,5 | 1,4 |
| **HYDRO** | 1,6 | 0,65 | 1,0 | 1,3 |
| **ANEMO** | 1,35 | 1,35 | 0,65 | 1,35 |
| **ELECTRO** | 1,3 | 1,6 | 1,0 | 0,65 |

- **Vantagem Elemental**: as combinações mais fortes são HYDRO contra PYRO (*Vaporização*) e ELECTRO contra HYDRO (*Eletrocussão*), ambas × 1,6; ANEMO causa × 1,35 contra qualquer elemento diferente (*Dispersão*).
- **Resistência**: ataques contra o mesmo elemento causam × 0,65 (35% a menos de dano).
- **Desvantagem**: PYRO contra HYDRO causa × 0,8 (*Chamas Apagadas*).
- **Efeito Queimadura**: o guardião PYRO (*Slime Gigante Pyro*) tem 40% de chance por turno de usar o *Lança-Chamas*, que aplica queimadura de 6 de dano por turno durante 2 turnos. Itens de cura e a subida de nível removem a queimadura.

---

## 🗼 Andares e Eventos

| Andar | Guardião | Elemento | HP | Ataque | Defesa |
| :---: | :--- | :---: | :---: | :---: | :---: |
| 1 | Slime Gigante Pyro | PYRO | 85 | 19 | 6 |
| 2 | Espectro das Marés | HYDRO | 120 | 26 | 8 |
| 3 | Guardião dos Ventos | ANEMO | 155 | 33 | 10 |
| 4 | Arconte da Tempestade | ELECTRO | 215 | 42 | 14 |

Após vencer o guardião dos andares 1 a 3, ocorre um evento aleatório: **Baú Mímico** (30%, combate extra contra o Mímico Voraz, que recompensa com o Elixir dos Arcontes), **Fonte Mística Sagrada** (25%, +50 HP e +35 Mana), **Altar dos Titãs** (20%, +5 Ataque e +2 Defesa) ou **Baú de Tesouro** (25%, um item de cura ou mana).

---

## 📦 Estrutura de Diretórios

```text
rpg-poo-java/
├── src/                          # Código-fonte Java
│   ├── modelo/                   # Entidade, Heroi, subclasses de herói, Monstro e Elemento
│   ├── combate/                  # Combativel, Batalha e Jogo
│   ├── itens/                    # Item, TipoItem e Inventario
│   ├── persistencia/             # GerenciadorPersistencia (checkpoints em disco)
│   ├── excecoes/                 # Exceções verificadas do domínio
│   └── telas/                    # Interface gráfica em Java Swing (TelaJogo, diálogos e TemaFantasia)
├── testes/                       # TesteIntegracao (suíte de testes automatizados)
├── docs/
│   ├── prints/                   # Capturas de tela usadas no manual
│   ├── uml/                      # Diagrama de Classes UML (DiagramaUML.pdf)
│   ├── manual/                   # Versão anterior do manual (2.0)
│   ├── relatorio/                # Relatório acadêmico ABNT
│   └── screenshots/              # Reservado
├── assets/imagens/               # Reservado
├── MANUAL_DO_USUARIO.md          # Manual do usuário (versão atual)
├── README.md                     # Apresentação do projeto e instruções
└── .gitignore                    # Ignora bin/, saves e arquivos de IDE
```

A pasta `bin/` é criada na compilação e não é versionada.

---

## 🚀 Como Compilar e Executar

O projeto não requer dependências externas nem ferramentas de build. Basta ter o **Java SE JDK 11 ou superior** instalado.

### No Linux / macOS:
```bash
# 1. Clonar o repositório e entrar no diretório
git clone https://github.com/IsabelaR0cha/rpg-poo-java.git
cd rpg-poo-java

# 2. Compilar todas as classes com codificação UTF-8
javac -encoding UTF-8 -d bin $(find src -name "*.java")

# 3. Executar o jogo
java -cp bin telas.TelaJogo
```

### No Windows (PowerShell):
```powershell
# 1. Compilar
javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName

# 2. Executar
java -cp bin telas.TelaJogo
```

### Executar os testes automatizados
```bash
javac -encoding UTF-8 -d bin $(find src testes -name "*.java")
java -cp bin testes.TesteIntegracao
```
No Windows, troque o primeiro comando por:
```powershell
javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse -Filter *.java src, testes).FullName
```
A saída esperada termina com `RESULTADO FINAL: 53/53 testes aprovados!`.

---

## 📖 Documentação Adicional
- [Manual do Usuário](MANUAL_DO_USUARIO.md)
- [Diagrama de Classes UML](docs/uml/DiagramaUML.pdf)
- [Relatório Acadêmico ABNT](docs/relatorio/)