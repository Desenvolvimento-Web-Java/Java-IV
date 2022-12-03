package com.autobots.automanager.hateos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ControleEmpresa;
import com.autobots.automanager.entidades.Empresa;

@Component
public class EmpresaHateos  implements Hateos<Empresa>{

	@Override
	public void adicionarLink(List<Empresa> lista) {
		for (Empresa entidade: lista) {
			long id = entidade.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ControleEmpresa.class)
							.pegarEmpresa(id))
					.withSelfRel();
			entidade.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Empresa objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleEmpresa.class)
						.pegarTodasEmpresas())
				.withRel("Empresas");
		objeto.add(linkProprio);
		Link atualizarEmp = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleEmpresa.class)
						.atualizarEmpresa(objeto.getId(), objeto))
				.withRel("Atualizar");
		objeto.add(atualizarEmp);
		Link deletarEmp = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ControleEmpresa.class)
						.deletarEmpresa(objeto.getId()))
				.withRel("Deletar");
		objeto.add(deletarEmp);
		
	}

}
