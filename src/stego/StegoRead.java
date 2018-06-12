package stego;

import java.util.*;
import java.awt.Color;

public class StegoRead {


    //for debugging
    public static void main(String[] args) {
        StegoRead readme = new StegoRead("./pictures/foriegn.png");
        System.out.println(readme.extractInformation());
    }

    private Picture picture;
    private LinkedList<ImagePoint> pointsList;
    private LinkedList<String> hiddenInfo;
    private static final int LSB_CONST = 2;
    private static final int charBits = 16;

    /* Constructor */
    public StegoRead(String pictureLocation) {
        if (pictureLocation.equals(null) || pictureLocation.equals(""))
            throw new IllegalArgumentException("picture location input is not valid");
        this.pointsList = new LinkedList<ImagePoint>();
        this.picture = new Picture(pictureLocation);
        this.hiddenInfo = new LinkedList<String>();
    }

    /**
     * collects a string from the lsb of an image*/

    /**
     * In english, only produce a char when there is enough elements in hiddenInfo to produce
     * a char. Sometimes hiddenInfo will only contain 1-15 bits of information.
     * Because a pixel only gives 3 lsbs of 2 bits each, and a char is 16 bits, therfore
     * there is going to be moments we cant extract a char.
     * <p>
     * So when there is enough time to extract (charBits / LSB_CONST) worth of bits from
     * hiddenInfo, we use a for loop and create a string of the representation, create a char based
     * on the string, and add the char to the decyphered string.
     **/
    public String extractInformation() {
        generatePoints(); //create points order to stego.StegoRead from linearly
        String returnedInfo = ""; //create an empty string that we will be using to return chars
        ImagePoint currentImagePoint;
        BinaryChar currentBinaryChar;

        /** See method comment for explanation**/
        while (pointsList.peek() != null) {
            currentImagePoint = pointsList.pop();
            addColorToHiddenInfo(picture.get(currentImagePoint.getX(), currentImagePoint.getY()));

            if (hiddenInfo.size() % (charBits / LSB_CONST) == 0) { /** Only add char when we can fully pop a char out **/

                String binaryString = "";
                for (int x = 0; x < charBits / LSB_CONST; x++)
                    binaryString += hiddenInfo.pop();

                currentBinaryChar = new BinaryChar(binaryString);

                if ((int) currentBinaryChar.getChar() == 27) /**If we hit an escape character..**/
                    break;

                returnedInfo += currentBinaryChar.getChar(); /**add current char to a running list of added chars*/
            }
        }
        return returnedInfo;
    }

    /**
     * given a color value, add the LSB of the colors pixels to an array called "hiddenInfo".
     * Notice that we need 4 values in "hiddenInfo" to create a letter, but each color only gives us
     * 3 values
     **/
    private void addColorToHiddenInfo(Color color) {
        if (color == null)
            throw new IllegalArgumentException("Color is null");

        /**Construct binary representations of colors**/
        BinaryInt red = new BinaryInt((color.getRed()));
        BinaryInt green = new BinaryInt((color.getGreen()));
        BinaryInt blue = new BinaryInt((color.getBlue()));

        /** Add lsb's of colors to a list of collected LSBs**/
        hiddenInfo.add(red.getLSB(LSB_CONST));
        hiddenInfo.add(green.getLSB(LSB_CONST));
        hiddenInfo.add(blue.getLSB(LSB_CONST));

    }

    /**
     * generates points to be used when reading
     **/
    private boolean generatePoints() {
        pointsList.clear(); // make sure linkedlist is clear
        for (int x = 0; x < picture.width(); x++)
            for (int y = 0; y < picture.height(); y++)
                pointsList.add(new ImagePoint(x, y));
        return true;
    }
}
