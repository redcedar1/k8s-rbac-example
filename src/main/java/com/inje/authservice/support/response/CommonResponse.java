package com.inje.authservice.support.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "공통 응답 객체")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {

    @Schema(description = "응답 Header")
    private Header header;

    @Schema(description = "응답 데이터")
    private T data;

    public static CommonResponse<Void> ok() { return ok(null); }

    public static <U> CommonResponse<U> ok(U data) {
        Header header = Header.builder()
                .isSuccessful(true)
                .resultCode(0)
                .build();
        return new CommonResponse<>(header, data);
    }

    public static CommonResponse<Void> error(int code) { return error(code, null); }

    public static <U> CommonResponse<U> error(int code, U data) {
        Header header = Header.builder()
                .isSuccessful(false)
                .resultCode(code)
                .build();
        return new CommonResponse<>(header, data);
    }
}