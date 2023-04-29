package br.ufpe.cin.residencia.banco;

import android.app.Application;
import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.residencia.banco.conta.Conta;
import br.ufpe.cin.residencia.banco.conta.ContaRepository;

//Ver anotações TODO no código
public class BancoViewModel extends AndroidViewModel {
    private ContaRepository repository;
    private LiveData<List<Conta>> contas;
    private MutableLiveData<Conta> _contas = new MutableLiveData<>();
    private MutableLiveData<List<Conta>> _listaContasAtual = new MutableLiveData<>();

    private List<Conta> contasAtuais = new ArrayList<>();
    private LiveData<List<Conta>> listaContasAtual = _listaContasAtual;

    public double saldoTotalBanco() {
        double saldoTotal = 0;
        for (Conta conta : this.contas.getValue()) {
            saldoTotal += conta.saldo;
        }
        return saldoTotal;
    }

    public BancoViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ContaRepository(BancoDB.getDB(application).contaDAO());
        this.contas = repository.getContas();
    }

    void transferir(String numeroContaOrigem, String numeroContaDestino, double valor) {
        //TODO implementar transferência entre contas (lembrar de salvar no BD os objetos Conta modificados)
        new Thread(() ->{
            Conta conta = repository.buscarPeloNumero(numeroContaOrigem);
            conta.debitar(valor);
            repository.atualizar(conta);

            Conta contaCredita = repository.buscarPeloNumero(numeroContaDestino);
            Log.i("TAG", "agora " + conta.nomeCliente);
            contaCredita.creditar(valor);
            repository.atualizar(contaCredita);
        }).start();
    }

    void creditar(String numeroConta, double valor) {
        //TODO implementar creditar em conta (lembrar de salvar no BD o objeto Conta modificado)
        new Thread( () -> {
            Conta contas =  this.repository.buscarPeloNumero(numeroConta);
            contas.creditar(valor);
            this.repository.atualizar(contas);
        } ).start();
    }
    void debitar(String numeroConta, double valor) {
        //TODO implementar debitar em conta (lembrar de salvar no BD o objeto Conta modificado)
        new Thread( () -> {
            Conta conta =  this.repository.buscarPeloNumero(numeroConta);
            conta.debitar(valor);
            this.repository.atualizar(conta);
        } ).start();
    }
    void buscarPeloNome(String nomeCliente) {
        //TODO implementar busca pelo nome do Cliente
        new Thread( () -> {
            List<Conta> contas =  this.repository.buscarPeloNome(nomeCliente);
            _listaContasAtual.postValue(contas);
        } ).start();
    }

    void buscarPeloCPF(String cpfCliente) {
        //TODO implementar busca pelo CPF do Cliente
        new Thread( () -> {
            List<Conta> listaContas = this.repository.buscarPeloCPF(cpfCliente);
            _listaContasAtual.postValue(listaContas);
        } ).start();
    }

    void buscarPeloNumero(String numeroConta) {
        //TODO implementar busca pelo número da Conta
        new Thread( () -> {
            Conta conta = this.repository.buscarPeloNumero(numeroConta);
            setContasAtuais(conta);
            _listaContasAtual.postValue(contasAtuais);
        } ).start();
    }

    public LiveData<List<Conta>> getContas() {
        return contas;
    }

    public void setContas(LiveData<List<Conta>> contas) {
        this.contas = contas;
    }

    public LiveData<List<Conta>> getListaContasAtual() {
        return listaContasAtual;
    }

    public void setListaContasAtual(LiveData<List<Conta>> listaContasAtual) {
        this.listaContasAtual = listaContasAtual;
    }

    public List<Conta> getContasAtuais() {
        return contasAtuais;
    }

    public void setContasAtuais(Conta contasAtual) {
        this.contasAtuais.add(contasAtual);
    }
}
