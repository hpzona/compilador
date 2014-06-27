package gals;

import java.net.URL;
import java.util.Properties;

public class Lexico implements Constants {

    private int position;
    private String input;
    private int[][] SCANNER_TABLE;

    public Lexico() {
        this("");
        this.lerArquivoProperties();
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

    private void refazerArray(String identificador, Properties propFile) {
        String temp;
        int numero;

        String[] a = propFile.getProperty(identificador).split(";");
        String[][] array = new String[a.length][a.length];
        for (int i = 0; i < a.length; i++) {
            array[i] = a[i].split(",");
        }

        SCANNER_TABLE = new int[array.length][array[0].length];

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                temp = array[i][j];
                numero = Integer.parseInt(temp);
                SCANNER_TABLE[i][j] = numero;
                //Se quiser ver a tabela...
                //System.out.print(" " + SCANNER_TABLE[i][j]); 
            }
            //Se quiser ver a tabela...
            //System.out.println();
        }
    }

    private void lerArquivoProperties() {
        Properties prop = new Properties();
        URL url = ClassLoader.getSystemResource("gals/ScannerConstants.properties");
        try {
            prop.load(url.openStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refazerArray("identificador", prop);
    }
}
