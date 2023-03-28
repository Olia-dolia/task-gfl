package liakh.olga.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EquationTest {

    @Test
    void oddNumberOfBrackets(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Equation e = new Equation();
            e.checkBrackets("7*(7-5*x)-(2=0");
        });
    }

}