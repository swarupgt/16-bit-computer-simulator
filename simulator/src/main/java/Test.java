package src.main.java;

public class Test {
    public static void main(String[] args) {
        Register[] GPR = new Register[2];
        for (int i = 0; i < 2; i++) {
            GPR[i] = new Register();
        }
        System.out.println("reg val: " + GPR[0].Get());

    }
}
