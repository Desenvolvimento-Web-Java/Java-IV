package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.repositorios.RepositorioServico;

@Service
public class ServicoServico {

  @Autowired
  private RepositorioServico repositorio;

  public List<Servico> pegarTodos() {
    List<Servico> pegarTodas = repositorio.findAll();
    return pegarTodas;
  }

  public Servico pegarPeloId(Long id) {
    Servico achar = repositorio.getById(id);
    return achar;
  }

  public Servico updateServico(Servico obj) {
    Servico newObj = pegarPeloId(obj.getId());
    updateData(newObj, obj);
    return repositorio.save(newObj);
  }

  private void updateData(Servico newObj, Servico obj) {
    newObj.setDescricao(obj.getDescricao());
    newObj.setNome(obj.getNome());
    newObj.setValor(obj.getValor());
  }

  public void deletar(Long id) {
    repositorio.deleteById(id);
  }
}
