package tabladdeverdad;

import java.util.Hashtable;
import java.util.Scanner;

public class TabladDeVerdad {

    static Scanner scaner = new Scanner(System.in);
    
    static String formula, formulaRespaldo;
    static int j, currentDepth;
    static int[] resueltos = new int[5];
    static Hashtable<Character, Integer> preposiciones = new Hashtable<Character, Integer>();
    
    public static void main(String[] args) {
        System.out.println("----------TABLAS DE VERDAD----------");
        annadirPreposiciones();
        menu();
    }
    
    private static void menu(){
        System.out.print("\n --MENU--"
                + "\n1) Ver instrucciones"
                + "\n2) Introducir formula"
                + "\n3) Hacer la tabla"
                + "\nSelecciona la opción: ");
        switch(scaner.nextInt()){
            case 1:
                instrucciones();
                break;
            case 2:
                introducir();
                break;
            case 3:
                resolver();
                break;
        }
    }
    
    private static void instrucciones(){
        System.out.println("\nIntroduce los operadores no lógicos con letras minusculas y los lógicos segun lo siguiente: \n"
                + "\n∧ Conjuntor: exponente '^'"
                + "\n∨ Disyuntor: uve mayscula 'V'"
                + "\n➡ Implicador: gion '-'"
                + "\n↔ Coimplicador: gion bajo '_'"
                + "\n¬ Negador: exclamación cerrada '!'"
                + "\nLa formula ha de estar lista para operar con ella. No dejes espacios.");
        menu();
    }
    
    private static void introducir(){
        System.out.print("\nIntroduce la formula: ");
        formula = scaner.next();
        menu();
    }
    
    private static void resolver(){
        imprimir(formula, hacedorTablaSeccionSecuenciada(formula), formula.length(), (int)Math.pow(2,getNvariables(formula)));
    }
    
    private static char[][] hacedorTablaSeccionSecuenciada(String seccion){
        boolean[][] matriz = getMatriz(seccion);
        char[][] tabla = getTabla(seccion);
        boolean listo = true;
        String formulilla = seccion;
        TabladDeVerdad.formulaRespaldo = seccion;
        int maxDepth = getMaxDepth(seccion);
        int depth = 0;
        for (int i = 0; i < (int)Math.pow(2,getNvariables(formula)); i++) {
                    while(listo){
                        formulilla = formula;
                        tabla = eliminarParentesisObsoletos(tabla, i);
                            for (int k = 0; k < formula.length(); k++) {
                                if(formula.charAt(k) == '('){
                                    depth++;
                                }else{
                                    if(formula.charAt(k) == ')'){
                                        depth--;
                                    }
                                }
                                if(depth == maxDepth){
                                    tabla = busquedaConjuntor(matriz,k,i, tabla);
                                    if(formulilla.charAt(k) == '^'){
                                        break;
                                    }
                                }
                            }
                            tabla = eliminarParentesisObsoletos(tabla, i);
                            maxDepth = getMaxDepth(formula);
                            depth = 0;
                            formulilla = formula;
                            for (int k = 0; k < formula.length(); k++) {
                                if(formula.charAt(k) == '('){
                                    depth++;
                                }else{
                                    if(formula.charAt(k) == ')'){
                                        depth--;
                                    }
                                }
                                if(depth == maxDepth){
                                    tabla = busquedaDistuntor(matriz,k,i, tabla);
                                    if(formulilla.charAt(k) == 'V'){
                                        break;
                                    }
                                }
                            }
                            tabla = eliminarParentesisObsoletos(tabla, i);
                            maxDepth = getMaxDepth(formula);
                            depth = 0;
                            formulilla = formula;
                            for (int k = 0; k < formula.length(); k++) {
                                if(formula.charAt(k) == '('){
                                    depth++;
                                }else{
                                    if(formula.charAt(k) == ')'){
                                        depth--;
                                    }
                                }
                                if(depth == maxDepth){
                                    tabla = busquedaImplicador(matriz,k,i, tabla);
                                    if(formulilla.charAt(k) == '-'){
                                        break;
                                    }
                                }
                            }
                            tabla = eliminarParentesisObsoletos(tabla, i);
                            maxDepth = getMaxDepth(formula);
                            depth = 0;
                            formulilla = formula;
                            for (int k = 0; k < formula.length(); k++) {
                                if(formula.charAt(k) == '('){
                                    depth++;
                                }else{
                                    if(formula.charAt(k) == ')'){
                                        depth--;
                                    }
                                }
                                if(depth == maxDepth){
                                    tabla = busquedaCoimplicador(matriz,k,i, tabla);
                                    if(formulilla.charAt(k) == '_'){
                                        break;
                                    }
                                }
                            }
                            tabla = eliminarParentesisObsoletos(tabla, i);
                            maxDepth = getMaxDepth(formula);
                            depth = 0;
                            formulilla = formula;
                            if(terminado(formula)){
                                listo = false;
                            }
                        }
                formula = formulaRespaldo;
                resueltos = limpiador(resueltos);
                maxDepth = getMaxDepth(formula);
                depth = 0;
                listo=true;
            }
            tabla = letrasEnTabla(seccion, matriz, tabla);
            return tabla;
            
    }
    
    private static int[] limpiador(int[] array){
        for (int z = 0; z < array.length; z++) {
            array[z]=0;
        }
        return array;
    }
    
    private static char[][] busquedaConjuntor(boolean[][] matriz, int k, int i, char[][] tabla){
        if(formula.charAt(k) == '^'){
            if(k-2 >=0){
                if(formula.charAt(k-2) == '!'){
                    if(k+2 <=formula.length()){
                        if(formula.charAt(k+1) == '!'){
                            return usoConjuntor(k, 2, 2, matriz, i, 5, true, true, tabla);
                        }else{
                            return usoConjuntor(k, 2, 1, matriz, i, 4, true, false, tabla);
                        }
                    }else{
                        return usoConjuntor(k, 2, 1, matriz, i, 4, true, false, tabla);
                    }
                }else{
                    if(formula.charAt(k+1) == '!'){
                        return usoConjuntor(k, 1, 2, matriz, i, 4, false, true, tabla);
                    }else{
                        return usoConjuntor(k, 1, 1, matriz, i, 3, false, false, tabla);
                    }
                }
            }else{
                if(k+2 <formula.length()){
                    if(formula.charAt(k+1) == '!'){
                        return usoConjuntor(k, 1, 2, matriz, i, 4, false, true, tabla);
                    }else{
                        return usoConjuntor(k, 1, 1, matriz, i, 3, false, false, tabla);
                    }
                }else{
                    return usoConjuntor(k, 1, 1, matriz, i, 3, false, false, tabla);
                }
            }
        }else{
            return tabla;
        }
    }
    
    private static char[][] busquedaDistuntor(boolean[][] matriz, int k, int i, char[][] tabla){
        if(formula.charAt(k) == 'V'){
            if(k-2 >=0){
                if(formula.charAt(k-2) == '!'){
                    if(k+2 <=formula.length()){
                        if(formula.charAt(k+1) == '!'){
                            return usoDisyuntor(k, 2, 2, matriz, i, 5, true, true, tabla);
                        }else{
                            return usoDisyuntor(k, 2, 1, matriz, i, 4, true, false, tabla);
                        }
                    }else{
                        return usoDisyuntor(k, 2, 1, matriz, i, 4, true, false, tabla);
                    }
                }else{
                    if(formula.charAt(k+1) == '!'){
                        return usoDisyuntor(k, 1, 2, matriz, i, 4, false, true, tabla);
                    }else{
                        return usoDisyuntor(k, 1, 1, matriz, i, 3, false, false, tabla);
                    }
                }
            }else{
                if(k+2 <formula.length()){
                    if(formula.charAt(k+1) == '!'){
                        return usoDisyuntor(k, 1, 2, matriz, i, 4, false, true, tabla);
                    }else{
                        return usoDisyuntor(k, 1, 1, matriz, i, 3, false, false, tabla);
                    }
                }else{
                    return usoDisyuntor(k, 1, 1, matriz, i, 3, false, false, tabla);
                }
            }
        }else{
            return tabla;
        }
    }
    
    private static char[][] busquedaImplicador(boolean[][] matriz, int k, int i, char[][] tabla){
        if(formula.charAt(k) == '-'){
            if(k-2 >=0){
                if(formula.charAt(k-2) == '!'){
                    if(k+2 <=formula.length()){
                        if(formula.charAt(k+1) == '!'){
                            return usoImplicador(k, 2, 2, matriz, i, 5, true, true, tabla);
                        }else{
                            return usoImplicador(k, 2, 1, matriz, i, 4, true, false, tabla);
                        }
                    }else{
                        return usoImplicador(k, 2, 1, matriz, i, 4, true, false, tabla);
                    }
                }else{
                    if(formula.charAt(k+1) == '!'){
                        return usoImplicador(k, 1, 2, matriz, i, 4, false, true, tabla);
                    }else{
                        return usoImplicador(k, 1, 1, matriz, i, 3, false, false, tabla);
                    }
                }
            }else{
                if(k+2 <formula.length()){
                    if(formula.charAt(k+1) == '!'){
                        return usoImplicador(k, 1, 2, matriz, i, 4, false, true, tabla);
                    }else{
                        return usoImplicador(k, 1, 1, matriz, i, 3, false, false, tabla);
                    }
                }else{
                    return usoImplicador(k, 1, 1, matriz, i, 3, false, false, tabla);
                }
            }
        }else{
            return tabla;
        }
    }
    
    private static char[][] busquedaCoimplicador(boolean[][] matriz, int k, int i, char[][] tabla){
        if(formula.charAt(k) == '_'){
            if(k-2 >=0){
                if(formula.charAt(k-2) == '!'){
                    if(k+2 <=formula.length()){
                        if(formula.charAt(k+1) == '!'){
                            return usoCoimplicador(k, 2, 2, matriz, i, 5, true, true, tabla);
                        }else{
                            return usoCoimplicador(k, 2, 1, matriz, i, 4, true, false, tabla);
                        }
                    }else{
                        return usoCoimplicador(k, 2, 1, matriz, i, 4, true, false, tabla);
                    }
                }else{
                    if(formula.charAt(k+1) == '!'){
                        return usoCoimplicador(k, 1, 2, matriz, i, 4, false, true, tabla);
                    }else{
                        return usoCoimplicador(k, 1, 1, matriz, i, 3, false, false, tabla);
                    }
                }
            }else{
                if(k+2 <formula.length()){
                    if(formula.charAt(k+1) == '!'){
                        return usoCoimplicador(k, 1, 2, matriz, i, 4, false, true, tabla);
                    }else{
                        return usoCoimplicador(k, 1, 1, matriz, i, 3, false, false, tabla);
                    }
                }else{
                    return usoCoimplicador(k, 1, 1, matriz, i, 3, false, false, tabla);
                }
            }
        }else{
            return tabla;
        }
    }
    
    private static char[][] usoConjuntor(int k, int pos1, int pos2, boolean[][] matriz, int i, int cantidad, boolean negativo1, boolean negativo2, char[][] tabla){
        boolean  prepo1, prepo2;
        if(formula.charAt(k-1) == '1'){
            prepo1 = true;
        }else{
            if(formula.charAt(k-1) == '0'){
                prepo1 = false;
            }else{
                prepo1 = matriz[preposiciones.get(formula.charAt(k-1))][i];
            }
        }
        if(formula.charAt(k+pos2) == '1'){
            prepo2 = true;
        }else{
            if(formula.charAt(k+pos2) == '0'){
                prepo2 = false;
            }else{
                prepo2 = matriz[preposiciones.get(formula.charAt(k+pos2))][i];
            }
        }
        return usoFinalConjuntor(k-pos1, cantidad, prepo1, prepo2, negativo1, negativo2, i, tabla, k);
    }
    
    private static char[][] usoDisyuntor(int k, int pos1, int pos2, boolean[][] matriz, int i, int cantidad, boolean negativo1, boolean negativo2, char[][] tabla){
        boolean  prepo1, prepo2;
        if(formula.charAt(k-1) == '1'){
            prepo1 = true;
        }else{
            if(formula.charAt(k-1) == '0'){
                prepo1 = false;
            }else{
                prepo1 = matriz[preposiciones.get(formula.charAt(k-1))][i];
            }
        }
        if(formula.charAt(k+pos2) == '1'){
            prepo2 = true;
        }else{
            if(formula.charAt(k+pos2) == '0'){
                prepo2 = false;
            }else{
                prepo2 = matriz[preposiciones.get(formula.charAt(k+pos2))][i];
            }
        }
        return usoFinalDisyuntor(k-pos1, cantidad, prepo1, prepo2, negativo1, negativo2, i, tabla, k);
    }
    
    private static char[][] usoImplicador(int k, int pos1, int pos2, boolean[][] matriz, int i, int cantidad, boolean negativo1, boolean negativo2, char[][] tabla){
        boolean  prepo1, prepo2;
        if(formula.charAt(k-1) == '1'){
            prepo1 = true;
        }else{
            if(formula.charAt(k-1) == '0'){
                prepo1 = false;
            }else{
                prepo1 = matriz[preposiciones.get(formula.charAt(k-1))][i];
            }
        }
        if(formula.charAt(k+pos2) == '1'){
            prepo2 = true;
        }else{
            if(formula.charAt(k+pos2) == '0'){
                prepo2 = false;
            }else{
                prepo2 = matriz[preposiciones.get(formula.charAt(k+pos2))][i];
            }
        }
        return usoFinalImplicador(k-pos1, cantidad, prepo1, prepo2, negativo1, negativo2, i, tabla, k);
    }
    
    private static char[][] usoCoimplicador(int k, int pos1, int pos2, boolean[][] matriz, int i, int cantidad, boolean negativo1, boolean negativo2, char[][] tabla){
        boolean  prepo1, prepo2;
        if(formula.charAt(k-1) == '1'){
            prepo1 = true;
        }else{
            if(formula.charAt(k-1) == '0'){
                prepo1 = false;
            }else{
                prepo1 = matriz[preposiciones.get(formula.charAt(k-1))][i];
            }
        }
        if(formula.charAt(k+pos2) == '1'){
            prepo2 = true;
        }else{
            if(formula.charAt(k+pos2) == '0'){
                prepo2 = false;
            }else{
                prepo2 = matriz[preposiciones.get(formula.charAt(k+pos2))][i];
            }
        }
        return usoFinalCoimplicador(k-pos1, cantidad, prepo1, prepo2, negativo1, negativo2, i, tabla, k);
    }
    
    private static char[][] replacement(boolean condicion, char[][] tabla, int tipo, int i){
        switch(tipo){
            case 1:
                if(condicion){
                    tabla[busquedaExt('^', resueltos[tipo-1])][i] = '1';
                }else{
                    tabla[busquedaExt('^', resueltos[tipo-1])][i] = '0';
                }
                break;
            case 2:
                if(condicion){
                    tabla[busquedaExt('V', resueltos[tipo-1])][i] = '1';
                }else{
                    tabla[busquedaExt('V', resueltos[tipo-1])][i] = '0';
                }
                break;
            case 3:
                if(condicion){
                    tabla[busquedaExt('-', resueltos[tipo-1])][i] = '1';
                }else{
                    tabla[busquedaExt('-', resueltos[tipo-1])][i] = '0';
                }
                break;
            case 4:
                if(condicion){
                    tabla[busquedaExt('_', resueltos[tipo-1])][i] = '1';
                }else{
                    tabla[busquedaExt('_', resueltos[tipo-1])][i] = '0';
                }
                break;
        }
        resueltos[tipo-1]++;
        return tabla;
    }
    
    private static int busquedaExt(char caracter, int deley){
        int depth = 0;
        int pos = 0;
        while (deley >=0){
            if(pos == formulaRespaldo.length()){
                pos = 0;
            }
            if(formulaRespaldo.charAt(pos) == '('){
                depth++;
            }else{
                if(formulaRespaldo.charAt(pos) == ')'){
                    depth--;
                }
            }
            if(caracter == '!' && currentDepth >= 1){
                if(formulaRespaldo.charAt(pos) == caracter && depth == currentDepth-1){
                    deley--;
                }
            }else{
                if(formulaRespaldo.charAt(pos) == caracter && depth == currentDepth){
                    deley--;
                }
            }
            
            pos++;
        }
        return pos-1;
    }
    
    private static char[][] usoFinalConjuntor (int pos, int cantidad, boolean valor1, boolean valor2, boolean negativo1, boolean negativo2, int i, char[][] tabla, int k){
        if(negativo1){
            if(negativo2){
                formula = remplazo(formula, pos, cantidad,conjuntor(negador(valor1),negador(valor2)));
                tabla = replacement(conjuntor(negador(valor1),negador(valor2)),tabla,1,i);
                resueltos[4]+=2;
            }else{
                formula = remplazo(formula, pos, cantidad,conjuntor(negador(valor1),valor2));
                tabla = replacement(conjuntor(negador(valor1),valor2),tabla,1,i);
                resueltos[4]++;
            }
        }else{
            if(negativo2){
                formula = remplazo(formula, pos, cantidad,conjuntor(valor1,negador(valor2)));
                tabla = replacement(conjuntor(valor1,negador(valor2)),tabla,1,i);
                resueltos[4]++;
                
            }else{
                formula = remplazo(formula, pos, cantidad,conjuntor(valor1,valor2));
                tabla = replacement(conjuntor(valor1,valor2),tabla,1,i);
            }
        }
        return tabla;
    }
    
    private static char[][] usoFinalDisyuntor (int pos, int cantidad, boolean valor1, boolean valor2, boolean negativo1, boolean negativo2, int i, char[][] tabla, int k){
        if(negativo1){
            if(negativo2){
                formula = remplazo(formula, pos, cantidad,disyuntor(negador(valor1),valor2));
                tabla = replacement(disyuntor(negador(valor1),negador(valor2)),tabla,2,i);
                resueltos[4]+=2;
            }else{
                formula = remplazo(formula, pos, cantidad,disyuntor(negador(valor1),negador(valor2)));
                tabla = replacement(disyuntor(negador(valor1),valor2),tabla,2,i);
                resueltos[4]++;
            }
        }else{
            if(negativo2){
                formula = remplazo(formula, pos, cantidad,disyuntor(valor1,negador(valor2)));
                tabla = replacement(disyuntor(valor1,negador(valor2)),tabla,2,i);
                resueltos[4]++;
            }else{
                formula = remplazo(formula, pos, cantidad,disyuntor(valor1,valor2));
                tabla = replacement(disyuntor(valor1,valor2),tabla,2,i);
            }
        }
        return tabla;
    }
    
    private static char[][] usoFinalImplicador (int pos, int cantidad, boolean valor1, boolean valor2, boolean negativo1, boolean negativo2, int i, char[][] tabla, int k){
        if(negativo1){
            if(negativo2){
                formula = remplazo(formula, pos, cantidad,implicador(negador(valor1),valor2));
                tabla = replacement(implicador(negador(valor1),negador(valor2)),tabla,3,i);
                resueltos[4]+=2;
            }else{
                formula = remplazo(formula, pos, cantidad,implicador(negador(valor1),negador(valor2)));
                tabla = replacement(implicador(negador(valor1),valor2),tabla,3,i);
                resueltos[4]++;
            }
        }else{
            if(negativo2){
                formula = remplazo(formula, pos, cantidad,implicador(valor1,negador(valor2)));
                tabla = replacement(implicador(valor1,negador(valor2)),tabla,3,i);
                resueltos[4]++;
            }else{
                formula = remplazo(formula, pos, cantidad,implicador(valor1,valor2));
                tabla = replacement(implicador(valor1,valor2),tabla,3,i);
            }
        }
        return tabla;
    }
    
    private static char[][] usoFinalCoimplicador (int pos, int cantidad, boolean valor1, boolean valor2, boolean negativo1, boolean negativo2, int i, char[][] tabla, int k){
        if(negativo1){
            if(negativo2){
                formula = remplazo(formula, pos, cantidad,coimplicador(negador(valor1),valor2));
                tabla = replacement(coimplicador(negador(valor1),negador(valor2)),tabla,4,i);
                resueltos[4]+=2;
            }else{
                formula = remplazo(formula, pos, cantidad,coimplicador(negador(valor1),negador(valor2)));
                tabla = replacement(coimplicador(negador(valor1),valor2),tabla,4,i);
                resueltos[4]++;
            }
        }else{
            if(negativo2){
                formula = remplazo(formula, pos, cantidad,coimplicador(valor1,negador(valor2)));
                tabla = replacement(coimplicador(valor1,negador(valor2)),tabla,4,i);
                resueltos[4]++;
            }else{
                formula = remplazo(formula, pos, cantidad,coimplicador(valor1,valor2));
                tabla = replacement(coimplicador(valor1,valor2),tabla,4,i);
            }
        }
        return tabla;
    }
    
    private static boolean[][] getMatriz(String formula){
        int nVariables = getNvariables(formula);
        int combinaciones =  (int)Math.pow(2,nVariables);
        
        int[] indexList = new int[nVariables];
        boolean matriz[][] = new boolean[nVariables][combinaciones];
        j = 0;
        matriz = bucle(0, nVariables, indexList, matriz);
        return matriz;
    }
    
    private static char[][] getTabla(String formula){
        return new char[formula.length()][(int)Math.pow(2,getNvariables(formula))];
    }
    
    private static int getNvariables(String texto){
        int numero = 0;
        String encontradas = "";
        for (int i = 0; i < texto.length(); i++) {
            if(texto.charAt(i) != '('&& texto.charAt(i) != ')'&& texto.charAt(i) != '^'&& texto.charAt(i) != 'V'&& texto.charAt(i) != '-'&& texto.charAt(i) != '_'&& texto.charAt(i) != '!'){
                if(encontradas.matches(".*["+texto.charAt(i)+"].*") == false){
                    numero++;
                    encontradas = encontradas+texto.charAt(i);
                }
             }
        }
        return numero;
    }
    
    private static boolean terminado(String formula){
        return formula.length() == 1;
    }
    
    static private boolean[][] bucle(int pos, int cantidad, int[] indexList, boolean[][] matriz){
        pos++; 
        for(int i=0; i<=2-1;i++){ 
           indexList[pos-1]=i; 
           if (pos == cantidad){
             Combinacion(cantidad, indexList, matriz);
           }else{
             bucle(pos, cantidad, indexList, matriz);
           }
        }
        return matriz;
    } 
    
    private static void Combinacion(int cantidad, int[] indexList, boolean[][] matriz){
        boolean[] vector = {true, false};
        StringBuffer combStr = new StringBuffer("");   
        for (int i=0; i<cantidad; i++){
            matriz[i][j] = vector[indexList[i]];
            combStr = combStr.append(String.valueOf(vector[indexList[i]])).append("  ");
        }
        j++;
    }
    
    private static void imprimir(String seccion ,char[][] array, int long1, int long2){
        for (int i = 0; i < seccion.length(); i++) {
            System.out.print(seccion.charAt(i)+" ");
        }
        System.out.println();
        for (int x = 0; x < long2; x++) {
           for (int y = 0; y < long1; y++) {
               System.out.print(array[y][x]+" ");
           }
           System.out.println();
                   
       }
   }
   
   private static void annadirPreposiciones(){
       preposiciones.put('p', 0);
       preposiciones.put('q', 1);
       preposiciones.put('r', 2);
       preposiciones.put('s', 3);
       preposiciones.put('t', 4);
       preposiciones.put('u', 5);
       preposiciones.put('v', 6);
       preposiciones.put('w', 7);
       preposiciones.put('x', 8);
       preposiciones.put('y', 9);
       preposiciones.put('z', 10);
       preposiciones.put('a', 11);
       preposiciones.put('b', 12);
       preposiciones.put('c', 13);
       preposiciones.put('d', 14);
       preposiciones.put('e', 15);
       preposiciones.put('f', 16);
       preposiciones.put('g', 17);
       preposiciones.put('h', 18);
       preposiciones.put('i', 19);
       preposiciones.put('j', 20);
       preposiciones.put('k', 21);
       preposiciones.put('l', 22);
       preposiciones.put('m', 23);
       preposiciones.put('n', 24);
       preposiciones.put('o', 25);
       
   }
   
    private static boolean conjuntor(boolean a,boolean b){
        return a && b;
    }
    
    private static boolean disyuntor(boolean a,boolean b){
        return a || b;
    }
    
    private static boolean implicador(boolean a,boolean b){
        if((a == b) || ((a == false) && (b == true))){
            return true;
        }else{
            return false;
        }
    }
    
    private static boolean coimplicador(boolean a,boolean b){
        return a == b;
    }
    
    private static boolean negador(boolean a){
        if(a){
            return false;
        }else{
            return true;
        }
    }
    
    private static String remplazo(String texto,int inicio, int cantidad,boolean valor){
        StringBuilder resultado=new StringBuilder(texto);
        if(valor){
            resultado.setCharAt(inicio, '1');
        }else{
            resultado.setCharAt(inicio, '0');
        }
        resultado.delete(inicio+1, (cantidad)+inicio);
        return resultado.toString();
    }

    private static char[][] letrasEnTabla(String seccion ,boolean[][] matriz, char[][] tabla) {
        for (int i = 0; i < seccion.length(); i++) {
            if(preposiciones.get(seccion.charAt(i)) != null){
                if(i != 0 && seccion.charAt(i-1) == '!'){
                    for (int k = 0; k < (int)Math.pow(2,getNvariables(seccion)); k++) {
                        if(matriz[preposiciones.get(seccion.charAt(i))][k]){
                            tabla[i][k] = '0';
                        }else{
                            tabla[i][k] = '1';
                        }
                    }
                }else{
                    for (int k = 0; k < (int)Math.pow(2,getNvariables(seccion)); k++) {
                        if(matriz[preposiciones.get(seccion.charAt(i))][k]){
                            tabla[i][k] = '1';
                        }else{
                            tabla[i][k] = '0';
                        }
                    }
                }
            }else{
                if((seccion.charAt(i)=='!' && seccion.charAt(i+1) != '(') || seccion.charAt(i)=='(' || seccion.charAt(i)==')'){
                    for (int k = 0; k < (int)Math.pow(2,getNvariables(seccion)); k++) {
                        tabla[i][k] = ' ';
                    }
                }
            }
        }
        return tabla;
    }
    
    private static int getMaxDepth(String seccion) {
        int Maxdepth = 0;
        int depth = 0;
        for (int i = 0; i < seccion.length(); i++) {
            if(seccion.charAt(i) == '('){
                depth++;
                if(depth > Maxdepth){
                    Maxdepth = depth;
                }
            }else{
                if(seccion.charAt(i) == ')'){
                    depth--;
                }
            }
        }
        if(depth != 0){
            System.out.println("\n AVISO:\n        La formula no cierra parentesis\n");
            System.exit(0);
        }
        currentDepth = Maxdepth;
        return Maxdepth;
    }

    private static char[][] eliminarParentesisObsoletos(char[][] tabla, int k) {
        int recorte = 0;
        StringBuilder blanco = new StringBuilder(formula);
        for (int i = 0; i < formula.length(); i++) {
            blanco.setCharAt(i, ' ');
        }
        StringBuilder nueva = new StringBuilder(blanco.toString());
        int pos = 0;
        for (int i = 0; i < formula.length(); i++) {
            if(formula.charAt(i) == '!' && formula.charAt(i+1) == '(' && formula.charAt(i+3) == ')'){
                if(formula.charAt(i+2) == '0'){
                    nueva.setCharAt(pos, '1');
                    i+=3;
                    recorte+=3;
                    tabla[busquedaExt('!', resueltos[4])][k] = '1';
                }else{
                    if(formula.charAt(i+2) == '1'){
                    nueva.setCharAt(pos, '0');
                    i+=3;
                    recorte+=3;
                    tabla[busquedaExt('!', resueltos[4])][k] = '0';
                    }
                }
            }else{
                if(formula.charAt(i) == '(' && formula.length() >= i+2 && formula.charAt(i+2) == ')'){
                    nueva.setCharAt(pos, formula.charAt(i+1));
                    i+=2;
                    recorte+=2;
                }else{
                    nueva.setCharAt(pos, formula.charAt(i));
                }
            }
            pos++;
        }
        char[] segunda = new char[nueva.length()-recorte];
        for (int i = 0; i < nueva.length()-recorte; i++) {
            segunda[i] = nueva.charAt(i);
        }
        String tercera = new String(segunda);
        formula = tercera;
        return tabla;
    }
   
}