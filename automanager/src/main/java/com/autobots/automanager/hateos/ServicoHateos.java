package com.autobots.automanager.hateos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleServico;
import com.autobots.automanager.entidades.Servico;

@Component
public class ServicoHateos implements Hateos<Servico> {

	@Override
	public void adicionarLink(List<Servico> lista) {
		for (Servico entidade: lista) {
			long id = entidade.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleServico.class)
							.pegarServico(id))
					.withSelfRel();
			entidade.add(linkProprio);
		}
		
	}

	@Override
	public void adicionarLink(Servico objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleServico.class)
						.pegarTodosServicos())
				.withRel("Serviços");
		objeto.add(linkProprio);
		Link atualizarUser = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleServico.class)
						.atualizarServico(objeto.getId(), objeto))
				.withRel("Atualizar");
		objeto.add(atualizarUser);

		
	}

}
