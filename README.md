# Ordenamiento de votos con Counting Sort (Ejercicio 25)

Taller práctico de algoritmos de ordenamiento — Estructuras de Datos — Universidad de Cartagena.

Simula votos para 5 candidatos (números enteros del 1 al 5) e implementa Counting Sort
manualmente para:

- contar los votos,
- ordenar los valores (ascendente y descendente),
- mostrar la frecuencia por candidato,
- determinar el candidato con más y con menos votos,
- detectar empates.

Complejidad: O(n + k) en tiempo y memoria (n = votos, k = candidatos). No compara votos entre sí.

## Ejecutar

Requiere Java 17 o superior.

```bash
javac -encoding UTF-8 -d out src/*.java
java -cp out VotosApp
```

## Archivos

- `CountingSortVotos.java`: el algoritmo (conteo, acumulado y ubicación estable) y contadores de operaciones.
- `Eleccion.java`: candidatos, votos, frecuencias, ganador, último lugar y empates.
- `VotosApp.java`: menú, simulación de votos, paso a paso y medición de rendimiento.
