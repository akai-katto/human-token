import java.util.ArrayList;
import java.util.Arrays;

public class BinaryChar extends Binary{


    //test cases
    public static void main(String[] args){
        BinaryChar cbin1 = new BinaryChar('a');
        BinaryChar cbin2 = new BinaryChar('7');

        if(cbin1.getChar() != 'a')
            System.out.print("test case 1 failed");

        if(! cbin1.binaryStr.equals("01100001"))
            System.out.print("test case 1 binary not equal");

        if(cbin2.getChar() != '7')
            System.out.print("test case 2 failed");

        if(! cbin2.binaryStr.equals("00110111"))
            System.out.print("test case 2 binary not equal");

        cbin2.changeLSB(2,"00");
        if(! cbin2.binaryStr.equals("00110100"))
            System.out.print("change LSB not working");

    }

    protected char charVal;
    static final int charBITS = 8;

    public BinaryChar(char input){
        super(charBITS,"nnnnnnnn"); //create null for now (super must be first statement, see super constructer)
        this.charVal = input;

        String binaryVal = Integer.toString(input, 2); //convert to base 2
        binaryVal = super.fillZeros(binaryVal);

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


}
