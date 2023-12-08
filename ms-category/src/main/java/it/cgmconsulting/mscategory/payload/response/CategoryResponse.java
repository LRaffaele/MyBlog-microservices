package it.cgmconsulting.mscategory.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CategoryResponse {

    private long postId;
    Set<String> categories;
}
