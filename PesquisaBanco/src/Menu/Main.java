package Menu;
import Objetos.Banco;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int operation;
        Banco banco = new Banco();
        do{
            System.out.println("Seja bem vindo!");
            System.out.println("Digite a operacao que deseja:");
            System.out.println("1- Abrir uma conta");
            System.out.println("2- Fazer uma transferencia");
            System.out.println("3- Pesquisar por uma conta");
            System.out.println("4- Atualizar dados");
            System.out.println("5- Apagar uma conta existente");
            System.out.println("6- Sair");
            operation = sc.nextInt();
            switch (operation) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Essa opcao nao é valida, digite outra.");
                    break;
            }
        }while(operation!=6);
    }
}