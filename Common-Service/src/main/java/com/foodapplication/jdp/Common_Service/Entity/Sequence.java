package com.foodapplication.jdp.Common_Service.Entity;

/*
 * Author Name : M.V.Krishna
 * Date: 25-03-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Data
@Entity
@Table(name = "SEQUENCES")
@NoArgsConstructor
@AllArgsConstructor
public class Sequence {
    @Id
    @Column(name = "TABLE_NAME", length = 128, nullable = false)
    private String tableName;

    @Column(name = "KB_ID")
    private BigDecimal kbId;

    @Column(name = "SEQ_START_VAL")
    private BigDecimal seqStartVal;

    @Column(name = "SEQ_INCREMENT_VAL")
    private BigDecimal seqIncrementVal;

    @Column(name = "SEQ_CURR_VAL")
    private BigDecimal seqCurrVal;

    @Column(name = "SEQ_NEXT_VAL")
    private BigDecimal seqNextVal;

    @Column(name = "SEQ_MAX_VAL")
    private BigDecimal seqMaxVal;

    @Column(name = "SEQ_CYCLE")
    private BigDecimal seqCycle;

    @Column(name = "CREATED_BY")
    private BigDecimal createdBy;

    @Column(name = "CREATION_DATE")
    private Timestamp creationDate;

    @Column(name = "LAST_UPDATED_BY")
    private BigDecimal lastUpdatedBy;

    @Column(name = "LAST_UPDATE_DATE")
    private Timestamp lastUpdateDate;

    @Column(name = "ATTRIBUTE1", length = 100)
    private String attribute1;

    @Column(name = "ATTRIBUTE2", length = 100)
    private String attribute2;

    @Column(name = "ATTRIBUTE3", length = 100)
    private String attribute3;

    @Column(name = "ATTRIBUTE4", length = 100)
    private String attribute4;

    @Column(name = "ATTRIBUTE5", length = 100)
    private String attribute5;

    @Column(name = "SEQ_DEPENDENT_ID1", length = 50)
    private String seqDependentId1;

    @Column(name = "SEQ_DEPENDENT_ID2", length = 50)
    private String seqDependentId2;

    @Column(name = "SEQ_DEPENDENT_ID3", length = 50)
    private String seqDependentId3;

    @Column(name = "USER_ID")
    private BigDecimal userId;

    @Column(name = "FOOD_SERVICE_ID")
    private BigDecimal foodServiceId;

    @Override
    public Sequence clone() {
        try {
            Sequence clone = (Sequence) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
