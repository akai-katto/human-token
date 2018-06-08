import java.util.ArrayList;
import java.util.Arrays;

public class BinaryChar extends Binary{


    //main for debugging
    public static void main(String[] args){
        BinaryChar cbin = new BinaryChar(' ');
        System.out.println(cbin.toString());
        ArrayList<String> binarySplit = cbin.splitBinary(2);
        System.out.println(binarySplit);
        System.out.println(cbin.charVal);
    }

    protected char charVal;
    static final int charBITS = 8;

    public BinaryChar(char input){
        super(charBITS,"nnnnnnnn"); //create null for now (super must be first statement, java quirk)
        this.charVal = input;

        String binaryVal = Integer.toString(input, 2); //convert to base 2

        if (binaryVal.length() < charBITS) { //fills in rest of bits with zero to get appropriate binary
            String zeros = "";
            for (int a = 0; a < charBITS - binaryVal.length(); a++)
                zeros += "0";
            binaryVal = zeros + binaryVal;
        }

        super.binaryStr = binaryVal;
        if (Integer.parseInt(super.binaryStr, 2) != (int) this.charVal)
            throw new NumberFormatException("Binary does not equal inputted value");
    }

    public BinaryChar(String binary){
        super(charBITS,binary);
        int intVal = Integer.parseInt(binary, 2);
        charVal = (char) intVal;
    }

    @Override
    public String toString() {
        return Character.toString(charVal);
    }

    public char getChar(){
        return charVal;
    }


    /**
     * see stackoverflow.com/questions/1085083/regular-expressions-in-c-examples for regex
     * explanation of
     *"(?<=\\G..)"
     */
    public ArrayList<String> splitBinary(int chunksOf){
        if(chunksOf <= 0 || chunksOf > charBITS)
            throw new IllegalArgumentException("Illegal argument: chunks either non zero or bigger than the LSB");
        if(this.bits%chunksOf!=0)
            throw new IllegalArgumentException("Illegal argument: chunks cannot have remainder");
        super.throwIfNull();

        String regex = "(?<=\\G";

        for(int x = 0; x < chunksOf; x++)
            regex+=".";
        regex+=")";
        return new ArrayList<String>(Arrays.asList(this.binaryStr.split(regex)));
    }
//stubbed
}
