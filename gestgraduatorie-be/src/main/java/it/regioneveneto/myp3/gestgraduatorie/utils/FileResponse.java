package it.regioneveneto.myp3.gestgraduatorie.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

public class FileResponse implements Serializable {

    private byte[] file;
    private String fileName;
    private String contentType = "application/pdf";

    public FileResponse(byte[] file, String fileName, String contentType) {
        this.file = file;
        this.fileName = fileName;
        this.contentType = contentType;
    }

    public byte[] getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public ResponseEntity<byte[]> toResponseEntity() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-disposition", "attachment; filename=\"" + fileName + "\"");
        headers.set("Content-Type", contentType);

        return ResponseEntity.ok().headers(headers).body(this.file);
    }

}
