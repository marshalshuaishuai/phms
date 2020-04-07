package com.mars.phms.domain;


import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_medical_history")
public class PhMedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "生病日期不能为空")
    private Date illDate;

    @NotBlank(message = "病症不能为空")
    private String disease;


    //导航属性
    //与成员 phmember多对一
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH},optional = false)
    private PhMember member;
    //与地区 pharea一对一
    @OneToOne
    @JoinColumn(name = "area_id",referencedColumnName = "id")
    private PhArea area;
    //与治疗情况 phmedicaltreatment一对多
    @OneToMany(mappedBy = "medicalHistory",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<PhMedicalTreatment> treatments;
}