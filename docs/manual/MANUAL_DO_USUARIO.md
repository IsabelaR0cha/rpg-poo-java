# MANUAL DO USUÁRIO — TORRE DOS ELEMENTOS ⚔️📜

**Disciplina:** Programação Orientada a Objetos — UFU  
**Equipe:** Celina, Isabela Dantas e Isabela Rocha  
**Versão do Sistema:** 2.0 (Swing Edition com Interface de Grimório & Checkpoint por Andar)

---

## 1. Visão Geral do Jogo

A **Torre dos Elementos** é um RPG tático em turnos ambientado em uma torre mística dividida em 4 andares. Cada andar possui um guardião sintonizado a um elemento primordial:
- **Andar 1:** Pyro (Fogo)
- **Andar 2:** Hydro (Água)
- **Andar 3:** Anemo (Vento)
- **Andar 4:** Electro (Trovão)

O objetivo do jogador é escolher sua classe de combate, definir sua afinidade elemental, gerenciar recursos de vida (HP) e energia (Mana), explorar eventos misteriosos entre os andares e derrotar o Guardião do Trovão no topo da torre.

---

## 2. Classes de Heróis e Atributos

Ao iniciar um novo jogo, o jogador pode escolher uma entre as quatro classes disponíveis:

| Classe | Estilo de Combate | HP Base | Mana Base | Ataque Base | Defesa Base | Habilidade Elemental (Custo) | Efeito Passivo |
| :--- | :--- | :---: | :---: | :---: | :---: | :--- | :--- |
| **Espadachim** | Tanque / Equilíbrio | **115** | **40** | **22** | **10** | **Lâmina Elemental (15 MP)**<br>+16 dano base c/ multiplicador elemental | **Postura Inabalável**<br>16% de chance de esquiva/bloqueio total |
| **Arcanista** | Mago / Dano Elemental | **90** | **75** | **25** | **5** | **Explosão Arcana (20 MP)**<br>+22 dano base c/ bônus de maestria (+15%) | **Mestria Elemental**<br>+15% no dano das reações elementais |
| **Atirador** | Precisão / Crítico | **95** | **55** | **24** | **7** | **Disparo Perfurante (14 MP)**<br>145% do ataque + 8 c/ multiplicador | **Olho de Falcão**<br>28% de chance de acerto crítico (150% de dano) |
| **Catalisador**| Conjurador Puro | **85** | **80** | **23** | **5** | **Ressonância Suprema (22 MP)**<br>+25 dano base c/ multiplicador elemental | **Infusão Mágica**<br>Ataques normais causam dano elemental e crítico |

---

## 3. Ciclo e Reações Elementais

As habilidades (e os ataques básicos do Catalisador) aplicam multiplicadores conforme o elemento do atacante contra o elemento do defensor:

- **Pyro:**
  - **x1.5** contra **Anemo** (*Propagação de Chamas*)
  - **x1.4** contra **Electro** (*Sobrecarga Explosiva*)
  - **x0.8** contra **Hydro** (*Chamas Apagadas*)
- **Anemo:**
  - **x1.35** contra qualquer elemento diferente (*Dispersão Elemental*)
- **Electro:**
  - **x1.8** contra **Hydro** (*Eletrocussão Devastadora*)
  - **x1.3** contra **Pyro** (*Sobrecarga*)
- **Hydro:**
  - **x1.6** contra **Pyro** (*Vaporização*)
  - **x1.3** contra **Electro** (*Condução Elétrica*)
- **Mesmo Elemento:**
  - **x0.65** (*Resistência Elemental*, -35% de dano)
- **Neutro:**
  - **x1.0** quando não há interação elemental especial.

### Efeito de Status: Queimadura (Combustão)
Ataques e contra-ataques de inimigos do elemento **PYRO** possuem 35% de chance de aplicar **Queimadura** por 2 turnos. No final de cada turno concluído, o herói sofre 6 de dano passivo até que o efeito expire. Ações inválidas (como tentar usar habilidade sem mana ou item indisponível) não avançam o turno nem aplicam o dano da queimadura.

---

## 4. Progressão e Eventos Entre Andares

Ao derrotar o monstro de um andar:
1. **Subida de Nível:** O herói ganha um nível e recebe permanentemente:
   - **+15 HP Máximo**
   - **+8 Mana Máxima**
   - **+4 Ataque**
   - **+2 Defesa**
   - Recuperação imediata de vida (+30 HP) e mana (+15 MP).
2. **Eventos Especiais:**
   - **Baú do Guardião (30%):** Concede poções alquímicas raras (Poção Grande de Cura ou Elixir Concentrado de Mana).
   - **Mímico Voraz (18%):** O baú é um monstro disfarçado com estatísticas aumentadas, iniciando um combate surpresa! Derrotá-lo rende o poderoso Elixir dos Arcontes.
   - **Fonte Sagrada (28%) ou Altar dos Titãs (24%):** Restaura vida/mana ou aumenta atributos permanentes antes da ascensão ao próximo andar.

---

## 5. Interface Gráfica e Controles

A interface utiliza componentes **Swing** customizados no estilo **Grimório de Fantasia Medieval**:

- **Tela de Criação / Seleção de Personagem:**
  - Campo para digitar o nome do viajante.
  - Cards interativos com as 4 classes e suas estatísticas base.
  - Cards de classes com vitórias ganham **moldura dourada** (`#D4AF37`) e distintivo `★ [Classe] ★`.
  - Seleção de afinidade elemental através de abas coloridas.
  - Painel de prévia em tempo real com atributos, descrição da habilidade elemental e custo de mana.
  - Botão de atalho para **Carregar Checkpoint Salvo**.
- **Tela de Combate (Diário de Batalha):**
  - **Identificação Visual Clara:**
    - Ficha do **Aliado: Viajante (Herói)** com contorno verde escuro reforçado.
    - Ficha do **Inimigo: [Nome do Monstro]** com contorno carmesim reforçado.
  - **Banner do Andar:** Exibe o número do andar atual e reflete a cor do elemento dominante.
  - **Barras Dinâmicas de HP e Mana:** Indicam visualmente e numericamente o estado dos combatentes.
  - **Botões Clássicos de Ação:** `Atacar [A]`, `Habilidade (XX MP) [H]`, `Mochila (X) [M]`, `Salvar`, `Carregar` e `Novo Herói`.
  - **Mochila de Itens:** Abre a lista agrupada de poções com tipo (`[CURA]` ou `[MANA]`) e valor do efeito.
  - **Diário de Combate em Pergaminho:** Log estruturado com prefixos explícitos por ação:
    - `▶ AÇÃO DO HERÓI: ...`
    - `✨ HABILIDADE DO HERÓI: ...`
    - `🧪 MOCHILA DO HERÓI: ...`
    - `◀ REAÇÃO DO INIMIGO: ...`
    - `⭐ TRIUNFO: ... foi subjugado!`
- **Menu Superior & Atalhos de Teclado:**
  - `Ctrl + N`: Novo Jogo (reinicia seleção de herói).
  - `Ctrl + S`: Salvar Checkpoint (grava no início do andar ou mid-battle).
  - `Ctrl + O`: Carregar Checkpoint (restaura o estado do arquivo `save_jogo.txt`).

---

## 6. Sistema de Save e Checkpoint Mid-Battle

- O salvamento preserva o estado completo da expedição: herói, inventário, andar atual e o **HP exato do monstro** durante a batalha através da linha `MONSTRO;<hpAtual>;<hpMax>`.
- Ao carregar um jogo salvo, o combate é retomado com o monstro tendo a vida exata que restava no momento do salvamento (não reinicia com 100% de vida).
- O arquivo gerado é `save_jogo.txt` na raiz da aplicação (ignorado pelo versionador de código conforme `.gitignore`).

---

## 7. Sistema de Conquistas e Platina da Torre

- Ao triunfar sobre o Arconte da Tempestade no 4º andar e zerar a Torre, a vitória daquela classe é salva no arquivo persistente `conquistas_torre.txt`.
- Na tela de criação/seleção de heróis, os heróis campeões são condecorados com uma **borda dourada brilhante** (`#D4AF37`) e o título `★ [Classe] ★`.
- O objetivo definitivo de rejogabilidade é vencer a Torre com as 4 classes (*Espadachim*, *Arcanista*, *Atirador* e *Catalisador*) até platinar o jogo e exibir todos os 4 cards dourados.

---

## 8. Instruções de Execução

### Pré-requisitos
- **Java SE Development Kit (JDK) 11** ou superior instalado.

### Linha de Comando (Linux / macOS)
```bash
# Compilar
javac -encoding UTF-8 -d bin $(find src -name "*.java")

# Executar
java -cp bin telas.TelaJogo
```

### Linha de Comando (Windows PowerShell)
```powershell
# Compilar
javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName

# Executar
java -cp bin telas.TelaJogo
```
