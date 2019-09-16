package cn.enigma.project.administrativedivision.dao;

import cn.enigma.project.administrativedivision.entity.AreaInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author luzh
 * Create: 2019-07-01 10:14
 * Modified By:
 * Description:
 */
@Repository
public interface AreaRepository extends CrudRepository<AreaInfo, Long> {

    Optional<AreaInfo> findByCode(String code);

    List<AreaInfo> findByParentCode(String parentCode);

    List<AreaInfo> findByParentId(Long parentId);
}
