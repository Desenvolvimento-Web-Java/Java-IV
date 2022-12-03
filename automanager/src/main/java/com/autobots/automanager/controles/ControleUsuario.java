package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.componentes.EmpresaSelecionador;
import com.autobots.automanager.componentes.UsuarioSelecionador;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.servicos.EmpresaServico;
import com.autobots.automanager.servicos.UsuarioServico;

@RestController
@RequestMapping ("/usuario")
public class ControleUsuario {
	@Autowired
	private UsuarioSelecionador usuSelecionador;
	
	@Autowired
	private EmpresaSelecionador empSelecionador;
	
	@Autowired
	private UsuarioServico usuServ;
	
	@Autowired
	private EmpresaServico empServ;

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
}