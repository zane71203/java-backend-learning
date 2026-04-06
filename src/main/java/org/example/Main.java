package org.example;

public class Main {

    public static void main(String[] args) {
        //Checkpoint 1
        printNumber(6);
        //Checkpoint 2
        int answer2 = squareNumber(6);
        System.out.println("Answer2 is "+ answer2 );
        //Checkpoint 3
        boolean answer3 = isEven(5);
        System.out.println(answer3);

    }
    //Checkpoint 1
    public static void printNumber(int n){
        System.out.println("Number:"+ n );
    }
    //Checkpoint 2
    public static int squareNumber(int m){
        return m*m;
    }
    //Checkpoint 3
    public static boolean isEven(int o){
            return o%2 == 0;
        }

}