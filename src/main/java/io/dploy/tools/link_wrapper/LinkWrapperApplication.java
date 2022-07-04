package io.dploy.tools.link_wrapper;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.dploy.tools.link_wrapper.domain.Category;
import io.dploy.tools.link_wrapper.domain.Importance;
import io.dploy.tools.link_wrapper.domain.Link;
import io.dploy.tools.link_wrapper.repos.CategoryRepository;
import io.dploy.tools.link_wrapper.repos.ImportanceRepository;
import io.dploy.tools.link_wrapper.repos.LinkRepository;


@SpringBootApplication
public class LinkWrapperApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkWrapperApplication.class, args);
    }

    /**
     * Insert testing data
     */
    @Bean
    public CommandLineRunner loadData(LinkRepository linkRepository, CategoryRepository categoryRepository, ImportanceRepository importanceRepository){
        return (args) -> {

            Category category = Category.builder()
                .name("Resource")
                .dateCreated(OffsetDateTime.now())
                .lastUpdated(OffsetDateTime.now())
                .build();
            
            Importance importance = Importance.builder()
                .name("Medium")
                .dateCreated(OffsetDateTime.now())
                .lastUpdated(OffsetDateTime.now())
                .build();

            Importance importance2 = Importance.builder()
                .name("Low")
                .dateCreated(OffsetDateTime.now())
                .lastUpdated(OffsetDateTime.now())
                .build();

            
            
            Link link = Link.builder()
                .url("http://some.url.com/interesting/resource")
                .comment("I want to check it later")
                .status(false)
                .dateCreated(OffsetDateTime.now())
                .lastUpdated(OffsetDateTime.now())
                .build();
            
            Link link2 = Link.builder()
                .url("http://some.url.com/interesting/resource2")
                .comment("I need to study this")
                .status(false)
                .dateCreated(OffsetDateTime.now())
                .lastUpdated(OffsetDateTime.now())
                .build();

            Link link3 = Link.builder()
                .url("http://some.url.com/interesting/resource3")
                .comment("Just to have it at my fingertips")
                .status(true)
                .dateCreated(OffsetDateTime.now())
                .lastUpdated(OffsetDateTime.now())
                .build();

            linkRepository.save(link);
            linkRepository.save(link2);
            linkRepository.save(link3);
            
            Set<Link> links = new HashSet<>(); 
            links.add(link);
            links.add(link2);
            
            Set<Link> links2 = new HashSet<>(); 
            links.add(link3);

            importance.setLinkImportanceLinks(links);
            importanceRepository.save(importance);

            importance2.setLinkImportanceLinks(links2);
            importanceRepository.save(importance2);


            links.addAll(links2);
            category.setLinkCategory(links);
            Set<Category> categories = new HashSet<>(); 
            categories.add(category);
            categoryRepository.save(category);

            link.setLinkCategoryCategorys(categories);
            link.setLinkImportance(importance);
            linkRepository.save(link);

            link2.setLinkCategoryCategorys(categories);
            link2.setLinkImportance(importance);
            linkRepository.save(link2);

            link3.setLinkCategoryCategorys(categories);
            link3.setLinkImportance(importance2);
            linkRepository.save(link3);
            

        };
    }

}
