package com.sarat.learning.basics;

import java.util.*;

// ============================================================================
// DSA in Java — Lesson 0: BASICS
// What is a data structure? How is data stored in memory? What is Big-O?
//
// Run in STS: right-click Main.java -> Run As -> Java Application
// Run in terminal (from repo root):
//   javac -d out lesson00-basics/com/sarat/learning/basics/Main.java
//   java -cp out com.sarat.learning.basics.Main
// ============================================================================
public class Main {

    // ------------------------------------------------------------------
    // 1. WHAT IS A DATA STRUCTURE?
    // A data structure is a way of organising data in memory so that a
    // specific operation is fast.
    //
    //   Need fast lookup by key?        -> HashMap
    //   Need first-in, first-out?       -> Queue
    //   Need biggest element quickly?   -> Heap
    //   Need data in order?             -> Array / Tree
    //
    // There is no "best" structure. Every choice is a trade-off:
    // fast in one operation, slow in another. Interviews test whether
    // you can name that trade-off — not whether you memorised a list.
    // ------------------------------------------------------------------

    // ------------------------------------------------------------------
    // 2. MEMORY: HOW AN ARRAY IS STORED
    // int[] a = {10, 20, 30, 40};
    // In memory the 4 ints sit in ONE continuous block, side by side:
    //
    //   address:  100   104   108   112
    //   value:    [10]  [20]  [30]  [40]
    //   index:     0     1     2     3
    //
    // That is why a[i] is O(1): address = start + i * sizeOf(int).
    // The computer does one multiplication, no searching.
    //
    // It is also why INSERTING in the middle is O(n): to insert at
    // index 1, every element after it must shift one place right.
    // And why an array has a FIXED size in Java: the block is reserved
    // once, it cannot grow. ArrayList grows by making a NEW, bigger
    // block and copying everything over — that copy is O(n).
    // ------------------------------------------------------------------

    // ------------------------------------------------------------------
    // 3. BIG-O: HOW TO READ IT
    // Big-O describes how time grows as n grows. Say it with numbers:
    //
    //   O(1)       constant:  1 step, whatever n is. a[i], HashMap.get
    //   O(log n)   halving:   n=1,000,000 -> ~20 steps. Binary search
    //   O(n)       linear:    n steps. One pass over an array
    //   O(n log n) sorting:   n=1,000,000 -> ~20,000,000 steps
    //   O(n^2)     nested:    n=10,000 -> 100,000,000 steps. Avoid.
    //
    // Architect test: at n = 2,000,000 (your streaming volume),
    // O(n^2) is ~4 trillion operations — dead. O(n log n) is ~42M.
    // O(n) is 2M. That is the whole point of Big-O in an interview.
    //
    // Space matters too: an algorithm that is O(n) time but O(n)
    // extra memory (a HashMap copy) may OOM at scale. Always state
    // BOTH time and space.
    // ------------------------------------------------------------------

    // --- Array basics, step by step -----------------------------------
    static void arrayBasics() {
        int[] a = {10, 20, 30, 40, 50};

        // Access: O(1)
        System.out.println("a[2] = " + a[2] + "  (expect 30)");

        // Traverse: O(n)
        System.out.print("Traverse: ");
        for (int i = 0; i < a.length; i++) System.out.print(a[i] + " ");
        System.out.println();

        // Search (linear): O(n) — check each element until found
        System.out.println("Linear search 40 -> index " + linearSearch(a, 40) + " (expect 3)");
        System.out.println("Linear search 99 -> index " + linearSearch(a, 99) + " (expect -1)");

        // Find max: O(n) — you must look at every element once
        System.out.println("Max = " + findMax(a) + " (expect 50)");

        // Reverse in place: O(n) time, O(1) extra space (two pointers)
        reverse(a);
        System.out.println("Reversed = " + Arrays.toString(a) + " (expect [50, 40, 30, 20, 10])");
    }

    static int linearSearch(int[] a, int target) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == target) return i;
        }
        return -1;
    }

    static int findMax(int[] a) {
        int max = a[0];
        for (int i = 1; i < a.length; i++) {
            if (a[i] > max) max = a[i];
        }
        return max;
    }

    // Two pointers: left at start, right at end, swap, move inwards.
    // This is the first pattern you must be able to write blindfolded.
    static void reverse(int[] a) {
        int left = 0, right = a.length - 1;
        while (left < right) {
            int temp = a[left];
            a[left] = a[right];
            a[right] = temp;
            left++;
            right--;
        }
    }

    // --- ArrayList: the growable array --------------------------------
    // ArrayList IS an array underneath. When it fills up, Java makes a
    // new array ~1.5x bigger and copies everything. So:
    //   get(i)  -> O(1)   (same as array)
    //   add(x)  -> O(1) amortised (occasionally O(n) on resize)
    //   add(i,x)-> O(n)   (elements must shift)
    //   remove  -> O(n)   (elements must shift)
    static void arrayListBasics() {
        List<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(20);
        list.add(30);
        System.out.println("ArrayList = " + list + " (expect [10, 20, 30])");
        System.out.println("list.get(1) = " + list.get(1) + " (expect 20)");
        list.remove(0); // shifts 20, 30 left — O(n)
        System.out.println("After remove(0) = " + list + " (expect [20, 30])");
    }

    // --- Binary search: O(log n), but ONLY on a sorted array ---------
    // Cut the search space in half every step. n=1,000,000 -> ~20 steps.
    // If the array is NOT sorted, binary search gives a wrong answer —
    // say that in an interview before you write it.
    static int binarySearch(int[] a, int target) {
        int left = 0, right = a.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2; // NOT (left+right)/2 — overflow!
            if (a[mid] == target) return mid;
            if (a[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println("=== Lesson 0: Basics — Arrays ===");
        arrayBasics();
        System.out.println();
        arrayListBasics();
        System.out.println();
        int[] sorted = {10, 20, 30, 40, 50};
        System.out.println("Binary search 30 in [10,20,30,40,50] -> "
                + binarySearch(sorted, 30) + " (expect 2)");
        System.out.println("Binary search 99 -> "
                + binarySearch(sorted, 99) + " (expect -1)");
        System.out.println();
        System.out.println("--- HOMEWORK (20 min each, timed) ---");
        System.out.println("H1. Second largest element in an array, in ONE pass, O(n), O(1) space.");
        System.out.println("    e.g. [10, 20, 30, 40] -> 30. Careful with duplicates: [40, 40, 30] -> 30.");
        System.out.println("H2. Move all zeroes to the end, keeping the order of non-zeroes.");
        System.out.println("    e.g. [0, 1, 0, 3, 12] -> [1, 3, 12, 0, 0]. Do it in place, O(n).");
        System.out.println("H3. Check if an array is sorted (ascending). Return true/false, O(n).");
        System.out.println("    Then: if it is NOT sorted, what is the cheapest way to search it? Why?");
    }
}
