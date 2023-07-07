package com.pyonsnalcolor.push.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushProductStoreRequestDto {

    @Parameter(example = "{ \"pushProductStores\": [\"PB_GS25\", \"EVENT_EMART24\"]}")
    @NotBlank
    private List<String> pushProductStores;
}