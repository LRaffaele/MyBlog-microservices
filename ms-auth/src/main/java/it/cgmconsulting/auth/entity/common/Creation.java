package it.cgmconsulting.auth.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter
public class Creation implements Serializable {

    @CreationTimestamp
    @Column(updatable = false) // non deve essere aggiornata
    private LocalDateTime createdAt;


}
