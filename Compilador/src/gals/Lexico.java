package gals;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class Lexico implements Constants {

    private int position;
    private String input;
    private int[][] SCANNER_TABLE;

    public Lexico() throws IOException {
        this("");
        LerArquivoProperties();

    }

    public Lexico(String input) {
        setInput(input);
    }

    public void setInput(String input) {
        this.input = input;
        setPosition(0);
    }

    public void setPosition(int pos) {
        position = pos;
    }

    public Token nextToken() throws LexicalError {
        if (!hasInput()) {
            return null;
        }

        int start = position;

        int state = 0;
        int lastState = 0;
        int endState = -1;
        int end = -1;

        while (hasInput()) {
            lastState = state;
            state = nextState(nextChar(), state);

            if (state < 0) {
                break;
            } else {
                if (tokenForState(state) >= 0) {
                    endState = state;
                    end = position;
                }
            }
        }
        if (endState < 0 || (endState != state && tokenForState(lastState) == -2)) {
            throw new LexicalError(SCANNER_ERROR[lastState], start);
        }

        position = end;

        int token = tokenForState(endState);

        if (token == 0) {
            return nextToken();
        } else {
            String lexeme = input.substring(start, end);
            token = lookupToken(token, lexeme);
            return new Token(token, lexeme, start);
        }
    }

    private int nextState(char c, int state) {
        int next = (SCANNER_TABLE[state][c]);
        System.out.print(next);
        return next;
    }

    private int tokenForState(int state) {
        if (state < 0 || state >= TOKEN_STATE.length) {
            return -1;
        }

        return TOKEN_STATE[state];
    }

    public int lookupToken(int base, String key) {
        int start = SPECIAL_CASES_INDEXES[base];
        int end = SPECIAL_CASES_INDEXES[base + 1] - 1;

        while (start <= end) {
            int half = (start + end) / 2;
            int comp = SPECIAL_CASES_KEYS[half].compareTo(key);

            if (comp == 0) {
                return SPECIAL_CASES_VALUES[half];
            } else if (comp < 0) {
                start = half + 1;
            } else //(comp > 0)
            {
                end = half - 1;
            }
        }

        return base;
    }

    private boolean hasInput() {
        return position < input.length();
    }

    private char nextChar() {
        if (hasInput()) {
            return input.charAt(position++);
        } else {
            return (char) -1;
        }
    }

    private String[][] fetchArrayFromPropFile(String propertyName, Properties propFile) {
        String temp;
        int numero = 0;
        String[] a = propFile.getProperty(propertyName).split(";");
        // Para debugar e verificar se leu o array corretamente
        /*for (int i = 0; i < a.length; i++) {
         System.out.println(a[i]);
         }*/

        String[][] array = new String[a.length][a.length];
        for (int i = 0; i < a.length; i++) {
            array[i] = a[i].split(",");
        }

        SCANNER_TABLE = new int[array.length][array[0].length];

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                temp = array[i][j];
                numero = Integer.parseInt(temp);
                System.out.print(numero);
                SCANNER_TABLE[i][j] = numero;
                System.out.print(" " + SCANNER_TABLE[i][j]);
            }
            System.out.println();
        }
        return array;
    }

    private void LerArquivoProperties() throws IOException {
        Properties prop = new Properties();
        URL url = ClassLoader.getSystemResource("gals/ScannerConstants.properties");

        try {
            prop.load(url.openStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("------- By Using URL class -------");
        //System.out.println(prop.get("content"));
        //System.out.println(prop);

        fetchArrayFromPropFile("content", prop);

        /*for (int i = 0; i < SCANNER_TABLE_TEMP.length; i++) {
         for (int j = 0; j < SCANNER_TABLE_TEMP[i].length; j++){
         temp = SCANNER_TABLE_TEMP[i][j];
         System.out.print(temp);
         //numero = Integer.parseInt(temp);
         //SCANNER_TABLE[i][j]=numero;
         //System.out.print(","+SCANNER_TABLE[i][j]);
         }
         }*/
        /*Properties prop = new Properties();
         InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("gals/ScannerConstants.properties");
 
         try{
         prop.load(inputStream);
         }catch(IOException e)
         {
         e.printStackTrace();
         }
 
         System.out.println("------- By Using Thread class -------");
         //Print required values
         System.out.println(prop.get("content"));
 
         //Print all the properties
         System.out.println(prop);*/
    }
}
