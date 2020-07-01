import org.junit.Test;
import static org.junit.Assert.*;

public class TestArrayDequeGold {
    @Test
    public void testStudentDeque(){
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<>();

        for (int k = 0; k < 50; k++) {
            Integer x = (int) StdRandom.uniform();
            sad.addFirst(x);
            solution.addFirst(x);
            Integer sadAns = sad.removeFirst();
            Integer solAns = solution.removeFirst();
            assertEquals(sadAns, solAns);
        }
    }
}
