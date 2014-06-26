package controladora;

import gals.LexicalError;
import gals.Lexico;
import gals.Token;
import java.io.IOException;
import visao.JanelaPrincipal;

public class AnalisadorLexico {

    private JanelaPrincipal jp;

    public AnalisadorLexico(JanelaPrincipal janelaPrincipal) {
        jp = janelaPrincipal;
    } 
    
    public void analisarLexico(String codigo) throws IOException  {

        Lexico lexico = new Lexico();
        lexico.setInput(codigo);

        try {
            Token t = null;
            while ((t = lexico.nextToken()) != null) {
            }
            System.out.print("Análise completa\n");
            jp.mostrarResultaAnalise("Análise completa\nNenhum erro encontrado");
        } catch (LexicalError e) {
            System.out.println(e.getPosition());
            System.out.println(e.getMessage());
            //jp.setCursorNoErro(e.getPosition());
            jp.mostrarResultaAnalise("Erro léxico na posição: "+e.getPosition()+"\n"+e.getMessage()+"\n");
        } finally {
            
        }
    }
    
}
