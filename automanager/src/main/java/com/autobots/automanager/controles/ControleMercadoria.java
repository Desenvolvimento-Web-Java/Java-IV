package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.componentes.EmpresaSelecionador;
import com.autobots.automanager.componentes.MercadoriaSelecionador;
import com.autobots.automanager.componentes.UsuarioSelecionador;
import com.autobots.automanager.componentes.VendaSelecionador;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.hateos.MercadoriaHateos;
import com.autobots.automanager.servicos.EmpresaServico;
import com.autobots.automanager.servicos.MercadoriaServico;
import com.autobots.automanager.servicos.UsuarioServico;
import com.autobots.automanager.servicos.VendaServico;

@RestController
@RequestMapping("/mercadoria")
public class ControleMercadoria {
	@Autowired
	private MercadoriaServico mercServ;
	
	@Autowired
	private MercadoriaSelecionador mercSelecionador;
	
	@Autowired
	private UsuarioServico usuServ;
	
	@Autowired
	private EmpresaServico empServ;
	
	@Autowired
	private VendaServico vendServ;
	
	@Autowired
	private MercadoriaHateos hateos;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
	@GetMapping ("/todos")
	public ResponseEntity<?> pegarTodasMercadorias (){
		List<Mercadoria> todos = mercServ.pegarTodos();
		HttpStatus status = HttpStatus.ACCEPTED;
		if (todos.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<>(status);
		}else {
			return new ResponseEntity<>(todos,status);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
	@GetMapping ("/todos/{idMercadoria}")
	public ResponseEntity<?> pegarMercadoria (@PathVariable Long idMercadoria){
		Mercadoria selecionado = mercSelecionador.selecionar(mercServ.pegarTodos(), idMercadoria);
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
	@PutMapping("/atualizar/{idMercadoria}")
	public ResponseEntity<?> atualizarMercadoria(
		@PathVariable Long idMercadoria,
		@RequestBody Mercadoria atualizador
	){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Mercadoria> mercadorias = mercServ.pegarTodos();
		Mercadoria mercadoria = mercSelecionador.selecionar(mercadorias, idMercadoria);
		if (mercadoria != null) {
		      atualizador.setId(idMercadoria);
		      mercServ.updateMercadoria(atualizador);
		      status = HttpStatus.OK;
		      return new ResponseEntity<>("Mercadoria atualizado com sucesso", status);
		    }
		return new ResponseEntity<>(status);
	}
	
	  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
	  @DeleteMapping("/deletar/{idMercadoria}")
	  public ResponseEntity<?> deletarMercadoria(@PathVariable Long idMercadoria) {
	    List<Mercadoria> mercadorias = mercServ.pegarTodos();
	    List<Usuario> usuarios = usuServ.pegarTodos();
	    List<Empresa> empresas = empServ.pegarTodas();
	    List<Venda> vendas = vendServ.pegarTodos();
	    Mercadoria selecionado = mercSelecionador.selecionar(
	      mercadorias,
	      idMercadoria
	    );
	    if (selecionado == null) {
	      return new ResponseEntity<>(
	        "Não existe essa mercadoria, digite outro ID",
	        HttpStatus.NOT_FOUND
	      );
	    } else {
	      for (Usuario mercadoriaUsuario : usuarios) {
	        for (Mercadoria userMercadoria : mercadoriaUsuario.getMercadorias()) {
	          if (userMercadoria.getId() == idMercadoria) {
	            usuServ.deletarMercadoria(
	              mercadoriaUsuario.getId(),
	              idMercadoria
	            );
	          }
	        }
	      }
	      for (Empresa mercadoriaEmpresa : empresas) {
	        for (Mercadoria empresaMercadoria : mercadoriaEmpresa.getMercadorias()) {
	          if (empresaMercadoria.getId() == idMercadoria) {
	            empServ.deletarMercadoria(
	              mercadoriaEmpresa.getId(),
	              idMercadoria
	            );
	          }
	        }
	      }
	      for (Venda mercadoriaVenda : vendas) {
	        for (Mercadoria vendaMercadoria : mercadoriaVenda.getMercadorias()) {
	          if (vendaMercadoria.getId() == idMercadoria) {
	            vendServ.deletarMercadoria(
	              mercadoriaVenda.getId(),
	              idMercadoria
	            );
	          }
	        }
	      }
	      mercServ.delete(idMercadoria);
	      return new ResponseEntity<>("Deletado com sucesso", HttpStatus.OK);
	    }
	  }
}
