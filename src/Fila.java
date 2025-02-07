import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public static void Fila(String[] args) {
    String datos = "src/datos.csv";
    List<List<Integer>> matrizClientes = new ArrayList<>();
    int numeroCajeros = 0;

    try (BufferedReader lec = new BufferedReader(new FileReader(datos))) {
        String linea = lec.readLine();
        if (linea != null) {
            String[] primeraLinea = linea.split(",");
            numeroCajeros = Integer.parseInt(primeraLinea[0]);
        }

        String lineaDatos;
        while ((lineaDatos = lec.readLine()) != null) {
            String[] valores = lineaDatos.split(",");
            List<Integer> fila = new ArrayList<>();
            for (int i = 1; i < valores.length; i++) {
                fila.add(Integer.parseInt(valores[i]));
            }
            matrizClientes.add(fila);
        }
    } catch (IOException e) {
        System.err.println("Error: " + e.getMessage());
        return;
    }

    List<Cajero> cajeros = new ArrayList<>();
    for (int i = 0; i < numeroCajeros; i++) {
        List<Integer> filaClientes = matrizClientes.get(i);
        Cajero cajero = new Cajero(i + 1, filaClientes, 1.0);
        cajeros.add(cajero);
        cajero.start();
    }

    for (Cajero cajero : cajeros) {
        try {
            cajero.join();
        } catch (InterruptedException e) {
            System.err.println("Error en el cajero " + cajero.getId() + ": " + e.getMessage());
        }
    }
}
