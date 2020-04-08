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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
@Table(name = "tb_member")
public class PhMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "姓名不能为空")
    private String name;
    private Integer sex;
    @NotNull(message = "请选择出生日期")
    @DateTimeFormat(pattern = "yyyy/mm/dd")
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
    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<PhMedicalHistory> medicalHistories;
    //与地区 pharea一对一
    @OneToOne
    @JoinColumn(name = "area_id",referencedColumnName = "id")
    private PhArea area;
}