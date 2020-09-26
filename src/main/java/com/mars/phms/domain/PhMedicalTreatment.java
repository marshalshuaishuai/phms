package com.mars.phms.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author mars
 * @create 2020-04-07 15:04
 */
@Data
@Entity
@Table(name = "tb_medical_treatment")
public class PhMedicalTreatment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull(message = "就诊日期不能为空")
    @DateTimeFormat(pattern = "yyyy/mm/dd")
    private Date medicalDate;
    @NotBlank(message = "就诊医院不能为空")
    private String hospital;
    @NotBlank(message = "主治医生不能为空")
    private String attendingDoctor;
    @NotBlank(message = "病症不能为空")
    private String disease;
    @NotBlank(message = "诊断不能为空")
    private String diagnosis;
    @NotBlank(message = "治疗方案不能为空")
    private String treatment;

    //导航属性
    //与扫描件 phscaningcopy一对多
    @OneToMany(mappedBy = "medicalHistory",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<PhScaningCopy> scaningCopies;
    //与病史 phmedicalhistory多对一
    @ManyToOne(cascade = {CascadeType.REFRESH,CascadeType.MERGE},optional = false)
    private PhMedicalHistory medicalHistory;
}
