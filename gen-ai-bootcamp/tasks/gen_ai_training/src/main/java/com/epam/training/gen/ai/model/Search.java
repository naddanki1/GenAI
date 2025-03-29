package com.epam.training.gen.ai.model;

import io.qdrant.client.grpc.Points;

public record Search(String id, float score) {
    public static Search from(Points.ScoredPoint point) {
        return new Search(
                point.getId().getNum() + "", // Convert ID to String
                point.getScore()
        );
    }
}
