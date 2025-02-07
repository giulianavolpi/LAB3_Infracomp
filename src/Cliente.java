public class Cliente extends Thread {
    private BuzonOrdenes buzon;

    public Cliente(BuzonOrdenes pbuzon) {
        this.buzon = pbuzon;
    }

    @Override
    public void run() {
        int uidActual = 0;
        while (uidActual != 100) {
            buzon.modUltimaOrden(uidActual);
            uidActual++;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
