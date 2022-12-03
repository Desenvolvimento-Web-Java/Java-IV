package com.autobots.automanager.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.repositorios.RepositorioCredencial;

@Service
public class CredencialServico {
	@Autowired
	private RepositorioCredencial repoCred;
	
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
}
