import java.util.Random;

class Fila extends Thread {

    private int[] fila;
    private Compartido compartido;
    private boolean esUltimo;

    public Fila(int[] pfila, Compartido pcompartido, boolean pesUltimo) {
        this.fila = pfila;
        this.compartido = pcompartido;
        this.esUltimo = pesUltimo;
    }

    @Override
    public void run() {
        int sumaFila = 0;

        for (int i = 0; i < fila.length; i++) {
            sumaFila += fila[i];
        }
        compartido.adicionarAcumFila(sumaFila);
        if (esUltimo) {
            compartido.darTotal();

        }
    }

}

class Compartido {

    private int suma;
    // private int[][] matriz;

    public synchronized void adicionarAcumFila(int sumaFila) {

        suma += sumaFila;
    }

    public synchronized void darTotal() {

        System.out.println("La suma de la matriz es: " + suma);
    }

}

public class Matriz {

    public static void main(String[] args) throws InterruptedException {

        Compartido compartido = new Compartido();
        int n = 100;
        int[][] matriz = new int[n][n];
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matriz[i][j] = rand.nextInt(100);
            }
        }

        Thread[] filas = new Thread[n];
        for (int i = 0; i < n; i++) {
            boolean esUltimo = false;
            filas[i] = new Fila(matriz[i], compartido, esUltimo);
            filas[i].start();
        }

        compartido.darTotal();

    }
}
