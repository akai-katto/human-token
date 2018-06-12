package stego;

import java.util.LinkedList;
import java.awt.Color;
import java.util.Random;


public class StegoWrite {

    //for debugging
    public static void main(String[] args) {

        StegoWrite writeme = new StegoWrite("日本語をはなしませんですか",
                "./pictures/picture.png");
        writeme.writeInformationToNewImage();
        writeme.picture.show();
    }

    private static final int LSB_CONST = 2; //in this program we're only working with 2 LSB. feel free to modify
    private Picture picture;
    private LinkedList<ImagePoint> pointsList;
    private LinkedList<String> allBits;
    private Random random;
    private String input;
    private static final int charBits = 16;

    public StegoWrite(String input, String imageLocation) {
        if (input.equals("") || input.equals(null))
            throw new IllegalArgumentException("constructor argument is null");
        if (imageLocation.equals("") || imageLocation.equals(null))
            throw new IllegalArgumentException("constructor argument is null");

        this.input = input;
        this.pointsList = new LinkedList<ImagePoint>();
        this.random = new Random();
        this.picture = new Picture(imageLocation);
        this.allBits = new LinkedList<String>();
    }

    /**
     * A validation system for the function to call when words needs to be set.
     * Ensures that there is enough space and allBits (the binary bits) we will be using
     * is empty.
     */
    private boolean setWords() { //pre-recs for generating binary bits. Consider combining functions in future
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

        BinaryChar escChar = new BinaryChar("0000000000011011"); //escape char.
        returnedList.addAll(escChar.splitBinary(LSB_CONST)); //knows when to stop reading

        for (int a = returnedList.size(); a < (picture.height() * picture.width()) * 3; a++)
            returnedList.add(getRandomBinary());

        return returnedList;
    }

    /**
     * For validating user input.
     * Equation is (length * 16 + 16) < (b*h * const * 3)
     * 16*length because each char takes 16 bits, plus exit char.
     * 3 because 3 colors per pixel
     */
    public boolean checkEnoughSpace() {
        if (((input.length() * charBits) + charBits) > (picture.height() * picture.width() * LSB_CONST * 3)) // 3 different pixels.
            return false;
        return true;
    }

    public void showPicture() {
        this.picture.show();
    }


    /**
     * given a stego.ImagePoint (xn,yn) for some n, change the lsb of the colors
     * of that stego.ImagePoint by popping allBits (a list consisting of all the binary
     * that will be written into the image, with respect to LSB_CONST.
     */
    public void writeInformationToNewImage() {
        setWords();
        generatePoints(); //generate points that we will be using to stego.StegoWrite information
        ImagePoint currentImagePoint;
        for (int a = 0; a < allBits.size(); a++) {
            currentImagePoint = pointsList.pop();

            picture.set(currentImagePoint.getX(), currentImagePoint.getY(),
                    changeColor(picture.get(currentImagePoint.getX(),
                            currentImagePoint.getY()), allBits.remove(0),
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
     * Allows the developer to modify the points in which the program will stego.StegoWrite in
     * Creates a (x1,y1), (x1,y2)...(xn,yn) linear mapping.
     */
    private void generatePoints() {
        pointsList.clear(); // make sure linkedlist is clear
        for (int x = 0; x < picture.width(); x++)
            for (int y = 0; y < picture.height(); y++)
                pointsList.add(new ImagePoint(x, y));

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
