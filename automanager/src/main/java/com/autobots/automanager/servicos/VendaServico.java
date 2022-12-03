package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.componentes.VendaSelecionador;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.repositorios.RepositorioVenda;

@Service
public class VendaServico {

  @Autowired
  private RepositorioVenda repositorio;
  
  @Autowired
  private VendaSelecionador select;
  
  @Autowired
  private MercadoriaServico servicoMercadoria;
  
  public List<Venda> pegarTodos() {
    List<Venda> pegarTodas = repositorio.findAll();
    return pegarTodas;
  }

  public void salvar(Venda cadastro) {
    repositorio.save(cadastro);
  }

  public Venda pegarPeloId(Long id) {
    Venda achar = repositorio.getById(id);
    return achar;
  }

  public Venda update(Venda obj) {
    Venda newObj = pegarPeloId(obj.getId());
    updateData(newObj, obj);
    return repositorio.save(newObj);
  }

  private void updateData(Venda newObj, Venda obj) {
    newObj.setIdentificacao(obj.getIdentificacao());
  }
  
  public void deletar(Long obj) {
	  repositorio.deleteById(obj);
  }
  
  
	public void deletarMercadoria(Long idEmpresa, Long idMercadoria) {
		List<Venda> todos = pegarTodos();
		Venda selecionado = select.selecionar(todos, idMercadoria);
		Mercadoria mercadoria = servicoMercadoria.pegarPeloId(idMercadoria); 
		if(selecionado.getId() == idEmpresa) {
			selecionado.getMercadorias().remove(mercadoria);
		}
	}

}
