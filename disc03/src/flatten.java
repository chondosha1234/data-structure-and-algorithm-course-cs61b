public class flatten {

    /** takes a 2 dimensional array and 'flattens' it into a one dimensional array
     * with all the elements of the inner arrays
     */
    public static int[] flatten(int[][] x){
        int totalLength= 0;
        for (int i = 0; i < x.length; i++){   //cycle through the first array of arrays
            totalLength += x[i].length;      // adds up length of each inner array to find length of final array
        }

        int[] a = new int[totalLength];   //make a new array to return
        int aIndex = 0;

        for(int j = 0; j < x.length; j++){     //cycle through first array like before
            for (int k = 0; k < x[j].length; k++){   // cycle through each inner array
                a[aIndex] = x[j][k];                   //set the value in final array to element from inner array
                aIndex += 1;
            }
        }

        return a;
    }
}
