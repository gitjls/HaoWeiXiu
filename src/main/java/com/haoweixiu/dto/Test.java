package com.haoweixiu.dto;

/**
 * Created by Administrator on 2016/10/25 0025.
 */
public class Test {

    public static void main(String[] args) {

        A a = new Test().new A();
        int b=a.Add(1, 2);
        System.out.print("结果是"+b);
    }

    public class A {

        int Add(int a, int b) {
            try {
                return a + b;
            } catch (Exception e) {
                System.out.print("进catch");
            } finally {
                System.out.print("进finally");
            }
            return 0;
        }
    }

}
