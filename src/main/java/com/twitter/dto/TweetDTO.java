package com.twitter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TweetDTO {

    private Long id;

    private String user;

    private String text;

    private String localization;

    private boolean validation;
}
