public class BinaryInt extends Binary {


    public static void main(String[] args) {
        BinaryInt binInt1 = new BinaryInt("00110111");
        String lsb = binInt1.getLSB(2);
        System.out.println(lsb);

    }

    static final int intBITS = 8;
    protected int numericalVal;

    public BinaryInt(int input) {
        super(intBITS, "nnnnnnnn"); //create null for now (super must be first statement, java quirk)

        if (input < 0)
            throw new IllegalArgumentException("no negative integers for Binary Int");

        String binaryVal = Integer.toString(input, 2); //convert to base 2

        if (binaryVal.length() < intBITS) { //fills in rest of bits with zero to get appropriate binary (e.g 111 = 0000111)
            String zeros = "";
            for (int a = 0; a < intBITS - binaryVal.length(); a++)
                zeros += "0";
            binaryVal = zeros + binaryVal;
        }

        super.binaryStr = binaryVal;
        numericalVal = input;

        if (Integer.parseInt(super.binaryStr, 2) != this.numericalVal)
            throw new NumberFormatException("Binary does not equal inputted value");

    }

    /**
    Create a BinaryInt object from a binary
     */
    public BinaryInt(String binary){
        super(intBITS, binary);

        numericalVal = Integer.parseInt(binary, 2);
        //put a throw here
    }

    public int getNumericalVal(){
        return numericalVal;
    }

    @Override
    public String toString() {
        return Integer.toString(numericalVal);
    }


    //updates numerical value in binaryInt when lsb is called
    @Override
    public void changeLSB(int otherBits, String otherBinary){
        super.changeLSB(otherBits,otherBinary);
        this.numericalVal = Integer.parseInt(super.binaryStr, 2);
    }

    //updates numerical value of binaryInt when lsb is called
    @Override
    public void changeLSB(Binary otherBinary) {
        super.changeLSB(otherBinary);
        this.numericalVal = Integer.parseInt(super.binaryStr, 2);
    }

}
