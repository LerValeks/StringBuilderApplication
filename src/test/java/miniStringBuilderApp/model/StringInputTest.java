package miniStringBuilderApp.model;


import miniStringBuilderApp.exceptions.UserRequestException;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static java.lang.System.lineSeparator;
import static org.junit.jupiter.api.Assertions.*;

class StringInputTest {

    StringInput stringInput1;
    StringInput stringInput2;

    StringInput stringInput3;
    StringInput stringInput4;
    StringInput stringInput5;
    StringInput stringInput6;

    @BeforeEach
    void setUp() {
        stringInput1 = new StringInput();
        stringInput2 = new StringInput();
        stringInput3 = new StringInput(1, 16,"aabc", BigInteger.valueOf(7L));
        stringInput4 = new StringInput(1, 4,"asAD12UD", BigInteger.valueOf(16L));
        stringInput5 = new StringInput(1, 4,"ab", BigInteger.valueOf(4L));

        stringInput6 = new StringInput(2, 40,"1234567890qwertyuiopasdfghjk", BigInteger.valueOf(254985L));


    }

    @Test
    @DisplayName("Ensures that only Distinct Characters would be used going forward for permutation")
    public void testUserInputStringWillShowOnlyDistinctCharacters() {


        stringInput1.setUserDistinctInput("aabbbccccdd");
        stringInput2.setUserDistinctInput("111d");


        assertEquals("abcd", stringInput1.getUserDistinctInput(), "Should return only distinct characters");
        assertEquals("1d", stringInput2.getUserDistinctInput(),"Should return only distinct characters");
    }

    @Test
    @DisplayName("Ensures permutation is using properly parameters and providing proper result ")
    public void testProperPermutationFileWillBeGeneratedForGivenStringInput() {


        String correctPermutation1 = ("a" + lineSeparator() +"ab" + lineSeparator()+ "abc" + lineSeparator()+ "ac"+ lineSeparator()+
                "acb"+ lineSeparator()+ "ba"+ lineSeparator()+ "bac");
        String correctPermutation2 = ("aA1"+ lineSeparator() + "aA12"+ lineSeparator() + "aA1D"+ lineSeparator() + "aA1U"+ lineSeparator()
                + "aA1s"+ lineSeparator() + "aA2"+ lineSeparator() + "aA21"+ lineSeparator() + "aA2D"+ lineSeparator() + "aA2U"+ lineSeparator() +
                "aA2s"+ lineSeparator() + "aAD1"+ lineSeparator() + "aAs"+ lineSeparator() + "aAs1"+ lineSeparator() + "aAs2"+ lineSeparator() +
                "aAsD"+ lineSeparator() + "aAsU");
        String correctPermutation3 = ("a"+ lineSeparator() + "ab"+ lineSeparator() + "b"+ lineSeparator() + "ba");


        stringInput3.setStringPermutation();
        stringInput3.setStringPermutationFile();

        stringInput4.setStringPermutation();
        stringInput4.setStringPermutationFile();

        stringInput5.setStringPermutation();
        stringInput5.setStringPermutationFile();

        String testPermutation1 = stringInput3.getStringPermutationFile();
        String testPermutation2 = stringInput4.getStringPermutationFile();
        String testPermutation3 = stringInput5.getStringPermutationFile();

        assertEquals(correctPermutation1,testPermutation1, "Test if proper number of permutations in sorted order will be generated" );
        assertEquals(correctPermutation2,testPermutation2, "Test if proper number of permutations in sorted order will be generated");
        assertEquals(correctPermutation3,testPermutation3, "Test if proper number of permutations in sorted order will be generated");


    }

    @Test
    @DisplayName("Ensures that Max number of permutation will be correctly calculated ")
    public void testMaximumDistinctPermutation() throws UserRequestException {

        stringInput3.setMaxDistinctPermutation();
        stringInput4.setMaxDistinctPermutation();
        stringInput5.setMaxDistinctPermutation();

        BigInteger numberOfPermutation1 = stringInput3.getMaxDistinctPermutation();
        BigInteger numberOfPermutation2 = stringInput4.getMaxDistinctPermutation();
        BigInteger numberOfPermutation3 = stringInput5.getMaxDistinctPermutation();

        assertEquals(BigInteger.valueOf(15L),numberOfPermutation1, "Check if correct max number of permutations can be generated" );
        assertEquals(BigInteger.valueOf(1099L),numberOfPermutation2, "Check if correct max number of permutations can be generated");
        assertEquals(BigInteger.valueOf(4L),numberOfPermutation3, "Check if correct max number of permutations can be generated");

    }

    @Test()
    @DisplayName("Ensures that exception is thrown when StringInput is too long")
    public void WhenStringInputToLong_ThenThrowAnException() throws UserRequestException {

        assertThrows(UserRequestException.class,
                ()->   {stringInput6.setMaxDistinctPermutation();
        });


    }




}