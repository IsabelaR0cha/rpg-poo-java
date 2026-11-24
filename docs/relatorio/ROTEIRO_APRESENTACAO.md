# Roteiro de Apresentação Oral — Torre dos Elementos

**Disciplina:** Programação Orientada a Objetos (POO) — Universidade Federal de Uberlândia (UFU)  
**Projeto:** Torre dos Elementos — RPG em Java SE & Swing  
**Repositório:** github.com/IsabelaR0cha/rpg-poo-java  
**Duração máxima:** 30 minutos (~28 min de fala + 2 min de perguntas)

**Equipe**

| Integrante | Papel | Foco na apresentação |
| :--- | :--- | :--- |
| Isabela Rocha | Arquiteta | Abertura, contexto, UML, organização do repositório |
| Isabela Dantas | Backend | Combate, reações elementais, hierarquias, exceções, persistência |
| Celina | Designer de UI / QA | Swing, tema Grimório, UX, gameplay ao vivo, manual |

---

## 1. Divisão estratégica dos 30 minutos

| Bloco | Tempo | Duração | Conteúdo | Responsável | Slides |
| :---: | :---: | :---: | :--- | :--- | :---: |
| 1 | 0:00 – 6:00 | 6 min | Abertura, contexto do jogo e modelagem UML | Isabela Rocha | 1 a 4 |
| 2 | 6:00 – 16:00 | 10 min | Arquitetura de domínio, regras de combate e conceitos de POO no código | Isabela Dantas | 5 a 10 |
| 3 | 16:00 – 24:00 | 8 min | Interface Swing, UX e demonstração ao vivo | Celina | 11 a 13 |
| 4 | 24:00 – 28:00 | 4 min | Testes automatizados, persistência e conclusão | Equipe (ver divisão abaixo) | 14 a 16 |
| 5 | 28:00 – 30:00 | 2 min | Perguntas da banca / encerramento | Todas | 17 |

**Divisão interna do Bloco 4 (4 min):** Isabela Dantas 1:30 (testes e persistência) → Celina 1:00 (QA e validação) → Isabela Rocha 1:30 (conclusão e agradecimento).

**Marcos de controle (peça a alguém para sinalizar o tempo):** 6:00 passa para Dantas · 16:00 passa para Celina · 24:00 volta para a equipe · 28:00 abre perguntas.

---

## 2. Preparação antes da apresentação (checklist)

- [ ] Compilar e testar o jogo na máquina que será usada na defesa (`javac -encoding UTF-8 -d bin $(find src testes -name "*.java")` e `java -cp bin telas.TelaJogo`).
- [ ] Rodar `TesteIntegracao` antes e deixar a saída com as 53 asserções aprovadas num terminal já aberto (ou em print no slide 14).
- [ ] Deixar um arquivo `save_jogo.txt` de reserva pronto, para o caso de o salvamento ao vivo falhar.
- [ ] Ter abertos, em janelas separadas: slides, jogo, IDE no arquivo `Combativel.java` e `docs/uml/DiagramaUML.pdf`.
- [ ] Ensaiar a demo pelo menos duas vezes. O jogo tem aleatoriedade (crítico, esquiva, eventos entre andares), então a sequência exata pode variar.
- [ ] Combinar quem opera o mouse na demo (Celina) e quem narra o código (Dantas) caso a banca peça para abrir o fonte.

---

## 3. Roteiro fala por fala

> As falas abaixo são um texto-base. Cada integrante deve adaptá-lo com as próprias palavras, mantendo o vocabulário técnico. Tempo estimado de leitura em voz natural: cerca de 130 palavras por minuto.

### BLOCO 1 — Abertura, contexto e UML (0:00 – 6:00) · Isabela Rocha

**Slide 1 — Abertura (0:00 – 0:45)**

> "Boa noite, professor. Nosso projeto se chama **Torre dos Elementos**, um RPG tático em turnos desenvolvido em Java SE, com interface gráfica em Swing, como trabalho final da disciplina de Programação Orientada a Objetos. Eu sou a Isabela Rocha, fui responsável pela modelagem UML, pela arquitetura geral e pela documentação. A Isabela Dantas cuidou do domínio e do backend, e a Celina, da interface, da experiência do usuário e dos testes de sistema. Nos próximos 30 minutos vamos apresentar o problema, as decisões de projeto, uma demonstração ao vivo e as evidências de qualidade."

**Slide 2 — O jogo e o objetivo acadêmico (0:45 – 2:15)**

> "A Torre dos Elementos é um jogo em que o jogador cria um herói e enfrenta os guardiões de uma torre de quatro andares, cada um regido por um elemento: Pyro, Hydro, Anemo e Electro. A mecânica central são as reações elementais, inspiradas em Genshin Impact: o elemento do herói contra o elemento do monstro altera o dano causado. Escolhemos esse domínio porque ele é rico em entidades com comportamento parecido, mas não idêntico, o que justifica naturalmente herança, polimorfismo e interfaces. Todo o código é Java puro, sem nenhuma biblioteca externa, usando apenas `javax.swing` e `java.awt` na interface."

**Slide 3 — Arquitetura em camadas (2:15 – 3:45)**

> "Organizamos o código em pacotes com responsabilidades bem separadas. O pacote `modelo` guarda as entidades; `combate` contém o motor de batalha e o fluxo do jogo; `itens` trata do inventário; `persistencia` grava e lê checkpoints; `excecoes` reúne as exceções de domínio; `telas` concentra toda a interface; e `testes` tem a suíte automatizada. A decisão mais importante aqui foi que a interface depende do domínio, mas o domínio não conhece a interface. Isso nos permitiu testar toda a lógica de combate sem abrir uma janela, e é por isso que a suíte de testes roda sem interface gráfica."

**Slide 4 — Modelagem UML (3:45 – 6:00)**

> "Este é o diagrama de classes. No topo da hierarquia está a interface `Combativel`, que define o contrato mínimo de qualquer combatente: atacar, receber dano, informar se está vivo, nome, pontos de vida e elemento. A classe abstrata `Entidade` implementa essa interface e concentra os atributos comuns: nome, vida, ataque e defesa. Dela herdam duas ramificações: `Monstro`, e `Heroi`, também abstrata, que por sua vez é especializada em quatro classes concretas: `Espadachim`, `Arcanista`, `Atirador` e `Catalisador`. Temos ainda a relação de agregação entre `Heroi` e `Inventario`, que guarda objetos `Item`, e dois tipos enumerados, `Elemento` e `TipoItem`. Agora a Isabela Dantas vai mostrar como essa modelagem se traduz em código e em regras de combate."

*Transição:* olhar para a Isabela Dantas e passar o controle da apresentação.

---

### BLOCO 2 — Domínio, combate e POO no código (6:00 – 16:00) · Isabela Dantas

**Slide 5 — Abstração e hierarquia de classes (6:00 – 8:00)**

> "Obrigada, Isabela. Vou começar pela abstração. `Entidade` é abstrata porque não faz sentido instanciar uma entidade genérica; ela existe para reunir o que é comum a heróis e monstros, e implementa o método `receberDano`, que aplica a redução de defesa uma única vez para todos. `Heroi` também é abstrata e declara um conjunto de métodos abstratos, entre eles `usarHabilidade`, `getNomeHabilidade` e `getCustoManaHabilidade`. Isso obriga cada classe concreta a definir a sua própria habilidade. Os atributos são `protected` ou `private` e o acesso é feito por métodos, o que é o nosso **encapsulamento**: por exemplo, a mana só muda por `gastarMana`, `recuperarMana` e `subirNivel`, nunca por atribuição direta de fora da classe."

**Slide 6 — Polimorfismo e interface (8:00 – 10:00)**

> "O **polimorfismo** aparece de duas formas. Primeiro, a `Batalha` chama `heroi.usarHabilidade(monstro)` sem saber qual é a subclasse do herói: o Espadachim aplica a Lâmina Elemental, o Arcanista a Explosão Arcana com bônus de 15% em reações, o Atirador o Disparo Perfurante e o Catalisador a Ressonância Suprema. Segundo, as assinaturas de `atacar` e `receberDano` vêm da interface `Combativel`, então o motor trabalha com a abstração e não com classes concretas. Isso é o **princípio da inversão de dependência**: o código de alto nível depende de uma interface. E se quisermos adicionar um quinto herói ou um novo tipo de inimigo, basta criar uma classe, sem alterar a `Batalha`."

**Slide 7 — Mostrando no código (10:00 – 11:00)**

> "Aqui vemos um trecho de `Arcanista`. Repare no `@Override` em `usarHabilidade`, no cálculo `ataque + 22` multiplicado pela reação elemental e por 1.15, e na linha que lança `ManaInsuficienteException` quando `gastarMana` retorna falso."

**Slide 8 — Reações elementais (11:00 – 13:00)**

> "O coração do domínio é o enum `Elemento`, que encapsula a regra no método `getMultiplicadorContra`. O ciclo elemental prioriza Pyro contra Anemo com 1.5, Anemo contra qualquer outro diferente com 1.35 de Dispersão, Electro contra Hydro com 1.6 de Eletrocussão e Hydro contra Pyro com 1.6 de Vaporização. Interações de desvantagem ou secundárias também possuem multiplicadores dedicados: Pyro contra Hydro reduz a 0.8 porque as chamas são apagadas, e Hydro contra Electro causa 1.3 de Condução Elétrica. Elementos iguais sofrem resistência, com multiplicador 0.65. Além disso, os monstros Pyro têm 35% de chance de causar **Queimadura**, um efeito contínuo de dois turnos que é processado no fim de cada turno válido. Colocar essa regra num enum evita `if` espalhados pelo código e deixa a tabela num único lugar, fácil de testar."

**Slide 9 — Batalha, Jogo e proteção de turno (13:00 – 14:45)**

> "A classe `Batalha` é a máquina de turnos. Ela calcula esquiva, crítico, dano e reação, executa o contra-ataque do monstro e processa a queimadura. A `Jogo` orquestra os quatro andares e sorteia o evento entre andares: baú mímico, fonte mística, altar dos titãs ou baú de tesouro. Uma decisão de projeto importante está em `executarHabilidade`: a habilidade é tentada **antes** de qualquer efeito de turno. Se a mana for insuficiente, a exceção é lançada e nada muda: o turno não avança, o monstro não contra-ataca e a queimadura não é processada. É a nossa **proteção de turno**, e evita que um erro do jogador custe um turno inteiro."

**Slide 10 — Exceções e composição (14:45 – 16:00)**

> "Criamos três exceções de domínio, todas verificadas, derivadas de `Exception`: `ManaInsuficienteException`, `ItemNaoEncontradoException` e `PersistenciaException`. Optamos por exceções verificadas para que o compilador obrigue quem chama o método a tratar o erro; na interface, cada uma vira uma mensagem amigável em vez de um travamento. Por fim, o `Heroi` mantém um `Inventario` e o `Inventario` mantém uma lista de `Item`; a lista é devolvida como não modificável, então ninguém altera a mochila por fora. Agora passo a palavra para a Celina, que vai mostrar tudo isso funcionando."

*Transição:* "…e que vai mostrar tudo isso funcionando." Celina assume o mouse.

---

### BLOCO 3 — Swing, UX e demonstração ao vivo (16:00 – 24:00) · Celina

**Slide 11 — Interface Swing e tema Grimório (16:00 – 17:30)**

> "Obrigada, Isabela. Eu fiquei com a interface. Toda a tela foi construída em Swing puro, sem editor visual e sem bibliotecas de tema. A classe `TemaFantasia` centraliza as cores, as fontes e a criação de componentes, como os botões de madeira, com a estética de grimório e pergaminho. Cada elemento tem uma cor própria, de modo que o jogador identifica a afinidade rapidamente. A `TelaJogo` tem duas abas: a ficha do herói, onde se cria o personagem, e a torre, onde acontece a batalha. Os diálogos de inventário, de recompensa de baú, de fim de jogo, de guia da torre e de sobre são modais e independentes."

**Slide 12 — Decisões de UX e Game Feel (17:30 – 18:30)**

> "Pensando na experiência do usuário, a criação do personagem mostra uma **prévia dinâmica** de atributos e habilidade assim que a classe ou o elemento muda; o combate conta com **atalhos rápidos de teclado** (tecla A para atacar, H para habilidade e M para mochila); além do log narrativo em pergaminho, o card do monstro possui um **rótulo animado em tempo real** que exibe na hora os acertos críticos, reações de vaporização e esquivas; e a mochila de itens possui badges claros indicando curas que removem queimadura. Vamos agora ao jogo, que vale mais que qualquer slide."

**Slide 13 — Demonstração ao vivo (18:30 – 24:00)** → siga o **Guia de Gameplay** na seção 4. Enquanto Celina opera, Isabela Dantas narra os conceitos de POO e Isabela Rocha cuida do cronômetro.

*Transição final:* "Como vocês viram, o salvamento recuperou exatamente o estado do herói. Isso nos leva à parte de qualidade, que a Isabela Dantas apresenta."

---

### BLOCO 4 — Testes, persistência e conclusão (24:00 – 28:00)

**Slide 14 — Testes automatizados (24:00 – 25:15) · Isabela Dantas**

> "Para garantir que o que mostramos funciona por construção, e não por sorte, criamos o `TesteIntegracao`, uma suíte independente da interface com **53 asserções**. Ela cobre as constantes e os construtores das classes, a herança e o polimorfismo das habilidades, o ciclo e os multiplicadores elementais, o dano crítico, a progressão e a subida de nível, o tratamento das exceções e o controle de turnos, o inventário e o consumo de itens, e a persistência por andar. Como o domínio não depende do Swing, qualquer pessoa roda a suíte pela linha de comando e vê o resultado verde."

**Slide 15 — Persistência (25:15 – 26:00) · Isabela Dantas**

> "A persistência é feita pelo `GerenciadorPersistencia`, que grava em um arquivo texto os registros `ANDAR`, `HEROI` e `ITEM`, separados por ponto e vírgula. Na leitura, cada linha é validada: registro desconhecido, linha incompleta ou item antes do herói geram `PersistenciaException`. Os recursos são abertos com *try-with-resources*, então os arquivos sempre são fechados com segurança. O checkpoint guarda o andar, o herói com todos os seus atributos consolidados e os itens da mochila."

**Slide 16a — QA e validação (26:00 – 27:00) · Celina**

> "Do lado da qualidade, eu validei o sistema com casos de teste manuais: criação de cada uma das quatro classes, uso de habilidade sem mana, uso de item inexistente, salvar e carregar em andares diferentes, derrota e reinício, e os eventos entre andares. Os resultados foram registrados no manual e nas capturas de tela que estão no repositório."

**Slide 16b — Conclusão (27:00 – 28:00) · Isabela Rocha**

> "Para concluir: o projeto cobre classes e objetos, encapsulamento, herança, classes abstratas, interfaces, polimorfismo, agregação, exceções personalizadas, persistência em disco e testes automatizados, tudo em Java puro. Aprendemos principalmente que uma boa modelagem inicial reduz o retrabalho: a interface `Combativel` e as classes abstratas permitiram trabalhar em paralelo, cada uma na sua camada. Como trabalhos futuros, vemos mais classes e monstros, vários inimigos por batalha e a troca do arquivo texto por serialização ou banco de dados. Obrigada pela atenção, estamos à disposição para perguntas."

---

### BLOCO 5 — Perguntas da banca (28:00 – 30:00)

**Quem responde o quê:**

| Tema da pergunta | Quem responde |
| :--- | :--- |
| UML, decisões de arquitetura, repositório, ABNT | Isabela Rocha |
| Regras de combate, herança, polimorfismo, exceções, persistência | Isabela Dantas |
| Interface, UX, testes manuais, manual | Celina |

**Perguntas prováveis e respostas curtas**

1. **Por que `Entidade` e `Heroi` são abstratas e não interfaces?** Porque elas compartilham estado (vida, ataque, defesa, mana) e implementação (`receberDano`, `gastarMana`). Interface só define contrato; classe abstrata permite reuso de código e atributos. A interface `Combativel` fica para o que é comum a qualquer combatente.
2. **Qual a diferença entre agregação e composição no projeto?** O `Inventario` é criado dentro do `Heroi` e a lista de `Item` pertence a ele: na prática, é uma composição forte. Preferimos dizer "agregação/composição" porque os `Item` poderiam, em teoria, existir fora de uma mochila (por exemplo, como recompensa de baú antes de serem adicionados).
3. **Por que exceções verificadas?** Para forçar o tratamento na chamada e transformar o erro em mensagem para o jogador; nenhuma delas representa falha de programação.
4. **Onde está o polimorfismo mais claro?** Em `Batalha.executarHabilidade()`, que chama `usarHabilidade` na referência `Heroi`; a versão executada depende da subclasse em tempo de execução (ligação dinâmica).
5. **Por que arquivo texto e não serialização?** Simplicidade, legibilidade para a banca e controle da validação campo a campo. Serialização é um trabalho futuro.
6. **O que acontece se o save estiver corrompido?** O gerenciador lança `PersistenciaException` com mensagem específica e a interface mostra um aviso; o jogo em andamento não é alterado.
7. **Existe bloqueio de dano?** O jogo implementa **esquiva** (herói 8% base; Espadachim 16%) e **redução por defesa** em `Entidade.receberDano` (a defesa reduz o dano pela metade do seu valor, com mínimo de 2). Não há uma ação de "bloquear" separada; se perguntarem, diga isso com naturalidade.
8. **O sistema é testado?** Sim: 53 asserções automatizadas independentes da interface, mais casos de teste manuais de QA.
9. **Por que o domínio não conhece o Swing?** Baixo acoplamento: permite testar sem interface e trocar a interface sem mexer nas regras.

---

## 4. Guia do gameplay ao vivo (Bloco 3 — até 5:30 de demo, dentro de um total de 8 min)

### Preparação da demo

- Abrir o jogo já na aba **"📜 Ficha do Herói / Criação"**.
- Ter em mente a sequência: Espadachim com 40 de Mana inicial e custo de 15 por habilidade.

### Passo 1 — Criação do personagem com prévia dinâmica (≈1:00)

1. Digitar um nome (ex.: "Viajante").
2. Clicar em uma classe e depois alternar entre elementos (**PYRO, HYDRO, ANEMO, ELECTRO**), mostrando a prévia de atributos e de habilidade mudando em tempo real.
3. **Escolha recomendada:** escolher **Espadachim** com elemento **HYDRO**. O andar 1 é o Slime Gigante Pyro, o que produz **Vaporização (x1.6)** logo de início. O Espadachim começa com 40 de Mana e sua habilidade consome 15 MP: com dois usos (30 MP), sobram apenas 10 MP, facilitando demonstrar a exceção de mana em seguida!
4. Narração (Dantas): "A classe define a hierarquia, o elemento define a afinidade; a prévia vem dos métodos abstratos implementados em cada subclasse."
5. Clicar em **"⚔ Iniciar Aventura na Torre"**.

### Passo 2 — Rodada de combate e animação visual (≈2:00)

1. **Ataque básico** (botão "⚔ Atacar" ou tecla rápida **A**). Apontar no log o dano e, se ocorrer, o crítico ou a esquiva da passiva.
2. **Habilidade com reação** (botão "✨ Habilidade" ou tecla rápida **H**). Mostrar no log o texto **[VAPORIZAÇÃO! Dano x1.6]** e apontar o **rótulo animado em azul** abaixo da barra do monstro (`💧 VAPORIZAÇÃO! (x1.6 DANO)`).
3. Apontar o contra-ataque do monstro e o efeito de **Queimadura** se ocorrer.
4. Narração (Celina): "Vejam que além do log em estilo pergaminho, o combate agora exibe um feedback visual em tempo real no card do monstro!"

### Passo 3 — Ação sem recurso: exceção e proteção de turno (≈1:00)

1. Usar a Habilidade pela segunda vez (gasta mais 15 MP, restando 10 MP).
2. Na terceira rodada, tentar usar a Habilidade com apenas 10 MP restantes.
3. O jogo exibe a mensagem de **energia insuficiente** (origem: `ManaInsuficienteException`). Mostrar que o **número do turno não avançou**, que o monstro **não contra-atacou** e que a **vida do herói não foi penalizada**.
4. Abrir a **Mochila** (tecla **M**), mostrando os badges universais `[+] CURA` e `[*] MANA`, e consumir um Éter de Mana para restaurar a energia.
5. Narração (Dantas): "A habilidade é tentada antes dos efeitos de turno; se falhar, a exceção interrompe tudo e nada é alterado."

### Passo 4 — Vitória do andar, baú e subida de nível (≈1:00)

1. Derrotar o guardião do andar 1.
2. Mostrar o **diálogo de recompensa** e o **aviso de subida de nível** (bônus canônico: +15 HP máx., +8 Mana máx., +4 Ataque, +2 Defesa, centralizados em `Heroi`).
3. O evento entre andares é sorteado: **Baú Mímico (30%)**, **Fonte Mística (25%)**, **Altar dos Titãs (20%)** ou **Baú de Tesouro (25%)**. Se vier o mímico, destacar que o inimigo é do mesmo tipo `Monstro` com a flag `eMimico`.

### Passo 5 — Salvar checkpoint e carregar (≈1:00)

1. Clicar em **"💾 Salvar"** (ou `Ctrl+S`). Mostrar a confirmação do checkpoint no início do andar.
2. Desferir um ataque ou gastar mana para alterar propositalmente o estado atual.
3. Clicar em **"📂 Carregar"** (ou `Ctrl+O`). Mostrar que o estado **retorna exatamente** ao do checkpoint (vida, mana, atributos e inventário consolidados).
4. Narração (Dantas): "O arquivo tem um registro por linha e a leitura valida cada um; um save corrompido lança `PersistenciaException`."

---

## 5. Estrutura completa dos slides

### Slide 1 — Torre dos Elementos: RPG em Java SE & Swing
**Fala:** Isabela Rocha · **Tempo:** 0:45
- Trabalho final de Programação Orientada a Objetos, UFU
- Equipe: Isabela Rocha, Isabela Dantas e Celina
- Repositório: github.com/IsabelaR0cha/rpg-poo-java

### Slide 2 — O jogo e o objetivo acadêmico
**Fala:** Isabela Rocha · **Tempo:** 1:30
- RPG tático em turnos numa torre de 4 andares: Pyro, Hydro, Anemo e Electro
- Reações elementais em cadeia, inspiradas em Genshin Impact
- Java puro, sem bibliotecas externas (`javax.swing` + `java.awt`)
- Objetivo: aplicar todos os pilares de POO num sistema completo

### Slide 3 — Arquitetura em camadas
**Fala:** Isabela Rocha · **Tempo:** 1:30
- `modelo` · `combate` · `itens` · `persistencia` · `excecoes` · `telas` · `testes`
- O domínio não depende da interface (baixo acoplamento)
- Lógica de combate testável sem janela gráfica

### Slide 4 — Modelagem UML (diagrama de classes)
**Fala:** Isabela Rocha · **Tempo:** 2:15
- Interface `Combativel` → classe abstrata `Entidade` → `Heroi` (abstrata) e `Monstro`
- 4 heróis concretos: `Espadachim`, `Arcanista`, `Atirador`, `Catalisador`
- `Heroi` agrega `Inventario`, que contém `Item`
- Enums: `Elemento` e `TipoItem`

### Slide 5 — Abstração e encapsulamento
**Fala:** Isabela Dantas · **Tempo:** 2:00
- `Entidade` e `Heroi` abstratas: reúnem estado e comportamento comuns
- Métodos abstratos em `Heroi`: `usarHabilidade`, `getNomeHabilidade`, `getCustoManaHabilidade`
- Atributos `protected`/`private`; mudanças só por métodos (`gastarMana`, `curar`, `subirNivel`)
- `receberDano` centralizado: defesa reduz o dano, com mínimo de 2

### Slide 6 — Polimorfismo e interface
**Fala:** Isabela Dantas · **Tempo:** 2:00
- Uma chamada `heroi.usarHabilidade(monstro)`, quatro comportamentos
- `Combativel` desacopla `Batalha` das classes concretas
- Extensível: novo herói = nova classe, sem alterar `Batalha`

| Classe | HP Base | Mana Base | ATQ | DEF | Custo Habilidade | Passiva |
| :--- | :-: | :-: | :-: | :-: | :-: | :--- |
| Espadachim | 115 | 40 | 22 | 10 | 15 MP | 16% de Esquiva / Bloqueio |
| Arcanista | 90 | 75 | 25 | 5 | 20 MP | +15% de Dano Elemental |
| Atirador | 95 | 55 | 24 | 7 | 14 MP | 28% de Chance de Crítico |
| Catalisador | 85 | 80 | 23 | 5 | 22 MP | Ataque Básico Elemental |

### Slide 7 — POO no código: `@Override` e exceção
**Fala:** Isabela Dantas · **Tempo:** 1:00
- `@Override` em `usarHabilidade`
- Verificação de mana antes do efeito
- `throw new ManaInsuficienteException(...)`

```java
@Override
public void usarHabilidade(Combativel alvo) throws ManaInsuficienteException {
    if (!gastarMana(getCustoManaHabilidade())) {
        throw new ManaInsuficienteException("[!] Mana insuficiente ...");
    }
    double mult = this.elemento.getMultiplicadorContra(alvo.getElemento()) * 1.15;
    alvo.receberDano((int) ((this.ataque + 22) * mult));
}
```

### Slide 8 — Reações elementais
**Fala:** Isabela Dantas · **Tempo:** 2:00
- Regra encapsulada no enum `Elemento.getMultiplicadorContra`
- Vantagens principais: Pyro $\rightarrow$ Anemo (1.5x), Anemo $\rightarrow$ Todos (1.35x), Electro $\rightarrow$ Hydro (1.6x), Hydro $\rightarrow$ Pyro (1.6x)
- Reações secundárias: Pyro $\rightarrow$ Electro (1.4x), Hydro $\rightarrow$ Electro (1.3x), Electro $\rightarrow$ Pyro (1.3x) e Pyro $\rightarrow$ Hydro (0.8x)
- Mesmo elemento: **x0.65** (Resistência)
- Efeito contínuo de **Queimadura**: 2 turnos, 35% de chance dos monstros Pyro

### Slide 9 — Motor de batalha e proteção de turno
**Fala:** Isabela Dantas · **Tempo:** 1:45
- `Batalha`: turnos, esquiva, crítico (+50% de dano), reação e queimadura
- `Jogo`: 4 andares + eventos (baú mímico, fonte, altar, baú de tesouro)
- Habilidade testada **antes** dos efeitos de turno: exceção não consome turno

### Slide 10 — Exceções personalizadas e composição
**Fala:** Isabela Dantas · **Tempo:** 1:15
- `ManaInsuficienteException`, `ItemNaoEncontradoException`, `PersistenciaException`
- Exceções verificadas: o compilador obriga o tratamento amigável
- `Inventario` devolve lista não modificável (defesa do encapsulamento)

### Slide 11 — Interface Swing e tema Grimório
**Fala:** Celina · **Tempo:** 1:30
- Swing puro: `javax.swing` + `java.awt`, sem editor visual
- `TemaFantasia` centraliza cores, fontes e botões de madeira
- Duas abas: ficha do herói e torre (batalha)
- Diálogos modais: inventário, baú, fim de jogo, guia da torre, sobre

### Slide 12 — Experiência do usuário (UX) e Game Feel
**Fala:** Celina · **Tempo:** 1:00
- Prévia dinâmica de atributos e habilidades na criação
- Atalhos rápidos: **A** (atacar), **H** (habilidade), **M** (mochila)
- Rótulos animados de combate em tempo real (Crítico, Vaporização, Sobrecarga)
- Badges universais na mochila indicando itens que removem queimadura

### Slide 13 — Demonstração ao vivo
**Fala:** Celina (com apoio de Dantas e Rocha) · **Tempo:** 5:30 a 6:00
- Criação $\rightarrow$ combate $\rightarrow$ exceção $\rightarrow$ vitória $\rightarrow$ salvar/carregar

### Slide 14 — Testes automatizados
**Fala:** Isabela Dantas · **Tempo:** 1:15
- `TesteIntegracao`: **53 asserções**, sem interface gráfica
- Cobre herança, polimorfismo, reações, crítico, progressão, exceções, inventário e persistência
- Executável pela linha de comando (`java -cp bin testes.TesteIntegracao`)

### Slide 15 — Persistência de checkpoints
**Fala:** Isabela Dantas · **Tempo:** 0:45
- `GerenciadorPersistencia`: salva e carrega arquivo texto formatado (`save_jogo.txt`)
- Registros `ANDAR`, `HEROI` e `ITEM` validados campo a campo na leitura
- *try-with-resources* e tratamento com `PersistenciaException`

```text
ANDAR;2
HEROI;Viajante;Espadachim;HYDRO;2;96;130;25;48;26;12
ITEM;Poção de Cura;CURA;30;Restaura 30 HP e remove queimadura.
```

### Slide 16 — QA e conclusão
**Fala:** Celina (QA, 1:00) e Isabela Rocha (conclusão, 1:00) · **Tempo:** 2:00
- QA: casos manuais de criação, mana insuficiente, item inexistente, salvar/carregar, derrota e eventos
- Todos os 10 pilares de POO aplicados e comprovados no código
- Aprendizado: modelagem inicial reduz retrabalho e permite desenvolvimento paralelo
- Próximos passos: mais classes, múltiplos inimigos, serialização/banco de dados

### Slide 17 — Perguntas
**Fala:** Todas · **Tempo:** 2:00
- Agradecimentos e encerramento
- Repositório: github.com/IsabelaR0cha/rpg-poo-java

---

## 6. Resumo da distribuição por pessoa

| Integrante | Slides | Tempo total aproximado |
| :--- | :--- | :---: |
| Isabela Rocha | 1 a 4, parte do 16 (conclusão), perguntas de arquitetura | ≈ 7:30 |
| Isabela Dantas | 5 a 10, 14 e 15, perguntas de domínio | ≈ 11:30 |
| Celina | 11 a 13, QA no 16, perguntas de UX | ≈ 9:00 |
| Perguntas | 17 | 2:00 |
