package mergesort;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class MergeSortParalelo {

    public static class MergeTask extends RecursiveTask<int[]> {
        private int[] array;

        public MergeTask(int[] array) {
            this.array = array;
        }

        @Override
        protected int[] compute() {
            if (array.length < 2) {
                return array;
            }
            int mid = array.length / 2;
            int[] left = new int[mid];
            int[] right = new int[array.length - mid];

            System.arraycopy(array, 0, left, 0, mid);
            System.arraycopy(array, mid, right, 0, array.length - mid);

            MergeTask leftTask = new MergeTask(left);
            MergeTask rightTask = new MergeTask(right);
            leftTask.fork();
            rightTask.fork();

            int[] leftSorted = leftTask.join();
            int[] rightSorted = rightTask.join();

            return merge(leftSorted, rightSorted);
        }

        private int[] merge(int[] left, int[] right) {
            int[] result = new int[left.length + right.length];
            int i = 0, j = 0, k = 0;
            while (i < left.length && j < right.length) {
                if (left[i] <= right[j]) {
                    result[k++] = left[i++];
                } else {
                    result[k++] = right[j++];
                }
            }
            while (i < left.length) {
                result[k++] = left[i++];
            }
            while (j < right.length) {
                result[k++] = right[j++];
            }
            return result;
        }
    }

    public static void parallelMergeSort(int[] array) {
        ForkJoinPool pool = new ForkJoinPool();
        array = pool.invoke(new MergeTask(array));
    }
}
