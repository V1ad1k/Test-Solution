package com.application;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.ejb.Singleton;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
@Singleton
class PopulationController {

    private final Map<Long, Population> populations = new ConcurrentHashMap<>();
    private final Date activeFrom = new Date();
    private int requests = 0;

    public PopulationController() {
    }


    @PostMapping("/populations")
    public ResponseEntity<?> createPopulation(@RequestBody Population population) {
        requests++;
        if (population.getId() == null) {
            return new ResponseEntity<>("Population ID is not set.", HttpStatus.BAD_REQUEST);
        }
        if (populations.containsKey(population.getId())) {
            return new ResponseEntity<>("Population with ID [" + population.getId() + "] already exists.", HttpStatus.CONFLICT);
        }
        populations.put(population.getId(), population);
        return new ResponseEntity<>(population, HttpStatus.CREATED);
    }


    @GetMapping("/populations/{id}")
    public ResponseEntity<?> findPopulationById(@PathVariable Long id) {
        requests++;
        if (!populations.containsKey(id)) {
            return new ResponseEntity<>("Entity with id [" + id + "] is not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(populations.get(id), HttpStatus.OK);
    }

    /**
     * This is a redirect endpoint
     *
     * @return response entity
     */
    @GetMapping("/populations")
    public ResponseEntity<?> getPopulations2() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/populations"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("/populations")
    public ResponseEntity<?> getPopulations(@RequestParam(value = "name", required = false) String name) {
        requests++;
        if (ObjectUtils.isEmpty(name)) {
            return new ResponseEntity<>(populations.values(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(populations.values().stream().filter(s -> name.equals(s.getName())), HttpStatus.OK);
        }
    }


    @PutMapping("/populations")
    public ResponseEntity<?> replacePopulation(@RequestBody Population newPopulation) {
        requests++;
        long id = newPopulation.getId();
        if (!populations.containsKey(id)) {
            return new ResponseEntity<>("Entity with id [" + id + "] is not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(populations.get(id), HttpStatus.OK);
    }

    @PutMapping("/populations/byname")
    public ResponseEntity<?> replacePopulationByName(@RequestParam("name") String name, @RequestBody Population newPopulation) {
        requests++;
        List<Population> namedPopulations = populations.values().stream().filter(s -> name.equals(s.getName())).collect(Collectors.toList());
        namedPopulations.forEach(population -> populations.put(population.getId(), new Population(population.getId(), newPopulation.getName(), newPopulation.getPopulation())));
        return new ResponseEntity<>(namedPopulations, HttpStatus.OK);
    }


    @DeleteMapping("/populations/{id}")
    public ResponseEntity<?> deletePopulation(@PathVariable Long id) {
        requests++;
        if (!populations.containsKey(id)) {
            return new ResponseEntity<>("Entity with id [" + id + "] is not found.", HttpStatus.NOT_FOUND);
        }
        populations.remove(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/healthcheck")
    public ResponseEntity<?> healthcheck() {
        return new ResponseEntity<>(new Status(requests, activeFrom), HttpStatus.OK);
    }
}