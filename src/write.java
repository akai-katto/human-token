import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.Color;
import java.util.Random;


public class write {

    //for debugging
    public static void main(String[] args) {
        write writeme = new write("it seems stuff is not working correctly",
                "/profile.png");
        writeme.writeInformationToNewImage();
        writeme.picture.show();
    }

    private static final int LSB_CONST = 2; //in this program we're only working with 2 LSB. feel free to modify
    private Picture picture;
    private LinkedList<point> pointsList;
    private LinkedList<String> allBits;
    private Random random;
    private String input;

    write(String input, String imageLocation) {
        if (input.equals("") || input.equals(null))
            throw new IllegalArgumentException("constructor argument is null");
        if (imageLocation.equals("") || imageLocation.equals(null))
            throw new IllegalArgumentException("constructor argument is null");

        this.input = input;
        this.pointsList = new LinkedList<point>();
        this.random = new Random();
        this.picture = new Picture(imageLocation);
        this.allBits = new LinkedList<String>();
    }

    /**
     * A validation system for the function to call when words needs to be set.
     * Ensures that there is enough space and allBits (the binary bits) we will be using
     * is empty.
     */
    private boolean setWords(){ //pre-recs for generating binary bits. Consider combining functions in future
        if (!checkEnoughSpace())
            throw new IllegalArgumentException("not enough space in image");
        if (!allBits.isEmpty())
            allBits.clear();

        allBits = generateBinaryBits(input);
        return true;
    }
    /**
     * Given a string, convert it to binary bits in chunks LSB_CONST.
     * After it is done splitting the string, add an escape character,
     * then fill in the remainder with random binary to keep the artificats of an image
     * consistant
     */
    private LinkedList<String> generateBinaryBits(String message) {
        LinkedList<String> returnedList = new LinkedList<String>();
        String splitMessage[] = message.split("");

        for (int a = 0; a < splitMessage.length; a++) {
            BinaryChar charBin = new BinaryChar(splitMessage[a].charAt(0));
            returnedList.addAll(charBin.splitBinary(LSB_CONST));
        }

        BinaryChar escChar = new BinaryChar("00011011"); //escape char.
        returnedList.addAll(escChar.splitBinary(LSB_CONST)); //knows when to stop reading

        for (int a = returnedList.size(); a < (picture.height() * picture.width()) * 3; a++)
            returnedList.add(getRandomBinary());

        return returnedList;
    }
    /**
     * For validating user input.
     * Equation is (length * 8 + 8) < (b*h * const * 3)
     * 3 because 3 colors per pixel
     */
    protected boolean checkEnoughSpace() {
        if ((input.length() * 8 + 8) > (picture.height() * picture.width() * LSB_CONST * 3)) // 3 different pixels.
            return false;
        return true;
    }

    protected void showPicture(){
        this.picture.show();
    }


    /**
     * given a point (xn,yn) for some n, change the lsb of the colors
     * of that point by popping allBits (a list consisting of all the binary
     * that will be written into the image, with respect to LSB_CONST.
     */
    protected void writeInformationToNewImage() {
        setWords();
        generatePoints(); //generate points that we will be using to write information
        point currentPoint;
        for (int a = 0; a < allBits.size(); a++) {
            currentPoint = pointsList.pop();

            picture.set(currentPoint.getX(), currentPoint.getY(),
                    changeColor(picture.get(currentPoint.getX(),
                            currentPoint.getY()), allBits.remove(0),
                            allBits.remove(0), allBits.remove(0)));
        }

    }


    /**
     * get random binary bits to fill in the rest of the pixels.
     */
    private String getRandomBinary() {
        return (random.nextInt(LSB_CONST) + "" + (random.nextInt(LSB_CONST)));
    }

    /**
     * Allows the developer to modify the points in which the program will write in
     * Creates a (x1,y1), (x1,y2)...(xn,yn) linear mapping.
     */
    private void generatePoints() {
        pointsList.clear(); // make sure linkedlist is clear
        for (int x = 0; x < picture.width(); x++)
            for (int y = 0; y < picture.height(); y++)
                pointsList.add(new point(x, y));

    }

    /**
     * accepts a color, and three different binary representations in the form of a string.
     * Changes the LSB binary bits of the color respectively.
     */
    private Color changeColor(Color changedColor, String val1, String val2, String val3) {
        BinaryInt red = new BinaryInt(changedColor.getRed());
        BinaryInt green = new BinaryInt(changedColor.getGreen());
        BinaryInt blue = new BinaryInt(changedColor.getBlue());

        red.changeLSB(LSB_CONST, val1);
        green.changeLSB(LSB_CONST, val2);
        blue.changeLSB(LSB_CONST, val3);

        return new Color(red.getNumericalVal(), green.getNumericalVal(), blue.getNumericalVal());
    }
}
