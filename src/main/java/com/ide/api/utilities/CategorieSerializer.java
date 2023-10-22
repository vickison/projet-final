package com.ide.api.utilities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ide.api.entities.Categorie;

import java.io.IOException;

public class CategorieSerializer extends JsonSerializer<Categorie> {
    @Override
    public void serialize(Categorie categorie, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("categorieID", categorie.getCategorieID());
        jsonGenerator.writeStringField("nom", categorie.getNom());
        // Include additional fields as needed
        // Avoid recursive serialization by not calling other getters here
        jsonGenerator.writeEndObject();
    }
}
