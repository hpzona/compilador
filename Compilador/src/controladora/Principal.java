package controladora;

import visao.JanelaPrincipal;


public class Principal {
    
    public static void main(String[] args) {
    
     JanelaPrincipal visao;
     Controladora controladora;
     
     visao = new JanelaPrincipal();
     controladora = new Controladora(visao);
      
     visao.interaja();
    
    }
    
}
