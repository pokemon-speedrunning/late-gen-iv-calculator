package be.lycoops.vincent.iv.model;

import be.lycoops.vincent.iv.model.json.PokemonModel;
import be.lycoops.vincent.iv.model.json.PokemonStats;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PokemonModelProvider {

    public static PokemonModel loadPokemonFile(String route) {
        System.out.println("loading pokemon from file");
        if (route != null) {
            String pokemonFile = String.format("be/lycoops/vincent/iv/pokemon/%s.json", route);
            String json = null;

            try {
                Path path = FilePathProvider.getPath(pokemonFile);
                if (path != null) {
                    json = Files.readString(path);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (json == null) {

                if (route.contains("-")) {
                    pokemonFile = String.format("be/lycoops/vincent/iv/pokemon/%s.json", route.split("-")[0]);

                    try {
                        Path path = FilePathProvider.getPath(pokemonFile);
                        if (path != null) {
                            json = Files.readString(path);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (json == null) {
                System.out.println("pokemon could not be loaded");
            } else {
                Gson g = new Gson();

                return g.fromJson(json, PokemonModel.class);
            }
        }
        return new PokemonModel(0,0, "",
                new PokemonStats(0,0,0,0,0,0),
                new PokemonStats(0,0,0,0,0,0),
                new PokemonStats(0,0,0,0,0,0));
    }
}
