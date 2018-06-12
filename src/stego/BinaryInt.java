package stego;

public class BinaryInt extends Binary {


    //test cases / debugging
    public static void main(String[] args) {
        BinaryInt binInt1 = new BinaryInt("00110111"); //55

        if (binInt1.getNumericalVal() != 55)
            System.out.print("test case 1 failed");

        binInt1.changeLSB(2, "00"); //move 3 places down relative to 55

        if (binInt1.getNumericalVal() != 52)
            System.out.print("test case 1 LSB failed");

    }

    static final int intBITS = 8;
    protected int numericalVal;

    public BinaryInt(int input) {
        super(intBITS, "nnnnnnnn"); //create null for now (super must be first statement, java quirk)

        if (input < 0)
            throw new IllegalArgumentException("no negative integers for stego.Binary Int");

        String binaryVal = Integer.toString(input, 2); //convert to base 2
        binaryVal = super.fillZeros(binaryVal); //fill in zeros where appropriate

        super.binaryStr = binaryVal;
        this.numericalVal = input;

        if (Integer.parseInt(super.binaryStr, 2) != this.numericalVal)
            throw new NumberFormatException("stego.Binary does not equal inputted value");

    }

    /**
     * Create a stego.BinaryInt object from a binary
     */
    public BinaryInt(String binary) {
        super(intBITS, binary);

        this.numericalVal = Integer.parseInt(binary, 2);
        //put a throw here
    }

    public int getNumericalVal() {
        return numericalVal;
    }

    @Override
    public String toString() {
        return Integer.toString(numericalVal);
    }


    //updates numerical value in binaryInt when lsb is called
    @Override
    public void changeLSB(int otherBits, String otherBinary) {
        super.changeLSB(otherBits, otherBinary);
        this.numericalVal = Integer.parseInt(super.binaryStr, 2);
    }

    //updates numerical value of binaryInt when lsb is called
    @Override
    public void changeLSB(Binary otherBinary) {
        super.changeLSB(otherBinary);
        this.numericalVal = Integer.parseInt(super.binaryStr, 2);
    }

}
