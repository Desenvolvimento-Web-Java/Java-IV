package com.autobots.automanager.controles;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.componentes.UsuarioSelecionador;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.servicos.UsuarioServico;

@RestController
@RequestMapping("/usuario")
public class ControleDocumento {
	@Autowired
	private UsuarioServico usuServ;
	@Autowired
	private UsuarioSelecionador usuSelecionador;
	
	@GetMapping("/documento/{idUsuario}")
	public ResponseEntity<Set<Documento>> pegarDocumentos(@PathVariable Long idUsuario){
		List<Usuario> pegarTodosUsuarios = usuServ.pegarTodos();
		Usuario selecionado =  usuSelecionador.selecionar(pegarTodosUsuarios, idUsuario);
		if(selecionado != null) {
			Set<Documento> docsUsuarios = selecionado.getDocumentos();
			return new ResponseEntity<>(docsUsuarios,HttpStatus.FOUND);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/documento/{idUsuario}/atualizar/{idDocumento}")
	public ResponseEntity<?> atualizarDocumento(
			@PathVariable Long idUsuario,
			@PathVariable Long idDocumento,
			@RequestBody Documento atualizar){
		List<Usuario> pegarTodosUsuarios = usuServ.pegarTodos();
		Usuario selecionado =  usuSelecionador.selecionar(pegarTodosUsuarios, idUsuario);
		if(selecionado != null) {
			atualizar.setId(idDocumento);
			usuServ.updateDocumento(atualizar);
			return new ResponseEntity<>("Atualizado com sucesso",HttpStatus.FOUND);
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
