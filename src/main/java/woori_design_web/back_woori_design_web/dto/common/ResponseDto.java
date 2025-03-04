package woori_design_web.back_woori_design_web.dto.common;

public record ResponseDto<T>(
        Status status,
        String msg,
        T data
) {
    public enum Status {
        SUCCESS, FAILURE
    }
}

