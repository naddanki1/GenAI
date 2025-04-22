package com.epam.training.gen.ai.controller;

import com.azure.ai.openai.models.EmbeddingItem;
import com.epam.training.gen.ai.model.Search;
import com.epam.training.gen.ai.vector.SimpleVectorActions;
import io.qdrant.client.grpc.Points;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/vector")
public class VectorController {
    @Autowired
    SimpleVectorActions simpleVectorActions;

    @GetMapping("/generate")
    public ResponseEntity<EmbeddingItem> generate(@RequestParam(value = "input") String input) {
        var embdeddings = simpleVectorActions.getEmbeddings(input);
        return ResponseEntity.ok(embdeddings.get(0));
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam(value = "input") String input) throws ExecutionException, InterruptedException {
        simpleVectorActions.processAndSaveText(input);
        return ResponseEntity.ok("Success");
    }

    @GetMapping("/search")
    public ResponseEntity<List<Search>> search(@RequestParam(value = "input") String input) throws ExecutionException, InterruptedException {
        var scorePoints = simpleVectorActions.search(input);
        var results = scorePoints.stream()
                .map(Search::from)
                .toList();
        return ResponseEntity.ok(results);
    }

    @PostMapping("/collection")
    public ResponseEntity<String> createCollection() throws ExecutionException, InterruptedException {
       simpleVectorActions.createCollection();
        return ResponseEntity.ok("Success");
    }
}
