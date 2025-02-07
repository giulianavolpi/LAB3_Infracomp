public class BuzonOrdenes extends Thread {
    private Integer idUltimaOrden;

    public int darUltimaOrden() {
        synchronized (idUltimaOrden) {
            int cacheUltimo = idUltimaOrden;
            idUltimaOrden = -1;
            return cacheUltimo;
        }
    }

    public synchronized void modUltimaOrden(int data) {
        idUltimaOrden = data;
    }

    public synchronized boolean darHayUltimaOrden() {
        return idUltimaOrden != -1;
    }
}
