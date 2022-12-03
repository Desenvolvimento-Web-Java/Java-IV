package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.componentes.MercadoriaSelecionador;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.servicos.MercadoriaServico;

@RestController
@RequestMapping("/mercadoria")
public class ControleMercadoria {
	@Autowired
	private MercadoriaServico mercServ;
	@Autowired
	private MercadoriaSelecionador mercSelecionador;
	
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
}
