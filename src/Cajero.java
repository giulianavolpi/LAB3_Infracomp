import javax.management.RuntimeErrorException;

// public class Cajero extends Thread {
//     private Integer idUltimaOrden;

//     private boolean disponible; 

//     public int Cajero() {
//         synchronized (idUltimaOrden) {
//             int cacheUltimo = idUltimaOrden;
//             idUltimaOrden = -1;
//             return cacheUltimo;
//         }
//     }

//     public synchronized void adicion(){
//         while (disponible){
//             try {
//                 wait();
//             } catch (InterruptedException e) {
//                 //throw new RuntimeErrorException(e);
//             }
//         }
//         notify();
//         disponible=false;
//         //arrayList().add(id);
//     }

//     public synchronized void modUltimaOrden(int data) {
//         idUltimaOrden = data;
//     }

//     public synchronized boolean darHayUltimaOrden() {
//         return idUltimaOrden != -1;
//     }
// }

public class Cajero extends Thread {
    private int id;
    private Fila fila;
    private double factorCansancio = 1.0;

    public Cajero(int id, Fila fila) {
        this.id = id;
        this.fila = fila;
    }

    @Override
    public void run() {
        while (true) {
            Cliente cliente = fila.retirarCliente();
            if (cliente == null) {
                System.out.println("Cajero " + id + " ha terminado su turno.");
                break;
            }

            int tiempoCalculado = (int) (cliente.getTiempoBase() * factorCansancio);
            System.out.println("El cajero " + id + " atendiendo al cliente " + cliente.getClienteId() +
                    " (tiempo de procesamiento básico: " + cliente.getTiempoBase() + "ms) " +
                    "factor de cansancio actual: " + String.format("%.4f", factorCansancio) + " " +
                    "tiempo de procesamiento actual: " + tiempoCalculado + "ms");

            try {
                Thread.sleep(tiempoCalculado);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            factorCansancio += cliente.getTiempoBase() * 0.0001;
            System.out.println("El cajero " + id + " terminó de atender al cliente " + cliente.getClienteId() +
                    " (tiempo de procesamiento básico: " + cliente.getTiempoBase() + "ms) " +
                    "factor de cansancio actual: " + String.format("%.4f", factorCansancio) + " " +
                    "tiempo de procesamiento actual: " + tiempoCalculado + "ms");
        }
    }
}
