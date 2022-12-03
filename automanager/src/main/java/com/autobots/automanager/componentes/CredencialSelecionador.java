package com.autobots.automanager.componentes;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Credencial;

@Component
public class CredencialSelecionador implements Selecionador<Credencial, Long> {
	@Override
	public Credencial selecionar(List<Credencial> credencial, Long id) {
		Credencial selecionado = null;
		for(Credencial selecionador: credencial) {
			if(selecionador.getId().longValue() == id.longValue()) {
				selecionado = selecionador;
				break;
			}
		}
		return selecionado;
	}
}
