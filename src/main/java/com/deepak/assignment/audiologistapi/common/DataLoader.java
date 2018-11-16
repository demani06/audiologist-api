package com.deepak.assignment.audiologistapi.common;

import com.deepak.assignment.audiologistapi.domain.Audiologist;
import com.deepak.assignment.audiologistapi.repository.AudiologistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataLoader implements CommandLineRunner {

    private AudiologistRepository audiologistRepository;

    public DataLoader(AudiologistRepository audiologistRepository) {
        this.audiologistRepository = audiologistRepository;
    }

    @Override
    public void run(String... strings) {

        if (audiologistRepository.count() > 0) {
            //Do not want to save duplicate if there are already audiologist
            log.info("Skip the saving of Audiologists data...");
            return;
        }

        log.info("Saving Audiologists data...");

        Audiologist audiologist1 = Audiologist.builder().firstName("Dwayne").lastName("Thomas").build();
        Audiologist audiologist2 = Audiologist.builder().firstName("Hayas").lastName("Henry").build();
        Audiologist audiologist3 = Audiologist.builder().firstName("Robert").lastName("Koch").build();

        audiologistRepository.saveAndFlush(audiologist1);
        audiologistRepository.saveAndFlush(audiologist2);
        audiologistRepository.saveAndFlush(audiologist2);

        log.info("saved Audiologists data...");

    }
}
