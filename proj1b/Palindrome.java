public class Palindrome{

    /** returns a Deque object from a word. Each letter being an element in the DLList */
    public Deque<Character> wordToDeque(String word){
        LinkedListDeque<Character> DLList = new LinkedListDeque<>(word.charAt(0));
        int i;
        for (i = 1; i < word.length(); i+=1){
            DLList.addLast(word.charAt(i));
        }
        return DLList;
    }

    /** returns a Deque object from a word, each letter being an element in an AList */
    public Deque<Character> wordToDequeArray(String word) {
        ArrayDeque<Character> AList = new ArrayDeque<>(word.charAt(0));
        int j;
        for (j = 1; j < word.length(); j+=1){
            AList.addLast(word.charAt(j));
        }
        return AList;
    }

    /** takes a word and returns true if it is a palindrome, that is, it is spelled the same forward
     * and backward
     */
    public boolean isPalindrome(String word){
        if (word == ""){
            return true;
        }

        Deque d = wordToDeque(word);
        String backward = "";

        for (int i = 0; i < word.length(); i++){
            backward += d.removeLast();
        }
        return word.equals(backward);
    }

    /** second isPalindrome method which takes a characterComparator object
     * it shows if word is a palindrome according to the CC
     * for example if we use OffByOne, the word "flake" is an offbyone palindrome
     * because 'f' and 'e' are off by one, and 'l' and 'k' also
     */
    public boolean isPalindrome(String word, CharacterComparator cc){
        Deque d = wordToDeque(word);
        for (int i = 1; i < word.length()/2; i++){
            char first = (char) d.removeFirst();
            char last = (char) d.removeLast();
            if (!cc.equalChars(first, last)){
                return false;
            }
        }
        return true;
    }
}