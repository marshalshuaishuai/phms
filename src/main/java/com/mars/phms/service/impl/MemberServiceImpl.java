package com.mars.phms.service.impl;

import com.mars.phms.domain.PhMember;
import com.mars.phms.repository.MemberRepository;
import com.mars.phms.service.MemberService;
import com.mars.phms.vo.MemberRecentDiseaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mars
 * @create 2020-04-07 16:19
 */
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public List<PhMember> findByUserId(long uId) {
        return memberRepository.findByUserId(uId);
    }

    @Override
    public PhMember findById(long id) {
        PhMember member=memberRepository.findById(id).orElse(null);
        return member;
    }

    @Override
    public PhMember saveMember(PhMember member) {
        return memberRepository.save(member);
    }

    @Override
    public List<PhMember> findAll(Example<PhMember> memberExample){
        return memberRepository.findAll(memberExample);
    }

    @Override
    public void deleteMember(long id) {
        memberRepository.deleteById(id);
    }

    @Override
    public Page<PhMember> findAllPaged(Example<PhMember> memberExample, int pageNum, int pageSize) {
        Sort sort= Sort.by(Sort.Direction.DESC,"id");
        Pageable pageable= PageRequest.of(pageNum,pageSize,sort);
        Page<PhMember> members=memberRepository.findAll(memberExample,pageable);
        return members;
    }

    @Override
    public List<MemberRecentDiseaseVo> findRecentDiseaseForMember(String username) {
        return memberRepository.findRecentDiseaseForMember(username);
    }
}
