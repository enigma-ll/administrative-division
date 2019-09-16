package cn.enigma.project.administrativedivision.spider;

import cn.enigma.project.administrativedivision.dto.AreaDTO;
import lombok.Data;

import java.util.List;

@Data
public class DownLoadBean {
    private List<AreaDTO> areas;
    private List<String> urls;
    private String parentCode;

    public DownLoadBean(List<AreaDTO> areas, List<String> urls, String parentCode) {
        super();
        this.areas = areas;
        this.urls = urls;
        this.parentCode = parentCode;
    }
}
