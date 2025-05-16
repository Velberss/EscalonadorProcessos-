# Escalonador de Processos

## Sobre o Projeto

Este projeto implementa um **escalonador de processos** em Java. Ele simula o comportamento de um sistema operacional ao agendar a execução de múltiplos processos, utilizando dados de entrada fornecidos por arquivos de texto.

A aplicação lê os dados de processos a partir de arquivos (`TESTE-XX.txt`), processa-os de acordo com o algoritmo de escalonamento definido, e gera os resultados em arquivos de saída correspondentes (`TESTE-XX-RESULTADO.txt`).

## Estrutura do Projeto

- `Escalonador.java`: Classe principal que realiza o escalonamento dos processos.
- `LeitorDeArquivo.java`: Responsável por ler os dados dos processos a partir de arquivos de texto.
- `Processo.java`: Classe que representa um processo com suas propriedades (como tempo de chegada, duração, etc).
- `TESTE-XX.txt`: Arquivos de entrada com dados de exemplo para testes.
- `TESTE-XX-RESULTADO.txt`: Resultados gerados pela execução do escalonador.

## Como Executar

1. Compile os arquivos `.java`:
   ```bash
   javac *.java
