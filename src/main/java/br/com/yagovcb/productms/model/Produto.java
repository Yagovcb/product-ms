package br.com.yagovcb.productms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.NotNull;


/**
 *  Classe da entidade {@link Produto}
 *
 *  Criado por Yago Castelo Branco
 *
 * @since 21/06/2021
 * */
@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "descricao")
    @NotNull
    private String description;

    @Column(name = "preco")
    @NotNull
    private double price;
}
