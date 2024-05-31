package model;

import jflex.exceptions.SilentExit;

public class ExecuteJFlex {

    public static void main(String[] args) {
        String lexerFile = System.getProperty("user.dir") + "/src/main/Lexer.flex";
        try {
            jflex.Main.generate(new String[]{lexerFile});
        } catch (SilentExit ex) {
            System.out.println("Error al compilar/generar el archivo flex: " + ex);
        }
    }

}
