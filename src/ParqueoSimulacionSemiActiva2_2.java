import java.util.Random;

class Disponibilidad {
    private volatile boolean hayEspacio = true;

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

    public synchronized boolean intentarEntrar(String auto) {
        if (espaciosOcupados < capacidadMaxima) {
            espaciosOcupados++;
            // revisa si hay espacios disponibles
            if (espaciosOcupados >= capacidadMaxima) {
                // sincroniza
                disponibilidad.modHayEspacio(false);
            }
            System.out.println(auto + " ha estacionado. Espacios ocupados: " + espaciosOcupados);
            return true; // logró estacionar
        }
        return false; // no logró estacionar
    }

    public synchronized void salir(String auto) {
        // revisa si hay autos en el parqueadero
        if (espaciosOcupados > 0) {
            // resta espacio
            espaciosOcupados--;
            System.out.println(auto + " ha salido. Espacios ocupados: " + espaciosOcupados);
            // libera el espacio y sincroniza
            disponibilidad.modHayEspacio(true);
        }
    }
}

class Auto extends Thread {
    private final Parqueo Parqueo;
    private final String nombre;

    public Auto(Parqueo Parqueo, String nombre) {
        this.Parqueo = Parqueo;
        this.nombre = nombre;
    }

    @Override
    public void run() {
        boolean estacionado = false;

        // Semi-activa: el auto intenta entrar hasta encontrar un espacio disponible
        while (!estacionado) {
            if (Parqueo.intentarEntrar(nombre)) {
                estacionado = true;
            } else {

                Thread.yield(); // Cede CPU para permitir que otros hilos corran
                try {
                    Thread.sleep(10); // Pequeña pausa para evitar un ciclo de espera infinita
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (estacionado) { // Solo ejecuta la espera si logró estacionar
            try {
                int tiempoEstacionado = new Random().nextInt(5000) + 1000; // entre 5 y 1 seg
                System.out.println(nombre + " permanecerá estacionado por " + tiempoEstacionado + " ms.");
                Thread.sleep(tiempoEstacionado);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Parqueo.salir(nombre);
        }
    }
}

public class ParqueoSimulacionSemiActiva2_2 {
    public static void main(String[] args) {
        final int capacidad_Autos = 10;
        final int numeroAutos = 40;

        Parqueo Parqueo = new Parqueo(capacidad_Autos);
        Auto[] autos = new Auto[numeroAutos]; // Arreglo para almacenar los hilos

        // Creación y ejecución de los hilos
        for (int i = 0; i < numeroAutos; i++) {
            autos[i] = new Auto(Parqueo, "Auto-" + (i + 1));
            // corren en paralelo
            autos[i].start();
        }

        // Espera a que todos los autos terminen antes de cerrar el programa
        for (int i = 0; i < numeroAutos; i++) {
            try {
                autos[i].join(); // main espera a que cada auto termine, los threads de autos corren en paralelo
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Simulación finalizada: Todos los autos han entrado y salido.");
    }
}

// Respuesta a la pregunta teórica:
// Las principales diferencias observadas se basan en la espera de los carros al
// entrar al parqueadero.
// En la implementación 1 todos los autos siguen intentando ingresar así esté
// lleno, esto genera salidas repetitivas con intentos fallidos.
// Con respecto a la implementación 2, los autos hacen pausas con el uso de
// yield() y pausas con el uso de sleep() si el parqueadero está lleno.
// La forma de implementar la espera en la manera 2 es más controlada y eso
// genera menos intentos fallidos y menos mensajes de espera.
// En conclusión la implementación 2 es más eficiente y controlada en la espera
// de los autos al intentar ingresar al parqueadero.
