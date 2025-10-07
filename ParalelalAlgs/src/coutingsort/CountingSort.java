package coutingsort;

public class CountingSort {
    public static void countingSort(int[] array) {
        int n = array.length;
        int max = array[0];
        int min = array[0];

        // Encontrando o valor máximo e mínimo
        for (int i = 1; i < n; i++) {
            if (array[i] > max) max = array[i];
            if (array[i] < min) min = array[i];
        }

        // Criação do array de contagem
        int range = max - min + 1;
        int[] count = new int[range];
        int[] output = new int[n];

        // Contagem de ocorrências
        for (int i = 0; i < n; i++) {
            count[array[i] - min]++;
        }

        // Preenchendo o array de saída com os valores ordenados
        for (int i = 1; i < range; i++) {
            count[i] += count[i - 1];
        }

        // Construindo o array de saída
        for (int i = n - 1; i >= 0; i--) {
            output[count[array[i] - min] - 1] = array[i];
            count[array[i] - min]--;
        }

        // Copiando o array de saída para o array original
        System.arraycopy(output, 0, array, 0, n);
    }
}
