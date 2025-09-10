package com.inje.authservice.support.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Header {
    private boolean isSuccessful;
    private int resultCode;
}