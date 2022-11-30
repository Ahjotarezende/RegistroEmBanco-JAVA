package Menu;
import Objetos.Banco;
import Objetos.Navegation;
import Objetos.Pessoa;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Navegation nav = new Navegation();
        Scanner sc = new Scanner(System.in);
        int operacao;
        Banco banco = new Banco();
        do{
            System.out.println("Seja bem vindo!");
            System.out.println("Digite a operacao que deseja:");
            System.out.println("1- Abrir uma conta");
            System.out.println("2- Fazer uma transferencia");
            System.out.println("3- Pesquisar por uma conta");
            System.out.println("4- Atualizar dados");
            System.out.println("5- Apagar uma conta existente");
            System.out.println("6- Comprimir dados");
            System.out.println("7- Descomprimir dados");
            System.out.println("8- Sair");
            operacao = sc.nextInt();
            switch (operacao) {
                case 1:
                    Pessoa p = banco.novaConta();
                    long pos = banco.salvarConta(p, p.getIdConta());
                    nav.criarConta(p.getIdConta(), pos);
                    break;
                case 2:
                    banco.transferencia();
                    break;
                case 3:
                    System.out.println("Qual conta deseja procurar? (ID)");
                    int id = sc.nextInt();
                    banco.pesqConta(nav.lerID(id));
                    break;
                case 4:
                    banco.atualizarConta();
                    break;
                case 5:
                    banco.deletarConta();
                    break;
                case 6:
                    banco.comprimirReg();
                    break;
                case 7:
                    banco.descomprimirReg();
                    break;
                case 8:
                    break;
                default:
                    System.out.println("Essa opcao nao é valida, digite outra.");
                    break;
            }
        }while(operacao!=8);
    }
}