package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.repositorios.RepositorioCredencial;
import com.autobots.automanager.repositorios.RepositorioCredencialUsuarioSenha;

@Service
public class CredencialServico {
	@Autowired
	private RepositorioCredencial repoCred;
	
	@Autowired
	private RepositorioCredencialUsuarioSenha repoCredencialUsuarioSenha;
	
	public List<Credencial> pegarTodos() {
		List<Credencial> pegarTodos = repoCred.findAll();
		return pegarTodos;
	}
	
	public Credencial pegarPeloId(Long id) {
		Credencial achar = repoCred.getById(id);
		return achar;
	}
	
	public void salvarCredencial(Credencial credencial) {
		repoCred.save(credencial);
	}
	
	public List<CredencialUsuarioSenha> credencial() {
		return repoCredencialUsuarioSenha.findAll();
	}
}
