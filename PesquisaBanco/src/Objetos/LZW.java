package Objetos;
import java.util.ArrayList;
import java.io.IOException;

public class LZW {
    private int tam_line;
    private int[] tam_col;
    //Dicionario a ser usado
    private byte[][] dict = new byte[100000][1000];

    LZW(){
        byte b = -128;
        this.tam_line = 0;
        this.tam_col = new int[10000];
        //Inserção dos caracteres separados
        for(int i = 0; i<256; i++){
            this.dict[i][0]= b;
            b = (byte) ((byte) b+1);
            this.tam_col[this.tam_line]++;
            this.tam_line++;
        }
    }

    void addFim(ArrayList<Byte> seq){//Adiciona cada sequencia ao final do dicionario
        LZW lzw  = new LZW();
        for(int i=0; i<seq.size();i++){
            lzw.dict[lzw.tam_line][i] = seq.get(i);
            lzw.tam_col[lzw.tam_line]++;
        }
        lzw.tam_line++;
    }

    ArrayList<Integer> compressaoReg(byte[] dadosOrig) throws IOException {
        //vetor de bytes recebido sera compressado
        LZW lzw  = new LZW(); //Dicionario usado
        int j = 0, indice = -1, p = 0, aux=0, maior_seq=-1;
        ArrayList<Integer> vetCodificado = new ArrayList<Integer>();
        ArrayList<Byte> seq = new ArrayList<Byte>();

        while(p<dadosOrig.length){
            //procura pela maior sequencia no vetor
            for(int i=0;i<lzw.tam_line;i++){
                aux=0;
                for(j=0; j<(dadosOrig.length-p) && j<=lzw.tam_col[i]; j++){
                    if((dadosOrig[p+j])== lzw.dict[i][j]){
                        aux++;
                    }
                }
                if(aux>maior_seq){
                    indice = i;
                    maior_seq = aux;
                }
            }

            //Adiciona a lista a maior sequencia
            for(int i=0; i<lzw.tam_col[indice]; i++)
                seq.add(lzw.dict[indice][i]);
            //Adiciona ao "vetCodificado" a posição da sequencia
            vetCodificado.add(indice);
            p=p+lzw.tam_col[indice];
            //Adiciona no dicionário a maior sequência
            if(p<dadosOrig.length){
                seq.add((dadosOrig[p]));
                lzw.addFim(seq);
                seq.clear();
                maior_seq=-1;
            }
        }
        return vetCodificado;
    }

    ArrayList<Byte> descReg(ArrayList<Integer> vetCod){
        //ira descomprimir o vetor recebido
        LZW lzw  = new LZW();//Dicionario que sera usado

        ArrayList<Byte> seq = new ArrayList<Byte>();
        ArrayList<Byte> vet_decode = new ArrayList<Byte>();

        //Adiciona primeiro caracter
        seq.add(lzw.dict[vetCod.get(0)][0]);
        lzw.addFim(seq);
        vet_decode.add(lzw.dict[vetCod.get(0)][0]);
        seq.clear();

        //Dados que restaram
        for(int i=1; i<vetCod.size();i++){
            lzw.dict[lzw.tam_line-1][lzw.tam_col[lzw.tam_line-1]] = lzw.dict[vetCod.get(i)][0];
            lzw.tam_col[lzw.tam_line-1]++;
            for(int j=0;j<lzw.tam_col[vetCod.get(i)];j++){
                seq.add(lzw.dict[vetCod.get(i)][j]);
                vet_decode.add(lzw.dict[vetCod.get(i)][j]);
            }
            lzw.addFim(seq);
            seq.clear();
        }
        return vet_decode;
    }
}
