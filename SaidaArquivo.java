import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class SaidaArquivo {

    public void ResultadoTXT() throws IOException {
        FileWriter arq = new FileWriter("../sistemas/TESTERESULTADO-03.txt");
        PrintWriter gravarArq = new PrintWriter(arq);
    }
}
