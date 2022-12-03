package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.componentes.CredencialSelecionador;
import com.autobots.automanager.componentes.UsuarioSelecionador;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.hateos.UsuarioHateos;
import com.autobots.automanager.servicos.CredencialServico;
import com.autobots.automanager.servicos.UsuarioServico;

@RestController
@RequestMapping ("/usuario")
public class ControleCredencial {
	@Autowired
	private CredencialServico credServ;
	
	@Autowired
	private CredencialSelecionador credSelecionador;
	
	@Autowired
	private UsuarioServico usuServ;
	
	@Autowired
	private UsuarioSelecionador usuSelecionador;
	
	@Autowired
	private UsuarioHateos hateos;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_GERENTE','ROLE_VENDEDOR')")
	@GetMapping ("credencial/todos")
	public ResponseEntity<?> pegarTodosCredenciais (){
		List<Credencial> todos = credServ.pegarTodos();
		HttpStatus status = HttpStatus.ACCEPTED;
		if (todos.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<>(status);
		}else {
			return new ResponseEntity<>(todos,status);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_GERENTE','ROLE_VENDEDOR')")
	@GetMapping("credencial/todos/{idCredencial}")
	public ResponseEntity<?> pegarCredencial (@PathVariable Long idCredencial){
		Credencial todos = credSelecionador.selecionar(credServ.pegarTodos(), idCredencial);
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
	@PutMapping("/credencial/registro-credencial/{idUsuario}")
	public ResponseEntity<?> registroCredencial(@RequestBody CredencialUsuarioSenha registroCred, @PathVariable Long idUsuario){
		List<Usuario> todos = usuServ.pegarTodos();
		Usuario select = usuSelecionador.selecionar(todos, idUsuario);
		List<CredencialUsuarioSenha> credenciais = credServ.credencial();
		BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
		if (select == null) {
			return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
		}else {
			boolean erroBolean = false;
			for (CredencialUsuarioSenha bodyCredencial : credenciais) {
				String senha = codificador.encode(registroCred.getSenha());
				registroCred.setSenha(senha);
				select.getCredenciais().add(registroCred);
				usuServ.salvarUsuario(select);
				return new ResponseEntity<>("Credencial registrado com sucesso", HttpStatus.CREATED);
			}
		}
		return null;
	}
}
