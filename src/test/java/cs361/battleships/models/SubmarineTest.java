package cs361.battleships.models;

import org.junit.Assert;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class SubmarineTest {

    //Testing to make sure the kind is converted to uppercase and submerged is initialized to true, this is a unique init for this extended class
    @Test
    public void testSubmarineSubmergedInit(){
        Submarine tester = new Submarine("submarine",true);
        String answer = "SUBMARINE";
        assertTrue(tester.getKind().equals("SUBMARINE"));
        assertTrue(tester.submerged);
        //this should convert the kind to uppercase and test the submerged flag to false.
        //this is how the submerged flag is passed through the front end
        Submarine testerB = new Submarine("submarine");
        assertTrue(testerB.getKind().equals("SUBMARINE"));
        assertFalse(testerB.submerged);
    }

    //This class is unique in that it has 5 occupied squares, I want to make sure that the loop for setting those squares generates all 5
    @Test
    public void testSquaresCount(){
        Submarine tester = new Submarine("SUBMARINE");
        tester.populateSquares(5,'E',true);
        assertTrue(tester.getOccupiedSquares().size() == 5);
    }
}