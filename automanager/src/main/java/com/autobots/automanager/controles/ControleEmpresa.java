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
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.hateos.EmpresaHateos;
import com.autobots.automanager.servicos.EmpresaServico;



@RestController
@RequestMapping("/empresa")
public class ControleEmpresa {
	@Autowired
	private EmpresaSelecionador empSelecionador;
	
	@Autowired
	private EmpresaServico empServ;
	
	  @Autowired
	  private EmpresaHateos hateos;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping ("/todos")
	public ResponseEntity<?> pegarTodasEmpresas (){
		List<Empresa> todos = empServ.pegarTodas();
		HttpStatus status = HttpStatus.ACCEPTED;
		if (todos.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<>(status);
		}else {
			return new ResponseEntity<>(todos,status);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping ("/todos/{idEmpresa}")
	public ResponseEntity<?> pegarEmpresa (@PathVariable Long idEmpresa){
		Empresa selecionado = empSelecionador.selecionar(empServ.pegarTodas(), idEmpresa);
		HttpStatus status = HttpStatus.I_AM_A_TEAPOT;
		if (selecionado == null) {
			status = HttpStatus.NOT_FOUND;
			return new ResponseEntity<>(status);
		}else {
			status = HttpStatus.FOUND;
			return new ResponseEntity<>(selecionado, status);
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarEmpresa(@RequestBody Empresa empresa) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (empresa.getId() == null) {
			empServ.salvar(empresa);
			status = HttpStatus.CREATED;
			return new ResponseEntity<>("Cadastro da empresa efetuado", HttpStatus.CREATED);
		}
		return new ResponseEntity<>(status);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PutMapping("/atualizar/{idEmpresa}")
	public ResponseEntity<?> atualizarEmpresa(
		@PathVariable Long idEmpresa,
		@RequestBody Empresa atualizador
	){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Empresa> empresas = empServ.pegarTodas();
		Empresa empresa = empSelecionador.selecionar(empresas, idEmpresa);
		if (empresa != null) {
		      atualizador.setId(idEmpresa);
		      empServ.updateEmpresa(atualizador);
		      status = HttpStatus.OK;
		      return new ResponseEntity<>("Empresa atualizada com sucesso", status);
		    }
		return new ResponseEntity<>(status);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@DeleteMapping("/deletar/{idEmpresa}")
	 public ResponseEntity<?> deletarEmpresa(@PathVariable Long idEmpresa) {
		 List<Empresa> empresas = empServ.pegarTodas();
		 Empresa empresa = empSelecionador.selecionar(empresas, idEmpresa);
		 if(empresa != null) {
			 empServ.deletar(idEmpresa);
			 return new ResponseEntity<>("Deletado com suecsso", HttpStatus.ACCEPTED);
		 }
		 return new ResponseEntity<>("Não encontrada", HttpStatus.NOT_FOUND);
	 }
}
