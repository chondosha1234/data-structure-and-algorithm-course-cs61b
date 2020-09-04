package byow.Core;

public class InputString implements Input{
    private String input;
    private int index;

    public InputString(String s){
        input = s;
        index = 0;
    }

    @Override
    public char getNextKey() {
        char next = input.charAt(index);
        index += 1;
        return next;
    }

    @Override
    public boolean possibleNextInput() {
        return index < input.length();
    }


}
