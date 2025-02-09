import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Lab3Carlos {
    public static void main(String[] args) throws InterruptedException {
        int numeroDeClientes = 20;
        int numeroDeCajeros = 5;

        Fila fila = new Fila();

        List<Cliente> clientes = new ArrayList<>();

        for (int i = 1; i <= numeroDeClientes; i++) {
            Cliente cliente = new Cliente(i, fila);
            clientes.add(cliente);
            cliente.start();
        }

        Cajero cajero1 = new Cajero(1, fila);
        Cajero cajero2 = new Cajero(2, fila);
        Cajero cajero3 = new Cajero(3, fila);
        Cajero cajero4 = new Cajero(4, fila);
        Cajero cajero5 = new Cajero(5, fila);

        cajero1.start();
        cajero2.start();
        cajero3.start();
        cajero4.start();
        cajero5.start();

        for (Cliente cliente : clientes) {
            cliente.join();
        }

        for (int i = 0; i < numeroDeCajeros; i++) {
            fila.incorporacionFila(new Cliente(-1, fila));
        }

        cajero1.join();
        cajero2.join();
        cajero3.join();
        cajero4.join();
        cajero5.join();
    }

    static class Fila {
        LinkedList<Cliente> filaClientes;

        public Fila() {
            this.filaClientes = new LinkedList<>();
        }

        public synchronized void incorporacionFila(Cliente cliente) {
            filaClientes.add(cliente);
            System.out.println("El cliente " + cliente.uid + " a entrado a la fila");
            notifyAll();
        }

        public synchronized Cliente retiroFila() {
            while (filaClientes.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return filaClientes.poll();
        }
    }

    static class Cliente extends Thread {
        int uid;
        int tiempo;
        Fila fila;

        public Cliente(int uid, Fila fila) {
            this.uid = uid;
            this.fila = fila;
            this.tiempo = new Random().nextInt(2000) + 500;
        }

        @Override
        public void run() {
            System.out.println("El cliente " + uid + " ha sido creado");
            fila.incorporacionFila(this);
            try {
                Thread.sleep(new Random().nextInt(1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Cajero extends Thread {
        int id;
        Fila fila;
        double factorDeCansancio = 1.0;

        public Cajero(int id, Fila fila) {
            this.id = id;
            this.fila = fila;
        }

        @Override
        public void run() {
            while (true) {
                Cliente cliente = fila.retiroFila();
                if (cliente.uid == -1) {
                    System.out.println("El cajero " + id + " ha terminado de atender.");
                    break;
                }

                int tiempoBase = cliente.tiempo;
                int tiempoCalculado = (int) (tiempoBase * factorDeCansancio);

                System.out.println("El cajero " + id + " atendiendo al cliente " + cliente.uid +
                        " (tiempo de procesamiento básico: " + tiempoBase + "ms, " +
                        "factor de cansancio actual: " + factorDeCansancio + ", " +
                        "tiempo de procesamiento actual: " + tiempoCalculado + "ms)");

                try {
                    Thread.sleep(tiempoCalculado);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }

                factorDeCansancio += 0.001 * tiempoBase;
            }
        }
    }
}
