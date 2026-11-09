/**
 * Counting Sort aplicado a votos.
 *
 * Cada voto es un número entero que identifica a un candidato dentro de un
 * rango pequeño [minimo, maximo] (por ejemplo, candidatos 1 a 5).
 *
 * El algoritmo NO compara votos entre sí. Funciona en tres pasos:
 *  1. Conteo:     conteo[v - minimo]++ por cada voto v           -> O(n)
 *  2. Acumulado:  conteo[i] += conteo[i - 1]                     -> O(k)
 *                 Ahora conteo[i] indica cuántos votos son <= candidato i,
 *                 es decir, la posición donde TERMINA ese candidato en el
 *                 arreglo ordenado.
 *  3. Ubicación:  se recorre la entrada de derecha a izquierda y cada voto
 *                 se coloca en salida[--conteo[v - minimo]]          -> O(n)
 *                 Recorrer al revés hace que el algoritmo sea estable.
 *
 * Complejidad total: O(n + k), donde n = votos y k = cantidad de candidatos.
 * Memoria adicional: O(n + k) (arreglo de salida y arreglo de conteo).
 */
public class CountingSortVotos {

    private final int minimo;
    private final int maximo;

    // Contadores de trabajo del último ordenamiento
    private long operacionesConteo;
    private long operacionesAcumulado;
    private long operacionesUbicacion;

    // Frecuencias del último ordenamiento (antes de convertirlas en acumulado)
    private int[] frecuencias;

    public CountingSortVotos(int minimo, int maximo) {
        if (minimo > maximo) {
            throw new IllegalArgumentException("El mínimo no puede ser mayor que el máximo.");
        }
        this.minimo = minimo;
        this.maximo = maximo;
    }

    /** Ordena de forma ascendente y devuelve un NUEVO arreglo (la entrada no cambia). */
    public int[] ordenarAscendente(int[] votos) {
        validar(votos);
        int k = maximo - minimo + 1;
        int[] conteo = new int[k];
        operacionesConteo = 0;
        operacionesAcumulado = 0;
        operacionesUbicacion = 0;

        // Paso 1: contar cuántas veces aparece cada candidato
        for (int voto : votos) {
            conteo[voto - minimo]++;
            operacionesConteo++;
        }
        frecuencias = conteo.clone(); // se guarda la frecuencia de cada candidato

        // Paso 2: acumulado (posición final de cada candidato en la salida)
        for (int i = 1; i < k; i++) {
            conteo[i] += conteo[i - 1];
            operacionesAcumulado++;
        }

        // Paso 3: ubicar cada voto en su posición (de derecha a izquierda = estable)
        int[] salida = new int[votos.length];
        for (int i = votos.length - 1; i >= 0; i--) {
            int indice = votos[i] - minimo;
            conteo[indice]--;
            salida[conteo[indice]] = votos[i];
            operacionesUbicacion++;
        }
        return salida;
    }

    /** Descendente: se recorre el arreglo de frecuencias del candidato mayor al menor. */
    public int[] ordenarDescendente(int[] votos) {
        ordenarAscendente(votos); // calcula frecuencias
        int[] salida = new int[votos.length];
        int pos = 0;
        for (int i = frecuencias.length - 1; i >= 0; i--) {
            for (int j = 0; j < frecuencias[i]; j++) {
                salida[pos++] = i + minimo;
            }
        }
        return salida;
    }

    /** Todos los votos deben estar dentro del rango de candidatos. */
    private void validar(int[] votos) {
        if (votos == null) {
            throw new IllegalArgumentException("El arreglo de votos no puede ser nulo.");
        }
        for (int i = 0; i < votos.length; i++) {
            if (votos[i] < minimo || votos[i] > maximo) {
                throw new IllegalArgumentException("Voto inválido en la posición " + i + ": " + votos[i]
                        + " (debe estar entre " + minimo + " y " + maximo + ").");
            }
        }
    }

    public int[] getFrecuencias() {
        return frecuencias == null ? new int[0] : frecuencias.clone();
    }

    public int getMinimo() {
        return minimo;
    }

    public int getMaximo() {
        return maximo;
    }

    public long getOperacionesConteo() {
        return operacionesConteo;
    }

    public long getOperacionesAcumulado() {
        return operacionesAcumulado;
    }

    public long getOperacionesUbicacion() {
        return operacionesUbicacion;
    }

    public long getOperacionesTotales() {
        return operacionesConteo + operacionesAcumulado + operacionesUbicacion;
    }
}
