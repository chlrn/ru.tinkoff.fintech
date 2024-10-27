package com.example.rutinkofffintech.task_11.snapshot;

import java.util.ArrayList;
import java.util.List;

public class SnapshotManager<T> {
    private final List<Snapshot<T>> snapshots = new ArrayList<>();

    public void saveSnapshot(Snapshot<T> snapshot) {
        snapshots.add(snapshot);
    }

    public Snapshot<T> getSnapshot(int index) {
        if (index < 0 || index >= snapshots.size()) {
            throw new IndexOutOfBoundsException("No snapshot found at index " + index);
        }
        return snapshots.get(index);
    }

    public List<Snapshot<T>> getAllSnapshots() {
        return new ArrayList<>(snapshots);
    }
}
