public class Processo {
    int chegada;
    int execucao;
    int espera;
    int tempoRestante;

    public Processo(int chegada, int execucao) {
        this.chegada = chegada;
        this.execucao = execucao;
        this.espera = 0;
        this.tempoRestante = execucao;
    }
}
