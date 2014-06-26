package controladora;

import gals.LexicalError;
import gals.Lexico;
import gals.SemanticError;
import gals.Semantico;
import gals.Sintatico;
import gals.SyntaticError;
import visao.JanelaPrincipal;

public class AnalisadorSintatico {

    private JanelaPrincipal jp;

    public AnalisadorSintatico(JanelaPrincipal janelaPrincipal) {
        jp = janelaPrincipal;
    }

    public void analisarSintaxe(String codigo) {
        Lexico lexico = new Lexico();
        Sintatico sintatico = new Sintatico();
        Semantico semantico = new Semantico();
        lexico.setInput(codigo);

        try {
            sintatico.parse(lexico, semantico);
            System.out.print("Análise completa\n");
            jp.mostrarResultaAnalise("Análise completa\nNenhum erro encontrado");
        } catch (LexicalError | SyntaticError ex) {
            jp.setCursorNoErro(ex.getPosition());
            jp.mostrarResultaAnalise("Erro sintárico na posição: " + ex.getPosition() + "\n" + ex.getMessage() + "\n");
        } catch (SemanticError ex) {
            jp.setCursorNoErro(ex.getPosition());
            jp.mostrarResultaAnalise("Erro Semantico na posição: " + ex.getPosition() + "\n" + ex.getMessage() + "\n");
        }
    }
}
