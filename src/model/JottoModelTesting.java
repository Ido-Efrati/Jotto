package model;
import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;
public class JottoModelTesting {
    private JottoModel JM = new JottoModel();

    /*
     * Testing strategy
     * Test for valid puzzles with valid inputs this will include:
     * Perfect guess, wrong guess, some letters in right place, some right letters but not in right place
     * Test for invalid inputs.
     * wrong format, invalid puzzle number, word that is too long or too short, capitel letters
     */
    @Test
    //Test a perfect guess
    public void perfectGuess() {
        assertEquals("guess 5 5",JM.makeGuess(BigInteger.valueOf(16952), "cargo"));  
    }
    
    public void delay() {
        assertEquals("guess 5 5",JM.makeGuess(BigInteger.valueOf(16952), "cargo"));  
    }

    
    @Test
    //Test a guess that has nothing to do with the word
    public void wrongGuess() {
        assertEquals("guess 0 0",JM.makeGuess(BigInteger.valueOf(16952), "fleet"));  
    }
    
    @Test
    //Test for some right letters in the right place
    public void someRightInPlaceGuess() {
        assertEquals("guess 1 1",JM.makeGuess(BigInteger.valueOf(16952), "birds"));  
    }
    
    @Test
    //Test for write letter but in the wrong place
    public void someRightNotInPlaceGuess() {
        assertEquals("guess 1 0",JM.makeGuess(BigInteger.valueOf(16952), "plays"));  
    }

    @Test
    //Test for an invalid word- too short
    public void guessToShort() {
        assertEquals("Invalid guess.",JM.makeGuess(BigInteger.valueOf(16952), "play"));  
    }

    @Test
    //Test for an invalid word- too long
    public void guessToLong() {
        assertEquals("Invalid guess.",JM.makeGuess(BigInteger.valueOf(16952), "playing"));  
    }
    
    @Test
    //Test for an invalid word- no such word
    public void NotAValidword() {
        assertEquals("Invalid guess.",JM.makeGuess(BigInteger.valueOf(16952), "bobly"));  
    }

    @Test
    // invalid puzzle number- positive integer
    public void NonValidPuzzleNumber() {
        assertEquals("Invalid puzzle ID.",JM.makeGuess(BigInteger.valueOf(0), "tests"));  
    }
    
    @Test
    // invalid puzzle number- negative integer
    public void NonValidPuzzleNumberABC() {
        assertEquals("Invalid puzzle ID.",JM.makeGuess(BigInteger.valueOf(-5555), "tests"));  
    }
 
    @Test
    //trying to guess without a guess
    public void WrongFormat() {
        assertEquals("Ill-formatted URL.",JM.makeGuess(BigInteger.valueOf(5), ""));  
    }
    
    @Test
    //Test for an upper case  word- this addressing a code review comment
    public void upperCase() {
        assertEquals("Invalid guess.",JM.makeGuess(BigInteger.valueOf(16952), "CARGO"));  
    }

    @Test
    //Test for an space in word-this addressing a code review comment
    public void spaceInWord() {
        assertEquals("Invalid guess.",JM.makeGuess(BigInteger.valueOf(16952), "car go"));  
    }
    @Test
    //Test for an space between words-this addressing a code review comment
    public void spaceBetweenWords() {
        assertEquals("Invalid guess.",JM.makeGuess(BigInteger.valueOf(16952), "cargo beans"));  
    }



}
