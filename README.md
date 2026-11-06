# DSA in Java — for a 20+ Year Technical Architect

Learn data structures in Java the DevOps way: **code → commit → CI runs → visualize → merge.**

Every lesson lives in its own folder, runs in CI, and has a visualization.

## Repo layout


```
dsa-java-architect/
├── .github/workflows/ci.yml   # CI: compile + run every lesson on every push/PR
├── .gitignore
├── README.md
├── visualizer/index.html      # Open in a browser: step through Two Sum,
│                              # HashMap buckets, Top-K heap — no server needed
├── lesson01-arrays-hashing/Main.java
├── lesson02-two-pointers-sliding-window/Main.java   # next
├── lesson03-stacks-queues/Main.java
└── ... (one folder per lesson, same pattern)
```

## Prerequisites

- Java 21 (Temurin / Oracle) — `java -version`
- Git + a GitHub account
- Any IDE: IntelliJ IDEA, VS Code + Java extension, or Eclipse

## Run a lesson locally

```bash
cd lesson01-arrays-hashing
javac Main.java
java Main
```

Expected output is printed at the end of each section in `Main.java`.

## Visualize an algorithm

Open `visualizer/index.html` in a browser (double-click it — no server needed).

- **Two Sum visualizer:** step through the array, watch the HashMap fill up, see the exact moment `target - x` is found.
- **HashMap buckets:** type a key, see which bucket it lands in, and how collisions chain.
- **Top-K heap:** watch the min-heap keep only the K biggest frequencies.

Rule for every lesson from now on: **if you can't draw it, you don't understand it yet.**
Use the visualizer first, then code it, then explain it aloud.

## Workflow for every lesson (DevOps model)

1. Create a branch: `git checkout -b lesson02-two-pointers`
2. Write the code + a short note in the lesson folder
3. Run it locally: `javac Main.java && java Main`
4. Commit and push, open a Pull Request to `main`
5. GitHub Actions CI compiles and runs **all** lessons — green before merge
6. Merge the PR, delete the branch, start the next lesson

CI runs on every push and pull request to `main`. A red CI means the lesson
doesn't run clean — fix it before merging, exactly like production.

## Lessons

| # | Lesson | Focus |
|---|--------|-------|
| 1 | Arrays & Hashing | Complexity, Two Sum, Contains Duplicate, Top-K, build-your-own HashMap |
| 2 | Two Pointers & Sliding Window | Next |
| 3 | Stacks, Queues & Monotonic Stack | |
| 4 | Linked Lists | |
| 5 | Trees, BST & Heaps | |
| 6 | Graphs — BFS, DFS, Topological Sort | |
| 7 | Dynamic Programming | |
| 8 | Tries, Union-Find & Advanced Structures | |
| 9 | System-design DSA | Caching, consistent hashing, B-trees/indexes, Top-K at scale |
| 10 | Timed MAANG mocks | |
