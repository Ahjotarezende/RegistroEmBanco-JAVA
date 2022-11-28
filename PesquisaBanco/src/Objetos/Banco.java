package Objetos;

import java.io.RandomAccessFile;

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

    public int confirmarUsuario(String test){
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
}
