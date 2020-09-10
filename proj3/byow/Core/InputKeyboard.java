package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

public class InputKeyboard implements Input{

    /** returns next key pressed.
     * while loop is infinite, waits for user to press a key before returning and continuing program
     *
     * @return   character of key pressed
     */
    @Override
    public char getNextKey() {
        while(true) {
            if (StdDraw.hasNextKeyTyped()) {
                char input = Character.toLowerCase(StdDraw.nextKeyTyped());
                return input;
            }
        }
    }

    @Override
    public boolean possibleNextInput() {
        return true;
    }
}
