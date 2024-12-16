import java.util.*;

class Escalonador {

    // Algoritmo FIFO
    public void fifo(List<Processo> processos) {

        Queue<Processo> fila = new LinkedList<>();
        int clock = 0;
        int totalEspera = 0, totalTurnaround = 0;
        int quantidadeProcessos = processos.size();

        processos.sort(Comparator.comparingInt(p -> p.chegada));

        while (!processos.isEmpty() || !fila.isEmpty()) {
            Iterator<Processo> it = processos.iterator();
            while (it.hasNext()) {
                Processo p = it.next();
                if (p.chegada <= clock) {
                    fila.add(p);
                    it.remove();
                }
            }

            if (!fila.isEmpty()) {
                Processo atual = fila.poll();
                clock += atual.execucao;
                totalEspera += clock - atual.chegada - atual.execucao;
                totalTurnaround += clock - atual.chegada;
            } else {
                clock++;
            }
        }

        float mediaEspera = (float) totalEspera / quantidadeProcessos;
        float mediaTurnaround = (float) totalTurnaround / quantidadeProcessos;
  
        System.out.printf("FIFO - Média de tempo de resposta: %.3f\n", mediaEspera);
        System.out.printf("FIFO - Média de tempo de espera: %.3f\n", mediaEspera);
        System.out.printf("FIFO - Média de turnaround: %.3f\n", mediaTurnaround);
    }

    // Algoritmo SJF
    public void sjf(List<Processo> processos) {
        if (processos.isEmpty()) {
            System.out.println("Nenhum processo para escalonar com SJF.");
            return;
        }

        int clock = 0;
        int totalEspera = 0, totalTurnaround = 0, totalResposta = 0;
        int quantidadeProcessos = processos.size();

        processos.sort(Comparator.comparingInt(p -> p.chegada));
        List<Processo> filaProntos = new ArrayList<>();

        while (!processos.isEmpty() || !filaProntos.isEmpty()) {
            Iterator<Processo> it = processos.iterator();
            while (it.hasNext()) {
                Processo p = it.next();
                if (p.chegada <= clock) {
                    filaProntos.add(p);
                    it.remove();
                }
            }

            if (!filaProntos.isEmpty()) {
                filaProntos.sort(Comparator.comparingInt(p -> p.execucao));
                Processo atual = filaProntos.remove(0);

                if (atual.espera == 0) {
                    atual.espera = clock - atual.chegada;
                    totalResposta += atual.espera;
                }

                clock += atual.execucao;
                totalEspera += clock - atual.chegada - atual.execucao;
                totalTurnaround += clock - atual.chegada;
            } else {
                clock++;
            }
        }

        float mediaEspera = (float) totalEspera / quantidadeProcessos;
        float mediaTurnaround = (float) totalTurnaround / quantidadeProcessos;
        float mediaResposta = (float) totalResposta / quantidadeProcessos;

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

        List<Processo> processosRestantes = new ArrayList<>(processos);
        processos.sort(Comparator.comparingInt(p -> p.chegada));
        Map<Integer, Integer> tempoRespostaMap = new HashMap<>();

        while (!processosRestantes.isEmpty() || !fila.isEmpty()) {
            Iterator<Processo> it = processosRestantes.iterator();
            while (it.hasNext()) {
                Processo p = it.next();
                if (p.chegada <= clock) {
                    fila.add(p);
                    it.remove();
                }
            }

            if (!fila.isEmpty()) {
                Processo atual = fila.poll();
                if (!tempoRespostaMap.containsKey(atual.id)) {
                    tempoRespostaMap.put(atual.id, clock - atual.chegada);
                }

                int tempoProcessado = Math.min(quantum, atual.tempoRestante);
                atual.tempoRestante -= tempoProcessado;
                clock += tempoProcessado;

                for (Processo p : fila) {
                    p.espera += tempoProcessado;
                }

                if (atual.tempoRestante > 0) {
                    fila.add(atual);
                } else {
                    totalTurnaround += clock - atual.chegada;
                    totalEspera += atual.espera;
                    totalResposta += tempoRespostaMap.get(atual.id);
                }
            } else {
                clock++;
            }
        }

        float mediaEspera = (float) totalEspera / quantidadeProcessos;
        float mediaTurnaround = (float) totalTurnaround / quantidadeProcessos;
        float mediaResposta = (float) totalResposta / quantidadeProcessos;

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

            for (Processo p : processos) {
                if (p.chegada <= clock) {
                    disponiveis.add(p);
                }
            }

            if (!disponiveis.isEmpty()) {
                disponiveis.sort(Comparator.comparingInt(p -> p.tempoRestante));
                Processo atual = disponiveis.get(0);

                if (!atual.respondido) {
                    totalResposta += clock - atual.chegada;
                    atual.respondido = true;
                }

                atual.tempoRestante--;
                clock++;

                if (atual.tempoRestante == 0) {
                    processos.remove(atual);
                    totalEspera += clock - atual.chegada - atual.execucao;
                    totalTurnaround += clock - atual.chegada;
                }
            } else {
                clock++;
            }
        }

        float mediaEspera = (float) totalEspera / quantidadeProcessos;
        float mediaTurnaround = (float) totalTurnaround / quantidadeProcessos;
        float mediaResposta = (float) totalResposta / quantidadeProcessos;

        System.out.println();
        System.out.printf("SRT - Média de tempo de resposta: %.3f\n", mediaResposta);
        System.out.printf("SRT - Média de tempo de espera: %.3f\n", mediaEspera);
        System.out.printf("SRT - Média de turnaround: %.3f\n", mediaTurnaround);
        
    }
}
