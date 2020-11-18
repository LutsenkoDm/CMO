package main.functional.components;

import java.util.LinkedList;
import java.util.Optional;

public class Buffer {
    private int maxSize;
    private final LinkedList<Request> buffer = new LinkedList<>();

    public Buffer(int maxSize) {
        this.maxSize = maxSize;
    }

    public void add(Request request) {
        if (buffer.size() == maxSize) {
            double timeInBuffer = request.getBeginTime() - buffer.getFirst().getBeginTime();
            Source firstInBufferRequestSource = buffer.getFirst().getSource();
            firstInBufferRequestSource.addTimeInBuffer(timeInBuffer);
            firstInBufferRequestSource.addTimeInSystem(timeInBuffer);
            firstInBufferRequestSource.incRejectedRequests();
            buffer.removeFirst();
        }
        buffer.add(request);
    }

    public Optional<Request> findRequestEarlierThen(double time) {
        for (int i = buffer.size() - 1; i >= 0; i--) {
            if (buffer.get(i).getBeginTime() <= time) {
                return Optional.of(buffer.get(i));
            }
        }
        return Optional.empty();
    }

    public Request getLast() {
        return buffer.getLast();
    }

    public Request removeLast() {
        return buffer.removeLast();
    }

    public Request remove(Request request) {
        buffer.remove(request);
        return request;
    }

    public double maxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public boolean full() {
        return maxSize == buffer.size();
    }

    public int getNumberOfRequests() {
        return buffer.size();
    }

    public void clear() {
        buffer.clear();
    }
    
    public boolean isEmpty() {
        return buffer.isEmpty();
    }
}
