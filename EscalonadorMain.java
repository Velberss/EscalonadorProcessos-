
import java.io.*;
import java.util.*;

public class EscalonadorMain {
    public static void main(String[] args) {
        String entradaArq = "../sistemas/TESTE-01.txt";
        int quantum = 0;
        List<Processo> processos = new ArrayList<>();

        // Leitura do arquivo
        try (BufferedReader lerArq = new BufferedReader(new FileReader(entradaArq))) {
            String linha = lerArq.readLine(); // Primeira linha contém o quantum
            if (linha != null) {
                quantum = Integer.parseInt(linha);
            }

            int id = 1; // Identificador único para cada processo
            while ((linha = lerArq.readLine()) != null) {
                String[] dados = linha.split(" ");
                int chegada = Integer.parseInt(dados[0]);
                int execucao = Integer.parseInt(dados[1]);
                processos.add(new Processo(id++, chegada, execucao));
            }
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
            return;
        }

        Escalonador escalonador = new Escalonador();

        // Executar os algoritmos de escalonamento
        System.out.println("--- FIFO ---");
        escalonador.fifo(new ArrayList<>(processos));

        System.out.println("--- SJF ---");
        escalonador.sjf(new ArrayList<>(processos));

        System.out.println("--- Round Robin (Quantum = " + quantum + ") ---");
        escalonador.rr(new ArrayList<>(processos), quantum);

        System.out.println("--- SRT ---");
        escalonador.srt(new ArrayList<>(processos));
    }
}

