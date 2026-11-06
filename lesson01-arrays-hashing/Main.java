import java.util.*;

// ============================================================================
// DSA in Java — Lesson 1: Complexity, Arrays & Hashing
// For a 20+ year Technical Architect: not "learn Java", but "defend every
// data-structure choice with complexity, trade-offs and scale numbers".
//
// Run:  javac Main.java && java Main
// Expected output is printed at the end of each section.
// ============================================================================
public class Main {

    // ------------------------------------------------------------------
    // PROBLEM 1 — Two Sum (LeetCode 1, MAANG staple)
    // Given nums and target, return indices of the two numbers that add
    // up to target. Exactly one solution exists.
    //
    // Brute force: try every pair -> O(n^2) time, O(1) space.
    // At n = 2,000,000 (your streaming volume) that is ~4 trillion
    // operations — dead on arrival. That is the architect answer:
    // "O(n^2) does not survive production scale."
    //
    // Hash map: one pass, store value -> index. For each x, ask
    // "have I seen target - x?" -> O(n) time, O(n) space.
    // Trade-off: we pay memory to buy time. That trade is the whole
    // lesson of hashing.
    // ------------------------------------------------------------------
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>(); // value -> index
        for (int i = 0; i < nums.length; i++) {
            int need = target - nums[i];
            if (seen.containsKey(need)) {
                return new int[]{seen.get(need), i};
            }
            seen.put(nums[i], i);
        }
        throw new IllegalArgumentException("No solution");
    }

    // ------------------------------------------------------------------
    // PROBLEM 2 — Contains Duplicate (LeetCode 217)
    // Same pattern, even simpler: a Set is a HashMap with no value.
    // O(n) time, O(n) space. Sorting first would be O(n log n) and
    // would destroy the original order — say that out loud in an
    // interview, it shows you think about side effects.
    // ------------------------------------------------------------------
    public static boolean containsDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        for (int n : nums) {
            if (!seen.add(n)) return true; // add() returns false if present
        }
        return false;
    }

    // ------------------------------------------------------------------
    // PROBLEM 3 — Top K Frequent Elements (LeetCode 347)
    // Count with a HashMap (O(n)), then keep only the K biggest with
    // a MIN-heap of size K (O(n log K)). Total O(n log K).
    //
    // Architect follow-up: "K is small, n is 2M events/min — why a
    // heap and not sort everything?" Sorting all frequencies is
    // O(n log n). The heap keeps only K items, so memory is O(K),
    // not O(n). At streaming scale that is the difference between
    // a job that runs and a job that OOMs.
    //
    // Java note: PriorityQueue is a min-heap by default. We order
    // entries by frequency, poll() drops the smallest when size > K.
    // ------------------------------------------------------------------
    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int n : nums) count.put(n, count.getOrDefault(n, 0) + 1);

        PriorityQueue<Map.Entry<Integer, Integer>> heap =
                new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));
        for (Map.Entry<Integer, Integer> e : count.entrySet()) {
            heap.offer(e);
            if (heap.size() > k) heap.poll(); // drop the least frequent
        }
        int[] out = new int[k];
        for (int i = k - 1; i >= 0; i--) out[i] = heap.poll().getKey();
        return out;
    }

    // ------------------------------------------------------------------
    // BUILD IT YOURSELF — a tiny HashMap with chaining
    // Architects get asked "how does HashMap work?" — the only honest
    // answer is to have built one. This is a minimal version:
    // buckets (array of linked lists), hash -> bucket index,
    // collisions chained, resize when load factor exceeds 0.75.
    //
    // Key points to say aloud:
    //  - hashCode() decides the bucket; equals() decides identity
    //    inside the bucket. Break equals/hashCode and the map breaks.
    //  - Load factor 0.75 is the time/space trade-off: resize doubles
    //    the buckets (O(n) rehash) to keep chains short (O(1) lookup).
    //  - Java 8+: a long chain becomes a tree (O(log n)) instead of
    //    a list (O(n)) — that is why a bad hash function is a
    //    denial-of-service risk, not just a performance bug.
    // ------------------------------------------------------------------
    static class MyHashMap<K, V> {
        private static class Node<K, V> {
            K key; V value; Node<K, V> next;
            Node(K k, V v, Node<K, V> n) { key = k; value = v; next = n; }
        }
        private Node<K, V>[] buckets;
        private int size = 0;
        private static final double LOAD_FACTOR = 0.75;

        @SuppressWarnings("unchecked")
        MyHashMap() { buckets = (Node<K, V>[]) new Node[16]; }

        private int bucket(K key) {
            return (key.hashCode() & 0x7fffffff) % buckets.length;
        }
        public void put(K key, V value) {
            if ((double) (size + 1) / buckets.length > LOAD_FACTOR) resize();
            int i = bucket(key);
            for (Node<K, V> n = buckets[i]; n != null; n = n.next) {
                if (n.key.equals(key)) { n.value = value; return; }
            }
            buckets[i] = new Node<>(key, value, buckets[i]);
            size++;
        }
        public V get(K key) {
            for (Node<K, V> n = buckets[bucket(key)]; n != null; n = n.next) {
                if (n.key.equals(key)) return n.value;
            }
            return null;
        }
        @SuppressWarnings("unchecked")
        private void resize() {
            Node<K, V>[] old = buckets;
            buckets = (Node<K, V>[]) new Node[old.length * 2];
            size = 0;
            for (Node<K, V> head : old) {
                for (Node<K, V> n = head; n != null; n = n.next) put(n.key, n.value);
            }
        }
        public int size() { return size; }
    }

    // ------------------------------------------------------------------
    // MAIN — run everything, check the expected outputs yourself.
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        System.out.println("Two Sum [2,7,11,15] target 9 -> "
                + Arrays.toString(twoSum(new int[]{2, 7, 11, 15}, 9))
                + "   (expect [0, 1])");
        System.out.println("Contains Duplicate [1,2,3,1] -> "
                + containsDuplicate(new int[]{1, 2, 3, 1})
                + "   (expect true)");
        System.out.println("Contains Duplicate [1,2,3,4] -> "
                + containsDuplicate(new int[]{1, 2, 3, 4})
                + "   (expect false)");
        System.out.println("Top 2 Frequent [1,1,1,2,2,3] -> "
                + Arrays.toString(topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2))
                + "   (expect [1, 2] in some order)");

        MyHashMap<String, Integer> map = new MyHashMap<>();
        map.put("claims", 200);
        map.put("orders", 150);
        map.put("claims", 201); // overwrite, size stays 2
        System.out.println("MyHashMap get(\"claims\") -> " + map.get("claims")
                + "   (expect 201), size -> " + map.size() + " (expect 2)");

        // --- HOMEWORK (do these before Lesson 2, timed 20 min each) ---
        // H1. Group Anagrams: ["eat","tea","tan","ate","nat","bat"] ->
        //     [["bat"],["nat","tan"],["ate","eat","tea"]]. Hint: sorted
        //     word (or char-count) as the HashMap key.
        // H2. Longest Consecutive Sequence: [100,4,200,1,3,2] -> 4
        //     ([1,2,3,4]). Hint: only start counting at a number whose
        //     predecessor is NOT in the set — that is what makes it O(n).
        // H3. Subarray Sum Equals K: count subarrays summing to K.
        //     Hint: prefix sums in a HashMap, same pattern as Two Sum.
    }
}
