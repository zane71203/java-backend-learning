package org.example;

public class Main {

    public static void main(String[] args) {

        //Exercise 1（baseline）
        int i;

        for (i = 1; i < 6; i++) {
            System.out.println(i);
            if (i == 5) {
                System.out.println("End of Exercise 1（baseline）");
            }
        }

        //Exercise 2（baseline）

        int j = 5;

        while (j < 6) {
            System.out.println(j);
            j--;
            if (j == 0) {
                System.out.println("End of Exercise 2（baseline）");
                break;
            }

        }

        //Exercise 3（control flow）
        int k=0;
        while (k < 10) {

            k++;


            if (k == 5) {
                System.out.println("end of Exercise 3（control flow）");
                break;
            }
            System.out.println(k);
        }
        //Exercise 4（continue）
        int l;
        for (l=1; l<6; l++ ){

            if(l==3){
                continue;
            }

            System.out.println(l);

            if(l==5){
                System.out.println("end of Exercise 4（continue） ");
            }

        }
        //Exercise 5（condition loop）
        int m = 5;
        while (m>0) {

            System.out.println("Hello");

            if(m==1){
                System.out.println("end of Exercise 5（condition loop） ");
            }
            m--;

        }

        //Exercise 6（do-while）
        int n = 0;
        do {
            System.out.println(n);
            n++;
            if (n==5){
                System.out.println("end of Exercise 6（do-while） ");
            }
        }while (
                n<=4
        );

        //Exercise 7（重要 checkpoint）
        for (int o = 1; o < 10; o += 2){
            System.out.println(o);
            if (o==9){
                System.out.println("end of Exercise 7（重要 checkpoint）");
            }
        }
        //Exercise 8（nested loop baseline）

        for (int x=1;x<=5;x++){
            for (int y=1; y<=x ;y++){
                System.out.print("*");
            }

            System.out.println();
        }

        //8.1
        for (int row = 5; row >= 1; row--) {

            for (int star = 1; star <= row; star++) {
                System.out.print("*");
            }

            System.out.println();

        }

        //8.2
        for (int out = 1 ; out <=5 ; out++){

            for (int in = 1 ; in <= out; in++){

                System.out.print(in);

            }

            System.out.println();
        }



    }
}