import java.util.*;
import java.awt.Color;

public class read {

    public static void main(String[] args) {
        read readme = new read("/newphoto.png");
        System.out.println(readme.extractInformation());
    }

    private Picture picture;
    private LinkedList<point> pointsList;
    private LinkedList<String> hiddenInfo;
    private static final int LSB_CONST = 2;

    /* Constructor */
    public read(String pictureLocation) {
        if (pictureLocation.equals(null) || pictureLocation.equals(""))
            throw new IllegalArgumentException("picture location input is not valid");
        this.pointsList = new LinkedList<point>();
        this.picture = new Picture(pictureLocation);
        this.hiddenInfo = new LinkedList<String>();
    }

    /**
     * collects a string from the lsb of an image*/

    /**In english, only produce a char when there is enough elements in hiddenInfo to produce
     * a char. Sometimes hiddenInfo will only contain 1-6 bits of information.
     * Because a pixel only gives 3 lsbs of 2 bits each, and a char is 8 bits,
     * there is going to be moments we cant extract a char.**/
    protected String extractInformation() {
        generatePoints(); //create points order to read from linearly
        String returnedInfo = ""; //create an empty string that we will be using to return chars
        point currentPoint;
        BinaryChar currentBinaryChar;

        /** See method comment for explanation**/
        while (pointsList.peek() != null) {
            currentPoint = pointsList.pop();
            addColorToHiddenInfo(picture.get(currentPoint.getX(), currentPoint.getY()));

            if (hiddenInfo.size() % (8 / LSB_CONST) == 0) { /** Only add char when we can fully pop a char out **/
                currentBinaryChar = new BinaryChar(hiddenInfo.pop() + hiddenInfo.pop()
                                                  + hiddenInfo.pop() + hiddenInfo.pop());

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
                pointsList.add(new point(x, y));
        return true;
    }
}
