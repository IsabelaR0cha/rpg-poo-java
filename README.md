# RPG POO Java

## Apresentação

Este repositório reúne o projeto final da disciplina de Programação Orientada a Objetos. A equipe desenvolverá um RPG simples em Java com interface gráfica Swing. O conceito específico do jogo, incluindo seu universo e suas regras, ainda está em definição.

O projeto está na etapa de organização inicial do repositório e da documentação.

## Objetivo

Aplicar os principais conceitos de Programação Orientada a Objetos no desenvolvimento de um jogo interativo, da modelagem de personagens e itens à integração entre o combate, a persistência e a interface gráfica.

## Tecnologias

| Tecnologia | Uso previsto |
| --- | --- |
| Java | Implementação do jogo, das regras de negócio e da persistência. |
| Java Swing (`javax.swing`) | Construção das telas e interação com o jogador. |
| Git e GitHub | Controle de versões, colaboração e revisão de código. |

A configuração inicial usará Java sem Maven ou Gradle.

## Estrutura do projeto

```text
rpg-poo-java/
├── src/
│   ├── modelo/
│   ├── combate/
│   ├── itens/
│   ├── telas/
│   ├── persistencia/
│   └── excecoes/
├── assets/
│   └── imagens/
├── docs/
│   ├── uml/
│   ├── relatorio/
│   ├── manual/
│   └── screenshots/
├── testes/
├── README.md
└── .gitignore
```

| Diretório | Finalidade | Responsável principal |
| --- | --- | --- |
| `./` | Reunir o README e os arquivos de organização do repositório. | Isabela Rocha |
| `src/` | Reunir o código-fonte Java. | Equipe |
| `src/modelo/` | Organizar as entidades e classes de negócio do jogo. | Isabela Dantas |
| `src/combate/` | Concentrar as regras e o controle das batalhas por turnos. | Equipe |
| `src/itens/` | Organizar os itens e as regras de inventário. | Equipe |
| `src/telas/` | Reunir as telas e os componentes Swing. | Celina |
| `src/persistencia/` | Reunir os mecanismos de gravação e recuperação dos dados do jogo. | Isabela Dantas |
| `src/excecoes/` | Reunir as exceções personalizadas do projeto. | Isabela Dantas |
| `assets/` | Reunir os recursos visuais do jogo. | Equipe |
| `assets/imagens/` | Armazenar imagens usadas na interface e no jogo. | Equipe |
| `docs/` | Centralizar os materiais acadêmicos e a documentação de uso. | Isabela Rocha |
| `docs/uml/` | Armazenar a modelagem e o diagrama UML de classes. | Isabela Rocha |
| `docs/relatorio/` | Reunir o relatório acadêmico e os trechos de código que demonstram os conceitos de POO. | Isabela Rocha |
| `docs/manual/` | Armazenar o manual do usuário. | Celina |
| `docs/screenshots/` | Armazenar as capturas de tela do sistema para a documentação. | Celina |
| `testes/` | Reunir os casos de teste e os registros de validação. | Celina |

Os arquivos vazios `.gitkeep` permitem versionar os diretórios enquanto aguardam seu conteúdo.

## Equipe e responsabilidades

| Integrante | Responsabilidades | Diretórios principais |
| --- | --- | --- |
| Isabela Rocha | Organizar o repositório no GitHub, padronizar a estrutura e a documentação, manter o README, elaborar o diagrama UML e o relatório conforme as normas da ABNT, organizar os exemplos de código e participar da modelagem e da implementação em Java. | Raiz do repositório (especialmente `README.md`), `docs/uml/` e `docs/relatorio/` |
| Isabela Dantas | Implementar entidades e regras de negócio, desenvolver a persistência, tratar exceções, criar exceções personalizadas, apoiar a modelagem orientada a objetos e implementar o código Java em conjunto com Isabela Rocha. | `src/modelo/`, `src/persistencia/` e `src/excecoes/` |
| Celina | Desenvolver a interface Swing, validar funcionalidades e a aplicação dos conceitos de POO, elaborar e executar testes, preparar o manual, capturar telas, revisar a documentação e preparar a demonstração. | `src/telas/`, `testes/`, `docs/manual/` e `docs/screenshots/` |

As três integrantes participarão da implementação, da revisão e da apresentação. O desenvolvimento de `src/combate/` e `src/itens/`, a integração das funcionalidades, a revisão de código, os testes de integração e a preparação da apresentação serão atividades compartilhadas. A responsável principal coordena a atividade e as demais integrantes podem contribuir em qualquer diretório.

## Requisitos funcionais previstos

O escopo definitivo depende da escolha do tema do jogo. A proposta inicial prevê as seguintes funcionalidades.

- Criação ou seleção de personagens.
- Diferentes classes de personagens.
- Monstros e inimigos.
- Batalhas por turnos.
- Inventário de itens.
- Persistência dos dados do jogo.
- Interface gráfica interativa.

## Conceitos de POO

A checklist acompanha os conceitos e os requisitos técnicos da disciplina.

- [ ] Classes e objetos.
- [ ] Encapsulamento.
- [ ] Herança.
- [ ] Polimorfismo.
- [ ] Classes abstratas.
- [ ] Interfaces.
- [ ] Composição e agregação.
- [ ] Persistência de dados em arquivos ou banco de dados.
- [ ] Tratamento de exceções, incluindo exceções personalizadas.
- [ ] Interface gráfica interativa com Java Swing.

## Documentação

| Material | Local previsto |
| --- | --- |
| Modelagem conceitual e diagrama UML de classes | [docs/uml/](docs/uml/) |
| Relatório acadêmico | [docs/relatorio/](docs/relatorio/) |
| Manual do usuário | [docs/manual/](docs/manual/) |
| Capturas de tela do sistema | [docs/screenshots/](docs/screenshots/) |

- [ ] Elaborar o relatório acadêmico conforme as normas da ABNT, com capa, introdução e conclusão.
- [ ] Incluir a modelagem conceitual e o diagrama UML de classes.
- [ ] Demonstrar os conceitos de POO com trechos do código.
- [ ] Documentar as bibliotecas utilizadas.
- [ ] Elaborar o manual do usuário com capturas de tela.
- [ ] Incluir o link do repositório no relatório. [Repositório no GitHub](https://github.com/IsabelaR0cha/rpg-poo-java)

## Organização do desenvolvimento

1. Atualizar a branch `main` antes de iniciar uma tarefa.
2. Criar uma branch para cada funcionalidade ou tarefa.
3. Implementar e testar as alterações.
4. Criar commits com mensagens descritivas.
5. Abrir um Pull Request para revisão.
6. Integrar as alterações à branch `main` após a revisão.

Exemplos de nomes de branches para tarefas futuras.

- `feature/sistema-combate`
- `feature/interface-inicial`
- `docs/diagrama-uml`
- `fix/correcao-inventario`

## Execução

As instruções de compilação e execução serão incluídas após a implementação inicial, de acordo com o ambiente Java escolhido pela equipe.

## Apresentação do projeto

A apresentação é obrigatória e tem duração máxima de 30 minutos.

- [ ] Explicar o projeto.
- [ ] Executar o programa durante a apresentação.
- [ ] Demonstrar o funcionamento do jogo.
