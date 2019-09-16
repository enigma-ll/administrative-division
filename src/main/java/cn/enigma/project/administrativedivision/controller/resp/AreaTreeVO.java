package cn.enigma.project.administrativedivision.controller.resp;

import cn.enigma.project.administrativedivision.entity.AreaInfo;
import cn.enigma.project.administrativedivision.global.Globals;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luzh
 */
@Data
public class AreaTreeVO implements Serializable {
    private static final long serialVersionUID = 3086155212306283903L;
    @ApiModelProperty(value = "区域id")
    private Long id;
    @ApiModelProperty(value = "区域名称")
    private String name;
    @ApiModelProperty(value = "所有父级区域id")
    private String parentIds;
    @ApiModelProperty(value = "所有父级区域名称")
    private String address;
    @ApiModelProperty(value = "父区域id")
    private Long parentId;
    @ApiModelProperty(value = "区域类型1社区0区域")
    private Boolean hasChild;
    @ApiModelProperty(value = "区域码")
    private String code;
    @ApiModelProperty(value = "子区域")
    private List<AreaTreeVO> children;

    public void addChildren(AreaTreeVO vo) {
        this.children.add(vo);
    }

    public AreaTreeVO(AreaInfo areaInfo) {
        this.id = areaInfo.getId();
        this.name = areaInfo.getName();
        this.parentId = areaInfo.getParentId();
        this.parentIds = areaInfo.getFid();
        this.address = areaInfo.getAddress();
        this.children = new ArrayList<>();
        this.hasChild = !Globals.isEmpty(areaInfo.getUrl());
        this.code = areaInfo.getCode();
    }
}
