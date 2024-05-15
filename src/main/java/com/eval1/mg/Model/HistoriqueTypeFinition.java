package com.eval1.mg.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "historique_type_finition")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoriqueTypeFinition
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
            @JoinColumn(name = "idTypeFinition",nullable = false)
    TypeFinition typeFinition;
    double augmentation;
    String designation;

    Date daty;
}
