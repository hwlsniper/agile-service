package io.choerodon.agile.domain.service.impl;

import io.choerodon.mybatis.service.BaseServiceImpl;
import io.choerodon.agile.domain.service.*;
import io.choerodon.core.exception.CommonException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.agile.infra.dataobject.IssueLinkDO;

/**
 * 敏捷开发Issue链接
 *
 * @author dinghuang123@gmail.com
 * @since 2018-05-14 21:50:34
 */
@Service
@Transactional(rollbackFor = CommonException.class)
public class IIssueLinkServiceImpl extends BaseServiceImpl<IssueLinkDO> implements IIssueLinkService {

}