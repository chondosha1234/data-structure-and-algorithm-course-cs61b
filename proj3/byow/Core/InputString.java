package byow.Core;

public class InputString implements Input{
    private String input;
    private int index;

    public InputString(String s){
        input = s;
        index = 0;
    }

    /** returns next character in the input string, always converts to lower case for
     * convenience in matching
     * @return  next char in input string
     */
    @Override
    public char getNextKey() {
        char next = input.charAt(index);
        index += 1;
        return Character.toLowerCase(next);
    }

    /** returns true if there is another character in input string to read
     *
     * @return t/f
     */
    @Override
    public boolean possibleNextInput() {
        return index < input.length();
    }


}
