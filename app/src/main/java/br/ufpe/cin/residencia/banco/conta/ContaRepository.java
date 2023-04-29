package br.ufpe.cin.residencia.banco.conta;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import java.util.List;

//Ver anotações TODO no código
public class ContaRepository {
    private ContaDAO dao;
    private LiveData<List<Conta>> contas;

    public ContaRepository(ContaDAO dao) {
        this.dao = dao;
        this.contas = dao.contas();
    }

    public LiveData<List<Conta>> getContas() {
        return contas;
    }

    @WorkerThread
    public void inserir(Conta c) {
        dao.adicionar(c);
    }

    @WorkerThread
    public void atualizar(Conta c) {
        //TODO implementar atualizar
        dao.atualizarConta(c);
    }

    @WorkerThread
    public void remover(Conta c) {
        //TODO implementar remover
        dao.removerConta(c);
    }

    @WorkerThread
    public List<Conta> buscarPeloNome(String nomeCliente) {
        //TODO implementar busca
        dao.buscarContaNome(nomeCliente);
        return dao.buscarContaNome(nomeCliente);
    }

    @WorkerThread
    public List<Conta> buscarPeloCPF(String cpfCliente) {
        //TODO implementar busca
        dao.buscarPorCpf(cpfCliente);
        return dao.buscarPorCpf(cpfCliente);
    }

    @WorkerThread
    public Conta buscarPeloNumero(String numeroConta) {
        Conta conta = dao.buscarPorNumero(numeroConta);
        //TODO implementar busca
        return dao.buscarPorNumero(numeroConta);
    }
}
