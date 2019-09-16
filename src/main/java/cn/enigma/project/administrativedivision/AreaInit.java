package cn.enigma.project.administrativedivision;

import cn.enigma.project.administrativedivision.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author luzh
 * Create: 2019-07-01 10:14
 * Modified By:
 * Description:
 */
@Component
public class AreaInit {

    private final AreaService areaService;

    @Autowired
    public AreaInit(AreaService areaService) {
        this.areaService = areaService;
    }

    @PostConstruct
    public void startAreaInit() {
        areaService.initArea();
    }
}
