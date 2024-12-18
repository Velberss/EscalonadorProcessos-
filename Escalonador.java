import java.util.*;

public class Escalonador {

    // Algoritmo FIFO (First-In-First-Out)
    public void fifo(List<Processo> processos) {
        // Fila de processos, simulando a execução de processos
        Queue<Processo> fila = new LinkedList<>();
        int clock = 0; // Contador de tempo
        int totalEspera = 0, totalTurnaround = 0; // Variáveis para cálculos de tempo
        int quantidadeProcessos = processos.size(); // Número total de processos

        // Ordena os processos pela chegada
        processos.sort(Comparator.comparingInt(p -> p.chegada));

        // Enquanto houver processos ou processos na fila
        while (!processos.isEmpty() || !fila.isEmpty()) {
            Iterator<Processo> it = processos.iterator();
            while (it.hasNext()) {
                Processo p = it.next();
                // Adiciona à fila os processos que chegaram até o tempo atual
                if (p.chegada <= clock) {
                    fila.add(p);
                    it.remove(); // Remove o processo da lista de processos restantes
                }
            }

            // Executa o primeiro processo da fila (FIFO)
            if (!fila.isEmpty()) {
                Processo atual = fila.poll(); // Retira o processo da fila
                clock += atual.execucao; // Atualiza o relógio com o tempo de execução do processo
                totalEspera += clock - atual.chegada - atual.execucao; // Calcula o tempo de espera
                totalTurnaround += clock - atual.chegada; // Calcula o tempo de turnaround
            } else {
                clock++; // Se não há processos para processar, incrementa o relógio (idle)
            }
        }

        // Calcula as médias dos tempos
        float mediaEspera = (float) totalEspera / quantidadeProcessos;
        float mediaTurnaround = (float) totalTurnaround / quantidadeProcessos;

        // Exibe os resultados de FIFO
        System.out.printf("FIFO - Média de tempo de resposta: %.3f\n", mediaEspera);
        System.out.printf("FIFO - Média de tempo de espera: %.3f\n", mediaEspera);
        System.out.printf("FIFO - Média de turnaround: %.3f\n", mediaTurnaround);
    }

    // Algoritmo SJF (Shortest Job First)
    public void sjf(List<Processo> processos) {
        if (processos.isEmpty()) {
            System.out.println("Nenhum processo para escalonar com SJF.");
            return;
        }

        int clock = 0;
        int totalEspera = 0, totalTurnaround = 0, totalResposta = 0; // Variáveis para cálculos
        int quantidadeProcessos = processos.size();

        // Ordena os processos pela chegada
        processos.sort(Comparator.comparingInt(p -> p.chegada));
        List<Processo> filaProntos = new ArrayList<>();

        // Enquanto houver processos ou fila de processos prontos
        while (!processos.isEmpty() || !filaProntos.isEmpty()) {
            Iterator<Processo> it = processos.iterator();
            while (it.hasNext()) {
                Processo p = it.next();
                // Adiciona processos à fila se já chegaram
                if (p.chegada <= clock) {
                    filaProntos.add(p);
                    it.remove();
                }
            }

            // Se houver processos prontos para execução, escolhe o com o menor tempo de
            // execução
            if (!filaProntos.isEmpty()) {
                filaProntos.sort(Comparator.comparingInt(p -> p.execucao)); // Ordena pela execução
                Processo atual = filaProntos.remove(0); // Retira o processo de menor execução

                if (atual.espera == 0) {
                    atual.espera = clock - atual.chegada; // Calcula o tempo de espera
                    totalResposta += atual.espera;
                }

                clock += atual.execucao; // Atualiza o relógio com o tempo de execução
                totalEspera += clock - atual.chegada - atual.execucao; // Calcula o tempo de espera
                totalTurnaround += clock - atual.chegada; // Calcula o tempo de turnaround
            } else {
                clock++; // Se não há processos prontos, incrementa o relógio
            }
        }

        // Calcula as médias
        float mediaEspera = (float) totalEspera / quantidadeProcessos;
        float mediaTurnaround = (float) totalTurnaround / quantidadeProcessos;
        float mediaResposta = (float) totalResposta / quantidadeProcessos;

        // Exibe os resultados de SJF
        System.out.println();
        System.out.printf("SJF - Média de tempo de resposta: %.3f\n", mediaResposta);
        System.out.printf("SJF - Média de tempo de espera: %.3f\n", mediaEspera);
        System.out.printf("SJF - Média de tempo de turnaround: %.3f\n", mediaTurnaround);
    }

    // Algoritmo Round Robin
    public void rr(List<Processo> processos, int quantum) {
        Queue<Processo> fila = new LinkedList<>();
        int clock = 0;
        int totalEspera = 0, totalTurnaround = 0, totalResposta = 0;
        int quantidadeProcessos = processos.size();

        // Organiza os processos pela chegada
        List<Processo> processosRestantes = new ArrayList<>(processos);
        processos.sort(Comparator.comparingInt(p -> p.chegada));
        Map<Integer, Integer> tempoRespostaMap = new HashMap<>();

        // Enquanto houver processos ou fila de processos
        while (!processosRestantes.isEmpty() || !fila.isEmpty()) {
            Iterator<Processo> it = processosRestantes.iterator();
            while (it.hasNext()) {
                Processo p = it.next();
                // Adiciona processos à fila de acordo com o tempo de chegada
                if (p.chegada <= clock) {
                    fila.add(p);
                    it.remove(); // Remove o processo da lista
                }
            }

            // Se houver processos na fila, começa a execução
            if (!fila.isEmpty()) {
                Processo atual = fila.poll();

                // Calcula o tempo de resposta se for a primeira execução
                if (!tempoRespostaMap.containsKey(atual.id)) {
                    tempoRespostaMap.put(atual.id, clock - atual.chegada);
                }

                // Processa o processo pelo quantum
                int tempoProcessado = Math.min(quantum, atual.tempoRestante);
                atual.tempoRestante -= tempoProcessado;
                clock += tempoProcessado; // Atualiza o relógio

                // Atualiza o tempo de espera dos outros processos na fila
                for (Processo p : fila) {
                    p.espera += tempoProcessado;
                }

                // Se o processo ainda tem tempo restante, retorna à fila
                if (atual.tempoRestante > 0) {
                    fila.add(atual);
                } else {
                    // Caso o processo termine, calcula o tempo de turnaround e espera
                    totalTurnaround += clock - atual.chegada;
                    totalEspera += atual.espera;
                    totalResposta += tempoRespostaMap.get(atual.id);
                }
            } else {
                clock++; // Se não há processos para processar, o tempo continua
            }
        }

        // Calcula as médias dos tempos
        float mediaEspera = (float) totalEspera / quantidadeProcessos;
        float mediaTurnaround = (float) totalTurnaround / quantidadeProcessos;
        float mediaResposta = (float) totalResposta / quantidadeProcessos;

        // Exibe os resultados de RR
        System.out.println();
        System.out.printf("RR - Média de tempo de espera: %.3f\n", mediaEspera);
        System.out.printf("RR - Média de tempo de resposta: %.3f\n", mediaResposta);
        System.out.printf("RR - Média de tempo de turnaround: %.3f\n", mediaTurnaround);
    }

    // Algoritmo Shortest Remaining Time
    public void srt(List<Processo> processos) {
        int clock = 0;
        int totalEspera = 0, totalTurnaround = 0, totalResposta = 0;
        int quantidadeProcessos = processos.size();

        processos.sort(Comparator.comparingInt(p -> p.chegada));

        while (!processos.isEmpty()) {
            List<Processo> disponiveis = new ArrayList<>();

            // Verifica os processos que estão disponíveis até o tempo atual
            for (Processo p : processos) {
                if (p.chegada <= clock) {
                    disponiveis.add(p);
                }
            }

            // Se houver processos disponíveis, escolhe o de menor tempo restante
            if (!disponiveis.isEmpty()) {
                disponiveis.sort(Comparator.comparingInt(p -> p.tempoRestante));
                Processo atual = disponiveis.get(0);

                if (!atual.respondido) {
                    totalResposta += clock - atual.chegada;
                    atual.respondido = true;
                }

                atual.tempoRestante--; // Processa o processo
                clock++;

                // Se o processo terminar, remove e calcula os tempos
                if (atual.tempoRestante == 0) {
                    processos.remove(atual);
                    totalEspera += clock - atual.chegada - atual.execucao;
                    totalTurnaround += clock - atual.chegada;
                }
            } else {
                clock++; // Se não há processos disponíveis, o tempo continua
            }
        }

        // Calcula as médias
        float mediaEspera = (float) totalEspera / quantidadeProcessos;
        float mediaTurnaround = (float) totalTurnaround / quantidadeProcessos;
        float mediaResposta = (float) totalResposta / quantidadeProcessos;

        // Exibe os resultados de SRT
        System.out.println();
        System.out.printf("SRT - Média de tempo de resposta: %.3f\n", mediaResposta);
        System.out.printf("SRT - Média de tempo de espera: %.3f\n", mediaEspera);
        System.out.printf("SRT - Média de turnaround: %.3f\n", mediaTurnaround);
    }
}
