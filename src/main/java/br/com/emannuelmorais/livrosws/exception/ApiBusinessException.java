package br.com.emannuelmorais.livrosws.exception;




public class ApiBusinessException extends RuntimeException {

    private static final long serialVersionUID = -334234234234234L;

    public ApiBusinessException(String message) {
        super(message);
    }

    public ApiBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiBusinessException(Throwable cause) {
        super(cause);
    }
}
