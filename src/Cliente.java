import java.util.Random;

public class Cliente extends Thread {
    private int id;
    private int tiempoBase;
    private Fila fila;

    public Cliente(int id, int tiempoBase, Fila fila) {
        this.id = id;
        this.tiempoBase = tiempoBase;
        this.fila = fila;
    }

    public int getClienteId() {
        return id;
    }

    public int getTiempoBase() {
        return tiempoBase;
    }

    @Override
    public void run() {
        fila.agregarCliente(this);
        try {
            Thread.sleep(new Random().nextInt(501));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}