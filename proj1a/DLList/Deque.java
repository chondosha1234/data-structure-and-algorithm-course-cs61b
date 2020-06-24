public interface Deque<genType> {

    void addFirst(genType item);

    void addLast(genType item);

    default boolean isEmpty(){
        return size() == 0;
    }

    int size();

    void printDeque();

    genType removeFirst();

    genType removeLast();

    genType get(int index);

}
