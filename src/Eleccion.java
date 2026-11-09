/**
 * Representa una elección: los nombres de los candidatos y los votos recibidos.
 * El candidato número c (desde 1) corresponde a nombres[c - 1].
 *
 * Usa CountingSortVotos para ordenar y para obtener la frecuencia de cada
 * candidato, y a partir de esas frecuencias calcula ganador, último lugar y
 * empates.
 */
public class Eleccion {

    private final String[] nombres;
    private int[] votos;
    private final CountingSortVotos ordenador;

    public Eleccion(String[] nombres) {
        if (nombres == null || nombres.length < 2) {
            throw new IllegalArgumentException("Debe haber al menos dos candidatos.");
        }
        this.nombres = nombres.clone();
        this.votos = new int[0];
        this.ordenador = new CountingSortVotos(1, nombres.length);
    }

    public void setVotos(int[] votos) {
        this.votos = votos.clone();
        ordenador.ordenarAscendente(this.votos); // valida y calcula frecuencias
    }

    public int[] getVotos() {
        return votos.clone();
    }

    public int cantidadCandidatos() {
        return nombres.length;
    }

    public String nombreCandidato(int numero) {
        return nombres[numero - 1];
    }

    public CountingSortVotos getOrdenador() {
        return ordenador;
    }

    public int[] ordenarAscendente() {
        return ordenador.ordenarAscendente(votos);
    }

    public int[] ordenarDescendente() {
        return ordenador.ordenarDescendente(votos);
    }

    /** frecuencias[c - 1] = votos del candidato c. */
    public int[] frecuencias() {
        ordenador.ordenarAscendente(votos);
        return ordenador.getFrecuencias();
    }

    /** Números de los candidatos que tienen la mayor cantidad de votos (más de uno = empate). */
    public int[] candidatosConMasVotos() {
        int[] f = frecuencias();
        int mayor = f[0];
        for (int v : f) {
            mayor = Math.max(mayor, v);
        }
        return candidatosCon(f, mayor);
    }

    /** Números de los candidatos con la menor cantidad de votos. */
    public int[] candidatosConMenosVotos() {
        int[] f = frecuencias();
        int menor = f[0];
        for (int v : f) {
            menor = Math.min(menor, v);
        }
        return candidatosCon(f, menor);
    }

    private int[] candidatosCon(int[] f, int cantidad) {
        int total = 0;
        for (int v : f) {
            if (v == cantidad) {
                total++;
            }
        }
        int[] resultado = new int[total];
        int k = 0;
        for (int i = 0; i < f.length; i++) {
            if (f[i] == cantidad) {
                resultado[k++] = i + 1;
            }
        }
        return resultado;
    }

    /**
     * Todos los grupos de candidatos con la misma cantidad de votos (empates).
     * Como las frecuencias están en un arreglo indexado por candidato, basta
     * con comparar cada par una sola vez.
     */
    public String describirEmpates() {
        int[] f = frecuencias();
        boolean[] usado = new boolean[f.length];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < f.length; i++) {
            if (usado[i]) {
                continue;
            }
            StringBuilder grupo = new StringBuilder(nombres[i]);
            int integrantes = 1;
            for (int j = i + 1; j < f.length; j++) {
                if (f[j] == f[i]) {
                    grupo.append(", ").append(nombres[j]);
                    usado[j] = true;
                    integrantes++;
                }
            }
            if (integrantes > 1) {
                sb.append("  - Empate con ").append(f[i]).append(" voto(s): ").append(grupo).append('\n');
            }
        }
        return sb.length() == 0 ? "  No hay empates entre candidatos.\n" : sb.toString();
    }
}
