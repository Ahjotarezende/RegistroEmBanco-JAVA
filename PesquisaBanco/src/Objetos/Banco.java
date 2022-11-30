package Objetos;

import java.io.RandomAccessFile;
import java.util.Random;
import java.util.Scanner;

public class Banco {
    public int gerarID(){
        int ultimoID;
        try(RandomAccessFile randomAcess = new RandomAccessFile("TrabalhoAEDIII/PesquisaBanco/src/output/arqBanco.txt", "r")){
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
        try(RandomAccessFile randomAccess = new RandomAccessFile("TrabalhoAEDIII/PesquisaBanco/src/output/arqBanco.txt", "r")){
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
        sc.nextLine();
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
            try(RandomAccessFile randomAcess = new RandomAccessFile("TrabalhoAEDIII/PesquisaBanco/src/output/arqBanco.txt", "rw")){
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

    public long salvarConta(Pessoa p, int id){
        long pos=0;
        try(RandomAccessFile randomAccess = new RandomAccessFile("TrabalhoAEDIII/PesquisaBanco/src/output/arqBanco.txt", "rw")){
            randomAccess.writeInt(id);
            randomAccess.seek(randomAccess.length());
            pos=randomAccess.getFilePointer();
            byte[] dadosConta;
            dadosConta = p.toBTArray();
            randomAccess.writeByte(0);
            randomAccess.writeInt(dadosConta.length);
            randomAccess.write(dadosConta);
        }catch(Exception e){
            e.printStackTrace();
        }
        return pos;
    }

    public void pesqConta(Long pos){
        try(RandomAccessFile randomAcess = new RandomAccessFile("TrabalhoAEDIII/PesquisaBanco/src/output/arqBanco.txt", "r")){
            randomAcess.seek(pos);
            byte lapide = randomAcess.readByte();
            byte[] dadosConta = new byte[randomAcess.readInt()];
            randomAcess.read(dadosConta);
            if(lapide!=1){
                Pessoa p = new Pessoa(dadosConta);
                System.out.println("Conta encontrada!");
                System.out.println("Nome: "+p.getNome());
                System.out.println("Email:");
                for (int i = 0; i < p.getEmail().length; i++) {
                    System.out.println("Email "+(i+1)+": "+p.getEmail()[i]);
                }
                System.out.println("Nome de usuario: "+p.getNomeUsuario());
                System.out.println("Cidade: "+p.getCidade());
                System.out.println("CPF: "+p.getCpf());
            }
            else
                System.out.println("Conta nao encontrada.");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void atualizarConta(){
        try(RandomAccessFile randomAcess = new RandomAccessFile("TrabalhoAEDIII/PesquisaBanco/src/output/arqBanco.txt", "rw")){
            Scanner sc = new Scanner(System.in);
            System.out.println("Digite o ID da conta: ");
            int id = sc.nextInt();
            Navegation nav = new Navegation();
            long pos = nav.lerID(id);
            if(pos>=0){
                Pessoa p;
                randomAcess.seek(pos);
                if(randomAcess.readByte()!=1){
                    byte[] dadosConta = new byte[randomAcess.readInt()];
                    p = new Pessoa(dadosConta);
                    this.pesqConta(pos);
                    System.out.println("Novo nome: ");
                    p.setNome(sc.nextLine());
                    while(true){
                        System.out.println("Novo nome de usuario: ");
                        String nomeUsuario = sc.nextLine();
                        if(verificaNome(nomeUsuario)==1){
                            p.setNomeUsuario(nomeUsuario);
                            break;
                        }else
                            System.out.println("Nome em uso, escolha outro!");
                    }
                    System.out.println("Quantos emails deseja cadastrar: ");
                    int qtdEmails = 1;
                    do {
                        qtdEmails = sc.nextInt();
                    }while(qtdEmails<=0);
                    String[] emails = new String[qtdEmails];
                    for (int i = 0; i < qtdEmails; i++) {
                        System.out.println("Informe o "+i+" email: ");
                        emails[i]=sc.nextLine();
                    }
                    p.setEmail(emails);
                    System.out.println("Nome da sua cidade: ");
                    p.setCidade(sc.nextLine());
                    System.out.println("CPF: ");
                    p.setCpf(sc.nextLine());
                    System.out.println("Nova senha: ");
                    p.setSenha(sc.nextLine());
                    System.out.println("Saldo da conta: ");
                    float saldo;
                    while(true){
                        saldo = sc.nextFloat();
                        if(saldo<0){
                            System.out.println("Saldo não pode ser negativo. Digite novamente:");
                        }else{
                            p.setSaldo(saldo);
                            break;
                        }
                    }
                    p.setTransRealizadas(0);
                    randomAcess.seek(pos+1);
                    int tamanhoReg = randomAcess.readInt();
                    byte[] newReg = p.toBTArray();
                    if(newReg.length<=dadosConta.length){
                        randomAcess.seek(pos+1);
                        randomAcess.writeInt(newReg.length);
                        randomAcess.write(newReg);
                    }else{
                        randomAcess.seek(pos);
                        randomAcess.writeByte(1);
                        long newPos = randomAcess.length();
                        randomAcess.seek(newPos);
                        randomAcess.writeByte(0);
                        randomAcess.writeInt(newReg.length);
                        nav.updateID(id, newPos);
                        randomAcess.write(newReg);
                    }
                }
            }else
                System.out.println("Usuario nao encontrado.");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void deletarConta(){
        try(RandomAccessFile randomAccess = new RandomAccessFile("TrabalhoAEDIII/PesquisaBanco/src/output/arqBanco.txt", "rw")){
            Scanner sc = new Scanner(System.in);
            System.out.println("ID da conta a ser deletada.");
            int id = sc.nextInt();
            Navegation nav = new Navegation();
            long pos = nav.lerID(id);
            if(pos>=0){
                randomAccess.seek(pos);
                byte lapide = randomAccess.readByte();
                byte[] dadosConta = new byte[randomAccess.readInt()];
                Pessoa p = new Pessoa(dadosConta);
                if(lapide!=1){
                    char op;
                    System.out.println("Voce concorda em deletar a conta no nome de "+p.getNome()+" cujo CPF é: "+p.getCpf());
                    while(true) {
                        System.out.println("S - Sim │ N - Não");
                        op = sc.next().charAt(0);
                        if(op=='S') {
                            randomAccess.writeByte(1);
                            break;
                        }else if(op!='S' || op!='N'){
                            System.out.println("Opção invalida, digite novamente.");
                        }
                        else
                            break;
                    }

                }
            }else
                System.out.println("Usuario nao encontrado.");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
