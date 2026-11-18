package br.edu.unifio.nutricase.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.unifio.nutricase.entidades.Atleta;

public interface AtletaRepositorio extends JpaRepository<Atleta, Long> {
}
