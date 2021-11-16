package it.regioneveneto.myp3.gestgraduatorie.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnteDTO {
//    private Long idEnte;
//    private String tenant;
//    private String descrizione;
//    private String abilitato;
//	private String nodeCode;
//    private Long organId;
//    private String stato;
    private Long tenantId;
    private String tenantCode;
    private String description;
}
