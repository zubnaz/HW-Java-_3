package org.example.storage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("store")
public class StorageProperties {
    private String location="D:\\SHAG\\Java\\CW\\4\\my-mapster\\public\\uploading";
}
