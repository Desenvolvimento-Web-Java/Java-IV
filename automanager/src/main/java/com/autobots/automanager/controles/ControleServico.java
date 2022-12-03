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
import com.autobots.automanager.componentes.ServicoSelecionador;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.hateos.ServicoHateos;
import com.autobots.automanager.servicos.EmpresaServico;
import com.autobots.automanager.servicos.ServicoServico;
import com.autobots.automanager.servicos.VendaServico;

@RestController
@RequestMapping("/servico")
public class ControleServico {
	  @Autowired
	  private ServicoServico servServ;

	  @Autowired
	  private ServicoSelecionador servSelecionador;
	  
	  @Autowired
	  private EmpresaServico empServ;
	  
	  @Autowired
	  private EmpresaSelecionador empSelecionador;
	  
	  @Autowired
	  private VendaServico vendServ;
	  
	  @Autowired
	  private ServicoHateos hateos;
	  
	  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
	  @GetMapping ("/todos")
		public ResponseEntity<?> pegarTodosServicos (){
			List<Servico> todos = servServ.pegarTodos();
			HttpStatus status = HttpStatus.ACCEPTED;
			if (todos.isEmpty()) {
				status = HttpStatus.NOT_FOUND;
				return new ResponseEntity<>(status);
			}else {
				return new ResponseEntity<>(todos,status);
			}
		}
	  
	  	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
		@GetMapping ("/todos/{idServico}")
		public ResponseEntity<?> pegarServico (@PathVariable Long idServico){
			Servico selecionado = servSelecionador.selecionar(servServ.pegarTodos(), idServico);
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
		@PostMapping ("/cadastro/{idEmpresa}")
		public ResponseEntity<?> cadastroServico(
			@PathVariable Long idEmpresa, 
			@RequestBody Servico body
		){
			List<Empresa> todos = empServ.pegarTodas();
			Empresa selecionados = empSelecionador.selecionar(todos, idEmpresa);
			if(selecionados != null) {
				selecionados.getServicos().add(body);
				empServ.salvar(selecionados);
				return new ResponseEntity<>("Serviço cadastrado na empresa" + selecionados.getNomeFantasia(), HttpStatus.CREATED);
			}else {
				return new ResponseEntity<>("Empresa não encontrada", HttpStatus.NOT_FOUND);
			}
		}
		
	  	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
		@PutMapping("/atualizar/{idServico}")
		public ResponseEntity<?> atualizarServico(
			@PathVariable Long idServico,
			@RequestBody Servico atualizador
		){
			HttpStatus status = HttpStatus.NOT_FOUND;
			List<Servico> servicos = servServ.pegarTodos();
			Servico servico = servSelecionador.selecionar(servicos, idServico);
			if (servico != null) {
			      atualizador.setId(idServico);
			      servServ.updateServico(atualizador);
			      status = HttpStatus.OK;
			      return new ResponseEntity<>("Servico atualizado com sucesso", status);
			    }
			return new ResponseEntity<>(status);
		}
	  	
	    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
	    @DeleteMapping("/deletar/{idServico}")
	    public ResponseEntity<?> deletarServico(@PathVariable Long idServico) {
	      List<Servico> lista = servServ.pegarTodos();
	      List<Venda> vendas = vendServ.pegarTodos();
	      Servico select = servSelecionador.selecionar(lista, idServico);
	      List<Empresa> empresas = empServ.pegarTodas();
	      if (select != null) {
	        for (Empresa empresasServico : empresas) {
	          for (Servico ServicoNaEmpresas : empresasServico.getServicos()) {
	            if (ServicoNaEmpresas.getId().equals(select.getId())) {
	              empresasServico.getServicos().remove(ServicoNaEmpresas);
	            }
	          }
	        }
	        for (Venda vendasServico : vendas) {
	          for (Servico servicoNaVenda : vendasServico.getServicos()) {
	            if (servicoNaVenda.getId().equals(select.getId())) {
	              vendasServico.getServicos().remove(servicoNaVenda);
	            }
	          }
	        }
	        servServ.deletar(idServico);
	        return new ResponseEntity<>("Servico Deletado", HttpStatus.ACCEPTED);
	      }
	      return new ResponseEntity<>("Servico não encontrado", HttpStatus.NOT_FOUND);
	    }
}
