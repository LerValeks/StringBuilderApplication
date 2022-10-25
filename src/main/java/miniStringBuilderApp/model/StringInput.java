package miniStringBuilderApp.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import miniStringBuilderApp.exceptions.UserRequestException;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "file_storage")
public class StringInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int minLength;
    private int maxLength;
    private String userInput;
    private String userDistinctInput;
    private BigInteger numberOfUniqueStringsRequested;
    private BigInteger maxDistinctPermutation;
    private String status = "Running";
    private String downloadUrl = "";
    @Transient
    @JsonIgnore
    private Set<String> stringPermutation;
    @Lob
    @JsonIgnore
    private String stringPermutationFile;


    public StringInput(int minLength, int maxLength, String userInput, BigInteger numberOfUniqueStringsRequested) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.userInput = userInput;
        this.numberOfUniqueStringsRequested = numberOfUniqueStringsRequested;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserDistinctInput(String userInput) {
        String input = userInput.chars()
                .mapToObj(e -> (char) e)
                .collect(Collectors.toSet())
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
        this.userDistinctInput = input;
    }


    public void setStringPermutationFile() {
        this.stringPermutationFile = String.join(lineSeparator(), stringPermutation);
    }

    public void setStringPermutation() {
        setUserDistinctInput(userInput);
        Set<String> treeSet = new TreeSet<>();
        stringPermutation = treeSet;
        permutation(userDistinctInput, "");
    }

    private void permutation(String userInputString, String ans) {

        if (BigInteger.valueOf(stringPermutation.size()).compareTo(numberOfUniqueStringsRequested) == 0) {
            return;
        }

        // If string is empty stop permutation
        if (userInputString.length() == 0) {
            if (ans.length() > maxLength || ans.length() < minLength) {
                return;
            }
            stringPermutation.add(ans);
            return;
        }
        for (int i = 0; i < userInputString.length(); i++) {
            // ith character of str
            char ch = userInputString.charAt(i);
            // Rest of the string after excluding
            // the ith character
            String ros = userInputString.substring(0, i) +
                    userInputString.substring(i + 1);

            permutation(ros, ans + ch);
        }
        for (int i = 0; i < userInputString.length(); i++) {

            // Rest of the string after excluding
            // the ith character
            String ros = userInputString.substring(0, i) +
                    userInputString.substring(i + 1);
            // Recursive call
            permutation(ros, ans);
        }
    }

    //Factorial function
    private BigInteger fact(long a) {

        BigInteger f = BigInteger.valueOf(1);
        for (long i = 2; i <= a; i++)
            f = f.multiply(BigInteger.valueOf(i))  ;
        return f;
    }

    private BigInteger permute(long lengthOfUserInput, long r) throws UserRequestException {
        BigInteger ans = BigInteger.valueOf(0L);
        try {
            ans = (fact(lengthOfUserInput).divide(fact(lengthOfUserInput - r)) );
        } catch (ArithmeticException e) {
            throw new UserRequestException("Input String is too Long to generate unique values. Please provide shorter string or put more non distinct characters");
        }
        return ans;
    }

    // Function to find the total
    // number of combinations possible
    public BigInteger setMaxDistinctPermutation() throws UserRequestException {
        setUserDistinctInput(userInput);
        int lengthOfUserInput = userDistinctInput.length();
        BigInteger sum = BigInteger.valueOf(0L);

        int factorialMaxValue = Math.min(maxLength, lengthOfUserInput);

        for (int r = minLength; r <= factorialMaxValue; r++) {
            //ToDO catch case when BigInteger become negative BigInteger by substrcting curren Sum from BigIntegerMax and than compare result with new permutation. If new Permutationis bigger than trhow arror

            BigInteger incrementalNumber = permute(lengthOfUserInput, r);
            if (BigInteger.valueOf(9223372036854775807l).subtract(sum).compareTo(incrementalNumber)==-1  ){
                throw new UserRequestException("Provided Input String is to huge to generate unique value. Please provide shorter string");
            }
            sum = sum.add(incrementalNumber);

        }
        return this.maxDistinctPermutation = sum;
    }

    public void setdownloadUrl() {
        if (status != "Running") {
            downloadUrl = "http://localhost:8081/api/v1/files/" + id;
        }
    }


}
