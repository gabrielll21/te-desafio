package com.sistema.desafios.config;

import com.sistema.desafios.model.Categoria;
import com.sistema.desafios.model.Subcategoria;
import com.sistema.desafios.repository.CategoriaRepository;
import com.sistema.desafios.repository.SubcategoriaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final CategoriaRepository categoriaRepository;
    private final SubcategoriaRepository subcategoriaRepository;

    public DataInitializer(CategoriaRepository categoriaRepository, SubcategoriaRepository subcategoriaRepository) {
        this.categoriaRepository = categoriaRepository;
        this.subcategoriaRepository = subcategoriaRepository;
    }

    @PostConstruct
    public void init() {
        if (categoriaRepository.count() == 0 && subcategoriaRepository.count() == 0) {
            // Fitness
            Categoria fitness = new Categoria();
            fitness.setNome("Fitness");
            fitness.setDescricao("Desafios relacionados a exercícios físicos e condicionamento.");
            categoriaRepository.save(fitness);

            Subcategoria treinoForca = new Subcategoria();
            treinoForca.setNome("Treinamento de Força");
            treinoForca.setDescricao("Desafios focados em treinamento de força.");
            treinoForca.setCategoria(fitness);
            subcategoriaRepository.save(treinoForca);

            Subcategoria cardio = new Subcategoria();
            cardio.setNome("Cardio");
            cardio.setDescricao("Desafios focados em exercícios cardiovasculares.");
            cardio.setCategoria(fitness);
            subcategoriaRepository.save(cardio);

            Subcategoria flexibilidade = new Subcategoria();
            flexibilidade.setNome("Flexibilidade");
            flexibilidade.setDescricao("Desafios para melhorar a flexibilidade.");
            flexibilidade.setCategoria(fitness);
            subcategoriaRepository.save(flexibilidade);

            Subcategoria endurance = new Subcategoria();
            endurance.setNome("Desafios de Endurance");
            endurance.setDescricao("Desafios para resistência física.");
            endurance.setCategoria(fitness);
            subcategoriaRepository.save(endurance);

            // Saúde
            Categoria saude = new Categoria();
            saude.setNome("Saúde");
            saude.setDescricao("Desafios relacionados à saúde e bem-estar.");
            categoriaRepository.save(saude);

            Subcategoria nutricao = new Subcategoria();
            nutricao.setNome("Nutrição");
            nutricao.setDescricao("Desafios relacionados à alimentação saudável.");
            nutricao.setCategoria(saude);
            subcategoriaRepository.save(nutricao);

            Subcategoria saudeMental = new Subcategoria();
            saudeMental.setNome("Saúde Mental");
            saudeMental.setDescricao("Desafios para o bem-estar mental.");
            saudeMental.setCategoria(saude);
            subcategoriaRepository.save(saudeMental);

            Subcategoria bemEstar = new Subcategoria();
            bemEstar.setNome("Bem-Estar");
            bemEstar.setDescricao("Desafios para melhorar o bem-estar geral.");
            bemEstar.setCategoria(saude);
            subcategoriaRepository.save(bemEstar);

            Subcategoria prevencaoDoencas = new Subcategoria();
            prevencaoDoencas.setNome("Prevenção de Doenças");
            prevencaoDoencas.setDescricao("Desafios para prevenção de doenças.");
            prevencaoDoencas.setCategoria(saude);
            subcategoriaRepository.save(prevencaoDoencas);

            // Desenvolvimento Pessoal
            Categoria desenvolvimentoPessoal = new Categoria();
            desenvolvimentoPessoal.setNome("Desenvolvimento Pessoal");
            desenvolvimentoPessoal.setDescricao("Desafios para crescimento pessoal e habilidades.");
            categoriaRepository.save(desenvolvimentoPessoal);

            Subcategoria comunicacao = new Subcategoria();
            comunicacao.setNome("Habilidades de Comunicação");
            comunicacao.setDescricao("Desafios para melhorar a comunicação.");
            comunicacao.setCategoria(desenvolvimentoPessoal);
            subcategoriaRepository.save(comunicacao);

            Subcategoria gestaoTempo = new Subcategoria();
            gestaoTempo.setNome("Gestão do Tempo");
            gestaoTempo.setDescricao("Desafios para melhor gerenciamento do tempo.");
            gestaoTempo.setCategoria(desenvolvimentoPessoal);
            subcategoriaRepository.save(gestaoTempo);

            Subcategoria autoajuda = new Subcategoria();
            autoajuda.setNome("Autoajuda");
            autoajuda.setDescricao("Desafios para autoaperfeiçoamento.");
            autoajuda.setCategoria(desenvolvimentoPessoal);
            subcategoriaRepository.save(autoajuda);

            Subcategoria mindfulness = new Subcategoria();
            mindfulness.setNome("Mindfulness");
            mindfulness.setDescricao("Desafios para atenção plena e meditação.");
            mindfulness.setCategoria(desenvolvimentoPessoal);
            subcategoriaRepository.save(mindfulness);

            // Aventura
            Categoria aventura = new Categoria();
            aventura.setNome("Aventura");
            aventura.setDescricao("Desafios para os amantes de aventura e atividades ao ar livre.");
            categoriaRepository.save(aventura);

            Subcategoria aoArLivre = new Subcategoria();
            aoArLivre.setNome("Desafios ao Ar Livre");
            aoArLivre.setDescricao("Desafios para atividades externas.");
            aoArLivre.setCategoria(aventura);
            subcategoriaRepository.save(aoArLivre);

            Subcategoria viagens = new Subcategoria();
            viagens.setNome("Viagens");
            viagens.setDescricao("Desafios relacionados a viagens e exploração.");
            viagens.setCategoria(aventura);
            subcategoriaRepository.save(viagens);

            Subcategoria esportesRadicais = new Subcategoria();
            esportesRadicais.setNome("Esportes Radicais");
            esportesRadicais.setDescricao("Desafios de esportes radicais.");
            esportesRadicais.setCategoria(aventura);
            subcategoriaRepository.save(esportesRadicais);

            Subcategoria exploracao = new Subcategoria();
            exploracao.setNome("Exploração");
            exploracao.setDescricao("Desafios de exploração e descoberta.");
            exploracao.setCategoria(aventura);
            subcategoriaRepository.save(exploracao);

            // Criatividade
            Categoria criatividade = new Categoria();
            criatividade.setNome("Criatividade");
            criatividade.setDescricao("Desafios para estimular a criatividade.");
            categoriaRepository.save(criatividade);

            Subcategoria artes = new Subcategoria();
            artes.setNome("Artes e Ofícios");
            artes.setDescricao("Desafios artísticos e manuais.");
            artes.setCategoria(criatividade);
            subcategoriaRepository.save(artes);

            Subcategoria escrita = new Subcategoria();
            escrita.setNome("Escrita Criativa");
            escrita.setDescricao("Desafios de escrita.");
            escrita.setCategoria(criatividade);
            subcategoriaRepository.save(escrita);

            Subcategoria musica = new Subcategoria();
            musica.setNome("Música");
            musica.setDescricao("Desafios musicais.");
            musica.setCategoria(criatividade);
            subcategoriaRepository.save(musica);

            Subcategoria fotografia = new Subcategoria();
            fotografia.setNome("Fotografia");
            fotografia.setDescricao("Desafios fotográficos.");
            fotografia.setCategoria(criatividade);
            subcategoriaRepository.save(fotografia);

            // Tecnologia
            Categoria tecnologia = new Categoria();
            tecnologia.setNome("Tecnologia");
            tecnologia.setDescricao("Desafios relacionados à tecnologia e inovação.");
            categoriaRepository.save(tecnologia);

            Subcategoria programacao = new Subcategoria();
            programacao.setNome("Programação");
            programacao.setDescricao("Desafios de programação.");
            programacao.setCategoria(tecnologia);
            subcategoriaRepository.save(programacao);

            Subcategoria designGrafico = new Subcategoria();
            designGrafico.setNome("Design Gráfico");
            designGrafico.setDescricao("Desafios de design.");
            designGrafico.setCategoria(tecnologia);
            subcategoriaRepository.save(designGrafico);

            Subcategoria desenvolvimentoWeb = new Subcategoria();
            desenvolvimentoWeb.setNome("Desenvolvimento Web");
            desenvolvimentoWeb.setDescricao("Desafios de desenvolvimento web.");
            desenvolvimentoWeb.setCategoria(tecnologia);
            subcategoriaRepository.save(desenvolvimentoWeb);

            Subcategoria inovacao = new Subcategoria();
            inovacao.setNome("Inovação");
            inovacao.setDescricao("Desafios de inovação tecnológica.");
            inovacao.setCategoria(tecnologia);
            subcategoriaRepository.save(inovacao);

            // Social
            Categoria social = new Categoria();
            social.setNome("Social");
            social.setDescricao("Desafios para interação social e comunidade.");
            categoriaRepository.save(social);

            Subcategoria voluntariado = new Subcategoria();
            voluntariado.setNome("Voluntariado");
            voluntariado.setDescricao("Desafios de voluntariado.");
            voluntariado.setCategoria(social);
            subcategoriaRepository.save(voluntariado);

            Subcategoria networking = new Subcategoria();
            networking.setNome("Networking");
            networking.setDescricao("Desafios para ampliar rede de contatos.");
            networking.setCategoria(social);
            subcategoriaRepository.save(networking);

            Subcategoria eventosComunitarios = new Subcategoria();
            eventosComunitarios.setNome("Eventos Comunitários");
            eventosComunitarios.setDescricao("Desafios em eventos da comunidade.");
            eventosComunitarios.setCategoria(social);
            subcategoriaRepository.save(eventosComunitarios);

            Subcategoria desafiosAmizade = new Subcategoria();
            desafiosAmizade.setNome("Desafios de Amizade");
            desafiosAmizade.setDescricao("Desafios para fortalecer amizades.");
            desafiosAmizade.setCategoria(social);
            subcategoriaRepository.save(desafiosAmizade);
        }
    }
}