package cn.enigma.project.administrativedivision.dto;

import lombok.Data;

/**
 * @author luzh
 * Create: 2019-07-01 10:43
 * Modified By:
 * Description:
 */
@Data
public class AreaDTO {
    private String name;
    private String fullCode;
    private String code;
    private String parentCode;
    private String url;

    public AreaDTO(String name, String fullCode, String code, String parentCode, String url) {
        this.name = name;
        this.fullCode = fullCode;
        this.code = code;
        this.parentCode = parentCode;
        this.url = url;
    }
}
