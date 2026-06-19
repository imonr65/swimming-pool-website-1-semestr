package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {
    private Long id;
    private String originalFileName;
    private String storageFileName;
    private Long size;
    private String type;
}
