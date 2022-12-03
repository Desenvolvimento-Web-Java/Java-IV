package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.componentes.EmpresaSelecionador;
import com.autobots.automanager.componentes.VendaSelecionador;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.hateos.VeiculoHateos;
import com.autobots.automanager.servicos.EmpresaServico;
import com.autobots.automanager.servicos.UsuarioServico;
import com.autobots.automanager.servicos.VeiculoServico;
import com.autobots.automanager.servicos.VendaServico;

@RestController
@RequestMapping("/venda")
public class ControleVenda {
	@Autowired 
	private VendaSelecionador vendSelecionador;
	@Autowired
	private VendaServico vendServ;
	@Autowired
	private EmpresaServico empServ;
	@Autowired
	private EmpresaSelecionador empSelecionador;
	@Autowired
	private UsuarioServico usuServ;
	@Autowired
	private VeiculoServico veiServ;
	@Autowired
	private VeiculoHateos hateos;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
	@GetMapping ("/todos")
	public ResponseEntity<?> pegarTodasVendas (){
		List<Venda> todos = vendServ.pegarTodos();
		HttpStatus status = HttpStatus.ACCEPTED;
		if (todos.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<>(status);
		}else {
			return new ResponseEntity<>(todos,status);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
	@GetMapping ("/todos/{idVenda}")
	public ResponseEntity<?> pegarVenda (@PathVariable Long idVenda){
		Venda selecionado = vendSelecionador.selecionar(vendServ.pegarTodos(), idVenda);
		HttpStatus status = HttpStatus.I_AM_A_TEAPOT;
		if (selecionado == null) {
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<>(status);
		}else {
			status = HttpStatus.FOUND;
			return new ResponseEntity<>(selecionado, status);
		}
	}
	
	  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
	  @PutMapping("/atualizar/{idVenda}")
	  public ResponseEntity<?> atualizarVenda(
	    @PathVariable Long idVenda,
	    @RequestBody Venda atualizador
	  ) {
	    HttpStatus status = HttpStatus.BAD_REQUEST;
	    List<Venda> vendas = vendServ.pegarTodos();
	    Venda venda = vendSelecionador.selecionar(vendas, idVenda);
	    if (venda != null) {
	      atualizador.setId(idVenda);
	      vendServ.update(atualizador);
	      status = HttpStatus.OK;
	    }
	    return new ResponseEntity<>(status);
	  }
	  
	  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
	  @PostMapping("/cadastro/{idEmpresa}")
	  public ResponseEntity<?> cadastroVenda(
	    @RequestBody Venda vendas,
	    @PathVariable Long idEmpresa
	  ) {
	    List<Empresa> selecionarEmpresa = empServ.pegarTodas();
	    Empresa selecionada = empSelecionador.selecionar(
	      selecionarEmpresa,
	      idEmpresa
	    );
	    if (selecionada != null) {
	      Usuario clienteSelecionado = usuServ.pegarPeloId(
	        vendas.getCliente().getId()
	      );
	      Usuario funcionarioSelecionado = usuServ.pegarPeloId(
	        vendas.getFuncionario().getId()
	      );
	      Veiculo veiculoSelecionador = veiServ.pegarPeloId(
	        vendas.getVeiculo().getId()
	      );
	      for (Mercadoria bodyMercadoria : vendas.getMercadorias()) {
	        vendas.getMercadorias().clear();
	        Mercadoria novaMercadoria = new Mercadoria();
	        novaMercadoria.setDescricao(bodyMercadoria.getDescricao());
	        novaMercadoria.setCadastro(bodyMercadoria.getCadastro());
	        novaMercadoria.setFabricao(bodyMercadoria.getFabricao());
	        novaMercadoria.setNome(bodyMercadoria.getNome());
	        novaMercadoria.setQuantidade(bodyMercadoria.getQuantidade());
	        novaMercadoria.setValidade(bodyMercadoria.getValidade());
	        novaMercadoria.setValor(bodyMercadoria.getValor());
	        vendas.getMercadorias().add(novaMercadoria);
	      }
	      for (Servico bodyServico : vendas.getServicos()) {
	        Servico novoServico = new Servico();
	        novoServico.setDescricao(bodyServico.getDescricao());
	        novoServico.setNome(bodyServico.getNome());
	        novoServico.setValor(bodyServico.getValor());
	        vendas.getServicos().add(novoServico);
	      }
	      funcionarioSelecionado.getVendas().add(vendas);
	      vendas.setCliente(clienteSelecionado);
	      vendas.setFuncionario(funcionarioSelecionado);
	      vendas.setVeiculo(veiculoSelecionador);
	      selecionada.getVendas().add(vendas);
	      empServ.salvar(selecionada);
	      return new ResponseEntity<>(
	        "Serviço cadastrado na empresa: " + selecionada.getNomeFantasia(),
	        HttpStatus.CREATED
	      );
	    } else {
	      return new ResponseEntity<>(
	        "Empresa não encontrada",
	        HttpStatus.NOT_FOUND
	      );
	    }
	  }
	  
	  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
	  @DeleteMapping("/deletar/{idVenda}")
	  public ResponseEntity<?> deletarVendas(@PathVariable Long idVenda) {
	    List<Empresa> empresas = empServ.pegarTodas();
	    List<Veiculo> veiculos = veiServ.pegarTodos();
	    List<Usuario> usuarios = usuServ.pegarTodos();
	    for (Empresa mercadoriaEmpresa : empresas) {
	      for (Venda empresaMercadoria : mercadoriaEmpresa.getVendas()) {
	        if (empresaMercadoria.getId() == idVenda) {
	        	empServ.deletarVendas(mercadoriaEmpresa.getId(), idVenda);
	        }
	      }
	    }
	    for (Veiculo mercadoriaEmpresa : veiculos) {
	      for (Venda empresaMercadoria : mercadoriaEmpresa.getVendas()) {
	        if (empresaMercadoria.getId() == idVenda) {
	        	empresaMercadoria.setVeiculo(null);
	          veiServ.deletarVendas(mercadoriaEmpresa.getId(), idVenda);
	        }
	      }
	    }
	    for (Usuario mercadoriaEmpresa : usuarios) {
	      for (Venda empresaMercadoria : mercadoriaEmpresa.getVendas()) {
	        if (empresaMercadoria.getId() == idVenda) {
	          usuServ.deletarVendas(mercadoriaEmpresa.getId(), idVenda);
	        }
	      }
	    }
	    vendServ.deletar(idVenda);
	    return null;
	  }

}
