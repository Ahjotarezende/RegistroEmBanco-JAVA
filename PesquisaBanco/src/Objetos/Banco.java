package Objetos;

import java.io.RandomAccessFile;
import java.util.Scanner;

public class Banco {
    public int gerarID(){
        int ultimoID;
        try(RandomAccessFile randomAcess = new RandomAccessFile("PesquisaBanco/src/output/arqBanco.txt", "r")){
            try{
                ultimoID = randomAcess.readInt();
            }catch(Exception e){
                ultimoID = -1;
            }
        }catch(Exception e){
            return -1;
        }
        return ultimoID+1;
    }

    public int verificaNome(String test){
        try(RandomAccessFile randomAccess = new RandomAccessFile("PesquisaBanco/src/output/arqBanco.txt", "r")){
            randomAccess.seek(4);
            byte lapide = randomAccess.readByte();
            int tamRegistro = randomAccess.readInt();
            byte[] bytes = new byte[tamRegistro];
            randomAccess.read(bytes);
            if(lapide==0){
                Pessoa p = new Pessoa(bytes);
                if(test.equals(p.getNomeUsuario()))
                    return 0;
            }
        }catch(Exception e){
            return 1;
        }
        return 1;
    }
    public Pessoa novaConta(){
        Pessoa p = new Pessoa(gerarID());
        System.out.print("Nome: ");
        Scanner sc = new Scanner(System.in);
        p.setNome(sc.nextLine());
        int qtdEmail=1;
        System.out.println("\nQuantos email voce terá? <Pelo menos 1>");
        do{
            qtdEmail=sc.nextInt();
        }while(qtdEmail<=0);
        String[] emails = new String[qtdEmail];
        for (int i = 0; i < qtdEmail; i++) {
            System.out.print("\nInforme seu "+(i+1)+" email: ");
            emails[i]=sc.nextLine();
        }
        p.setEmail(emails);
        String nomeUsuario;
        while(true){
            System.out.println("\nNome de usuario: ");
            nomeUsuario = sc.nextLine();
            if(verificaNome(nomeUsuario)==0)
                System.out.println("Nome em uso, escolha outro!");
            else{
                p.setNomeUsuario(nomeUsuario);
                break;
            }
        }
        System.out.print("Cidade: ");
        p.setCidade(sc.nextLine());
        System.out.printf("\nSenha: ");
        p.setSenha(sc.nextLine());
        System.out.printf("\nSeu CPF: ");
        p.setCpf(sc.nextLine());
        System.out.printf("\nQual será seu saldo inicial: ");
        p.setSaldo(sc.nextFloat());
        return p;
    }

    public void transferencia(){
        Scanner sc = new Scanner(System.in);
        Navegation nav = new Navegation();
        int beneficiado, depositante;
        float valor;
        System.out.print("\nInforme o ID do beneficiado: ");
        beneficiado = sc.nextInt();
        System.out.print("\nInforme o ID do depositante: ");
        depositante = sc.nextInt();
        System.out.printf("\nValor da transferencia: ");
        valor = sc.nextFloat();

        long posBen, posDep;
        posBen = nav.lerID(beneficiado);
        posDep = nav.lerID(depositante);

        if(posBen<0)
            System.out.println("Usuario não encontrado.");
        if(posDep<0)
            System.out.println("Usuario nao encontrado.");
        if(posBen>=0 && posDep>=0){
            try(RandomAccessFile randomAcess = new RandomAccessFile("PesquisaBanco/src/output/arqBanco.txt", "rw")){
                randomAcess.seek(posDep);
                randomAcess.readByte();
                int tamReg = randomAcess.readInt();
                byte[] registro = new byte[tamReg];
                randomAcess.read(registro);
                Pessoa p = new Pessoa(registro);
                if(p.getSaldo()>=valor) {
                    p.setTransRealizadas(p.getTransRealizadas() + 1);
                    p.setSaldo(p.getSaldo()-valor);
                    byte[] newReg = p.toBTArray();
                    if(tamReg>=newReg.length){
                        randomAcess.seek(posDep+1);
                        randomAcess.writeInt(tamReg);
                        randomAcess.write(newReg);
                    }
                    else{
                        randomAcess.seek(posDep);
                        randomAcess.writeByte(1);
                        randomAcess.seek(randomAcess.length());
                        randomAcess.writeByte(0);
                        randomAcess.writeInt(newReg.length);
                        randomAcess.write(newReg);
                    }
                    randomAcess.seek(posBen);
                    randomAcess.readByte();
                    int tamReg2 = randomAcess.readInt();
                    byte[] registro2 = new byte[tamReg2];
                    randomAcess.read(registro2);
                    Pessoa p2 = new Pessoa(registro2);
                    p2.setSaldo(p2.getSaldo()+valor);
                    p2.setTransRealizadas(p2.getTransRealizadas()+1);
                    byte[] newReg2 = p2.toBTArray();
                    if(tamReg2>=newReg2.length){
                        randomAcess.seek(posBen+1);
                        randomAcess.writeInt(tamReg2);
                        randomAcess.write(newReg2);
                    }else{
                        randomAcess.seek(posBen);
                        randomAcess.writeByte(1);
                        randomAcess.seek(randomAcess.length());
                        randomAcess.writeByte(0);
                        randomAcess.writeInt(newReg.length);
                        randomAcess.write(newReg2);
                    }
                }
                else
                    System.out.println("Voce nao tem saldo suficiente para esta transação.");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
