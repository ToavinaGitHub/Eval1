package com.eval1.mg.Exception;

public class ValeurInvalideException extends Exception{

    public ValeurInvalideException(String att){
        super("Valeur de : "+att+" invalide");
    }
}

