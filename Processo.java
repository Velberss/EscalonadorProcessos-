import java.io.*;
import java.util.*;

class Processo {
    int id;
    int chegada;
    int execucao;
    int tempoRestante;
    int espera;
    boolean respondido;

    public Processo(int id, int chegada, int execucao) {
        this.id = id;
        this.chegada = chegada;
        this.execucao = execucao;
        this.tempoRestante = execucao;
        this.espera = 0;
        this.respondido = false;
    }
}