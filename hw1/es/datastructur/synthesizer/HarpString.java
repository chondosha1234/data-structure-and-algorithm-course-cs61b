package es.datastructur.synthesizer;

public class HarpString extends GuitarString {

    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    public HarpString(double frequency) {
        super(frequency);
        int capacity = (int) Math.round(SR / frequency);
        buffer = new ArrayRingBuffer(capacity);
        for (int i = 0; i < capacity; i++){
            buffer.enqueue(0.0);
        }
    }

    @Override
    public void tic(){
        double first = buffer.dequeue();
        double second = buffer.peek();
        double newSample = DECAY * ((first + second) / 2);
        buffer.enqueue(newSample);
    }
}
