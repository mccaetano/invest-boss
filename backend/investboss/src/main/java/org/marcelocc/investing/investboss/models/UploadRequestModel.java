package org.marcelocc.investing.investboss.models;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadRequestModel {
    private MultipartFile file;
    private String dateFormat;
}
