package com.autobots.automanager.hateos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleVeiculo;
import com.autobots.automanager.entidades.Veiculo;

@Component
public class VeiculoHateos implements Hateos<Veiculo>{

	@Override
	public void adicionarLink(List<Veiculo> lista) {
		for (Veiculo entidade: lista) {
			long id = entidade.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleVeiculo.class)
							.pegarVeiculo(id))
					.withSelfRel();
			entidade.add(linkProprio);
		}
		
	}

	@Override
	public void adicionarLink(Veiculo objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleVeiculo.class)
						.pegarTodosVeiculos())
				.withRel("Veiculos");
		objeto.add(linkProprio);
		Link atualizarVeic = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleVeiculo.class)
						.atualizarVeiculo(objeto.getId(), objeto))
				.withRel("Atualizar");
		objeto.add(atualizarVeic);
		Link deletarVeic = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleVeiculo.class)
						.deletarVeiculo(objeto.getId()))
				.withRel("Deletar");
		objeto.add(deletarVeic);
		
	}

}