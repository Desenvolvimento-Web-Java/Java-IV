package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.componentes.CredencialSelecionador;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.servicos.CredencialServico;

@RestController
@RequestMapping ("/credencial")
public class ControleCredencial {
	@Autowired
	private CredencialServico credServ;
	
	@Autowired
	private CredencialSelecionador credSelecionador;
	
	@GetMapping ("/todos")
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
	
	@GetMapping("/todos/{idCredencial}")
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
}
