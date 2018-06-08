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
     * Java Work-Around, if we need to insantiate a binary to be null, it will be n.. for any amount
     * of zzz's needed. Functions are written with a throw if binaryStr contains nnnn. Explains why inherited objects
     * always instantiate "nn"
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
     * 1111111 -> lsb(2,00) -> 11111100
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


    //easy function to call to see if we can work with binaryStr or not
    protected void throwIfNull() {
        if (this.binaryStr.contains("n"))
            throw new IllegalArgumentException("binary representation is null (contain's n's)");
    }


}