import java.util.*;

public class Escalonador {

    // Algoritmo FIFO (First-In-First-Out)
    public String fifo(List<Processo> processos) {
        StringBuilder resultado = new StringBuilder();
        Queue<Processo> fila = new LinkedList<>();
        int clock = 0;
        int totalEspera = 0, totalTurnaround = 0;
        int quantidadeProcessos = processos.size();

        // Ordena os processos pela chegada
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

        resultado.append("FIFO - Média de tempo de resposta: ").append(String.format("%.3f", mediaEspera)).append("\n");
        resultado.append("FIFO - Média de tempo de espera: ").append(String.format("%.3f", mediaEspera)).append("\n");
        resultado.append("FIFO - Média de turnaround: ").append(String.format("%.3f", mediaTurnaround)).append("\n");

        return resultado.toString();
    }

    // Algoritmo SJF (Shortest Job First)
    public String sjf(List<Processo> processos) {
        StringBuilder resultado = new StringBuilder();
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

        resultado.append("SJF - Média de tempo de resposta: ").append(String.format("%.3f", mediaResposta)).append("\n");
        resultado.append("SJF - Média de tempo de espera: ").append(String.format("%.3f", mediaEspera)).append("\n");
        resultado.append("SJF - Média de turnaround: ").append(String.format("%.3f", mediaTurnaround)).append("\n");

        return resultado.toString();
    }

    // Algoritmo Round Robin
    public String rr(List<Processo> processos, int quantum) {
        StringBuilder resultado = new StringBuilder();
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

        resultado.append("RR - Média de tempo de resposta: ").append(String.format("%.3f", mediaResposta)).append("\n");
        resultado.append("RR - Média de tempo de espera: ").append(String.format("%.3f", mediaEspera)).append("\n");
        resultado.append("RR - Média de turnaround: ").append(String.format("%.3f", mediaTurnaround)).append("\n");

        return resultado.toString();
    }

    // Algoritmo Shortest Remaining Time
    public String srt(List<Processo> processos) {
        StringBuilder resultado = new StringBuilder();
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

        resultado.append("SRT - Média de tempo de resposta: ").append(String.format("%.3f", mediaResposta)).append("\n");
        resultado.append("SRT - Média de tempo de espera: ").append(String.format("%.3f", mediaEspera)).append("\n");
        resultado.append("SRT - Média de turnaround: ").append(String.format("%.3f", mediaTurnaround)).append("\n");

        return resultado.toString();
    }
}
