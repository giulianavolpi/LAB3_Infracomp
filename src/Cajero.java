import javax.management.RuntimeErrorException;

public class Cajero extends Thread {
    private Integer idUltimaOrden;

    private boolean disponible; 

    public int Cajero() {
        synchronized (idUltimaOrden) {
            int cacheUltimo = idUltimaOrden;
            idUltimaOrden = -1;
            return cacheUltimo;
        }
    }

    public synchronized void adicion(){
        while (disponible){
            try {
                wait();
            } catch (InterruptedException e) {
                //throw new RuntimeErrorException(e);
            }
        }
        notify();
        disponible=false;
        //arrayList().add(id);
    }
    
    public synchronized void modUltimaOrden(int data) {
        idUltimaOrden = data;
    }

    public synchronized boolean darHayUltimaOrden() {
        return idUltimaOrden != -1;
    }
}
