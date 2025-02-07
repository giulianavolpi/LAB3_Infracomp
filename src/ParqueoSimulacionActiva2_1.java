import java.util.Random;

class Disponibilidad {
    private boolean hayEspacio = true;

    public synchronized boolean darHayEspacio() {
        return hayEspacio;
    }

    public synchronized void modHayEspacio(boolean estado) {
        hayEspacio = estado;
    }
}

class Parqueo {
    private int capacidadMaxima;
    private int espaciosOcupados;
    private final Disponibilidad disponibilidad;

    public Parqueo(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.espaciosOcupados = 0;
        this.disponibilidad = new Disponibilidad();
    }

    public boolean entrar(String auto) {
        //Activa
        while (!disponibilidad.darHayEspacio()) {
            System.out.println(auto + " intentó entrar, pero el estacionamiento está lleno.");
        }

        synchronized (this) {
            if (espaciosOcupados < capacidadMaxima) {
                espaciosOcupados++;
                //revisa si hay espacios disponibles
                if (espaciosOcupados >= capacidadMaxima) {
                    //sincroniza
                    disponibilidad.modHayEspacio(false);
                }
                System.out.println(auto + " ha estacionado. Espacios ocupados: " + espaciosOcupados);
                return true; // logro estacionar
            }
        }
        return false; // no logro estacionar
    }

    public synchronized void salir(String auto) {
        //revisa si hay autos en el parqueadero
        if (espaciosOcupados > 0) {
            //resta espacio
            espaciosOcupados--;
            System.out.println(auto + " ha salido. Espacios ocupados: " + espaciosOcupados);
            //libera el espacio y sincroniza
            disponibilidad.modHayEspacio(true);
        }
    }
}

class Auto extends Thread {
    private final Parqueo Parqueo;
    private final String nombre;
    private boolean estacionado = false; //Control interno

    public Auto(Parqueo Parqueo, String nombre) {
        this.Parqueo = Parqueo;
        this.nombre = nombre;
    }

    @Override
    public void run() {
        estacionado = Parqueo.entrar(nombre);

        if (estacionado) { //Solo espera si logró estacionar
            try {
                int tiempoEstacionado = new Random().nextInt(5000) + 1000;// entre 5 y 1 seg
                System.out.println(nombre + " permanecerá estacionado por " + tiempoEstacionado + " ms.");
                Thread.sleep(tiempoEstacionado);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Parqueo.salir(nombre);
        }
    }
}

public class ParqueoSimulacionActiva2_1 {
    public static void main(String[] args) {
        final int capacidad_Autos = 10;
        final int numeroAutos = 40;

        Parqueo Parqueo = new Parqueo(capacidad_Autos);
        Auto[] autos = new Auto[numeroAutos]; // Arreglo para almacenar los hilos

        for (int i = 0; i < numeroAutos; i++) {
            autos[i] = new Auto(Parqueo, "Auto-" + (i + 1));
            // corren en paralelo
            autos[i].start();
        }

        //Espera a que todos los autos terminen antes de cerrar el programa
        for (int i = 0; i < numeroAutos; i++) {
            try {
    
                autos[i].join(); // main espera a que cada auto termine los threads de autos corren en paralelo
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Simulación finalizada: Todos los autos han entrado y salido.");
    }
}
