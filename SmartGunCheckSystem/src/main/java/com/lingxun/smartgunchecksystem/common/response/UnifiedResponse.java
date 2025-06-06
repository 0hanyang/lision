package com.lingxun.smartgunchecksystem.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Unified API Response")
public class UnifiedResponse<T> {

    @Schema(description = "Status Code (e.g., 200 for success, 500 for error)", example = "200")
    private int code;

    @Schema(description = "Response Message", example = "Success")
    private String message;

    @Schema(description = "Response Data")
    private T data;

    private UnifiedResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> UnifiedResponse<T> success(T data) {
        return new UnifiedResponse<>(200, "Success", data);
    }

    public static <T> UnifiedResponse<T> success(String message, T data) {
        return new UnifiedResponse<>(200, message, data);
    }

    public static <T> UnifiedResponse<T> success() {
        return new UnifiedResponse<>(200, "Success", null);
    }

    public static <T> UnifiedResponse<T> error(int code, String message) {
        return new UnifiedResponse<>(code, message, null);
    }

    public static <T> UnifiedResponse<T> error(ResponseCode responseCode) {
        return new UnifiedResponse<>(responseCode.getCode(), responseCode.getMessage(), null);
    }

    public static <T> UnifiedResponse<T> error(ResponseCode responseCode, String messageDetails) {
        return new UnifiedResponse<>(responseCode.getCode(), responseCode.getMessage() + ": " + messageDetails, null);
    }
}
