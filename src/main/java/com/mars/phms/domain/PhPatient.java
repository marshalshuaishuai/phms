package com.mars.phms.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_patient")
public class PhPatient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "姓名不能为空")
    private String name;
    private char sex;
    private Date birthday;
    private int height;
    private int weight;
    private String idNumber;
    private String healthInsuranceNumber;

    //导航属性
    //与用户 phuser多对一
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH},optional = false)
    private PhUser user;
    //与病史 phmedicalhistory一对多
    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<PhMedicalHistory> medicalHistories;
    //与地区 pharea一对一
    @OneToOne
    @JoinColumn(name = "area_id",referencedColumnName = "id")
    private PhArea area;
}