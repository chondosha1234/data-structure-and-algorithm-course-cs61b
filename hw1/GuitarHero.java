import es.datastructur.synthesizer.GuitarString;

public class GuitarHero {

    public static void main(String[] args){

        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        GuitarString[] notes = new GuitarString[37];   // 37 notes on keyboard
        for (int i = 0; i < 37; i++){
            notes[i] = new GuitarString(440 * Math.pow(2, ((i-24)/12)));
        }

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                notes[index].pluck();
            }

            /* compute the superposition of samples */
            double sample = 0.0;
            for (int index = 0; index < 37; index++){
                sample += notes[index].sample();
            }


            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int index = 0; index < 37; index++){
                notes[index].tic();
            }

        }
    }
}
