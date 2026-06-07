package exceptions;

/**
 * Kelas exception khusus untuk menangani error pada perhitungan geometri
 * Demonstrasi Exception Handling
 */
public class GeometryException extends Exception {
    
    // Konstanta untuk tipe error
    public static final int INVALID_INPUT = 1;
    public static final int NEGATIVE_VALUE = 2;
    public static final int ZERO_VALUE = 3;
    public static final int EMPTY_INPUT = 4;
    public static final int INVALID_SHAPE = 5;
    
    private int errorCode;
    private String detailedMessage;
    
    // Constructor overloading - Demonstrasi Overloading
    public GeometryException() {
        super("Terjadi kesalahan pada perhitungan geometri");
        this.errorCode = INVALID_INPUT;
        this.detailedMessage = "Kesalahan tidak diketahui";
    }
    
    public GeometryException(String message) {
        super(message);
        this.errorCode = INVALID_INPUT;
        this.detailedMessage = message;
    }
    
    public GeometryException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.detailedMessage = message;
    }
    
    public GeometryException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = INVALID_INPUT;
        this.detailedMessage = message;
    }
    
    // Getter methods
    public int getErrorCode() {
        return errorCode;
    }
    
    public String getDetailedMessage() {
        return detailedMessage;
    }
    
    // Method untuk mendapatkan pesan error yang user-friendly
    public String getUserFriendlyMessage() {
        switch (errorCode) {
            case NEGATIVE_VALUE:
                return "Error: Nilai tidak boleh negatif! " + detailedMessage;
            case ZERO_VALUE:
                return "Error: Nilai harus lebih dari 0! " + detailedMessage;
            case EMPTY_INPUT:
                return "Error: Input tidak boleh kosong! " + detailedMessage;
            case INVALID_SHAPE:
                return "Error: Bentuk geometri tidak valid! " + detailedMessage;
            default:
                return "Error: " + detailedMessage;
        }
    }
    
    @Override
    public String toString() {
        return "[Kode Error: " + errorCode + "] " + getUserFriendlyMessage();
    }
}