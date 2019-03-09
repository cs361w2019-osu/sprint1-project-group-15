package cs361.battleships.models;

import org.junit.Assert;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class SquareTest {

    @Test
    public void testSquareRows(){
        Square tester = new Square();
        for(int i = 0; i<=10; i++) {
            tester.setRow(i);
            assertTrue(tester.getRow() == i);
        }
    }

    @Test
    public void testSquareCols(){
        Square tester = new Square();
        for(int i = 63; i<=74; i++) {
            tester.setColumn((char)i);
            assertTrue(tester.getColumn() == (char)i);
        }
    }

    @Test
    public void testSquareInit(){
        Square tester = new Square(5,'e');
        assertTrue(tester.getRow()==5);
        assertTrue(tester.getColumn()=='e');
    }

}