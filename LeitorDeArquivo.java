import java.io.*;
import java.util.*;

public class LeitorDeArquivo {
    public static void main(String[] args) {
        String entradaArq = "../sistemas/TESTE-03.txt";
        int quantum = 0;
        List<Processo> processosFIFO = new ArrayList<>();
        List<Processo> processosSJF = new ArrayList<>();
        List<Processo> processosSRT = new ArrayList<>();
        List<Processo> processosRR = new ArrayList<>();

        // Leitura do arquivo
        try (BufferedReader lerArq = new BufferedReader(new FileReader(entradaArq))) {  
            String linha = lerArq.readLine();
            quantum = Integer.parseInt(linha);

            while ((linha = lerArq.readLine()) != null) {
                String[] dados = linha.split(" ");
                int chegada = Integer.parseInt(dados[0]);
                int execucao = Integer.parseInt(dados[1]);
                processosFIFO.add(new Processo(1,chegada, execucao));
                processosSJF.add(new Processo(1,chegada, execucao));
                processosRR.add(new Processo(1,chegada, execucao));
            }
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
            return;
        }

        // Executa o algoritmo FIFO usando a classe Escalonador
        Escalonador escalonador = new Escalonador();
        escalonador.fifo(processosFIFO);
        escalonador.sjf(processosSJF);
        escalonador.srt(processosSRT);
        escalonador.srt(processosRR);
    }
}
