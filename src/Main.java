import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// public class Main {
//     public static void main(String[] args) throws InterruptedException {
//         BuzonOrdenes buzon = new BuzonOrdenes();
//         CreadorOrdenes creador = new CreadorOrdenes(buzon);
//         Operario operario1 = new Operario(1, buzon);
//         Operario operario2 = new Operario(2, buzon);
//         creador.start();
//         operario1.start();
//         operario2.start();
//     }
// }

public class Main {
    public static void main(String[] args) {
        Fila fila = new Fila();
        List<Cajero> cajeros = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            Cajero cajero = new Cajero(i, fila);
            cajeros.add(cajero);
        }

        for (int i = 1; i <= 10; i++) {
            new Cliente(i, new Random().nextInt(2001), fila).start();
        }

        for (Cajero cajero : cajeros) {
            cajero.start();
        }
    }
}