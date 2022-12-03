package com.autobots.automanager.hateos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleMercadoria;
import com.autobots.automanager.entidades.Mercadoria;

@Component
public class MercadoriaHateos implements Hateos<Mercadoria>{

	@Override
	public void adicionarLink(List<Mercadoria> lista) {
		for(Mercadoria mercadoria : lista) {
			long id = mercadoria.getId();
			Link linkpropio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(ControleMercadoria.class)
							.pegarMercadoria(id))
					.withSelfRel();
			mercadoria.add(linkpropio);
		}
		
	}

	@Override
	public void adicionarLink(Mercadoria objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleMercadoria.class)
						.pegarTodasMercadorias())
				.withRel("Mercadorias");
		objeto.add(linkProprio);
		Link atualizarMerc = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleMercadoria.class)
						.atualizarMercadoria(objeto.getId(), objeto))
				.withRel("Atualizar");
		objeto.add(atualizarMerc);

		
	}

}