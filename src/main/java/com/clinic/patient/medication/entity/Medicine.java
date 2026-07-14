package com.clinic.patient.medication.entity;

import com.clinic.patient.applicationCommonFeature.mapping.db.MedicineDetailsConvert;
import com.clinic.patient.doctor.entity.Doctor;
import com.clinic.patient.medication.repositories.DietInstructionRepository;
import com.clinic.patient.medication.state.Shift;
import com.clinic.patient.user.entity.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL , targetEntity = Doctor.class)
    @JoinColumn(nullable = false,name="doctor_prescribed" )
    private Doctor prescribedBy;

    private LocalDate prescribedOn;

    @Convert(converter = MedicineDetailsConvert.class)
    @Lazy
    private List<MedicineDetails> details;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL , targetEntity = User.class)
    private User patient;

    @ManyToOne(fetch = FetchType.LAZY , targetEntity = DietInstruction.class)
    @Lazy
    private DietInstruction diet;


    @Getter
    @Setter
    @NoArgsConstructor

   public static class MedicineDetails {

        @JsonCreator
        public MedicineDetails(@JsonProperty double dosageMg,@JsonProperty LocalDate expire,
                               @JsonProperty Boolean isActive,@JsonProperty String medicineName,
                               @JsonProperty String notes,@JsonProperty Shift shift,
                               @JsonProperty int tenure) {
            this.dosageMg = dosageMg;
            this.expire = expire;
            this.isActive = isActive;
            this.medicineName = medicineName;
            this.notes = notes;
            this.shift = shift;
            this.tenure = tenure;
        }

        private double dosageMg;
        private Boolean isActive;
        private LocalDate expire;
        private int tenure;
        private String medicineName;
        private String notes;
        private Shift shift;
    }

@Transient
private DietInstructionRepository dietInstructionRepository;
    @PreUpdate
    @PrePersist
    private void setDiet(){
        if(diet!=null){
            dietInstructionRepository.save(diet);
        }
    }
}
