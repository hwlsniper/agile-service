package io.choerodon.agile.domain.agile.converter;

import io.choerodon.agile.api.dto.IssueCreationNumDTO;
import io.choerodon.agile.infra.dataobject.IssueCreationNumDO;
import io.choerodon.core.convertor.ConvertorI;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * Created by HuangFuqiang@choerodon.io on 2018/7/16.
 * Email: fuqianghuang01@gmail.com
 */
@Component
public class IssueCreationNumConverter implements ConvertorI<Object, IssueCreationNumDO, IssueCreationNumDTO> {

    @Override
    public IssueCreationNumDTO doToDto(IssueCreationNumDO issueCreationNumDO) {
        IssueCreationNumDTO issueCreationNumDTO = new IssueCreationNumDTO();
        BeanUtils.copyProperties(issueCreationNumDO, issueCreationNumDTO);
        return issueCreationNumDTO;
    }
}
