import java.util.*;

class Processo {
    int id;
    int chegada;
    int execucao;
    int tempoRestante;
    int espera;

    public Processo(int id, int chegada, int execucao) {
        this.id = id;
        this.chegada = chegada;
        this.execucao = execucao;
        this.tempoRestante = execucao;
        this.espera = 0;
    }
}

public class Escalonador {

    // Algoritmo FIFO
    public void fifo(List<Processo> processos) {
        Queue<Processo> fila = new LinkedList<>();
        int clock = 0;
        int totalEspera = 0, totalTurnaround = 0;
        int quantidadeProcessos = processos.size();

        // Ordenar os processos pela ordem de chegada
        processos.sort(Comparator.comparingInt(p -> p.chegada));

        while (!processos.isEmpty() || !fila.isEmpty()) {
            // Adicionar processos que chegaram ao clock atual à fila
            Iterator<Processo> it = processos.iterator();
            while (it.hasNext()) {
                Processo p = it.next();
                if (p.chegada <= clock) {
                    fila.add(p);
                    it.remove();
                }
            }
            // Executar o processo na frente da fila
            if (!fila.isEmpty()) {
                Processo atual = fila.poll();
                clock += atual.execucao;
                totalEspera += clock - atual.chegada - atual.execucao;
                totalTurnaround += clock - atual.chegada;
            } else {
                clock++;
            }
        }

        // Calcula as médias
        float mediaEspera = (float) totalEspera / quantidadeProcessos;
        float mediaTurnaround = (float) totalTurnaround / quantidadeProcessos;

        // Exibir os resultados
        System.out.printf("FIFO - Média de tempo de espera: %.3f\n", mediaEspera);
        System.out.printf("FIFO - Média de turnaround: %.3f\n", mediaTurnaround);
    }

    // Algoritmo SJF
    public void sjf(List<Processo> processos) {
        int clock = 0;
        int totalEspera = 0, totalTurnaround = 0;
        int quantidadeProcessos = processos.size();

        // Ordenar por tempo de chegada inicialmente
        processos.sort(Comparator.comparingInt(p -> p.chegada));

        while (!processos.isEmpty()) {
            // Obter os processos disponíveis
            List<Processo> disponiveis = new ArrayList<>();
            for (Processo p : processos) {
                if (p.chegada <= clock) {
                    disponiveis.add(p);
                }
            }

            if (!disponiveis.isEmpty()) {
                // Escolher o processo com menor tempo de execução
                disponiveis.sort(Comparator.comparingInt(p -> p.execucao));
                Processo atual = disponiveis.get(0);
                processos.remove(atual);

                clock += atual.execucao;
                totalEspera += clock - atual.chegada - atual.execucao;
                totalTurnaround += clock - atual.chegada;
            } else {
                clock++;
            }
        }

        float mediaEspera = (float) totalEspera / quantidadeProcessos;
        float mediaTurnaround = (float) totalTurnaround / quantidadeProcessos;

        System.out.printf("SJF - Média de tempo de espera: %.3f\n", mediaEspera);
        System.out.printf("SJF - Média de turnaround: %.3f\n", mediaTurnaround);
    }

    // Algoritmo Round Robin
    public void rr(List<Processo> processos, int quantum) {
        Queue<Processo> fila = new LinkedList<>();
        int clock = 0;
        int totalEspera = 0, totalTurnaround = 0;
        int quantidadeProcessos = processos.size();

        processos.sort(Comparator.comparingInt(p -> p.chegada));

        while (!processos.isEmpty() || !fila.isEmpty()) {
            // Adicionar processos que chegaram ao clock atual à fila
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
                int tempoProcessado = Math.min(quantum, atual.tempoRestante);
                atual.tempoRestante -= tempoProcessado;
                clock += tempoProcessado;

                // Incrementar tempo de espera para os demais na fila
                for (Processo p : fila) {
                    p.espera += tempoProcessado;
                }

                if (atual.tempoRestante > 0) {
                    fila.add(atual);
                } else {
                    totalEspera += clock - atual.chegada - atual.execucao;
                    totalTurnaround += clock - atual.chegada;
                }
            } else {
                clock++;
            }
        }

        float mediaEspera = (float) totalEspera / quantidadeProcessos;
        float mediaTurnaround = (float) totalTurnaround / quantidadeProcessos;

        System.out.printf("RR - Média de tempo de espera: %.3f\n", mediaEspera);
        System.out.printf("RR - Média de turnaround: %.3f\n", mediaTurnaround);
    }

    // Algoritmo Shortest Remaining Time (SRT)
    public void srt(List<Processo> processos) {
        int clock = 0;
        int totalEspera = 0, totalTurnaround = 0;
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

        System.out.printf("SRT - Média de tempo de espera: %.3f\n", mediaEspera);
        System.out.printf("SRT - Média de turnaround: %.3f\n", mediaTurnaround);
    }
}
