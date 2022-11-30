package Objetos;

import java.io.RandomAccessFile;

public class Navegation {
    public void criarConta(int id, long pos) {
        try(RandomAccessFile randomAcess = new RandomAccessFile("PesquisaBanco/src/output/tabelaIndex.txt", "rw")){
            int hash = this.defHash(id);
            int posTabela = hash*20;
            randomAcess.seek(posTabela);
            int idLido;
            try{
                idLido = randomAcess.readInt();
            }catch(Exception e){
                idLido = 0;
            }
            if(idLido == 0){
                randomAcess.seek(posTabela);
                randomAcess.writeInt(id);
                randomAcess.writeLong(pos);
                randomAcess.writeLong(-1);
            }else if(idLido != 0){
                randomAcess.seek(posTabela);
                while(true){
                    randomAcess.seek(randomAcess.getFilePointer()+12);
                    long PosicaoID = randomAcess.getFilePointer();
                    long ProximoDado = randomAcess.readLong();
                    if(ProximoDado == -1){
                        randomAcess.seek(PosicaoID);
                        randomAcess.writeLong((long)id*20);
                        randomAcess.seek((long)id*20);
                        randomAcess.writeInt(id);
                        randomAcess.writeLong(pos);
                        randomAcess.writeLong(-1);
                        break;
                    }else
                        randomAcess.seek(ProximoDado);
                }
            }
        }catch(Exception e){e.printStackTrace();}
    }
    public int defHash(int id){
        return id%101;
    }

    public long lerID(int id){
        try(RandomAccessFile randomAcess = new RandomAccessFile("PesquisaBanco/src/output/tabelaIndex.txt", "r")){
            int hash = this.defHash(id);
            int posTabela = hash*20;
            randomAcess.seek(posTabela);
            int idLido;
            try{
                idLido = randomAcess.readInt();
            }catch(Exception e){
                idLido=0;
            }
            if(idLido != 0){
                while(true){
                    if(idLido==id)
                        return randomAcess.readLong();
                    else{
                        randomAcess.seek(randomAcess.getFilePointer()+8);
                        long ProxPos = randomAcess.readLong();
                        if(ProxPos == -1){
                            return -1;
                        }
                        else{
                            randomAcess.seek(ProxPos);
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public void updateID(int id, long change){
        try(RandomAccessFile randomAcess = new RandomAccessFile("PesquisaBanco/src/output/tabelaIndex.txt", "rw")){
            int hash = this.defHash(id);
            int PosTabela = hash*20;
            randomAcess.seek(PosTabela);
            int idLido;
            try{
                idLido = randomAcess.readInt();
            }catch(Exception e){
                idLido = 0;
            }
            if(idLido == 0)
                System.out.println("Este ID não existe!");
            else{
                while(true){
                    if(idLido == id){
                        randomAcess.writeLong(change);
                        break;
                    }
                    else{
                        randomAcess.seek(randomAcess.getFilePointer()+8);
                        long proxPos = randomAcess.readLong();
                        if(proxPos!=-1)
                            randomAcess.seek(proxPos);
                        else
                            System.out.println("Erro!");
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
