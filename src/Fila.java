import java.util.LinkedList;
import java.util.Queue;

// public static void Fila(String[] args) {
//     String datos = "src/datos.csv";
//     List<List<Integer>> matrizClientes = new ArrayList<>();
//     int numeroCajeros = 0;

//     List<Cajero> cajeros = new ArrayList<>();
//     for (int i = 0; i < numeroCajeros; i++) {
//         List<Integer> filaClientes = matrizClientes.get(i);
//         Cajero cajero = new Cajero(i + 1, filaClientes, 1.0);
//         cajeros.add(cajero);
//         cajero.start();
//     }

//     for (Cajero cajero : cajeros) {
//         try {
//             cajero.join();
//         } catch (InterruptedException e) {
//             System.err.println("Error en el cajero " + cajero.getId() + ": " + e.getMessage());
//         }
//     }
// }

// class Fila {

//     public Fila() {

//     }

//     private Queue<Cliente> filaClientes = new LinkedList<>();

//     public synchronized void agregarCliente(Cliente cliente) {
//         filaClientes.add(cliente);
//         System.out
//                 .println("Cliente agregado: " + cliente.getClienteId() + " - Tiempo Base: " + cliente.getTiempoBase());
//         notify();
//     }

//     public synchronized Cliente retirarCliente() {
//         if (filaClientes.isEmpty()) {
//             return null;
//         }

//         while (filaClientes.isEmpty()) {
//             try {
//                 System.out.println("Cajero en espera...");
//                 wait();
//             } catch (InterruptedException e) {
//                 e.printStackTrace();
//             }
//         }
//         return filaClientes.poll();
//     }

// }

public class Fila {
    private Queue<Cliente> filaClientes = new LinkedList<>();

    public synchronized void agregarCliente(Cliente cliente) {
        filaClientes.add(cliente);
        System.out
                .println("Cliente " + cliente.getClienteId() + " agregado a la fila (tiempo de procesamiento: "
                        + cliente.getTiempoBase() + ")");
        notifyAll();
    }

    public synchronized Cliente retirarCliente() {
        while (filaClientes.isEmpty()) {
            try {
                System.out.println("Cajero en esperando retiro de cliente...");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return filaClientes.poll();
    }

}
