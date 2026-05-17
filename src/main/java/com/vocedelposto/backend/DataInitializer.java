package com.vocedelposto.backend;

import com.vocedelposto.backend.model.Tag;
import com.vocedelposto.backend.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public void run(String... args) {
        if (tagRepository.count() > 2) return; // già inizializzato

        String[][] tags = {
                {"cucina italiana", "cibo"},
                {"cucina etnica", "cibo"},
                {"vegetariano", "cibo"},
                {"pesce", "cibo"},
                {"pizza", "cibo"},
                {"caffè di qualità", "cibo"},
                {"aperitivo", "cibo"},
                {"dolci e pasticceria", "cibo"},
                {"tranquillo", "atmosfera"},
                {"vivace", "atmosfera"},
                {"romantico", "atmosfera"},
                {"informale", "atmosfera"},
                {"elegante", "atmosfera"},
                {"all'aperto", "atmosfera"},
                {"economico", "prezzo"},
                {"rapido", "servizio"},
                {"ottimo servizio", "servizio"},
                {"adatto ai bambini", "servizio"}
        };

        for (String[] tag : tags) {
            if (tagRepository.findAll().stream().noneMatch(t -> t.getName().equals(tag[0]))) {
                Tag t = new Tag();
                t.setName(tag[0]);
                t.setCategory(tag[1]);
                tagRepository.save(t);
            }
        }
    }
}