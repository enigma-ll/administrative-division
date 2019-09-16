package cn.enigma.project.administrativedivision.service.impl;

import cn.enigma.project.administrativedivision.controller.resp.AreaTreeVO;
import cn.enigma.project.administrativedivision.dao.AreaRepository;
import cn.enigma.project.administrativedivision.dto.AreaDTO;
import cn.enigma.project.administrativedivision.entity.AreaInfo;
import cn.enigma.project.administrativedivision.global.Globals;
import cn.enigma.project.administrativedivision.service.AreaService;
import cn.enigma.project.administrativedivision.spider.DownLoadBean;
import cn.enigma.project.administrativedivision.spider.processor.MyPageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author luzh
 * Create: 2019-07-01 10:15
 * Modified By:
 * Description:
 */
@Slf4j
@Service
public class AreaServiceImpl implements AreaService {

    private static final String CHINA = "86";
    private static final String ROOT_ADDRESS = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018";

    private final AreaRepository areaRepository;

    @Autowired
    public AreaServiceImpl(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    @Override
    public void initArea() {
        AreaInfo areaInfo;
        if (areaRepository.count() == 0 || !areaRepository.findByCode(CHINA).isPresent()) {
            areaInfo = new AreaInfo();
            areaInfo.setName(" 中华人民共和国");
            areaInfo.setCode(CHINA);
            areaInfo.setAddress("");
            areaInfo.setFid("");
            areaInfo.setParentId(0L);
            areaInfo.setUrl(ROOT_ADDRESS);
            areaRepository.save(areaInfo);
        }

    }

    @Override
    public void download() {
        Thread thread = new Thread(() -> {
            AreaInfo areaInfo = areaRepository.findByCode(CHINA).orElseThrow(() -> new RuntimeException("系统异常"));
            downloadAndSave(areaInfo.getUrl(), areaInfo.getCode());
        });
        thread.start();
    }

    private void downloadAndSave(String url, String parentCode) {
        List<AreaInfo> areaList = areaRepository.findByParentCode(parentCode);
        if (areaList.isEmpty()) {
            ResultItems items = Spider.create(new MyPageProcessor()).get(url);
            if (null != items) {
                DownLoadBean bean = items.get("bean");
                AreaInfo parentArea = areaRepository.findByCode(bean.getParentCode())
                        .orElseThrow(() -> new RuntimeException("区域码无效"));
                // 先把当前页面的区域保存起来
                for (AreaDTO area : bean.getAreas()) {
                    AreaInfo info = new AreaInfo(area);
                    info.setAddress(Globals.isEmpty(parentArea.getAddress()) ? info.getName() : parentArea.getAddress() + "." + info.getName());
                    info.setParentId(parentArea.getId());
                    info.setFid(Globals.isEmpty(parentArea.getFid()) ? "[" + parentArea.getId() + "]" : parentArea.getFid() + "-" + "[" + parentArea.getId() + "]");
                    areaRepository.save(info);
                }
                // 然后再挨个爬下级区域
                for (AreaDTO area : bean.getAreas()) {
                    if (!Globals.isEmpty(area.getUrl())) {
                        try {
                            Thread.sleep(new Double(ThreadLocalRandom.current().nextDouble(1.0, 3.0) * 1000).intValue());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        downloadAndSave(area.getUrl(), area.getCode());
                    }
                }
            } else {
                try {
                    Thread.sleep(new Double(ThreadLocalRandom.current().nextDouble(5.0, 10.0) * 1000).intValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                downloadAndSave(url, parentCode);
            }
        } else {
            for (AreaInfo areaInfo : areaList) {
                if (!Globals.isEmpty(areaInfo.getUrl())) {
                    downloadAndSave(areaInfo.getUrl(), areaInfo.getCode());
                }
            }
        }
    }

    @Override
    public AreaTreeVO getAreaTree(String code, int level) {
        if (level == 1) {
            return getAreaTreeOneLevel(code);
        }
        if (level > 1) {
            AreaTreeVO root = getAreaTreeOneLevel(code);
            if (null == root) {
                return null;
            }
            List<AreaTreeVO> vos = root.getChildren();
            root.setChildren(new ArrayList<>());
            for (AreaTreeVO vo : vos) {
                vo = getAreaTree(vo.getCode(), level - 1);
                root.addChildren(vo);
            }
            return root;
        }
        return null;
    }

    private AreaTreeVO getAreaTreeOneLevel(String code) {
        AreaInfo rootArea = areaRepository.findByCode(code).orElse(null);
        if (null == rootArea) {
            return null;
        }
        AreaTreeVO root = new AreaTreeVO(rootArea);
        if (Globals.isEmpty(rootArea.getUrl())) {
            return root;
        }
        areaRepository.findByParentId(rootArea.getId()).forEach((info) -> root.addChildren(new AreaTreeVO(info)));
        return root;
    }
}
