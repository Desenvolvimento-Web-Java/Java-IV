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

import com.autobots.automanager.componentes.UsuarioSelecionador;
import com.autobots.automanager.componentes.VeiculoSelecionador;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.servicos.UsuarioServico;
import com.autobots.automanager.servicos.VeiculoServico;

@RestController
@RequestMapping("/veiculo")
public class ControleVeiculo {
	@Autowired
	private UsuarioServico usuServ;
	@Autowired
	private VeiculoServico veiServ;
	@Autowired
	private UsuarioSelecionador usuSelecionador;
	@Autowired
	private VeiculoSelecionador veiSelecionador;
	
	@GetMapping ("/todos")
	public ResponseEntity<?> pegarTodosVeiculos (){
		List<Veiculo> todos = veiServ.pegarTodos();
		HttpStatus status = HttpStatus.ACCEPTED;
		if (todos.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<>(status);
		}else {
			return new ResponseEntity<>(todos,status);
		}
		
	}
	
	@GetMapping ("/todos/{idVeiculo}")
	public ResponseEntity<?> pegarVeiculo (@PathVariable Long idVeiculo){
		Veiculo selecionado = veiSelecionador.selecionar(veiServ.pegarTodos(), idVeiculo);
		HttpStatus status = HttpStatus.I_AM_A_TEAPOT;
		if (selecionado == null) {
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<>(status);
		}else {
			status = HttpStatus.FOUND;
			return new ResponseEntity<>(selecionado, status);
		}
	}
	
	@PostMapping ("/cadastro/{idUsuario}")
	public ResponseEntity<?> cadastroVeiculo(
		@PathVariable Long idUsuario,
		@RequestBody Veiculo body
		){
		List<Usuario> todos = usuServ.pegarTodos();
		Usuario selecionados = usuSelecionador.selecionar(todos, idUsuario);
		if(selecionados != null) {
			selecionados.getVeiculos().add(body);
			body.setProprietario(selecionados);
			veiServ.salvar(body);
			return new ResponseEntity<>("Veiculo cadastrado no usuario: " + selecionados.getNome(), HttpStatus.ACCEPTED);
		}else {
			return new ResponseEntity<>("Usuario não encontrado", HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/atualizar/{idVeiculo}")
	public ResponseEntity<?> atualizarVeiculo(
		@PathVariable Long idVeiculo,
		@RequestBody Veiculo atualizador
	){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Veiculo> veiculos = veiServ.pegarTodos();
		Veiculo veiculo = veiSelecionador.selecionar(veiculos, idVeiculo);
		if (veiculo != null) {
		      atualizador.setId(idVeiculo);
		      veiServ.updateVeiculo(atualizador);
		      status = HttpStatus.OK;
		      return new ResponseEntity<>("Veiculo atualizado com sucesso", status);
		    }
		return new ResponseEntity<>(status);
	}
}
