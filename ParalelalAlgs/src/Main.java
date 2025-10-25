import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import bubblesort.BubbleSort;
import bubblesort.BubbleSortParalelo;
import coutingsort.CountingSort;
import coutingsort.CountingSortParalelo;
import mergesort.MergeSort;
import mergesort.MergeSortParalelo;
import quicksort.QuickSort;
import quicksort.QuickSortParalelo;

public class Main {

    private static final int[] TAMANHOS = {1000, 5000, 10000, 20000, 50000};
    private static final int AMOSTRAS = 5;
    private static final int THREADS_PARALEROS = 4;

    public static void main(String[] args) {
        String arquivoCSV = "resultados.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoCSV))) {
            writer.write("Algoritmo,Tipo,Tamanho,Tempo(ms),Threads\n");

            for (int tamanho : TAMANHOS) {
                System.out.println("Testando tamanho: " + tamanho);
                for (int amostra = 0; amostra < AMOSTRAS; amostra++) {
                    int[] dadosOriginais = gerarDadosAleatorios(tamanho);

                    executarETestar(writer, "Bubble Sort", "Sequencial", dadosOriginais.clone(),
                            array -> BubbleSort.bubbleSort(array), 1);

                    executarETestar(writer, "Bubble Sort", "Paralelo", dadosOriginais.clone(),
                            array -> BubbleSortParalelo.parallelBubbleSort(array), THREADS_PARALEROS);

                    executarETestar(writer, "Quick Sort", "Sequencial", dadosOriginais.clone(),
                            array -> QuickSort.quickSort(array, 0, array.length - 1), 1);

                    executarETestar(writer, "Quick Sort", "Paralelo", dadosOriginais.clone(),
                            array -> QuickSortParalelo.parallelQuickSort(array, 0, array.length - 1), THREADS_PARALEROS);

                    executarETestar(writer, "Merge Sort", "Sequencial", dadosOriginais.clone(),
                            array -> MergeSort.mergeSort(array), 1);

                    executarETestar(writer, "Merge Sort", "Paralelo", dadosOriginais.clone(),
                            array -> MergeSortParalelo.parallelMergeSort(array), THREADS_PARALEROS);

                    executarETestar(writer, "Counting Sort", "Sequencial", dadosOriginais.clone(),
                            array -> CountingSort.countingSort(array), 1);

                    executarETestar(writer, "Counting Sort", "Paralelo", dadosOriginais.clone(),
                            array -> CountingSortParalelo.parallelCountingSort(array), THREADS_PARALEROS);
                }
            }

            System.out.println("Testes concluídos. Resultados salvos em: " + arquivoCSV);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] gerarDadosAleatorios(int tamanho) {
        Random rand = new Random();
        int[] dados = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            dados[i] = rand.nextInt(100_000);
        }
        return dados;
    }

    @FunctionalInterface
    interface SortAlgorithm {
        void sort(int[] array);
    }

    private static void executarETestar(BufferedWriter writer, String algoritmo, String tipo,
                                        int[] dados, SortAlgorithm algoritmoOrdenacao, int threads) throws IOException {
        long inicio = System.currentTimeMillis();
        algoritmoOrdenacao.sort(dados);
        long fim = System.currentTimeMillis();
        long duracao = fim - inicio;

        writer.write(String.format("%s,%s,%d,%d,%d\n", algoritmo, tipo, dados.length, duracao, threads));
        writer.flush();
    }
}
