package com.mars.phms.service.impl;

import com.mars.phms.domain.PhMember;
import com.mars.phms.repository.MemberRepository;
import com.mars.phms.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
}
