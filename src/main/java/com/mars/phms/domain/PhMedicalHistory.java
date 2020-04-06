package com.mars.phms.domain;


import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_medical_history")
public class PhMedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "就诊日期不能为空")
    private Date medicalDate;
    @NotBlank(message = "就诊医院不能为空")
    private String hospital;
    @NotBlank(message = "主治医生不能为空")
    private String attendingDoctor;
    @NotBlank(message = "病症不能为空")
    private String disease;
    @NotBlank(message = "诊断不能为空")
    private String diagnosis;
    @NotBlank(message = "治疗情况不能为空")
    private String treatment;

    //导航属性
    //与患者 phpatient多对一
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH},optional = false)
    private PhPatient patient;
    //与扫描件 phscaningcopy一对多
    @OneToMany(mappedBy = "medicalHistory",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<PhScaningCopy> scaningCopies;
}