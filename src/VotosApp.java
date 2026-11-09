import java.util.Random;
import java.util.Scanner;

/**
 * Ejercicio 25 - Ordenamiento de votos con Counting Sort.
 *
 * Menú de consola para simular o ingresar votos, ordenarlos, ver la
 * frecuencia por candidato, el más y el menos votado y los empates.
 */
public class VotosApp {

    private static final Scanner entrada = new Scanner(System.in);
    private static Eleccion eleccion;

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   ORDENAMIENTO DE VOTOS - COUNTING SORT");
        System.out.println("===========================================");
        eleccion = new Eleccion(new String[]{
                "Ana Martínez", "Bruno Díaz", "Carla Ruiz", "David Ortiz", "Elena Castro"});
        eleccion.setVotos(new int[]{3, 1, 4, 1, 5, 2, 3, 3, 5, 1, 2, 4, 3, 1, 5, 2, 3, 4});
        System.out.println("Elección de ejemplo cargada: 5 candidatos y 18 votos.");

        int opcion;
        do {
            System.out.println();
            System.out.println("1. Ver candidatos");
            System.out.println("2. Simular votos aleatorios");
            System.out.println("3. Ingresar votos manualmente");
            System.out.println("4. Ver votos antes y después de ordenar");
            System.out.println("5. Frecuencia por candidato (conteo)");
            System.out.println("6. Candidato con más y con menos votos / empates");
            System.out.println("7. Ver paso a paso el arreglo de conteo");
            System.out.println("8. Cargar caso de prueba con empate");
            System.out.println("9. Medir rendimiento con muchos votos");
            System.out.println("0. Salir");
            opcion = leerEntero("Opción: ");
            try {
                switch (opcion) {
                    case 1 -> verCandidatos();
                    case 2 -> simularVotos();
                    case 3 -> ingresarVotos();
                    case 4 -> verOrdenamiento();
                    case 5 -> verFrecuencias();
                    case 6 -> verResultados();
                    case 7 -> pasoAPaso();
                    case 8 -> cargarEmpate();
                    case 9 -> medirRendimiento();
                    case 0 -> System.out.println("Fin del programa.");
                    default -> System.out.println("[!] Opción no válida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("[!] " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void verCandidatos() {
        for (int c = 1; c <= eleccion.cantidadCandidatos(); c++) {
            System.out.println("  " + c + " -> " + eleccion.nombreCandidato(c));
        }
    }

    private static void simularVotos() {
        int cantidad = leerEntero("Cantidad de votos a simular: ");
        if (cantidad < 1 || cantidad > 50_000_000) {
            throw new IllegalArgumentException("La cantidad debe estar entre 1 y 50.000.000.");
        }
        int semilla = leerEntero("Semilla aleatoria: ");
        eleccion.setVotos(votosAleatorios(cantidad, eleccion.cantidadCandidatos(), semilla));
        System.out.println("[OK] Se simularon " + cantidad + " voto(s).");
    }

    private static int[] votosAleatorios(int cantidad, int candidatos, long semilla) {
        Random azar = new Random(semilla);
        int[] votos = new int[cantidad];
        for (int i = 0; i < cantidad; i++) {
            votos[i] = 1 + azar.nextInt(candidatos);
        }
        return votos;
    }

    /** Los votos se escriben separados por espacios o comas. Los inválidos se rechazan. */
    private static void ingresarVotos() {
        System.out.println("Escriba los votos (números de 1 a " + eleccion.cantidadCandidatos()
                + ") separados por espacios o comas:");
        String linea = entrada.hasNextLine() ? entrada.nextLine().trim() : "";
        if (linea.isEmpty()) {
            throw new IllegalArgumentException("No se ingresó ningún voto.");
        }
        String[] partes = linea.split("[,\\s]+");
        int[] temporales = new int[partes.length];
        int validos = 0;
        int nulos = 0;
        for (String p : partes) {
            try {
                int v = Integer.parseInt(p);
                if (v >= 1 && v <= eleccion.cantidadCandidatos()) {
                    temporales[validos++] = v;
                } else {
                    nulos++;
                }
            } catch (NumberFormatException e) {
                nulos++;
            }
        }
        if (validos == 0) {
            throw new IllegalArgumentException("Ninguno de los votos ingresados es válido.");
        }
        int[] votos = new int[validos];
        for (int i = 0; i < validos; i++) {
            votos[i] = temporales[i];
        }
        eleccion.setVotos(votos);
        System.out.println("[OK] Votos válidos: " + validos + " | votos nulos descartados: " + nulos);
    }

    private static void verOrdenamiento() {
        int[] votos = eleccion.getVotos();
        System.out.println("Antes (orden de llegada): " + texto(votos));
        System.out.println("Ascendente:               " + texto(eleccion.ordenarAscendente()));
        CountingSortVotos o = eleccion.getOrdenador();
        System.out.println("Descendente:              " + texto(eleccion.ordenarDescendente()));
        System.out.printf("Operaciones (ascendente): conteo=%d, acumulado=%d, ubicación=%d, total=%d%n",
                o.getOperacionesConteo(), o.getOperacionesAcumulado(), o.getOperacionesUbicacion(),
                o.getOperacionesTotales());
        System.out.println("Comparaciones entre votos: 0 (Counting Sort no compara elementos).");
    }

    private static void verFrecuencias() {
        int[] f = eleccion.frecuencias();
        int total = eleccion.getVotos().length;
        System.out.printf("%-4s %-16s %7s %8s  %s%n", "N.º", "Candidato", "Votos", "%", "Gráfico");
        for (int i = 0; i < f.length; i++) {
            double porcentaje = total == 0 ? 0 : 100.0 * f[i] / total;
            int barras = total == 0 ? 0 : (int) Math.round(40.0 * f[i] / total);
            System.out.printf("%-4d %-16s %7d %7.1f%%  %s%n", i + 1, eleccion.nombreCandidato(i + 1),
                    f[i], porcentaje, "#".repeat(barras));
        }
        System.out.println("Total de votos: " + total);
    }

    private static void verResultados() {
        int[] f = eleccion.frecuencias();
        int[] mas = eleccion.candidatosConMasVotos();
        int[] menos = eleccion.candidatosConMenosVotos();
        System.out.println(mas.length == 1
                ? "Más votado: " + eleccion.nombreCandidato(mas[0]) + " con " + f[mas[0] - 1] + " voto(s)."
                : "EMPATE en el primer lugar con " + f[mas[0] - 1] + " votos: " + nombres(mas));
        System.out.println(menos.length == 1
                ? "Menos votado: " + eleccion.nombreCandidato(menos[0]) + " con " + f[menos[0] - 1] + " voto(s)."
                : "EMPATE en el último lugar con " + f[menos[0] - 1] + " votos: " + nombres(menos));
        System.out.println("Empates entre candidatos:");
        System.out.print(eleccion.describirEmpates());
    }

    /** Muestra la relación entre el arreglo de conteo y el arreglo ordenado. */
    private static void pasoAPaso() {
        int[] votos = eleccion.getVotos();
        if (votos.length > 40) {
            System.out.println("(Se muestra con los primeros 40 votos para que sea legible.)");
            int[] recorte = new int[40];
            System.arraycopy(votos, 0, recorte, 0, 40);
            votos = recorte;
        }
        int k = eleccion.cantidadCandidatos();
        int[] conteo = new int[k];
        System.out.println("Entrada: " + texto(votos));
        for (int v : votos) {
            conteo[v - 1]++;
        }
        System.out.println("Paso 1 - conteo[c] = votos del candidato c:        " + texto(conteo));
        for (int i = 1; i < k; i++) {
            conteo[i] += conteo[i - 1];
        }
        System.out.println("Paso 2 - acumulado (dónde termina cada candidato): " + texto(conteo));
        for (int c = 1; c <= k; c++) {
            int fin = conteo[c - 1];
            int inicio = c == 1 ? 0 : conteo[c - 2];
            System.out.println("   Candidato " + c + " ocupa las posiciones " + inicio + " a " + (fin - 1)
                    + (fin == inicio ? " (no tiene votos)" : ""));
        }
        int[] salida = new int[votos.length];
        for (int i = votos.length - 1; i >= 0; i--) {
            salida[--conteo[votos[i] - 1]] = votos[i];
        }
        System.out.println("Paso 3 - salida ordenada:                         " + texto(salida));
    }

    private static void cargarEmpate() {
        eleccion.setVotos(new int[]{1, 2, 3, 2, 1, 4, 2, 1, 3, 4, 5, 1, 2, 3, 4});
        System.out.println("[OK] Caso cargado: Ana y Bruno empatan con 4 votos, Carla y David con 3; Elena tiene 1.");
    }

    /** Counting Sort es lineal: al multiplicar los votos por 10 el tiempo crece ~10 veces. */
    private static void medirRendimiento() {
        int[] tamanios = {1_000, 10_000, 100_000, 1_000_000, 10_000_000};
        CountingSortVotos ordenador = new CountingSortVotos(1, eleccion.cantidadCandidatos());
        ordenador.ordenarAscendente(votosAleatorios(100_000, eleccion.cantidadCandidatos(), 1)); // calentar JVM
        System.out.printf("%-12s %-14s %-14s%n", "Votos", "Operaciones", "Tiempo (ms)");
        for (int n : tamanios) {
            int[] votos = votosAleatorios(n, eleccion.cantidadCandidatos(), 25);
            long inicio = System.nanoTime();
            int[] ordenado = ordenador.ordenarAscendente(votos);
            double ms = (System.nanoTime() - inicio) / 1_000_000.0;
            if (!estaOrdenado(ordenado)) {
                System.out.println("[!] Error: el resultado no quedó ordenado.");
            }
            System.out.printf("%-12d %-14d %-14.3f%n", n, ordenador.getOperacionesTotales(), ms);
        }
    }

    private static boolean estaOrdenado(int[] a) {
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1] > a[i]) {
                return false;
            }
        }
        return true;
    }

    private static String nombres(int[] candidatos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < candidatos.length; i++) {
            sb.append(eleccion.nombreCandidato(candidatos[i]));
            if (i < candidatos.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    private static String texto(int[] a) {
        if (a.length > 60) {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < 30; i++) {
                sb.append(a[i]).append(", ");
            }
            return sb.append("... (").append(a.length).append(" en total)]").toString();
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i]);
            if (i < a.length - 1) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (!entrada.hasNextLine()) {
                return 0;
            }
            try {
                return Integer.parseInt(entrada.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[!] Debe escribir un número entero.");
            }
        }
    }
}
