import java.util.ArrayList;
import java.util.Arrays;

public abstract class Binary {

//for debugging, remove "abstract" to test individual features of binary
/*    public static void main(String[] args) {
        Binary binary1 = new Binary(8, "00000000");

        System.out.println(binary1);
        binary1.changeLSB(2,"11");

        System.out.println(binary1);
    }*/


    protected int bits;
    protected String binaryStr;

    /**
     * Note the exception for 'n' and why 'n' is always in inherited objects:
     * The first call in an inherited object needs to be the super constructer. As a consequence,
     * we cannot first parse the input (in some cases a char, in some cases an int) into a set of valid
     * binary strings. So super is called with 'nnnnnn' for a set amount of n's, then is replaced later.
     */
    public Binary(int bits, String binaryStr) {
        if (binaryStr.length() != bits)
            throw new IllegalArgumentException("invalid argument, binary " +
                    "representation must be same length as bits");
        if (!binaryStr.matches("[01n]+")) //regex exp for only 0 1 and n
            throw new IllegalArgumentException("Illegal binary expression");

        this.bits = bits;
        this.binaryStr = binaryStr;
    }


    /**
     * to be overloaded in inheritance
     */
    public String toString() {
        return binaryStr;
    }


    /**
     * Given one binary object and another, replace the LSB of the given binary with
     * another binary object
     *
     * @param otherBinary must be smaller than the given binary.
     */
    protected void changeLSB(Binary otherBinary) {
        if (otherBinary.getBits() >= this.bits)
            throw new IllegalArgumentException("illegal binary operation, otherbits size:  " + otherBinary.getBits()
                    + ". This.binary size: " + this.bits + ". other must be smaller");
        if (otherBinary == null)
            throw new IllegalArgumentException("null argument");
        throwIfNull();

        this.binaryStr = this.binaryStr.substring(0, bits - otherBinary.getBits());
        this.binaryStr += otherBinary.getBinaryStr();

        if (this.binaryStr.length() != bits) //validate that operation is legal
            throw new ArithmeticException("LSB changed does not equal to original binary");
    }


    /**
     * Given another set of bits and its representation, replace the LSB of this binary
     * i.e, 1111111 -> lsb(2,00) -> 11111100
     */
    protected void changeLSB(int otherBits, String otherBinary) {
        if (otherBits >= this.bits || otherBinary.length() > this.bits)
            throw new IllegalArgumentException("illegal binary operation, otherbits size:  " + otherBits
                    + ". This.binary size: " + this.bits + ". other must be smaller");
        if (otherBinary.length() != otherBits)
            throw new IllegalArgumentException("bits does not equate to length of the binary");
        if (otherBinary.equals("") || otherBinary == null)
            throw new IllegalArgumentException("null argument");
        throwIfNull();

        this.binaryStr = this.binaryStr.substring(0, bits - otherBits);
        this.binaryStr += otherBinary;

        if (this.binaryStr.length() != bits) //validate that operation is legal
            throw new ArithmeticException("LSB changed does not equal to original binary");
    }

    protected int getBits() {
        return bits;
    }

    protected String getBinaryStr() {
        return binaryStr;
    }


    /**
     * Gets the least significant bit of a binary string
     * see https://en.wikipedia.org/wiki/Bit_numbering
     *
     * @param lsbBits
     * @return
     */
    protected String getLSB(int lsbBits) {
        throwIfNull();
        if (this.binaryStr.length() < lsbBits)
            throw new IllegalArgumentException("trying to retrieve lsb of a binary " +
                    "that is bigger than the binary itself");

        return this.binaryStr.substring(this.bits - lsbBits, bits);

    }

    /**
     * Splits a binaryString into discrete chunks (i.e 8 bits, input 4, will divide it into 4 unique chunks of 2 bits.
     * Used for LSB where bits are divided between different pixels.
     *
     * see stackoverflow.com/questions/1085083/regular-expressions-in-c-examples for regex
     * explanation of
     *"(?<=\\G..)"
     */
    protected ArrayList<String> splitBinary(int chunksOf){
        if(chunksOf <= 0 || chunksOf > this.bits)
            throw new IllegalArgumentException("Illegal argument: chunks either non zero or bigger than the LSB");
        if(this.bits%chunksOf!=0)
            throw new IllegalArgumentException("Illegal argument: chunks cannot have remainder");
        throwIfNull();

        String regex = "(?<=\\G";

        for(int x = 0; x < chunksOf; x++)
            regex+=".";
        regex+=")";
        return new ArrayList<String>(Arrays.asList(this.binaryStr.split(regex)));
    }
    /**
     *We need to maintain the appropriate amount of digits when working with binary. For example,
     * 52 is 110100. An integer contains 8 bits, so it needs be 00110100 instead.
     *
     * another example is 5 being 101, needs to be 00000101.
     */
    protected String fillZeros(String binaryArg){
        if (binaryArg.length() < this.bits) {
            String zeros = "";
            for (int a = 0; a < this.bits - binaryArg.length(); a++)
                zeros += "0";
            binaryArg = zeros + binaryArg;
        }

        return binaryArg;
    }

    /**
     * easy function to call to see if binaryStr that was instantiated is valid or not.
     */
    protected void throwIfNull() {
        if (this.binaryStr.contains("n"))
            throw new IllegalArgumentException("binary representation is null (contain's n's)");
    }
}