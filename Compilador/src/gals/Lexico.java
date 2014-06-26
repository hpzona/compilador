package gals;

import static com.sun.jmx.mbeanserver.DefaultMXBeanMappingFactory.propertyName;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class Lexico implements Constants {

    private int position;
    private String input;
    private String[][] SCANNER_TABLE;

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
        int next = Integer.parseInt(SCANNER_TABLE[state][c]);
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

    private static String[][] fetchArrayFromPropFile(String propertyName, Properties propFile) {
        //int numero[][];
        String[] a = propFile.getProperty(propertyName).split(";");
        // Para debugar e verificar se leu o array corretamente
        for (int i = 0; i < a.length; i++) {
           // numero[i] = Integer.parseInt(a[i]);
           System.out.println(a[i]);
        }
        String[][] array = new String[a.length][a.length];
        for (int i = 0; i < a.length; i++) {
            array[i] = a[i].split(",");
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

        String[][] SCANNER_TABLE = fetchArrayFromPropFile("content", prop);
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
