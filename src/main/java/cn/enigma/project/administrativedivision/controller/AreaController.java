package cn.enigma.project.administrativedivision.controller;

import cn.enigma.project.administrativedivision.controller.resp.AreaTreeVO;
import cn.enigma.project.administrativedivision.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luzh
 * Create: 2019-07-02 09:25
 * Modified By:
 * Description:
 */
@RestController
@RequestMapping("area")
public class AreaController {

    private final AreaService areaService;

    @Autowired
    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @GetMapping("download")
    public void download() {
        areaService.download();
    }

    @GetMapping("tree")
    public AreaTreeVO getAreaTree(String code, int depth) {
        return areaService.getAreaTree(code, depth);
    }
}
