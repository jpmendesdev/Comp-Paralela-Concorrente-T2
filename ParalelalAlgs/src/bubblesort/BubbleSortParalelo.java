package bubblesort;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;
public class BubbleSortParalelo {
    public static class ParallelTask extends RecursiveTask<Void> {
        private int[] array;
        private int start;
        private int end;

        public ParallelTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Void compute() {
            if (end - start <= 10) {
                bubbleSort(array, start, end);
                return null;
            }

            int mid = (start + end) / 2;
            ParallelTask task1 = new ParallelTask(array, start, mid);
            ParallelTask task2 = new ParallelTask(array, mid + 1, end);
            invokeAll(task1, task2);
            task1.join();
            task2.join();
            return null;
        }

        private void bubbleSort(int[] array, int start, int end) {
            for (int i = start; i < end - 1; i++) {
                for (int j = start; j < end - i - 1; j++) {
                    if (array[j] > array[j+1]) {
                        int temp = array[j];
                        array[j] = array[j+1];
                        array[j+1] = temp;
                    }
                }
            }
        }
    }

    public static void parallelBubbleSort(int[] array) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ParallelTask(array, 0, array.length));
    }

}
