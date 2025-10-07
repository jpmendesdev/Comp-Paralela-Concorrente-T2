package coutingsort;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class CountingSortParalelo {

    public static class CountingTask extends RecursiveTask<int[]> {
        private int[] array;
        private int start, end;
        private int min, max;

        public CountingTask(int[] array, int start, int end, int min, int max) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.min = min;
            this.max = max;
        }

        @Override
        protected int[] compute() {
            int[] count = new int[max - min + 1];
            for (int i = start; i < end; i++) {
                count[array[i] - min]++;
            }
            return count;
        }
    }

    public static void parallelCountingSort(int[] array) {
        int n = array.length;
        int max = array[0];
        int min = array[0];

        // Encontrando o valor máximo e mínimo
        for (int i = 1; i < n; i++) {
            if (array[i] > max) max = array[i];
            if (array[i] < min) min = array[i];
        }

        // Usando ForkJoinPool para paralelizar
        ForkJoinPool pool = new ForkJoinPool();
        int numThreads = Runtime.getRuntime().availableProcessors();
        int chunkSize = n / numThreads;
        CountingTask[] tasks = new CountingTask[numThreads];

        // Dividindo o trabalho de contagem em subtarefas
        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? n : (i + 1) * chunkSize;
            tasks[i] = new CountingTask(array, start, end, min, max);
            tasks[i].fork();
        }

        // Combinando os resultados
        int[] finalCount = new int[max - min + 1];
        for (CountingTask task : tasks) {
            int[] partialCount = task.join();
            for (int i = 0; i < partialCount.length; i++) {
                finalCount[i] += partialCount[i];
            }
        }

        // Agora temos a contagem total, podemos construir o array de saída sequencialmente
        int[] output = new int[n];
        for (int i = 1; i < finalCount.length; i++) {
            finalCount[i] += finalCount[i - 1];
        }

        for (int i = n - 1; i >= 0; i--) {
            output[finalCount[array[i] - min] - 1] = array[i];
            finalCount[array[i] - min]--;
        }

        System.arraycopy(output, 0, array, 0, n);
    }
}
