package cn.enigma.project.administrativedivision.service;

import cn.enigma.project.administrativedivision.controller.resp.AreaTreeVO;

/**
 * @author luzh
 * Create: 2019-07-01 10:15
 * Modified By:
 * Description:
 */
public interface AreaService {
    void initArea();

    void download();

    AreaTreeVO getAreaTree(String code, int level);
}
