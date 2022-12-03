package com.autobots.automanager.hateos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleVenda;
import com.autobots.automanager.entidades.Venda;

@Component
public class VendaHateos implements Hateos<Venda>{

	@Override
	public void adicionarLink(List<Venda> lista) {
		for (Venda entidade: lista) {
			long id = entidade.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleVenda.class)
							.pegarVenda(id))
					.withSelfRel();
			entidade.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Venda objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleVenda.class)
						.pegarTodasVendas())
				.withRel("Vendas");
		objeto.add(linkProprio);
		Link atualizarVend = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleVenda.class)
						.atualizarVenda(objeto.getId(), objeto))
				.withRel("Atualizar");
		objeto.add(atualizarVend);
		Link deletarVend = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleVenda.class)
						.deletarVendas(objeto.getId()))
				.withRel("Deletar");
		objeto.add(deletarVend);
	}
}