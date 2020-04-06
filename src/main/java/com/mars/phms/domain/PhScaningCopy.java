package com.mars.phms.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_scaning_copy")
public class PhScaningCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "名称不能为空")
    private String name;
    @NotBlank(message = "存储位置不能为空")
    private String storePos;

    //导航属性
    //与病史 phmedicalhistory多对一
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH},optional = false)
    private PhMedicalHistory medicalHistory;
}