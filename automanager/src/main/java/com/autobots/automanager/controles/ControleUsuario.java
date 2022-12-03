package com.autobots.automanager.controles;

import java.util.List;
import java.util.Set;

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
import com.autobots.automanager.componentes.UsuarioSelecionador;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.hateos.UsuarioHateos;
import com.autobots.automanager.servicos.EmpresaServico;
import com.autobots.automanager.servicos.UsuarioServico;
import com.autobots.automanager.servicos.VeiculoServico;
import com.autobots.automanager.servicos.VendaServico;

@RestController
@RequestMapping ("/usuario")
public class ControleUsuario {
	@Autowired
	private UsuarioSelecionador usuSelecionador;
	
	@Autowired
	private VendaServico vendServ;
	
	@Autowired
	private VeiculoServico veiServ;
	
	@Autowired
	private UsuarioServico usuServ;
	
	@Autowired
	private EmpresaServico empServ;
	
	@Autowired
	private UsuarioHateos hateos;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_GERENTE','ROLE_VENDEDOR')")
	@GetMapping ("/todos")
	public ResponseEntity<?> pegarTodosUsuarios (){
		List<Usuario> todos = usuServ.pegarTodos();
		HttpStatus status = HttpStatus.ACCEPTED;
		if (todos.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<>(status);
		}else {
			return new ResponseEntity<>(todos,status);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_GERENTE','ROLE_VENDEDOR')")
	@GetMapping("/todos/{idUsuario}")
	public ResponseEntity<?> pegarUsuario (@PathVariable Long idUsuario){
		Usuario todos = usuSelecionador.selecionar(usuServ.pegarTodos(), idUsuario);
		HttpStatus status = HttpStatus.I_AM_A_TEAPOT;
		if (todos == null) {
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<>(status);
		}else {
			status = HttpStatus.FOUND;
			return new ResponseEntity<>(todos, status);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_GERENTE','ROLE_VENDEDOR')")
	@PostMapping ("/cadastro/{idEmpresa}")
	public ResponseEntity<?> cadastroUsuario(@PathVariable Long idEmpresa, @RequestBody Usuario cadastro){
		List<Empresa> empresas = empServ.pegarTodas();
		EmpresaSelecionador empSelecionador = new EmpresaSelecionador();
		Empresa main = empSelecionador.selecionar(empresas, idEmpresa);
		if (main != null) {
			main.getUsuarios().add(cadastro);
			empServ.salvar(main);
			return new ResponseEntity<>("Cadastro de usuário efetuado", HttpStatus.CREATED);
		}else {
	    	return new ResponseEntity<>("Empresa não encontrada", HttpStatus.NOT_FOUND);
	    }
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_GERENTE','ROLE_VENDEDOR')")
	@PutMapping("/atualizar/{idUsuario}")
	public ResponseEntity<?> atualizarUsuario(
		@PathVariable Long idUsuario,
		@RequestBody Usuario atualizador
	){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Usuario> usuarios = usuServ.pegarTodos();
		Usuario usuario = usuSelecionador.selecionar(usuarios, idUsuario);
		if (usuario != null) {
		      atualizador.setId(idUsuario);
		      usuServ.updateUsuario(atualizador);
		      status = HttpStatus.OK;
		      return new ResponseEntity<>("Usuário atualizado com sucesso", status);
		    }
		return new ResponseEntity<>(status);
	}
	
	  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_GERENTE','ROLE_VENDEDOR')")
	  @DeleteMapping("/deletar/{idUsuario}")
	  public ResponseEntity<?> DeletarUser(@PathVariable Long idUsuario){
		  List<Usuario> usuarios = usuServ.pegarTodos();
		  Usuario user = usuSelecionador.selecionar(usuarios, idUsuario);
		  if(user != null) {
			  Set<Documento> documentos = user.getDocumentos();
			  Set<Telefone> telefones = user.getTelefones();
			  Set<Email> emails = user.getEmails();
			  Set<Credencial> credenciais = user.getCredenciais();
			  Set<Mercadoria> mercadorias = user.getMercadorias();
			  Set<Venda> vendas = user.getVendas();
			  for(Venda vendaExistentes : vendServ.pegarTodos()) {
				  if(vendaExistentes.getFuncionario().getId() == idUsuario) {
					  vendaExistentes.setFuncionario(null);
				  }
				  if(vendaExistentes.getCliente().getId() == idUsuario) {
					  vendaExistentes.setCliente(null);
				  }
			  }
			  Set<Veiculo> veiculos = user.getVeiculos();
			  for(Veiculo veiculosExistente : veiServ.pegarTodos()) {
				  if(veiculosExistente.getProprietario().getId().equals(idUsuario)) {
					  veiculosExistente.setProprietario(null);
				  }
			  }
			  user.getDocumentos().removeAll(documentos);
			  user.getTelefones().removeAll(telefones);
			  user.getEmails().removeAll(emails);
			  user.getCredenciais().removeAll(credenciais);
			  user.getMercadorias().removeAll(mercadorias);
			  user.getVeiculos().removeAll(veiculos);
			  user.getVendas().removeAll(vendas);
			  user.setEndereco(null);
			  user.setNivelDeAcesso(null);
			  usuServ.deletar(idUsuario);
			  return new ResponseEntity<>("Deletado com sucesso", HttpStatus.ACCEPTED);
		  }else {
			  return new ResponseEntity<>("Não encontrado", HttpStatus.NOT_FOUND);
		  }
	  }
}