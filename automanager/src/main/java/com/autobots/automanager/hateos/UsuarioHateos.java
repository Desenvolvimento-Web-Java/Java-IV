package com.autobots.automanager.hateos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleUsuario;
import com.autobots.automanager.entidades.Usuario;

@Component
public class UsuarioHateos implements Hateos<Usuario> {

	@Override
	public void adicionarLink(List<Usuario> lista) {
		for (Usuario cliente : lista) {
			long id = cliente.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleUsuario.class)
							.pegarUsuario(id))
					.withSelfRel();
			cliente.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Usuario objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleUsuario.class)
						.pegarTodosUsuarios())
				.withRel("Clientes");
		objeto.add(linkProprio);
		Link atualizarUser = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleUsuario.class)
						.atualizarUsuario(objeto.getId(), objeto))
				.withRel("Atualizar");
		objeto.add(atualizarUser);

	}

}
