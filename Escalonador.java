import java.util.*;
public class Escalonador {
    
    // Algoritmo FIFO
    public void fifo(List<Processo> processos) {
        System.out.println("Executando FIFO...");
        Queue<Processo> fila = new LinkedList<>();
        int clock = 0;
        int totalEspera = 0, totalRetorno = 0, totalTurnaround = 0;
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
                Processo atual = fila.peek();

                // Processa o tempo restante
                atual.tempoRestante--;
                clock++;

                // Incrementar tempo de espera para os demais na fila
                for (Processo p : fila) {
                    if (p != atual) {
                        p.espera++;
                    }
                }

                // Se o processo atual terminou
                if (atual.tempoRestante == 0) {
                    fila.poll();
                    int turnaround = atual.execucao + atual.espera;
                    totalEspera += atual.espera;
                    totalTurnaround += turnaround;
                    totalRetorno += atual.espera; // Tempo de retorno no FIFO é igual ao tempo de espera
                }
            } else {
                // Avançar o relógio se não houver processos disponíveis
                clock++;
            }
        }

        // Calcula as médias
        float mediaEspera = (float) totalEspera / quantidadeProcessos;
        float mediaTurnaround = (float) totalTurnaround / quantidadeProcessos;
        float mediaRetorno = (float) totalRetorno / quantidadeProcessos;

        // Exibir os resultados
        System.out.printf("Média de tempo de espera: %.2f\n", mediaEspera);
        System.out.printf("Média de tempo de retorno: %.2f\n", mediaRetorno);
        System.out.printf("Média de turnaround: %.2f\n", mediaTurnaround);
    }

    // Algoritmo SJF
    public void sjf(List<Integer> chegada, List<Integer> execucao) {
        System.out.println("Executando SJF...");
        // Lógica para SJF
    }

    // Algoritmo Round Robin
    public void roundRobin(List<Integer> chegada, List<Integer> execucao, int quantum) {
        System.out.println("Executando Round Robin...");
        // Lógica para Round Robin
    }

    // Algoritmo de Prioridade
    public void prioridade(List<Integer> chegada, List<Integer> execucao, List<Integer> prioridade) {
        System.out.println("Executando Escalonamento por Prioridade...");
        // Lógica para Prioridade
    }
}