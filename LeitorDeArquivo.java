import java.io.*;
import java.util.*;

public class LeitorDeArquivo {
    public static void main(String[] args) {
        String entradaArq = "../sistemas/TESTE-01.txt";
        int quantum = 0;
        List<Processo> processosFIFO = new ArrayList<>();
        List<Processo> processosSJF = new ArrayList<>();
        List<Processo> processosSRT = new ArrayList<>();
        List<Processo> processosRR = new ArrayList<>();

        try (BufferedReader lerArq = new BufferedReader(new FileReader(entradaArq))) {
            String linha = lerArq.readLine();
            quantum = Integer.parseInt(linha);

            int idProcesso = 1;
            while ((linha = lerArq.readLine()) != null) {
                String[] dados = linha.split(" ");
                int chegada = Integer.parseInt(dados[0]);
                int execucao = Integer.parseInt(dados[1]);
                processosFIFO.add(new Processo(idProcesso++, chegada, execucao));
                processosSJF.add(new Processo(idProcesso++, chegada, execucao));
                processosSRT.add(new Processo(idProcesso++, chegada, execucao));
                processosRR.add(new Processo(idProcesso++, chegada, execucao));
            }
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
            return;
        }

        Escalonador escalonador = new Escalonador();
        escalonador.fifo(new ArrayList<>(processosFIFO));
        escalonador.sjf(new ArrayList<>(processosSJF));
        escalonador.srt(new ArrayList<>(processosSRT));

        if (quantum > 0) {
            escalonador.rr(new ArrayList<>(processosRR), quantum);
        } else {
            System.err.println("Quantum inválido. O valor de quantum deve ser maior que zero.");
        }
    }
}