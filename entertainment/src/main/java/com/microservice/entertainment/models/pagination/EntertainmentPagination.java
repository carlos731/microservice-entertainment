package com.microservice.entertainment.models.pagination;

import com.microservice.entertainment.models.response.EntertainmentResponse;
import lombok.*;
import org.springframework.hateoas.PagedModel;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EntertainmentPagination {
    private List<EntertainmentResponse> results;
    private PagedModel.PageMetadata pageMetadata;
//    private Map<String, String> links;
}
