package cn.enigma.project.administrativedivision.entity;

import cn.enigma.project.administrativedivision.dto.AreaDTO;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author luzh
 * Create: 2019-07-01 10:08
 * Modified By:
 * Description:
 */
@Data
@Entity
@Table(name = "area_info")
@DynamicUpdate
@DynamicInsert
public class AreaInfo implements Serializable {

    private static final long serialVersionUID = -8401342657612961805L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(255) default '' comment '区域名称'")
    private String name;

    @Column(name = "full_code", length = 32, columnDefinition = "varchar(32) default '' comment '完整区域码'")
    private String fullCode;

    @Column(name = "code", length = 32, columnDefinition = "varchar(32) default '' comment '区域码'")
    private String code;

    @Column(name = "parent_code", length = 32, columnDefinition = "varchar(32) default '' comment '父区域区域码'")
    private String parentCode;

    @Column(name = "parent_id", columnDefinition = "bigint default 0 comment '父区域id'")
    private Long parentId;

    @Column(name = "fid", columnDefinition = "varchar(255) default '' comment '所有父级区域id'")
    private String fid;

    @Column(name = "address", columnDefinition = "varchar(255) default '' comment '完整地址'")
    private String address;

    @Column(name = "url", columnDefinition = "varchar(255) default '' comment '统计局网站url'")
    private String url;

    public AreaInfo() {
    }

    public AreaInfo(AreaDTO bean) {
        this.name = bean.getName();
        this.fullCode = bean.getFullCode();
        this.code = bean.getCode();
        this.parentCode = bean.getParentCode();
        this.url = bean.getUrl();
    }
}
