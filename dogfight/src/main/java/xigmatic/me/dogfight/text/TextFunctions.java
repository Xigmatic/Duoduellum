package xigmatic.me.dogfight.text;

import java.util.Arrays;

public class TextFunctions {
    public TextFunctions() {}


    /**
     * Function used for getting a number of empty chat pixels as a string
     * @param numPixels Number of empty pixels
     * @return String of characters that represent empty pixels
     */
    public static String getPixelsAsString(int numPixels) {
        StringBuilder result = new StringBuilder();

        // Adds pixels until no more are needs
        while(numPixels > 0) {
            // Adds 20 until the total number of pixels needed is less than 20
            if(numPixels >= 20) {
                result.append("⒇");

                numPixels -= 20;

            // Add 1-19 pixels by using code for the ⑳ character (just before the ⑴ character)
            } else {
                result.append(Character.toChars(9331 + numPixels));

                numPixels = 0;
            }
        }

        return result.toString();
    }


    /**
     * Adds empty pixels before or after an input string to set it to a certain width
     * @param inputString String to be width-corrected
     * @param pixelWidth number of pixels the TOTAL string will eventually be
     * @param addBefore Determines if the empty pixels are added before the inputString
     * @return String with empty pixels
     */
    public static String forceTextToWidth(String inputString, int pixelWidth, boolean addBefore) {
        // Subtracts already existing
        for(char letter : inputString.toCharArray()) {
            // ONE MORE PIXEL FOR EACH LETTER DUE TO SPACING IN-GAME
            // 1 pixel
            if(letter == 'i')
                pixelWidth -= 2;
                // 2 pixels
            else if(letter == 'l')
                pixelWidth -= 3;
                // 3 pixels
            else if(Arrays.asList('t','I').contains(letter))
                pixelWidth -= 4;
                // 4 pixels
            else if(Arrays.asList('k','f').contains(letter))
                pixelWidth -= 5;
                // 5 pixels
            else
                pixelWidth -= 6;
        }

        if(pixelWidth < 0)
            // Returns to throw an error, signalling the string was shorter than the specified width
            return null;

        // Adds pixels after input string
        if(addBefore)
            return getPixelsAsString(pixelWidth) + inputString;

        // Adds pixels after input string
        else
            return inputString + getPixelsAsString(pixelWidth);
    }
}
