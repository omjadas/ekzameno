package com.ekzameno.ekzameno.models;

/**
 * Metadata for models.
 */
public class Meta {
    public String eTag;

    /**
     * Create an instance of Meta for a model.
     *
     * @param eTag Entity tag for the model
     */
    public Meta(String eTag) {
        this.eTag = eTag;
    }
}
