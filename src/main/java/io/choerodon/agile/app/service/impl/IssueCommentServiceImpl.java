package io.choerodon.agile.app.service.impl;


import io.choerodon.agile.api.dto.IssueCommentCreateDTO;
import io.choerodon.agile.api.dto.IssueCommentDTO;
import io.choerodon.agile.api.dto.IssueCommentUpdateDTO;
import io.choerodon.agile.app.assembler.IssueCommentAssembler;
import io.choerodon.agile.app.service.IssueCommentService;
import io.choerodon.agile.domain.agile.entity.DataLogE;
import io.choerodon.agile.domain.agile.entity.IssueCommentE;
import io.choerodon.agile.domain.agile.repository.DataLogRepository;
import io.choerodon.agile.domain.agile.repository.IssueCommentRepository;
import io.choerodon.agile.infra.dataobject.IssueCommentDO;
import io.choerodon.agile.infra.mapper.IssueCommentMapper;
import io.choerodon.core.convertor.ConvertHelper;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 敏捷开发Issue评论
 *
 * @author dinghuang123@gmail.com
 * @since 2018-05-14 21:59:45
 */
@Service
@Transactional(rollbackFor = CommonException.class)
public class IssueCommentServiceImpl implements IssueCommentService {

    private static final String FILED_COMMENT = "Comment";

    @Autowired
    private IssueCommentRepository issueCommentRepository;
    @Autowired
    private IssueCommentAssembler issueCommentAssembler;
    @Autowired
    private IssueCommentMapper issueCommentMapper;
    @Autowired
    private DataLogRepository dataLogRepository;

    @Override
    public IssueCommentDTO createIssueComment(Long projectId, IssueCommentCreateDTO issueCommentCreateDTO) {
        IssueCommentE issueCommentE = issueCommentAssembler.issueCommentCreateDtoToEntity(issueCommentCreateDTO);
        CustomUserDetails customUserDetails = DetailsHelper.getUserDetails();
        issueCommentE.setUserId(customUserDetails.getUserId());
        issueCommentE.setProjectId(projectId);
        return queryByProjectIdAndCommentId(projectId, issueCommentRepository.create(issueCommentE).getCommentId());
    }

    @Override
    public IssueCommentDTO updateIssueComment(IssueCommentUpdateDTO issueCommentUpdateDTO, List<String> fieldList) {
        if (fieldList != null && !fieldList.isEmpty()) {
            IssueCommentE issueCommentE = issueCommentRepository.update(issueCommentAssembler.
                    issueCommentUpdateDtoToEntity(issueCommentUpdateDTO), fieldList.toArray(new String[fieldList.size()]));
            return queryByProjectIdAndCommentId(issueCommentE.getProjectId(), issueCommentE.getCommentId());
        } else {
            return null;
        }
    }

    @Override
    public List<IssueCommentDTO> queryIssueCommentList(Long projectId, Long issueId) {
        return ConvertHelper.convertList(issueCommentMapper.queryIssueCommentList(projectId, issueId), IssueCommentDTO.class);
    }

    private void dataLogComment(Long projectId, Long commentId) {
        IssueCommentDO issueCommentDO = issueCommentMapper.selectByPrimaryKey(commentId);
        if (issueCommentDO == null) {
            throw new CommonException("error.comment.get");
        }
        DataLogE dataLogE = new DataLogE();
        dataLogE.setProjectId(projectId);
        dataLogE.setIssueId(issueCommentDO.getIssueId());
        dataLogE.setFiled(FILED_COMMENT);
        dataLogE.setOldValue(issueCommentDO.getCommentText());
        dataLogRepository.create(dataLogE);
    }

    @Override
    public int deleteIssueComment(Long projectId, Long commentId) {
        dataLogComment(projectId, commentId);
        return issueCommentRepository.delete(projectId, commentId);
    }

    @Override
    public int deleteByIssueId(Long issueId) {
        IssueCommentDO issueCommentDO = new IssueCommentDO();
        issueCommentDO.setIssueId(issueId);
        return issueCommentMapper.delete(issueCommentDO);
    }

    private IssueCommentDTO queryByProjectIdAndCommentId(Long projectId, Long commentId) {
        IssueCommentDO issueCommentDO = new IssueCommentDO();
        issueCommentDO.setProjectId(projectId);
        issueCommentDO.setCommentId(commentId);
        return ConvertHelper.convert(issueCommentMapper.selectOne(issueCommentDO), IssueCommentDTO.class);
    }
}