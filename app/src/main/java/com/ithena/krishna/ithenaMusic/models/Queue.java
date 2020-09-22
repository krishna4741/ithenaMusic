package com.ithena.krishna.ithenaMusic.models;

import java.util.ArrayList;
import java.util.List;


public class Queue {
    private List<UnifiedTrack> queue;

    public Queue() {
        queue = new ArrayList<>();
    }

    public List<UnifiedTrack> getQueue() {
        return queue;
    }

    public void setQueue(List<UnifiedTrack> queue) {
        this.queue = queue;
    }

    public void addToQueue(UnifiedTrack track) {
        queue.add(track);
    }

    public void removeItem(UnifiedTrack ut) {
        for (int i = 0; i < queue.size(); i++) {
            if (ut.equals(queue.get(i))) {
                queue.remove(i);
                break;
            }
        }
    }

}
