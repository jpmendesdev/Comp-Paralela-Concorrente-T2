package quicksort;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class QuickSortParalelo {

    public static class QuickSortTask extends RecursiveTask<Void> {
        private int[] array;
        private int low, high;

        public QuickSortTask(int[] array, int low, int high) {
            this.array = array;
            this.low = low;
            this.high = high;
        }

        @Override
        protected Void compute() {
            if (low < high) {
                int pi = partition(array, low, high);
                QuickSortTask leftTask = new QuickSortTask(array, low, pi - 1);
                QuickSortTask rightTask = new QuickSortTask(array, pi + 1, high);
                leftTask.fork();
                rightTask.fork();
                leftTask.join();
                rightTask.join();
            }
            return null;
        }

        private int partition(int[] array, int low, int high) {
            int pivot = array[high];
            int i = low - 1;
            for (int j = low; j < high; j++) {
                if (array[j] <= pivot) {
                    i++;
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
            int temp = array[i + 1];
            array[i + 1] = array[high];
            array[high] = temp;
            return i + 1;
        }
    }

    public static void parallelQuickSort(int[] array, int low, int high) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new QuickSortTask(array, low, high));
    }
}
