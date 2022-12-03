package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.componentes.EmpresaSelecionador;
import com.autobots.automanager.componentes.VendaSelecionador;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
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
	

}
