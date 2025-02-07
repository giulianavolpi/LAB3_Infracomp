import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BuzonOrdenes buzon = new BuzonOrdenes();
        CreadorOrdenes creador = new CreadorOrdenes(buzon);
        Operario operario1 = new Operario(1, buzon);
        Operario operario2 = new Operario(2, buzon);
        creador.start();
        operario1.start();
        operario2.start();
    }
}