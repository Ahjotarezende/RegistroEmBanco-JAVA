package Objetos;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Pessoa {
    private int idConta;
    private String Nome;
    private String[] email;
    private String nomeUsuario;
    private String senha;
    private String cpf;
    private String cidade;
    private int transRealizadas;
    private float saldo;

    public Pessoa(byte[] dados) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(dados);
        DataInputStream dis = new DataInputStream(bais);
        this.setIdConta(dis.readInt());
        this.setNome(dis.readUTF());
        String[] emails = new String[dis.readInt()];
        for(int i=0; i<emails.length; i++)
            email[i]=dis.readUTF();
        this.setEmail(emails);
        this.setNomeUsuario(dis.readUTF());
        this.setSenha(dis.readUTF());
        this.setCpf(dis.readUTF());
        this.setCidade(dis.readUTF());
        this.setTransRealizadas(dis.readInt());
    }

    public int getIdConta() {
        return idConta;
    }

    public void setIdConta(int idConta) {
        this.idConta = idConta;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String[] getEmail() {
        return email;
    }

    public void setEmail(String[] email) {
        this.email = email;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public int getTransRealizadas() {
        return transRealizadas;
    }

    public void setTransRealizadas(int transRealizadas) {
        this.transRealizadas = transRealizadas;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }
}
